package com.ishoal.ws.admin.dto;

import com.ishoal.core.domain.BankAccount;

public class BuyerListingDto {
    
    private String buyerId;
    private String userName;
    private String firstName;
    private String surname;
    private String mobileNumber;
    private String emailAddress;
    private String organisation;
    private String companyNo;
    private String westcoastAccountNumber;
    private String vatNumber;
    private BankAccount bankAccount;
    
    private BuyerListingDto() {
        super();
    }
    
    private BuyerListingDto(Builder builder) {
        this();
        buyerId = builder.buyerId;
        userName = builder.userName;
        firstName = builder.firstName;
        surname = builder.surname;
        mobileNumber = builder.mobileNumber;
        emailAddress = builder.emailAddress;
        organisation = builder.organisation;
        companyNo = builder.companyNo;
        westcoastAccountNumber = builder.westcoastAccountNumber;
        vatNumber = builder.vatNumber;
        bankAccount = builder.bankAccount;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getOrganisation() {
        return organisation;
    }
    
    public String getWestcoastAccountNumber() {
        return westcoastAccountNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public BankAccount getBankAccount(){
        return bankAccount;
    }
    
    public static Builder aBuyerListing() {
        return new Builder();
    }
    
    public static final class Builder {
        private String buyerId;
        private String userName;
        private String firstName;
        private String surname;
        private String mobileNumber;
        private String emailAddress;
        private String organisation;
        private String companyNo;
        private String westcoastAccountNumber;
        private String vatNumber;
        private BankAccount bankAccount;
       
        private Builder() {
        }
        
        public Builder buyerId(String val) {
            buyerId = val;
            return this;
        }

        public Builder userName(String val) {
            userName = val;
            return this;
        }

       
        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder surname(String val) {
            surname = val;
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

        public Builder companyNo(String val) {
            companyNo = val;
            return this;
        }
        
        public Builder organisation(String val) {
            organisation = val;
            return this;
        }
        
        public Builder westcoastAccountNumber(String val) {
            westcoastAccountNumber = val;
            return this;
        }
        
        public Builder vatNumber(String val) {
            vatNumber = val;
            return this;
        }
        
        public Builder bankAccount(BankAccount val){
            bankAccount = val;
            return this;
        }
        
        public BuyerListingDto build() {
            return new BuyerListingDto(this);
        }
    }

}
