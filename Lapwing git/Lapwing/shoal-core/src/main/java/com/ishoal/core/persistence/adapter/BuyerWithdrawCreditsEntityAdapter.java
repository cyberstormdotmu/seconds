package com.ishoal.core.persistence.adapter;

import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerWithdrawCredits;
import com.ishoal.core.persistence.entity.BuyerWithdrawCreditEntity;

public class BuyerWithdrawCreditsEntityAdapter {

	private BuyerWithdrawCreditAdapter creditAdapter = new BuyerWithdrawCreditAdapter();
	
	public BuyerWithdrawCredits adapt(List<BuyerWithdrawCreditEntity> entities){
		if(entities == null){
	    	   return BuyerWithdrawCredits.emptyBuyerWithdrawCredits();
		}
		BuyerWithdrawCredits.Builder builder = BuyerWithdrawCredits.someBuyerWithdrawCredits();
		entities.forEach(entity -> builder.buyerWithdrawCredits(creditAdapter.adapt(entity)));
		return builder.build();
	}
	
	public List<BuyerWithdrawCreditEntity> adapt(BuyerWithdrawCredits credits) {
		return IterableUtils.mapToList(credits, buyerWithdrawCredit -> creditAdapter.adapt(buyerWithdrawCredit));
	}
	
}
