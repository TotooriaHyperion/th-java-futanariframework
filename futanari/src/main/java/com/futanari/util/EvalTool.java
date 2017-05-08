package com.futanari.util;

import java.util.List;

/**
 * 表达式执行工具
 * 使用自定类EL表达式 $[x.y.z]
 */

public class EvalTool {

	public static String evalString(Object fromObj, String el) {
		try {
			String innerEl;//${el}中的el
			String temp = "";
			RegexTool reg = new RegexTool();
			reg.setCodestr(el);
			reg.setRegex("[\\$][\\[][[a-z]|[A-Z]|[0-9]|[\u4E00-\u9FA5]|[\\.]|[_]]*[\\]]");// $[数字|字母|汉字|.|_]
			List<String> elList = reg.findMatchingList();
			for (String elExpress : elList) {
				innerEl = elExpress.replaceAll("\\$", "");
				innerEl = innerEl.replaceAll("\\[", "");
				innerEl = innerEl.replaceAll("\\]", "");
				temp = EvalTool.evalObject(fromObj, innerEl) == null ? "" : EvalTool.evalObject(fromObj, innerEl).toString();
				el = el.replace(elExpress, temp);
			}
		} catch (Exception e) {
			//LogTool.inf(EvalTool.class,"parseException:"+el);
			el = "";
		}
		return el;
	}

	//可以获取Map中的对象
	public static Object evalObject(Object fromObj, String el) {
		el = el.replaceAll("\\$", "");
		el = el.replaceAll("\\[", "");
		el = el.replaceAll("\\]", "");
		String[] objs = el.split("\\.");
		Object resultObj = null;
		try {
			String methodName;
			resultObj = fromObj;
			for (String obj : objs) {
				if (resultObj != null) {
					if (!obj.equals("item")) {
						methodName = "get" + StringTool.up1stLetter(obj);
						//从get方法中取值
						if (ReflectTool.getClassMethod(methodName, resultObj.getClass(), null) != null) {
							methodName = "get" + StringTool.up1stLetter(obj);
							resultObj = ReflectTool.invoke(resultObj, methodName);
						}
						//从map中取值
						else {
							resultObj = ReflectTool.invoke(resultObj, "get", Object.class, obj);
						}
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = null;
		}
		return resultObj;
	}


	//可以获取Map中的对象
	public static <T> T eval(Object fromObj, String el) {
		el = el.replaceAll("\\$", "");
		el = el.replaceAll("\\[", "");
		el = el.replaceAll("\\]", "");
		String[] objs = el.split("\\.");
		Object resultObj = null;
		try {
			String methodName;
			resultObj = fromObj;
			for (String obj : objs) {
				if (resultObj != null) {
					if (!obj.equals("item")) {
						methodName = "get" + StringTool.up1stLetter(obj);
						//从get方法中取值
						if (ReflectTool.getClassMethod(methodName, resultObj.getClass(), null) != null) {
							methodName = "get" + StringTool.up1stLetter(obj);
							resultObj = ReflectTool.invoke(resultObj, methodName);
						}
						//从map中取值
						else {
							resultObj = ReflectTool.invoke(resultObj, "get", Object.class, obj);
						}
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = null;
		}
		if (resultObj != null)
			return (T) resultObj;
		else
			return (T) "";
	}
}
