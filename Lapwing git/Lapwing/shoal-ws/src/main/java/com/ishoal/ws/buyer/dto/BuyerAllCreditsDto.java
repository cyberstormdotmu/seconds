package com.ishoal.ws.buyer.dto;

import java.util.List;

public class BuyerAllCreditsDto {
	private BuyerLapwingCreditsDto lapwingCredits;
	private List<BuyerVendorCreditsDto> vendorCredits;
	private PaymentChargesDto paymentCharges;

	public BuyerAllCreditsDto() {

	}

	public BuyerAllCreditsDto(Builder builder) {
		this.lapwingCredits = builder.lapwingCredits;
		this.vendorCredits = builder.vendorCredits;
		this.paymentCharges = builder.paymentCharges;
	}

	public static Builder aBuyerAllCreditsDto() {
		return new Builder();
	}

	public BuyerLapwingCreditsDto getLapwingCredits() {
		return lapwingCredits;
	}

	public List<BuyerVendorCreditsDto> getVendorCredits() {
		return vendorCredits;
	}

	public PaymentChargesDto getPaymentCharges() {
		return paymentCharges;
	}

	public static final class Builder {
		private BuyerLapwingCreditsDto lapwingCredits;
		private List<BuyerVendorCreditsDto> vendorCredits;
		private PaymentChargesDto paymentCharges;

		public Builder() {

		}

		public Builder(BuyerAllCreditsDto copy) {
			this.lapwingCredits = copy.lapwingCredits;
			this.vendorCredits = copy.vendorCredits;
			this.paymentCharges = copy.paymentCharges;
		}

		public Builder lapwingCredits(BuyerLapwingCreditsDto val) {
			this.lapwingCredits = val;
			return this;
		}

		public Builder vendorCredits(List<BuyerVendorCreditsDto> val) {
			this.vendorCredits = val;
			return this;
		}

		public Builder paymentCharges(PaymentChargesDto val) {
			this.paymentCharges = val;
			return this;
		}

		public BuyerAllCreditsDto build() {

			return new BuyerAllCreditsDto(this);
		}
	}
}
