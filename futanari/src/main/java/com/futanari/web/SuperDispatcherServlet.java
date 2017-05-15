package com.futanari.web;

import com.futanari.util.RandomIDUtil;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TotooriaHyperion on 2017/5/15.
 */

// 记录接口日志
public class SuperDispatcherServlet extends DispatcherServlet {
	private static final Logger log = Logger.getLogger(SuperDispatcherServlet.class);
	private static final long serialVersionUID = -6763713779463635319L;
	private static final String LOG_ID = "logId";
	private static final Map<Long, String> logMap = new HashMap<>();
	private static int localPort = 0;

	public SuperDispatcherServlet() {
		super();
	}

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = System.currentTimeMillis();
		String logId = RandomIDUtil.getNewUUID();
		long threadId = Thread.currentThread().getId();
		if (localPort == 0) {
			localPort = request.getLocalPort();
		}
		String referer = request.getHeader("referer");
		String X_Hash_ip = request.getHeader("X-Hash-ip");

		logMap.put(threadId, logId);
		request.setAttribute(LOG_ID, logId);
		String attribute = getAttributeLog(request);
		StringBuffer buf = new StringBuffer("{");
		buf.append("logId:").append(logId).append(" ,");
		buf.append("referer:").append(referer).append(" ,");
		buf.append("method:").append(request.getMethod()).append(" ,");

		buf.append("requestURI:").append(getRequestUri(request)).append(" ,");
		buf.append("requestIP:").append(WebHelper.getRequestIp(request)).append(",");
		buf.append("X-Hash-ip:").append(X_Hash_ip).append(",");
		buf.append("requestAttribute:").append(attribute).append("}");

		log.info(buf.toString());
		super.doService(request, response);
		long end = System.currentTimeMillis() - start;

		buf = new StringBuffer("{");
		buf.append("logId:").append(logId).append(" ,");
		buf.append("totalTime:").append(end).append(" }");
		logMap.remove(threadId);
		log.info(buf.toString());

	}

	private static String getAttributeLog(HttpServletRequest request) {
		Map<String, String> attribute = new HashMap<>();
		Enumeration<String> attrNames = request.getParameterNames();
		while (attrNames.hasMoreElements()) {
			String attrName = attrNames.nextElement();
			attribute.put(attrName, request.getParameter(attrName));
		}
		return attribute.toString();
	}

	private static String getRequestUri(HttpServletRequest request) {
		String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
		if (uri == null) {
			uri = request.getRequestURI();
		}
		return uri;
	}

	public static String getLogIdByThreadId(long threadId) {
		return logMap.get(threadId);
	}

	public static Integer getlocalPort() {
		return localPort;
	}

}
