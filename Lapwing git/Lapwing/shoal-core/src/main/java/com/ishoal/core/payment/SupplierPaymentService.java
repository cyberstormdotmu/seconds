package com.ishoal.core.payment;

import java.util.List;

import com.ishoal.core.domain.OfferReference;
import com.ishoal.core.domain.SupplierPaymentForAnOrder;
import com.ishoal.core.repository.SupplierPaymentRepository;

public class SupplierPaymentService {

	private final SupplierPaymentRepository supplierPaymentRepository;

	public SupplierPaymentService(SupplierPaymentRepository supplierPaymentRepository) {
		this.supplierPaymentRepository = supplierPaymentRepository;
	}

	public List<SupplierPaymentForAnOrder> fetchSupplierPayments(OfferReference offerReference) {
		return supplierPaymentRepository.fetchPayments(offerReference);
	}
	
	public SupplierPaymentForAnOrder insertSupplierPayment(SupplierPaymentForAnOrder payment){
		return supplierPaymentRepository.insertSupplierPayment(payment);
	}
}
