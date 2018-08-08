package com.ishoal.core.contact;

import com.ishoal.email.EmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ContactServiceTest {

    @Mock
    private EmailService emailService;

    private String recipientEmailAddress = "contactus@ishoal.com";

    private ContactService service;

    /*@Before
    public void before() {
        service = new ContactService(emailService, recipientEmailAddress);
    }*/

    @Test
    public void theServiceCallsTheEmailService() {
        service.sendContactRequest("hello", "Bob", "New Co", "bob@new.co", "0113 111 1111", "Hello");

        verify(emailService).sendMessage(any());
    }
}