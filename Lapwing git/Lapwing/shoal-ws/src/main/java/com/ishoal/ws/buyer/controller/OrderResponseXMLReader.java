package com.ishoal.ws.buyer.controller;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.User;
import com.ishoal.core.orders.OrderConfirmationService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@RestController
@Configuration
@Component
public class OrderResponseXMLReader {

    private static final Logger logger = LoggerFactory.getLogger(OrderResponseXMLReader.class);
    
    private final OrderConfirmationService orderService;
    
    public OrderResponseXMLReader(OrderConfirmationService orderService){
        this.orderService = orderService;
    }
    
    @Value("${shoal.mail.API_KEY}")
    private String apiKey;
    
    @Value("${shoal.westcoast.sftp.username}")
    private String username;
    
    @Value("${shoal.westcoast.sftp.url}")
    private String url;
    
    @Value("${shoal.westcoast.sftp.port}")
    private String port;
    
    @Value("${shoal.westcoast.sftp.password}")
    private String password;
    
    @Value("${shoal.westcoast.sftp.downloadpath}")
    private String downloadpath;
    
    @Value("${shoal.westcoast.sftp.response.downloadpath}")
    private String responseDownloadpath;
    
    @Value("${shoal.mail.name}")
    private String adminName;
    
    @Value("${shoal.mail.username}")
    private String adminEmail;
    
    //@Scheduled(fixedRate = 2000*60)
    public void readOrderResponseXML(){
        
        Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, url, Integer.parseInt(port));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            try {
                //sftpChannel.get("/UAT/outbound/ORDER_RESPONSE.xml", "D:\\LAPWING\\ORDER_RESPONSE.xml");
                Vector filelist = sftpChannel.ls(downloadpath);
                for(int i=0; i<filelist.size();i++){
                    LsEntry entry = (LsEntry) filelist.get(i);
                    
                    if(!entry.getFilename().toString().trim().equalsIgnoreCase("..") && !entry.getFilename().toString().trim().equalsIgnoreCase(".")){
                        
                        if(entry.getFilename().toString().toLowerCase().contains("ack")){
                            
                            sftpChannel.get(downloadpath +entry.getFilename().toString(), responseDownloadpath + entry.getFilename().toString());
                            logger.info("Reading file with name : " + responseDownloadpath + entry.getFilename().toString());
                            
                            File inputFile = new File(responseDownloadpath + entry.getFilename().toString());
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            Document doc = dBuilder.parse(inputFile);
                            doc.getDocumentElement().normalize();
                            
                         // for Error Status tag
                            NodeList errorStatusTagList = doc.getElementsByTagName("ErrorStatus");
                            Node errorStatusNode = errorStatusTagList.item(0);
                            Element errorStatusElement = (Element) errorStatusNode;
                            
                            String errorStatus = errorStatusTagList.item(0).getTextContent().toString();
                            String errorNumber = errorStatusElement.getAttribute("ErrorNumber").toString();
                            
                            logger.info("ErrorStatus >>>>>>>>>>>> " + errorStatusTagList.item(0).getTextContent());
                            logger.info("ErrorNumber >>>>>>>>>>>> " + errorStatusElement.getAttribute("ErrorNumber"));
                            
                            // for CustomerPO tag
                            NodeList customerPOTagList = doc.getElementsByTagName("CustomerPO");
                            logger.info("CustomerPO >>>>>>>>>>>> " + customerPOTagList.item(0).getTextContent());
                            
                            if(errorStatus == "" && errorNumber == ""){
                                String orderRef = customerPOTagList.item(0).getTextContent().toString();
                                boolean isMailSent = sendAcknowledgementMail(orderRef);
                                
                                if(isMailSent){
                                    logger.info("Confirm email sent successfully.");
                                }else{
                                    logger.info("Error while sending mail.");
                                }   
                            }
                            
                            logger.info("Deleting file...");
                            sftpChannel.rm(downloadpath +entry.getFilename());
                            logger.info("File successfully deleted.");
                            
                        }else if(entry.getFilename().toString().toLowerCase().contains("des")){
                            
                            sftpChannel.get(downloadpath +entry.getFilename().toString(), responseDownloadpath + entry.getFilename().toString());
                            logger.info("Reading file with name : " + responseDownloadpath + entry.getFilename().toString());
                            
                            File inputFile = new File(responseDownloadpath + entry.getFilename().toString());
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            Document doc = dBuilder.parse(inputFile);
                            doc.getDocumentElement().normalize();
                            
                         // for Error Status tag
                            NodeList errorStatusTagList = doc.getElementsByTagName("ErrorStatus");
                            Node errorStatusNode = errorStatusTagList.item(0);
                            Element errorStatusElement = (Element) errorStatusNode;
                            
                            String errorStatus = errorStatusTagList.item(0).getTextContent().toString();
                            String errorNumber = errorStatusElement.getAttribute("ErrorNumber").toString();
                            
                            logger.info("ErrorStatus >>>>>>>>>>>> " + errorStatusTagList.item(0).getTextContent());
                            logger.info("ErrorNumber >>>>>>>>>>>> " + errorStatusElement.getAttribute("ErrorNumber"));
                            
                            // for CustomerPO tag
                            NodeList customerPOTagList = doc.getElementsByTagName("CustomerPO");
                            logger.info("CustomerPO >>>>>>>>>>>> " + customerPOTagList.item(0).getTextContent());   
                            
                            if(errorStatus == "" && errorNumber == ""){
                                String orderRef = customerPOTagList.item(0).getTextContent().toString();
                                boolean isMailSent = sendOrderConfirmationMail(orderRef);
                                
                                if(isMailSent){
                                    logger.info("Confirm email sent successfully.");
                                }else{
                                    logger.info("Error while sending mail.");
                                }   
                            }
                            
                            logger.info("Deleting file...");
                            sftpChannel.rm(downloadpath +entry.getFilename());
                            logger.info("File successfully deleted.");
                            
                        }else{
                            // future code here
                        }
                        
                    }
                    
                }
                logger.info("Done Reading : " + filelist.size());
            } catch(Exception e) {
                if(e.getMessage().contains("No such file")) {
                    logger.info("No such file is available.");
                }else {
                    e.printStackTrace();
                    logger.info("Error while fetching file.");
                }
            }
            //sftpChannel.put("D:\\EDLMetadata.xsd", "/usr/test/EDLMetadata.xsd");
            
