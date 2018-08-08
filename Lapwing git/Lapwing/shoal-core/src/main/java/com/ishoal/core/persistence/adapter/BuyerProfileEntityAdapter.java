package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.Addresses;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;

public class BuyerProfileEntityAdapter {

	private final UserEntityAdapter userEntityAdapter = new UserEntityAdapter();
	private final OrganisationEntityAdapter organisationEntityAdapter = new OrganisationEntityAdapter();
	private final ContactEntityAdapter contactEntityAdapter = new ContactEntityAdapter();
	private final BankAccountEntityAdapter bankAccountEntityAdapter = new BankAccountEntityAdapter();
	private final AddressesEntityAdapter addressesAdapter = new AddressesEntityAdapter();

	public BuyerProfileEntityAdapter() {

	}
	public BuyerProfile adapt(BuyerProfileEntity entity) {

		if (entity == null) {
			return null;
		}
		return BuyerProfile.aBuyerProfile().id(entity.getId())
				.user(userEntityAdapter.adapt(entity.getUser()))
				.organisation(organisationEntityAdapter.adapt(entity.getOrganisation()))
				.contact(contactEntityAdapter.adapt(entity.getContact()))
				.bankAccount(bankAccountEntityAdapter.adapt(entity.getBankAccount())).isCompleted(entity.isCompleted())
				.createdDate(entity.getCreated()).modifiedDate(entity.getModified()).version(entity.getVersion())
				.addresses(adaptAddresses(entity)).build();
		
	}
	private Addresses adaptAddresses(BuyerProfileEntity entity) {
        return addressesAdapter.adapt(entity.getAddresses());
    }
	
	public BuyerProfileEntity adapt(BuyerProfile buyer){
		
		BuyerProfileEntity buyerProfileEntity = new BuyerProfileEntity();
		buyerProfileEntity.setId(buyer.getId());
		buyerProfileEntity.setUser(userEntityAdapter.adapt(buyer.getUser()));
		buyerProfileEntity.setOrganisation(organisationEntityAdapter.adapt(buyer.getOrganisation()));
		
		if(buyer.getContact() != null)
		  {
		  buyerProfileEntity.setContact(contactEntityAdapter.adapt(buyer.getContact()));
		  }
		  if(buyer.getBankAccount() != null)
		  {
		  buyerProfileEntity.setBankAccount(bankAccountEntityAdapter.adapt(buyer.getBankAccount()));
		  }
		return buyerProfileEntity;
	}
}