package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.BuyerVendorCreditEntity;
import com.ishoal.core.persistence.entity.VendorEntity;

public interface BuyerVendorCreditEntityRepository extends CrudRepository<BuyerVendorCreditEntity, Long>{

	BuyerVendorCreditEntity findByVendorAndBuyer(VendorEntity vendor, BuyerProfileEntity buyer);
	
	List<BuyerVendorCreditEntity> findByBuyer(BuyerProfileEntity buyerProfileEntity);
	
	@Query("SELECT bvc FROM BuyerVendorCredit bvc JOIN bvc.buyer b JOIN b.user u WHERE u.username = ?1")
	List<BuyerVendorCreditEntity> findByUser(String username);

	List<BuyerVendorCreditEntity> findByVendor(VendorEntity entity);

	List<BuyerVendorCreditEntity> findByBuyerAndVendor(BuyerProfileEntity buyerProfileEntity, VendorEntity vendorentity);
}