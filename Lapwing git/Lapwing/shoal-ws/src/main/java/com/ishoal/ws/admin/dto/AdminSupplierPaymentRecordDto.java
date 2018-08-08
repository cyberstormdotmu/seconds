package com.ishoal.ws.admin.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class AdminSupplierPaymentRecordDto {

	private DateTime date;
	private String from;
	private String to;
	private String referenceNumber;
	private BigDecimal amount;
	private String status;
	private BigDecimal lapwingBalance;
	private BigDecimal supplierBalance;

	
	public AdminSupplierPaymentRecordDto()
	{
		
	}
	
	public AdminSupplierPaymentRecordDto(Builder builder)
	{
		this.date = builder.date;
		this.from = builder.from;
		this.to = builder.to;
		this.referenceNumber = builder.referenceNumber;
		this.amount = builder.amount;
		this.status = builder.status;
		this.lapwingBalance = builder.lapwingBalance;
		this.supplierBalance = builder.supplierBalance;
	}
	
	public static Builder anAdminSupplierPaymentRecordDto()
	{
		return new Builder();
	}
	
	public DateTime getDate() {
		return date;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getStatus() {
		return status;
	}

	public BigDecimal getLapwingBalance() {
		return lapwingBalance;
	}

	public BigDecimal getSupplierBalance() {
		return supplierBalance;
	}

	public static final class Builder{
		private DateTime date;
		private String from;
		private String to;
		private String referenceNumber;
		private BigDecimal amount;
		private String status;
		private BigDecimal lapwingBalance;
		private BigDecimal supplierBalance;
		
		public Builder date(DateTime val)
		{
			this.date = val;
			return this;
		}
		
		public Builder from(String val)
		{
			this.from = val;
			return this;
		}
		
		public Builder to(String val)
		{
			this.to = val;
			return this;
		}
		
		public Builder referenceNumber(String val)
		{
			this.referenceNumber = val;
			return this;
		}
		
		public Builder amount(BigDecimal val)
		{
			this.amount = val;
			return this;
		}
		
		public Builder status(String val)
		{
			this.status = val;
			return this;
		}
		
		public Builder lapwingBalance(BigDecimal val) {
			this.lapwingBalance = val;
			return this;
		}

		public Builder supplierBalance(BigDecimal val) {
			this.supplierBalance = val;
			return this;
		}

		public AdminSupplierPaymentRecordDto build()
		{
			return new AdminSupplierPaymentRecordDto(this);
		}
	}
}
