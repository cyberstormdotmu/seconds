package com.ishoal.core.persistence.adapter;

import static com.ishoal.core.domain.BuyerVendorCredit.aBuyerVendorCredit;

import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.entity.BuyerVendorCreditEntity;

public class BuyerVendorCreditEntityAdapter {
	 private final VendorEntityAdapter vendorAdapter = new VendorEntityAdapter();
	 private final BuyerProfileEntityAdapter buyerProfileEntityAdapter = new BuyerProfileEntityAdapter(); 
	public BuyerVendorCredit adapt(BuyerVendorCreditEntity entity) {
        if(entity == null) {
            return null;
        }
        return aBuyerVendorCredit()
        	.id(entity.getId())
            .buyer(buyerProfileEntityAdapter.adapt(entity.getBuyer()))
            .vendor(adaptVendor(entity))
            .totalCredit(entity.getTotalCredit())
            .availableCredit(entity.getAvailableCredit())
            .paymentDueDays(entity.getPaymentDueDays())
            .build();
    }
	public BuyerVendorCreditEntity adapt(BuyerVendorCredit credit) {

		BuyerVendorCreditEntity entity = new BuyerVendorCreditEntity();
		entity.setId(credit.getId());
		entity.setBuyer(buyerProfileEntityAdapter.adapt(credit.getBuyer()));
		entity.setTotalCredit(credit.getTotalCredit());
		entity.setAvailableCredit(credit.getAvailableCredit());
		entity.setVendor(vendorAdapter.adapt(credit.getVendor()));
		entity.setPaymentDueDays(credit.getPaymentDueDays());
		
		return entity;
	}
	   private Vendor adaptVendor(BuyerVendorCreditEntity entity) {
	        return vendorAdapter.adapt(entity.getVendor());
	    }
	   
	   public List<BuyerVendorCredit> adapt(List<BuyerVendorCreditEntity> entities) {
	        return IterableUtils.mapToList(entities, this::adapt);
	    }
}
