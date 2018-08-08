package com.ishoal.core.domain;

public class OrderReturn {

	private Long id;
	private OrderId orderId;
	private OrderLineId orderLineId;
	private long quantity;

	private OrderReturn(Builder builder) {
		this.id = builder.id;
		this.orderId = builder.orderId;
		this.orderLineId = builder.orderLineId;
		this.quantity = builder.quantity;
	}

	public static Builder anOrderReturn() {
		return new Builder();
	}

	public Long getId() {
		return id;
	}

	public OrderId getOrderId() {
		return orderId;
	}

	public OrderLineId getOrderLineId() {
		return orderLineId;
	}

	public long getQuantity() {
		return quantity;
	}

	public static class Builder {

		private Long id;
		private OrderId orderId;
		private OrderLineId orderLineId;
		private long quantity;

		private Builder() {
			super();
		}

		public Builder id(Long val) {

			id = val;
			return this;
		}

		public Builder orderLineId(OrderLineId id) {
			this.orderLineId = id;
			return this;
		}

		public Builder orderId(OrderId id) {
			this.orderId = id;
			return this;
		}

		public Builder quantity(long quantity) {
			this.quantity = quantity;
			return this;
		}

		public OrderReturn build() {
			return new OrderReturn(this);
		}
	}

}
