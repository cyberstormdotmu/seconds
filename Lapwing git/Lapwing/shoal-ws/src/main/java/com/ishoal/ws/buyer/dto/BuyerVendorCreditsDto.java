package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;

public class BuyerVendorCreditsDto {

    private String buyerName;
	private String vendorName;
	private BigDecimal totalCredits;
	private BigDecimal availableCredits;

	public BuyerVendorCreditsDto() {

	}

	public BuyerVendorCreditsDto(Builder builder) {
	    this.buyerName = builder.buyerName;
		this.vendorName = builder.vendorName;
		this.totalCredits = builder.totalCredits;
		this.availableCredits = builder.availableCredits;
	}

	public static Builder aBuyerVendorCreditsDto() {
		return new Builder();
	}

	public String getVendorName() {
		return vendorName;
	}

	public BigDecimal getTotalCredits() {
		return totalCredits;
	}

	public BigDecimal getAvailableCredits() {
		return availableCredits;
	}

	public String getBuyerName() {
        return buyerName;
    }

    public static final class Builder {
		private String vendorName;
		private String buyerName;
		private BigDecimal totalCredits;
		private BigDecimal availableCredits;

		public Builder() {

		}

		public Builder(BuyerVendorCreditsDto copy) {
		    this.buyerName = copy.buyerName;
			this.vendorName = copy.vendorName;
			this.totalCredits = copy.totalCredits;
			this.availableCredits = copy.availableCredits;
		}

		public Builder buyerName(String val) {
            this.buyerName = val;
            return this;
        }
		
		public Builder vendorName(String val) {
			this.vendorName = val;
			return this;
		}

		public Builder totalCredits(BigDecimal val) {
			this.totalCredits = val;
			return this;
		}

		public Builder availableCredits(BigDecimal val) {
			this.availableCredits = val;
			return this;
		}

		public BuyerVendorCreditsDto build() {

			return new BuyerVendorCreditsDto(this);
		}
	}
}
