package com.ishoal.core.domain;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class OutstandingBalanceOfADay {
	private DateTime date;
	private BigDecimal balanceToBeTransferredToSupplier;
	private BigDecimal balanceToBePaidToLapwing;

	public OutstandingBalanceOfADay() {

	}

	public OutstandingBalanceOfADay(Builder builder) {
		this.date = builder.date;
		this.balanceToBeTransferredToSupplier = builder.balanceToBeTransferredToSupplier;
		this.balanceToBePaidToLapwing = builder.balanceToBePaidToLapwing;
	}

	public static Builder anOutstandingBalanceOfADay() {
		return new Builder();
	}

	public DateTime getDate() {
		return date;
	}

	public BigDecimal getBalanceToBeTransferredToSupplier() {
		return balanceToBeTransferredToSupplier;
	}

	public BigDecimal getBalanceToBePaidToLapwing() {
		return balanceToBePaidToLapwing;
	}

	public static class Builder {
		private DateTime date;
		private BigDecimal balanceToBeTransferredToSupplier;
		private BigDecimal balanceToBePaidToLapwing;

		public Builder date(DateTime val) {
			this.date = val;
			return this;
		}

		public Builder balanceToBeTransferredToSupplier(BigDecimal val) {
			this.balanceToBeTransferredToSupplier = val;
			return this;
		}

		public Builder balanceToBePaidToLapwing(BigDecimal val) {
			this.balanceToBePaidToLapwing = val;
			return this;
		}

		public OutstandingBalanceOfADay build() {
			return new OutstandingBalanceOfADay(this);
		}

	}
}
