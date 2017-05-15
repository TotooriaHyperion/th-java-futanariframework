package com.futanari.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.dom4j.Document;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONUtils {
	public static ArrayList<JSONObject> parseXmlJSONArray(Object obj) {
		ArrayList<JSONObject> infos = new ArrayList<JSONObject>();

		if (obj instanceof JSONArray) {
			JSONArray infs = (JSONArray) obj;
			infos = (ArrayList<JSONObject>) JSONObject.parseArray(infs.toJSONString(), JSONObject.class);
		} else if (obj instanceof JSONObject) {
			infos.add((JSONObject) obj);
		} else {
			return null;
		}
		return infos;
	}

	public static JSONObject dom4jXmltoJSON(Document doc) throws JSONException {
		return JSONObject.parseObject(org.json.XML.toJSONObject(doc.asXML()).toString());
	}

	public static <T> T jsonToObject(String json, Type type) {
		T obj = null;
		try {
			obj = new Gson().fromJson(json, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static String objectToJson(Object obj) {
		return objectToJson(obj, null);
	}

	public static String objectToJson(Object obj, Type type) {
		return objectToJson(obj, type, null);
	}

	public static String objectToJson(Object obj, Type type, String dateFormat) {
		String json = null;
		try {
			GsonBuilder gb = new GsonBuilder().serializeNulls();
			if (dateFormat != null && !"".equals(dateFormat)) {
				gb.setDateFormat(dateFormat);
			}
			if (type != null) {
				json = gb.create().toJson(obj, type);
			} else {
				json = gb.create().toJson(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

}
