package com.aotain.payInterface.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;


/**
 * 时间处理的工具类
 * @author Jason.CW.Cheung
 *
 */
public class DateUtils {

	public static int WEEK_DAY_NUM = 7;

	/**
	 * Cut time on the Date object including hour, min, sec and msec and return
	 * a new object contains year, month and day only.
	 * 
	 * @param date
	 *            The date to be trimmed
	 * @param unit
	 *            see Calendar.HOUR_OF_DAY, MINUTE, SECOND, etc
	 * @return
	 */
	public static Date trimDate(Date date, int unit) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			return trimDate(cal, unit).getTime();
		} else {
			return null;
		}
	}

	public static java.sql.Date addDays(java.sql.Date date2, int unit) {
		Calendar c = Calendar.getInstance();
		c.setTime(date2);
		c.add(Calendar.DAY_OF_MONTH, unit);
		date2.setTime(c.getTimeInMillis());
		return date2;
	}

	@SuppressWarnings("fallthrough")
	public static Calendar trimDate(Calendar cal, int unit) {
		Calendar calendar = (Calendar) cal.clone();

		switch (unit) {
		case Calendar.YEAR:
			calendar.set(Calendar.MONTH, 0);

		case Calendar.MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, 0);

		case Calendar.DAY_OF_MONTH:
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);

		case Calendar.HOUR_OF_DAY:
			calendar.set(Calendar.MINUTE, 0);

		case Calendar.MINUTE:
			calendar.set(Calendar.SECOND, 0);

		case Calendar.SECOND:
			calendar.set(Calendar.MILLISECOND, 0);

		case Calendar.MILLISECOND:

			// clear nothing
			break;
		}

		return calendar;
	}

	public static String formatDateyyyyMMdd(Date date) {
		return formatDateTime(DateFormatConstant.DATE_WITHOUT_SEPARATOR_SHORT,
				date);
	}

	public static String formatDateTime(Date date) {
		return formatDateTime(DateFormatConstant.DATETIME_CHT, date);
	}

	public static String formatDateLongTime(Date date) {
		return formatDateTime(DateFormatConstant.DATETIME_CHS_LONG, date);
	}

	public static String formatDateTime(String format, Date date) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String formatNetricsDate(String formate, Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat(formate, Locale.ENGLISH);
		return sdf.format(d);
	}

	/**
	 * Return a proper formatted date string conform to Netrics Date
	 * standard(US)
	 * 
	 * <p>
	 * Notes: 1. Netrics date format follows US Locales
	 * 
	 * @param d
	 *            - any date
	 * @return String of the date comform to Netrics Standard
	 */
	public static String formatDate(Date d) {
		if (d == null)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		return sdf.format(d);
	}

	public static String formatDateHK(Date date) {
		return formatDateTime(DateFormatConstant.DATE_CHT_SLASH, date);
	}

	public static String formatDateDB(Date date) {
		return formatDateTime(DateFormatConstant.DATE_CHS_HYPHEN, date);
	}

	public static Calendar changeTimeZone(Calendar date, TimeZone timezone) {
		if (!date.getTimeZone().equals(timezone)) {
			Calendar newDate = Calendar.getInstance(timezone);

			newDate.setTimeInMillis(date.getTimeInMillis());

			return newDate;
		}

		return date;
	}

	/**
	 * used computer default timezone
	 */
	public static Calendar changeTimeZone(Calendar date) {
		return changeTimeZone(date, TimeZone.getDefault());
	}

	/**
	 * pass object to automatically change call the Calendar fields timezone and
	 * not trim the time
	 * 
	 * @param object
	 * @param timeZone
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void changeInstanceFieldsTimeZone(Object object,
			TimeZone timeZone) throws IntrospectionException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

		for (PropertyDescriptor pd : pds) {
			if (pd.getPropertyType().equals(Calendar.class)
					&& (pd.getReadMethod().getParameterTypes().length == 0)) {
				Method getter = pd.getReadMethod();
				Method setter = pd.getWriteMethod();
				Object[] args = null;
				Calendar orginalc = (Calendar) getter.invoke(object, args);

				if (orginalc != null) {
					setter.invoke(object, new Object[] { changeTimeZone(orginalc, timeZone) });
				}
			}
		}
	}

	public static void changeInstanceFieldsTimeZone(Object object)
			throws IllegalArgumentException, IntrospectionException,
			IllegalAccessException, InvocationTargetException {
		changeInstanceFieldsTimeZone(object, TimeZone.getDefault());
	}

	public static Date parse(String format, String dateString)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		return sdf.parse(dateString);
	}

	/**
	 * convert Lunar date to Gregorian date String format yyyy-MM-dd
	 * 
	 * @param calen
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	/*
	 * public static String lunarDateToGregorianDateString(String calen, String
	 * day, String month, String year) { if ((calen != null) && (day != null) &&
	 * (month != null) && (year != null)) { if (calen.equals("L")) {
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Calendar
	 * calendar =
	 * LunarCalendarChecker.gregorianDateFromLunarDate(Integer.parseInt(year),
	 * Integer.parseInt(month), Integer.parseInt(day));
	 * 
	 * return sdf.format(calendar.getTime()); } }
	 * 
	 * return null; }
	 */

	public static Calendar toCalendar(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			return cal;
		}
	}

	public static Date addDateOfMonth(Date date, int days) {
		Calendar calendar = toCalendar(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}

	public static java.sql.Date addDateOfMonthForSqlDate(java.sql.Date date,
			int days) {
		Calendar calendar = toCalendar(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return new java.sql.Date(calendar.getTime().getTime());
	}

	public static String toString(Calendar cal) {
		if (cal == null) {
			return null;
		} else {
			return "{" + cal.getTime().toString() + ", locale="
					+ cal.getTimeZone().getDisplayName() + "}";
		}
	}

	public static String toString(ArrayList<Calendar> cals) {
		if (cals == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(cals.getClass().getSimpleName());
		sb.append("{");

		for (int i = 0; i < cals.size(); i++) {
			sb.append(toString(cals.get(i)));

			if (i < (cals.size() - 1)) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	public static int getDayOfMonth(Date date) {
		return toCalendar(date).get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfWeek(Date date) {
		return toCalendar(date).get(Calendar.DAY_OF_WEEK);
	}

	public static String getYYYY(Date date) {
		return String.valueOf(toCalendar(date).get(Calendar.YEAR));
	}

	public static String getMM(Date date) {
		return StringUtils.leftPad(String.valueOf(toCalendar(date).get(
				Calendar.MONTH) + 1), 2, "0");
	}

	public static String getDD(Date date) {
		return StringUtils.leftPad(String.valueOf(toCalendar(date).get(
				Calendar.DAY_OF_MONTH)), 2, "0");
	}

	public static int getYear(Date date) {
		return toCalendar(date).get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		return toCalendar(date).get(Calendar.MONTH);
	}

	public static int getMonthChina(Date date) {
		return toCalendar(date).get(Calendar.MONTH) + 1;
	}

	public static int getMonthChina(java.sql.Date date) {
		return toCalendar(date).get(Calendar.MONTH) + 1;
	}

	public static int getDay(Date date) {
		return toCalendar(date).get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfYear(Date date) {
		return toCalendar(date).get(Calendar.DAY_OF_YEAR);
	}

	public static int DateMinusDate(java.sql.Date d1, java.sql.Date d2) {
		long l1 = d1 == null ? 0 : d1.getTime();
		long l2 = d2 == null ? 0 : d2.getTime();
		return (int) ((l1 - l2) / (24 * 60 * 60 * 1000));
	}

	public static String toString(Calendar[] cals) {
		if (cals == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(cals.getClass().getSimpleName());
		sb.append("{");

		for (int i = 0; i < cals.length; i++) {
			sb.append(toString(cals[i]));

			if (i < cals.length) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	public static Date parseDate(String yyyy, String mm, String dd)
			throws ParseException, NumberFormatException {
		Date bdate = null;

		if (yyyy == null || yyyy.trim().length() == 0)
			return null;
		if (mm == null || mm.trim().length() == 0)
			return null;
		if (dd == null || dd.trim().length() == 0)
			return null;
		if (mm.length() == 1)
			mm = "0" + mm;
		if (dd.length() == 1)
			dd = "0" + dd;

		try {
			Integer.parseInt(yyyy);
			Integer.parseInt(mm);
			Integer.parseInt(dd);
			bdate = parseDate(yyyy + "-" + mm + "-" + dd, "yyyy-MM-dd");
		} catch (NumberFormatException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		}
		return bdate;
	}

	public static Date parseDate(String strdate, String format)
			throws ParseException {
		Date bdate = null;
		try {
			if (null == strdate || StringUtils.isBlank(strdate)) {
				return bdate;
			}
			SimpleDateFormat dFormat = new SimpleDateFormat(format);
			dFormat.setLenient(false);
			bdate = new Date(dFormat.parse(strdate).getTime());
			// Sys.log.printDebug("Convert StringToDate : " + bdate);
		} catch (ParseException e) {

			throw e;
		}
		return bdate;
	}

	public static String parseDate(Date date, String format) {
		SimpleDateFormat dFormat = new SimpleDateFormat(format);
		dFormat.setLenient(false);
		String dateStr = dFormat.format(date);
		return dateStr;
	}
	
	public static String parseDateString(String strdate, String sourceFormat, String destinationFormat) throws ParseException {
		Date date = parseDate(strdate, sourceFormat);
		String dateStr = parseDate(date, destinationFormat);
		return dateStr;
	}

	public static java.sql.Date parseDateSql(String strdate, String format)
			throws ParseException {
		java.sql.Date bdate = null;
		try {
			if (null == strdate) {
				return bdate;
			}
			SimpleDateFormat dFormat = new SimpleDateFormat(format);
			dFormat.setLenient(false);
			bdate = new java.sql.Date(dFormat.parse(strdate).getTime());
			// Sys.log.printDebug("Convert StringToDate : " + bdate);
		} catch (ParseException e) {

			throw e;
		}
		return bdate;
	}

	/**
	 * dateformat use dd/MM/yyyy
	 * 
	 * @param strdate
	 * @return
	 * @throws ParseException
	 */
	public static java.sql.Date parseDateSql(String strdate)
			throws ParseException {
		return parseDateSql(strdate, DateFormatConstant.DATE_CHT_SLASH);
	}

	public static java.sql.Date parseAllDateSql(String strdate)
			throws ParseException {
		java.sql.Date date = null;
		try {
			date = parseDateSql(strdate, DateFormatConstant.DATE_CHT_SLASH);
		} catch (ParseException e) {
			try {
				date = parseDateSql(strdate, DateFormatConstant.DATE_CHS_HYPHEN);
			} catch (ParseException e1) {
				try {
					date = parseDateSql(strdate,
							DateFormatConstant.DATE_ENG_SLASH);
				} catch (ParseException e2) {
					return parseDateSql(strdate,
							DateFormatConstant.DATE_CHT_SLASH);
				}
			}
		}
		return date;
	}

	public static Date parseDate(String yyyy, String MM, String dd, String HH,
			String mm, String ss) throws ParseException {
		Date bdate = null;

		if (yyyy == null || yyyy.trim().length() == 0)
			return null;
		if (MM == null || MM.trim().length() == 0)
			return null;
		if (dd == null || dd.trim().length() == 0)
			return null;
		if (HH == null || HH.trim().length() == 0)
			return null;
		if (mm == null || mm.trim().length() == 0)
			return null;
		if (ss == null || ss.trim().length() == 0)
			return null;
		if (MM.length() == 1)
			MM = "0" + MM;
		if (dd.length() == 1)
			dd = "0" + dd;
		if (HH.length() == 1)
			HH = "0" + HH;
		if (mm.length() == 1)
			mm = "0" + mm;
		if (ss.length() == 1)
			ss = "0" + ss;

		try {
			bdate = parseDate(yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm
					+ ":" + ss, "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			throw e;
		}
		return bdate;
	}

	public static Date parseDate(String yyyy, String MM, String dd, String HH,
			String mm) throws ParseException, NumberFormatException {
		Date bdate = null;

		if (yyyy == null || yyyy.trim().length() == 0)
			return null;
		if (MM == null || MM.trim().length() == 0)
			return null;
		if (dd == null || dd.trim().length() == 0)
			return null;
		if (HH == null || HH.trim().length() == 0)
			return null;
		if (mm == null || mm.trim().length() == 0)
			return null;
		if (MM.length() == 1)
			MM = "0" + MM;
		if (dd.length() == 1)
			dd = "0" + dd;
		if (HH.length() == 1)
			HH = "0" + HH;
		if (mm.length() == 1)
			mm = "0" + mm;

		try {
			Integer.parseInt(yyyy);
			Integer.parseInt(MM);
			Integer.parseInt(dd);
			Integer.parseInt(HH);
			Integer.parseInt(mm);
			bdate = parseDate(yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm,
					"yyyy-MM-dd HH:mm");
		} catch (NumberFormatException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		}
		return bdate;
	}

	// 计算两个任意时间中间的间隔天敄1�7�1�7
	public static int getIntervalDays(Date startday, Date endday) {
		if (startday.after(endday)) {
			Date cal = startday;
			startday = endday;
			endday = cal;
		}
		long sl = startday.getTime();
		long el = endday.getTime();
		long ei = el - sl;
		return (int) (ei / (1000 * 60 * 60 * 24));
	}

	public static String formatUsDateTime(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

		return sdf.format(date);
	}

	public static boolean checkDateFormat(String dateStringToCheck,
			String format) throws ParseException {

		boolean isDateFormatCorrect;

		try {
			parseDate(dateStringToCheck, format);
			isDateFormatCorrect = true;
		} catch (ParseException e) {
			isDateFormatCorrect = false;
			throw e;
		}

		return isDateFormatCorrect;

	}

	/*
	 * Compare two dates with year, month and date but ignore time
	 * compareDateIgnoreTime(Date startDate, Date endDate) -1 : startDate >
	 * endDate 0 : startDate == endDate 1 : startDate < endDate
	 */
	public static int compareDateIgnoreTime(Date startDate, Date endDate) {
		Calendar calStartDate = Calendar.getInstance();
		Calendar calEndDate = Calendar.getInstance();
		calStartDate.setTime(startDate);
		calEndDate.setTime(endDate);
		int yearOfStartDate = calStartDate.get(Calendar.YEAR);
		int yearOfEndDate = calEndDate.get(Calendar.YEAR);
		int monthOfStartDate = calStartDate.get(Calendar.MONTH);
		int monthOfEndDate = calEndDate.get(Calendar.MONTH);
		int dateOfStartDate = calStartDate.get(Calendar.DATE);
		int dateOfEndDate = calEndDate.get(Calendar.DATE);

		if (yearOfStartDate > yearOfEndDate) {
			return -1;
		} else if (yearOfStartDate == yearOfEndDate) {
			if (monthOfStartDate > monthOfEndDate) {
				return -1;
			} else if (monthOfStartDate == monthOfEndDate) {
				if (dateOfStartDate > dateOfEndDate) {
					return -1;
				} else if (dateOfStartDate == dateOfEndDate) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}

	public static int daysDifference(Date fromDate, Date toDate) {
		return compareDateIgnoreTime(fromDate, toDate);
	}

	public static String getPatternByLocale(Locale locale) {
		if (locale == null) {
			return DateFormatConstant.DATE_ENG_SLASH;
		}
		if (Locale.US.equals(locale))
			return DateFormatConstant.DATE_ENG_SLASH;

		if (Locale.SIMPLIFIED_CHINESE.equals(locale))
			return DateFormatConstant.DATE_CHS_HYPHEN;

		if (Locale.TRADITIONAL_CHINESE.equals(locale))
			return DateFormatConstant.DATE_CHT_SLASH;

		return DateFormatConstant.DATE_ENG_SLASH;

	}

	public static Date makeDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	public static java.sql.Date makeSqlDate(int year, int month, int day) {
		Date date = makeDate(year, month, day);
		return new java.sql.Date(date.getTime());
	}

	public static java.sql.Date getCS_NULL_DATE() {
		return getNullDate();
	}

	public static java.sql.Date getNullDate() {
		return makeSqlDate(1111, 11, 11);
	}

	public static java.sql.Date getCS_9999_DATE() {
		return makeSqlDate(9999, 1, 1);
	}

	public static java.sql.Date getCS_CONVERT_DATE() {
		return makeSqlDate(2000, 10, 1);
	}

	public static int getDiffYear(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
	}

	public static boolean isDate(String format, String dateString) {
		if (StringUtil.isEmptyString(dateString)) {
			return false;
		}
		try {
			parse(format, dateString);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static String getNullIfDefaultDate(String date) {
		if (date.equals("11/11/1111")) {
			date = "";
		}
		return date;
	}

	public static boolean isNullDate(Date date) {
		if (date == null) {
			return true;
		} else {
			String dateStr = DateUtils.formatDateHK(date);
			if ("11/11/1111".equals(dateStr)) {
				return true;
			}
		}
		return false;
	}

	// "dd/MM/yyyy" convert to "yyyy-MM-dd" "20/12/2012"-->"2012-12-20"
	public static String getConvertDate(String date) {
		if (date.contains("/")) {
			String[] str_arry = date.split("/");

			return str_arry[2] + "-" + str_arry[1] + "-" + str_arry[0];
		} else {
			return date;
		}

	}

	// "yyyy-MM-dd" convert to "dd/MM/yyyy" "2012-12-20"-->"20/12/2012"
	public static String getConvertDate2(String date) {
		if (date.contains("-")) {
			String[] str_arry = date.split("-");

			return str_arry[2] + "/" + str_arry[1] + "/" + str_arry[0];
		} else {
			return date;
		}

	}

	public static java.sql.Date newDate(java.sql.Date temp) {
		return temp != null ? new java.sql.Date(temp.getTime()) : null;
	}

	public static java.sql.Date newDate(Date temp) {
		return temp != null ? new java.sql.Date(temp.getTime()) : null;
	}

	public static String getStryyyyMMddTHHmmssSSS(Date d) {
		return parseDate(d, DateFormatConstant.DATETIME_WS_LONG);
	}

	public static String getyyyyMMddHHmm(Date d) {
		return parseDate(d, DateFormatConstant.DATETIME_WITHOUT_SECOND);
	}

	public static String getDateString(Object obj) {
		String defualtString = "";
		if (obj == null || "11/11/1111".equals(obj.toString())
				|| "11-11-1111".equals(obj.toString())) {
			return defualtString;
		}
		return getConvertDate2(obj.toString());
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println(parseDate(parseDate("20130902083431", DateFormatConstant.DATETIME_WITHOUT_SEPARATOR_LONG), DateFormatConstant.DATETIME_CHS));
		Date date = new Date();
		String day = getDD(date);
		System.out.println(day);
		String today = getYYYY(date) + getMM(date) + getDD(date);
		String yestoday = getYYYY(date) + getMM(date) + String.valueOf(getDay(date) - 1);
		System.out.println("today:" + today + ",yestoday:" + yestoday);
		System.out.println(parseDate(new Date(), DateFormatConstant.DATETIME_CHS));
		
	}

}
