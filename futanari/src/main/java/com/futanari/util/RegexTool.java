package com.futanari.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool {
	private String codestr;
	private String regex;

	public String getCodestr() {
		return codestr;
	}

	public void setCodestr(String codestr) {
		this.codestr = codestr;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public boolean isMatching() {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(codestr);
		boolean isnum = true;
		isnum = m.matches();
		return isnum;
	}

	public List findMatchingList() {

		List list = new ArrayList();
		Matcher m = Pattern.compile(regex).matcher(codestr);
		while (m.find()) {
			list.add(m.group(0));
		}
		return list;
	}

	public static void main(String args[])

	{
		String s = "<script>function aaa(){alert();}</script>";
		RegexTool regex = new RegexTool();
		//regex.setRegex("[src]+[=]+[[a-z]|[A-Z]|[0-9]|[:]|[@]|[.]|[$]]+[ ]+");
		//regex.setRegex("[[[a-z]|[A-Z]|[0-9]|[:]|[@]|[.]|[<]|[//]|[>]|[$]]+[\u4e00-\u9fa5]+]+");
		//regex.setRegex("[<img[ ]+src=\'|\"]+[[a-z]|[A-Z]|[0-9]]+[\'|\".[[a-z]|[A-Z]]+[ ]*[//>]]+");//匹配<img/>
		//regex.setRegex("['|\"][[a-z]|[A-Z]|[0-9]|/]+.[gif|GIF|jpg|JPG|bmp|BMP|png|PNG]+['|\"]");
		regex.setRegex("<script[^>]*>[\\d\\D]*?</script>");
		regex.setCodestr(s);
		List l = regex.findMatchingList();
		//System.out.println("size: "+l.size());
		for (int i = 0; i < l.size(); i++) {
			System.out.println(l.get(i).toString());
		}

	}

}
