package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Address {
	private final Long id;
    private final String organisationName;
    private final String departmentName;
    private final String buildingName;
    private final String streetAddress;
    private final String locality;
    private final String postTown;
    private final String postcode;

    private Address(Builder builder) {
    	
    	id = builder.id;
        postcode = builder.postcode;
        postTown = builder.postTown;
        locality = builder.locality;
        streetAddress = builder.streetAddress;
        buildingName = builder.buildingName;
        departmentName = builder.departmentName;
        organisationName = builder.organisationName;
    }
    
    public String toString() {
        return organisationName + "<br />" + 
                departmentName + "<br />" + 
                buildingName + "<br />" +
                streetAddress + "<br />" +
                locality + "<br />" +
                postTown + "<br />" +
                postcode;
    }
    
    public static Builder anAddress() {
        return new Builder();
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public Long getId() {
		return id;
	}

	public String getDepartmentName() {
        return departmentName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public String getPostTown() {
        return postTown;
    }

    public String getPostcode() {
        return postcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        return new EqualsBuilder()
        		.append(id, address.id)
                .append(organisationName, address.organisationName)
                .append(departmentName, address.departmentName)
                .append(buildingName, address.buildingName)
                .append(streetAddress, address.streetAddress)
                .append(locality, address.locality)
                .append(postTown, address.postTown)
                .append(postcode, address.postcode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
        		.append(id)
                .append(organisationName)
                .append(departmentName)
                .append(buildingName)
                .append(streetAddress)
                .append(locality)
                .append(postTown)
                .append(postcode)
                .toHashCode();
    }

    public static final class Builder {
    	private Long id;
        private String organisationName;
        private String departmentName;
        private String buildingName;
        private String streetAddress;
        private String locality;
        private String postTown;
        private String postcode;

        private Builder() {

        }

        public Builder id(Long val) {

            id = val;
            return this;
        }
        
        public Builder organisationName(String val) {

            organisationName = val;
            return this;
        }

        public Builder departmentName(String val) {

            departmentName = val;
            return this;
        }

        public Builder buildingName(String val) {

            buildingName = val;
            return this;
        }

        public Builder streetAddress(String val) {

            streetAddress = val;
            return this;
        }

        public Builder locality(String val) {

            locality = val;
            return this;
        }

        public Builder postTown(String val) {

            postTown = val;
            return this;
        }

        public Builder postcode(String val) {

            postcode = val;
            return this;
        }

        public Address build() {

            return new Address(this);
        }
        
    }
    
}
