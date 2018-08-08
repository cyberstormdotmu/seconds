package com.ishoal.ws.admin.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ishoal.ws.common.dto.AddressDto;
import com.ishoal.ws.common.dto.OrderLineDto;
import com.ishoal.ws.common.dto.OrderSummaryDto;

public class AdminOrderDto {

	private OrderSummaryDto summary;
	private long version;
	private AdminOrderBuyerDto buyer;
	private AddressDto invoiceAddress;
	private AddressDto deliveryAddress;
	private List<OrderLineDto> lines;
	private List<AdminOrderPaymentDto> payments;
	private List<AdminAppliedCreditDto> appliedCredits;

	private AdminOrderDto() {
		super();
	}

	private AdminOrderDto(Builder builder) {
		summary = builder.summary;
		version = builder.version;
		buyer = builder.buyer;
		invoiceAddress = builder.invoiceAddress;
		deliveryAddress = builder.deliveryAddress;
		lines = builder.lines;
		payments = builder.payments;
		appliedCredits = builder.appliedCredits;

	}

	public static Builder anOrder() {
		return new Builder();
	}

	public OrderSummaryDto getSummary() {
		return summary;
	}

	public long getVersion() {
		return version;
	}

	public AdminOrderBuyerDto getBuyer() {
		return buyer;
	}

	public AddressDto getInvoiceAddress() {
		return invoiceAddress;
	}

	public AddressDto getDeliveryAddress() {
		return deliveryAddress;
	}

	public List<OrderLineDto> getLines() {
		return Collections.unmodifiableList(lines);
	}

	public List<AdminOrderPaymentDto> getPayments() {
		return Collections.unmodifiableList(payments);
	}

	public List<AdminAppliedCreditDto> getAppliedCredits() {
		return appliedCredits;
	}

	public static final class Builder {
		private OrderSummaryDto summary;
		private long version;
		private AdminOrderBuyerDto buyer;
		private AddressDto invoiceAddress;
		private AddressDto deliveryAddress;
		private List<OrderLineDto> lines;
		private List<AdminOrderPaymentDto> payments;
		private List<AdminAppliedCreditDto> appliedCredits;

		private Builder() {
		}

		public Builder summary(OrderSummaryDto val) {
			summary = val;
			return this;
		}

		public Builder version(long val) {
			version = val;
			return this;
		}

		public Builder buyer(AdminOrderBuyerDto val) {
			buyer = val;
			return this;
		}

		public Builder invoiceAddress(AddressDto val) {
			invoiceAddress = val;
			return this;
		}

		public Builder deliveryAddress(AddressDto val) {
			deliveryAddress = val;
			return this;
		}

		public Builder lines(List<OrderLineDto> val) {
			lines = new ArrayList<>(val);
			return this;
		}

		public Builder payments(List<AdminOrderPaymentDto> val) {
			payments = new ArrayList<>(val);
			return this;
		}

		public Builder appliedCredits(List<AdminAppliedCreditDto> val) {
			appliedCredits = new ArrayList<>(val);
			return this;
		}

		public AdminOrderDto build() {
			return new AdminOrderDto(this);
		}
	}
}
