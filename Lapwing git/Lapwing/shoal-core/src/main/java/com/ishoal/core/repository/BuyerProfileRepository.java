package com.ishoal.core.repository;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BankAccount;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.BuyerProfiles;
import com.ishoal.core.domain.Contact;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.adapter.BankAccountEntityAdapter;
import com.ishoal.core.persistence.adapter.BuyerProfileEntityAdapter;
import com.ishoal.core.persistence.adapter.ContactEntityAdapter;
import com.ishoal.core.persistence.adapter.OrganisationEntityAdapter;
import com.ishoal.core.persistence.adapter.UserEntityAdapter;
import com.ishoal.core.persistence.adapter.VendorEntityAdapter;
import com.ishoal.core.persistence.entity.BankAccountEntity;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.ContactEntity;
import com.ishoal.core.persistence.entity.OrganisationEntity;
import com.ishoal.core.persistence.entity.UserEntity;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.security.SecurePassword;

public class BuyerProfileRepository {

    private final BuyerProfileEntityRepository buyerProfileEntityRepository;
    private final OrganisationEntityAdapter organisationEntityAdapter;
    private final BankAccountEntityAdapter bankAccountEntityAdapter;
    private final ContactEntityAdapter contactEntityAdapter;
    private final BuyerProfileEntityAdapter buyerProfileEntityAdapter;
    private final VendorEntityAdapter vendorEntityAdapter;
    private final UserEntityAdapter userEntityAdapter;

    public BuyerProfileRepository(BuyerProfileEntityRepository buyerProfileEntityRepository) {
        this.buyerProfileEntityRepository = buyerProfileEntityRepository;
        this.organisationEntityAdapter = new OrganisationEntityAdapter();
        this.bankAccountEntityAdapter = new BankAccountEntityAdapter();
        this.contactEntityAdapter = new ContactEntityAdapter();
        this.userEntityAdapter = new UserEntityAdapter();
        this.buyerProfileEntityAdapter = new BuyerProfileEntityAdapter();
        this.vendorEntityAdapter = new VendorEntityAdapter();
        
    }

    public BuyerProfile createBuyerProfile(User user, BuyerProfile buyerProfile) {

        BuyerProfileEntity buyerProfileEntity = new BuyerProfileEntity();

        UserEntity userEntity = userEntityAdapter.adaptWithPassword(user);
        buyerProfileEntity.setUser(userEntity);
        create(buyerProfileEntity, buyerProfile.getOrganisation());

        BuyerProfileEntity savedEntity = buyerProfileEntityRepository.save(buyerProfileEntity);
        return buyerProfileEntityAdapter.adapt(savedEntity);
    }

    public BuyerProfile createNewBuyerProfile(BuyerProfile buyerProfile) {

        BuyerProfileEntity buyerProfileEntity = new BuyerProfileEntity();

        UserEntity userEntity = userEntityAdapter.adaptWithPassword(buyerProfile.getUser());
        buyerProfileEntity.setUser(userEntity);
        create(buyerProfileEntity, buyerProfile.getOrganisation());

        BuyerProfileEntity savedEntity = buyerProfileEntityRepository.save(buyerProfileEntity);
        return buyerProfileEntityAdapter.adapt(savedEntity);
    }
    
    public BuyerProfile fetchBuyerProfile(User user) {

    	BuyerProfileEntity entity = buyerProfileEntityRepository.findByUsername(user.getUsername());
    	
    	BuyerProfile buyerProfile = buyerProfileEntityAdapter.adapt(entity);
    	
    	return buyerProfile;
    }

