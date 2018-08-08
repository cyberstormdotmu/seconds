package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ishoal.core.persistence.entity.SupplierPaymentsEntity;

public interface SupplierPaymentEntityRepository extends CrudRepository<SupplierPaymentsEntity, Long> {

	List<SupplierPaymentsEntity> findByOfferReference(String reference);
	
}
