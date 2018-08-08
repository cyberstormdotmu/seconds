package com.ishoal.ws.buyer.controller;

import static com.ishoal.ws.buyer.dto.OrderBalancesDto.someOrderBalances;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.User;
import com.ishoal.core.orders.OrderSeekService;
import com.ishoal.ws.buyer.dto.BuyerOrderDto;
import com.ishoal.ws.buyer.dto.OrderBalancesDto;
import com.ishoal.ws.buyer.dto.adapter.BuyerOrderDtoAdapter;
import com.ishoal.ws.common.dto.OrderSummaryDto;
import com.ishoal.ws.common.dto.adapter.OrderSummaryDtoAdapter;

@RestController
@RequestMapping("/ws/orders")
public class OrderSeekController {
    private static final Logger logger = LoggerFactory.getLogger(OrderSeekController.class);

    private final OrderSeekService orderService;
    private final OrderSummaryDtoAdapter orderSummaryDtoAdapter;
    private final BuyerOrderDtoAdapter buyerOrderDtoAdapter;

    public OrderSeekController(OrderSeekService orderService,
                               OrderSummaryDtoAdapter orderSummaryDtoAdapter,
                               BuyerOrderDtoAdapter buyerOrderDtoAdapter) {
        this.orderService = orderService;
        this.orderSummaryDtoAdapter = orderSummaryDtoAdapter;
        this.buyerOrderDtoAdapter = buyerOrderDtoAdapter;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<OrderSummaryDto> listOrders(User user) {
        logger.info("List Orders for user [{}]", user);

        Orders orders = orderService.findBuyerOrders(user);
        return orderSummaryDtoAdapter.adapt(orders.orderedByDateDescending());
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "getOrderBalance")
    public OrderBalancesDto getCreditBalances(User user) {
        logger.info("Credit balances requested for user=[{}]", user);
        return someOrderBalances()
        		.moneyOwnedBalance(orderService.findCreditMoneyOwnedBalance(user))
        		.latePaymentBalance(orderService.findCreditLatePaymentBalance(user))
        		.accountPaybleBalance(orderService.findCreditAccountsPayableBalance(user))
        		.build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{reference}")
    public ResponseEntity<BuyerOrderDto> fetchOrder(@PathVariable("reference") String orderReference, User user) {
        logger.info("Fetch Order with reference [{}] for user [{}]", orderReference, user);

        PayloadResult<Order> result = orderService.findBuyerOrder(user, OrderReference.from(orderReference));
        if(result.isError()) {
            logger.warn("No order found with reference [{}] for user [{}]", orderReference, user);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(buyerOrderDtoAdapter.adapt(result.getPayload()));
    }
}