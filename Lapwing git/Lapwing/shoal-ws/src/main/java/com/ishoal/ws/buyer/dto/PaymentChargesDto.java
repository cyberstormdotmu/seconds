package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;

public class PaymentChargesDto {
	private String name;
	private BigDecimal paymentChargesPercentage;
	private BigDecimal paymentExtraCharge;

	public PaymentChargesDto() {

	}

	public PaymentChargesDto(Builder builder) {
		this.name = builder.name;
		this.paymentChargesPercentage = builder.paymentChargesPercentage;
		this.paymentExtraCharge = builder.paymentExtraCharge;

	}

	public static Builder aPaymentChargesDto() {
		return new Builder();
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPaymentChargesPercentage() {
		return paymentChargesPercentage;
	}

	public BigDecimal getPaymentExtraCharge() {
		return paymentExtraCharge;
	}

	public static final class Builder {

		private String name;
		private BigDecimal paymentChargesPercentage;
		private BigDecimal paymentExtraCharge;

		public Builder() {

		}

		public Builder(PaymentChargesDto copy) {
			this.name = copy.name;
			this.paymentChargesPercentage = copy.paymentChargesPercentage;
			this.paymentExtraCharge = copy.paymentExtraCharge;
		}

		public Builder name(String val) {
			this.name = val;
			return this;
		}

		public Builder paymentChargesPercentage(BigDecimal val) {
			this.paymentChargesPercentage = val;
			return this;
		}

		public Builder paymentExtraCharge(BigDecimal val) {
			this.paymentExtraCharge = val;
			return this;
		}

        public PaymentChargesDto build() {
            return new PaymentChargesDto(this);
        }
	}
}
