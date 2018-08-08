package com.ishoal.ws.buyer.adapter;

import static com.ishoal.core.domain.BuyerCreditInfo.aBuyerCreditInfo;
import com.ishoal.core.domain.BuyerCreditInfo;
import com.ishoal.ws.buyer.dto.adapter.BuyerProfileDtoAdapter;
import com.ishoal.ws.common.dto.BuyerCreditInfoDto;
import com.ishoal.ws.common.dto.adapter.AddressDtoAdapter;

public class BuyerCreditInfoRequestAdapter {
	private AddressDtoAdapter addressAdapter = new AddressDtoAdapter();
	private BuyerProfileDtoAdapter buyerAdapter = new BuyerProfileDtoAdapter();
	public BuyerCreditInfo adapt(BuyerCreditInfoDto creditDetails) {
	
		if (creditDetails == null) {
			return null;
		}
		return aBuyerCreditInfo()
				.id(creditDetails.getId())
				.buyer(buyerAdapter.adapt(creditDetails.getBuyer()))
				.companyName(creditDetails.getCompanyName())
				.contactName(creditDetails.getContactName())
				.invoiceAddress(addressAdapter.adapt(creditDetails.getInvoiceAddress()))
				.deliveryAddress(addressAdapter.adapt(creditDetails.getDeliveryAddress()))
				.registeredAddress(addressAdapter.adapt(creditDetails.getRegisteredAddress()))
				.emailAddress(creditDetails.getEmailAddress())
				.landlineNumber(creditDetails.getLandlineNumber())
				.purchasingManager(creditDetails.getPurchasingManager())
				.telephoneNumber(creditDetails.getTelephoneNumber())
				.tradingAs(creditDetails.getTradingAs())
				.website(creditDetails.getWebsite())
				.isCreditAccount(creditDetails.isIsCreditAccount())
				.isCashAccount(creditDetails.isIsCashAccount())
				.printName(creditDetails.getPrintName())
				.date(creditDetails.getRegistrationDate())
				.fullCompanyName(creditDetails.getFullCompanyName())
				.tradingName(creditDetails.getTradingName())
				.vatRegistration(creditDetails.getVatRegistration())
				.expectedCredit(creditDetails.getExpectedCredit())
				.build();
	}

	public BuyerCreditInfoDto adapt(BuyerCreditInfo creditDetails) {
		if (creditDetails == null) {
			return null;
		}
		return BuyerCreditInfoDto.aBuyerCreditInfoDto()
				.id(creditDetails.getId())
				.buyer(buyerAdapter.adapt(creditDetails.getBuyer()))
				.companyName(creditDetails.getCompanyName())
				.contactName(creditDetails.getContactName())
				.invoiceAddress(addressAdapter.adapt(creditDetails.getInvoiceAddress()))
				.deliveryAddress(addressAdapter.adapt(creditDetails.getDeliveryAddress()))
				.registeredAddress(addressAdapter.adapt(creditDetails.getRegisteredAddress()))
				.emailAddress(creditDetails.getEmailAddress())
				.landlineNumber(creditDetails.getLandlineNumber())
				.purchasingManager(creditDetails.getPurchasingManager())
				.telephoneNumber(creditDetails.getTelephoneNumber())
				.tradingAs(creditDetails.getTradingAs())
				.website(creditDetails.getWebsite())
				.isCreditAmount(creditDetails.isCreditAccount())
				.isCashAmount(creditDetails.isCashAccount())
				.printName(creditDetails.getPrintName())
				.date(creditDetails.getRegistrationDate())
				.fullCompanyName(creditDetails.getFullCompanyName())
				.tradingName(creditDetails.getTradingName())
				.vatRegistration(creditDetails.getVatRegistration())
				.expectedCredit(creditDetails.getExpectedCredit())
				.build();
	}
}
