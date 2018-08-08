package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerAppliedCredits;
import com.ishoal.core.domain.CreditMovementType;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.core.domain.OrderPayments;
import com.ishoal.core.domain.TaxableAmount;
import com.ishoal.ws.buyer.dto.BuyerOrderPaymentDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuyerOrderPaymentDtoAdapter {

    public List<BuyerOrderPaymentDto> adapt(Order order) {
        List<BuyerOrderPaymentDto> payments = adapt(order.getAppliedCredits());
        payments.addAll(adapt(order.getPayments()));
        return payments;
    }

    private List<BuyerOrderPaymentDto> adapt(OrderPayments payments) {
        return payments.stream().map(it -> adapt(it)).collect(Collectors.toList());
    }

    private BuyerOrderPaymentDto adapt(OrderPayment payment) {
        return BuyerOrderPaymentDto.anOrderPayment()
                .dateReceived(payment.getDateReceived())
                .paymentType(payment.getPaymentType().getDisplayName())
                .amount(payment.getAmount())
                .paymentGatewayCharges(payment.getPaymentGatewayCharges())
                .paymentRecordType(payment.getPaymentRecordType())
                .build();
    }

    private List<BuyerOrderPaymentDto> adapt(BuyerAppliedCredits appliedCredits) {
        Map<String, List<BuyerAppliedCredit>> mappedCredits = new HashMap<>();

        for(BuyerAppliedCredit appliedCredit : appliedCredits) {
            String mapKey = mapKey(appliedCredit);
            if(!mappedCredits.containsKey(mapKey)) {
                mappedCredits.put(mapKey, new ArrayList<>());
            }
            List<BuyerAppliedCredit> creditList = mappedCredits.get(mapKey);
            creditList.add(appliedCredit);
        }

        return mappedCredits.values().stream().map(it -> adapt(it)).collect(Collectors.toList());
    }

    private String mapKey(BuyerAppliedCredit appliedCredit) {
        return appliedCredit.getOriginalOrderReference().toString()
                + appliedCredit.getOriginalOrderLineProductCode().toString();
    }

    private BuyerOrderPaymentDto adapt(List<BuyerAppliedCredit> credits) {
        BuyerAppliedCredit firstCredit = credits.get(0);

        TaxableAmount amount = TaxableAmount.sum(credits.stream(), BuyerAppliedCredit::getAmount);

        String paymentType;
        if(firstCredit.getSpendType().compareTo(CreditMovementType.V_SPEND)==0)
        {
        	paymentType = "Supplier Credits";
        }
        else
        {
        	paymentType = "Shoal Credit";
        }
        
        return BuyerOrderPaymentDto.anOrderPayment()
                .dateReceived(firstCredit.getCreated())
                .paymentType(paymentType)
                .amount(amount.gross())
                .originalOrderReference(firstCredit.getOriginalOrderReference().asString())
                .originalOrderLineProductCode(firstCredit.getOriginalOrderLineProductCode().toString())
                .build();
    }
}
