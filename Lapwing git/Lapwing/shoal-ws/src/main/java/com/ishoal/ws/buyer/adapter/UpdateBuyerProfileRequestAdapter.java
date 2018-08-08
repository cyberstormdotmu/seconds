package com.ishoal.ws.buyer.adapter;

import static com.ishoal.core.domain.BankAccount.aBankAccount;
import static com.ishoal.core.domain.Contact.aContact;
import static com.ishoal.core.domain.Organisation.anOrganisation;

import com.ishoal.core.buyer.UpdateBuyerProfileRequest;
import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.dto.BankAccountDto;
import com.ishoal.ws.buyer.dto.BuyerProfileDto;
import com.ishoal.ws.buyer.dto.ContactDto;
import com.ishoal.ws.buyer.dto.OrganisationDto;
import com.ishoal.ws.buyer.dto.adapter.UserDtoAdapter;


public class UpdateBuyerProfileRequestAdapter {
	private final UserDtoAdapter userDtoAdapter = new UserDtoAdapter();
    public UpdateBuyerProfileRequestAdapter() {

    }

    public UpdateBuyerProfileRequest buildUpdateRequest(User user, BuyerProfileDto buyerSignup) {
        OrganisationDto organisation = buyerSignup.getOrganisation();
        ContactDto contact = buyerSignup.getContact();
        BankAccountDto bankAccount = buyerSignup.getBankAccount();

        if (bankAccount != null) {
        return UpdateBuyerProfileRequest.anUpdateBuyerProfileRequest()
            .user(userDtoAdapter.adapt(buyerSignup.getUser(), user)) 
            .organisation(anOrganisation().name(organisation.getName()).registrationNumber(
                organisation.getRegistrationNumber()).industry(organisation.getIndustry()).numberOfEmps(organisation.getNumberOfEmps()).build())
            .contact(aContact().title(contact.getTitle()).firstName(contact.getFirstName())
                .surname(contact.getSurname()).emailAddress(contact.getEmailAddress()).password(contact.getPassword())
                .phoneNumber(contact.getPhoneNumber()).build())
            .bankAccount(
                aBankAccount().accountName(bankAccount.getAccountName()).sortCode(bankAccount.getSortCode())
                    .accountNumber(bankAccount.getAccountNumber()).bankName(bankAccount.getBankName())
                    .buildingName(bankAccount.getBuildingName()).streetAddress(bankAccount.getStreetAddress())
                    .locality(bankAccount.getLocality()).postTown(bankAccount.getPostTown()).postcode(
                    bankAccount.getPostcode()).build())
            .build();
        } else {
        return UpdateBuyerProfileRequest.anUpdateBuyerProfileRequest()
            .user(userDtoAdapter.adapt(buyerSignup.getUser(), user)) 
            .organisation(anOrganisation().name(organisation.getName()).registrationNumber(
                organisation.getRegistrationNumber()).industry(organisation.getIndustry()).numberOfEmps(organisation.getNumberOfEmps()).build())
            .contact(aContact().title(contact.getTitle()).firstName(contact.getFirstName())
                .surname(contact.getSurname()).emailAddress(contact.getEmailAddress()).password(contact.getPassword())
                .phoneNumber(contact.getPhoneNumber()).build())
            .bankAccount(
                aBankAccount().accountName(null).sortCode(null)
                    .accountNumber(null).bankName(null)
                    .buildingName(null).streetAddress(null)
                    .locality(null).postTown(null).postcode(null).build())
            .build();
        	
        }
    }
}