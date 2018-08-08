package com.ishoal.ws.common.dto.adapter;

import static com.ishoal.common.util.IterableUtils.mapToCollection;

import java.util.Collections;
import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.Addresses;
import com.ishoal.ws.common.dto.AddressDto;

public class AddressDtoAdapter {
	public List<AddressDto> adapt(Addresses addresses) {
		if (addresses == null) {
			return Collections.emptyList();
		}
		return IterableUtils.mapToList(addresses, this::adapt);
	}

	public AddressDto adapt(Address domain) {
		if (domain == null) {
			return null;
		}

		return AddressDto.anAddressDto().id(domain.getId()).organisationName(domain.getOrganisationName())
				.departmentName(domain.getDepartmentName()).buildingName(domain.getBuildingName())
				.streetAddress(domain.getStreetAddress()).locality(domain.getLocality()).postTown(domain.getPostTown())
				.postcode(domain.getPostcode()).build();
	}

	public Addresses adapt(List<AddressDto> addresses) {
		return mapToCollection(addresses, this::adapt, Addresses::over);
	}

	public Address adapt(AddressDto dto) {
		if (dto == null) {
			return null;
		}

		return Address.anAddress().id(dto.getId()).organisationName(dto.getOrganisationName()).departmentName(dto.getDepartmentName())
				.buildingName(dto.getBuildingName()).streetAddress(dto.getStreetAddress()).locality(dto.getLocality())
				.postTown(dto.getPostTown()).postcode(dto.getPostcode()).build();
	}

}