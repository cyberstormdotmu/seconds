package com.ishoal.sms.textmagic;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ishoal.sms.SmsService;
import com.textmagic.sdk.RestClient;
import com.textmagic.sdk.RestException;
import com.textmagic.sdk.resource.instance.TMNewMessage;

@RunWith(MockitoJUnitRunner.class)
public class TextMagicSmsServiceTest {

    @Mock
    private RestClient mockRestClient;

    @Mock
    private TMNewMessage mockNewMessage;

    private SmsService service;

    @Before
    public void before() {
        this.service = new TextMagicSmsService(this.mockRestClient);

        when(this.mockRestClient.getResource(TMNewMessage.class)).thenReturn(this.mockNewMessage);
    }

    @Test
    public void shouldReturnFalseIfTextServiceThrowsException() {
        whenSendOnMessageThrowsException();
        assertThat(sendSms(), is(false));
    }
    
    @Test
    public void shouldReturnFalseIfTextServiceDoesNotThrowException() {
        assertThat(sendSms(), is(true));
    }
    
    @Test
    public void shouldSendMessageToCorrectRecipient() {
        String recipient = randomMobileNumber();
        sendSmsTo(recipient);
        verify(this.mockNewMessage).setPhones(asList(recipient));
    }
    
    @Test
    public void shouldSendCorrectMessageText() {
        String text = randomTextMessage();
        sendSmsMessage(text);
        verify(this.mockNewMessage).setText(text);
    }
    
    private boolean sendSms() {
        return sendSmsTo(randomMobileNumber());
    }
    
    public boolean sendSmsTo(String recipient) {
        return sendSms(recipient, randomTextMessage());
    }
    
    public boolean sendSmsMessage(String message) {
        return sendSms(randomMobileNumber(), message);
    }
    
    public boolean sendSms(String recipient, String message) {
        return this.service.sendSms(recipient, message);
    }

    private String randomTextMessage() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

    private String randomMobileNumber() {
        return RandomStringUtils.randomNumeric(11);
    }

    private void whenSendOnMessageThrowsException() {
        try {
            when(this.mockNewMessage.send()).thenThrow(new RestException("message", Integer.valueOf(1)));
        } catch (RestException e) {
            throw new IllegalStateException("Won't happen here", e);
        }

    }
}