package com.futanari.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StringTool {
	public static boolean isInArray(String str, String[] strArray) {
		boolean result = false;
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i].equals("")) {
				result = false;
			} else if (str.indexOf(strArray[i].trim()) != -1) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean isNull(String str) {
		String string = str;
		if (str == null)
			string = "";
		if (string.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotNull(String str) {
		String string = str;
		boolean result = true;
		if (str == null)
			string = "";
		if (!string.equals("")) {
			result = true;
		} else {
			result = false;
		}
		//System.out.println(str+":"+result);
		return result;
	}

	//大写首字母
	public static String up1stLetter(String word) {
		char[] chars = word.toCharArray();
		if (chars[0] >= 'a' && chars[0] < 'z') {
			chars[0] = (char) (chars[0] - 32);
		}
		return new String(chars);
	}

	//小写首字母
	public static String lower1stLetter(String word) {
		char[] chars = word.toCharArray();
		if (chars[0] >= 'A' && chars[0] < 'Z') {
			chars[0] = (char) (chars[0] + 32);
		}
		return new String(chars);
	}

	public static String toString(Object inObj, String dataFormat) {
		String obj = null;
		if (inObj != null) {
			if (inObj.getClass().equals(Date.class) || inObj.getClass().equals(Timestamp.class)) {
				obj = DateTool.getFormatDate(dataFormat, (Date) inObj);
			} else if (inObj.getClass().equals(Timestamp.class)) {
				obj = DateTool.getFormatDate(dataFormat, (Timestamp) inObj);
			} else {
				obj = inObj.toString();
			}
		} else {
			obj = "";
		}
		return obj;
	}

	/**
	 * 将输入的字串左补0到指定位数
	 *
	 * @param str       源字串
	 * @param totalSize 补齐后的位数
	 * @return String
	 */
	public static String addZero(String str, int totalSize) {
		if (str == null)
			str = "";
		int length = str.length();
		if (totalSize == length) {
			return str;
		}
		if (totalSize < length) {
			return str.substring(length - totalSize, length - 1);
		}
		for (int i = 0; i < totalSize - length; i++) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 返回字符串的副本，忽略前导'0'。
	 *
	 * @param str 源字串
	 * @return String
	 */
	public static String trimLeadingZero(String str) {
		if (str == null || str.length() == 0)
			return str;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(0) == '0') {
				str = str.substring(1);
			} else {
				break;
			}
		}
		return str;
	}

	public static String nullToEmpty(Object sourceString) {
		if (sourceString == null) {
			return "";
		}
		return sourceString.toString();
	}

	public static Object StringToObj(String s, Class clazz) {
		Object obj = null;
		if (s.equals("NULL")) {
			obj = null;
		} else if (clazz.equals(Integer.class)) {
			obj = Integer.parseInt(s);
		} else if (clazz.equals(int.class)) {
			obj = Integer.parseInt(s);
		} else if (clazz.equals(Long.class)) {
			obj = Long.parseLong(s);
		} else if (clazz.equals(long.class)) {
			obj = Long.parseLong(s);
		} else if (clazz.equals(Double.class)) {
			obj = Double.parseDouble(s);
		} else if (clazz.equals(double.class)) {
			obj = Double.parseDouble(s);
		} else if (clazz.equals(Date.class)) {
			try {
				obj = DateTool.getDateTime(s, "yyyy-MM-dd HH:mm:ss:SSS");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (clazz.equals(String.class)) {
			obj = s;
		} else {
			new Exception("Unsupported type").printStackTrace();
			return null;
		}
		return obj;
	}

	public static String ObjToString(Object obj) {
		String str = null;
		if (obj == null) {
			str = "";
		} else {
			Class clazz = obj.getClass();
			if (clazz.equals(Integer.class)) {
				str = obj.toString();
			} else if (clazz.equals(int.class)) {
				str = obj + "";
			} else if (clazz.equals(Long.class)) {
				str = obj.toString();
			} else if (clazz.equals(long.class)) {
				str = obj + "";
			} else if (clazz.equals(Double.class)) {
				str = obj.toString();
			} else if (clazz.equals(double.class)) {
				str = obj + "";
			} else if (clazz.equals(Date.class)) {
				str = DateTool.getFormatDate("yyyy-MM-dd HH:mm:ss:SSS", (Date) obj);

			} else if (clazz.equals(String.class)) {
				str = obj + "";

			} else if (clazz.getSimpleName().endsWith("Exception")) {
				str = null;
			} else {
				//不支持类型
				str = "";
			}
		}
		return str;
	}

	/**
	 * 按大写字母分割字符串
	 *
	 * @return List<String>
	 */
	public static List<String> splitByUpLetter(String str) {
		List<String> list = new ArrayList<>();
		char[] c = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < c.length; i++) {
			if (Character.isUpperCase(c[i])) {
				list.add(sb.toString());
				sb = new StringBuffer();
			}
			sb.append(c[i]);
			if (i == c.length - 1) {
				list.add(sb.toString());
			}
		}
		return list;
	}

	public static void main(String[] args) {
		String str = "insureApplyDao";
		List l = StringTool.splitByUpLetter(str);
		//System.out.println(l);
	}
}
