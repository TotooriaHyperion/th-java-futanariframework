package com.futanari.util;

import com.futanari.dto.IpData;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * map的一些工具类
 * 
 * @author jack
 */
public class MapUtil {
	public static void main(String args[]) throws Exception {
		// IpInfo info =new IpInfo();
		// MapUtil.objectToMap(circleInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("country", "上海");
		map.put("country_id", "021");
		map.put("ip", "021--fafafaf:afaf");
		IpData info = (IpData) MapUtil.mapToObject(map, IpData.class);
		System.out.print(MapUtil.objectToMap(info));
		System.out.print(new Double(3.4010D).toString());
	}

	/**
	 * 返回由对象的属性为key,值为map的value的Map集合
	 * 
	 * @param object Object
	 * @return mapValue Map<String,String>
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object object) {
		return objectToMap(object, false);
	}

	public static Map<String, Object> objectToMap(Object object, boolean ignoreNullValue) {
		Map<String, Object> mapValue = new LinkedHashMap<String, Object>();
		try {
			Class<?> cls = object.getClass();
			Field[] superField = cls.getSuperclass().getDeclaredFields();
			Field[] fields = cls.getDeclaredFields();
			List<Field[]> fieldList = new ArrayList<Field[]>();
			fieldList.add(superField);
			fieldList.add(fields);
			for (Field[] fieldArray : fieldList) {
				for (Field field : fieldArray) {
					String name = field.getName();
					String temp = name.toUpperCase();
					if (!"serialVersionUID".equals(name) && !name.equals(temp)) {
						String strGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
						Method method = cls.getMethod(strGet);
						if (method != null) {
							Object obj = method.invoke(object);
							if (ignoreNullValue && obj == null) {
								continue;
							}
							mapValue.put(name, obj);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapValue;
	}

	/**
	 * 将map的值拷贝到对象，对象为普通bean map key 对应bean的属性，
	 * 
	 * @param map
	 * @param classz
	 * @return
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> classz) {
		Object object = null;
		try {
			object = Class.forName(classz.getName()).newInstance();
			Class<?> cls = object.getClass();
			Field[] superField = cls.getSuperclass().getDeclaredFields();
			Field[] fields = cls.getDeclaredFields();
			List<Field[]> fieldList = new ArrayList<Field[]>();
			fieldList.add(superField);
			fieldList.add(fields);
			for (Field[] fieldArray : fieldList) {
				for (Field field : fieldArray) {
					String name = field.getName();
					Class<?> type = field.getType();
					String temp = name.toUpperCase();
					if (!"serialVersionUID".equals(name) && !name.equals(temp)) {
						Object value = map.get(name);
						if (value!=null && StringTool.isNotNull(value.toString())) {
							String valType = value.getClass().getName();
							if (type.getName().equals("java.lang.String")) {
								if ("java.util.Date".equals(valType)) {
									value = DateTimeUtil.formatDateTime((Date) value, DateTimeUtil.DATETIME_PATTERN);
								} else {
									value = String.valueOf(value.toString());
								}
							} else if (type.getName().equals("java.lang.Byte")) {
								value = Byte.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Integer")) {
								value = Integer.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Float")) {
								value = Float.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Double")) {
								value = Double.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Long")) {
								value = Long.valueOf(value.toString());
							} else if (type.getName().equals("java.util.Date")) {
								if (value.toString().contains(":")) {
									value = DateTimeUtil.parseDateTime(value.toString());
								} else {
									value = DateTimeUtil.parseDate(value.toString());
								}
							} else if (type.getName().equals("java.lang.Boolean")) {
								value = Boolean.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Short")) {
								value = Short.valueOf(value.toString());
							}
							else if(type.getName().equals("java.math.BigDecimal")){
								value = BigDecimal.valueOf(Double.valueOf(value.toString()));
							}
							else {
								continue;
							}
							String strGet = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
							Method method = cls.getMethod(strGet, type);
							if (method != null) {
								method.invoke(object, new Object[] { value });
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	public static Object mapToObject(Map<String, Object> map, Object object) {
		try {
			Class<?> cls = object.getClass();
			Field[] superField = cls.getSuperclass().getDeclaredFields();
			Field[] fields = cls.getDeclaredFields();
			List<Field[]> fieldList = new ArrayList<Field[]>();
			fieldList.add(superField);
			fieldList.add(fields);
			for (Field[] fieldArray : fieldList) {
				for (Field field : fieldArray) {
					String name = field.getName();
					Class<?> type = field.getType();
					String temp = name.toUpperCase();
					if (!"serialVersionUID".equals(name) && !name.equals(temp)) {
						Object value = map.get(name);
						if (value != null) {
							String valType = value.getClass().getName();
							if (type.getName().equals("java.lang.String")) {
								if ("java.util.Date".equals(valType)) {
									value = DateTimeUtil.formatDateTime((Date) value, DateTimeUtil.DATETIME_PATTERN);
								} else {
									value = String.valueOf(value.toString());
								}
							} else if (type.getName().equals("java.lang.Byte")) {
								value = Byte.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Integer")) {
								value = Integer.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Float")) {
								value = Float.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Double")) {
								value = Double.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Long")) {
								value = Long.valueOf(value.toString());
							} else if (type.getName().equals("java.util.Date")&&!("java.util.Date".equals(valType))) {
								if (value.toString().contains(":")) {
									value = DateTimeUtil.parseDateTime(value.toString());
								} else {
									value = DateTimeUtil.parseDate(value.toString());
								}
							} else if (type.getName().equals("java.lang.Boolean")) {
								value = Boolean.valueOf(value.toString());
							} else if (type.getName().equals("java.lang.Short")) {
								value = Short.valueOf(value.toString());
							} else {
								continue;
							}
							String strGet = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
							Method method = cls.getMethod(strGet, type);
							if (method != null) {
								method.invoke(object, new Object[] { value });
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	public static Object copyToObject(Object from, Object dest) {
		Map<String, Object> fromMap = objectToMap(from);
		return mapToObject(fromMap, dest);
	}
}
