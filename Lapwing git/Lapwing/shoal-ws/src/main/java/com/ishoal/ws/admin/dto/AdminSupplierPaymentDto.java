package com.ishoal.ws.admin.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class AdminSupplierPaymentDto {
	private String from;
	private String to;
	private DateTime dateReceived;
	private String paymentType;
	private BigDecimal amount;
	private String userReference;
	private String recordPaymentStatus;
	private DateTime paymentRecordDate;
	private String offerReference;

	public AdminSupplierPaymentDto()
	{
		
	}
	
	private AdminSupplierPaymentDto(Builder builder) {
		this.from = builder.from;
		this.to = builder.to;
		this.dateReceived = builder.dateReceived;
		this.paymentType = builder.paymentType;
		this.amount = builder.amount;
		this.userReference = builder.userReference;
		this.recordPaymentStatus = builder.recordPaymentStatus;
		this.paymentRecordDate = builder.paymentRecordDate;
		this.offerReference = builder.offerReference;
	}

	public static Builder aAdminSupplierPaymentDto() {
		return new Builder();
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
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

	public String getRecordPaymentStatus() {
		return recordPaymentStatus;
	}

	public DateTime getPaymentRecordDate() {
		return paymentRecordDate;
	}

	public String getOfferReference() {
		return offerReference;
	}

	public static class Builder {
		private String from;
		private String to;
		private DateTime dateReceived;
		private String paymentType;
		private BigDecimal amount;
		private String userReference;
		private String recordPaymentStatus;
		private DateTime paymentRecordDate;
		private String offerReference;
		
		public Builder from(String val) {
			this.from = val;
			return this;
		}

		public Builder to(String val) {
			this.to = val;
			return this;
		}

		public Builder dateReceived(DateTime val) {
			this.dateReceived = val;
			return this;
		}
		
		public Builder paymentType(String val) {
			this.paymentType = val;
			return this;
		}

		public Builder amount(BigDecimal val) {
			this.amount = val;
			return this;
		}

		public Builder userReference(String val) {
			this.userReference = val;
			return this;
		}

		public Builder recordPaymentStatus(String val) {
			this.recordPaymentStatus = val;
			return this;
		}

		public Builder paymentRecordDate(DateTime val) {
			paymentRecordDate = val;
			return this;
		}
		
		public Builder offerReference(String val) {
			this.offerReference = val;
			return this;
		}

		public AdminSupplierPaymentDto build() {
			return new AdminSupplierPaymentDto(this);
		}
	}
}
