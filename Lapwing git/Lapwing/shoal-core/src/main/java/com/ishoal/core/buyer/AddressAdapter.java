package com.ishoal.core.buyer;

import com.ishoal.core.domain.Address;

public class AddressAdapter {

	public AddressAdapter() {

	}

	public Address buildRequest(AddressRequest newAddress) {

		return Address.anAddress().id(newAddress.getId()).departmentName(newAddress.getDepartmentName())
				.buildingName(newAddress.getBuildingName()).streetAddress(newAddress.getStreetAddress())
				.locality(newAddress.getLocality()).postTown(newAddress.getPostTown())
				.postcode(newAddress.getPostcode()).build();
	}
	
	
	public AddressRequest buildRequest(Address newAddress) {

		return AddressRequest.aAddNewAddressRequest().departmentName(newAddress.getDepartmentName())
				.buildingName(newAddress.getBuildingName()).streetAddress(newAddress.getStreetAddress())
				.locality(newAddress.getLocality()).postTown(newAddress.getPostTown())
				.postcode(newAddress.getPostcode()).build();
	}
}
