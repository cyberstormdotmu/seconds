package com.ishoal.ws.buyer.dto;

import com.ishoal.core.domain.Vendor;

public class UserDto {

    private String firstName;
    private String surname;
    private String emailAddress;
    private String password;
    private String mobileNumber;
    private String registrationToken;
    private String westcoastAccountNumber;
    private String lapwingAccountNumber;
    private String buyerReferralCode;
    private String appliedReferralCode;
    private String appliedFor;
    private Vendor vendor;

    public UserDto() {

    }
    private UserDto(Builder builder) {

        firstName = builder.firstName;
        surname = builder.surname;
        emailAddress = builder.emailAddress;
        password = builder.password;
        mobileNumber = builder.mobileNumber;
        registrationToken = builder.registrationToken;
        westcoastAccountNumber = builder.westcoastAccountNumber;
        lapwingAccountNumber = builder.lapwingAccountNumber;
        appliedReferralCode = builder.appliedReferralCode;
        buyerReferralCode = builder.buyerReferralCode;
        appliedFor = builder.appliedFor;
        vendor = builder.vendor;
    }
    
    public String getRegistrationToken() {
		return registrationToken;
	}
    
	public static Builder aUserDto() {
        return new Builder();
    }

	public String getFirstName() {

        return firstName;
    }

    public String getSurname() {

        return surname;
    }
    
    public String getEmailAddress() {

        return emailAddress;
    }

    public static Builder aNewUserDto() {

        return new Builder();
    }

    public String getPassword() {

        return password;
    }

    public String getWestcoastAccountNumber() {
		return westcoastAccountNumber;
	}
	public String getLapwingAccountNumber() {
		return lapwingAccountNumber;
	}
	
	public String getBuyerReferralCode() {
		return buyerReferralCode;
	}
	public String getAppliedReferralCode() {
		return appliedReferralCode;
	}
	
	public Vendor getVendor() {
		return vendor;
	}
	
	public void clearPassword() {
        this.password = "";
    }

	public String getAppliedFor() {
		return appliedFor;
	}
	
    public String getMobileNumber() {
    	return mobileNumber;
    }

    public static final class Builder {
        private String emailAddress;
        private String password;
        private String surname;
        private String firstName;
        private String mobileNumber;
        private String registrationToken;
        private String westcoastAccountNumber;
        private String lapwingAccountNumber;
        private String buyerReferralCode;
        private String appliedReferralCode;
        private String appliedFor;
        private Vendor vendor;
        
        public Builder() {

        }

        public Builder(UserDto copy) {

            this.firstName = copy.firstName;
            this.surname = copy.surname;
            this.emailAddress = copy.emailAddress;
            this.password = copy.password;
            this.mobileNumber = copy.mobileNumber;
            this.registrationToken = copy.registrationToken;
            this.westcoastAccountNumber = copy.westcoastAccountNumber;
            this.lapwingAccountNumber = copy.lapwingAccountNumber;
            this.appliedReferralCode = copy.appliedReferralCode;
            this.buyerReferralCode = copy.buyerReferralCode;
            this.appliedFor = copy.appliedFor;
            this.vendor = copy.vendor;
        }
        
        public Builder buyerReferralCode(String val) {

        	buyerReferralCode = val;
            return this;
        }
        
        public Builder appliedReferralCode(String val) {

        	appliedReferralCode = val;
            return this;
        }
        
        public Builder vendor(Vendor vendor) {
			this.vendor = vendor;
			return this;
		}
        
        public Builder mobileNumber(String val) {

            mobileNumber = val;
            return this;
        }
        
        public Builder registrationToken(String val) {

        	registrationToken = val;
            return this;
        }

        public Builder emailAddress(String val) {

            emailAddress = val;
            return this;
        }

        public Builder password(String val) {

            password = val;
            return this;
        }

        public Builder surname(String val) {

            surname = val;
            return this;
        }

        public Builder westcoastAccountNumber(String val) {

        	westcoastAccountNumber = val;
            return this;
        }
        
        public Builder lapwingAccountNumber(String val) {

        	lapwingAccountNumber = val;
            return this;
        }
        
        public Builder appliedFor(String val) {

        	appliedFor = val;
            return this;
        }
        
        public Builder firstName(String val) {

            firstName = val;
            return this;
        }
        
        public UserDto build() {

            return new UserDto(this);
        }
    }
}
