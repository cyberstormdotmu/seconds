package com.ishoal.ws.admin.controller;

import com.ishoal.common.Result;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.orders.OrderCancellationService;
import com.ishoal.ws.exceptionhandler.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/admin/orders")
public class AdminOrderCancellationController {
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderCancellationController.class);

    private final OrderCancellationService orderService;

    public AdminOrderCancellationController(OrderCancellationService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{orderReference}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderReference") String orderReference, @RequestParam(value = "version") long version) {
        logger.info("Cancel Order for order with orderReference=[{}], version=[{}]", orderReference, version);

        Result result = this.orderService.cancel(OrderReference.from(orderReference), version);
        ResponseEntity<?> response;
        if(result.isSuccess()) {
            logger.info("Successfully cancelled order with orderReference=[{}]", orderReference);
            response = ResponseEntity.ok().build();
        } else {
            logger.warn("Order with orderReference=[{}] could not be cancelled. Error=[{}]", orderReference, result.getError());
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
        return response;
    }
}
