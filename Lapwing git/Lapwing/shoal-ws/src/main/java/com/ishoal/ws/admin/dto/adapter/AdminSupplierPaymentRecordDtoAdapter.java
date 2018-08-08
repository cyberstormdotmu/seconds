package com.ishoal.ws.admin.dto.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ishoal.core.domain.AdminSupplierPaymentRecord;
import com.ishoal.ws.admin.dto.AdminSupplierPaymentRecordDto;

public class AdminSupplierPaymentRecordDtoAdapter {

	public AdminSupplierPaymentRecordDto adapt(AdminSupplierPaymentRecord record) {

		return AdminSupplierPaymentRecordDto.anAdminSupplierPaymentRecordDto().date(record.getDate())
				.from(record.getFrom()).to(record.getTo()).referenceNumber(record.getReferenceNumber())
				.amount(record.getAmount()).status(record.getStatus()).lapwingBalance(record.getLapwingBalance())
				.supplierBalance(record.getSupplierBalance()).build();
	}

	public List<AdminSupplierPaymentRecordDto> adapt(List<AdminSupplierPaymentRecord> records) {
		List<AdminSupplierPaymentRecordDto> list = new ArrayList();
		for (AdminSupplierPaymentRecord record : records) {
			list.add(adapt(record));
		}
		return list;
	}
}
