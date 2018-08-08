package com.ishoal.core.persistence.adapter;

import java.util.List;

import com.ishoal.core.domain.BuyerVendorCredits;
import com.ishoal.core.persistence.entity.BuyerVendorCreditEntity;

public class BuyerVendorCreditsEntityAdapter {

	private BuyerVendorCreditEntityAdapter buyerVendorCreditAdapter = new BuyerVendorCreditEntityAdapter();

	public BuyerVendorCredits adapt(List<BuyerVendorCreditEntity> entities) {
		if(entities == null)
		{
			return BuyerVendorCredits.emptybuyerVendorCredits();
		}
		BuyerVendorCredits.Builder builder = BuyerVendorCredits.somebuyerVendorCredits();
		entities.forEach(entity -> builder.buyerVendorCredit(buyerVendorCreditAdapter.adapt(entity)));
		
		return builder.build();
	}
}