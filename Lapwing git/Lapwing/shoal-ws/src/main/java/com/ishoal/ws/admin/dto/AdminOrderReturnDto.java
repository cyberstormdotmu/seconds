package com.ishoal.ws.admin.dto;

public class AdminOrderReturnDto {

	private Long id;
	private Long orderId;
	private Long orderLineId;
	private long quantity;

	private AdminOrderReturnDto(Builder builder) {
		this.id = builder.id;
		this.orderId = builder.orderId;
		this.orderLineId = builder.orderLineId;
		this.quantity = builder.quantity;
	}

	public static Builder anAdminOrderReturnDto() {
		return new Builder();
	}

	public Long getId() {
		return id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getOrderLineId() {
		return orderLineId;
	}

	public long getQuantity() {
		return quantity;
	}

	public static class Builder {

		private Long id;
		private Long orderId;
		private Long orderLineId;
		private long quantity;

		private Builder() {
			super();
		}

		public Builder id(Long val) {

			id = val;
			return this;
		}

		public Builder orderLineId(Long id) {
			this.orderLineId = id;
			return this;
		}

		public Builder orderId(Long id) {
			this.orderId = id;
			return this;
		}

		public Builder quantity(long quantity) {
			this.quantity = quantity;
			return this;
		}

		public AdminOrderReturnDto build() {
			return new AdminOrderReturnDto(this);
		}
	}

}
