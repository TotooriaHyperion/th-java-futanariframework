package com.futanari;

/**
 * Created by user on 2017/5/3.
 */
public class TestPackageType {
	public static void main(String[] args) {
		Boolean B = null;
		if (B) {
			System.out.println(B);
		}
		testType(B);
	}

	private static void testType(boolean b) {
		System.out.println(b);
	}
}
