package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;

public class AppliedVendorCreditDto {

	private String vendorName;
	private BigDecimal creditsApplied;

	
	public AppliedVendorCreditDto()
	{
		
	}
	
	private AppliedVendorCreditDto(Builder builder) {
		this.vendorName = builder.vendorName;
		this.creditsApplied = builder.creditsApplied;
	}

	public static Builder aAppliedVendorCreditDto() {
		return new Builder();
	}

	public String getVendorName() {
		return vendorName;
	}

	public BigDecimal getCreditsApplied() {
		return creditsApplied;
	}

	public static class Builder {
		private String vendorName;
		private BigDecimal creditsApplied;

		public Builder vendorName(String val) {
			this.vendorName = val;
			return this;
		}

		public Builder creditsApplied(BigDecimal val) {
			this.creditsApplied = val;
			return this;
		}
		
		public AppliedVendorCreditDto build()
		{
			return new AppliedVendorCreditDto(this);
		}
	}
}
