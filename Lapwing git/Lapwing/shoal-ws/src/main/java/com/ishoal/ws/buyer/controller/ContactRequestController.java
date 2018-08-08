package com.ishoal.ws.buyer.controller;

import com.ishoal.core.contact.ContactService;
import com.ishoal.ws.buyer.dto.ContactRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/ws/contact")
public class ContactRequestController {

    private static final Logger logger = LoggerFactory.getLogger(ContactRequestController.class);

    private final ContactService contactService;

    public ContactRequestController(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public void handleContactRequest(@Valid @RequestBody ContactRequestDto dto) {
        logger.info("Contact form submission received");
        contactService.sendContactRequest(dto.getName(), dto.getCompanyName(), dto.getEmailAddress(), dto.getPhoneNumber(),
            dto.getMessage(), dto.getMessageType());
    }
}
