package com.ishoal.ws.admin.dto.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ishoal.core.domain.SupplierPaymentForAnOrder;
import com.ishoal.ws.admin.dto.AdminSupplierPaymentDto;

public class AdminSupplierPaymentDtoAdapter {

	public AdminSupplierPaymentDto adapt(SupplierPaymentForAnOrder payment) {
		return AdminSupplierPaymentDto.aAdminSupplierPaymentDto().from(payment.getFrom()).to(payment.getTo())
				.dateReceived(payment.getDateReceived()).paymentType(payment.getPaymentType())
				.amount(payment.getAmount()).userReference(payment.getUserReference())
				.recordPaymentStatus(payment.getRecordPaymentStatus()).paymentRecordDate(payment.getPaymentRecordDate())
				.build();
	}

	public List<AdminSupplierPaymentDto> adapt(List<SupplierPaymentForAnOrder> payments) {
		List<AdminSupplierPaymentDto> result = new ArrayList();

		for (SupplierPaymentForAnOrder payment : payments) {
			result.add(adapt(payment));
		}
		return result;
	}

	public SupplierPaymentForAnOrder adapt(AdminSupplierPaymentDto adminSupplierPaymentDto)
	{
		return SupplierPaymentForAnOrder.aSupplierPaymentForAnOrder().from(adminSupplierPaymentDto.getFrom())
		.to(adminSupplierPaymentDto.getTo())
		.dateReceived(adminSupplierPaymentDto.getDateReceived())
		.paymentType(adminSupplierPaymentDto.getPaymentType())
		.amount(adminSupplierPaymentDto.getAmount())
		.userReference(adminSupplierPaymentDto.getUserReference())
		.paymentRecordDate(adminSupplierPaymentDto.getPaymentRecordDate())
		.recordPaymentStatus(adminSupplierPaymentDto.getRecordPaymentStatus())
		.offerReference(adminSupplierPaymentDto.getOfferReference())
		.build();
	}
}
