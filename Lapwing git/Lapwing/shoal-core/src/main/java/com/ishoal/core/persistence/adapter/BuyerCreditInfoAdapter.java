package com.ishoal.core.persistence.adapter;

import static com.ishoal.core.domain.BuyerCreditInfo.aBuyerCreditInfo;
import com.ishoal.core.domain.BuyerCreditInfo;
import com.ishoal.core.persistence.entity.BuyerCreditInfoEntity;

public class BuyerCreditInfoAdapter {
	private AddressEntityAdapter addressAdapter = new AddressEntityAdapter();
	private BuyerProfileEntityAdapter buyerAdapter = new BuyerProfileEntityAdapter();
	
	public BuyerCreditInfo adapt(BuyerCreditInfoEntity entity) {
        if(entity == null) {
            return null;
        }
        return aBuyerCreditInfo()
        	.id(entity.getId())
            .buyer(buyerAdapter.adapt(entity.getBuyer()))
            .companyName(entity.getCompanyName())
            .contactName(entity.getContactName())
            .invoiceAddress(addressAdapter.adapt(entity.getInvoiceAddress()))
            .deliveryAddress(addressAdapter.adapt(entity.getDeliveryAddress()))
            .registeredAddress(addressAdapter.adapt(entity.getRegisteredAddress()))
            .emailAddress(entity.getEmailAddress())
            .landlineNumber(entity.getLandlineNumber())
            .purchasingManager(entity.getPurchasingManager())
            .telephoneNumber(entity.getTelephoneNumber())
            .tradingAs(entity.getTradingAs())
            .website(entity.getWebsite())
            .isCreditAccount(entity.isCreditAccount())
            .isCashAccount(entity.isCashAccount())
            .printName(entity.getPrintName())
            .date(entity.getRegistrationDate())
            .fullCompanyName(entity.getFullCompanyName())
            .tradingName(entity.getTradingName())
            .vatRegistration(entity.getVatRegistration())
            .expectedCredit(entity.getExpectedCredit())
            .build();
    }

	public BuyerCreditInfoEntity adapt(BuyerCreditInfo credit) {

		BuyerCreditInfoEntity entity = new BuyerCreditInfoEntity();
		entity.setId(credit.getId());
		entity.setBuyer(buyerAdapter.adapt(credit.getBuyer()));
		entity.setCashAccount(credit.isCashAccount());
		entity.setCashAccount(credit.isCreditAccount());
		entity.setCompanyName(credit.getCompanyName());
		entity.setEmailAddress(credit.getEmailAddress());
		entity.setLandlineNumber(credit.getLandlineNumber());
		entity.setTelephoneNumber(credit.getTelephoneNumber());
		entity.setWebsite(credit.getWebsite());
		entity.setTradingName(credit.getTradingName());
		entity.setInvoiceAddress(addressAdapter.adapt(credit.getInvoiceAddress(),entity.getBuyer()));
		entity.setDeliveryAddress(addressAdapter.adapt(credit.getDeliveryAddress(),entity.getBuyer()));
		entity.setRegisteredAddress(addressAdapter.adapt(credit.getRegisteredAddress(),entity.getBuyer()));
		entity.setPrintName(credit.getPrintName());
		entity.setTradingAs(credit.getTradingAs());
		entity.setRegistrationDate(credit.getRegistrationDate());
		entity.setFullCompanyName(credit.getFullCompanyName());
		entity.setContactName(credit.getContactName());
		entity.setVatRegistration(credit.getVatRegistration());
		entity.setPurchasingManager(credit.getPurchasingManager());
		entity.setExpectedCredit(credit.getExpectedCredit());
		return entity;
	}  
}
