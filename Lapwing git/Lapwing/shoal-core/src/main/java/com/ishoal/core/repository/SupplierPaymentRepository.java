package com.ishoal.core.repository;

import java.util.List;

import com.ishoal.core.domain.OfferReference;
import com.ishoal.core.domain.SupplierPaymentForAnOrder;
import com.ishoal.core.persistence.adapter.SupplierPaymentsEntityAdapter;
import com.ishoal.core.persistence.entity.SupplierPaymentsEntity;
import com.ishoal.core.persistence.repository.SupplierPaymentEntityRepository;

public class SupplierPaymentRepository {

	private final SupplierPaymentEntityRepository supplierPaymentEntityRepository;
	private final SupplierPaymentsEntityAdapter supplierPaymentsEntityAdapter = new SupplierPaymentsEntityAdapter(); 

	public SupplierPaymentRepository(SupplierPaymentEntityRepository supplierPaymentEntityRepository) {
		this.supplierPaymentEntityRepository = supplierPaymentEntityRepository;
	}

	public List<SupplierPaymentForAnOrder> fetchPayments(OfferReference offerReference) {
		List<SupplierPaymentsEntity> supplierPayments = supplierPaymentEntityRepository.findByOfferReference(offerReference.asString());
		return supplierPaymentsEntityAdapter.adapt(supplierPayments);
	}
	
	public SupplierPaymentForAnOrder insertSupplierPayment(SupplierPaymentForAnOrder payment)
	{
		SupplierPaymentsEntity entity = supplierPaymentsEntityAdapter.adapt(payment);
		SupplierPaymentsEntity savedEntity = supplierPaymentEntityRepository.save(entity);
		
		return supplierPaymentsEntityAdapter.adapt(savedEntity);
	}
}
