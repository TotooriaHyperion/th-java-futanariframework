package com.futanari.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.json.JSONException;

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
}
