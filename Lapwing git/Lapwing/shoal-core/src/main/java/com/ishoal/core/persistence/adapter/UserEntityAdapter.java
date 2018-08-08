package com.ishoal.core.persistence.adapter;

import static com.ishoal.core.domain.User.aUser;

import java.util.List;

import com.ishoal.core.domain.Roles;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.UserEntity;
import com.ishoal.core.persistence.entity.UserRoleEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.security.SecurePassword;

public class UserEntityAdapter {

    private final UserRoleEntityAdapter userRoleEntityAdapter = new UserRoleEntityAdapter();
    private final VendorEntityAdapter vendorAdapter = new VendorEntityAdapter();
    public User adaptWithPassword(UserEntity entity) {
        if(entity == null) {
            return null;
        }
        return adaptInternal(entity).build();
    }

    public User adapt(UserEntity entity) {
        if(entity == null) {
            return null;
        }
        return adaptInternal(entity).hashedPassword(SecurePassword.PROTECTED).build();
    }

    private User.Builder adaptInternal(UserEntity entity) {
        return aUser().id(UserId.from(entity.getId()))
                .username(entity.getUsername())
                .forename(entity.getForename())
                .registrationToken(entity.getRegistrationToken())
                .surname(entity.getSurname())
                .mobileNumber(entity.getMobileNumber())
                .hashedPassword(SecurePassword.fromBCryptHash(entity.getHashedPassword()))
                .roles(adaptRoles(entity))
                .lapwingAccountNumber(entity.getLapwingAccountNumber())
                .westcoastAccountNumber(entity.getWestcoastAccountNumber())
                .buyerReferralCode(entity.getBuyerReferralCode())
                .appliedReferralCode(entity.getAppliedReferralCode())
                .appliedFor(entity.getAppliedFor())
                .vendor(adaptVendor(entity));
    }
    private Vendor adaptVendor(UserEntity entity) {
        return vendorAdapter.adapt(entity.getVendor());
    }
    private Roles adaptRoles(UserEntity entity) {
        return userRoleEntityAdapter.adapt(entity.getRoles());
    }

    private List<UserRoleEntity> adaptRoles(Roles roles) {
        return userRoleEntityAdapter.adapt(roles);
    }

    public UserEntity adaptWithPassword(User user) {

        UserEntity entity = adapt(user);
        entity.setPassword(user.getHashedPassword().getValue());
        return entity;
    }

    public UserEntity adapt(User user) {

        UserEntity entity = new UserEntity();
        entity.setId(user.getId().asLong());
        entity.setAuthoriseDate(user.getAuthoriseDate());
        entity.setForename(user.getForename());
        entity.setSurname(user.getSurname());
        entity.setUsername(user.getUsername());
        entity.setMobileNumber(user.getMobileNumber());
        entity.setRegistrationToken(user.getRegistrationToken());
        entity.addRoles(adaptRoles(user.getRoles()));
        entity.setLapwingAccountNumber(user.getLapwingAccountNumber());
        entity.setWestcoastAccountNumber(user.getWestcoastAccountNumber());
        entity.setBuyerReferralCode(user.getBuyerReferralCode());
        entity.setAppliedReferralCode(user.getAppliedReferralCode());
        entity.setAppliedFor(user.getAppliedFor());
        return entity;
    }

    public UserEntity adaptWithVendor(User user, VendorEntity vendorEntityResponse) {
        UserEntity entity = adapt(user);
        entity.setPassword(user.getHashedPassword().getValue());
        entity.setVendor(vendorEntityResponse);
        return entity;
    }
}
