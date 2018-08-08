package com.ishoal.core.domain;

import java.util.List;

public class SupplierPaymentsDetails {
	private List<SupplierPaymentForAnOrder> supplierPaymentForAnOrder;
	private List<OutstandingBalanceOfADay> outstandingBalanceOfADay;

	public SupplierPaymentsDetails() {

	}

	public SupplierPaymentsDetails(Builder builder) {
		this.supplierPaymentForAnOrder = builder.supplierPaymentForAnOrder;
		this.outstandingBalanceOfADay = builder.outstandingBalanceOfADay;
	}

	public Builder aSupplierPaymentsDetails() {
		return new Builder();
	}

	public List<SupplierPaymentForAnOrder> getSupplierPaymentForAnOrder() {
		return supplierPaymentForAnOrder;
	}

	public List<OutstandingBalanceOfADay> getOutstandingBalanceOfADay() {
		return outstandingBalanceOfADay;
	}

	public static class Builder {
		private List<SupplierPaymentForAnOrder> supplierPaymentForAnOrder;
		private List<OutstandingBalanceOfADay> outstandingBalanceOfADay;

		public Builder supplierPaymentForAnOrder(List<SupplierPaymentForAnOrder> val) {
			this.supplierPaymentForAnOrder = val;
			return this;
		}

		public Builder outstandingBalanceOfADay(List<OutstandingBalanceOfADay> val) {
			this.outstandingBalanceOfADay = val;
			return this;
		}

		public SupplierPaymentsDetails build() {
			return new SupplierPaymentsDetails(this);
		}
	}

}
