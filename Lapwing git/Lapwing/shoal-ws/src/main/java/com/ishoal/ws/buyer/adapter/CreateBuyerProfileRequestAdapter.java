package com.ishoal.ws.buyer.adapter;

import com.ishoal.core.buyer.RegisterBuyerRequest;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import com.ishoal.core.security.SecurePassword;
import com.ishoal.ws.buyer.dto.BuyerRegistrationDto;

public class CreateBuyerProfileRequestAdapter {
    public CreateBuyerProfileRequestAdapter() {

    }

    public RegisterBuyerRequest buildRequest(BuyerRegistrationDto registration, String randomString) {

        RegisterBuyerRequest request = RegisterBuyerRequest.aCreateBuyerProfileRequest()
            .user(User.aUser().forename(registration.getBuyer().getFirstName())
                .surname(registration.getBuyer().getSurname())
                .username(registration.getBuyer().getEmailAddress())
                .mobileNumber(registration.getOrganisation().getMobileNumber())
                .hashedPassword(
                    SecurePassword.fromClearText(registration.getBuyer().getPassword()))
                .registrationToken(randomString)
                .appliedFor(registration.getBuyer().getAppliedFor())
                .lapwingAccountNumber(registration.getBuyer().getLapwingAccountNumber())
                .westcoastAccountNumber(registration.getBuyer().getWestcoastAccountNumber())
                .appliedReferralCode(registration.getBuyer().getAppliedReferralCode())
                .appliedFor(registration.getBuyer().getAppliedFor())
                .build())
            .organisation(Organisation.anOrganisation().name(registration.getOrganisation().getName())
            	.registrationNumber(registration.getOrganisation().getRegistrationNumber())
                .build())
            .build();

        registration.clearPassword();
        return request;
    }
}