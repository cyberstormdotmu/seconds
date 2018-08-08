package com.ishoal.ws.common.dto;

public class AddressDto {
	private Long id;
    private String organisationName;
    private String departmentName;
    private String buildingName;
    private String streetAddress;
    private String locality;
    private String postTown;
    private String postcode;

    private AddressDto() {
        super();
    }

    private AddressDto(Builder builder) {
        this();
        id = builder.id;
        organisationName = builder.organisationName;
        departmentName = builder.departmentName;
        buildingName = builder.buildingName;
        streetAddress = builder.streetAddress;
        locality = builder.locality;
        postTown = builder.postTown;
        postcode = builder.postcode;
    }

    public static Builder anAddressDto() {
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

        public AddressDto build() {
            return new AddressDto(this);
        }
    }
}
