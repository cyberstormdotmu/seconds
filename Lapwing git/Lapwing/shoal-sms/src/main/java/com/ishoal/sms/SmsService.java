package com.ishoal.sms;

public interface SmsService {
    boolean sendSms(String recipient, String message);
}
