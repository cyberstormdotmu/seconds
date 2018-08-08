package com.ishoal.core.domain;

import java.math.BigDecimal;

public class PriceMovement {

    private final BigDecimal customerCredit;
    private final String reason;
    
    private PriceMovement(Builder builder) {
        this.customerCredit = builder.customerCredit;
        this.reason = builder.reason;
    }
    
    public static Builder aPriceMovement() {
        return new Builder();
    }
    
    public BigDecimal getCustomerCredit() {
        return customerCredit;
    }

    public String getReason() {
        return reason;
    }

    public static class Builder {
        
        private BigDecimal customerCredit;
        private String reason;
        
        private Builder() {
            super();
        }
        
        public Builder customerCredit(BigDecimal customerCredit) {
            this.customerCredit = customerCredit;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public PriceMovement build() {
            return new PriceMovement(this);
        }
    }
}