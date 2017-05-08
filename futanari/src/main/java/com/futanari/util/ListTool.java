package com.futanari.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ListTool {

	/**
	 * @param orderKey orderByWhat
	 * @return self
	 * @see List<Map>进行排序
	 */
	public static List<Map> orderMapList(List<Map> maplist, String orderKey) {
		MapComparator Comparator = new MapComparator();
		Comparator.setKey(orderKey);
		maplist.sort(Comparator);
		return maplist;

	}

	/**
	 * @param orderKey orderByWhat
	 * @return self
	 * @see List<Map>进行倒序排序
	 */
	public static List<Map> orderMapListDesc(List<Map> maplist, String orderKey) {
		MapComparator Comparator = new MapComparator();
		Comparator.setKey(orderKey);
		maplist.sort(Comparator);
		Collections.reverse(maplist);
		return maplist;

	}

	/**
	 * @param orderKey orderByWhat
	 * @return self
	 * @see List<Object>进行排序,要求Object为pojo对象
	 */
	public static List<Object> orderObjList(List<Object> list, String orderKey) {
		ObjComparator Comparator = new ObjComparator();
		Comparator.setKey(orderKey);
		list.sort(Comparator);
		return list;

	}

	/**
	 * @param orderKey orderByWhat
	 * @return self
	 * @see List<Object>进行倒序排序,要求Object为pojo对象
	 */
	public static List<Object> orderObjListDesc(List<Object> list, String orderKey) {
		ObjComparator Comparator = new ObjComparator();
		Comparator.setKey(orderKey);
		list.sort(Comparator);
		Collections.reverse(list);
		return list;
	}

	public static List arrayToList(Object[] array) {
		List<Object> list = new ArrayList<>();
		if (array != null)
			Collections.addAll(list, array);
		return list;
	}

	public static Object[] listToArray(List list) {
		return list.toArray();
	}

	public static boolean isInList(Object obj, List list) {
		boolean result = false;
		for (Object object : list) {
			if (obj.equals(object)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean isInList(Object obj, Object[] list) {
		boolean result = false;
		for (Object object : list) {
			if (obj.equals(object)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 将list 顺序打乱
	 *
	 * @param list source
	 */
	public static void randomList(List list) {
		Collections.shuffle(list);
	}

	/**
	 * 判断列表是否为null或长度为0
	 *
	 * @param list source
	 * @return boolean
	 */
	public static boolean isEmpty(List list) {
		boolean result = false;
		if (list == null) {
			result = true;
		} else if (list.isEmpty()) {
			result = true;
		}
		return result;
	}

	public static void clear(List list) {
		if (list != null) {
			list.clear();
		}
	}

	/**
	 * 判断list不为空
	 *
	 * @param list source
	 * @return boolean
	 */
	public static boolean isNotNull(List list) {
		boolean flag = false;
		if (list != null && list.size() > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断list为空
	 *
	 * @param list source
	 * @return boolean
	 */
	public static boolean isNull(List list) {
		boolean flag = false;
		if (list == null || list.size() == 0) {
			flag = true;
		}
		return flag;
	}
}
