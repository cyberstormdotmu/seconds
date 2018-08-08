package com.ishoal.ws.buyer.dto;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

// This class must be Serializable as it is stored in the session state.
// Must also use concrete ArrayList since List is not serializable.
public class ShoppingBasketDto implements Serializable {

    private static final long serialVersionUID = -6906099445368223101L;
    //@JsonProperty("items")
    private ArrayList<ShoppingBasketItemDto> items;

    private ShoppingBasketDto() {
        items = new ArrayList<>();
    }

    private ShoppingBasketDto(Builder builder) {
        this();
        items.addAll(builder.items);
    }

    public static Builder aShoppingBasket() {
        return new Builder();
    }

    public static ShoppingBasketDto anEmptyShoppingBasket() {
        return aShoppingBasket().build();
    }

    public ArrayList<ShoppingBasketItemDto> getItems() {
        return items;
    }

    public ShoppingBasketItemDto findItem(String productCode) {
        for (ShoppingBasketItemDto item : items) {
            if (item.getProductCode().equals(productCode)) {
                return item;
            }
        }
        return null;
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
        private ArrayList<ShoppingBasketItemDto> items = new ArrayList<>();

        private Builder() {
        }

        public Builder item(ShoppingBasketItemDto val) {
            items.add(val);
            return this;
        }

        public ShoppingBasketDto build() {
            return new ShoppingBasketDto(this);
        }
    }
}
