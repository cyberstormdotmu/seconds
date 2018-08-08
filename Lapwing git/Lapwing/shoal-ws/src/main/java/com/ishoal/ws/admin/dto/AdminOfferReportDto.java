package com.ishoal.ws.admin.dto;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

public class AdminOfferReportDto {
	private String productCode;
	private String productName;
	private String vendorName;
	private DateTime offerStartDate;
	private DateTime offerEndDate;
	private boolean offerOpen;
	private long confirmedVolume;
	private long paidVolume;
	private long unpaidVolume;
	private long partiallyPaidVolume;
	private AdminPriceBandDto currentPriceBand;
	private BigDecimal buyerCreditsEarned;
	private BigDecimal buyerCreditsSpent;
	private BigDecimal fullPaymentsReceived;
	private BigDecimal unpaidAmount;
	private List<AdminPaymentDetailsOfOrderDto> allRouteDetails;
	private BigDecimal outstandingBalanceToSupplier;
	private BigDecimal outstandingBalanceToLapwing;
	private List<OutstandingBalanceOfADayDto> outstandingBalanceOfADay;
	private List<SupplierPaymentForAnOrderDto> supplierPayments;

	private AdminOfferReportDto() {
		super();
	}

	private AdminOfferReportDto(Builder builder) {
		this();
		productCode = builder.productCode;
		productName = builder.productName;
		vendorName = builder.vendorName;
		offerStartDate = builder.offerStartDate;
		offerEndDate = builder.offerEndDate;
		offerOpen = builder.offerOpen;
		confirmedVolume = builder.confirmedVolume;
		paidVolume = builder.paidVolume;
		unpaidVolume = builder.unpaidVolume;
		partiallyPaidVolume = builder.partiallyPaidVolume;
		currentPriceBand = builder.currentPriceBand;
		buyerCreditsEarned = builder.buyerCreditsEarned;
		buyerCreditsSpent = builder.buyerCreditsSpent;
		fullPaymentsReceived = builder.fullPaymentsReceived;
		unpaidAmount = builder.unpaidAmount;
		allRouteDetails = builder.allRouteDetails;
		outstandingBalanceToSupplier = builder.outstandingBalanceToSupplier;
		outstandingBalanceToLapwing = builder.outstandingBalanceToLapwing;
		outstandingBalanceOfADay = builder.outstandingBalanceOfADay;
		supplierPayments = builder.supplierPayments;
	}

	public static Builder anOfferReport() {
		return new Builder();
	}

	public String getProductCode() {
		return productCode;
	}

	public String getProductName() {
		return productName;
	}

	public DateTime getOfferStartDate() {
		return offerStartDate;
	}

	public DateTime getOfferEndDate() {
		return offerEndDate;
	}

	public boolean isOfferOpen() {
		return offerOpen;
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

	public AdminPriceBandDto getCurrentPriceBand() {
		return currentPriceBand;
	}

	public BigDecimal getBuyerCreditsEarned() {
		return buyerCreditsEarned;
	}

	public BigDecimal getBuyerCreditsSpent() {
		return buyerCreditsSpent;
	}

	public BigDecimal getFullPaymentsReceived() {
		return fullPaymentsReceived;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public String getVendorName() {
		return vendorName;
	}


	public BigDecimal getOutstandingBalanceToSupplier() {
		return outstandingBalanceToSupplier;
	}

	public BigDecimal getOutstandingBalanceToLapwing() {
		return outstandingBalanceToLapwing;
	}

	public List<AdminPaymentDetailsOfOrderDto> getAllRouteDetails() {
		return allRouteDetails;
	}

	public List<OutstandingBalanceOfADayDto> getOutstandingBalanceOfADay() {
		return outstandingBalanceOfADay;
	}

	public List<SupplierPaymentForAnOrderDto> getSupplierPayments() {
		return supplierPayments;
	}

	public static final class Builder {
		private String productCode;
		private String productName;
		private String vendorName;
		private DateTime offerStartDate;
		private DateTime offerEndDate;
		private boolean offerOpen;
		private long confirmedVolume;
		private long paidVolume;
		private long unpaidVolume;
		private long partiallyPaidVolume;
		private AdminPriceBandDto currentPriceBand;
		private BigDecimal buyerCreditsEarned;
		private BigDecimal buyerCreditsSpent;
		private BigDecimal fullPaymentsReceived;
		private BigDecimal unpaidAmount;
		private List<AdminPaymentDetailsOfOrderDto> allRouteDetails;
		private BigDecimal outstandingBalanceToSupplier;
		private BigDecimal outstandingBalanceToLapwing;
		private List<OutstandingBalanceOfADayDto> outstandingBalanceOfADay;
		private List<SupplierPaymentForAnOrderDto> supplierPayments;

		private Builder() {
		}

		public Builder productCode(String val) {
			productCode = val;
			return this;
		}

		public Builder productName(String val) {
			productName = val;
			return this;
		}

		public Builder vendorName(String val) {
			vendorName = val;
			return this;
		}

		public Builder offerStartDate(DateTime val) {
			offerStartDate = val;
			return this;
		}

		public Builder offerEndDate(DateTime val) {
			offerEndDate = val;
			return this;
		}

		public Builder offerOpen(boolean val) {
			offerOpen = val;
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

		public Builder currentPriceBand(AdminPriceBandDto val) {
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

		public Builder fullPaymentsReceived(BigDecimal val) {
			fullPaymentsReceived = val;
			return this;
		}

		public Builder unpaidAmount(BigDecimal val) {
			unpaidAmount = val;
			return this;
		}

		public Builder outstandingBalanceToSupplier(BigDecimal val) {
			this.outstandingBalanceToSupplier = val;
			return this;
		}

		public Builder outstandingBalanceToLapwing(BigDecimal val) {
			this.outstandingBalanceToLapwing = val;
			return this;
		}

		public Builder allRouteDetails(List<AdminPaymentDetailsOfOrderDto> val) {
			allRouteDetails = val;
			return this;
		}

		public Builder outstandingBalanceOfADay(List<OutstandingBalanceOfADayDto> val) {
			this.outstandingBalanceOfADay = val;
			return this;
		}

		public Builder supplierPayments(List<SupplierPaymentForAnOrderDto> val) {
			this.supplierPayments = val;
			return this;
		}

		public AdminOfferReportDto build() {
			return new AdminOfferReportDto(this);
		}
	}
}
