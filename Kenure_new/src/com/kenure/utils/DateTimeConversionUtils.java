package com.kenure.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * @author TatvaSoft
 *
 */
public class DateTimeConversionUtils {
	
	/*
	 * Input : This method takes Date object from different time zones
	 * Output : This method returns Date object with UTC time zone
	 */
	public static Date getDateInUTC(Date dateTimeObject) {
		
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			return dateFormatLocal.parse( dateFormatUTC.format(dateTimeObject) );
		} catch (ParseException e) {
		}
		return null;
	}
	
	
	/*
	 * Input : This method takes Date object from different time zones and customer time zone
	 * Output : This method returns converted UTC Date object from customer's time zone
	 */
	public static Date getDateInUTCFromCustomerTimeZone(Date dateTimeObject, String customerTimeZone) {
		
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

		SimpleDateFormat dateFormatInCustomerTimeZone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("GMT"+customerTimeZone));
		
		try {
			return dateFormatInCustomerTimeZone.parse( dateFormatUTC.format(dateTimeObject) );
		} catch (ParseException e) {
		}
		return null;
	}
	
	/*
	 * Input : This method takes Date object with UTC time zone and 'timeZone' parameter value to convert it to given time zone from UTC
	 * Output : This method returns Date object with specified time zone in 'timeZone' parameter
	 */
	public static Date getDateObjectByTimezone(Date dateObjectInUTC, String timeZone) {
		
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("GMT"+timeZone));
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		try {
			return dateFormatUTC.parse(dateFormat.format(dateObjectInUTC));
		} catch (ParseException e) {
		}
		return null;
	}
	
	/*
	 * Input : This method takes Date object with UTC time zone and 'timeZone' parameter value to convert it to given time zone from UTC
	 * Output : This method returns String form of Date object with specified time zone in 'timeZone' parameter
	 */
	public static String getDateStringByTimezone(Date dateObjectInUTC, String timeZone) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"+timeZone));
		
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Date date = null;
		
		try {
			date = isoFormat.parse(dateObjectInUTC.toString());
		} catch (ParseException e) {
		}
		
		return dateFormat.format(date);
	}
	
	
	public static String getStringFromDate(Date dateObject) {
		
		if (dateObject != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			return dateFormat.format(dateObject);
		} else {
			return null;
		}
	}
	
	
	public static Date getDateFromString(String dateInStringForm) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			return dateFormat.parse(dateInStringForm);
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static Date getPreviousDate(Date date){
		 // Use the Calendar class to subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
	}
	
	public static Date getNextDate(Date date){
		 // Use the Calendar class to subtract one day
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.add(Calendar.DAY_OF_YEAR, +1);
       return calendar.getTime();
	}
	
	public static String getTodaysDateAsString(){
		try{
			LocalDate date = LocalDate.now();
			String format = date.toString();
			format = format.replace("-", "");
			return format;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getPreviousDateBasedOnCurrentDateFromMidNight(){
		Date date = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(calender.get(Calendar.YEAR), calender.get(calender.MONTH), calender.get(calender.DAY_OF_MONTH)-1, 00, 00, 00);
		return calender.getTime();
	}
	
	public static Date getTodaysDateToMidNight(){
		Date date = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(calender.get(Calendar.YEAR), calender.get(calender.MONTH), calender.get(calender.DAY_OF_MONTH)-1, 23, 59, 59);
		return calender.getTime();
	}
	
	public static Date getDateFromNowToAppliedMonthDuration(int period){
		Date date = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(calender.get(Calendar.YEAR), calender.get(calender.MONTH)-period, calender.get(calender.DAY_OF_MONTH)
					,calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), calender.get(Calendar.SECOND));
		return calender.getTime();
	}
	
}