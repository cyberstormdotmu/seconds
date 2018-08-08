package com.ishoal.core.buyer;

import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;

public class RegisterBuyerRequest {

    private User user;
    private Organisation organisation;

    private RegisterBuyerRequest(Builder builder) {

        user = builder.user;
        organisation = builder.organisation;
    }

    public static Builder aCreateBuyerProfileRequest() {

        return new Builder();
    }

    public User getUser() {

        return user;
    }

    public Organisation getOrganisation() {

        return organisation;
    }

    public static final class Builder {
        private User user;
        private Organisation organisation;

        public Builder() {

        }

        public Builder(RegisterBuyerRequest copy) {

            this.user = copy.user;
            this.organisation = copy.organisation;
        }

        public RegisterBuyerRequest build() {

            return new RegisterBuyerRequest(this);
        }

        public Builder user(User val) {

            user = val;
            return this;
        }

        public Builder organisation(Organisation val) {

            organisation = val;
            return this;
        }
    }
}