    public BuyerProfile updateBuyerProfile(User user, BuyerProfile buyerProfile) {

        BuyerProfile updatedProfile = null;
        
        BuyerProfileEntity currentBuyerProfile = buyerProfileEntityRepository.findByUsername(user.getUsername());
        buyerProfile.getUser().setHashedPassword(SecurePassword.fromBCryptHash(currentBuyerProfile.getUser().getHashedPassword()));
        buyerProfile.getUser().setAppliedFor(currentBuyerProfile.getUser().getAppliedFor());
        buyerProfile.getUser().setVendor(vendorEntityAdapter.adapt(currentBuyerProfile.getUser().getVendor()));
        if (currentBuyerProfile != null) {
            updateProfileAttributes(currentBuyerProfile, buyerProfile);
            currentBuyerProfile.setCompleted(buyerProfile.isCompleted());
            BuyerProfileEntity savedEntity = buyerProfileEntityRepository.save(currentBuyerProfile);
            updatedProfile = buyerProfileEntityAdapter.adapt(savedEntity);
        }
        return updatedProfile;
    }

    private void updateProfileAttributes(BuyerProfileEntity entity, BuyerProfile buyerProfile) {
    	
    	if (exists(entity.getUser())) {
            update(entity, buyerProfile.getUser());
        } else {
            create(entity, buyerProfile.getUser());
        }

        if (exists(entity.getOrganisation())) {
            update(entity, buyerProfile.getOrganisation());
        } else {
            create(entity, buyerProfile.getOrganisation());
        }

        if (exists(entity.getContact())) {
            update(entity, buyerProfile.getContact());
        } else {
            create(entity, buyerProfile.getContact());
        }


        if (exists(entity.getBankAccount())) {
            update(entity, buyerProfile.getBankAccount());
        } else {
            create(entity, buyerProfile.getBankAccount());
        }

    }
    
    private boolean exists(UserEntity entity) {

        return entity != null && entity.getId() != null;
    }

    private void create(BuyerProfileEntity entity, User user) {

        if(user.getVendor() != null){
            entity.setUser(userEntityAdapter.adaptWithVendor(user, vendorEntityAdapter.adapt(user.getVendor())));
        }
        else {
            entity.setUser(userEntityAdapter.adaptWithPassword(user));
        }
    }

    private void update(BuyerProfileEntity entity, User user) {

        Long currentId = entity.getUser().getId();
        create(entity, user);
        entity.getUser().setId(currentId);
    }

    private boolean exists(OrganisationEntity entity) {

        return entity != null && entity.getId() != null;
    }

    private void create(BuyerProfileEntity entity, Organisation organisation) {

        entity.setOrganisation(organisationEntityAdapter.adapt(organisation));
    }

    private void update(BuyerProfileEntity entity, Organisation organisation) {

        Long currentId = entity.getOrganisation().getId();
        create(entity, organisation);
        entity.getOrganisation().setId(currentId);
    }

    private boolean exists(ContactEntity entity) {

        return entity != null && entity.getId() != null;
    }

    private void create(BuyerProfileEntity entity, Contact contact) {

        entity.setContact(contactEntityAdapter.adapt(contact));
    }

    private void update(BuyerProfileEntity entity, Contact contact) {

        Long currentId = entity.getContact().getId();
        create(entity, contact);
        entity.getContact().setId(currentId);
    }

    private boolean exists(BankAccountEntity entity) {

        return entity != null && entity.getId() != null;
    }

    private void update(BuyerProfileEntity entity, BankAccount bankAccount) {
        Long currentId = entity.getBankAccount().getId();
        create(entity, bankAccount);
        entity.getBankAccount().setId(currentId);
    }

    private void create(BuyerProfileEntity entity, BankAccount account) {

        entity.setBankAccount(bankAccountEntityAdapter.adapt(account));
    }

    public BuyerProfiles findAllBuyers() {
        return IterableUtils.mapToCollection(buyerProfileEntityRepository.findAllConfirm("CONFIRM"), buyerProfileEntityAdapter::adapt, BuyerProfiles::over);
    }
    
    public BuyerProfile fetchProfileById(String id) {
    	return buyerProfileEntityAdapter.adapt(buyerProfileEntityRepository.findOne(Long.parseLong(id)));   
    }
}
