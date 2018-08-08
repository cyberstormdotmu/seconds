package com.ishoal.email.spring;

import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.ishoal.email.EmailMessage;
import com.ishoal.email.EmailService;

public class SpringEmailService implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(SpringEmailService.class);

	public static final String DEFAULT_FROM_ADDRESS = "noreply@the-shoal.com";

	private final JavaMailSenderImpl javaMailSenderImpl;

	public SpringEmailService(JavaMailSenderImpl javaMailSenderImpl) {

		this.javaMailSenderImpl = javaMailSenderImpl;
	}

	@Override
	public void sendMessage(EmailMessage message) {

		JavaMailSenderImpl mailMessage = buildSpringMailMessage(message);

		//sendEmailUsingSpringMailSender(mailMessage);
		System.out.println(">>>>>>>>>>> " +message.getRecipient().getAddress() +" <<<<<<<<");
		logger.error("sent an email to {}", message.getRecipient().getAddress());
	}

	private JavaMailSenderImpl buildSpringMailMessage(EmailMessage message) {
		/*Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.socketFactory.port", 587);
        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.socketFactory.fallback", "false");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        sender.setJavaMailProperties(mailProperties);
        
        
        sender.setHost("smtp.gmail.com");
        sender.setPort(587);
        sender.setUsername("sarthakpatel10@gmail.com");
        sender.setPassword("8758481734");*/
		
		MimeMessage message2 = javaMailSenderImpl.createMimeMessage();

		// use the true flag to indicate you need a multipart message
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message2, true);
			helper.setFrom(DEFAULT_FROM_ADDRESS);
		
			helper.setSubject(message.getSubject());
			helper.setTo(message.getRecipient().toString());
			
	
			// use the true flag to indicate the text included is HTML
			helper.setText(message.getText(), true);
			
			javaMailSenderImpl.send(message2);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return javaMailSenderImpl;
		
		
		/*SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSentDate(new Date());
		mailMessage.setFrom(DEFAULT_FROM_ADDRESS); 
		mailMessage.setTo(message.getRecipient().toString());
		//mailMessage.setBcc(message.getRecipient().toString());
		//mailMessage.setCc(message.getRecipient().toString());
		mailMessage.setSubject(message.getSubject());
		mailMessage.setText(message.getText());
		return mailMessage;*/
	}

	/*private void sendEmailUsingSpringMailSender(SimpleMailMessage mailMessage) {

		try {
			mailSender.send(mailMessage);
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}*/
}
