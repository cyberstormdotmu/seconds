package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.ws.admin.dto.BuyerDetailsDto;
import com.ishoal.ws.admin.dto.BuyerListingDto;
import com.ishoal.ws.buyer.dto.BuyerAllCreditsDto;
import com.ishoal.ws.buyer.dto.OrderBalancesDto;

public class BuyerDetailsDtoAdapter {

	public BuyerDetailsDto adapt(BuyerListingDto buyerListingDto, OrderBalancesDto orderBalancesDto,
			BuyerAllCreditsDto buyerAllCreditsDto) {

		return BuyerDetailsDto.aBuyerDetails()
							  .buyerListing(buyerListingDto)
							  .orderBalances(orderBalancesDto)
							  .buyerAllCredits(buyerAllCreditsDto)
							  .build();
	}
}
