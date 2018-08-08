package com.ishoal.ws.admin.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class SupplierPaymentForAnOrderDto {

	private Long id;
	private String from;
	private String to;
	private DateTime dateReceived;
	private String paymentType;
	private BigDecimal amount;
	private String userReference;
	private String recordPaymentStatus;
	private DateTime paymentRecordDate;
	private DateTime createdDate;
	private String offerReference;

	private SupplierPaymentForAnOrderDto(Builder builder) {
		this.id = builder.id;
		this.from = builder.from;
		this.to = builder.to;
		this.dateReceived = builder.dateReceived;
		this.paymentType = builder.paymentType;
		this.amount = builder.amount;
		this.userReference = builder.userReference;
		this.recordPaymentStatus = builder.recordPaymentStatus;
		this.paymentRecordDate = builder.paymentRecordDate;
		this.createdDate = builder.createdDate;
		this.offerReference = builder.offerReference;
	}

	public static Builder aSupplierPaymentForAnOrderDto() {
		return new Builder();
	}

	public Long getId() {
		return id;
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

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public String getOfferReference() {
		return offerReference;
	}

	public static class Builder {
		private Long id;
		private String from;
		private String to;
		private DateTime dateReceived;
		private String paymentType;
		private BigDecimal amount;
		private String userReference;
		private String recordPaymentStatus;
		private DateTime paymentRecordDate;
		private DateTime createdDate;
		private String offerReference;

		public Builder id(Long val) {
			this.id = val;
			return this;
		}

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

		public Builder createdDate(DateTime val) {
			this.createdDate = val;
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
			this.paymentRecordDate = val;
			return this;
		}

		public Builder offerReference(String val) {
			this.offerReference = val;
			return this;
		}

		public SupplierPaymentForAnOrderDto build() {
			return new SupplierPaymentForAnOrderDto(this);
		}
	}
}
