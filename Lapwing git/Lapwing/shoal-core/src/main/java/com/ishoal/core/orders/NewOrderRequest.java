package com.ishoal.core.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.PaymentMethod;
import com.ishoal.core.domain.User;
import com.ishoal.payment.buyer.PaymentCardToken;

public class NewOrderRequest {

	private final User buyer;
	private final NewOrderRequestOrderLines lines;
	private final AppliedVendorCredits appliedVendorCredits;
	private final BigDecimal creditToBeApplied;
	private final Address invoiceAddress;
	private final Address deliveryAddress;
	private final PaymentMethod paymentMethod;
	private final PaymentCardToken paymentCardToken;

	private NewOrderRequest(Builder builder) {
		this.buyer = builder.buyer;
		this.lines = NewOrderRequestOrderLines.over(builder.lines);
		this.creditToBeApplied = Optional.ofNullable(builder.creditToBeApplied).orElse(BigDecimal.ZERO);
		this.appliedVendorCredits = AppliedVendorCredits.over(builder.appliedVendorCredits);
		this.invoiceAddress = builder.invoiceAddress;
		this.deliveryAddress = builder.deliveryAddress;
		this.paymentMethod = builder.paymentMethod;
		this.paymentCardToken = builder.paymentCardToken;
	}

	public static NewOrderRequest.Builder aNewOrder() {
		return new Builder();
	}

	public User getBuyer() {
		return buyer;
	}

	public NewOrderRequestOrderLines getLines() {
		return lines;
	}

	public BigDecimal getCreditToBeApplied() {
		return creditToBeApplied;
	}

	public AppliedVendorCredits getAppliedVendorCredits() {
		return appliedVendorCredits;
	}

	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public PaymentCardToken getPaymentCardToken() {
		return paymentCardToken;
	}
	
	public BigDecimal calculateAppliedVendorCredits()
	{
		BigDecimal result=BigDecimal.ZERO;
		for(AppliedVendorCredit credit : appliedVendorCredits)
		{
			result = result.add(credit.getCreditsApplied());
		}
		return result;
	}

	public static class Builder {

		private User buyer;
		private final List<NewOrderRequestOrderLine> lines;
		private final List<AppliedVendorCredit> appliedVendorCredits;
		private BigDecimal creditToBeApplied;
		private Address invoiceAddress;
		private Address deliveryAddress;
		private PaymentMethod paymentMethod;
		private PaymentCardToken paymentCardToken;

		public Builder() {
			this.lines = new ArrayList<>();
			this.appliedVendorCredits = new ArrayList<>();
		}

		public Builder buyer(User buyer) {
			this.buyer = buyer;
			return this;
		}

		public Builder with(NewOrderRequestOrderLine line) {
			this.lines.add(line);
			return this;
		}

		public Builder with(List<NewOrderRequestOrderLine> lines) {
			this.lines.addAll(lines);
			return this;
		}

		
		public Builder with(AppliedVendorCredit vendorCredit) {
			this.appliedVendorCredits.add(vendorCredit);
			return this;
		}

		public Builder withCredits(List<AppliedVendorCredit> vendorCredits) {
			this.appliedVendorCredits.addAll(vendorCredits);
			return this;
		}

		
		public Builder creditToBeApplied(BigDecimal creditToBeApplied) {
			this.creditToBeApplied = creditToBeApplied;
			return this;
		}

		public Builder invoiceAddress(Address invoiceAddress) {
			this.invoiceAddress = invoiceAddress;
			return this;
		}

		public Builder deliveryAddress(Address deliveryAddress) {
			this.deliveryAddress = deliveryAddress;
			return this;
		}

		public Builder paymentMethod(PaymentMethod paymentMethod) {
			this.paymentMethod = paymentMethod;
			return this;
		}

		public Builder paymentCardToken(PaymentCardToken paymentCardToken) {
			this.paymentCardToken = paymentCardToken;
			return this;
		}

		public NewOrderRequest build() {
			return new NewOrderRequest(this);
		}
	}
}