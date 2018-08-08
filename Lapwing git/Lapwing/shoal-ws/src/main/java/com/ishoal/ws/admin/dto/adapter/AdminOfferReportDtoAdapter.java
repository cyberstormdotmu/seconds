package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.core.domain.OfferReport;
import com.ishoal.ws.admin.dto.AdminOfferReportDto;

public class AdminOfferReportDtoAdapter {

    private AdminPriceBandDtoAdapter priceBandAdapter = new AdminPriceBandDtoAdapter();
    private AdminOfferPaymentDtoAdapter offerPaymentAdapter = new AdminOfferPaymentDtoAdapter();
    private AdminRoutePaymentDtoAdapter adminRoutePaymentDtoAdapter = new AdminRoutePaymentDtoAdapter();
    private AdminPaymentDetailsOfOrderDtoAdapter adminRouteOneAndTwoPaymentDtoAdapter = new AdminPaymentDetailsOfOrderDtoAdapter(); 
    private SupplierPaymentForAnOrderDtoAdapter supplierPaymentForAnOrderDtoAdapter = new SupplierPaymentForAnOrderDtoAdapter(); 
    private OutstandingBalanceOfADayDtoAdapter outstandingBalanceOfADayDtoAdapter = new OutstandingBalanceOfADayDtoAdapter();
    public AdminOfferReportDto adapt(OfferReport report) {
        return AdminOfferReportDto.anOfferReport()
                .productCode(report.getProduct().getCode().toString())
                .productName(report.getProduct().getName())
                .vendorName(report.getProduct().getVendor().getName())
                .offerStartDate(report.getOffer().getStartDateTime())
                .offerEndDate(report.getOffer().getEndDateTime())
                .offerOpen(report.getOffer().getEndDateTime().isAfterNow())
                .confirmedVolume(report.getConfirmedVolume())
                .paidVolume(report.getPaidVolume())
                .unpaidVolume(report.getUnpaidVolume())
                .partiallyPaidVolume(report.getPartiallyPaidVolume())
                .currentPriceBand(priceBandAdapter.adapt(report.getCurrentPriceBand()))
                .buyerCreditsEarned(report.getBuyerCreditsEarned())
                .buyerCreditsSpent(report.getBuyerCreditsSpent())
                .fullPaymentsReceived(report.getBuyerPaymentsReceived())
                .unpaidAmount(report.getBuyerPaymentsOutstanding())
                .allRouteDetails(adminRouteOneAndTwoPaymentDtoAdapter.adapt(report.getAllRouteDetails()))
                .outstandingBalanceToSupplier(report.getOutstandingBalanceToSupplier())
                .outstandingBalanceToLapwing(report.getOutstandingBalanceToLapwing())
                .outstandingBalanceOfADay(outstandingBalanceOfADayDtoAdapter.adapt(report.getOutstandingBalanceOfADay()))
                .supplierPayments(supplierPaymentForAnOrderDtoAdapter.adapt(report.getSupplierPayments()))
                .build();
    }
}
