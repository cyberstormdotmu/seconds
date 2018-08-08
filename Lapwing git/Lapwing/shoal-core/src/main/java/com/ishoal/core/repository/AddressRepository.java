package com.ishoal.core.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.adapter.AddressEntityAdapter;
import com.ishoal.core.persistence.entity.AddressEntity;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.repository.AddressEntityRepository;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;

public class AddressRepository {
	private final AddressEntityRepository addressEntityRepository;
	private final AddressEntityAdapter addressEntityAdapter = new AddressEntityAdapter();
	private final BuyerProfileEntityRepository buyerProfileEntityRepository;
	private static final Logger logger = LoggerFactory.getLogger(AddressRepository.class);
	public AddressRepository(AddressEntityRepository addressEntityRepository,
			BuyerProfileEntityRepository buyerProfileEntityRepository) {
		this.addressEntityRepository = addressEntityRepository;
		this.buyerProfileEntityRepository = buyerProfileEntityRepository;
	}

	public Address saveAddress(User user, Address address) {

		BuyerProfileEntity entity = buyerProfileEntityRepository.findByUsername(user.getUsername());
		AddressEntity addressEntity = addressEntityAdapter.adapt(address, entity);
		AddressEntity updatedAddress = null;
		if (addressEntity.getId() != null){
			AddressEntity currentAddress = addressEntityRepository.findById(addressEntity.getId());
			if (currentAddress != null && exists(currentAddress)) {
					updatedAddress = addressEntityRepository.save(addressEntity);
			}
		}
		else
		{
			updatedAddress = addressEntityRepository.save(addressEntity);
		}
		return addressEntityAdapter.adapt(updatedAddress);
	}

	private boolean exists(AddressEntity entity) {
	    logger.info("boolean exissts address");
		return entity != null && entity.getId() != null;
	}
	
	public PayloadResult<Address> findAddressValidatingVersion(Long id) {
		
		Address address = findBy(id);
		if(address == null) {
		    return PayloadResult.error("Address with byuerProfile not found");
		}
		return PayloadResult.success(address);
	}
	
	public Address findBy(Long id) {
		AddressEntity addressEntity = this.addressEntityRepository.findById(id);
		if(addressEntity != null) {
			return addressEntityAdapter.adapt(addressEntity); 
		}
		return null;
	}

	public Result deleteAddress(Address address, Long id) {
		if(!removeAddress(address, id)) {
			return SimpleResult.error("Address with byuerProfile not found");
		}
		return SimpleResult.success();
	}
	
	private boolean removeAddress(Address address, Long id) {
		if(address.getId().equals(id)) {
			addressEntityRepository.delete(id);
			logger.info("query");
			return true;
		}
		logger.info("Delete Not");
		return false;
	}

}
