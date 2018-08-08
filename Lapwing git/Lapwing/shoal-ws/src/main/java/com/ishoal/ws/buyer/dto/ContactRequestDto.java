package com.ishoal.ws.buyer.dto;

import org.hibernate.validator.constraints.NotEmpty;

import com.ishoal.ws.common.validation.NoHtml;

public class ContactRequestDto {
    @NotEmpty
    private String companyName;

    @NotEmpty
    private String name;

    @NoHtml
    private String phoneNumber;

    @NoHtml
    @NotEmpty
    private String emailAddress;

    @NotEmpty
    private String message;
    
    @NotEmpty
    private String messageType;

    private ContactRequestDto() {
        super();
    }

    private ContactRequestDto(Builder builder) {
        this();
        companyName = builder.companyName;
        name = builder.name;
        phoneNumber = builder.phoneNumber;
        emailAddress = builder.emailAddress;
        message = builder.message;
        messageType = builder.messageType;
    }

    public static Builder aContactRequest() {
        return new Builder();
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmailAddress() {
        return emailAddress;
    }

    public String getMessage() {
        return message;
    }


    public String getMessageType() {
		return messageType;
	}


	public static final class Builder {
        private String companyName;
        private String name;
        private String phoneNumber;
        private String emailAddress;
        private String message;
        private String messageType;
        
        private Builder() {
        }

        public Builder companyName(String val) {
            companyName = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder phoneNumber(String val) {
        	phoneNumber = val;
            return this;
        }

        public Builder emailAddress(String val) {
            emailAddress = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }
        
        public Builder messageType(String val) {
        	messageType = val;
            return this;
        }

        public ContactRequestDto build() {
            return new ContactRequestDto(this);
        }
    }
}
