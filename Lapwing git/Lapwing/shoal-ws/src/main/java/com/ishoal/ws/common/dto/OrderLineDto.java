package com.ishoal.ws.common.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderLineDto {
	private long id;
	private String productCode;
	private String productName;
	private long quantity;
	private BigDecimal initialUnitPrice;
	private BigDecimal currentUnitPrice;
	private TaxableAmountDto amount;
	private BigDecimal vatRate;
	private TaxableAmountDto creditTotal;
	private List<OrderLineShoalCreditDto> credits;
	private Long stock;
	private long returnedQuantites;

	private OrderLineDto() {
		super();
	}

	private OrderLineDto(Builder builder) {
		this();
		id = builder.id;
		productCode = builder.productCode;
		productName = builder.productName;
		quantity = builder.quantity;
		initialUnitPrice = builder.initialUnitPrice;
		currentUnitPrice = builder.currentUnitPrice;
		amount = builder.amount;
		vatRate = builder.vatRate;
		creditTotal = builder.creditTotal;
		credits = new ArrayList<>(builder.credits);
		stock = builder.stock;
		returnedQuantites = builder.returnedQuantites;
	}

	public static Builder anOrderLine() {
		return new Builder();
	}

	public String getProductCode() {
		return productCode;
	}

	public Long getStock() {
		return stock;
	}

	public String getProductName() {
		return productName;
	}

	public long getQuantity() {
		return quantity;
	}

	public BigDecimal getInitialUnitPrice() {
		return initialUnitPrice;
	}

	public long getReturnedQuantites() {
		return returnedQuantites;
	}

	public BigDecimal getCurrentUnitPrice() {
		return currentUnitPrice;
	}

	public TaxableAmountDto getAmount() {
		return amount;
	}

	public BigDecimal getVatRate() {
		return vatRate;
	}

	public TaxableAmountDto getCreditTotal() {
		return creditTotal;
	}

	public List<OrderLineShoalCreditDto> getCredits() {
		return Collections.unmodifiableList(credits);
	}

	public long getId() {
		return id;
	}

	public static final class Builder {
		private long id;
		private String productCode;
		private String productName;
		private long quantity;
		private BigDecimal initialUnitPrice;
		private BigDecimal currentUnitPrice;
		private TaxableAmountDto amount;
		private BigDecimal vatRate;
		private TaxableAmountDto creditTotal;
		private List<OrderLineShoalCreditDto> credits = new ArrayList<>();
		private Long stock;
		private long returnedQuantites;

		private Builder() {
		}

		public Builder id(long val) {
			this.id = val;
			return this;
		}

		public Builder productCode(String val) {
			productCode = val;
			return this;
		}

		public Builder productName(String val) {
			productName = val;
			return this;
		}

		public Builder quantity(long val) {
			quantity = val;
			return this;
		}

		public Builder stock(Long val) {
			stock = val;
			return this;
		}

		public Builder initialUnitPrice(BigDecimal val) {
			initialUnitPrice = val;
			return this;
		}

		public Builder currentUnitPrice(BigDecimal val) {
			currentUnitPrice = val;
			return this;
		}

		public Builder amount(TaxableAmountDto val) {
			amount = val;
			return this;
		}

		public Builder vatRate(BigDecimal val) {
			vatRate = val;
			return this;
		}

		public Builder returnedQuantites(long val) {
			returnedQuantites = val;
			return this;
		}

		public Builder creditTotal(TaxableAmountDto val) {
			creditTotal = val;
			return this;
		}

		public Builder credits(List<OrderLineShoalCreditDto> val) {
			credits = new ArrayList<>(val);
			return this;
		}

		public OrderLineDto build() {
			return new OrderLineDto(this);
		}
	}
}
