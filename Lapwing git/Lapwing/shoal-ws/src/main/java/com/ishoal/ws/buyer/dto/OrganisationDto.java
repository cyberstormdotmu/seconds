package com.ishoal.ws.buyer.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OrganisationDto {
    private String name;
    private String registrationNumber;
    private String industry;
    private String numberOfEmps;  
    private String mobileNumber;
    
    public OrganisationDto() {

    }

    private OrganisationDto(Builder builder) {

        name = builder.name;
        registrationNumber = builder.registrationNumber;
        mobileNumber=builder.mobileNumber;
        industry=builder.industry;
        numberOfEmps=builder.numberOfEmps;
    }

    public String getName() {

        return name;
    }

    public String getMobileNumber() {
		return mobileNumber;
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

    public static Builder anOrganisationDto() {
        return new Builder();
    }

    
    public static final class Builder {
        private String name;
        private String registrationNumber;
        private String mobileNumber;
        private String industry;
        private String numberOfEmps;  

        public Builder() {

        }

        public Builder(OrganisationDto copy) {

            this.name = copy.name;
            this.registrationNumber = copy.registrationNumber;
            this.mobileNumber=copy.mobileNumber;
            this.industry=copy.industry;
            this.numberOfEmps=copy.numberOfEmps;
        }

        public Builder name(String val) {

            name = val;
            return this;
        }

        public Builder registrationNumber(String val) {

            registrationNumber = val;
            return this;
        }
        public Builder mobileNumber(String val) {

        	mobileNumber = val;
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
        

        public OrganisationDto build() {

            return new OrganisationDto(this);
        }
        
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }
}
