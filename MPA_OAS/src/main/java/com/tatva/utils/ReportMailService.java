package com.tatva.utils;

import java.util.Map;

import javax.activation.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

/**
 * 
 * @author TARKIK
 *
 */
public class ReportMailService
{
	final String HOST_NAME = "mail.tatvasoft.com";
	final int SMTP_PORT = 25;
	final String USERNAME = "sushant.banugariya@tatvasoft.com";
	final String PASSWORD = "SushantB272";
	final String TO="sushant.banugariya@tatvasoft.com";
	final String CC="";
	final String BCC="";
	String TEXT="Body:\n\nHi,\n \n \nPlease find attached the Daily Report for Appointment Scheduled Report for ";

	/**
	 * Java method implementation for sending email when application fails.
	 * it shows exception stack trace and its URL
	 *  
	 */
	public void execute(String subject,String message,String url) throws Exception{
		sendMail(TO, CC, BCC, subject, "Requested URL :-  "+url+"\n\n"+message, null);
	}
	
	
	public void executeMail(String emailid,String subject,String message) throws Exception{
		sendMail(emailid, CC, BCC, subject, message, null);
	}
	
	
	/**
	 * Java method implementation for 'Send e-mail of generated reports'.
	 *  
	 */
	public void execute(Map<String, DataSource> emailAttachmentList,String date) throws Exception {
		TEXT +=date+ " \n \n \n Thank You";
		System.out.println("text...   "+TEXT);
		sendMail(TO, CC, BCC, "Daily Reports", TEXT, emailAttachmentList);
	}
	
	/**
	 * This method sends email of reports as attachments to specified To, Cc & Bcc with specified subject & body text.
	 * 
	 * @param to - To address
	 * @param cc - Cc address
	 * @param bcc - Bcc address
	 * @param subject - Subject of the E-mail
	 * @param text - Body text for e-mail
	 * @param emailAttachmentList - Map of reports
	 * @throws EmailException
	 */
	private void sendMail(String to, String cc, String bcc, String subject, 
						  String text,Map<String, DataSource> emailAttachmentList) throws EmailException
	{
		MultiPartEmail email = new MultiPartEmail();
		
		// Set body text of E-Mail
		email.setMsg(StringUtils.defaultIfEmpty(text, ""));
		
		// Set From address
		email.setFrom(USERNAME);
		
		// Add To address
		String[] toAddress = splitAndTrim(to);
		if(toAddress != null)
		{
			for(String tempTo : toAddress)
			{
				if(!StringUtils.isBlank(tempTo))
				email.addTo(tempTo);
			}
		}
		
		// Add Cc address
		String[] ccAddress = splitAndTrim(cc);
		if(ccAddress != null)
		{
			for(String tempCc : ccAddress)
			{
				if(!StringUtils.isBlank(tempCc))
				email.addCc(tempCc);
			}
		}
		
		// Add Bcc address
		String[] bccAddress = splitAndTrim(bcc);
		if(bccAddress != null)
		{
			for(String tempBcc : bccAddress)
			{
				if(!StringUtils.isBlank(tempBcc))
				email.addBcc(tempBcc);
			}
		}
		
		// Add subject
		email.setSubject(StringUtils.defaultIfEmpty(subject, ""));
		
		// Add attachments
		if(emailAttachmentList!=null){
			for(String key : emailAttachmentList.keySet())
			{
				DataSource ds = emailAttachmentList.get(key);
				email.attach(ds, key, "");
			}
		}
		// Set mail server properties
		email.setHostName(HOST_NAME);
		email.setSmtpPort(SMTP_PORT);
		email.setAuthentication(USERNAME, PASSWORD);
		
		// Send mail
		email.send();
		
	}
	
	/**
	 * This method splits the string by ',' character and trims particular splitted string & returns array of string.
	 * 
	 * @param str - Input String
	 * @return String[] - String array of splitted and trimmed strings
	 */
	private String[] splitAndTrim(String str)
	{
		if (str != null) 
		{
			String[] splittedStrings = str.split(",");
			
			for (int i = 0; i < splittedStrings.length; i++) 
			{
				splittedStrings[i] = splittedStrings[i].trim();
			}
			
			return splittedStrings;
		}
		
		return null;
	}

}
