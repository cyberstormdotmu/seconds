package com.ishoal.core.domain;

public class Organisation  {

    private final OrganisationId id;
    private final String name;
    private final String registrationNumber;
    private final String industry;
    private final String numberOfEmps;  
    
    private Organisation(Builder builder) {
    	
        id = builder.id;
        name = builder.name;
        registrationNumber = builder.registrationNumber;
        industry=builder.industry;
        numberOfEmps=builder.numberOfEmps;
    }

    public static Builder anOrganisation() {
        return new Builder();
    }

    public OrganisationId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getIndustry() {
        return industry;
    }

    public String getNumberOfEmps() {
        return numberOfEmps;
    }


    public static final class Builder {
        private OrganisationId id = OrganisationId.emptyOrganisationId;
        private String name;
        private String registrationNumber;
        private String industry;
        private String numberOfEmps;  

        private Builder() {

        }

        public Builder(Organisation copy) {

            this.id = copy.id;
            this.name = copy.name;
            this.registrationNumber = copy.registrationNumber;
        }

        public Builder id(OrganisationId val) {

            id = val;
            return this;
        }

        public Builder name(String val) {

            name = val;
            return this;
        }

        public Builder registrationNumber(String val) {

            registrationNumber = val;
            return this;
        }
        
        public Builder industry(String val) {

            industry = val;
            return this;
        }
        
        public Builder numberOfEmps(String val) {

            numberOfEmps = val;
            return this;
        }
        
        public Organisation build() {

            return new Organisation(this);
        }
    }
}
