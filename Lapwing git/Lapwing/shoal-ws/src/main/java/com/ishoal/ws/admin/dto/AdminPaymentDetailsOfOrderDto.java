package com.ishoal.ws.admin.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class AdminPaymentDetailsOfOrderDto {
	private DateTime time;
	private String orderReference;
	private int quantities;
	private BigDecimal totalPricePaid;
	private BigDecimal minimumPrice;
	private BigDecimal lapwingCreditsUsed;
	private BigDecimal cardPaymentUsed;
	private BigDecimal supplierCreditsUsed;
	private BigDecimal totalDiscountMonies;
	private BigDecimal silverwingMargin;
	private BigDecimal balanceToBeTransferredToSupplier;
	private BigDecimal balanceToBePaidToLapwingexpected;
	private BigDecimal balanceToBePaidToLapwingactual;
	private BigDecimal silverwingMarginRetainedBySilverwing;
	private BigDecimal silverwingMarginPayableToSilverwing;
	private BigDecimal netMoneyExchangeSilverwingToSupplier;
	private BigDecimal netMoneyExchangeSupplierToSilverwing;

	public AdminPaymentDetailsOfOrderDto() {

	}

	public AdminPaymentDetailsOfOrderDto(Builder builder) {
		this.time = builder.time;
		this.orderReference = builder.orderReference;
		this.quantities = builder.quantities;
		this.totalPricePaid= builder.totalPricePaid;
		this.minimumPrice = builder.minimumPrice;
		this.lapwingCreditsUsed = builder.lapwingCreditsUsed;
		this.cardPaymentUsed = builder.cardPaymentUsed;
		this.supplierCreditsUsed = builder.supplierCreditsUsed;
		this.totalDiscountMonies = builder.totalDiscountMonies;
		this.silverwingMargin = builder.silverwingMargin;
		this.balanceToBeTransferredToSupplier = builder.balanceToBeTransferredToSupplier;
		this.balanceToBePaidToLapwingexpected = builder.balanceToBePaidToLapwingexpected;
		this.balanceToBePaidToLapwingactual = builder.balanceToBePaidToLapwingactual;
		this.silverwingMarginRetainedBySilverwing = builder.silverwingMarginRetainedBySilverwing;
		this.silverwingMarginPayableToSilverwing = builder.silverwingMarginPayableToSilverwing;
		this.netMoneyExchangeSilverwingToSupplier = builder.netMoneyExchangeSilverwingToSupplier;
		this.netMoneyExchangeSupplierToSilverwing = builder.netMoneyExchangeSupplierToSilverwing;
	}

	public static Builder anAdminPaymentDetailsOfOrderDto() {
		return new Builder();
	}

	public DateTime getTime() {
		return time;
	}

	public String getOrderReference() {
		return orderReference;
	}

	public int getQuantities() {
		return quantities;
	}

	public BigDecimal getTotalpricePaid() {
		return totalPricePaid;
	}

	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	public BigDecimal getLapwingCreditsUsed() {
		return lapwingCreditsUsed;
	}

	public BigDecimal getCardPaymentUsed() {
		return cardPaymentUsed;
	}

	public BigDecimal getSupplierCreditsUsed() {
		return supplierCreditsUsed;
	}

	public BigDecimal getTotalDiscountMonies() {
		return totalDiscountMonies;
	}

	public BigDecimal getSilverwingMargin() {
		return silverwingMargin;
	}

	public BigDecimal getBalanceToBeTransferredToSupplier() {
		return balanceToBeTransferredToSupplier;
	}

	public BigDecimal getBalanceToBePaidToLapwingexpected() {
		return balanceToBePaidToLapwingexpected;
	}

	public BigDecimal getBalanceToBePaidToLapwingactual() {
		return balanceToBePaidToLapwingactual;
	}

	public BigDecimal getSilverwingMarginRetainedBySilverwing() {
		return silverwingMarginRetainedBySilverwing;
	}

	public BigDecimal getSilverwingMarginPayableToSilverwing() {
		return silverwingMarginPayableToSilverwing;
	}

	public BigDecimal getNetMoneyExchangeSilverwingToSupplier() {
		return netMoneyExchangeSilverwingToSupplier;
	}

	public BigDecimal getNetMoneyExchangeSupplierToSilverwing() {
		return netMoneyExchangeSupplierToSilverwing;
	}

	public static final class Builder {
		private DateTime time;
		private String orderReference;
		private int quantities;
		private BigDecimal totalPricePaid;
		private BigDecimal minimumPrice;
		private BigDecimal lapwingCreditsUsed;
		private BigDecimal cardPaymentUsed;
		private BigDecimal supplierCreditsUsed;
		private BigDecimal totalDiscountMonies;
		private BigDecimal silverwingMargin;
		private BigDecimal balanceToBeTransferredToSupplier;
		private BigDecimal balanceToBePaidToLapwingexpected;
		private BigDecimal balanceToBePaidToLapwingactual;
		private BigDecimal silverwingMarginRetainedBySilverwing;
		private BigDecimal silverwingMarginPayableToSilverwing;
		private BigDecimal netMoneyExchangeSilverwingToSupplier;
		private BigDecimal netMoneyExchangeSupplierToSilverwing;

		public Builder time(DateTime val) {
			this.time = val;
			return this;
		}

		public Builder orderReference(String val) {
			this.orderReference = val;
			return this;
		}

		public Builder quantities(int val) {
			this.quantities = val;
			return this;
		}

		public Builder totalPricePaid(BigDecimal val) {
			this.totalPricePaid= val;
			return this;
		}

		public Builder minimumPrice(BigDecimal val) {
			this.minimumPrice = val;
			return this;
		}

		public Builder lapwingCreditsUsed(BigDecimal val) {
			this.lapwingCreditsUsed = val;
			return this;
		}

		public Builder cardPaymentUsed(BigDecimal val) {
			this.cardPaymentUsed = val;
			return this;
		}

		public Builder supplierCreditsUsed(BigDecimal val) {
			this.supplierCreditsUsed = val;
			return this;
		}

		public Builder totalDiscountMonies(BigDecimal val) {
			this.totalDiscountMonies = val;
			return this;
		}

		public Builder silverwingMargin(BigDecimal val) {
			this.silverwingMargin = val;
			return this;
		}

		public Builder balanceToBeTransferredToSupplier(BigDecimal val) {
			this.balanceToBeTransferredToSupplier = val;
			return this;
		}

		public Builder balanceToBePaidToLapwingexpected(BigDecimal val) {
			this.balanceToBePaidToLapwingexpected = val;
			return this;
		}

		public Builder balanceToBePaidToLapwingactual(BigDecimal val) {
			this.balanceToBePaidToLapwingactual = val;
			return this;
		}

		public Builder silverwingMarginRetainedBySilverwing(BigDecimal val) {
			this.silverwingMarginRetainedBySilverwing = val;
			return this;
		}

		public Builder silverwingMarginPayableToSilverwing(BigDecimal val) {
			this.silverwingMarginPayableToSilverwing = val;
			return this;
		}

		public Builder netMoneyExchangeSilverwingToSupplier(BigDecimal val) {
			this.netMoneyExchangeSilverwingToSupplier = val;
			return this;
		}

		public Builder netMoneyExchangeSupplierToSilverwing(BigDecimal val) {
			this.netMoneyExchangeSupplierToSilverwing = val;
			return this;
		}

		public AdminPaymentDetailsOfOrderDto build() {
			return new AdminPaymentDetailsOfOrderDto(this);
		}
	}
}
