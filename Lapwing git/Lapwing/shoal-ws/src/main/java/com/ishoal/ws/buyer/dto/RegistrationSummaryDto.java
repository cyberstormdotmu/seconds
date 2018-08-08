package com.ishoal.ws.buyer.dto;

import org.joda.time.DateTime;

public class RegistrationSummaryDto {

    private BuyerSummaryDto buyer;
    private OrganisationDto organisation;
    private DateTime registrationDate;

    private RegistrationSummaryDto(Builder builder) {

        buyer = builder.buyer;
        organisation = builder.organisation;
        registrationDate = builder.registrationDate;
    }

    public BuyerSummaryDto getBuyer() {

        return buyer;
    }

    public OrganisationDto getOrganisation() {

        return organisation;
    }

    public DateTime getRegisteredDate() {

        return registrationDate;
    }

    public static Builder aRegistration() {

        return new Builder();
    }

    public static final class Builder {
        private BuyerSummaryDto buyer;
        private OrganisationDto organisation;
        private DateTime registrationDate;

        public Builder() {

        }

        public Builder(RegistrationSummaryDto copy) {

            this.buyer = copy.buyer;
            this.organisation = copy.organisation;
            this.registrationDate = copy.registrationDate;
        }

        public Builder buyer(BuyerSummaryDto val) {

            buyer = val;
            return this;
        }

        public Builder organisation(OrganisationDto val) {

            organisation = val;
            return this;
        }

        public Builder registrationDate(DateTime val) {

            registrationDate = val;
            return this;
        }

        public RegistrationSummaryDto build() {

            return new RegistrationSummaryDto(this);
        }
    }
}
