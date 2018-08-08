package com.ishoal.sms.dummy;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.ishoal.sms.SmsService;


@RunWith(MockitoJUnitRunner.class)
public class LoggingSmsServiceTest {

    @Mock
    private Logger mockLogger;

    private SmsService service;

    @Before
    public void before() {
        this.service = new LoggingSmsService(this.mockLogger);
    }

    @Test
    public void shouldLogDetailsOfTextAndRecipient() {
        this.service.sendSms("07979123123", "Password reset code");
        verify(this.mockLogger).info("Would have sent SMS to [{}] with message [{}]", "07979123123",
                "Password reset code");
    }
}