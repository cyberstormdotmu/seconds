package com.ishoal.core.domain;

import java.math.BigDecimal;

public class BuyerVendorCredit {
	private Long id;
	private Vendor vendor;
	private BuyerProfile buyer;
	private BigDecimal totalCredit;
	private BigDecimal availableCredit;
	private int paymentDueDays;
	
	
	private BuyerVendorCredit(Builder builder) {
		id = builder.id;
		vendor = builder.vendor;
		buyer = builder.buyer;
		totalCredit=builder.totalCredit;
		availableCredit = builder.availableCredit;
		paymentDueDays = builder.paymentDueDays;		
    }

    public static Builder aBuyerVendorCredit() {
        return new Builder();
    }
	public Long getId() {
		return id;
	}
	public BuyerProfile getBuyer() {
		return buyer;
	}
	public Vendor getVendor() {
		return vendor;
	}

	public BigDecimal getTotalCredit() {
		return totalCredit;
	}

	public BigDecimal getAvailableCredit() {
		return availableCredit;
	}

	public int getPaymentDueDays() {
		return paymentDueDays;
	}


	public static final class Builder {
		private Long id;
		private Vendor vendor;
		private BuyerProfile buyer;
		private BigDecimal totalCredit;
		private BigDecimal availableCredit;
		private int paymentDueDays;
        private Builder() {

        }

        public Builder id(Long val) {
        	id = val;
            return this;
        }
        public Builder buyer(BuyerProfile val) {
        	buyer = val;
            return this;
        }
        public Builder vendor(Vendor val) {
        	vendor = val;
            return this;
        }
        public Builder totalCredit(BigDecimal val) {
        	totalCredit = val;
            return this;
        }
        public Builder availableCredit(BigDecimal val) {
        	availableCredit = val;
            return this;
        }
        public Builder paymentDueDays(int val) {
        	paymentDueDays = val;
            return this;
        }
        public BuyerVendorCredit build() {
            return new BuyerVendorCredit(this);
        }
	}
}
