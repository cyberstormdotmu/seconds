package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.BuyerWithdrawCredit;
import com.ishoal.core.persistence.entity.BuyerWithdrawCreditEntity;

public class BuyerWithdrawCreditAdapter {

	public BuyerWithdrawCreditAdapter(){}
	
	private final BuyerProfileEntityAdapter buyerProfileAdapter = new BuyerProfileEntityAdapter();
	
	public BuyerWithdrawCreditEntity adapt(BuyerWithdrawCredit credit){
		
		BuyerWithdrawCreditEntity entity = new BuyerWithdrawCreditEntity();
		entity.setId(credit.getId());
		entity.setBuyer(buyerProfileAdapter.adapt(credit.getBuyer()));
		entity.setRequestedAmount(credit.getRequestedAmount());
		entity.setRequestedDate(credit.getRequestedDate());
		entity.setPaymentType(credit.getPaymentType());
		entity.setReceivedDate(credit.getReceivedDate());
		entity.setWithdrawReference(credit.getWithdrawReference());
		entity.setWithdrawStatus(credit.getWithdrawStatus());
		entity.setConfirmedDate(credit.getConfirmedDate());
		return entity;
	}
	
	public BuyerWithdrawCredit adapt(BuyerWithdrawCreditEntity entity){
		
		if (entity == null) {
			return null;
		}
		
		return BuyerWithdrawCredit.aBuyerWithdrawCredit().id(entity.getId())
				.buyer(buyerProfileAdapter.adapt(entity.getBuyer()))
				.requestedAmount(entity.getRequestedAmount())
				.receivedDate(entity.getReceivedDate())
				.requestedDate(entity.getRequestedDate())
				.paymentType(entity.getPaymentType())
				.withdrawReference(entity.getWithdrawReference())
				.withdrawStatus(entity.getWithdrawStatus())
				.confirmedDate(entity.getConfirmedDate())
				.build();
	}
	
	public BuyerWithdrawCreditEntity adaptss(BuyerWithdrawCredit domain) {
		if (domain == null) {
			return null;
		}
		BuyerWithdrawCreditEntity entity=new BuyerWithdrawCreditEntity();
		
		entity.setId(domain.getId());
		entity.setRequestedAmount(domain.getRequestedAmount());
		entity.setRequestedDate(domain.getRequestedDate());
		entity.setPaymentType(domain.getPaymentType());
		entity.setReceivedDate(domain.getReceivedDate());
		entity.setWithdrawReference(domain.getWithdrawReference());
		entity.setWithdrawStatus(domain.getWithdrawStatus());
		entity.setConfirmedDate(domain.getConfirmedDate());
		return entity;	
	}
}
