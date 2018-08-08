package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.core.domain.OfferPayment;
import com.ishoal.ws.admin.dto.AdminOfferPaymentDto;
import com.ishoal.ws.common.dto.adapter.TaxableAmountDtoAdapter;

public class AdminOfferPaymentDtoAdapter {

    private TaxableAmountDtoAdapter taxableAmountAdapter = new TaxableAmountDtoAdapter();

    public AdminOfferPaymentDto adapt(OfferPayment payment) {
        return AdminOfferPaymentDto.aPayment()
                .paymentPerUnit(taxableAmountAdapter.adapt(payment.getPaymentPerUnit()))
                .pendingAmount(taxableAmountAdapter.adapt(payment.getPendingAmount()))
                .paidAmount(taxableAmountAdapter.adapt(payment.getPaidAmount()))
                .unpaidAmount(taxableAmountAdapter.adapt(payment.getUnpaidAmount()))
                .build();
    }
}
