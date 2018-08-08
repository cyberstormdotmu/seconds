package com.ishoal.core.domain;

import java.math.BigDecimal;
import java.util.List;

public class AdminRoutePayment {

	private List<RoutePaymentDetails> paymentDetails;
	private TaxableAmount amountReceived;
	private TaxableAmount amountPending;
	private TaxableAmount lapwingCreditsUsed;
	private TaxableAmount lapwingCreditsEarned;
	private BigDecimal totalLapwingMargin;
	private BigDecimal discountMoniesIncVat;
	private BigDecimal supplierBalance;
	private BigDecimal supplierKeeps;
	private BigDecimal lapwingBalance;
	private BigDecimal lapwingKeeps;

	public AdminRoutePayment() {

	}

	public AdminRoutePayment(Builder builder) {
		paymentDetails = builder.paymentDetails;
		amountReceived = builder.amountReceived;
		amountPending = builder.amountPending;
		lapwingCreditsUsed = builder.lapwingCreditsUsed;
		lapwingCreditsEarned = builder.lapwingCreditsEarned;
		totalLapwingMargin = builder.totalLapwingMargin;
		discountMoniesIncVat = builder.discountMoniesIncVat;
		supplierBalance = builder.supplierBalance;
		supplierKeeps = builder.supplierKeeps;
		lapwingBalance = builder.lapwingBalance;
		lapwingKeeps = builder.lapwingKeeps;
	}

	public static Builder anAdminRoutePaymentDto() {
		return new Builder();
	}

	public List<RoutePaymentDetails> getPaymentDetails() {
		return paymentDetails;
	}

	public TaxableAmount getAmountReceived() {
		return amountReceived;
	}

	public TaxableAmount getAmountPending() {
		return amountPending;
	}

	public TaxableAmount getLapwingCreditsUsed() {
		return lapwingCreditsUsed;
	}

	public TaxableAmount getLapwingCreditsEarned() {
		return lapwingCreditsEarned;
	}

	public BigDecimal getTotalLapwingMargin() {
		return totalLapwingMargin;
	}

	public BigDecimal getDiscountMoniesIncVat() {
		return discountMoniesIncVat;
	}

	public BigDecimal getSupplierBalance() {
		return supplierBalance;
	}

	public BigDecimal getSupplierKeeps() {
		return supplierKeeps;
	}

	public BigDecimal getLapwingBalance() {
		return lapwingBalance;
	}

	public BigDecimal getLapwingKeeps() {
		return lapwingKeeps;
	}

	public static final class Builder {
		private List<RoutePaymentDetails> paymentDetails;
		private TaxableAmount amountReceived;
		private TaxableAmount amountPending;
		private TaxableAmount lapwingCreditsUsed;
		private TaxableAmount lapwingCreditsEarned;
		private BigDecimal totalLapwingMargin;
		private BigDecimal discountMoniesIncVat;
		private BigDecimal supplierBalance;
		private BigDecimal supplierKeeps;
		private BigDecimal lapwingBalance;
		private BigDecimal lapwingKeeps;

		public Builder amountReceived(TaxableAmount val) {
			amountReceived = val;
			return this;
		}

		public Builder amountPending(TaxableAmount val) {
			amountPending = val;
			return this;
		}

		public Builder lapwingCreditsUsed(TaxableAmount val) {
			lapwingCreditsUsed = val;
			return this;
		}

		public Builder lapwingCreditsEarned(TaxableAmount val) {
			lapwingCreditsEarned = val;
			return this;
		}

		public Builder paymentDetails(List<RoutePaymentDetails> val) {
			paymentDetails = val;
			return this;
		}

		public Builder totalLapwingMargin(BigDecimal val) {
			totalLapwingMargin = val;
			return this;
		}

		public Builder discountMoniesIncVat(BigDecimal val) {
			discountMoniesIncVat = val;
			return this;
		}

		public Builder supplierBalance(BigDecimal val) {
			supplierBalance = val;
			return this;
		}

		public Builder supplierKeeps(BigDecimal val) {
			supplierKeeps = val;
			return this;
		}
		public Builder lapwingBalance(BigDecimal val) {
			lapwingBalance = val;
			return this;
		}
		
		public Builder lapwingKeeps(BigDecimal val) {
			lapwingKeeps = val;
			return this;
		}

		public AdminRoutePayment build() {
			return new AdminRoutePayment(this);
		}
	}
}
