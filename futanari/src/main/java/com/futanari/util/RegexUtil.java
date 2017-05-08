package com.futanari.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	private static boolean matcher(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(orginal);
		return m.find();
	}

	public static boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
	}

	public static boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*", orginal);
	}

	public static boolean isWholeNumber(String orginal) {
		return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
	}

	public static boolean isPositiveDecimal(String orginal) {
		return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
	}

	public static boolean isNegativeDecimal(String orginal) {
		return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
	}

	public static boolean isDecimal(String orginal) {
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}

	public static boolean isRealNumber(String orginal) {
		return isWholeNumber(orginal) || isDecimal(orginal);
	}

	public static boolean isEmail(String email) {
		return isMatch("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", email);
	}

	public static boolean isChinese(String email) {
		return isMatch("^[\u4e00-\u9fa5]{0,}$", email);
	}

	public static boolean isDate(String date) {
		return isMatch(
				"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$",
				date);
	}

	public static boolean isMobile(String mobiles) {
		// return isMatch("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$",
		// mobiles);
		return isMatch("^(1)\\d{10}$", mobiles);
	}

	public static boolean checkString(String regex, String str) {
		return isMatch(regex, str);
	}

	public static boolean checkIncluded(String regex, String str) {
		return matcher(regex, str);
	}

	public static void main(String[] a) {
		System.out
				.println(RegexUtil
						.checkIncluded(
								"(common\\.advList|common\\.getRecommend|common\\.getLoadingCover|topic\\.info|topic\\.package|topic\\.subList|topic\\.children|topic\\.authors|category\\.detail|category\\.leafs|category\\.packages|rank\\.packageList|package\\.detail)",
								"/apicenter/package.detail/ee4ea507701c0c5fac041942e6814f4d"));

		String regex = "(common\\.advList|common\\.getLoadingCover|author\\.info|author\\.package|topic\\.info|topic\\.package|topic\\.subList|topic\\.children|topic\\.authors|category\\.detail|category\\.leafs|category\\.packages|rank\\.packageList|package\\.detail)";

		String url = "http://ios-api.ccplay.com.cn/apicenter/author.info,author.package/da1d749e1e5ad920d52f21438f706b73";

		// url =
		// "http://ios-api.ccplay.com.cn/apicenter/topic.info,topic.package/cbdc47d39a35645508bd900f938486e9";
		System.out.println(RegexUtil.checkIncluded(regex, url));
	}
}
