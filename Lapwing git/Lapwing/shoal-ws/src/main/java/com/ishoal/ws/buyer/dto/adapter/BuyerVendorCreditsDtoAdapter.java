package com.ishoal.ws.buyer.dto.adapter;

import java.util.List;
import java.util.stream.Collectors;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.domain.BuyerVendorCredits;
import com.ishoal.ws.buyer.dto.BuyerVendorCreditsDto;

public class BuyerVendorCreditsDtoAdapter {

	public BuyerVendorCreditsDto adapt(BuyerVendorCredit buyerVendorCredit) {
		return BuyerVendorCreditsDto.aBuyerVendorCreditsDto().vendorName((buyerVendorCredit.getVendor().getName()))
				.totalCredits(buyerVendorCredit.getTotalCredit())
				.buyerName(buyerVendorCredit.getBuyer().getUser().getUsername())
				.availableCredits(buyerVendorCredit.getAvailableCredit()).build();
	}
	 public List<BuyerVendorCreditsDto> adapt(List<BuyerVendorCredit> buyerVendorCredits) {
	        return IterableUtils.mapToList(buyerVendorCredits, this::adapt);
	    }
    public List<BuyerVendorCreditsDto> adapt(BuyerVendorCredits buyerVendorCredits) {
        return buyerVendorCredits.stream().map(buyerVendorCredit -> adapt(buyerVendorCredit)).collect(Collectors.toList());
    } 
}
