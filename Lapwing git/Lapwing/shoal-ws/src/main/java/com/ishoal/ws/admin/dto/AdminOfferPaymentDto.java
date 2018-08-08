package com.ishoal.ws.admin.dto;


import com.ishoal.ws.common.dto.TaxableAmountDto;

import java.math.BigDecimal;

public class AdminOfferPaymentDto {
    private TaxableAmountDto paymentPerUnit;
    private TaxableAmountDto pendingAmount;
    private TaxableAmountDto paidAmount;
    private TaxableAmountDto unpaidAmount;

    private AdminOfferPaymentDto() {
        super();
    }

    private AdminOfferPaymentDto(Builder builder) {
        this();
        paymentPerUnit = builder.paymentPerUnit;
        pendingAmount = builder.pendingAmount;
        paidAmount = builder.paidAmount;
        unpaidAmount = builder.unpaidAmount;
    }

    public static Builder aPayment() {
        return new Builder();
    }

    public TaxableAmountDto getPaymentPerUnit() {
        return paymentPerUnit;
    }

    public TaxableAmountDto getPendingAmount() {
        return pendingAmount;
    }

    public TaxableAmountDto getPaidAmount() {
        return paidAmount;
    }

    public TaxableAmountDto getUnpaidAmount() {
        return unpaidAmount;
    }

    public static final class Builder {
        private static TaxableAmountDto ZERO = TaxableAmountDto.aTaxableAmount().net(BigDecimal.ZERO).vat(BigDecimal.ZERO).gross(BigDecimal.ZERO).build();
        private TaxableAmountDto paymentPerUnit = ZERO;
        private TaxableAmountDto pendingAmount = ZERO;
        private TaxableAmountDto paidAmount = ZERO;
        private TaxableAmountDto unpaidAmount = ZERO;

        private Builder() {
        }

        public Builder paymentPerUnit(TaxableAmountDto val) {
            paymentPerUnit = val;
            return this;
        }

        public Builder pendingAmount(TaxableAmountDto val) {
            pendingAmount = val;
            return this;
        }

        public Builder paidAmount(TaxableAmountDto val) {
            paidAmount = val;
            return this;
        }

        public Builder unpaidAmount(TaxableAmountDto val) {
            unpaidAmount = val;
            return this;
        }

        public AdminOfferPaymentDto build() {
            return new AdminOfferPaymentDto(this);
        }
    }
}
