package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.BankAccount;
import com.ishoal.core.persistence.entity.BankAccountEntity;

public class BankAccountEntityAdapter {
    public BankAccountEntityAdapter() {

    }

    public BankAccountEntity adapt(BankAccount bankAccount) {

        BankAccountEntity entity = new BankAccountEntity();
        entity.setAccountName(bankAccount.getAccountName());
        entity.setSortcode(bankAccount.getSortCode());
        entity.setAccountNumber(bankAccount.getAccountNumber());
        entity.setBankName(bankAccount.getBankName());
        entity.setBuildingName(bankAccount.getBuildingName());
        entity.setStreetAddress(bankAccount.getStreetAddress());
        entity.setLocality(bankAccount.getLocality());
        entity.setPostTown(bankAccount.getPostTown());
        entity.setPostCode(bankAccount.getPostcode());
        return entity;
    }

    public BankAccount adapt(BankAccountEntity bankAccount) {

        if (bankAccount == null) {
            return null;
        }

        return BankAccount.aBankAccount().accountName(bankAccount.getAccountName()).accountNumber(
            bankAccount.getAccountNumber())
            .sortCode(bankAccount.getSortcode()).bankName(bankAccount.getBankName()).buildingName(
                bankAccount.getBuildingName())
            .streetAddress(bankAccount.getStreetAddress()).locality(bankAccount.getLocality()).postTown(
                bankAccount.getPostTown())
            .postcode(bankAccount.getPostCode()).build();
    }
}