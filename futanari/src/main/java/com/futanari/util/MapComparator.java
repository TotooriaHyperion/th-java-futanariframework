package com.futanari.util;

import java.util.Comparator;
import java.util.Map;

public class MapComparator implements Comparator {

	private String key;

	public int compare(Object map1, Object map2) {
		Map v1 = (Map) map1;
		Map v2 = (Map) map2;
		String key1 = this.getKey();
		return new Double(v1.get(key1).toString()).compareTo(new Double(v2.get(key1).toString()));
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}

