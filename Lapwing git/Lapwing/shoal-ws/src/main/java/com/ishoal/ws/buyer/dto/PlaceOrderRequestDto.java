package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ishoal.ws.common.dto.AddressDto;

public class PlaceOrderRequestDto {

	private List<PlaceOrderRequestOrderLineDto> lines;
	private List<AppliedVendorCreditDto> appliedVendorCredits;
	private BigDecimal creditToBeApplied;
	private AddressDto invoiceAddress;
	private AddressDto deliveryAddress;
	private String paymentMethod;
	private String paymentCardToken;

	private PlaceOrderRequestDto() {
		this.lines = new ArrayList<>();
		this.appliedVendorCredits = new ArrayList<>();
	}

	private PlaceOrderRequestDto(Builder builder) {
		this();
		this.lines.addAll(builder.lines);
		this.creditToBeApplied = builder.creditToBeApplied;
		this.invoiceAddress = builder.invoiceAddress;
		this.deliveryAddress = builder.deliveryAddress;
		this.paymentMethod = builder.paymentMethod;
		this.paymentCardToken = builder.paymentCardToken;
	}

	public static Builder aPlaceOrderRequest() {
		return new Builder();
	}

	public List<AppliedVendorCreditDto> getAppliedVendorCredits() {
		return appliedVendorCredits;
	}

	public List<PlaceOrderRequestOrderLineDto> getLines() {
		return lines;
	}

	public BigDecimal getCreditToBeApplied() {
		return creditToBeApplied;
	}

	public AddressDto getInvoiceAddress() {
		return invoiceAddress;
	}

	public AddressDto getDeliveryAddress() {
		return deliveryAddress;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public String getPaymentCardToken() {
		return paymentCardToken;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public static class Builder {

		private final List<PlaceOrderRequestOrderLineDto> lines;
		private BigDecimal creditToBeApplied;
		private AddressDto invoiceAddress;
		private AddressDto deliveryAddress;
		private String paymentMethod;
		private String paymentCardToken;
		private List<AppliedVendorCreditDto> appliedVendorCredits;

		private Builder() {
			this.lines = new ArrayList<>();
			this.appliedVendorCredits = new ArrayList<>();
		}

		public Builder line(PlaceOrderRequestOrderLineDto.Builder line) {
			this.lines.add(line.build());
			return this;
		}

		public Builder appliedVendorCredit(AppliedVendorCreditDto.Builder vendorCredit) {
			this.appliedVendorCredits.add(vendorCredit.build());
			return this;
		}

		public Builder creditToBeApplied(BigDecimal creditToBeApplied) {
			this.creditToBeApplied = creditToBeApplied;
			return this;
		}

		public Builder invoiceAddress(AddressDto invoiceAddress) {
			this.invoiceAddress = invoiceAddress;
			return this;
		}

		public Builder deliveryAddress(AddressDto deliveryAddress) {
			this.deliveryAddress = deliveryAddress;
			return this;
		}

		public Builder paymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
			return this;
		}

		public Builder paymentCardToken(String paymentCardToken) {
			this.paymentCardToken = paymentCardToken;
			return this;
		}

		public PlaceOrderRequestDto build() {
			return new PlaceOrderRequestDto(this);
		}
	}
}