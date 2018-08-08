package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.Contact;
import com.ishoal.ws.buyer.dto.ContactDto;

public class ContactDtoAdapter {
    public ContactDtoAdapter() {

    }

    public ContactDto adapt(Contact contact) {

        if (contact == null) {
            return null;
        }
        return ContactDto.aContact().title(contact.getTitle()).firstName(contact.getFirstName()).surname(
            contact.getSurname())
            .emailAddress(contact.getEmailAddress()).phoneNumber(contact.getPhoneNumber()).build();
    }

	public Contact adapt(ContactDto contact) {
		 if (contact == null) {
	            return null;
	        }
		 return Contact.aContact().title(contact.getTitle()).firstName(contact.getFirstName()).surname(
		            contact.getSurname())
		            .emailAddress(contact.getEmailAddress()).phoneNumber(contact.getPhoneNumber()).build();
	}
}