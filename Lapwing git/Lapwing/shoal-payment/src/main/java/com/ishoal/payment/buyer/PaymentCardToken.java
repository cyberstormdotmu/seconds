package com.ishoal.payment.buyer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PaymentCardToken {

    public static final PaymentCardToken EMPTY_PAYMENT_TOKEN = new PaymentCardToken(null);

    private final String token;

    private PaymentCardToken(String token) {
        this.token = token;
    }
    
    public static PaymentCardToken from(String token) {
        if(token == null) {
            return EMPTY_PAYMENT_TOKEN;
        }
        return new PaymentCardToken(token);
    }

    public String asString() {
        return this.token;
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.token).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PaymentCardToken other = (PaymentCardToken) obj;
        return new EqualsBuilder().append(this.token, other.token).isEquals();
    }
}