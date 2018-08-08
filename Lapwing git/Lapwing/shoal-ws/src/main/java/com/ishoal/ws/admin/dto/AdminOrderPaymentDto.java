package com.ishoal.ws.admin.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.ishoal.core.domain.PaymentRecordType;
import com.ishoal.core.domain.PaymentType;

public class AdminOrderPaymentDto {
	private String paymentReference;
	private DateTime dateReceived;
	private String paymentType;
	private BigDecimal amount;
	private String userReference;
	private BigDecimal paymentGatewayCharges;
	private PaymentRecordType paymentRecordType;

	private AdminOrderPaymentDto() {
		super();
	}

	private AdminOrderPaymentDto(Builder builder) {
		this();
		paymentReference = builder.paymentReference;
		dateReceived = builder.dateReceived;
		paymentType = builder.paymentType.getDisplayName();
		amount = builder.amount;
		userReference = builder.userReference;
		paymentGatewayCharges = builder.paymentGatewayCharges;
		paymentRecordType = builder.paymentRecordType;
	}

	public static Builder anOrderPayment() {
		return new Builder();
	}

	public BigDecimal getPaymentGatewayCharges() {
		return paymentGatewayCharges;
	}

	public String getPaymentReference() {
		return paymentReference;
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

	public String getUserReference() {
		return userReference;
	}

	public PaymentRecordType getPaymentRecordType() {
		return paymentRecordType;
	}

	public static final class Builder {
		private String paymentReference;
		private DateTime dateReceived;
		private PaymentType paymentType;
		private BigDecimal amount;
		private String userReference;
		private BigDecimal paymentGatewayCharges;
		private PaymentRecordType paymentRecordType;

		private Builder() {
		}

		public Builder paymentReference(String val) {
			paymentReference = val;
			return this;
		}

		public Builder dateReceived(DateTime val) {
			dateReceived = val;
			return this;
		}

		public Builder paymentType(PaymentType val) {
			paymentType = val;
			return this;
		}

		public Builder amount(BigDecimal val) {
			amount = val;
			return this;
		}

		public Builder userReference(String val) {
			userReference = val;
			return this;
		}

		public Builder paymentGatewayCharges(BigDecimal val) {
			paymentGatewayCharges = val;
			return this;
		}

		public Builder paymentRecordType(PaymentRecordType val) {
			paymentRecordType = val;
			return this;
		}

		public AdminOrderPaymentDto build() {
			return new AdminOrderPaymentDto(this);
		}
	}
}
