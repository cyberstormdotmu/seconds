package com.ishoal.core.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity(name = "SupplierPayments")
@Table(name = "SUPPLIER_PAYMENTS")
public class SupplierPaymentsEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TRANSFER_FROM")
	private String from;

	@Column(name = "TRANSFER_TO")
	private String to;

	@Column(name = "RECEIVED_DATE")
	private DateTime dateReceived;

	@Column(name = "PAYMENT_TYPE")
	private String paymentType;

	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@Column(name = "USER_REFERENCE")
	private String userReference;

	@Column(name = "CREATED_DATE")
	private DateTime created;

	@Column(name = "RECORD_PAYMENT_STATUS")
	private String recordPaymentStatus;
	
	@Column(name = "PAYMENT_RECORD_DATE")
	private DateTime paymentRecordDate;

    @Column(name = "OFFER_REFERENCE")
    private String offerReference;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public DateTime getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(DateTime dateReceived) {
		this.dateReceived = dateReceived;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getUserReference() {
		return userReference;
	}

	public void setUserReference(String userReference) {
		this.userReference = userReference;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public String getRecordPaymentStatus() {
		return recordPaymentStatus;
	}

	public void setRecordPaymentStatus(String recordPaymentStatus) {
		this.recordPaymentStatus = recordPaymentStatus;
	}

	public DateTime getPaymentRecordDate() {
		return paymentRecordDate;
	}

	public void setPaymentRecordDate(DateTime paymentRecordDate) {
		this.paymentRecordDate = paymentRecordDate;
	}

	public String getOfferReference() {
		return offerReference;
	}

	public void setOfferReference(String offerReference) {
		this.offerReference = offerReference;
	}

}
