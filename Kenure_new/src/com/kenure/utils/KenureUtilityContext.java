package com.kenure.utils;
/**
 * 
 * @author TatvaSoft
 * 
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kenure.entity.User;

/*
 * 	This Class is for Kenure Utility Context constants
 */
public class KenureUtilityContext {

	public static String currentUser = "currentUser";

	public static String normalCustomer = "normalCustomer";

	public static HashMap<String, Integer> billingFrequency = new HashMap<String, Integer>();

	public static List<String> timeZone = new LinkedList<>();

	public static HashMap<String,String> billingColumnFields = new HashMap<String, String>();
	
	public static HashMap<String,String> freereportDCMap = new HashMap<String,String>();
	
	public static HashMap<String,String> freereportConsumerEPMap = new HashMap<String,String>();
	
	public static HashMap<String,String> freereportSiteMap = new HashMap<String,String>();
	
	public static HashMap<String,String> freereportInstallerMap = new HashMap<String,String>();
	
	public static HashMap<String,String> freereportConsumerMap = new HashMap<String,String>();
	
	public static final String SIMPLE_DATE_FORMAT ="yyyy-MM-dd HH:mm:ss";
	
	public static final String TIME_ZONE = "UTC";
	
	public static final String BLU_TOWER = "BluTower";
	
	public static final String OK_INFO_WAITING = "Ok_Info_WaitingForData";
	
	public static final String USERNAME = "Username:";
	
	public static final String PASSWORD = "Password:";
	
	public static final String CARRAIGE_RETURN = "\r";
	
	public static final String LOGOUT = "LOGOUT";
	
	public static final String RADIX = "cp1252";
	
	public static final String ERROR_IN_COMMAND_RESPONSE = "ERROR";
	
	public static final String OK_IN_COMMAND_RESPONSE = "OK";
	
	public static final String YES_IN_COMMAND_RESPONSE = "YES";
	
	public static final String NO_IN_COMMAND_RESPONSE = "NO";
	
	public static final String GREATER_THAN_SYMBOL = ">";
	
	public static final String SET_RTC_COMMAND = "#rtc=";
	
	public static final String SET_PIN_COMMAND = "#pin=Gl0D";
	
	public static final String STP_COMMAND = "#stp";
	
	public static final String CLEAR_COMMAND = "#clear";
	
	public static final String CLR_CARD_COMMAND = "#clr_card";
	
	public static final String UCD_COMMAND = "#ucd=";
	
	public static final String NID_COMMAND = "#nid=";
	
	public static final String NID_RESPONSE = "NID";
	
	public static final String PKT_COMMAND = "#pkt=0";
	
	public static final String NET3_COMMAND = "#net3=0";
	
	public static final String STS_COMMAND = "#sts";
	
	public static final String USR_COMMAND = "#usr=";
	
	public static final String PWD_COMMAND = "#pwd=";
	
	public static final String PLM_COMMAND = "#plm=0,0";
	
	public static final String SET_STT_COMMAND = "#stt=";
	
	public static final int THOUSAND = 1000;
	
	public static final String ENDPOINTS_CLEARED = "All endpoints cleared";
	
	public static final String DATA_ERASED = "Data Erased";
	
	public static final String batteryVoltageStr = "Primary Supply (mV):";
	
	public static final String RUN_COMMAND = "#run?";
	
	public static final String STT_COMMAND = "#stt";
	
	public static final String CML1_COMMAND = "#cml1";
	
	public static final String CMLN_COMMAND = "#cmln";
	
	// To check if user is Super Customer or Normal Customer based on this we set its updated by in db column
	
	public static User isNormalCustomer(HttpServletRequest request){
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		User loggedUser = null;
		if(isNormalCustomer != null && isNormalCustomer){
			loggedUser = (User) request.getSession().getAttribute("normalCustomer");
		}else{
			loggedUser = (User) request.getSession().getAttribute("currentUser");
		}
		return loggedUser;
	}

