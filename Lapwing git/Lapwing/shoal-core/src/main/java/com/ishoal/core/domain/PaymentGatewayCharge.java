package com.ishoal.core.domain;

import java.math.BigDecimal;

public class PaymentGatewayCharge {

	private String name;
	private BigDecimal paymentChargesPercentage;
	private BigDecimal paymentExtraCharge;

	public PaymentGatewayCharge() {

	}

	public PaymentGatewayCharge(Builder builder) {
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

		public Builder(PaymentGatewayCharge copy) {
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

        public PaymentGatewayCharge build() {
            return new PaymentGatewayCharge(this);
        }
	}
}