package com.ishoal.core.orders;

import java.math.BigDecimal;

public class AppliedVendorCredit {
	private String vendorName;
	private BigDecimal creditsApplied;

	private AppliedVendorCredit(Builder builder) {
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

		public AppliedVendorCredit build() {
			return new AppliedVendorCredit(this);
		}
	}

}
