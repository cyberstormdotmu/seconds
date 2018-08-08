package com.ishoal.core.repository;

import com.ishoal.core.domain.ContactUs;
import com.ishoal.core.persistence.adapter.ContactUsEntityAdapter;
import com.ishoal.core.persistence.repository.ContactUsEntityRepository;

public class ContactUsRepository {
	
	private final ContactUsEntityRepository contactUsEntityRepository;
	private final ContactUsEntityAdapter contactUsEntityAdapter = new ContactUsEntityAdapter();
	
	public ContactUsRepository(ContactUsEntityRepository contactUsEntityRepository) {
    	this.contactUsEntityRepository = contactUsEntityRepository;
    }

	public void saveContactRequest(ContactUs contactUs) {
		
		contactUsEntityRepository.save(contactUsEntityAdapter.adapt(contactUs));
		
	}

}
