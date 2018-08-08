package com.ishoal.ws.buyer.adapter;

import com.ishoal.core.buyer.AddressRequest;
import com.ishoal.ws.common.dto.AddressDto;

public class AddressRequestAdapter {

	public AddressRequestAdapter() {

	}

	public AddressRequest buildRequest(AddressDto newAddress){

		return AddressRequest.aAddNewAddressRequest()
				.id(newAddress.getId())
				.departmentName(newAddress.getDepartmentName())
				.buildingName(newAddress.getBuildingName())
				.streetAddress(newAddress.getStreetAddress())
				.locality(newAddress.getLocality())
				.postTown(newAddress.getPostTown())
				.postcode(newAddress.getPostcode())
				.build();
	}
}
