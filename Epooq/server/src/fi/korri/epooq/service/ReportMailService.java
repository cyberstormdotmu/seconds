package fi.korri.epooq.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataSource;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 
 * @author Tatvasoft
 *
 */
public class ReportMailService
{
	@Autowired
	static ReloadableResourceBundleMessageSource message;

	
	private final Logger log = Logger.getLogger(ReportMailService.class.getName());
	static Properties properties=new Properties();
	
	static String HOST_NAME ;
	static int SMTP_PORT ;//Integer.parseInt(properties.getProperty("smtp_port"));
	static String USERNAME;
	static String PASSWORD;
	static String TO;
	static String CC="";
	static String BCC="";
	static String TEXT="Body:\n\nHi,\n \n \nPlease find attached the Daily Report for Appointment Scheduled Report for ";

	public static void loadProperties(){
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("config.properties");
			HOST_NAME=properties.getProperty("host_name");
			SMTP_PORT=Integer.parseInt(properties.getProperty("smtp_port"));
			USERNAME = properties.getProperty("username");
			PASSWORD = properties.getProperty("password");
			TO=properties.getProperty("to");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Java method implementation for sending email when application fails.
	 * it shows exception stack trace and its URL
	 *  
	 */
	public void execute(String subject,String message,String url) throws Exception{
		loadProperties();
		sendMail(TO, CC, BCC, subject, "Requested URL :-  "+url+"\n\n"+message, null);
	}
	
	
	public void executeMail(String emailid,String subject,String message) throws Exception{
		loadProperties();
		sendMail(emailid, CC, BCC, subject, message, null);
	}
	
	
	/**
	 * Java method implementation for 'Send e-mail of generated reports'.
	 *  
	 */
	public void execute(Map<String, DataSource> emailAttachmentList,String date) throws Exception {
		loadProperties();
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
		loadProperties();
		HtmlEmail email = new HtmlEmail();
		
		// Set body text of E-Mail
		email.setHtmlMsg(text);
		
		// Set From address
		email.setFrom(USERNAME);
		
		// Add To address
		String[] toAddress = splitAndTrim(to);
		if(toAddress != null)
		{
			for(String tempTo : toAddress)
			{
				if(!tempTo.isEmpty())
				email.addTo(tempTo);
			}
		}
		
		// Add Cc address
		String[] ccAddress = splitAndTrim(cc);
		if(ccAddress != null)
		{
			for(String tempCc : ccAddress)
			{
				if(!(tempCc.isEmpty()))
				email.addCc(tempCc);
			}
		}
		
		// Add Bcc address
		String[] bccAddress = splitAndTrim(bcc);
		if(bccAddress != null)
		{
			for(String tempBcc : bccAddress)
			{
				if(!(tempBcc.isEmpty()))
				email.addBcc(tempBcc);
			}
		}
		
		// Add subject
		email.setSubject(subject);
		
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
		log.debug("mail sent successfully");
		
		
	}
	
	/**
	 * This method splits the string by ',' character and trims particular splitted string & returns array of string.
	 * 
	 * @param str - Input String
	 * @return String[] - String array of splitted and trimmed strings
	 */
	private String[] splitAndTrim(String str)
	{
		loadProperties();
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
