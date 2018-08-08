package com.ishoal.core.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "BuyerCreditInfo")
@Table(name = "BUYER_CREDIT_APPLICATION_INFORMATION")
@EntityListeners(AuditingEntityListener.class)
public class BuyerCreditInfoEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUYER_ID", referencedColumnName = "ID")
	private BuyerProfileEntity buyer;

	@Column(name = "COMPANY_NAME", length = 64)
	private String companyName;

	@Column(name = "TRADING_AS", length = 256)
	private String tradingAs;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INVOICE_ADDRESS_ID", referencedColumnName = "id")
	private AddressEntity invoiceAddress;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELIVERY_ADDRESS_ID", referencedColumnName = "id")
	private AddressEntity deliveryAddress;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGISTERED_ADDRESS_ID", referencedColumnName = "id")
	private AddressEntity registeredAddress;

	@Column(name = "LANDLINE_NUMBER", length = 12)
	private String landlineNumber;

	@Column(name = "WEBSITE", length = 256)
	private String website;

	@Column(name = "PURCHASING_MANAGER", length = 256)
	private String purchasingManager;

	@Column(name = "CONTACT_NAME", length = 256)
	private String contactName;

	@Column(name = "TELEPHONE_NUMBER", length = 12)
	private String telephoneNumber;

	@Column(name = "EMAIL_ADDRESS", length = 256)
	private String emailAddress;

	@Column(name = "IS_CASH_ACCOUNT")
	private boolean isCashAccount;

	@Column(name = "IS_CREDIT_ACCOUNT")
	private boolean isCreditAccount;

	@Column(name = "PRINT_NAME", length = 256)
	private String printName;

	@Column(name = "REGISTERED_DATE")
	private Date registrationDate;

	@Column(name = "FULL_COMPANY_NAME", length = 256)
	private String fullCompanyName;

	@Column(name = "TRADING_NAME", length = 64)
	private String tradingName;

	@Column(name = "VAT_REGISTRATION", length = 256)
	private String vatRegistration;

	@Column(name = "EXPECTED_CREDIT")
    private BigDecimal expectedCredit;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BuyerProfileEntity getBuyer() {
		return buyer;
	}

	public void setBuyer(BuyerProfileEntity buyer) {
		this.buyer = buyer;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTradingName() {
		return tradingName;
	}

	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}

	public AddressEntity getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(AddressEntity invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public AddressEntity getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(AddressEntity deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public AddressEntity getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(AddressEntity registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getLandlineNumber() {
		return landlineNumber;
	}

	public void setLandlineNumber(String landlineNumber) {
		this.landlineNumber = landlineNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPurchasingManager() {
		return purchasingManager;
	}

	public void setPurchasingManager(String purchasingManager) {
		this.purchasingManager = purchasingManager;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getTradingAs() {
		return tradingAs;
	}

	public void setTradingAs(String tradingAs) {
		this.tradingAs = tradingAs;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean isCashAccount() {
		return isCashAccount;
	}

	public void setCashAccount(boolean isCashAccount) {
		this.isCashAccount = isCashAccount;
	}

	public boolean isCreditAccount() {
		return isCreditAccount;
	}

	public void setCreditAccount(boolean isCreditAccount) {
		this.isCreditAccount = isCreditAccount;
	}

	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getFullCompanyName() {
		return fullCompanyName;
	}

	public void setFullCompanyName(String fullCompanyName) {
		this.fullCompanyName = fullCompanyName;
	}

	public String getVatRegistration() {
		return vatRegistration;
	}

	public void setVatRegistration(String vatRegistration) {
		this.vatRegistration = vatRegistration;
	}

    public BigDecimal getExpectedCredit() {
        return expectedCredit;
    }

    public void setExpectedCredit(BigDecimal expectedCredit) {
        this.expectedCredit = expectedCredit;
    }

}
