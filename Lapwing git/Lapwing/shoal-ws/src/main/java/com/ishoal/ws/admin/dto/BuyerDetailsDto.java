package com.ishoal.ws.admin.dto;

import com.ishoal.ws.buyer.dto.BuyerAllCreditsDto;
import com.ishoal.ws.buyer.dto.OrderBalancesDto;

public class BuyerDetailsDto {
	
    private BuyerListingDto buyerListing;
    private OrderBalancesDto orderBalances;
    private BuyerAllCreditsDto buyerAllCredits;
    
    private BuyerDetailsDto() {
        super();
    }
    
	public BuyerListingDto getBuyerListing() {
		return buyerListing;
	}

	public OrderBalancesDto getOrderBalances() {
		return orderBalances;
	}

	public BuyerAllCreditsDto getBuyerAllCredits() {
		return buyerAllCredits;
	}
	
	public static Builder aBuyerDetails() {
	    return new Builder();
	}
	
	private BuyerDetailsDto(Builder builder) {
        this();
        buyerListing = builder.buyerListing;
        orderBalances = builder.orderBalances;
        buyerAllCredits = builder.buyerAllCredits;
    }
	
	public static final class Builder {
        
		private BuyerListingDto buyerListing;
	    private OrderBalancesDto orderBalances;
	    private BuyerAllCreditsDto buyerAllCredits;

        private Builder() {
        }
        public Builder buyerListing(BuyerListingDto val) {
        	buyerListing = val;
            return this;
        }

        public Builder orderBalances(OrderBalancesDto val) {
        	orderBalances = val;
            return this;
        }

        public Builder buyerAllCredits(BuyerAllCreditsDto val) {
        	buyerAllCredits = val;
            return this;
        }
        
        public BuyerDetailsDto build() {
            return new BuyerDetailsDto(this);
        }
    }
}