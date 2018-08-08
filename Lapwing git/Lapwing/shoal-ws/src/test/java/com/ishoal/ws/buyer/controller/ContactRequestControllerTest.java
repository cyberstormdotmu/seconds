package com.ishoal.ws.buyer.controller;

import com.ishoal.core.contact.ContactService;
import com.ishoal.ws.buyer.dto.ContactRequestDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContactRequestControllerTest {

    @Mock
    private ContactService service;

    private ContactRequestController controller;

    @Before
    public void before() {
        controller = new ContactRequestController(service);
    }

    @Test
    public void theControllerCallsTheService() {
        ContactRequestDto dto = ContactRequestDto.aContactRequest()
                .name("John Doe")
                .companyName("Umbrella Corp")
                .emailAddress("john.doe@umbrel.la")
                .phoneNumber("0113 111 1111")
                .message("I would like to know more")
                .messageType("Contact_Us")
                .build();


        controller.handleContactRequest(dto);

        Mockito.verify(service).sendContactRequest("hello", "John Doe", "Umbrella Corp", "john.doe@umbrel.la",
                "0113 111 1111", "I would like to know more");
    }
}