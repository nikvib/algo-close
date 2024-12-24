package com.trade.algo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class AlgoDateUtils {

	public static String getDateAsString() {
		String pattern = "dd-MM-yyyy HH:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(new Date());
	}

	public static String getDateAsString(Date date) {
		String pattern = "dd-MM-yyyy HH:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	public static Date getDate(String date) throws ParseException {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
		DateTime dateTime = dtf.parseDateTime(date);
		return dateTime.toDate(); // 2009-07-16T19:20:30-05:00
	}
	
	public static Date getDateTillHour(String date) throws ParseException {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
		DateTime dateTime = dtf.parseDateTime(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime.toDate());
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getDateTillHour(Date date) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getDateWithTimeForKiteHistoricCandle(String date) throws ParseException {
		String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
		DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
		DateTime dateTime = dtf.parseDateTime(date);
		return dateTime.toDate(); // 2009-07-16T19:20:30-05:00
	}
	
	public static Date getDateWithTimeForTVHistoricCandle(String date) throws ParseException {
		String pattern = "dd-MM-yyyy HH:mm";
		DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
		DateTime dateTime = dtf.parseDateTime(date);
		return dateTime.toDate(); // 2009-07-16T19:20:30-05:00
	}

	public static String getOnlyTimeAsString() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		return timeFormat.format(new Date());
	}

	public static Date getSpecificDate(int year, int month, int day)  {
		Date date = new GregorianCalendar(year, month, day).getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(date);
		Date parse = null;
		try {
			parse = sdf.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parse;
	}
	
	public static Date getSpecificDateAndTime(int year, int month, int day,int hour, int minute,int sec)  {
		Date date = new GregorianCalendar(year, month, day, hour, minute, sec).getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(date);
		Date parse = null;
		try {
			parse = sdf.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parse;
	}

	public static Date getSpecificDate(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(date);
		Date parse = sdf.parse(format);
		return parse;
	}

	public static Date getDateWithoutTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getNextDay(Date d) {
		Date tomorrow = new Date(d.getTime() + (1000 * 60 * 60 * 24));
		return tomorrow;
	}

	public static Date getNextYear(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.YEAR, 1);
		return c.getTime();
	}

	public static Date getPreviousDay(Date d, int days) {
		Date yesterDay = new Date(d.getTime() - (1000 * 60 * 60 * 24));
		return yesterDay;
	}

	public static int getDayNumber(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getHourNumber(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	
	public static int getMinNumber(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}

	public static Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
	
	public static Date addDaysAndSetHour(Date date, int days, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.add(Calendar.DATE, days);
		c.add(Calendar.HOUR_OF_DAY, hour);
		return c.getTime();
	}

	public static Date addHoursToDate(int hours) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	public static Date addHoursToDate(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	public static Date addMinToDate(int min) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, min);
		return calendar.getTime();
	}

	public static Date addMinToDate(Date date, int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, min);
		return calendar.getTime();
	}

	public static Date addSecToDate(int sec) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, sec);
		return calendar.getTime();
	}

	public static Date addSecToDate(Date date, int sec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, sec);
		return calendar.getTime();
	}
	
	
	public static boolean isExitSessionActive() throws ParseException {
		Date d = getSpecificDateAndTime(2024, 06, 20, 17, 59, 59);
		int hr = Integer.parseInt(getDateAsString(d).substring(11, 13));
		if(hr>=8 && hr<=23) {
			return true;
		}
		return false;
	}

}
