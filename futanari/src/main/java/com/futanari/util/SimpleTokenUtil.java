package com.futanari.util;

import com.futanari.constants.Global;

import java.util.Map;
import java.util.TreeMap;

public class SimpleTokenUtil {

	// md5摘要验证数据完整性
	public static boolean checkToken(String inputToken, Object obj, String functionCode, String secreyKey) {
		if (inputToken != null && !"".equals(inputToken) && obj != null && secreyKey != null && !"".equals(secreyKey)) {
			String rightToken = buildToken(obj, functionCode, secreyKey);
			if (inputToken.equals(rightToken)) {
				return true;
			}
		}
		return false;
	}

	public static String buildToken(Object obj, String functionCode, String secreyKey) {
		String paramStr = buildParamStr(obj);
		return Md5Util.encodeString(paramStr + functionCode + "&" + secreyKey);
	}

	@SuppressWarnings("unchecked")
	private static String buildParamStr(Object obj) {
		StringBuilder str = new StringBuilder();
		Map<String, Object> oMap;
		if (obj instanceof Map) {
			oMap = (Map) obj;
		} else {
			oMap = MapUtil.objectToMap(obj);
		}
		if (oMap != null && oMap.size() > 0) {
			Map<String, Object> paramMap = new TreeMap<>();
			paramMap.putAll(oMap);
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (!Global.ACCESS_TOKEN.equalsIgnoreCase(key)) {
					if (value != null && !"".equals(value.toString().trim()) && !"null".equals(value.toString())) {
						str.append(key);
						str.append("=");
						str.append(value.toString());
						str.append("&");
					}
				}
			}
		}
		return str.toString();
	}
}
