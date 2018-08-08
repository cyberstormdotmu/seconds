package com.tatva.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author pci94
 *	Utility Class for Changing Date formats.. 
 */
public class DateUtil {

	/*public static String convertDateFromSlashtoDash(String created_date){
		String returnDate="";
		try{
			if(created_date!=null && !created_date.isEmpty()){
				SimpleDateFormat slashFormatter = new SimpleDateFormat("dd/MM/yyyy");
				Date dateFormat=slashFormatter.parse(created_date);
				SimpleDateFormat dashFormatter = new SimpleDateFormat("yyyy-MM-dd");
				returnDate=dashFormatter.format(dateFormat);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnDate;
	}*/
	
	/**
	 * 
	 * @param created_date
	 * @return date object in dd/MM/yyyy format
	 */
	public static Date convertDateFromStringtoDate(String created_date){
		Date returnDate=null;
		try{
			if(created_date!=null && !created_date.isEmpty()){
				SimpleDateFormat slashFormatter = new SimpleDateFormat("dd/MM/yyyy");
				returnDate=slashFormatter.parse(created_date);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnDate;
	}
	
	
	public static Date convertDateFromStringtoDate2(String created_date){
		Date returnDate=null;
		try{
			if(created_date!=null && !created_date.isEmpty()){
				SimpleDateFormat slashFormatter = new SimpleDateFormat("dd/MM/yyyy");
				returnDate=slashFormatter.parse(created_date);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnDate;
	}
	

	/**
	 * 
	 * @param time
	 * @return time up to minutes only in string format 
	 */

	public static String formatTime(String time){
		
		time=time.substring(0,5);
		return time;
	}
	
	/**
	 * 
	 * @param created_date
	 * @return date in string with dd/MM/yyyy format
	 */
	public static String convertDateFromDatetoString(Date created_date){
		String returnDate="";
		try{
			if( created_date!=null ){
				SimpleDateFormat dashFormatter = new SimpleDateFormat("dd/MM/yyyy");
				returnDate=dashFormatter.format(created_date);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnDate;
	}
	
	/**
	 * 
	 * @param date
	 * @return date in string with dd/MM/yyyy format
	 */
	public static String convertDateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String stringDate=sdf.format(date);
		return stringDate;
	}
	
	/**
	 * 
	 * @param date
	 * @return date in string format with yyyy/MM/dd format
	 */
	public static String convertInDashedFormat(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String stringDate=sdf.format(date);
		return stringDate;
	}
	
}
