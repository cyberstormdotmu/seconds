package com.ishoal.core.persistence.adapter;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerAppliedCreditId;
import com.ishoal.core.domain.BuyerAppliedCredits;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.persistence.entity.BuyerAppliedCreditEntity;
import com.ishoal.core.persistence.entity.BuyerCreditEntity;
import com.ishoal.core.persistence.entity.BuyerVendorCreditEntity;

import java.util.List;

import static com.ishoal.core.domain.BuyerAppliedCredit.aBuyerAppliedCredit;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;

public class BuyerAppliedCreditEntityAdapter {

    public BuyerAppliedCredits adapt(List<BuyerAppliedCreditEntity> entities) {

        return IterableUtils.mapToCollection(entities, it -> adapt(it), BuyerAppliedCredits::over);
    }

    public BuyerAppliedCredit adapt(BuyerAppliedCreditEntity entity) {
        return aBuyerAppliedCredit()
                .id(BuyerAppliedCreditId.from(entity.getId()))
                .status(entity.getStatus())
                .spendType(entity.getSpendType())
                .amount(fromNetAndVat(entity.getNetAmount(), entity.getVatAmount()))
                .status(entity.getStatus())
                .originalOrderReference(OrderReference.from(entity.getOriginalOrderReference()))
                .originalOrderLineProductCode(ProductCode.from(entity.getOriginalOrderLineProductCode()))
                .orderSpentOnReference(OrderReference.from(entity.getOrderSpentOnReference()))
                .created(entity.getCreated())
                .modified(entity.getModified())
                .build();
    }

    public List<BuyerAppliedCreditEntity> adapt(BuyerAppliedCredits credits, BuyerCreditEntity buyerCreditEntity) {
        return IterableUtils.mapToList(credits, credit -> adapt(credit, buyerCreditEntity));
    }

    public BuyerAppliedCreditEntity adapt(BuyerAppliedCredit appliedCredit, BuyerCreditEntity buyerCreditEntity) {

        BuyerAppliedCreditEntity appliedCreditEntity = new BuyerAppliedCreditEntity();
        appliedCreditEntity.setId(appliedCredit.getId().asLong());
        appliedCreditEntity.setBuyerCredit(buyerCreditEntity);
        appliedCreditEntity.setStatus(appliedCredit.getStatus());
        appliedCreditEntity.setSpendType(appliedCredit.getSpendType());
        appliedCreditEntity.setNetAmount(appliedCredit.getAmount().net());
        appliedCreditEntity.setVatAmount(appliedCredit.getAmount().vat());
        appliedCreditEntity.setGrossAmount(appliedCredit.getAmount().gross());
        appliedCreditEntity.setOriginalOrderReference(appliedCredit.getOriginalOrderReference().asString());
        appliedCreditEntity.setOriginalOrderLineProductCode(appliedCredit.getOriginalOrderLineProductCode().toString());
        appliedCreditEntity.setOrderSpentOnReference(appliedCredit.getOrderSpentOnReference().asString());
        appliedCreditEntity.setCreated(appliedCredit.getCreated());
        appliedCreditEntity.setModified(appliedCredit.getModified());

        return appliedCreditEntity;
    }
    
    
    public BuyerAppliedCreditEntity adapt(BuyerAppliedCredit appliedCredit, BuyerVendorCreditEntity buyerVendorCreditEntity) {

        BuyerAppliedCreditEntity appliedCreditEntity = new BuyerAppliedCreditEntity();
        appliedCreditEntity.setStatus(appliedCredit.getStatus());
        appliedCreditEntity.setSpendType(appliedCredit.getSpendType());
        appliedCreditEntity.setNetAmount(appliedCredit.getAmount().net());
        appliedCreditEntity.setVatAmount(appliedCredit.getAmount().vat());
        appliedCreditEntity.setGrossAmount(appliedCredit.getAmount().gross());
        appliedCreditEntity.setOriginalOrderLineProductCode(appliedCredit.getOriginalOrderLineProductCode().toString());
        appliedCreditEntity.setOrderSpentOnReference(appliedCredit.getOrderSpentOnReference().asString());
        appliedCreditEntity.setCreated(appliedCredit.getCreated());
        appliedCreditEntity.setModified(appliedCredit.getModified());
        appliedCreditEntity.setVendorCredit(buyerVendorCreditEntity);
        return appliedCreditEntity;
    }
    
}