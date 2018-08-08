package com.ishoal.core.persistence.entity;

import java.math.BigDecimal;

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
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "BuyerVendorCredit")
@Table(name = "BUYER_VENDOR_CREDIT", uniqueConstraints = @UniqueConstraint(columnNames = { "vendor_id", "buyer_id" }) )
@EntityListeners(AuditingEntityListener.class)
public class BuyerVendorCreditEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "ID")
	private VendorEntity vendor;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUYER_ID", referencedColumnName = "ID")
	private BuyerProfileEntity buyer;

	@Column(name = "TOTAL_CREDIT", precision = 9, scale = 2)
	private BigDecimal totalCredit;

	@Column(name = "AVAILABLE_CREDIT", precision = 9, scale = 2)
	private BigDecimal availableCredit;

	@Column(name = "PAYMENT_DUE_DAYS")
	private int paymentDueDays;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VendorEntity getVendor() {
		return vendor;
	}

	public void setVendor(VendorEntity vendor) {
		this.vendor = vendor;
	}

	public BuyerProfileEntity getBuyer() {
		return buyer;
	}

	public void setBuyer(BuyerProfileEntity buyer) {
		this.buyer = buyer;
	}

	public BigDecimal getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(BigDecimal totalCredit) {
		this.totalCredit = totalCredit;
	}

	public BigDecimal getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(BigDecimal availableCredit) {
		this.availableCredit = availableCredit;
	}

	public int getPaymentDueDays() {
		return paymentDueDays;
	}

	public void setPaymentDueDays(int paymentDueDays) {
		this.paymentDueDays = paymentDueDays;
	}
}
