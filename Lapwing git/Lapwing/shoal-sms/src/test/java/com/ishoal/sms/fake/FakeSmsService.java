package com.ishoal.sms.fake;

import com.ishoal.sms.SmsService;

public class FakeSmsService implements SmsService {

    @Override
    public boolean sendSms(String recipient, String message) {
        return true;
    }
}
