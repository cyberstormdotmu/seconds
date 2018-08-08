package com.ishoal.core.buyer;

import com.ishoal.core.domain.BuyerProfile;


public class AddressRequest {
	private Long id;
	private String organisationName;
	private String departmentName;
	private String buildingName;
	private String streetAddress;
	private String locality;
	private String postTown;
	private String postcode;
	private BuyerProfile buyer;

	public AddressRequest() {
	}

	public AddressRequest(Builder builder) {
		this.id = builder.id;
		this.organisationName = builder.organisationName;
		this.departmentName = builder.departmentName;
		this.buildingName = builder.buildingName;
		this.streetAddress = builder.streetAddress;
		this.locality = builder.locality;
		this.postTown = builder.postTown;
		this.postcode = builder.postcode;
		this.buyer = builder.buyer;
	}

	public static Builder aAddNewAddressRequest() {
		return new Builder();
	}


	public String getOrganisationName() {
		return organisationName;
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

	public Long getId() {
		return id;
	}

	public String getPostcode() {
		return postcode;
	}

	public BuyerProfile getBuyer() {
		return buyer;
	}

	 public void addBuyer(BuyerProfile buyerProfile)
	 {
		 this.buyer = buyerProfile;
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
		private BuyerProfile buyer;

		public Builder() {

		}

		public Builder(AddressRequest copy) {

			this.organisationName = copy.organisationName;
			this.departmentName = copy.departmentName;
			this.buildingName = copy.buildingName;
			this.streetAddress = copy.streetAddress;
			this.locality = copy.locality;
			this.postTown = copy.postTown;
			this.postcode = copy.postcode;
			this.buyer = copy.buyer;
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

		public Builder buyer(BuyerProfile val) {
			buyer = val;
			return this;
		}

		public AddressRequest build() {
			return new AddressRequest(this);
		}
	}
	
	/*@Override
	public boolean equals(Object obj) {
		 if (obj == this) {
	            return true;
	        }
	 
	         Check if o is an instance of Complex or not
	          "null instanceof [type]" also returns false 
	        if (!(obj instanceof AddressRequest)) {
	            return false;
	        }
	         
	        // typecast o to Complex so that we can compare data members 
	        AddressRequest Ad = (AddressRequest) obj;
	         
	        // Compare the data members and return accordingly 
	        return organisationName.equals(Ad.organisationName)
	                && departmentName.equals(Ad.departmentName)
	                && buildingName.equals(Ad.buildingName)
	                && streetAddress.equals(Ad.streetAddress)
	                && locality.equals(Ad.locality)
	                && postTown.equals(Ad.postTown)
	                && postcode.equals(Ad.postcode);
	}
	
	@Override
	public int hashCode() {
	    int hash = 3;
	    hash = 53 * hash + (this.organisationName != null ? this.organisationName.hashCode() : 0);
	    hash = 53 * hash + (this.departmentName != null ? this.departmentName.hashCode() : 0);
	    hash = 53 * hash + (this.buildingName != null ? this.buildingName.hashCode() : 0);
	    hash = 53 * hash + (this.streetAddress != null ? this.streetAddress.hashCode() : 0);
	    hash = 53 * hash + (this.locality != null ? this.locality.hashCode() : 0);
	    hash = 53 * hash + (this.postTown != null ? this.postTown.hashCode() : 0);
	    hash = 53 * hash + (this.postcode != null ? this.postcode.hashCode() : 0);
	    return hash;
	}*/
}