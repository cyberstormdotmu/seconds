package com.ishoal.core.buyer;

import com.ishoal.core.domain.User;

public class FetchBuyerProfileRequest {
    private User user;

    private FetchBuyerProfileRequest(Builder builder) {

        user = builder.user;
    }

    public static Builder aFetchBuyerProfileRequest() {

        return new Builder();
    }

    public User getUser() {

        return user;
    }


    public static final class Builder {
        private User user;

        private Builder() {

        }

        public Builder(FetchBuyerProfileRequest copy) {

            this.user = copy.user;
        }

        public FetchBuyerProfileRequest build() {

            return new FetchBuyerProfileRequest(this);
        }

        public Builder user(User val) {

            user = val;
            return this;
        }
    }
}
