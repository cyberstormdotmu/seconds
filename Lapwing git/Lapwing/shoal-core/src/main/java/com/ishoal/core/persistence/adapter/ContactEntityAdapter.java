package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.Contact;
import com.ishoal.core.persistence.entity.ContactEntity;

public class ContactEntityAdapter {
	
    public ContactEntityAdapter() {

    }

    public ContactEntity adapt(Contact contact) {

        ContactEntity entity = new ContactEntity();
        entity.setTitle(contact.getTitle());
        entity.setFirstname(contact.getFirstName());
        entity.setSurname(contact.getSurname());
        entity.setEmailAddress(contact.getEmailAddress());
        entity.setPhoneNumber(contact.getPhoneNumber());
        return entity;
    }

    public Contact adapt(ContactEntity entity) {

        if (entity == null) {
            return null;
        }

        return Contact.aContact().title(entity.getTitle())
            .firstName(entity.getFirstname()).surname(entity.getSurname())
            .emailAddress(entity.getEmailAddress()).phoneNumber(entity.getPhoneNumber())
            .build();
    }
}