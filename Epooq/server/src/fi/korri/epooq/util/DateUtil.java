package fi.korri.epooq.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	
	public static Date convertStringToDate(int day,int month,int year){
		String storyDateStr = day+"/"+month+"/"+year;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		Date storyDate=null;
		try 
		{
			storyDate = sdf.parse(storyDateStr);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		return storyDate;
	}
	
}
