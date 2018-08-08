package com.ishoal.core.persistence.adapter;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.ishoal.core.domain.SupplierPaymentForAnOrder;
import com.ishoal.core.persistence.entity.SupplierPaymentsEntity;

public class SupplierPaymentsEntityAdapter {

	public SupplierPaymentsEntity adapt(SupplierPaymentForAnOrder payment) {
		SupplierPaymentsEntity entity = new SupplierPaymentsEntity();
		if (payment.getId() != null) {
			entity.setId(payment.getId());
		}
		entity.setFrom(payment.getFrom());
		entity.setTo(payment.getTo());
		entity.setDateReceived(payment.getDateReceived());
		entity.setPaymentType(payment.getPaymentType());
		entity.setAmount(payment.getAmount());
		entity.setUserReference(payment.getUserReference());
		entity.setRecordPaymentStatus(payment.getRecordPaymentStatus());
		entity.setPaymentRecordDate(payment.getPaymentRecordDate());
		entity.setCreated(DateTime.now());
		entity.setOfferReference(payment.getOfferReference());
		
		return entity;
	}

	public SupplierPaymentForAnOrder adapt(SupplierPaymentsEntity entity) {
		return SupplierPaymentForAnOrder.aSupplierPaymentForAnOrder().from(entity.getFrom()).to(entity.getTo())
				.dateReceived(entity.getDateReceived()).paymentType(entity.getPaymentType()).amount(entity.getAmount())
				.userReference(entity.getUserReference()).recordPaymentStatus(entity.getRecordPaymentStatus())
				.paymentRecordDate(entity.getPaymentRecordDate()).createdDate(entity.getCreated()).offerReference(entity.getOfferReference()).build();
	}
	
	public List<SupplierPaymentForAnOrder> adapt(List<SupplierPaymentsEntity> entityList) {
		List<SupplierPaymentForAnOrder> list = new ArrayList<>();
		for(SupplierPaymentsEntity entity : entityList)
		{
			list.add(adapt(entity));
		}
		return list;
	}
}
