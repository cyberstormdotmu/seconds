package com.ishoal.core.buyer;

import static com.ishoal.core.domain.BankAccount.aBankAccount;
import static com.ishoal.core.domain.BuyerProfile.aBuyerProfile;
import static com.ishoal.core.domain.Contact.aContact;
import static com.ishoal.core.domain.Organisation.anOrganisation;

import com.ishoal.core.domain.BankAccount;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.Contact;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.adapter.BankAccountEntityAdapter;
import com.ishoal.core.persistence.adapter.ContactEntityAdapter;
import com.ishoal.core.persistence.adapter.OrganisationEntityAdapter;
import com.ishoal.core.persistence.adapter.UserEntityAdapter;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;

public class BuyerProfileAdapter {

    private final UserEntityAdapter userEntityAdapter = new UserEntityAdapter();
    private final OrganisationEntityAdapter organisationEntityAdapter = new OrganisationEntityAdapter();
    private final ContactEntityAdapter contactEntityAdapter = new ContactEntityAdapter();
    private final BankAccountEntityAdapter bankAccountEntityAdapter = new BankAccountEntityAdapter();
    
    public BuyerProfile adapt(RegisterBuyerRequest request, User user) {

        return aBuyerProfile().organisation(request.getOrganisation()).user(user).build();
    }
    public BuyerProfile adaptNew(RegisterBuyerRequest request) {

        return aBuyerProfile()
        		.organisation(request.getOrganisation())
        		.user(request.getUser()).build();
    }

    public BuyerProfile adapt(UpdateBuyerProfileRequest request) {

        User user = request.getUser();
        Organisation organisation = request.getOrganisation();
        Contact contact = request.getContact();
        BankAccount bankAccount = request.getBankAccount();

        return BuyerProfile.aBuyerProfile()
            .user(user)
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

    }
    
    public BuyerProfileEntity adapt(BuyerProfile buyerProfile) {

        BuyerProfileEntity entity = new BuyerProfileEntity();
        entity.setId(buyerProfile.getId());
        entity.setUser(userEntityAdapter.adapt(buyerProfile.getUser()));
        entity.setOrganisation(organisationEntityAdapter.adapt(buyerProfile.getOrganisation()));
        entity.setContact(contactEntityAdapter.adapt(buyerProfile.getContact()));
        entity.setBankAccount(bankAccountEntityAdapter.adapt(buyerProfile.getBankAccount()));
        entity.setCompleted(buyerProfile.isCompleted());
        
        return entity;
    }
    
}
