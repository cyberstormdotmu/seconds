package com.ishoal.ws.common.dto.adapter;

import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.BuyerAppliedCredits;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.BuyerCredits;
import com.ishoal.core.domain.CreditMovementType;
import com.ishoal.ws.common.dto.OrderLineShoalCreditDto;

import java.util.ArrayList;
import java.util.List;

import static com.ishoal.ws.common.dto.OrderLineShoalCreditDto.anOrderLineCredit;
import static java.util.Comparator.comparing;

public class OrderLineShoalCreditDtoAdapter {
    private final TaxableAmountDtoAdapter taxableAmountAdapter = new TaxableAmountDtoAdapter();

    public List<OrderLineShoalCreditDto> adapt(BuyerCredits credits) {
        List<OrderLineShoalCreditDto> creditDtos = new ArrayList<>();

        credits.stream().forEach(credit -> {
                creditDtos.add(adapt(credit));
                creditDtos.addAll(adapt(credit.getAppliedCredits()));
            }
        );

        creditDtos.sort(comparing((OrderLineShoalCreditDto it) -> it.getEffectiveDate() ));

        return creditDtos;
    }

    private OrderLineShoalCreditDto adapt(BuyerCredit credit) {
        return anOrderLineCredit()
                .effectiveDate(credit.getCreated())
                .creditMovementType(CreditMovementType.EARN.getDisplayName())
                .amount(taxableAmountAdapter.adapt(credit.getAmount()))
                .reason(credit.getReason())
                .build();
    }

    private List<OrderLineShoalCreditDto> adapt(BuyerAppliedCredits spentCredits) {
        List<OrderLineShoalCreditDto> creditDtos = new ArrayList<>();

        spentCredits.stream()
                .filter(it -> it.getStatus() != BuyerAppliedCreditStatus.CANCELLED)
                .forEach(spentCredit -> creditDtos.add(adapt(spentCredit)));

        return creditDtos;
    }

    private OrderLineShoalCreditDto adapt(BuyerAppliedCredit spentCredit) {
        return anOrderLineCredit()
                .effectiveDate(spentCredit.getCreated())
                .creditMovementType(spentCredit.getSpendType().getDisplayName())
                .amount(taxableAmountAdapter.adapt(spentCredit.getAmount().negate()))
                .orderSpentOnReference(spentCredit.getOrderSpentOnReference().asString())
                .build();
    }
}
