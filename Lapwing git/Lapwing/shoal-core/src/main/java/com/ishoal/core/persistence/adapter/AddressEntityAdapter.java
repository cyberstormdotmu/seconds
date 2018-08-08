package com.ishoal.core.persistence.adapter;

import java.util.Collections;
import java.util.List;
import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.Addresses;
import com.ishoal.core.persistence.entity.AddressEntity;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;

public class AddressEntityAdapter {
	public AddressEntityAdapter() {

	}

	public AddressEntity adapt(Address address, BuyerProfileEntity buyer) {
		AddressEntity entity = new AddressEntity();
		if(address.getId()!=null)
		{
			entity.setId(address.getId());
		}
		entity.setDepartmentName(address.getDepartmentName());
		entity.setBuildingName(address.getBuildingName());
		entity.setStreetAddress(address.getStreetAddress());
		entity.setLocality(address.getLocality());
		entity.setPostTown(address.getPostTown());
		entity.setPostcode(address.getPostcode());
		entity.setBuyer(buyer);
		return entity;
	}

	public AddressEntity adapt(Address address) {

		AddressEntity entity = new AddressEntity();
		entity.setId(address.getId());
		entity.setDepartmentName(address.getDepartmentName());
		entity.setBuildingName(address.getBuildingName());
		entity.setStreetAddress(address.getStreetAddress());
		entity.setLocality(address.getLocality());
		entity.setPostTown(address.getPostTown());
		entity.setPostcode(address.getPostcode());
		return entity;
	}

	public Address adapt(AddressEntity entity) {

		if (entity == null) {
			return null;
		}
		return Address.anAddress().id(entity.getId()).departmentName(entity.getDepartmentName()).buildingName(entity.getBuildingName())
				.streetAddress(entity.getStreetAddress()).locality(entity.getLocality()).postTown(entity.getPostTown())
				.postcode(entity.getPostcode()).build();
	}

	public List<AddressEntity> adapt(Addresses adapt) {
		if (adapt == null) {
			return Collections.emptyList();
		}
		return IterableUtils.mapToList(adapt, this::adapts);
	}

	public AddressEntity adapts(Address domain) {
		if (domain == null) {
			return null;
		}
		AddressEntity entity=new AddressEntity();
		
		entity.setBuildingName(domain.getBuildingName());
		entity.setDepartmentName(domain.getDepartmentName());
		entity.setId(domain.getId());
		entity.setLocality(domain.getLocality());
		entity.setPostcode(domain.getPostcode());
		entity.setPostTown(domain.getPostTown());
		entity.setStreetAddress(domain.getStreetAddress());
		return entity;	
	}
}