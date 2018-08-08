package com.ishoal.ws.buyer.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.util.ErrorType;
import com.ishoal.core.buyer.BuyerRegistrationService;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.email.EmailService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.*;
import com.ishoal.ws.buyer.adapter.CreateBuyerProfileRequestAdapter;
import com.ishoal.ws.buyer.dto.BuyerRegistrationDto;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/registration")
public class RegistrationController {
	@Value("${shoal.webRootUrl}")
	private String webRootUrl; 
	
	@Value("${shoal.mail.API_KEY}")
	private String apiKey;
	
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    
    private static final String API_KEY = " JYt50613HY95FGfSidwP6icuVXz7vo";
    private static final String SITE_SECRET = "6Lf8xjAUAAAAAM9AtBagSZHmpXeE0nARLFNCV0i4";
    private static final String SECRET_PARAM = "secret";
    private static final String RESPONSE_PARAM = "response";
    private static final String G_RECAPTCHA_RESPONSE = "g-recaptcha-response";
    private static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    
    @Autowired
    private EmailService SpringEmailService;
    
    private final BuyerRegistrationService buyerRegistrationService;
    private final CreateBuyerProfileRequestAdapter createBuyerProfileRequestAdapter = new
        CreateBuyerProfileRequestAdapter();
    
    public RegistrationController(BuyerRegistrationService buyerRegistrationService) {
    	
        this.buyerRegistrationService = buyerRegistrationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody BuyerRegistrationDto registration) {
    	String randomString = UUID.randomUUID().toString();
        logger.info("new buyer {} is registering for an account", registration.getBuyer().getEmailAddress());
        PayloadResult<BuyerProfile> result = buyerRegistrationService.registerBuyer(
            createBuyerProfileRequestAdapter.buildRequest(registration,randomString));
        
        if (result.isSuccess()) {
        	/*String htmlTemplate = "<img class='max-width' border='0' "
        			+ "style='display:block;color:#000000;text-decoration:none;font-family:Helvetica, arial, sans-serif;font-size:16px;max-width:60% !important;width:60%;height:auto !important;' "
        			+ "width='360' src='https://marketing-image-production.s3.amazonaws.com/uploads/254089d3c7d93243f3774c309e6859b66d8ee1ee9b2a17dfac84e54bbc410bdbcdfce0d10b76a5975f1ba2b86e58fee128769056dc757ba66ace0b37b7e16dcd.jpg' alt=''> "
        			+ "<div style='text-align: center;'><span style='font-size:22px;'><span style='font-family:trebuchet ms,helvetica,sans-serif;'>You're on your way!</span></span></div>"
        			+ "<div style='text-align: center;'>&nbsp;</div> "
        			+ "<div style='text-align: center;'><span style='font-size:22px;'><span style='font-family:trebuchet ms,helvetica,sans-serif;'>Let's confirm your email address.</span></span></div> "
        			+ "<div>&nbsp;</div> "
        			+ "<div>&nbsp;</div> "
        			+ "<a style='background-color:#0c1435;border:1px solid #333333;border-color:#0c1435;border-radius:6px;border-width:1px;color:#ffffff;display:inline-block;font-family:arial,helvetica,sans-serif;font-size:16px;font-weight:normal;letter-spacing:0px;line-height:16px;padding:12px 18px 12px 18px;text-align:center;text-decoration:none;' "
        			+ "href='" + webRootUrl + "/public/#/registration/confirm/"+ randomString + "' target='_blank'>Confirm Email Address</a> "
        			+ "<div style='text-align: center;'><span style='color:#9eb3c2;'><span style='font-family:trebuchet ms,helvetica,sans-serif;'>(Just so we know it's you.)</span></span></div> "
        			+ "<div> "
        			+ "<style type='text/css'>.sg-campaigns p.p1 {margin: 0.0px 0.0px 0.0px 0.0px; text-align: center; font: 12.0px Helvetica; color: #484848; -webkit-text-stroke: #484848} "
        			+ ".sg-campaigns span.s1 {font-kerning: none} "
        			+ ".sg-campaigns span.s2 {font: 10.0px Helvetica; font-kerning: none} "
        			+ "</style> "
        			+ "<p class='p1'><span style='font-size:10px;'><span style='font-family:trebuchet ms,helvetica,sans-serif;'><span class='s1'>© 2017 Casqol Ltd trading as Silverwing</span><span class='s1'>, All Rights Reserved.</span></span></span></p> "
        			+ "<p class='p1'><span style='font-size:10px;'><span style='font-family:trebuchet ms,helvetica,sans-serif;'><span class='s1'>243 High Street, Boston Spa, West Yorkshire, LS23 6AL</span></span></span></p> "
        			+ "</div>";
        	//String txt="To verify your account open the below link:"+webRootUrl+"/public/#/registration/confirm/"+randomString;
        	String subject="Welcome" + " " +registration.getBuyer().getFirstName()+" "+ registration.getBuyer().getSurname(); 
        	EmailMessage emailMessage = EmailMessage.anEmailMessage().subject(subject).recipient(registration.getBuyer().getEmailAddress()).text(htmlTemplate).build();
        	SpringEmailService.sendMessage(emailMessage);*/
        	
            try {
                OkHttpClient client = new OkHttpClient();
                String confirmLink = webRootUrl + "/public/#/registration/confirm/"+ randomString;
                
                MediaType mediaType = MediaType.parse("application/json");
                com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "{\r\n" + 
                        "  \"personalizations\": [\r\n" + 
                        "    {\r\n" + 
                        "      \"to\": [\r\n" + 
                        "        {\r\n" + 
                        "          \"email\": \""+ registration.getBuyer().getEmailAddress() +"\",\r\n" + 
                        "          \"name\": \""+ registration.getBuyer().getFirstName() +"\"\r\n" + 
                        " }     "+
                        
                        
                        "      ],\r\n" + 
                        "      \"substitutions\":{  \r\n" + 
                        "          \"-confirmEmailLink-\":\""+ confirmLink +"\"         \r\n" +
                        "         },\r\n" + 
                        "      \"subject\": \"Confirm Email !\"\r\n" + 
                        "    }\r\n" + 
                        "  ],\r\n" + 
                        "  \"from\": {\r\n" + 
                        "    \"email\": \"help@silverwing.co\",\r\n" + 
                        "    \"name\": \"Ashton Squires\"\r\n" + 
                        "  },\r\n" + 
                        "  \"subject\": \"Confirm Email !\",\r\n" + 
                        "  \"template_id\":\"a3a80f36-d001-4694-b10a-1d8e3faceb04\"\r\n" + 
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
                
                System.out.println(">>>>>>>>>>>>" + response.code());
                
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return ResponseEntity.ok().body(result);
        } else {
            return generateErrorResponse(result);
        }
    }
    private ResponseEntity<?> generateErrorResponse(PayloadResult<BuyerProfile> result) {

        if (result.getErrorType().equals(ErrorType.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorInfo.badRequest(result.getError()));
        } else {
            return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
    }
    
    @RequestMapping(value="/verifyCaptcha" ,method = RequestMethod.POST)
    public ResponseEntity<?> varifyCaptcha(@RequestParam("key") String  recaptchaResponseToken) {
        
        ResponseEntity<?> responseEntity = null;
        try {
            JSONObject jsonObject = performRecaptchaSiteVerify(recaptchaResponseToken);
            boolean success = jsonObject.getBoolean("success");
            System.out.println(" >>>>>> "+ success +" <<<<<<< ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return responseEntity;
    }
    
    private JSONObject performRecaptchaSiteVerify(String recaptchaResponseToken)
            throws IOException {
        URL url = new URL(SITE_VERIFY_URL);
        StringBuilder postData = new StringBuilder();
        addParam(postData, SECRET_PARAM, SITE_SECRET);
        addParam(postData, RESPONSE_PARAM, recaptchaResponseToken);

        return postAndParseJSON(url, postData.toString());
    }

    private JSONObject postAndParseJSON(URL url, String postData) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty(
                "charset", StandardCharsets.UTF_8.displayName());
        urlConnection.setRequestProperty(
                "Content-Length", Integer.toString(postData.length()));
        urlConnection.setUseCaches(false);
        urlConnection.getOutputStream()
                .write(postData.getBytes(StandardCharsets.UTF_8));
        JSONTokener jsonTokener = new JSONTokener(urlConnection.getInputStream());
        return new JSONObject(jsonTokener);
    }

    private StringBuilder addParam(
            StringBuilder postData, String param, String value)
            throws UnsupportedEncodingException {
        if (postData.length() != 0) {
            postData.append("&");
        }
        return postData.append(String.format("%s=%s", URLEncoder.encode(param, StandardCharsets.UTF_8.displayName()),
                    URLEncoder.encode(value, StandardCharsets.UTF_8.displayName())));
    }
    
}
