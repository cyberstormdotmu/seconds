package com.ishoal.ws.admin.dto.adapter;

import static com.ishoal.ws.admin.dto.BuyerListingDto.aBuyerListing;

import java.util.List;
import java.util.stream.Collectors;

import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.BuyerProfiles;
import com.ishoal.ws.admin.dto.BuyerListingDto;


public class BuyerListingDtoAdapter {
    
    public List<BuyerListingDto> adapt(BuyerProfiles buyerProfiles) {
        return buyerProfiles.stream().map(buyerProfile -> adapt(buyerProfile)).collect(Collectors.toList());
    }

    public BuyerListingDto adapt(BuyerProfile buyerProfile) {
        return aBuyerListing()
            .buyerId(buyerProfile.getId().toString())
            .userName(buyerProfile.getUser().getUsername())
            .firstName(buyerProfile.getUser().getForename())
            .surname(buyerProfile.getUser().getSurname())
            .mobileNumber(buyerProfile.getUser().getMobileNumber())
            .emailAddress(buyerProfile.getUser().getEmailAddress())
            .organisation(buyerProfile.getOrganisation().getName())
            .companyNo(buyerProfile.getOrganisation().getRegistrationNumber())
            .westcoastAccountNumber(buyerProfile.getUser().getWestcoastAccountNumber())
            .vatNumber(buyerProfile.getUser().getLapwingAccountNumber())
            .bankAccount(buyerProfile.getBankAccount())
            .build();
    }
}
