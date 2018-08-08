package com.kenure.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kenure.model.KDLEmailModel;

/**
 * 
 * This class provide Mail Sending functionality on 
 * 		new User creation and Forgot password 
 * 			functionality
 * 
 * @author TatvaSoft
 *
 */
@Component
public class MailSender {

	/*
	 * Logger object for checking Logs of this class.
	 */
	private Logger log = LoggerUtils.getInstance(MailSender.class);

	@Autowired
	private VelocityEngine velocityEngine;

	private HtmlEmail getEmailWithConfiguration(){
		HtmlEmail htmlEmail = new HtmlEmail();
		Properties prop = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties"); 

		try{
			if (input != null) {
				prop.load(input);
				htmlEmail.setHostName(prop.getProperty("mail.emailSMTPserver"));
				htmlEmail.setSslSmtpPort(prop.getProperty("mail.emailServerPort"));
				htmlEmail.setAuthenticator(new DefaultAuthenticator(prop.getProperty("mail.senderEmailID"), prop.getProperty("mail.senderPassword")));
				htmlEmail.setFrom((prop.getProperty("mail.senderEmailID")));
			} else {
				throw new FileNotFoundException("property file '" + input + "' not found in the classpath");
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return htmlEmail;
	}

	public boolean sendMail(String to, String subject,String[] body, String attachmentpath) throws Exception
	{
		try{
			//Properties of Email Configuration
			Properties prop = new Properties();


			InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties"); 

			if (input != null) {
				prop.load(input);
			} else {
				throw new FileNotFoundException("property file '" + input + "' not found in the classpath");
			}

			MultiPartEmail email = new MultiPartEmail();
			email.setHostName(prop.getProperty("mail.emailSMTPserver"));
			email.setSslSmtpPort(prop.getProperty("mail.emailServerPort"));
			email.setAuthenticator(new DefaultAuthenticator(prop.getProperty("mail.senderEmailID"), prop.getProperty("mail.senderPassword")));
			email.setFrom(prop.getProperty("mail.senderEmailID"));
			email.setSubject(subject);
			if(attachmentpath != null){
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(attachmentpath);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription("Please Find The attachment");
				attachment.setName(attachmentpath.substring(attachmentpath.lastIndexOf(File.separator)+1));
				email.attach(attachment);
			}

			if(body != null && !to.isEmpty() && !to.trim().isEmpty() && to != null)
			{
				String message="";

				for(int i=0;i<body.length;i++)
					message = message + body[i];

				email.setMsg(message);
				email.addTo(to);
				email.send();
			}
			else{

				log.warn("something goes wrong in email utils");
				return false;
			}

			return true;

		}catch(Exception e){
			log.warn(e.getMessage());
			return false;
		}
	}

	public void htmlEmailSender(String to, String subject,Map<String,KDLEmailModel> mailValue,String mailType,String attachmentpath){

		StringWriter stringWriter = new StringWriter();
		Template emailTemplate = null;
		VelocityContext velocityContext = null;

		List<Map<String, KDLEmailModel>> listMap = new ArrayList<Map<String,KDLEmailModel>>();
		listMap.add(mailValue);

		HtmlEmail htmlEmailWithConfig = getEmailWithConfiguration();
		htmlEmailWithConfig.setSubject(subject);
		try {
			htmlEmailWithConfig.addTo(to);
			if(attachmentpath != null && !attachmentpath.trim().isEmpty()){
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(attachmentpath);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription("Please Find The attachment");
				attachment.setName(attachmentpath.substring(attachmentpath.lastIndexOf(File.separator)+1));
				htmlEmailWithConfig.attach(attachment);
			}
		} catch (EmailException e1) {
			log.error(e1.getMessage());
		}

		if(mailType.equalsIgnoreCase("installer") || mailType.equalsIgnoreCase("site")){
			emailTemplate = velocityEngine.getTemplate("BluTower-Installer-Site.vm");
		}else if(mailType.equalsIgnoreCase("newAccount")){
			emailTemplate = velocityEngine.getTemplate("BluTower-NewAccount.vm");
		}else if (mailType.equalsIgnoreCase("forgotPassword")) {
			emailTemplate = velocityEngine.getTemplate("BluTower-ForgerPassword.vm");
		}else if(mailType.equalsIgnoreCase("endPointMail")){
			emailTemplate = velocityEngine.getTemplate("BluTower-EndpointAlert.vm");
		}else if(mailType.equalsIgnoreCase("dcAlert")){
			emailTemplate = velocityEngine.getTemplate("BluTower-TechnicianDCAlert.vm");
		}else if(mailType.equalsIgnoreCase("repeater")){
			emailTemplate = velocityEngine.getTemplate("BluTower-Repeater.vm");
		}
		// Load context with proper mailvalue type
		velocityContext = new VelocityContext(mailValue);
		// Merge Template With Field Data
		if(emailTemplate != null && mailValue != null){
			emailTemplate.merge(velocityContext, stringWriter);
			String content = stringWriter.toString();
			try {
				htmlEmailWithConfig.setHtmlMsg(content);
				htmlEmailWithConfig.send();
			} catch (EmailException e) {
				e.printStackTrace();
			}
		}
	}

}
