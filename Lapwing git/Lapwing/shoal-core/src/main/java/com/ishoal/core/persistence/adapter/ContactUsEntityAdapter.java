package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.ContactUs;
import com.ishoal.core.persistence.entity.ContactUsEntity;

public class ContactUsEntityAdapter {
	
	public ContactUsEntityAdapter() {
	
	}
	
	public ContactUsEntity adapt(ContactUs contactUs) {

	    ContactUsEntity entity = new ContactUsEntity();
	    entity.setCompanyName(contactUs.getCompanyName());
	    entity.setName(contactUs.getName());
	    entity.setEmailAddress(contactUs.getEmailAddress());
	    entity.setMobileNumber(contactUs.getMobileNumber());
	    entity.setMessage(contactUs.getMessage());
	    entity.setMessageType(contactUs.getMessageType());
	    return entity;
	}
	
	public ContactUs adapt(ContactUsEntity entity) {

	    if (entity == null) {
	        return null;
	    }

	    return ContactUs.aContactUs()
	    				.companyName(entity.getCompanyName())
	    				.name(entity.getName())
	    				.emailAddress(entity.getEmailAddress())
	    				.mobileNumber(entity.getMobileNumber())
	    				.message(entity.getMessage())
	    				.messageType(entity.getMessageType())
	    				.build();
	}

}