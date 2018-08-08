package com.ishoal.core.persistence.adapter;

import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.Addresses;
import com.ishoal.core.persistence.entity.AddressEntity;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;

public class AddressesEntityAdapter {

	private AddressEntityAdapter addressAdapter = new AddressEntityAdapter();

    public Addresses adapt(List<AddressEntity> entities) {
       if(entities == null)
       {
    	   return Addresses.emptyAddresses();
       }
       Addresses.Builder builder = Addresses.someAddresses();
       entities.forEach(entity -> builder.address(addressAdapter.adapt(entity)));
       return builder.build();
    }

    public List<AddressEntity> adapt(Addresses addresses, BuyerProfileEntity entity) {
    	 return IterableUtils.mapToList(addresses, address -> addressAdapter.adapt(address, entity));
    }
	
}
