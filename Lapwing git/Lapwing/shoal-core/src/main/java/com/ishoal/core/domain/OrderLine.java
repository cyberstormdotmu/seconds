package com.ishoal.core.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class OrderLine {

	private final OrderLineId id;
	private final Product product;
	private final Offer offer;
	private final long quantity;
	private final PriceBand initialPriceBand;
	private PriceBand currentPriceBand;
	private final TaxableAmount amount;
	private final BigDecimal vatRate;
	private final BuyerCredits credits;
	private final DateTime created;
	private final DateTime modified;
	private long returnedQuantites;

	private OrderLine(Builder builder) {
		this.id = builder.id;
		this.created = builder.created;
		this.modified = builder.modified;
		this.product = builder.product;
		this.offer = builder.offer;
		this.quantity = builder.quantity;
		this.initialPriceBand = builder.initialPriceBand;
		this.currentPriceBand = builder.currentPriceBand;
		this.amount = builder.amount;
		this.vatRate = builder.vatRate;
		this.credits = BuyerCredits.over(builder.credits);
		this.returnedQuantites = builder.returnedQuantites;
	}

	public static Builder anOrderLine() {
		return new Builder();
	}

	public long getReturnedQuantites() {
		return returnedQuantites;
	}

	public OrderLineId getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public Offer getOffer() {
		return offer;
	}

	public long getQuantity() {
		return quantity;
	}

	public PriceBand getInitialPriceBand() {
		return this.initialPriceBand;
	}

	public PriceBand getCurrentPriceBand() {
		return this.currentPriceBand;
	}

	public TaxableAmount getAmount() {
		return amount;
	}

	public BigDecimal getVatRate() {
		return vatRate;
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getModified() {
		return modified;
	}

	public BuyerCredits getCredits() {
		return BuyerCredits.over(credits.list());
	}

	public void addCredit(BuyerCredit credit) {
		this.credits.add(credit);
	}

	public void updateCurrentPriceBand(PriceBand priceBand) {
		this.currentPriceBand = priceBand;
	}

	public void updateReturnedQuantites(long val)
	{
		returnedQuantites = val;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof OrderLine)) {
			return false;
		}
		OrderLine other = (OrderLine) o;
		return id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public TaxableAmount getCreditTotal() {
		return credits.getTotal();
	}

	public TaxableAmount getConsumedCreditTotal() {
		return credits.getConsumedCreditTotal();
	}

	public TaxableAmount getAvailableCreditBalance() {
		return credits.getAvailableCreditBalance();
	}

	public static class Builder {

		private OrderLineId id = OrderLineId.emptyOrderLineId;
		private Product product;
		private Offer offer;
		private long quantity;
		private PriceBand initialPriceBand;
		private PriceBand currentPriceBand;
		private TaxableAmount amount;
		private BigDecimal vatRate;
		private List<BuyerCredit> credits = new ArrayList<>();
		private DateTime created;
		private DateTime modified;
		private long returnedQuantites;

		private Builder() {
			super();
		}

		public Builder id(OrderLineId id) {
			this.id = id;
			return this;
		}

		public Builder product(Product product) {
			this.product = product;
			return this;
		}

		public Builder offer(Offer offer) {
			this.offer = offer;
			return this;
		}

		public Builder quantity(long quantity) {
			this.quantity = quantity;
			return this;
		}

		public Builder initialPriceBand(PriceBand priceBand) {
			this.initialPriceBand = priceBand;
			return this;
		}

		public Builder currentPriceBand(PriceBand priceBand) {
			this.currentPriceBand = priceBand;
			return this;
		}

		public Builder amount(TaxableAmount amount) {
			this.amount = amount;
			return this;
		}

		public Builder vatRate(BigDecimal vatRate) {
			this.vatRate = vatRate;
			return this;
		}

		public Builder credit(BuyerCredit credit) {
			this.credits.add(credit);
			return this;
		}

		public Builder credits(List<BuyerCredit> credits) {
			this.credits.addAll(credits);
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

		public Builder returnedQuantites(long val) {
			returnedQuantites = val;
			return this;
		}

		public OrderLine build() {
			return new OrderLine(this);
		}
	}
}