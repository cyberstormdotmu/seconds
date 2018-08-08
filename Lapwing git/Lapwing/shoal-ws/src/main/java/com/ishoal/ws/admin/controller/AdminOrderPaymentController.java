package com.ishoal.ws.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.PaymentReference;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.orders.OrderConfirmationService;
import com.ishoal.core.orders.OrderPaymentService;
import com.ishoal.ws.admin.dto.AdminOrderPaymentDto;
import com.ishoal.ws.admin.dto.adapter.AdminOrderPaymentDtoAdapter;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/admin/orders")
public class AdminOrderPaymentController {
	private static final Logger logger = LoggerFactory.getLogger(AdminOrderPaymentController.class);

	private final OrderPaymentService orderService;
	private final AdminOrderPaymentDtoAdapter paymentAdapter;
	private final OrderConfirmationService orderConfirmationService;

	public AdminOrderPaymentController(OrderPaymentService orderService,
			OrderConfirmationService orderConfirmationService) {
		this.orderService = orderService;
		this.paymentAdapter = new AdminOrderPaymentDtoAdapter();
		this.orderConfirmationService = orderConfirmationService;
	}

	@RequestMapping(method = RequestMethod.POST, value = "{orderReference}/payments")
	public ResponseEntity<?> recordPayment(@PathVariable("orderReference") String orderReference,
			@RequestParam(value = "version") long version, @RequestBody AdminOrderPaymentDto payment) {
		logger.info("Record Payment for order with orderReference=[{}], version=[{}]", orderReference, version);
		Result finalResult = null;;
		PayloadResult<Order> result = this.orderService.recordPayment(OrderReference.from(orderReference), version,
				paymentAdapter.adapt(payment));
		if (result.getPayload().getPaymentStatus().compareTo(PaymentStatus.PAID) == 0) {
			finalResult = orderConfirmationService.confirm(result.getPayload().getReference(), orderConfirmationService.getVersion(result.getPayload().getReference()));
		}
		ResponseEntity<?> response;
		if (result.isSuccess()) {
			logger.info("Successfully recorded payment on order with orderReference=[{}]", orderReference);
			response = ResponseEntity.ok().build();
		} else {
			logger.warn("Payment could not be recorded on order with orderReference=[{}]. Error=[{}]", orderReference,
					result.getError());
			response = ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "{orderReference}/payments/{paymentReference}")
	public ResponseEntity<?> deletePayment(@PathVariable("orderReference") String orderReference,
			@RequestParam(value = "version") long version, @PathVariable("paymentReference") String paymentReference) {
		logger.info("Delete Payment with paymentReference=[{}] for order with orderReference=[{}], version=[{}]",
				paymentReference, orderReference, version);

		Result result = this.orderService.deletePayment(OrderReference.from(orderReference), version,
				PaymentReference.from(paymentReference));
		ResponseEntity<?> response;
		if (result.isSuccess()) {
			logger.info("Successfully deleted payment with paymentReference=[{}] from order with orderReference=[{}]",
					paymentReference, orderReference);
			response = ResponseEntity.ok().build();
		} else {
			logger.warn(
					"Payment with paymentReference=[{}] could not be deleted from order with orderReference=[{}]. Error=[{}]",
					paymentReference, orderReference, result.getError());
			response = ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
		}
		return response;
	}
}
