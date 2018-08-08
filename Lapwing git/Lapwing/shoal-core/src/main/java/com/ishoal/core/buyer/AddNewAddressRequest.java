package com.ishoal.core.buyer;

import com.ishoal.core.domain.BuyerProfile;

public class AddNewAddressRequest {

	private String departmentName;
	private String buildingName;
	private String streetAddress;
	private String locality;
	private String postTown;
	private String postcode;
	private BuyerProfile buyer;

	public AddNewAddressRequest() {
	}

	public AddNewAddressRequest(Builder builder) {

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

	public BuyerProfile getBuyer() {
		return buyer;
	}

	 public void addBuyer(BuyerProfile buyerProfile)
	 {
		 this.buyer = buyerProfile;
	 }
	 
	public static final class Builder {
		private String departmentName;
		private String buildingName;
		private String streetAddress;
		private String locality;
		private String postTown;
		private String postcode;
		private BuyerProfile buyer;

		public Builder() {

		}

		public Builder(AddNewAddressRequest copy) {

			this.departmentName = copy.departmentName;
			this.buildingName = copy.buildingName;
			this.streetAddress = copy.streetAddress;
			this.locality = copy.locality;
			this.postTown = copy.postTown;
			this.postcode = copy.postcode;
			this.buyer = copy.buyer;
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

		public AddNewAddressRequest build() {
			return new AddNewAddressRequest(this);
		}

	}

	
}
