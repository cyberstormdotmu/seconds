package com.ishoal.core.domain;

import static org.joda.time.DateTime.now;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.common.domain.OrderReference;

public class Order {

	private final OrderId id;
	private final long version;
	private final User buyer;
	private final Organisation buyerOrganisation;
	private final Vendor vendor;
	private final OrderReference reference;
	private final DateTime created;
	private final DateTime modified;
	private OrderLines lines;
	private final Address invoiceAddress;
	private final Address deliveryAddress;
	private final OrderPayments payments;
	private final BuyerAppliedCredits appliedCredits;
	private DateTime invoiceDate;
	private OrderStatus status;
	private PaymentStatus paymentStatus;
	private DateTime dueDate;

	private Order(Builder builder) {
		this.id = builder.id;
		this.version = builder.version;
		this.buyer = builder.buyer;
		this.buyerOrganisation = builder.buyerOrganisation;
		this.vendor = builder.vendor;
		this.reference = builder.reference;
		this.invoiceDate = builder.invoiceDate;
		this.created = builder.created;
		this.modified = builder.modified;
		this.status = builder.status;
		this.paymentStatus = builder.paymentStatus;
		this.dueDate = builder.dueDate;
		this.invoiceAddress = builder.invoiceAddress;
		this.deliveryAddress = builder.deliveryAddress;
		this.lines = OrderLines.over(builder.lines);
		this.payments = OrderPayments.over(builder.payments);
		this.appliedCredits = BuyerAppliedCredits.over(builder.appliedCredits);

	}

	public static Order.Builder anOrder() {
		return new Builder();
	}

	public OrderId getId() {
		return id;
	}

	public long getVersion() {
		return version;
	}

	public User getBuyer() {
		return buyer;
	}

