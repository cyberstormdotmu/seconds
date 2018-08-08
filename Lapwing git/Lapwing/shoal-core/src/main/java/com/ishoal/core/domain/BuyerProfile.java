package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

public class BuyerProfile {

    private Long id;
    private User user;
    private Organisation organisation;
    private Contact contact;
    private BankAccount bankAccount;
    private boolean isComplete;
    private DateTime createdDate;
    private DateTime modifiedDate;
    private Long version;
    private Addresses addresses;
    
    public BuyerProfile() {

    }
    private BuyerProfile(Builder builder) {

        id = builder.id;
        user = builder.user;
        organisation = builder.organisation;
        contact = builder.contact;
        bankAccount = builder.bankAccount;
        isComplete = builder.isComplete;
        createdDate = builder.createdDate;
        modifiedDate = builder.modifiedDate;
        version = builder.version;
        addresses = builder.addresses;
    }

    public static Builder aBuyerProfile() {

        return new Builder();
    }

    public Long getId() {

        return id;
    }

    public User getUser() {

        return user;
    }

    public Addresses getAddresses() {
		return addresses;
	}

	public Organisation getOrganisation() {

        return organisation;
    }

    public Contact getContact() {

        return contact;
    }


    public BankAccount getBankAccount() {

        return bankAccount;
    }

    public boolean isCompleted() {
        return isComplete;
    }

    public void setCompleted() {
        this.isComplete = true;
    }

    public DateTime getCreatedDate() {

        return createdDate;
    }

    public DateTime getModifiedDate() {

        return modifiedDate;
    }

    public Long getVersion() {

        return version;
    }

    public static final class Builder {
        private Long id;
        private User user;
        private Organisation organisation;
        private Contact contact;
        private BankAccount bankAccount;
        private boolean isComplete;
        private DateTime createdDate;
        private DateTime modifiedDate;
        private Long version;
        private Addresses addresses;

        public Builder() {

        }

        public Builder(BuyerProfile copy) {

            this.id = copy.id;
            this.user = copy.user;
            this.organisation = copy.organisation;
            this.contact = copy.contact;
            this.bankAccount = copy.bankAccount;
            this.isComplete = copy.isComplete;
            this.createdDate = copy.createdDate;
            this.modifiedDate = copy.modifiedDate;
            this.version = copy.version;
            this.addresses = copy.addresses;
        }

        public Builder id(Long val) {

            id = val;
            return this;
        }

        public Builder user(User val) {

            user = val;
            return this;
        }

        public Builder organisation(Organisation val) {

            organisation = val;
            return this;
        }

        public Builder contact(Contact val) {

            contact = val;
            return this;
        }


        public Builder addresses(Addresses val) {

            addresses = val;
            return this;
        }
        
        public Builder bankAccount(BankAccount val) {

            bankAccount = val;
            return this;
        }

        public Builder isComplete(boolean val) {

            isComplete = val;
            return this;
        }

        public Builder createdDate(DateTime val) {

            createdDate = val;
            return this;
        }

        public Builder modifiedDate(DateTime val) {

            modifiedDate = val;
            return this;
        }

        public Builder version(Long val) {

            version = val;
            return this;
        }

        public Builder isCompleted(boolean val) {

            isComplete = val;
            return this;
        }

        public BuyerProfile build() {

            return new BuyerProfile(this);
        }
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }
}
