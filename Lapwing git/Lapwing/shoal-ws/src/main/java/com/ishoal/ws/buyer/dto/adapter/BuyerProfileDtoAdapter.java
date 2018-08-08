package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.Addresses;
import com.ishoal.core.domain.BankAccount;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.Contact;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.dto.BuyerProfileDto;
import com.ishoal.ws.common.dto.adapter.AddressDtoAdapter;

public class BuyerProfileDtoAdapter {

	private final UserDtoAdapter userDtoAdapter = new UserDtoAdapter();
    private final ContactDtoAdapter contactDtoAdapter = new ContactDtoAdapter();
    private final OrganisationDtoAdapter organisationDtoAdapter = new OrganisationDtoAdapter();
    private final BankAccountDtoAdapter bankAccountDtoAdapter = new BankAccountDtoAdapter();
    private AddressDtoAdapter addressDtoAdapter = new AddressDtoAdapter();
     
    public BuyerProfileDtoAdapter() {

    }
    public BuyerProfileDto adapt(BuyerProfile buyerProfile) {

        Organisation organisation = buyerProfile.getOrganisation();
        User user = buyerProfile.getUser();
        Contact contact = buyerProfile.getContact();
        BankAccount bankAccount = buyerProfile.getBankAccount();
        Addresses addresses = buyerProfile.getAddresses();

        return BuyerProfileDto.aBuyerProfileDto()
        	
            .organisation(organisationDtoAdapter.adapt(organisation))
            .user(userDtoAdapter.adapt(user))
            .contact(contactDtoAdapter.adapt(contact))
            .bankAccount(bankAccountDtoAdapter.adapt(bankAccount))
            .completed(buyerProfile.isCompleted())
            .addresses(addressDtoAdapter.adapt(addresses))
            .build();
    }
	
	public BuyerProfile adapt(BuyerProfileDto buyer) {
	
		return BuyerProfile.aBuyerProfile()
	        	
	            .organisation(organisationDtoAdapter.adapt(buyer.getOrganisation()))
	            .user(userDtoAdapter.adapt(buyer.getUser()))
	            .contact(contactDtoAdapter.adapt(buyer.getContact()))
	            .bankAccount(bankAccountDtoAdapter.adapt(buyer.getBankAccount()))
	            .isCompleted(buyer.isIsCompleted())
	            .addresses(addressDtoAdapter.adapt(buyer.getAddresses()))
	            .build();
	}

	
}