	public Organisation getBuyerOrganisation() {
		return buyerOrganisation;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public OrderReference getReference() {
		return reference;
	}

	public DateTime getInvoiceDate() {
		return invoiceDate;
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getModified() {
		return modified;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public DateTime getDueDate() {
		return dueDate;
	}

	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public OrderLines getLines() {
		return lines;
	}

	public TaxableAmount getAvailableCreditBalance() {
		return lines.getAvailableCreditBalance();
	}

	public OrderPayments getPayments() {
		return payments;
	}

	public BuyerAppliedCredits getAppliedCredits() {
		return appliedCredits;
	}

	public TaxableAmount getTotal() {
		return lines.getTotal();
	}

	public TaxableAmount getCreditTotal() {
		return lines.getCreditTotal();
	}

	public BigDecimal getPaymentTotal() {
		return payments.getTotal();
	}

	public TaxableAmount getAppliedCreditTotal() {
		return appliedCredits.getTotal();
	}

	public BigDecimal getUnpaidAmount() {
		return getTotal().gross().subtract(getPaymentTotal()).subtract(getAppliedCreditTotal().gross())
				.add(getUsedVendorCredits());
	}

	public BigDecimal getAmountPaid() {
		return getPaymentTotal().add(getAppliedCreditTotal().gross()).subtract(getUsedVendorCredits());
	}

	public BigDecimal getUsedVendorCredits() {
		BigDecimal usedVendorCredits = BigDecimal.ZERO;
		for (BuyerAppliedCredit credit : appliedCredits) {
			if (credit.getSpendType().compareTo(CreditMovementType.V_SPEND) == 0
					&& credit.getStatus().compareTo(BuyerAppliedCreditStatus.CANCELLED) != 0) {
				usedVendorCredits = usedVendorCredits.add(credit.getAmount().gross());
			}
		}
		return usedVendorCredits;
	}

	public void updateOrderLines(OrderLines lines)
	{
		this.lines = lines;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Order)) {
			return false;
		}

		Order order = (Order) o;

		return new EqualsBuilder().append(reference, order.reference).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(reference).toHashCode();
	}

	public Result confirm(int dueDays) {
		if (!status.canTransitionTo(OrderStatus.CONFIRMED)) {
			return SimpleResult.error("The order is not in a state where it can be confirmed");
		}
		status = OrderStatus.CONFIRMED;
		invoiceDate = now().withTimeAtStartOfDay();
		if (paymentStatus != PaymentStatus.PAID) {
			dueDate = now().plusDays(dueDays).withTimeAtStartOfDay();
		}
		return SimpleResult.success();
	}

	public Result cancel() {
		if (!status.canTransitionTo(OrderStatus.CANCELLED)) {
			return SimpleResult.error("The order is not in a state where it can be cancelled");
		}
		status = OrderStatus.CANCELLED;
		return SimpleResult.success();
	}

	public Result addPayment(OrderPayment payment) {
		BigDecimal unpaid = getUnpaidAmount();
		BigDecimal paymentAmt = payment.getAmount();
		if (paymentAmt.compareTo(unpaid) > 0) {
			return SimpleResult.error("The payment to be added (£" + payment.getAmount() + ") is greater than "
					+ "the amount outstanding (£" + getUnpaidAmount() + ")");
		}

		payments.add(payment);
		recalculatePaymentStatus();

		return SimpleResult.success();
	}

	public OrderPayment searchForPayment(PaymentReference paymentReference) {
		for (OrderPayment payment : payments) {
			if (payment.getPaymentReference().equals(paymentReference)) {
				return payment;
			}
		}
		return null;
	}

	public int calculateAppliedVendorCreditsforOrder() {
		int count = 0;
		for (BuyerAppliedCredit credit : getAppliedCredits()) {
			if (credit.getSpendType().compareTo(CreditMovementType.V_SPEND) == 0) {
				count++;
			}
		}
		return count;
	}

	public int calculateAppliedLapwingCreditsforOrder() {
		int count = 0;
		for (BuyerAppliedCredit credit : getAppliedCredits()) {
			if (credit.getSpendType().compareTo(CreditMovementType.SPEND) == 0) {
				count++;
			}
		}
		return count;
	}
	
	public int calculateTotalQuantitesOrdered()
	{
		int totalquantites = 0;
		for(OrderLine line: lines)
		{
			totalquantites += line.getQuantity();
		}
		return totalquantites;
	}

	public BigDecimal calculateAmountOfAppliedLapwingCreditsforOrder() {
		BigDecimal appliedLapwingCredits = BigDecimal.ZERO;
		for (BuyerAppliedCredit credit : getAppliedCredits()) {
			if (credit.getSpendType().compareTo(CreditMovementType.SPEND) == 0) {
				appliedLapwingCredits = appliedLapwingCredits.add(credit.getAmount().getGross());
			}
		}
		return appliedLapwingCredits;
	}

	public int calculateCardPaymentsAtCheckoutforOrder() {
		int count = 0;
		for (OrderPayment payment : getPayments()) {
			if (payment.getPaymentRecordType().compareTo(PaymentRecordType.ORDER_CHECKOUT_PAYMENT) == 0) {
				count++;
			}
		}
		return count;
	}

	public Result deletePayment(PaymentReference paymentReference) {
		if (!payments.remove(paymentReference)) {
			return SimpleResult.error("No payment with reference " + paymentReference + " found");
		}

		recalculatePaymentStatus();

		return SimpleResult.success();
	}

	private void recalculatePaymentStatus() {
		BigDecimal unpaidAmount = getUnpaidAmount();

		if (unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
			paymentStatus = PaymentStatus.PAID;
			dueDate = null;
		} else if (unpaidAmount.compareTo(getTotal().gross()) == 0) {
			paymentStatus = PaymentStatus.UNPAID;
		} else {
			paymentStatus = PaymentStatus.PART_PAID;
		}
	}

	public void updateStatus(OrderStatus status) {
		this.status = status;
	}

	public static class Builder {

		private OrderId id = OrderId.emptyOrderId;
		private long version;
		private User buyer;
		private Organisation buyerOrganisation;
		private Vendor vendor;
		private OrderReference reference = OrderReference.create();
		private DateTime invoiceDate;
		private DateTime created;
		private DateTime modified;
		private OrderStatus status;
		private PaymentStatus paymentStatus;
		private DateTime dueDate;
		private Address invoiceAddress;
		private Address deliveryAddress;
		private final List<OrderLine> lines;
		private final List<OrderPayment> payments;
		private final List<BuyerAppliedCredit> appliedCredits;

		public Builder() {
			this.lines = new ArrayList<>();
			this.payments = new ArrayList<>();
			this.appliedCredits = new ArrayList<>();
		}

		public Builder id(OrderId id) {
			this.id = id;
			return this;
		}

		public Builder version(long version) {
			this.version = version;
			return this;
		}

		public Builder buyer(User buyer) {
			this.buyer = buyer;
			return this;
		}

		public Builder buyerOrganisation(Organisation buyerOrganisation) {
			this.buyerOrganisation = buyerOrganisation;
			return this;
		}

		public Builder vendor(Vendor vendor) {
			this.vendor = vendor;
			return this;
		}

		public Builder reference(OrderReference reference) {
			this.reference = reference;
			return this;
		}

		public Builder invoiceDate(DateTime invoiceDate) {
			this.invoiceDate = invoiceDate;
			return this;
		}

		public Builder created(DateTime created) {
			this.created = created;
			return this;
		}

		public Builder modified(DateTime modified) {
			this.modified = modified;
			return this;
		}

		public Builder status(OrderStatus status) {
			this.status = status;
			return this;
		}

		public Builder paymentStatus(PaymentStatus paymentStatus) {
			this.paymentStatus = paymentStatus;
			return this;
		}

		public Builder dueDate(DateTime dueDate) {
			this.dueDate = dueDate;
			return this;
		}

		public Builder invoiceAddress(Address address) {
			this.invoiceAddress = address;
			return this;
		}

		public Builder deliveryAddress(Address address) {
			this.deliveryAddress = address;
			return this;
		}

		public Builder line(OrderLine line) {
			this.lines.add(line);
			return this;
		}

		public Builder lines(List<OrderLine> lines) {
			this.lines.addAll(lines);
			return this;
		}

		public Builder payment(OrderPayment payment) {
			this.payments.add(payment);
			return this;
		}

		public Builder payments(List<OrderPayment> payments) {
			this.payments.addAll(payments);
			return this;
		}

		public Builder appliedCredits(BuyerAppliedCredits appliedCredits) {
			this.appliedCredits.addAll(appliedCredits.list());
			return this;
		}

		public Order build() {
			return new Order(this);
		}
	}
}