package com.futanari.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReflectTool {

	public static Object invoke(Object obj, String methodName, Class[] classes, Object[] args) {
		Object returnObj = null;
		try {
			Class c = obj.getClass();
			Method method = ReflectTool.getClassMethod(methodName, c, classes);
			returnObj = method.invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	public static Object invoke(Object obj, String methodName, Class classes, Object args) {
		Object returnObj = null;
		try {
			Class c = obj.getClass();
			Method method = ReflectTool.getClassMethod(methodName, c, new Class[]{classes});
			returnObj = method.invoke(obj, new Object[]{args});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	public static Object invoke(Object obj, String methodName) {
		Object returnObj = null;
		try {
			Class c = obj.getClass();
			Method method = ReflectTool.getClassMethod(methodName, c, null);
			returnObj = method.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	/**
	 * 递归获取类以及超类的方法
	 *
	 * @param methodName methodName
	 * @param clazz      clazz
	 * @return
	 */
	public static Method getClassMethod(String methodName, Class clazz, Class[] classes) {
		Method method = null;
		if (!clazz.equals(Object.class)) {
			try {
				method = clazz.getDeclaredMethod(methodName, classes);

			} catch (Exception e) {
				method = getClassMethod(methodName, clazz.getSuperclass(), classes);
			}
		}
		return method;


	}


	public static void setAttribute(Object obj, String attName, Object attValue) {
		Field field = null;
		try {
			//String dateType[]={"DATE","DATETIME","DAY"};
			field = getAttribute(obj, attName);
			//obj.getClass().getDeclaredField(attName);
			field.setAccessible(true);//强制访问private变量
			if (attValue == null) {
				if (field.getType().equals(int.class) || field.getType().equals(double.class) || field.getType().equals(long.class)) {
					field.set(obj, 0);
				} else {
					field.set(obj, attValue);
				}

			}
			
			/*
			else if(StringTool.isInArray(attName.toUpperCase(), dateType)){
				Date date=DateTool.convertToDate(attValue);
				if(field.getType().equals(Date.class)){
					field.set(obj, date);
				}
				else{
					field.set(obj, attValue);
				}
				
			}
			*/
			else if (attValue.getClass().equals(Date.class)) {
				if (field.getType().equals(Date.class)) {
					field.set(obj, attValue);
				} else if (field.getType().equals(Timestamp.class)) {
					field.set(obj, new Date(((Timestamp) attValue).getTime()));
				}
			} else if (attValue.getClass().equals(Timestamp.class)) {
				if (field.getType().equals(Date.class)) {
					field.set(obj, new Date(((Timestamp) attValue).getTime()));
				} else {
					field.set(obj, attValue);
				}

			}
			//对于BigDecimal特殊处理
			else if (attValue.getClass().equals(BigDecimal.class)) {
				if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
					field.set(obj, ((BigDecimal) attValue).intValue());
				} else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
					field.set(obj, ((BigDecimal) attValue).longValue());
				} else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
					field.set(obj, ((BigDecimal) attValue).doubleValue());
				} else if (field.getType().equals(String.class)) {
					field.set(obj, ((BigDecimal) attValue).toString());
				}
			}
			//对于BigDecimal特殊处理
			else if (attValue.getClass().equals(Long.class)) {
				if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
					field.set(obj, ((Long) attValue).intValue());
				} else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
					field.set(obj, ((Long) attValue).longValue());
				} else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
					field.set(obj, ((Long) attValue).doubleValue());
				} else if (field.getType().equals(String.class)) {
					field.set(obj, ((Long) attValue).toString());
				}
			}
			//对于BigDecimal特殊处理
			else if (attValue.getClass().equals(Double.class)) {
				if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
					field.set(obj, ((Double) attValue).intValue());
				} else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
					field.set(obj, ((Double) attValue).longValue());
				} else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
					field.set(obj, ((Double) attValue).doubleValue());
				} else if (field.getType().equals(String.class)) {
					field.set(obj, ((Double) attValue).toString());
				}
			}
			//对于BigDecimal特殊处理
			else if (attValue.getClass().equals(Integer.class)) {
				if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
					field.set(obj, ((Integer) attValue).intValue());
				} else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
					field.set(obj, ((Integer) attValue).longValue());
				} else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
					field.set(obj, ((Integer) attValue).doubleValue());
				} else if (field.getType().equals(String.class)) {
					field.set(obj, ((Integer) attValue).toString());
				}
			} else {
				field.setAccessible(true);//强制访问private变量
				field.set(obj, attValue);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Field getAttribute(Object obj, String attName) {
		Field field = null;
		try {
			field = obj.getClass().getDeclaredField(attName);
		} catch (Exception e) {
			try {
				field = obj.getClass().getSuperclass().getDeclaredField(attName);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return field;

	}

	public static Object getAttributeValue(Object obj, String attName) {
		Field field = null;
		Object value = null;
		try {
			field = obj.getClass().getDeclaredField(attName);
			field.setAccessible(true);//强制访问private变量
			value = field.get(obj);
		} catch (Exception e) {
			try {
				field = obj.getClass().getSuperclass().getDeclaredField(attName);
				field.setAccessible(true);//强制访问private变量
				value = field.get(obj);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return value;

	}


	/**
	 * 获取类的所有属性(含其直接类型)
	 *
	 * @param clazz clazz
	 */
	public static Field[] getAllField(Class clazz) {
		List<Field> allFields = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();


		Field[] superFields = clazz.getSuperclass().getDeclaredFields();

		//先添加子类属性
		allFields.addAll(ListTool.arrayToList(fields));

		//循环父类中的属性
		boolean isRepeat = false;
		for (Field f : superFields) {
			isRepeat = false;
			//循环子类中的属性
			for (Field sf : fields) {
				if (sf.getName().equals(f.getName())) {
					isRepeat = true;
					break;
				}

			}
			//不重复属性
			if (!isRepeat) {
				allFields.add(f);
			}

		}

		Field[] fieldArray = allFields.toArray(new Field[allFields.size()]);
		return fieldArray;

	}


	public static void main(String[] args) {
		List obj = new ArrayList();
		ReflectTool.invoke(obj, "add", new Class[]{Object.class}, new Object[]{"mac1"});
		Object b = ReflectTool.invoke(obj, "add", new Class[]{Object.class}, new Object[]{"mac2"});
	}
}
