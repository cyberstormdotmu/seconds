package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;

public class PlaceOrderRequestOrderLineDto {
    private String productCode;
    private long quantity;
    private BigDecimal unitPrice;
    private Long stock;
    private PlaceOrderRequestOrderLineDto() {
        super();
    }
    
    private PlaceOrderRequestOrderLineDto(Builder builder) {
        this();
        this.productCode = builder.productCode;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
        this.stock=builder.stock;
    }
    
    public static Builder anOrderLine() {
        return new Builder();
    }
    
    public String getProductCode() {
        return productCode;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Long getStock() {
		return stock;
	}


	public static class Builder {
        
        private String productCode;
        private long quantity;
        private BigDecimal unitPrice;
        private Long stock;
        private Builder() {
            super();
        }
        
        public Builder productCode(String productCode) {
            this.productCode = productCode;
            return this;
        }
        
        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }
        
        public Builder stock(Long stock) {
            this.stock = stock;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public PlaceOrderRequestOrderLineDto build() {
            return new PlaceOrderRequestOrderLineDto(this);
        }
    }
}