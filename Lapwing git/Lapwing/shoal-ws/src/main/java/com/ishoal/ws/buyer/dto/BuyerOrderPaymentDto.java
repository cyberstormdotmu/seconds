package com.ishoal.ws.buyer.dto;

import org.joda.time.DateTime;

import com.ishoal.core.domain.PaymentRecordType;

import java.math.BigDecimal;

public class BuyerOrderPaymentDto {
	private DateTime dateReceived;
	private String paymentType;
	private BigDecimal amount;
	private String originalOrderReference;
	private String originalOrderLineProductCode;
	private BigDecimal paymentGatewayCharges;
	private PaymentRecordType paymentRecordType;

	private BuyerOrderPaymentDto(Builder builder) {
		dateReceived = builder.dateReceived;
		paymentType = builder.paymentType;
		amount = builder.amount;
		originalOrderReference = builder.originalOrderReference;
		originalOrderLineProductCode = builder.originalOrderLineProductCode;
		paymentGatewayCharges = builder.paymentGatewayCharges;
		paymentRecordType = builder.paymentRecordType;
	}

	public DateTime getDateReceived() {
		return dateReceived;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getOriginalOrderReference() {
		return originalOrderReference;
	}

	public String getOriginalOrderLineProductCode() {
		return originalOrderLineProductCode;
	}

	public BigDecimal getPaymentGatewayCharges() {
		return paymentGatewayCharges;
	}

	public PaymentRecordType getPaymentRecordType() {
		return paymentRecordType;
	}

	public static Builder anOrderPayment() {
		return new Builder();
	}

	public static final class Builder {
		private DateTime dateReceived;
		private String paymentType;
		private BigDecimal amount;
		private String originalOrderReference;
		private String originalOrderLineProductCode;
		private BigDecimal paymentGatewayCharges;
		private PaymentRecordType paymentRecordType;

		private Builder() {
		}

		public Builder dateReceived(DateTime val) {
			dateReceived = val;
			return this;
		}

		public Builder paymentType(String val) {
			paymentType = val;
			return this;
		}

		public Builder amount(BigDecimal val) {
			amount = val;
			return this;
		}

		public Builder originalOrderReference(String val) {
			originalOrderReference = val;
			return this;
		}

		public Builder originalOrderLineProductCode(String val) {
			originalOrderLineProductCode = val;
			return this;
		}

		public Builder paymentGatewayCharges(BigDecimal val) {
			paymentGatewayCharges = val;
			return this;
		}

		public Builder paymentRecordType(PaymentRecordType val) {
			this.paymentRecordType = val;
			return this;
		}

		public BuyerOrderPaymentDto build() {
			return new BuyerOrderPaymentDto(this);
		}
	}
}