            sftpChannel.exit();
        } catch (JSchException e) {
            e.printStackTrace();
            logger.info("Error while reading data.");
        }
        System.out.println("------");
        session.disconnect();
    }
    
    public boolean sendOrderConfirmationMail(String orderRef){
        
        Order order = orderService.fetchOrder(OrderReference.from(orderRef));
        
        boolean result = false;
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "{\r\n" + 
                    "  \"personalizations\": [\r\n" + 
                    "    {\r\n" + 
                    "      \"to\": [\r\n" + 
                    "        {\r\n" + 
                    "          \"email\": \""+ order.getBuyer().getUsername() +"\",\r\n" +
                    "          \"name\": \""+ order.getBuyer().getForename() + " " + order.getBuyer().getSurname() +"\"\r\n" + 
                    "        }\r\n" + 
                    "      ],\r\n" + 
                    "      \"substitutions\":{  \r\n" + 
                    "          \"-firstName-\":\""+ order.getBuyer().getForename() +"\"         \r\n" +
                    "          \"-orderReference-\":\""+ order.getReference() +"\"         \r\n" +
                    "          \"-supplier-\":\""+ order.getVendor().getName() +"\"         \r\n" +
                    "          \"-silverwingCreditsUsed-\":\""+ order.calculateAppliedLapwingCreditsforOrder() +"\"         \r\n" +
                    "          \"-amountDue-\":\""+ order.getUnpaidAmount() +"\"         \r\n" +
                    "          \"-deliveryAddress-\":\""+ order.getDeliveryAddress().toString() +"\"         \r\n" +
                    "          \"-billlingAddress-\":\""+ order.getInvoiceAddress().toString() +"\"         \r\n" +
                    "          \"-amountPaid-\":\""+ order.getAmountPaid() +"\"         \r\n" +
                    "          \"-totalIncVAT-\":\""+ order.getUnpaidAmount().add(order.getAmountPaid()) +"\"         \r\n" +
                    "          \"-VATAmountInPounds-\":\""+ order.getTotal().getVat() +"\"         \r\n" +
                    "          \"-subtotalExVAT-\":\""+ order.getTotal().getNet() +"\"         \r\n" +
                    "          \"-quanity-\":\""+ order.calculateTotalQuantitesOrdered() +"\"         \r\n" +
                    "          \"-productName-\":\""+ order.getLines().list().get(0).getProduct().getName() +"\"         \r\n" +
                    "         },\r\n" +
                    "      \"subject\": \"Confirm Emai !\"\r\n" + 
                    "    }\r\n" + 
                    "  ],\r\n" + 
                    "  \"from\": {\r\n" + 
                    "    \"email\": \""+  adminEmail +"\",\r\n" + 
                    "    \"name\": \"Ashton Squires\"\r\n" + 
                    "  },\r\n" + 
                    "  \"subject\": \"Your Order is Confirmed !\",\r\n" + 
                    "  \"template_id\":\"1a64a944-5471-4f15-b00c-321c47b3c35f\"\r\n" + 
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
            
            if(response.isSuccessful()){
                result = true;
                logger.info("Mail sent successfully.");
            }else{
                logger.info("Error while sending the email.");
            }
                
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        return result;
    }
    
    public boolean sendAcknowledgementMail(String orderRef){
        
        boolean result = false;
        Order order = orderService.fetchOrder(OrderReference.from(orderRef));
        try {
            OkHttpClient client = new OkHttpClient();
            String userEmail = order.getBuyer().getUsername();
            String userName = order.getBuyer().getForename() + " " + order.getBuyer().getSurname();
            
            MediaType mediaType = MediaType.parse("application/json");
            com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "{\r\n" + 
                    "  \"personalizations\": [\r\n" + 
                    "    {\r\n" + 
                    "      \"to\": [\r\n" + 
                    "        {\r\n" + 
                    "          \"email\": \""+ userEmail +"\",\r\n" + 
                    "          \"name\": \""+ userName +"\"\r\n" + 
                    "        }\r\n" + 
                    "      ],\r\n" + 
                    "      \"substitutions\":{  \r\n" + 
                    "          \"-suppliername-\":\""+ order.getBuyer().getVendor().getName() +"\"         \r\n" +
                    "          \"-OrderReference-\":\""+ orderRef +"\"         \r\n" +
                    "          \"-firstname-\":\""+ order.getBuyer().getForename() +"\"         \r\n" +
                    "          \"-insertSupplierName-\":\""+ order.getBuyer().getVendor().getName() +"\"         \r\n" +
                    "         },\r\n" + 
                    "      \"subject\": \"Confirm Emai !\"\r\n" + 
                    "    }\r\n" + 
                    "  ],\r\n" + 
                    "  \"from\": {\r\n" + 
                    "    \"email\": \""+  adminEmail +"\",\r\n" + 
                    "    \"name\": \""+  adminName +"\",\r\n" + 
                    "  },\r\n" + 
                    "  \"subject\": \"Your Silver Wing Account Was Approved !\",\r\n" + 
                    "  \"template_id\":\"c335248d-4122-473d-ae8c-5ff79bfd9f17\"\r\n" + 
                    "\r\n" + 
                    "}");
            Request request = new Request.Builder()
              .url("https://api.sendgrid.com/v3/mail/send")
              .post(body)
              .addHeader("authorization", "Bearer " + apiKey)
              .addHeader("content-type", "application/json")
              .build();

            Response emailResponse = client.newCall(request).execute();
            
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
            
            if(emailResponse.isSuccessful()){
                result = true;
                logger.info("Mail sent successfully.");
            }else{
                logger.info("Error while sending the email.");
            }
            
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        return result;
    }
    
}
