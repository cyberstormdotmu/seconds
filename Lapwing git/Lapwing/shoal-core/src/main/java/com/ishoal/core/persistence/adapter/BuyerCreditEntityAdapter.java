package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.BuyerCreditId;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.persistence.entity.BuyerCreditEntity;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import org.joda.time.DateTime;

import static com.ishoal.core.domain.BuyerCredit.aBuyerCredit;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;

public class BuyerCreditEntityAdapter {
    private final BuyerAppliedCreditEntityAdapter appliedCreditEntityAdapter = new BuyerAppliedCreditEntityAdapter();

    public BuyerCreditEntity adapt(BuyerCredit credit, OrderLineEntity orderLineEntity) {

        BuyerCreditEntity creditEntity = new BuyerCreditEntity();
        creditEntity.setId(credit.getId().asLong());
        creditEntity.setOrderLine(orderLineEntity);
        creditEntity.setCreditType(credit.getCreditType());
        creditEntity.setNetAmount(credit.getAmount().net());
        creditEntity.setVatAmount(credit.getAmount().vat());
        creditEntity.setReason(credit.getReason());
        creditEntity.setCreated(credit.getCreated());
        creditEntity.setAppliedBuyerCredits(appliedCreditEntityAdapter.adapt(credit.getAppliedCredits(), creditEntity));
        return creditEntity;
    }

    public BuyerCredit adapt(BuyerCreditEntity creditEntity) {

        PaymentStatus paymentStatus = creditEntity.getOrderLine().getOrder().getPaymentStatus();
        DateTime offerEndDateTime = creditEntity.getOrderLine().getOffer().getEndDateTime();

        boolean isSpendable = paymentStatus == PaymentStatus.PAID;
        boolean isRedeemable = isSpendable && offerEndDateTime.isBeforeNow();

        return aBuyerCredit()
                .id(BuyerCreditId.from(creditEntity.getId()))
                .creditType(creditEntity.getCreditType())
                .amount(fromNetAndVat(creditEntity.getNetAmount(), creditEntity.getVatAmount()))
                .reason(creditEntity.getReason())
                .created(creditEntity.getCreated())
                .spendable(isSpendable)
                .redeemable(isRedeemable)
                .appliedCredits(appliedCreditEntityAdapter.adapt(creditEntity.getAppliedBuyerCredits()))
                .build();
    }
}