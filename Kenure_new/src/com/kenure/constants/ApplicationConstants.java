package com.kenure.constants;



/**
 * 
 * @author TatvaSoft
 *
 */

/*
 * This class contains all Application Constants 
 */
public class ApplicationConstants {

	private static ApplicationConstants appConstObj;

	private String projectPath;

	private String fullPath;
	
	public static final String AUTHENTICATIONFAILED = "authenticationfailed";

	public static final String SERVERVALIDATIONFAILED= "serversidevalidationfailed";

	public static final String AUTHENTICATIONSUCCESS = "authenticationsuccess";

	public static final String NOUSERFOUND = "nosuchuserfound";

	public static final String NOINSTALLERFOUND = "nosuchinstallerfound";

	public static final String NOCOUNTRYFOUND = "nosuchcountryfound";

	public static final String NOREGIONFOUND = "nosuchregionfound";

	public static final String NOBATTERYFOUND = "nosuchbatteryfound";

	public static final String NODUMETERFOUND = "nosuchdumeterfound";

	public static final String NOCONSUMERUSERFOUND = "nosuchconsumeruserfound";

	public static final String DATANOTFOUND="datanotfound";

	public static final String NOSITEFOUND="nosuchsitefound";

	public static final String DATAFETCHED = "datafetched";

	public static final String UPDATEDSUCCESSFULLY="successfullyupdated";

	public static final String ERRORWHILEUPDATING="errorwhileupdating";

	public static final String UNSUPPORTEDFILETYPE="unsupportedfiletype";

	public static final String ERRORWHILEFETCHING="errorwhilefetching";

	public static final String NOALERTS="noalerts";

	public static final String UNKNOWNERROR="unknownerror";
	
	public static final String RESOURCESPATH = "/WEB-INF/resources";
	
	public static final String MASTERFILEPATH = "routefiles";

	public static final String EPINSTALLATIONFILEPATH = "ep-installerfiles";
	
	public static final String DCINSTALLATIONFILEPATH = "dc-installerfiles";
	
	public static final String REPINSTALLATIONFILEPATH = "rep-installerfiles";
	
	public static final String TEMPFILEPATH = "temp";
	
	public static final String LAST24HRRECORDNOTFOUND = "Record Not Found Of Last 24Hours";

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	private ApplicationConstants(){

	}

	public static ApplicationConstants getAppConstObject(){
		if(appConstObj == null){
			appConstObj = new ApplicationConstants();
			return appConstObj;
		}
		return appConstObj;
	}

	// #fcc808 is Hex Code for Yellow colour
	public static final String NEWMESSAGE = "#fcc808";
	
	// #e60000 is Hex Code for Red colour
	public static final String REJECTEDMESSAGE = "#e60000";
	
	// #e67300 is Hex Code for Orange colour
	public static final String WAITINGMESSAGE = "#e67300";
	
	// #009900 is Hex Code for Green colour
	public static final String DONEMESSAGE = "#009900";
	
	// Free Report Constants
	
	public static final String DEFAULT="default";
	
	public static final String MONTH="month";
	
	public static final String WEEK="week";
	
	public static final String DATES="Select Dates";
	
}
