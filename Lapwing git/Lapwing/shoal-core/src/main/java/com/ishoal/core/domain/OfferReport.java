package com.ishoal.core.domain;

import java.math.BigDecimal;
import java.util.List;

public class OfferReport {
	private final Product product;
	private final Offer offer;
	private final long confirmedVolume;
	private final long paidVolume;
	private final long unpaidVolume;
	private final long partiallyPaidVolume;
	private final PriceBand currentPriceBand;
	private final BigDecimal buyerCreditsEarned;
	private final BigDecimal buyerCreditsSpent;
	private final BigDecimal buyerPaymentsReceived;
	private final BigDecimal buyerPaymentsOutstanding;
	private List<AdminPaymentDetailsOfOrder> allRouteDetails;
	private BigDecimal outstandingBalanceToSupplier;
	private BigDecimal outstandingBalanceToLapwing;
	private List<OutstandingBalanceOfADay> outstandingBalanceOfADay;
	private List<SupplierPaymentForAnOrder> supplierPayments;

	private OfferReport(Builder builder) {
		product = builder.product;
		offer = builder.offer;
		confirmedVolume = builder.confirmedVolume;
		paidVolume = builder.paidVolume;
		unpaidVolume = builder.unpaidVolume;
		partiallyPaidVolume = builder.partiallyPaidVolume;
		currentPriceBand = builder.currentPriceBand;
		buyerCreditsEarned = builder.buyerCreditsEarned;
		buyerCreditsSpent = builder.buyerCreditsSpent;
		buyerPaymentsReceived = builder.buyerPaymentsReceived;
		buyerPaymentsOutstanding = builder.buyerPaymentsOutstanding;
		allRouteDetails = builder.allRouteDetails;
		outstandingBalanceToSupplier = builder.outstandingBalanceToSupplier;
		outstandingBalanceToLapwing = builder.outstandingBalanceToLapwing;
		outstandingBalanceOfADay = builder.outstandingBalanceOfADay;
		supplierPayments = builder.supplierPayments;
	}

	public static Builder anOfferReport() {
		return new Builder();
	}

	public Product getProduct() {
		return product;
	}

	public Offer getOffer() {
		return offer;
	}

	public long getConfirmedVolume() {
		return confirmedVolume;
	}

	public long getPaidVolume() {
		return paidVolume;
	}

	public long getUnpaidVolume() {
		return unpaidVolume;
	}

	public long getPartiallyPaidVolume() {
		return partiallyPaidVolume;
	}

	public PriceBand getCurrentPriceBand() {
		return currentPriceBand;
	}

	public BigDecimal getBuyerCreditsEarned() {
		return buyerCreditsEarned;
	}

	public BigDecimal getBuyerCreditsSpent() {
		return buyerCreditsSpent;
	}

	public BigDecimal getBuyerPaymentsReceived() {
		return buyerPaymentsReceived;
	}

	public BigDecimal getBuyerPaymentsOutstanding() {
		return buyerPaymentsOutstanding;
	}

	public List<AdminPaymentDetailsOfOrder> getAllRouteDetails() {
		return allRouteDetails;
	}

	public BigDecimal getOutstandingBalanceToSupplier() {
		return outstandingBalanceToSupplier;
	}

	public BigDecimal getOutstandingBalanceToLapwing() {
		return outstandingBalanceToLapwing;
	}

	public List<OutstandingBalanceOfADay> getOutstandingBalanceOfADay() {
		return outstandingBalanceOfADay;
	}

	public List<SupplierPaymentForAnOrder> getSupplierPayments() {
		return supplierPayments;
	}

	public static final class Builder {
		private static final BigDecimal ZERO = new BigDecimal("0.00");

		private Product product;
		private Offer offer;
		private long confirmedVolume;
		private long paidVolume;
		private long unpaidVolume;
		private long partiallyPaidVolume;
		private PriceBand currentPriceBand;
		private BigDecimal buyerCreditsEarned = ZERO;
		private BigDecimal buyerCreditsSpent = ZERO;
		private BigDecimal buyerPaymentsReceived = ZERO;
		private BigDecimal buyerPaymentsOutstanding = ZERO;
		private List<AdminPaymentDetailsOfOrder> allRouteDetails;
		private BigDecimal outstandingBalanceToSupplier;
		private BigDecimal outstandingBalanceToLapwing;
		private List<OutstandingBalanceOfADay> outstandingBalanceOfADay;
		private List<SupplierPaymentForAnOrder> supplierPayments;

		private Builder() {
		}

		public Builder outstandingBalanceToSupplier(BigDecimal val) {
			this.outstandingBalanceToSupplier = val;
			return this;
		}

		public Builder outstandingBalanceToLapwing(BigDecimal val) {
			this.outstandingBalanceToLapwing = val;
			return this;
		}

		public Builder product(Product val) {
			product = val;
			return this;
		}

		public Builder offer(Offer val) {
			offer = val;
			return this;
		}

		public Builder confirmedVolume(long val) {
			confirmedVolume = val;
			return this;
		}

		public Builder paidVolume(long val) {
			paidVolume = val;
			return this;
		}

		public Builder unpaidVolume(long val) {
			unpaidVolume = val;
			return this;
		}

		public Builder partiallyPaidVolume(long val) {
			partiallyPaidVolume = val;
			return this;
		}

		public Builder currentPriceBand(PriceBand val) {
			currentPriceBand = val;
			return this;
		}

		public Builder buyerCreditsEarned(BigDecimal val) {
			buyerCreditsEarned = val;
			return this;
		}

		public Builder buyerCreditsSpent(BigDecimal val) {
			buyerCreditsSpent = val;
			return this;
		}

		public Builder buyerPaymentsReceived(BigDecimal val) {
			buyerPaymentsReceived = val;
			return this;
		}

		public Builder buyerPaymentsOutstanding(BigDecimal val) {
			buyerPaymentsOutstanding = val;
			return this;
		}

		public Builder allRouteDetails(List<AdminPaymentDetailsOfOrder> val) {
			allRouteDetails = val;
			return this;
		}

		public Builder outstandingBalanceOfADay(List<OutstandingBalanceOfADay> val) {
			outstandingBalanceOfADay = val;
			return this;
		}

		public Builder supplierPayments(List<SupplierPaymentForAnOrder> val) {
			this.supplierPayments = val;
			return this;
		}

		public OfferReport build() {
			return new OfferReport(this);
		}
	}
}