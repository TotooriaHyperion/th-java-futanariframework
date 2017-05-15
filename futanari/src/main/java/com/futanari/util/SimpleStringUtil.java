package com.futanari.util;

/**
 * Created by TotooriaHyperion on 2017/5/15.
 */
public class SimpleStringUtil {
	public static String replaceHtml(String text) {
		text = text.replaceAll("\\<", "&lt;");
		text = text.replaceAll("\\>", "&gt;");
		return text;
	}

	public static String replaceHtmlBr(String text) {
		text = text.replaceAll("\n", "<br/>");
		return text;
	}
}
