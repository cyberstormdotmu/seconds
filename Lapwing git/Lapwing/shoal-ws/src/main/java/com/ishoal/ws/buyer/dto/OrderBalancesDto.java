package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;

public class OrderBalancesDto {

	private BigDecimal moneyOwnedBalance;
	private BigDecimal latePaymentBalance;
	private BigDecimal accountPaybleBalance;

	private OrderBalancesDto(Builder builder) {
		moneyOwnedBalance = builder.moneyOwnedBalance;
		latePaymentBalance = builder.latePaymentBalance;
		accountPaybleBalance = builder.accountPaybleBalance;
	}
	public static Builder someOrderBalances() {

		return new Builder();
	}

	public BigDecimal getMoneyOwnedBalance() {
		return moneyOwnedBalance;
	}

	public BigDecimal getLatePaymentBalance() {
		return latePaymentBalance;
	}

	public BigDecimal getAccountPaybleBalance() {
		return accountPaybleBalance;
	}

	public static final class Builder {
		private BigDecimal moneyOwnedBalance;
		private BigDecimal latePaymentBalance;
		private BigDecimal accountPaybleBalance;

		private Builder() {
		}

		public Builder moneyOwnedBalance(BigDecimal val) {
			moneyOwnedBalance = val;
			return this;
		}

		public Builder latePaymentBalance(BigDecimal val) {
			latePaymentBalance = val;
			return this;
		}

		public Builder accountPaybleBalance(BigDecimal val) {
			accountPaybleBalance = val;
			return this;
		}

		public OrderBalancesDto build() {
			return new OrderBalancesDto(this);
		}
	}
}
