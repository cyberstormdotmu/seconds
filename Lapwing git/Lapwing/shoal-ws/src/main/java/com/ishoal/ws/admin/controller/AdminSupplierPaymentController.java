package com.ishoal.ws.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.domain.SupplierPaymentForAnOrder;
import com.ishoal.core.payment.SupplierPaymentService;
import com.ishoal.ws.admin.dto.AdminSupplierPaymentDto;
import com.ishoal.ws.admin.dto.adapter.AdminSupplierPaymentDtoAdapter;

@RestController
@RequestMapping("/ws/admin/supplierPayment")
public class AdminSupplierPaymentController {
	private static final Logger logger = LoggerFactory.getLogger(AdminSupplierPaymentController.class);

	private final SupplierPaymentService supplierPaymentService;
	private final AdminSupplierPaymentDtoAdapter adminSupplierPaymentDtoAdapter = new AdminSupplierPaymentDtoAdapter();

	public AdminSupplierPaymentController(SupplierPaymentService supplierPaymentService) {
		this.supplierPaymentService = supplierPaymentService;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/recordPayment")
	public SupplierPaymentForAnOrder recordSupplierPayment(@RequestBody AdminSupplierPaymentDto supplierPayment) {
		logger.info("Record Payment for date=[{}]", supplierPayment.getPaymentRecordDate());

		SupplierPaymentForAnOrder result = supplierPaymentService
				.insertSupplierPayment(adminSupplierPaymentDtoAdapter.adapt(supplierPayment));
		return result;

	}

}
