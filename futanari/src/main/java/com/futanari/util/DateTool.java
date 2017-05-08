package com.futanari.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import java.util.regex.Pattern;

public class DateTool {

	private static Logger log = Logger.getLogger(DateTool.class);
	private static String defaultDatePattern = null;
	public static final String DATE_MASK = "yyyy-MM-dd";
	public static final String DATE_TIME_MASK = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_PATTERN_MASK_1 = "HH:mm:ss";
	public static final String TIME_PATTERN_MASK_2 = "HH:mm";
	public static final String TIME_PATTERN_MASK_3 = "yyyy-MM";

	public static final Pattern DATE_PATTERN_1 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}$");
	public static final Pattern DATE_PATTERN_11 = Pattern.compile("\\d{4}-\\d{1}-\\d{1}$");
	public static final Pattern DATE_PATTERN_21 = Pattern.compile("\\d{4}-\\d{2}-\\d{1}$");
	public static final Pattern DATE_PATTERN_12 = Pattern.compile("\\d{4}-\\d{1}-\\d{2}$");
	public static final Pattern DATE_PATTERN_2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
	public static final Pattern DATE_PATTERN_3 = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$");
	public static final Pattern TIME_PATTERN_1 = Pattern.compile("\\d{2}:\\d{2}:\\d{2}$");
	public static final Pattern TIME_PATTERN_2 = Pattern.compile("\\d{2}:\\d{2}$");
	public static final Pattern YEAR_PATTERN = Pattern.compile("\\d{4}$");
	public static final Pattern YEAR_MONTH_PATTERN = Pattern.compile("\\d{4}-\\d{2}$");

	public static final Pattern MONTH_DAY_PATTERN = Pattern.compile("\\d{2}-\\d{2}$");

	public static final int DT_LASTDAY_OF_MONTH = 1; // 当前月的最后一天
	public static final int DT_TODAY_IN_LAST_MONTH = 2; // 上一个月的当天
	public static final int DT_FIRSTDAY_OF_MONTH = 3; // 当前月的第一天

	static {
		System.setProperty("user.timezone", "Asia/Shanghai");

	}

	// ~ Methods
	// ================================================================

	/**
	 * Return default datePattern (MM/dd/yyyy)
	 *
	 * @return a string representing the date pattern on the UI
	 */
	public static synchronized String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		try {
			defaultDatePattern = "MM/dd/yyyy";
		} catch (MissingResourceException mse) {
			defaultDatePattern = "yyyy-dd-MM";
		}
		return defaultDatePattern;
	}

	/**
	 * This method attempts to convert an Oracle-formatted date in the form
	 * dd-MMM-yyyy to mm/dd/yyyy.
	 *
	 * @param aDate date from database as a string
	 * @return formatted string for the ui
	 */
	public static final String getDate(Date aDate) {
		FastDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = FastDateFormat.getInstance(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 *
	 * @param aMask   the date pattern the string is in
	 * @param strDate a string representation of a date
	 * @return a converted Date object
	 * @throws ParseException
	 * @see FastDateFormat
	 */
	public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
		FastDateFormat df = null;
		Date date = null;
		df = FastDateFormat.getInstance(aMask);

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM
	 * a
	 *
	 * @param theTime the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(DateTool.DATE_TIME_MASK, theTime);
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 *
	 * @return the current date
	 * @throws ParseException
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		FastDateFormat df = FastDateFormat.getInstance(getDatePattern());

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 *
	 * @param aMask the date pattern the string is in
	 * @param aDate a date object
	 * @return a formatted string representation of the date
	 * @see FastDateFormat
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		FastDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = FastDateFormat.getInstance(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 解析日期字符串至日期类型内容
	 *
	 * @param date   日期字符串
	 * @param format 与日期字符串格式匹配的格式
	 * @return 解析后返回的日期
	 */
	public static Date parseDate(String date, String format) {
		try {
			FastDateFormat formatter;
			if (null == format)
				throw new IllegalArgumentException("错误的日期格式");
			formatter = FastDateFormat.getInstance(format);
			ParsePosition pos = new ParsePosition(0);
			return formatter.parse(date, pos);
		} catch (Exception e) {
			throw new IllegalArgumentException("错误的日期:" + date + " " + e);
		}
	}

	/**
	 * This method generates a string representation of a date based on the
	 * System Property 'dateFormat' in the format you specify on input
	 *
	 * @param aDate A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static final String convertDataToString(Date date, String pattern) {
		return getDateTime(pattern, date);
	}

	/**
	 * This method converts a String to a date using the datePattern
	 *
	 * @param strDate the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate) throws ParseException {
		Date aDate = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("converting date with pattern: " + getDatePattern());
			}
			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			log.error("Could not convert '" + strDate + "' to a date, throwing exception");
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}
		return aDate;
	}

	/**
	 * 解析日期字符串至日期类型内容,返回java.sql.Date日期类型数据内容
	 *
	 * @param date   日期字符串
	 * @param format 与日期字符串格式匹配的格式
	 * @return 解析后返回的日期
	 */
	public static java.sql.Date parseSqlDate(String date, String format) {
		try {
			FastDateFormat formatter;
			if (null == format)
				throw new IllegalArgumentException("错误的日期格式");
			formatter = FastDateFormat.getInstance(format);
			ParsePosition pos = new ParsePosition(0);
			Date utilDate = formatter.parse(date, pos);
			return new java.sql.Date(utilDate.getTime());
		} catch (Exception e) {
			throw new IllegalArgumentException("错误的日期:" + date + " " + e);
		}
	}

	/**
	 * 获取日期格式
	 *
	 * @param date 日期字符串
	 * @return 日期格式
	 * @throws Exception 非法参数或不支持格式错误信息
	 */
	public static String getDatePattern(String date) throws Exception {
		if (date == null || "".equals(date)) {
			throw new IllegalArgumentException("非法日期参数，无法解析日期");
		}
		if (DateTool.DATE_PATTERN_1.matcher(date).find()) {
			return "yyyy-MM-dd";
		}
		if (DateTool.DATE_PATTERN_12.matcher(date).find()) {
			return "yyyy-M-dd";
		}
		if (DateTool.DATE_PATTERN_21.matcher(date).find()) {
			return "yyyy-MM-d";
		}
		if (DateTool.DATE_PATTERN_11.matcher(date).find()) {
			return "yyyy-M-d";
		} else if (DATE_PATTERN_2.matcher(date).find()) {
			return "yyyy-MM-dd HH:mm:ss";
		} else if (DATE_PATTERN_3.matcher(date).find()) {
			return "yyyy-MM-dd HH:mm";
		} else if (TIME_PATTERN_1.matcher(date).find()) {
			return "HH:mm:ss";
		} else if (TIME_PATTERN_2.matcher(date).find()) {
			return "HH:mm";
		} else if (YEAR_PATTERN.matcher(date).find()) {
			return "yyyy";
		} else if (YEAR_MONTH_PATTERN.matcher(date).find()) {
			return "yyyy-MM";
		} else if (MONTH_DAY_PATTERN.matcher(date).find()) {
			return "MM-dd";
		} else {
			throw new Exception("未知日期格式，无法解析日期");
		}
	}

	/**
	 * 获取系统时间yyyy-MM-dd HH:mm:ss
	 */
	public static String getSystemDateTime() {
		return DateTool.convertDataToString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取系统时间yyyy-MM-dd
	 */
	public static String getSystemDate() {
		return DateTool.convertDataToString(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取系统年份
	 *
	 * @return
	 */
	public static String getSystemYear() {
		return DateTool.convertDataToString(new Date(), "yyyy");
	}

	/**
	 * 截取日期,可以截取到年，月，日，时，分，秒
	 *
	 * @param date
	 * @param truncType ：Calendar.YEAR， Calendar.YEAR， Calendar.DAY_OF_MONTH，
	 *                  Calendar.HOUR_OF_DAY， Calendar.MINUTE， Calendar.SECOND
	 * @return
	 * @author ykbao
	 * @since 2010-1-22
	 */
	public static Date trunc(Date date, int truncType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year, month, day, hour, minute, second;
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
		second = calendar.get(Calendar.SECOND);

		switch (truncType) {
			case Calendar.YEAR:
				month = Calendar.JANUARY;
			case Calendar.MONTH:
				day = 1;
			case Calendar.DAY_OF_MONTH:
				hour = 0;
			case Calendar.HOUR_OF_DAY:
				minute = 0;
			case Calendar.MINUTE:
				second = 0;
			case Calendar.SECOND:
			default:
		}
		calendar.set(year, month, day, hour, minute, second);
		return calendar.getTime();
	}

	/**
	 * 加减日期,可以加减年，月，日，时，分，秒。
	 *
	 * @param date
	 * @param field ：Calendar.YEAR， Calendar.YEAR， Calendar.DAY_OF_MONTH，
	 *              Calendar.HOUR_OF_DAY， Calendar.MINUTE， Calendar.SECOND
	 * @param value : 要加减的值，可以为负
	 * @return
	 * @author ykbao
	 * @since 2010-1-22
	 */
	public static Date add(Date date, int field, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(field, calendar.get(field) + value);
		return calendar.getTime();
	}

	/**
	 * 取当前日期(天)的当前周的第一天和最后一天日期
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String[] getWeek(String date, String format) {
		String[] result = new String[2];
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, format));
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
		}
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - dayOfWeek + 2);
		result[0] = getDateTime(format, calendar.getTime());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 6);
		result[1] = getDateTime(format, calendar.getTime());

		return result;
	}

	/**
	 * 取当前日期(天)的当前月的第一天和最后一天日期
	 *
	 * @param date
	 * @param format
	 */
	public static String[] getMonth(String date, String format) {
		String[] result = new String[2];
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, format));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		result[0] = getDateTime(format, calendar.getTime());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
		result[1] = getDateTime(format, calendar.getTime());

		return result;
	}

	/**
	 * 取当前月份第一天日期和最后一天日期 (yyyy-MM-dd)
	 *
	 * @return String
	 */
	public static String[] getMothByDay(String date) {
		String[] result = new String[2];
		FastDateFormat df = FastDateFormat.getInstance("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, "yyyy-MM-dd"));
		Calendar cpcalendar = (Calendar) calendar.clone();
		cpcalendar.set(Calendar.DAY_OF_MONTH, 1);
		result[0] = df.format(new Date(cpcalendar.getTimeInMillis()));
		cpcalendar.add(Calendar.MONTH, 1);
		cpcalendar.add(Calendar.DATE, -1);
		result[1] = df.format(new Date(cpcalendar.getTimeInMillis()));
		return result;
	}

	/**
	 * 取当前月份第一天日期和最后一天日期 (yyyy-MM-dd)
	 *
	 * @return String
	 */
	public static String[] getMothByDay(String date, String pattern) {
		String[] result = new String[2];
		FastDateFormat df = FastDateFormat.getInstance("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, pattern));
		Calendar cpcalendar = (Calendar) calendar.clone();
		cpcalendar.set(Calendar.DAY_OF_MONTH, 1);
		result[0] = df.format(new Date(cpcalendar.getTimeInMillis()));
		cpcalendar.add(Calendar.MONTH, 1);
		cpcalendar.add(Calendar.DATE, -1);
		result[1] = df.format(new Date(cpcalendar.getTimeInMillis()));
		return result;
	}

	public static String getTimePoint(int point) {
		long unit = 3600000;
		Calendar calendar = new GregorianCalendar(2000, 0, 1, 0, 0, 0);
		Date date = new Date(calendar.getTimeInMillis() + unit * point);

		return getDateTime(TIME_PATTERN_MASK_2, date);
	}

	public static String getWeekPoint(int point) {
		String result = null;

		switch (point) {
			case 0:
				result = "周一";
				break;
			case 1:
				result = "周二";
				break;
			case 2:
				result = "周三";
				break;
			case 3:
				result = "周四";
				break;
			case 4:
				result = "周五";
				break;
			case 5:
				result = "周六";
				break;
			case 6:
				result = "周日";
				break;
		}

		return result;
	}

	public static String getWeekPoint(Date date) {
		String result = null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
			case 1:
				result = "周日";
				break;
			case 2:
				result = "周一";
				break;
			case 3:
				result = "周二";
				break;
			case 4:
				result = "周三";
				break;
			case 5:
				result = "周四";
				break;
			case 6:
				result = "周五";
				break;
			case 7:
				result = "周六";
				break;
		}

		return result;
	}

	public static int getWeekDay(Date date) {
		String result = null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		return dayOfWeek;
	}

	public static int getDays4Month(String[] date, String format) {
		int result = 0;
		for (String day : date) {
			if (day != null && !day.equals("") && !day.equalsIgnoreCase("null")) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(parseDate(day, format));
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);

				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

				result = dayOfMonth > result ? dayOfMonth : result;
			}
		}

		return result;
	}

	public static String getMonthPoint(Date date) {
		String result = null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		result = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

		return result;
	}

	public static String getLastWeek(String date, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, format));
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);

		return getDateTime(format, calendar.getTime());
	}

	public static String getLastDay(String date, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, format));
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);

		return getDateTime(format, calendar.getTime());
	}

	/**
	 * 获取特定日期的格式化日期，以当前日期为基准
	 *
	 * @param mode int
	 *             指定的格式：DT_LASTDAY_OF_MONTH，DT_TODAY_IN_LAST_MONTH，DT_FIRSTDAY_OF_MONTH
	 * @return String
	 */
	public static String formatDate(int mode) {
		String retStr = "";
		FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		switch (mode) {
			case DateTool.DT_LASTDAY_OF_MONTH:
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				break;
			case DateTool.DT_TODAY_IN_LAST_MONTH:
				cal.add(Calendar.MONTH, -1);
				break;
			case DateTool.DT_FIRSTDAY_OF_MONTH:
				cal.set(Calendar.DAY_OF_MONTH, 1);
				break;
			default:
				break;
		}
		retStr = sdf.format(cal.getTime());
		return retStr;
	}

	/**
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0) && ((year % 100 != 0) | (year % 400 == 0))) {
			return true;
		} else {
			return false;
		}
	}

	public static Date getYesterday() {
		Date currentTime = new Date();
		return DateTool.getDateAdd(currentTime, Calendar.DATE, -1);
	}

	public static Date getTomorrow() {
		Date currentTime = new Date();
		return DateTool.getDateAdd(currentTime, Calendar.DATE, 1);
	}

	/**
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date getPreviousDay(Date date, int i) {
		return DateTool.getDateAdd(date, Calendar.DATE, (-1) * i);
	}

	/**
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date getNextDay(Date date, int i) {
		return DateTool.getDateAdd(date, Calendar.DATE, i);
	}

	public static String getFormatDate(String format, Date date) {
		String dateString = "";
		if (date != null) {
			FastDateFormat formatter = FastDateFormat.getInstance(format);
			dateString = formatter.format(date);
		}
		return dateString;
	}

	public static Date getDate(String date) {
		FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
		Date day = null;
		try {
			day = formatter.parse(date);
		} catch (ParseException e) {
		}
		return day;
	}

	public static Date getDateTime(String date) throws ParseException {
		Date date1 = null;

		try {
			String p = DateTool.getDatePattern(date);
			date1 = DateTool.getDateTime(date, p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date1;
	}

	public static Date getDateTime(String date, String format) throws ParseException {
		return (FastDateFormat.getInstance(format)).parse(date);
	}

	public static Date getTime(String time) throws ParseException {
		return (FastDateFormat.getInstance("HH:mm:ss")).parse(time);
	}

	public static int getAge(Date birthDay) throws Exception {
		int age = 0;
		try {
			Calendar cal = Calendar.getInstance();

			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH);
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
			cal.setTime(birthDay);

			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH);
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			age = yearNow - yearBirth;

			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth) {
						age--;
					} else {
						// do nothing
					}
				} else {
					age--;
				}
			} else {
			}
		} catch (Exception e) {
		}

		return age;
	}

	/**
	 * @param date1
	 * @param calendarEx like:Calendar.DATE
	 * @param time
	 * @return
	 */
	public static Date getDateAdd(Date date1, int calendarEx, int time) {
		Date endday = date1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endday);
		calendar.add(calendarEx, time);
		return calendar.getTime();

	}

	/**
	 * @param year
	 * @param month
	 * @return List<Date>
	 */
	public static List getDateList(String year, String month) {
		List datelist = new ArrayList();
		String bigMonth[] = {"01", "03", "05", "07", "08", "10", "12"};
		String smallMonth[] = {"04", "06", "09", "11"};
		Date tempdate;
		try {
			// 大月
			if (StringTool.isInArray(month, bigMonth)) {
				for (int i = 1; i <= 31; i++) {
					tempdate = DateTool.getDate(year + "-" + month + "-" + i);
					datelist.add(tempdate);
				}
			}
			// 小月
			else if (StringTool.isInArray(month, smallMonth)) {
				for (int i = 1; i <= 30; i++) {
					tempdate = DateTool.getDate(year + "-" + month + "-" + i);
					datelist.add(tempdate);
				}
			}
			// 闰年2月
			else if (DateTool.isLeapYear(Integer.parseInt(year))) {
				for (int i = 1; i <= 29; i++) {
					tempdate = DateTool.getDate(year + "-" + month + "-" + i);
					datelist.add(tempdate);
				}
			}
			// 平年2月
			else {
				for (int i = 1; i <= 28; i++) {
					tempdate = DateTool.getDate(year + "-" + month + "-" + i);
					datelist.add(tempdate);
				}
			}

		} catch (Exception e) {
		}

		return datelist;

	}

	public static Date convertToDate(Object dateStr) {
		Date d = null;
		if (dateStr == null) {
			d = null;
		} else if (dateStr.getClass().equals(Long.class)) {
			d = new Date((Long) dateStr);
		} else if (dateStr.getClass().equals(Date.class)) {
			d = (Date) dateStr;

		} else if (dateStr.getClass().equals(Timestamp.class)) {
			d = new Date(((Timestamp) dateStr).getTime());
		} else if (dateStr.getClass().equals(String.class)) {
			try {
				d = DateTool.parseDate(dateStr.toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (Exception e3) {
				try {
					d = DateTool.parseDate(dateStr.toString(), "yyyy-MM-dd HH:mm:ss.SS");
				} catch (Exception e4) {
				}
			}
		} else {
		}

		return d;
	}

	/**
	 * 得到某个时间段内的所有日期
	 *
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	/**
	 * 得到某个时间段内的天數
	 *
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	public static int findDatesNum(Date dBegin, Date dEnd) {
		int days = (int) ((dBegin.getTime() - dEnd.getTime()) / 86400000);
		return days;
	}

	/**
	 * 获取当年的第一天
	 *
	 * @return
	 */
	public static Date getCurrYearFirst() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}

	/**
	 * 获取当年的最后一天
	 *
	 * @return
	 */
	public static Date getCurrYearLast() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearLast(currentYear);
	}

	/**
	 * 获取某年第一天日期
	 *
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 *
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}


	public static void main(String[] args) throws Exception {
		try {
			String d = DateTool.getLastDay("2013-02-01", "yyyy-MM-dd");
			System.out.println(d);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
