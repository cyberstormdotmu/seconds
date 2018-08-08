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

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "BuyerWithdrawCredit")
@Table(name = "BUYER_WITHDRAW_CREDIT")
@EntityListeners(AuditingEntityListener.class)
public class BuyerWithdrawCreditEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUYER_ID", referencedColumnName = "ID")
	private BuyerProfileEntity buyer;
	
	@Column(name = "REQUESTED_AMOUNT",precision = 9, scale = 2)
	private BigDecimal requestedAmount;
	
	@Column(name = "REQUESTED_DATE")
	private DateTime requestedDate;
	
	@Column(name = "PAYMENT_TYPE")
	private String paymentType;
	
	@Column(name = "RECEIVED_DATE")
	private Date receivedDate;
	
	@Column(name = "WITHDRAW_REFERENCE")
	private String withdrawReference;
	
	@Column(name = "WITHDRAW_STATUS")
	private String withdrawStatus;
	
	@Column(name = "CONFIRM_DATE")
	private DateTime confirmedDate;

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

	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(BigDecimal requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public DateTime getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(DateTime requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getWithdrawReference() {
		return withdrawReference;
	}

	public void setWithdrawReference(String withdrawReference) {
		this.withdrawReference = withdrawReference;
	}

	public String getWithdrawStatus() {
		return withdrawStatus;
	}

	public void setWithdrawStatus(String withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}

	public DateTime getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(DateTime confirmedDate) {
		this.confirmedDate = confirmedDate;
	}
	
}
