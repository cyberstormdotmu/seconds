package com.ishoal.ws.buyer.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

// This class must be Serializable as it is stored in the session state.
/**
 * @author pca48
 *
 */
public class ShoppingBasketItemDto implements Serializable {

    private static final long serialVersionUID = 7013127149334889893L;

    private String productCode;
    private long currentVolume;
    private long quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceVat;
    private Long stock;
    private String vendorName;

    private ShoppingBasketItemDto() {
        super();
    }

    private ShoppingBasketItemDto(Builder builder) {
        this();
        productCode = builder.productCode;
        quantity = builder.quantity;
        unitPrice = builder.unitPrice;
        unitPriceVat = builder.unitPriceVat;
        currentVolume = builder.currentVolume;
        stock = builder.stock;
        vendorName = builder.vendorName;
    }

    public static Builder aShoppingBasketItem() {
        return new Builder();
    }

    public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public long getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(long currentVolume) {
        this.currentVolume = currentVolume;
    }

    public long getQuantity() {
        return quantity;
    }

	public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceVat() {
        return unitPriceVat;
    }

    public Long getStock() {
		return stock;
	}
	public void setUnitPriceVat(BigDecimal unitPriceVat) {
        this.unitPriceVat = unitPriceVat;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final class Builder {
        private String productCode;
        private long currentVolume;
        private long quantity;
        private BigDecimal unitPrice;
        private BigDecimal unitPriceVat;
        private Long stock;
        private String vendorName;


        private Builder() {
        }

        public Builder productCode(String val) {
            productCode = val;
            return this;
        }

        public Builder vendorName(String val) {
        	vendorName = val;
            return this;
        }

        public Builder currentVolume(long val) {
            currentVolume = val;
            return this;
        }

        public Builder quantity(long val) {
            quantity = val;
            return this;
        }

        public Builder unitPrice(BigDecimal val) {
            unitPrice = val;
            return this;
        }

        public Builder unitPriceVat(BigDecimal val) {
            unitPriceVat = val;
            return this;
        }
        
        public Builder stock(Long val) {
        	stock = val;
            return this;
        }

        public ShoppingBasketItemDto build() {
            return new ShoppingBasketItemDto(this);
        }
    }
}
