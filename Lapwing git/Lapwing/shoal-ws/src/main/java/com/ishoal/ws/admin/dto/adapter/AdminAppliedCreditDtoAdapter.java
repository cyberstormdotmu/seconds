package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerAppliedCredits;
import com.ishoal.ws.admin.dto.AdminAppliedCreditDto;
import com.ishoal.ws.common.dto.adapter.TaxableAmountDtoAdapter;

import java.util.List;

import static com.ishoal.ws.admin.dto.AdminAppliedCreditDto.anAppliedCredit;

public class AdminAppliedCreditDtoAdapter {
    private final TaxableAmountDtoAdapter taxableAmountAdapter = new TaxableAmountDtoAdapter();

    public List<AdminAppliedCreditDto> adapt(BuyerAppliedCredits appliedCredits) {
        return IterableUtils.mapToList(appliedCredits, this::adapt);
    }

    public AdminAppliedCreditDto adapt(BuyerAppliedCredit appliedCredit) {
        return anAppliedCredit()
                .status(appliedCredit.getStatus().getDisplayName())
                .spendType(appliedCredit.getSpendType().getDisplayName())
                .amount(taxableAmountAdapter.adapt(appliedCredit.getAmount()))
                .originalOrderReference(appliedCredit.getOriginalOrderReference().asString())
                .originalOrderLineProductCode(appliedCredit.getOriginalOrderLineProductCode().toString())
                .orderSpentOnReference(appliedCredit.getOrderSpentOnReference().asString())
                .created(appliedCredit.getCreated())
                .modified(appliedCredit.getModified())
                .build();
    }
}
