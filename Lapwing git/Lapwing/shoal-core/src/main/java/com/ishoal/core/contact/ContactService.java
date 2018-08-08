package com.ishoal.core.contact;

import static com.ishoal.email.EmailMessage.anEmailMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.ishoal.core.domain.ContactUs;
import com.ishoal.core.repository.ContactUsRepository;
import com.ishoal.email.EmailMessage;
import com.ishoal.email.EmailService;

public class ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    
    @Value("${shoal.mail.API_KEY}")
	private String apiKey;
    
    @Value("${shoal.mail.username}")
	private String adminEmail;
    
    @Value("${shoal.mail.name}")
	private String adminName;

    private final ContactUsRepository contactUsRepository;
    private final EmailService emailService;
    private final String recipientEmailAddress;

    public ContactService(EmailService emailService, String recipientEmailAddress, ContactUsRepository contactUsRepository) {
        this.emailService = emailService;
        this.recipientEmailAddress = recipientEmailAddress;
        this.contactUsRepository = contactUsRepository;
    }

    public void sendContactRequest(String name, String companyName, String emailAddress, String phoneNumber, String message, String messageType ) {
        String text = String.format("<p>Name:" + name + "</p> <p>Company:"+companyName+"</p> <p>Email address:"+emailAddress+"</p> <p>Phone number:"+phoneNumber+"</p> <p>Message:"+message+"</p>");

        /*EmailMessage emailMessage = anEmailMessage()
                .recipient(emailAddress)
                .subject("Shoal Website Contact Form Submission")
                .text(text)
                .build();

        logger.debug("Sending contact form submission email [{}]", emailMessage);

        emailService.sendMessage(emailMessage);*/
    	try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "{\r\n" + 
                    "  \"personalizations\": [\r\n" + 
                    "    {\r\n" + 
                    "      \"to\": [\r\n" + 
                    "        {\r\n" + 
                    "          \"email\": \""+ emailAddress +"\",\r\n" + 
                    "          \"name\": \""+ name +"\"\r\n}" + 
                    "      ],\r\n" + 
                    "      \"substitutions\":{  \r\n" + 
                    "          \"-userName-\":\""+ name +"\",         \r\n" +
                    "          \"-Messagecontent-\":\""+ text +"\"         \r\n" +
                    "         },\r\n" + 
                    "      \"subject\": \"Confirm Email !\"\r\n" + 
                    "    }\r\n" + 
                    "  ],\r\n" + 
                    "  \"from\": {\r\n" + 
                    "    \"email\": \""+ adminEmail +"\",\r\n" + 
                    "    \"name\": \""+ adminName +"\"\r\n" + 
                    "  },\r\n" + 
                    "  \"subject\": \" Contact us query.\",\r\n" + 
                    "  \"template_id\":\"d2ca9f88-4000-42d6-a987-ff34e74cf995\"\r\n" + 
                    "\r\n" +                
                    "}");
            Request request = new Request.Builder()
              .url("https://api.sendgrid.com/v3/mail/send")
              .post(body)
              .addHeader("authorization", "Bearer " + apiKey)
              .addHeader("content-type", "application/json")
              .build();

            Response response = client.newCall(request).execute();
            
            /*
             *  response.code() method gives response code :
             *  Ex.
             *  202 :- Accepted / Success
             *  401 :- Unauthorized
             *  404 :- Not Found etc.
             *  
             *  response.message() method gives message :
             *  Ex.
             *  "Accepted" if mail sent
             *  
             *  response.isSuccessful() method gives boolean response :
             *  Ex. 
             *  true if successful
             */
            
            System.out.println(">>>>>>>>>>>>" + response.code() + "\n" + response.body().string());
            
            } catch (IOException e) {
                e.printStackTrace();
            }

        saveContactRequest(name, companyName, emailAddress, phoneNumber, message, messageType);
    }
    
    public void saveContactRequest(String name, String companyName, String emailAddress, String mobileNumber, String message, String messageType) {
    	
    	String text = null;
    	String emailSubject = null;
    	
        ContactUs contactUs = ContactUs.aContactUs()
        								.companyName(companyName)
        								.name(name)
        								.emailAddress(emailAddress)
        								.mobileNumber(mobileNumber)
        								.message(message)
        								.messageType(messageType)
        								.build();
        contactUsRepository.saveContactRequest(contactUs);
        
        if(messageType.equals("Contact_Us")) {
        	text = String.format( "Query is Raised by one user. Please check below query.<p>Name:" + name + "</p> <p>Company:"+companyName+"</p> <p>Email address:"+emailAddress+"</p> <p>Phone number:"+mobileNumber+"</p> <p>Message:"+message+"</p>");
        	
        	emailSubject = "Contact us query raised";
        }
        else {
        	text = String.format( "Problem is reported by one user.Please check below problem.<p>Name:" + name + "</p> <p>Company:"+companyName+"</p> <p>Email address:"+emailAddress+"</p> <p>Phone number:"+mobileNumber+"</p> <p>Message:"+message+"</p>",
                    name, companyName, emailAddress, mobileNumber, message);
        	emailSubject = "Get in touch query raised";
		}
        
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "{\r\n" + 
                    "  \"personalizations\": [\r\n" + 
                    "    {\r\n" + 
                    "      \"to\": [\r\n" + 
                    "        {\r\n" + 
                    "          \"email\": \""+ adminEmail +"\",\r\n" + 
                    "          \"name\": \""+ " " +"\"\r\n}" +                     
                    "      ],\r\n" + 
                    "      \"substitutions\":{  \r\n" + 
                    "          \"-userName-\":\""+ name +"\",         \r\n" +
                    "          \"-Messagecontent-\":\""+ text +"\"         \r\n" +
                    "         },\r\n" + 
                    "      \"subject\": \""+ emailSubject +"\"\r\n" + 
                    "    }\r\n" + 
                    "  ],\r\n" + 
                    "  \"from\": {\r\n" + 
                    "    \"email\": \""+ adminEmail +"\",\r\n" + 
                    "    \"name\": \""+ adminName +"\"\r\n" + 
                    "  },\r\n" + 
                    "  \"subject\": \"Confirm Email !\",\r\n" + 
                    "  \"template_id\":\"d2ca9f88-4000-42d6-a987-ff34e74cf995\"\r\n" + 
                    "\r\n" + 
                    "}");
            Request request = new Request.Builder()
              .url("https://api.sendgrid.com/v3/mail/send")
              .post(body)
              .addHeader("authorization", "Bearer " + apiKey)
              .addHeader("content-type", "application/json")
              .build();

            Response response = client.newCall(request).execute();
            logger.debug("Sending contact form submission email [{}]", response);
            /*
             *  response.code() method gives response code :
             *  Ex.
             *  202 :- Accepted / Success
             *  401 :- Unauthorized
             *  404 :- Not Found etc.
             *  
             *  response.message() method gives message :
             *  Ex.
             *  "Accepted" if mail sent
             *  
             *  response.isSuccessful() method gives boolean response :
             *  Ex. 
             *  true if successful
             */
            
            System.out.println(">>>>>>>>>>>>" + response.code()  + "\n" + response.message() );
            
            } catch (IOException e) {
                e.printStackTrace();
            }
        /*EmailMessage emailMessage = anEmailMessage()
                .recipient("nimesh.patel@tatvasoft.com")
                .subject("Silverwing Website Contact Form Submission")
                .text(text)
                .build();*/
    }
}
