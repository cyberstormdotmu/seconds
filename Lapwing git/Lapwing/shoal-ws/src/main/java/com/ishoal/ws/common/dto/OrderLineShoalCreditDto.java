package com.ishoal.ws.common.dto;

import org.joda.time.DateTime;

public class OrderLineShoalCreditDto {
    private DateTime effectiveDate;
    private String creditMovementType;
    private TaxableAmountDto amount;
    private String reason;
    private String orderSpentOnReference;

    private OrderLineShoalCreditDto() {
        super();
    }

    private OrderLineShoalCreditDto(Builder builder) {
        this();
        effectiveDate = builder.effectiveDate;
        creditMovementType = builder.creditMovementType;
        amount = builder.amount;
        reason = builder.reason;
        orderSpentOnReference = builder.orderSpentOnReference;
    }

    public static Builder anOrderLineCredit() {
        return new Builder();
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    public String getCreditMovementType() {
        return creditMovementType;
    }

    public TaxableAmountDto getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public String getOrderSpentOnReference() {
        return orderSpentOnReference;
    }

    public static final class Builder {
        private DateTime effectiveDate;
        private String creditMovementType;
        private TaxableAmountDto amount;
        private String reason;
        private String orderSpentOnReference;

        private Builder() {
        }

        public Builder effectiveDate(DateTime val) {
            effectiveDate = val;
            return this;
        }

        public Builder creditMovementType(String val) {
            creditMovementType = val;
            return this;
        }

        public Builder amount(TaxableAmountDto val) {
            amount = val;
            return this;
        }

        public Builder reason(String val) {
            reason = val;
            return this;
        }

        public Builder orderSpentOnReference(String val) {
            orderSpentOnReference = val;
            return this;
        }

        public OrderLineShoalCreditDto build() {
            return new OrderLineShoalCreditDto(this);
        }
    }
}
