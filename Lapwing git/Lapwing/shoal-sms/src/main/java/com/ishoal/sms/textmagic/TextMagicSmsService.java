package com.ishoal.sms.textmagic;

import com.ishoal.sms.SmsService;
import com.textmagic.sdk.RestClient;
import com.textmagic.sdk.RestException;
import com.textmagic.sdk.resource.instance.TMNewMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

public class TextMagicSmsService implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(TextMagicSmsService.class);

    @Value("${shoal.sms.enabled}")
	private boolean smsEnabled;
    
    private final RestClient client;

    public TextMagicSmsService(RestClient client) {
        this.client = client;
    }

    @Override
    public boolean sendSms(String recipientMobileNumber, String messageText) {
    	System.out.println(smsEnabled);
    	if(smsEnabled)
    	{
        try {
            TMNewMessage message = client.getResource(TMNewMessage.class);
            message.setText(messageText);
            message.setPhones(Arrays.asList(recipientMobileNumber));
            message.send();
            return true;
        } catch (RestException e) {
            logger.error("Failed to send SMS message to [{}]", recipientMobileNumber, e);
            return false;
        }
    	}
    	else{
    		/*try {*/
                TMNewMessage message = client.getResource(TMNewMessage.class);
                message.setText(messageText);
                message.setPhones(Arrays.asList(recipientMobileNumber));
                /*message.send();*/
                return true;
                /* } catch (RestException e) {
                logger.error("Failed to send SMS message to [{}]", recipientMobileNumber, e);
                return false;
            }*/
    	}
    }
}