package com.ishoal.ws.buyer.dto;

public class BuyerRegistrationDto {
    private UserDto buyer;
    private OrganisationDto organisation;
    private String siteDateKey;

    public BuyerRegistrationDto() {

    }

    private BuyerRegistrationDto(Builder builder) {

        buyer = builder.buyer;
        organisation = builder.organisationDto;
        siteDateKey = builder.siteDateKey;
    }

    public static Builder aBuyerRegistrationDto() {

        return new Builder();
    }

    public void clearPassword() {
        buyer.clearPassword();
    }

    public UserDto getBuyer() {
        return buyer;
    }

    public OrganisationDto getOrganisation() {

        return organisation;
    }

    public String getSiteDateKey(){
        
        return siteDateKey;
    }
    

    public static final class Builder {
        private UserDto buyer;
        private OrganisationDto organisationDto;
        private String siteDateKey;

        public Builder() {

        }

        public Builder(BuyerRegistrationDto copy) {

            this.buyer = copy.buyer;
            this.organisationDto = copy.organisation;
            this.siteDateKey = copy.siteDateKey;
        }

        public BuyerRegistrationDto build() {

            return new BuyerRegistrationDto(this);
        }

        public Builder buyer(UserDto val) {

            buyer = val;
            return this;
        }

        public Builder organisation(OrganisationDto val) {

            organisationDto = val;
            return this;
        }
        
        public Builder siteDateKey(String val){
            
            siteDateKey = val;
            return this;
            
        }
        
        
    }
}
