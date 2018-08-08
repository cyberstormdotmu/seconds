package com.ishoal.core.domain;

import java.math.BigDecimal;

public class RoutePaymentDetails {

	private BigDecimal unitPrice;
	private long quantitiesConfirmed;
	private int numberOfPaymentsReceived;
	private int numberOfPaymentsPending;
	private int numberOfPartialPayments;
	private BigDecimal discountMoniesPerOrder;
	private int numberOfOrderPlaced;
	private int paidQuantities;
	private int unpaiQuantities;
	private int partpaidQuantities;

	public RoutePaymentDetails() {
	}

	public RoutePaymentDetails(Builder builder) {
		this.unitPrice = builder.unitPrice;
		this.quantitiesConfirmed = builder.quantitiesConfirmed;
		this.numberOfPaymentsReceived = builder.numberOfPaymentsReceived;
		this.numberOfPaymentsPending = builder.numberOfPaymentsPending;
		this.discountMoniesPerOrder = builder.discountMoniesPerOrder;
		this.numberOfPartialPayments = builder.numberOfPartialPayments;
		this.paidQuantities = builder.paidQuantities;
		this.unpaiQuantities = builder.unpaiQuantities;
		this.partpaidQuantities = builder.partpaidQuantities;
		this.numberOfOrderPlaced = builder.numberOfOrderPlaced;
	}

	public static Builder aRoutePaymentDetails() {
		return new Builder();
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public long getQuantitiesConfirmed() {
		return quantitiesConfirmed;
	}

	public int getNumberOfPaymentsReceived() {
		return numberOfPaymentsReceived;
	}

	public int getNumberOfPaymentsPending() {
		return numberOfPaymentsPending;
	}

	public int getNumberOfPartialPayments() {
		return numberOfPartialPayments;
	}

	public BigDecimal getDiscountMoniesPerOrder() {
		return discountMoniesPerOrder;
	}

	public int getPaidQuantities() {
		return paidQuantities;
	}

	public int getUnpaiQuantities() {
		return unpaiQuantities;
	}

	public int getPartpaidQuantities() {
		return partpaidQuantities;
	}

	public int getnumberOfOrderPlaced() {
		return numberOfOrderPlaced;
	}

	public static final class Builder {
		private BigDecimal unitPrice;
		private long quantitiesConfirmed;
		private int numberOfPaymentsReceived;
		private int numberOfPaymentsPending;
		private int numberOfPartialPayments;
		private BigDecimal discountMoniesPerOrder;
		private int paidQuantities;
		private int unpaiQuantities;
		private int partpaidQuantities;
		private int numberOfOrderPlaced;

		private Builder() {

		}

		public Builder unitPrice(BigDecimal val) {
			unitPrice = val;
			return this;
		}

		public Builder quantitiesConfirmed(long val) {
			quantitiesConfirmed = val;
			return this;
		}

		public Builder numberOfPaymentsReceived(int val) {
			numberOfPaymentsReceived = val;
			return this;
		}

		public Builder numberOfPaymentsPending(int val) {
			numberOfPaymentsPending = val;
			return this;
		}

		public Builder numberOfPartialPayments(int val) {
			numberOfPartialPayments = val;
			return this;
		}

		public Builder discountMoniesPerOrder(BigDecimal val) {
			discountMoniesPerOrder = val;
			return this;
		}
		
		public Builder paidQuantities(int val) {
			paidQuantities = val;
			return this;
		}
		
		public Builder unpaiQuantities(int val) {
			unpaiQuantities = val;
			return this;
		}
		
		public Builder numberOfOrderPlaced(int val) {
			numberOfOrderPlaced = val;
			return this;
		}
		
		public Builder partpaidQuantities(int val) {
			partpaidQuantities = val;
			return this;
		}


		public RoutePaymentDetails build() {
			return new RoutePaymentDetails(this);
		}
	}
}