package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.BankAccount;
import com.ishoal.ws.buyer.dto.BankAccountDto;

public class BankAccountDtoAdapter {
    public BankAccountDtoAdapter() {

    }

    public BankAccountDto adapt(BankAccount bankAccount) {

        if (bankAccount == null) {
            return null;
        }

        return BankAccountDto.aBankAccount().accountName(bankAccount.getAccountName())
            .sortCode(bankAccount.getSortCode()).accountNumber(bankAccount.getAccountNumber())
            .bankName(bankAccount.getBankName()).buildingName(bankAccount.getBuildingName()).streetAddress(
                bankAccount.getStreetAddress()).locality(bankAccount.getLocality()).postTown(bankAccount.getPostTown())
            .postcode(bankAccount.getPostcode()).build();
    }

	public BankAccount adapt(BankAccountDto bankAccount) {
		if (bankAccount == null) {
            return null;
        }

        return BankAccount.aBankAccount().accountName(bankAccount.getAccountName())
            .sortCode(bankAccount.getSortCode()).accountNumber(bankAccount.getAccountNumber())
            .bankName(bankAccount.getBankName()).buildingName(bankAccount.getBuildingName()).streetAddress(
                bankAccount.getStreetAddress()).locality(bankAccount.getLocality()).postTown(bankAccount.getPostTown())
            .postcode(bankAccount.getPostcode()).build();
	}
}