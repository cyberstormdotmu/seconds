package com.ishoal.ws.common.dto;

import com.ishoal.core.domain.Vendor;
import com.ishoal.ws.buyer.dto.BuyerProfileDto;

public class BuyerVendorCreditDto {
	private Long id;
	private Vendor vendor;
	private BuyerProfileDto buyer;
	private Double totalCredit;
	private Double availableCredit;
	private int paymentDueDays;

	public BuyerVendorCreditDto() {

	}

	private BuyerVendorCreditDto(Builder builder) {
		id = builder.id;
		vendor = builder.vendor;
		buyer = builder.buyer;
		totalCredit = builder.totalCredit;
		availableCredit = builder.availableCredit;
		paymentDueDays = builder.paymentDueDays;

	}

	public static Builder aBuyerVendorCreditDto() {
		return new Builder();
	}

	public Long getId() {
		return id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public BuyerProfileDto getBuyer() {
		return buyer;
	}

	public Double getTotalCredit() {
		return totalCredit;
	}

	public Double getAvailableCredit() {
		return availableCredit;
	}

	public int getPaymentDueDays() {
		return paymentDueDays;
	}

	public static final class Builder {
		private Long id;
		private Vendor vendor;
		private BuyerProfileDto buyer;
		private Double totalCredit;
		private Double availableCredit;
		private int paymentDueDays;

		public Builder() {

		}

		public Builder(BuyerVendorCreditDto copy) {

			id = copy.id;
			vendor = copy.vendor;
			buyer = copy.buyer;
			totalCredit = copy.totalCredit;
			availableCredit = copy.availableCredit;
			paymentDueDays = copy.paymentDueDays;

		}

		public Builder id(Long val) {
			id = val;
			return this;
		}

		public Builder vendor(Vendor val) {
			vendor = val;
			return this;
		}

		public Builder buyer(BuyerProfileDto val) {
			buyer = val;
			return this;
		}

		public Builder totalCredit(Double val) {
			totalCredit = val;
			return this;
		}

		public Builder availableCredit(Double val) {
			availableCredit = val;
			return this;
		}

		public Builder paymentDueDays(int val) {
			paymentDueDays = val;
			return this;
		}

		public BuyerVendorCreditDto build() {

			return new BuyerVendorCreditDto(this);
		}
	}
}