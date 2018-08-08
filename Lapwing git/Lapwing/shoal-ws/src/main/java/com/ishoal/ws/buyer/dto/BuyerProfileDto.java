package com.ishoal.ws.buyer.dto;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ishoal.ws.common.dto.AddressDto;

public class BuyerProfileDto {

	private UserDto user;
    private OrganisationDto organisation;
    private ContactDto contact;
    private BankAccountDto bankAccount;
    private boolean isCompleted;
    private List<AddressDto> addresses;
    
    public List<AddressDto> getAddresses() {
		return addresses;
	}

	public BuyerProfileDto() {

    }

    private BuyerProfileDto(Builder builder) {

    	user = builder.user;
        organisation = builder.organisation;
        contact = builder.contact;
        bankAccount = builder.bankAccount;
        isCompleted = builder.isCompleted;
        addresses = builder.addresses;
    }

    public UserDto getUser() {
		return user;
	}

	public OrganisationDto getOrganisation() {

        return organisation;
    }

    public ContactDto getContact() {

        return contact;
    }


    public BankAccountDto getBankAccount() {

        return bankAccount;
    }

    // stupid name for this getter is because jackson is renaming the JSON field
    // 'isCompleted' to 'completed'.
    public boolean isIsCompleted() {

        return isCompleted;
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }
    public static Builder aBuyerProfileDto() {
        return new Builder();
    }

    public static final class Builder {
    	private UserDto user;
        private OrganisationDto organisation;
        private ContactDto contact;
        private BankAccountDto bankAccount;
        private boolean isCompleted;
        private List<AddressDto> addresses;

        public Builder() {

        }

        public Builder(BuyerProfileDto copy) {

        	this.user = copy.user;
            this.organisation = copy.organisation;
            this.contact = copy.contact;
            this.bankAccount = copy.bankAccount;
            this.isCompleted = copy.isCompleted;
            this.addresses = copy.addresses;
        }
        
        public Builder user(UserDto val) {

            user = val;
            return this;
        }
        
        public Builder organisation(OrganisationDto val) {

            organisation = val;
            return this;
        }

        public Builder contact(ContactDto val) {

            contact = val;
            return this;
        }

        public Builder addresses(List<AddressDto> val) {

            addresses = val;
            return this;
        }

        
        public Builder bankAccount(BankAccountDto val) {

            bankAccount = val;
            return this;
        }

        public Builder completed(boolean val) {

            isCompleted = val;
            return this;
        }

        public BuyerProfileDto build() {

            return new BuyerProfileDto(this);
        }
    }
}