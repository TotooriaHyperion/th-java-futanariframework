package com.futanari.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public final class RandomIDUtil {
	/**
	 * 生成唯一编号
	 * 
	 * @return
	 */
	public static String getNewUUID() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	public static long getRandom(long seed) {
		long random = (long) (Math.random() * seed);
		return random;
	}

	public static long getRandom(long min, long max) {
		return Math.round(Math.random() * (max - min) + min);
	}

	public static String getNumber(int length) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < length; i++) {
			str.append(getRandom(0, 9));
		}
		return str.toString();
	}

	public static String simpleGetShortUUID() {
		String a = "";
		try {
			a = getShortUUID(UUID.randomUUID().toString().replace("-", ""),16,null,22);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	// 根据32位uuid生成22位唯一随机数
	private static String getShortUUID(String uuid,int orgRadix,String[] destChars,int retLength) throws Exception {
		String[] theChars = destChars == null ? new String[] { "a", "b", "c", "d", "e", "f",
				"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" } : destChars;
		int radix = theChars.length;
		StringBuilder shortBuffer = new StringBuilder();
		char[] us = uuid.toCharArray();
		int[] ui = new int[us.length];
		for(int i=0;i<us.length;i++) {
			ui[i] = Integer.parseInt(String.valueOf(us[i]),16);
		}
		while((ui = nextInts(ui)) != null) {
			int cur = 0;
			for(int i=0;i<ui.length;i++) {
				cur = ui[i];
				ui[i]/=radix;
				if(i+1<ui.length) {
					ui[i+1] += cur % radix * orgRadix;
				}
			}
			shortBuffer.append(theChars[cur % radix]);
		}
		if(shortBuffer.length() > retLength) {
			throw new Exception("数据无法压缩到这个长度");
		}
		while (shortBuffer.length() < retLength) {
			shortBuffer.append("0");
		}
		return shortBuffer.reverse().toString();
	}

	private static int[] nextInts(int[] c) {
		for(int i=0;i<c.length;i++) {
			if(c[i] != 0) {
				int[] next = new int[c.length - i];
				System.arraycopy(c,i,next,0,c.length - i);
				return next;
			}
		}
		return null;
	}

	public static void main(String[] a) {
		// System.out.println(System.nanoTime());
		// System.out.println(System.currentTimeMillis());
		// StringBuilder uuid = new StringBuilder(getNewUUID());
		// int count = 0;
		// for (int i = 0; i < uuid.length(); i++) {
		// if (i % 2 == 0) {
		// }
		// count = +uuid.charAt(i);
		//
		// }
		// for (int i = 0; i < 10; i++)
		// System.out.println(getRandom(1));
		// // System.out.println(getRandom(1));

		// System.out.println(getNumber(6));

//		Set<String> set = new HashSet<String>();
//		for (int i = 0; i < 9999; i++) {
//			String id = String.valueOf(getTransactionNoShort());
//			set.add(id);
//			System.out.println(id);
//		}
//		System.out.println(set.size());

		System.out.println(UUID.randomUUID());

		for(int i=0;i<10;i++) {
			String currentUUID = UUID.randomUUID().toString().replace("-", "");
			System.out.println(currentUUID);
			try {
				System.out.println(getShortUUID(currentUUID,16,null,22));
				System.out.println(simpleGetShortUUID());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
