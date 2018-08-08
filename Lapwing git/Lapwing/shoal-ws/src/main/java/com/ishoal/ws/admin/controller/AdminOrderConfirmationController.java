package com.ishoal.ws.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.Result;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.orders.OrderConfirmationService;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/admin/orders")
public class AdminOrderConfirmationController {
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderConfirmationController.class);
    private static final String API_KEY = "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";
    private final OrderConfirmationService orderService;
    
    @Value("${shoal.mail.username}")
	private String adminEmail;

    public AdminOrderConfirmationController(OrderConfirmationService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{orderReference}/confirm")
    public ResponseEntity<?> confirmOrder(@PathVariable("orderReference") String orderReference, @RequestParam(value = "version") long version) {
        logger.info("Confirm Order for order with orderReference=[{}], version=[{}]", orderReference, version);

        Result result = this.orderService.confirm(OrderReference.from(orderReference), version);
        ResponseEntity<?> response;
        if(result.isSuccess()) {
        	Order order = orderService.fetchOrder(OrderReference.from(orderReference));
        	
            logger.info("Successfully confirmed order with orderReference=[{}]", orderReference);
            response = ResponseEntity.ok().build();
        } else {
            logger.warn("Order with orderReference=[{}] could not be confirmed. Error=[{}]", orderReference, result.getError());
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
        return response;
    }
}
