package com.ishoal.core.buyer;

import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.BankAccount;
import com.ishoal.core.domain.Contact;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UpdateBuyerProfileRequest {

    private final User user;
    private final Organisation organisation;
    private final Contact contact;
    private final Address deliveryAddress;
    private final BankAccount bankAccount;

    private UpdateBuyerProfileRequest(Builder builder) {

        bankAccount = builder.bankAccount;
        deliveryAddress = builder.deliveryAddress;
        contact = builder.contact;
        organisation = builder.organisation;
        user = builder.user;
    }

    public static Builder anUpdateBuyerProfileRequest() {
        return new Builder();
    }

    public Organisation getOrganisation() {

        return organisation;
    }

    public Contact getContact() {

        return contact;
    }

    public Address getDeliveryAddress() {

        return deliveryAddress;
    }

    public BankAccount getBankAccount() {

        return bankAccount;
    }

    public User getUser() {

        return user;
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }


    public static final class Builder {
        private Organisation organisation;
        private User user;
        private Contact contact;
        private Address deliveryAddress;
        private BankAccount bankAccount;

        public Builder() {

        }

        public Builder organisation(Organisation val) {

            organisation = val;
            return this;
        }

        public Builder user(User val) {

            user = val;
            return this;
        }

        public Builder contact(Contact val) {

            contact = val;
            return this;
        }

        public Builder deliveryAddress(Address val) {

            deliveryAddress = val;
            return this;
        }

        public Builder bankAccount(BankAccount val) {

            bankAccount = val;
            return this;
        }

        public UpdateBuyerProfileRequest build() {

            return new UpdateBuyerProfileRequest(this);
        }
    }
}
