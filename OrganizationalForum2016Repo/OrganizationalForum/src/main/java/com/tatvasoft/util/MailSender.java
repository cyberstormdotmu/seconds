package com.tatvasoft.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

/**
 *	@author TatvaSoft
 * 	This class is email utility class for send an email to user
 *         for forgot password recovery functionality
 */
public class MailSender {
	
	/**
	 * This instance variable is for sender's emailId.
	 */
	final String senderEmailID = "urvashi.varsani@tatvasoft.com";
	
	/**
	 * This instance variable is for sender's Password.
	 */
	final String senderPassword = "ez*Dlvm1Z0";
	
	/**
	 * This instance variable is for email SMTP server.
	 */
	final String emailSMTPserver = "mail.tatvasoft.com";
	
	/**
	 * This instance variable is for sender's server port number.
	 */
	final String emailServerPort = "25";
	
	/**
	 * This instance variable is for receiver's emailId.
	 */
	String receiverEmailID = null;
	
	/**
	 * This instance variable is for email subject.
	 */
	String emailSubject = null;
	
	/**
	 * This instance variable is for email body.
	 */
	String emailBody = null;

	/*
	 * This method send email to entered email address with auto-generated
	 * password
	 */
	public MailSender(String receiverEmailID, String emailSubject,
			String emailBody) {

		this.receiverEmailID = receiverEmailID;
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;

		Properties props = new Properties();
		props.put("mail.smtp.user", senderEmailID);
		props.put("mail.smtp.host", emailSMTPserver);
		props.put("mail.smtp.port", emailServerPort);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", emailServerPort);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");

		try {
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			
			MimeMessage msg = new MimeMessage(session);
			msg.setText(emailBody);
			msg.setSubject(emailSubject);
			msg.setFrom(new InternetAddress(senderEmailID));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					receiverEmailID));
			Transport.send(msg);
		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}
	
	/* 
	 * 	This class authenticate SMTP protocol for sending mail
	 */
	public class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(senderEmailID, senderPassword);
		}
	}
}
