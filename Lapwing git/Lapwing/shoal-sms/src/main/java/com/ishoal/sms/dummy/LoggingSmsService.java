package com.ishoal.sms.dummy;

import com.ishoal.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingSmsService implements SmsService {

    private final Logger logger;
    
    public LoggingSmsService() {
        this(LoggerFactory.getLogger(LoggingSmsService.class));
    }
    
    public LoggingSmsService(Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public boolean sendSms(String recipient, String messageText) {
        logger.info("Would have sent SMS to [{}] with message [{}]", recipient, messageText);
        return true;
    }
}