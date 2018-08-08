package com.ishoal.core.domain;

import java.math.BigDecimal;
import java.util.Date;

public class BuyerCreditInfo {
	private Long id;
	private BuyerProfile buyer;
	private String companyName;
	private String tradingAs;
	private Address invoiceAddress;
	private Address deliveryAddress;
	private Address registeredAddress;
	private String landlineNumber;
	private String website;
	private String purchasingManager;
	private String contactName;
	private String telephoneNumber;
	private String emailAddress;
	private boolean isCashAccount;
	private boolean isCreditAccount;
	private String printName;
	private Date registrationDate;
	private String fullCompanyName;	
	private String tradingName;
	private String vatRegistration;
	private BigDecimal expectedCredit;
	
	private BuyerCreditInfo(Builder builder) {
		id = builder.id;
		buyer = builder.buyer;
		companyName = builder.companyName;
		tradingAs=builder.tradingAs;
		invoiceAddress = builder.invoiceAddress;
		deliveryAddress = builder.deliveryAddress;
		registeredAddress = builder.registeredAddress;
		landlineNumber = builder.landlineNumber;
		website = builder.website;
		purchasingManager = builder.purchasingManager;
		contactName=builder.contactName;
		telephoneNumber=builder.telephoneNumber;
		emailAddress=builder.emailAddress;
		isCashAccount=builder.isCashAccount;
		isCreditAccount=builder.isCreditAccount;
		printName=builder.printName;
		registrationDate=builder.registrationDate;
		fullCompanyName=builder.fullCompanyName;
		tradingName=builder.tradingName;
		vatRegistration=builder.vatRegistration;
		expectedCredit=builder.expectedCredit;
    }

    public static Builder aBuyerCreditInfo() {
        return new Builder();
    }
	public Long getId() {
		return id;
	}
	public BuyerProfile getBuyer() {
		return buyer;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getTradingName() {
		return tradingName;
	}
	public Address getDeliveryAddress() {
		return deliveryAddress;
	}
	public Address getRegisteredAddress() {
		return registeredAddress;
	}
	public String getLandlineNumber() {
		return landlineNumber;
	}
	public String getWebsite() {
		return website;
	}
	public String getPurchasingManager() {
		return purchasingManager;
	}
	
	public String getContactName() {
		return contactName;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
	public String getTradingAs() {
		return tradingAs;
	}

	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	
	public boolean isCashAccount() {
		return isCashAccount;
	}

	public boolean isCreditAccount() {
		return isCreditAccount;
	}

	public String getPrintName() {
		return printName;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public String getFullCompanyName() {
		return fullCompanyName;
	}

	public String getVatRegistration() {
		return vatRegistration;
	}

	public BigDecimal getExpectedCredit() {
        return expectedCredit;
    }

    public static final class Builder {
    	private Long id;
    	private BuyerProfile buyer;
    	private String companyName;
    	private String tradingAs;
    	private Address invoiceAddress;
    	private Address deliveryAddress;
    	private Address registeredAddress;
    	private String landlineNumber;
    	private String website;
    	private String purchasingManager;
    	private String contactName;
    	private String telephoneNumber;
    	private String emailAddress;
    	private boolean isCashAccount;
    	private boolean isCreditAccount;
    	private String printName;
    	private Date registrationDate;
    	private String fullCompanyName;	
    	private String tradingName;
    	private String vatRegistration;
    	private BigDecimal expectedCredit;
    	
        private Builder() {

        }

        public Builder id(Long val) {
        	id = val;
            return this;
        }
        public Builder buyer(BuyerProfile val) {
        	buyer = val;
            return this;
        }
        public Builder companyName(String val) {
        	companyName = val;
            return this;
        }
        public Builder tradingAs(String val) {
        	tradingAs = val;
            return this;
        }
        public Builder invoiceAddress(Address val) {
        	invoiceAddress = val;
            return this;
        }
        public Builder deliveryAddress(Address val) {
        	deliveryAddress = val;
            return this;
        }
        public Builder registeredAddress(Address val) {
        	registeredAddress = val;
            return this;
        }
        public Builder landlineNumber(String val) {
        	landlineNumber = val;
            return this;
        }
        public Builder website(String val) {
        	website = val;
            return this;
        }
        public Builder purchasingManager(String val) {
        	purchasingManager = val;
            return this;
        }
        public Builder contactName(String val) {
        	contactName = val;
            return this;
        }
        public Builder telephoneNumber(String val) {
        	telephoneNumber = val;
            return this;
        }
        public Builder emailAddress(String val) {
        	emailAddress = val;
            return this;
        }
        public Builder isCashAccount(boolean val) {
        	isCashAccount = val;
            return this;
        }
        public Builder isCreditAccount(boolean val) {
        	isCreditAccount = val;
            return this;
        }
        public Builder printName(String val) {
        	printName = val;
            return this;
        }
       
        public Builder date(Date val) {
        	registrationDate = val;
            return this;
        }
        public Builder fullCompanyName(String val) {
        	fullCompanyName = val;
            return this;
        }
        public Builder tradingName(String val) {
        	tradingName = val;
            return this;
        }
        public Builder vatRegistration(String val) {
        	vatRegistration = val;
            return this;
        }
       
        public Builder expectedCredit(BigDecimal val) {
            expectedCredit = val;
            return this;
        }
        public BuyerCreditInfo build() {
            return new BuyerCreditInfo(this);
        }
	}
}
