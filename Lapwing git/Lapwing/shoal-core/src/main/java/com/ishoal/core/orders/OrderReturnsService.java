package com.ishoal.core.orders;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLineId;
import com.ishoal.core.domain.OrderReturn;
import com.ishoal.core.repository.OrderRepository;
import com.ishoal.core.repository.OrderReturnRepository;

public class OrderReturnsService {

	private final OrderReturnRepository orderReturnRepository;
	private final OrderRepository orderRepository;

	public OrderReturnsService(OrderReturnRepository orderReturnRepository, OrderRepository orderRepository) {
		this.orderReturnRepository = orderReturnRepository;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public PayloadResult<OrderReturn> retrunOrder(String orderReference, Long orderLineId,
			long quantitiesToBeReturned) {
		Order order = orderRepository.findBy(OrderReference.from(orderReference));
		if (order != null) {
			OrderReturn orderReturn = OrderReturn.anOrderReturn().orderId(order.getId())
					.orderLineId(OrderLineId.from(orderLineId)).quantity(quantitiesToBeReturned).build();
			OrderReturn savedOrderReturn = orderReturnRepository.returnOrder(orderReturn);

			return PayloadResult.success(savedOrderReturn);
		}

		return PayloadResult.error("Bad Request");
	}
}
