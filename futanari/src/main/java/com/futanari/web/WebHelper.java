package com.futanari.web;

import com.futanari.util.JSONUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TotooriaHyperion on 2017/5/15.
 */
public class WebHelper {

	private static final String MSG = "msg";
	private static final String IS_SUCCESS = "isSuccess";
	private static final Logger loger = Logger.getLogger(WebHelper.class);

	public static void sendRedirect(HttpServletResponse response, String url) throws IOException {
		response.sendRedirect(response.encodeRedirectURL(url));
	}

	public static String getCurrRequestUrl(HttpServletRequest request) throws UnsupportedEncodingException {
		StringBuffer currRequestUrl = request.getRequestURL();
		Enumeration<String> parameterNames = request.getParameterNames();
		boolean flag = true;
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String value = request.getParameter(paramName);
			if (flag) {
				// currRequestUrl.append("?" + paramName + "=" + value);
				currRequestUrl.append("?" + paramName + "=" + URLEncoder.encode(value, "UTF-8"));
				flag = false;
			} else {
				// currRequestUrl.append("&" + paramName + "=" + value);
				currRequestUrl.append("&" + paramName + "=" + URLEncoder.encode(value, "UTF-8"));
				flag = false;
			}
		}
		return currRequestUrl.toString();
	}

	// 获取IP
	public static String getRequestIp(HttpServletRequest request) {
		// return request.getRemoteAddr();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	// 是否是ajax
	public static boolean isAjaxRequest(HttpServletRequest request) {
		if ((request.getHeader("accept").indexOf("application/json") > -1) || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {
			return true;
		}
		return false;
	}

	// 输出JSON字符串
	public static String outputJson(String json, HttpServletResponse response) {
		return output(json, response, "text/html;charset=UTF-8");
	}

	public static String outputText(String text, HttpServletResponse response) {
		return output(text, response, "text/html;charset=UTF-8");
	}

	public static String outputXml(String xml, HttpServletResponse response) {
		return output(xml, response, "text/xml;charset=UTF-8");
	}

	public static String output(String str, HttpServletResponse response, String contentType) {
//		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType(contentType);
		try {
			PrintWriter out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 输出Excel
	public static void outputExcel(File file, HttpServletResponse rsp, String excelFileName) {
		try {
			rsp.setContentType("application/vnd.ms-excel");
			rsp.setCharacterEncoding("UTF-8");
			rsp.addHeader("Content-disposition", "attachment;filename=\"" + new String((excelFileName + "_" + file.getName()).getBytes("UTF-8"), "ISO8859_1") + "\"");
			ServletOutputStream os = rsp.getOutputStream();
			os.write(FileUtils.readFileToByteArray(file));
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 输出接口状态
	public static String outputMsg(boolean isSuccess, String msg, HttpServletResponse response) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put(IS_SUCCESS, isSuccess);
		retMap.put(MSG, msg);
		return outputMsg(retMap, response);
	}

	public static String outputMsg(Map<String, Object> retMap, HttpServletResponse response) {
		String json = JSONUtils.objectToJson(retMap);
		return outputJson(json, response);
	}

	// jsonp支持
	public static String outputForJsonp(String jsonpCallback, boolean isSuccess, String msg, HttpServletResponse response) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put(IS_SUCCESS, isSuccess);
		retMap.put(MSG, msg);
		return outputForJsonp(jsonpCallback, retMap, response);
	}

	public static String outputForJsonp(String jsonpCallback, Map<String, Object> retMap, HttpServletResponse response) {
		String json = JSONUtils.objectToJson(retMap);
		return outputForJsonp(jsonpCallback, json, response);
	}

	public static String outputForJsonp(String jsonpCallback, String json, HttpServletResponse response) {
		return outputJson(jsonpCallback + "(" + json + ")", response);
	}

	public static Map<String, Object> buildParamMapFromRequest(HttpServletRequest request) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			String value = request.getParameter(name);
			paramMap.put(name, value);
		}
		return paramMap;
	}

	public static void setRequestAttributesFromRequestParam(HttpServletRequest request) {
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			String value = request.getParameter(name);
			request.setAttribute(name, value);
		}
	}

	@SuppressWarnings("rawtypes")
	public static String extractRequestHeader(HttpServletRequest req, String headerName) {
		Enumeration aHeader = req.getHeaders(headerName);
		if (aHeader != null) {
			while (aHeader.hasMoreElements()) {
				Object obj = aHeader.nextElement();
				if (!StringUtils.isEmpty(obj)) {
					return obj.toString();
				}
			}
		}
		return null;
	}

	public static String getRequestUrl(HttpServletRequest req) {
		String rquestUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getRequestURI();
		if (req.getServerPort() == 80) {
			rquestUrl = req.getScheme() + "://" + req.getServerName() + req.getRequestURI();
		}
		return rquestUrl;
	}

	public static String getHttpBody(HttpServletRequest req) {
		try {
			InputStream inputStream = req.getInputStream();
			BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine = null;
			StringBuilder buf = new StringBuilder();
			while ((inputLine = buff.readLine()) != null) {
				buf.append(inputLine);
			}
			buff.close();
			return buf.toString();
		} catch (IOException e) {
			loger.error("getHttpBody", e);
		}
		return null;
	}

}
