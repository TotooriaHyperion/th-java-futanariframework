package com.futanari.learn;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


public class TestServlet extends HttpServlet {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");

	private final static Logger logger = Logger.getLogger(TestServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;chatset=utf-8");
		String ret = "My first servlet program: " + sdf.format(new Date());
		System.out.println(ret);
		resp.getWriter().write(ret);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		String reqMethod = req.getMethod();
		System.out.println(reqMethod);
		String reqURI = req.getRequestURI();
		System.out.println(reqURI);
		StringBuffer reqURL = req.getRequestURL();
		System.out.println(reqURL);
		String Protocol = req.getProtocol();
		System.out.println(Protocol);
		Enumeration headers = req.getHeaderNames();
		HashMap<String, Object> headerMap = new HashMap<>();
		while (headers.hasMoreElements()) {
			String aName = ISO8859toUTF8((String) headers.nextElement());
			headerMap.put(aName, req.getHeader(aName));
		}
		System.out.println(JSONObject.toJSONString(headerMap));

		String queryString = req.getQueryString();
		System.out.println(queryString);

		Enumeration parameterNames = req.getParameterNames();
		HashMap<String, Object> parameterMap = new HashMap<>();
		while (parameterNames.hasMoreElements()) {
			String aName = ISO8859toUTF8((String) parameterNames.nextElement());
			parameterMap.put(aName, ISO8859toUTF8(req.getParameter(aName)));
		}
		System.out.println(parameterMap);

		Map<String, String[]> customMap = req.getParameterMap();
		HashMap<String, String> customMapUTF8 = new HashMap<>();

		for (Map.Entry<String, String[]> aParam : customMap.entrySet()) {
			String key = aParam.getKey();
			String[] value = aParam.getValue();
			String utfKey = ISO8859toUTF8(key);
			String[] utfValue = new String[value.length];
			for (int i = 0; i < value.length; i++) {
				utfValue[i] = ISO8859toUTF8(value[i]);
			}
			customMapUTF8.put(utfKey, String.join(",", utfValue));
		}

		System.out.println(customMap);
		System.out.println(customMapUTF8);

		InputStream in = req.getInputStream();

		byte[] b = new byte[1024];
		int k = 0;
		StringBuilder sber = new StringBuilder();

		while ((k = in.read(b)) != -1) {
			sber.append(new String(b, 0, k, "UTF-8"));
		}

		Object jsonObj = JSONObject.parse(sber.toString());

		System.out.println(jsonObj);

		HashMap<String,Object> reqParam = new HashMap<>();

		if (jsonObj instanceof JSONObject) {
			JSONObject obj = (JSONObject) jsonObj;
			for(Map.Entry<String,Object> entry : obj.entrySet()) {
				reqParam.put(entry.getKey(),entry.getValue());
			}
		}

		System.out.println(reqParam);

		this.doGet(req, resp);
	}

	private static String ISO8859toUTF8(String isoStr) {
		if (isoStr == null) {
			return null;
		}
		try {
			return new String(isoStr.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
