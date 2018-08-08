package com.ishoal.core.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "PaymentGatewayCharges")
@Table(name = "payment_gateway_charges")
public class PaymentGatewayChargesEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="NAME")
	private String name;

	@Column(name="PAYMENT_CHARGES_PERCENTAGE")
	private BigDecimal paymentChargesPercentage;

	@Column(name="PAYMENT_EXTRA_CHARGE")
	private BigDecimal paymentExtraCharge;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPaymentChargesPercentage() {
		return paymentChargesPercentage;
	}

	public void setPaymentChargesPercentage(BigDecimal paymentChargesPercentage) {
		this.paymentChargesPercentage = paymentChargesPercentage;
	}

	public BigDecimal getPaymentExtraCharge() {
		return paymentExtraCharge;
	}

	public void setPaymentExtraCharge(BigDecimal paymentExtraCharge) {
		this.paymentExtraCharge = paymentExtraCharge;
	}
}
