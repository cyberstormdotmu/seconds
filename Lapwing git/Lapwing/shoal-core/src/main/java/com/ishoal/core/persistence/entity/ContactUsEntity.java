package com.ishoal.core.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="ContactUs")
@Table(name = "CONTACT_US")
public class ContactUsEntity {
	
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "company_name")
    private String companyName;
    @Column(name = "name")
    private String name;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "email")
    private String emailAddress;
    @Column(name = "message")
    private String message;
    @Column(name = "message_type")
    private String messageType;
   
	public Long getId() {
		return id;
	}
    
	public String getCompanyName() {
		return companyName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public String getMessage() {
		return message;
	}
    
    public void setId(Long id) {
		this.id = id;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
}