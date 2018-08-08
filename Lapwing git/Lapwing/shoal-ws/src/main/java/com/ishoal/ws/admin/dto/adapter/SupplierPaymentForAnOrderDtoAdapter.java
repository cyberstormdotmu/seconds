package com.ishoal.ws.admin.dto.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ishoal.core.domain.SupplierPaymentForAnOrder;
import com.ishoal.ws.admin.dto.SupplierPaymentForAnOrderDto;

public class SupplierPaymentForAnOrderDtoAdapter {

	public SupplierPaymentForAnOrderDto adapt(SupplierPaymentForAnOrder payment)
	{
		return SupplierPaymentForAnOrderDto.aSupplierPaymentForAnOrderDto()
		.from(payment.getFrom())
		.to(payment.getTo())
		.dateReceived(payment.getDateReceived())
		.paymentType(payment.getPaymentType())
		.amount(payment.getAmount())
		.userReference(payment.getUserReference())
		.recordPaymentStatus(payment.getRecordPaymentStatus())
		.paymentRecordDate(payment.getPaymentRecordDate())
		.createdDate(payment.getCreatedDate())
		.offerReference(payment.getOfferReference())
		.build();
	}
	public List<SupplierPaymentForAnOrderDto> adapt(List<SupplierPaymentForAnOrder> payments)
	{
		List<SupplierPaymentForAnOrderDto> list = new ArrayList<>();
		for (SupplierPaymentForAnOrder payment: payments) {
			list.add(adapt(payment));
		}
		return list;
	}
}
