package com.ishoal.ws.common.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.ishoal.ws.buyer.dto.BuyerProfileDto;

public class BuyerCreditInfoDto {
	private Long id;
	private BuyerProfileDto buyer;
	private String companyName;
	private String tradingAs;
	private AddressDto invoiceAddress;
	private AddressDto deliveryAddress;
	private AddressDto registeredAddress;
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
	
	public BuyerCreditInfoDto() {

    }

    private BuyerCreditInfoDto(Builder builder) {
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
		id=builder.id;
		vatRegistration=builder.vatRegistration;
		expectedCredit=builder.expectedCredit;
		
    }

    public static Builder aBuyerCreditInfoDto() {

        return new Builder();
    }
    
    public BuyerProfileDto getBuyer() {
		return buyer;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getTradingName() {
		return tradingName;
	}

	public AddressDto getInvoiceAddress() {
		return invoiceAddress;
	}

	public AddressDto getDeliveryAddress() {
		return deliveryAddress;
	}

	public AddressDto getRegisteredAddress() {
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

	public String getEmailAddress() {
		return emailAddress;
	}
	  
	public boolean isIsCashAccount() {
		return isCashAccount;
	}

	public boolean isIsCreditAccount() {
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

	public Long getId() {
		return id;
	}

	public String getVatRegistration() {
		return vatRegistration;
	}

	public BigDecimal getExpectedCredit() {
        return expectedCredit;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public static final class Builder {
		private Long id;
    	private BuyerProfileDto buyer;
    	private String companyName;
    	private String tradingAs;
    	private AddressDto invoiceAddress;
    	private AddressDto deliveryAddress;
    	private AddressDto registeredAddress;
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
    	
        public Builder() {

        }

        public Builder(BuyerCreditInfoDto copy) {
        	
        	buyer = copy.buyer;
        	id=copy.id;
    		companyName = copy.companyName;
    		tradingAs=copy.tradingAs;
    		invoiceAddress = copy.invoiceAddress;
    		deliveryAddress = copy.deliveryAddress;
    		registeredAddress = copy.registeredAddress;
    		landlineNumber = copy.landlineNumber;
    		website = copy.website;
    		purchasingManager = copy.purchasingManager;
    		contactName=copy.contactName;
    		telephoneNumber=copy.telephoneNumber;
    		emailAddress=copy.emailAddress;
    		isCashAccount=copy.isCashAccount;
    		isCreditAccount=copy.isCreditAccount;
    		printName=copy.printName;
    		registrationDate=copy.registrationDate;
    		fullCompanyName=copy.fullCompanyName;
    		tradingName=copy.tradingName;
    		vatRegistration=copy.vatRegistration;
    		expectedCredit=copy.expectedCredit;
        }
        public Builder id(Long val) {
        	id = val;
            return this;
        }
        
        public Builder buyer(BuyerProfileDto val) {
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
        public Builder invoiceAddress(AddressDto val) {
        	invoiceAddress = val;
            return this;
        }
        public Builder deliveryAddress(AddressDto val) {
        	deliveryAddress = val;
            return this;
        }
        public Builder registeredAddress(AddressDto val) {
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
        public Builder isCashAmount(boolean val) {
        	isCashAccount = val;
            return this;
        }
        public Builder isCreditAmount(boolean val) {
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
        
        public BuyerCreditInfoDto build() {

            return new BuyerCreditInfoDto(this);
        }
}
}