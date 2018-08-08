package com.ishoal.ws.admin.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderReturn;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.User;
import com.ishoal.core.orders.OrderReturnsService;
import com.ishoal.core.orders.OrderSeekService;
import com.ishoal.ws.admin.dto.AdminOrderDto;
import com.ishoal.ws.admin.dto.adapter.AdminOrderDtoAdapter;
import com.ishoal.ws.admin.dto.adapter.AdminOrderReturnDtoAdapter;
import com.ishoal.ws.common.dto.OrderSummaryDto;
import com.ishoal.ws.common.dto.adapter.OrderSummaryDtoAdapter;

@RestController
@RequestMapping("/ws/admin/orders")
public class AdminOrderSeekController {
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderSeekController.class);

    private final OrderSeekService orderService;
    private final OrderSummaryDtoAdapter orderSummaryAdapter;
    private final AdminOrderDtoAdapter orderAdapter;
    private final OrderReturnsService orderReturnsService;
    private final AdminOrderReturnDtoAdapter adminOrderReturnDtoAdapter=new AdminOrderReturnDtoAdapter();
    public AdminOrderSeekController(OrderSeekService orderService, OrderReturnsService orderReturnsService) {
        this.orderService = orderService;
        this.orderSummaryAdapter = new OrderSummaryDtoAdapter();
        this.orderAdapter = new AdminOrderDtoAdapter();
        this.orderReturnsService = orderReturnsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<OrderSummaryDto> listOrders(@RequestParam(value = "status", required = false) Set<OrderStatus> orderStatuses) {
        logger.info("List Orders for orders with status=[{}]", orderStatuses);

        Orders orders = orderService.findByStatus(orderStatuses);
        return orderSummaryAdapter.adapt(orders.orderedByDateDescending());
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "supplier")
    public List<OrderSummaryDto> listOrders(@RequestParam(value = "status", required = false) Set<OrderStatus> orderStatuses, User user) {
        logger.info("List Orders for orders with status=[{}]", orderStatuses);

        Orders orders = orderService.findByStatusAndSupplier(orderStatuses, user);
        return orderSummaryAdapter.adapt(orders.orderedByDateDescending());
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = "buyerOrders")
    public List<OrderSummaryDto> listOrdersByBuyer(@RequestParam(value = "status") Set<OrderStatus> orderStatuses, @RequestParam(value = "buyerId") Long id) {
        logger.info("List Orders for orders with status=[{}]", orderStatuses);
        Orders orders = orderService.findByStatusBuyerOrders(orderStatuses, id);
        return orderSummaryAdapter.adapt(orders.orderedByDateDescending());
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "buyerOrdersOfSupplier")
    public List<OrderSummaryDto> listOrdersByBuyerOfSupplier(@RequestParam(value = "status") Set<OrderStatus> orderStatuses, @RequestParam(value = "buyerId") Long id, User user) {
        logger.info("List Orders for orders with status=[{}]", orderStatuses);
        Orders orders = orderService.findByStatusBuyerOrdersOfSupplier(orderStatuses, id, user);
        return orderSummaryAdapter.adapt(orders.orderedByDateDescending());
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "{orderReference}")
    public ResponseEntity<AdminOrderDto> fetchOrder(@PathVariable("orderReference") String orderReference) {
        logger.info("Fetch Order for order with orderReference=[{}]", orderReference);

        PayloadResult<Order> result = orderService.findOrder(OrderReference.from(orderReference));
        ResponseEntity<AdminOrderDto> response;
        if(result.isSuccess()) {
            response = ResponseEntity.ok(orderAdapter.adapt(result.getPayload()));
        } else {
            logger.warn("No order found with reference [{}]", orderReference);
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }
    @RequestMapping(method = RequestMethod.GET, value = "orderRetrun")
	public ResponseEntity<?> retrunOrder(@RequestParam(value = "orderLine") Long orderLine,
			@RequestParam(value = "orderReference") String orderReference,
			@RequestParam(value = "orderRetrunQuantity") long orderRetrunQuantity) {

		PayloadResult<OrderReturn> result = orderReturnsService.retrunOrder(orderReference, orderLine, orderRetrunQuantity);
		ResponseEntity<?> response;
		if(result.isSuccess())
		{
			response = ResponseEntity.ok(adminOrderReturnDtoAdapter.adapt(result.getPayload()));
		}
		else
		{
			response = ResponseEntity.badRequest().body(result.getError());
		}
		return response;
	}
}