	static {
		billingFrequency.put("Weekly", 7);
		billingFrequency.put("Monthly", 30);
		billingFrequency.put("Quarterly", 90);
		billingFrequency.put("Yearly", 365);

		// Different TimeZone 
		timeZone.add("-12:00");
		timeZone.add("-11:00");
		timeZone.add("-10:00");
		timeZone.add("-09:30");
		timeZone.add("-09:00");
		timeZone.add("-08:00");
		timeZone.add("-07:00");
		timeZone.add("-06:00");
		timeZone.add("-05:00");
		timeZone.add("-04:30");
		timeZone.add("-04:00");
		timeZone.add("-03:00");
		timeZone.add("-02:00");
		timeZone.add("-01:00");
		timeZone.add("+00:00");
		timeZone.add("+01:00");
		timeZone.add("+02:00");
		timeZone.add("+03:00");
		timeZone.add("+03:30");
		timeZone.add("+04:00");
		timeZone.add("+04:30");
		timeZone.add("+05:00");
		timeZone.add("+05:30");
		timeZone.add("+05:45");
		timeZone.add("+06:00");
		timeZone.add("+06:30");
		timeZone.add("+07:00");
		timeZone.add("+08:00");
		timeZone.add("+08:45");
		timeZone.add("+09:00");
		timeZone.add("+09:30");
		timeZone.add("+10:00");
		timeZone.add("+10:30");
		timeZone.add("+11:00");
		timeZone.add("+11:30");
		timeZone.add("+12:00");
		timeZone.add("+12:45");
		timeZone.add("+13:00");
		timeZone.add("+14:00");
		
		// Billing column data added here
		
		billingColumnFields.put("Register Id", "registerId");
		billingColumnFields.put("Billing Frequency (in Days)", "billingFrequency");
		billingColumnFields.put("Billing Start Date","billingStartDate");
		billingColumnFields.put("Billing End Date","billingEndDate");
		billingColumnFields.put("Current Reading","currentReading");
		billingColumnFields.put("Last Reading","lastReading");
		billingColumnFields.put("Consumed Unit","consumedUnit");
		billingColumnFields.put("Total Amount","totalAmount");
		billingColumnFields.put("Bill Date","billDate");
	
		// FreeReport Data and Selection fields
		freereportDCMap.put("Dc-SerailNumber", "dcSerialNumber");
		freereportDCMap.put("Location(Latitude and Longitude)","location");
		freereportDCMap.put("Simcard Number","simNumber");
		freereportDCMap.put("Site Name","siteName");
		freereportDCMap.put("Installer Name","installerName");
		
		freereportConsumerEPMap.put("Site Name","siteName");
		freereportConsumerEPMap.put("Consumer Name","consumerName");
		freereportConsumerEPMap.put("Tariff Plan","tariffName");
		freereportConsumerEPMap.put("End Point Serial Number","epSerailnumber");
		freereportConsumerEPMap.put("Location","location");
		
		freereportSiteMap.put("Name","name");
		freereportSiteMap.put("Under Region","region");
		freereportSiteMap.put("Status","status");
		freereportSiteMap.put("Commissioning-Type","commissioningType");
		
		freereportInstallerMap.put("Installation Name","installerName");
		freereportInstallerMap.put("Name","name");
		
		freereportConsumerMap.put("Consumer Account Number","consumerAccountNumber");
		freereportConsumerMap.put("First Name","firstName");
		freereportConsumerMap.put("Last Name","lastName");
		freereportConsumerMap.put("Address","address");
		freereportConsumerMap.put("Street Name","streetName");
		freereportConsumerMap.put("Zip-Code","zip");
		
		
		Collections.unmodifiableCollection(timeZone);
		Collections.unmodifiableMap(billingColumnFields);
		Collections.unmodifiableMap(freereportDCMap);
		Collections.unmodifiableMap(freereportConsumerEPMap);
		Collections.unmodifiableMap(freereportSiteMap);
		Collections.unmodifiableMap(freereportInstallerMap);
		Collections.unmodifiableMap(freereportConsumerMap);
	}

}