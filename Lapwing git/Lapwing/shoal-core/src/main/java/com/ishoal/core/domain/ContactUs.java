package com.ishoal.core.domain;

public class ContactUs {

    private String companyName;
    private String name;
    private String mobileNumber;
    private String emailAddress;
    private String message;
    private String messageType;

    private ContactUs(Builder builder) {

    	companyName = builder.companyName;
    	name = builder.name;
    	mobileNumber = builder.mobileNumber;
    	emailAddress = builder.emailAddress;
    	message = builder.message;
    	messageType = builder.messageType;
    }

    public String getCompanyName() {
		return companyName;
	}

	public String getName() {
		return name;
	}

	public String getMobileNumber() {
		return mobileNumber;
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

	public static Builder aContactUs() {

        return new Builder();
    }

    public static final class Builder {
    	private String companyName;
        private String name;
        private String mobileNumber;
        private String emailAddress;
        private String message;
        private String messageType;

        public Builder() {

        }

        public Builder(ContactUs copy) {

            this.companyName = copy.companyName;
            this.name = copy.name;
            this.mobileNumber = copy.mobileNumber;
            this.emailAddress = copy.emailAddress;
            this.message = copy.message;
            this.messageType = copy.messageType;
        }

        public Builder companyName(String val) {

        	companyName = val;
            return this;
        }

        public Builder name(String val) {

        	name = val;
            return this;
        }

        public Builder mobileNumber(String val) {

        	mobileNumber = val;
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

        public ContactUs build() {

            return new ContactUs(this);
        }
    }
}
