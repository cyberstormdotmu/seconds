package com.ishoal.core.orders;

import com.ishoal.core.domain.ProductCode;

import java.math.BigDecimal;

public class NewOrderRequestOrderLine {

    private final ProductCode productCode;
    private final long quantity;
    private final BigDecimal unitPrice;
    private final Long stock;

    private NewOrderRequestOrderLine(Builder builder) {
        this.productCode = builder.productCode;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
       this.stock=builder.stock;
    }
    
    public static Builder anUnconfirmedOrderLine() {
        return new Builder();
    }

    public ProductCode getProductCode() {
        return productCode;
    }
    
    public long getQuantity() {
        return quantity;
    }

	public Long getStock() {
		return stock;
	}

	public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public static class Builder {

        private ProductCode productCode;
        private long quantity;
        private BigDecimal unitPrice;
        private Long stock;
    

        public Builder productCode(ProductCode productCode) {
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

        public NewOrderRequestOrderLine build() {
            return new NewOrderRequestOrderLine(this);
        }
    }
}