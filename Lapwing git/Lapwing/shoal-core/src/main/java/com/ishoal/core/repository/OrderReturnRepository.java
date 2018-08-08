package com.ishoal.core.repository;

import java.util.List;

import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderLines;
import com.ishoal.core.domain.OrderReturn;
import com.ishoal.core.persistence.adapter.OrderReturnEntityAdapter;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import com.ishoal.core.persistence.entity.OrderReturnEntity;
import com.ishoal.core.persistence.repository.OrderEntityRepository;
import com.ishoal.core.persistence.repository.OrderLineEntityRepository;
import com.ishoal.core.persistence.repository.OrderReturnEntityRepository;

public class OrderReturnRepository {

	private final OrderReturnEntityRepository orderReturnEntityRepository;
	private final OrderEntityRepository orderEntityRepository;
	private final OrderLineEntityRepository orderLineEntityRepository;
	private final OrderReturnEntityAdapter orderReturnEntityAdapter = new OrderReturnEntityAdapter();

	public OrderReturnRepository(OrderReturnEntityRepository orderReturnEntityRepository,
			OrderEntityRepository orderEntityRepository, OrderLineEntityRepository orderLineEntityRepository) {
		this.orderReturnEntityRepository = orderReturnEntityRepository;
		this.orderEntityRepository = orderEntityRepository;
		this.orderLineEntityRepository = orderLineEntityRepository;
	}

	public OrderReturn returnOrder(OrderReturn orderReturn) {

		OrderEntity order = orderEntityRepository.findById(orderReturn.getOrderId().asLong());
		OrderLineEntity orderLine = orderLineEntityRepository.findOne(orderReturn.getOrderLineId().asLong());

		OrderReturnEntity savedEntity = orderReturnEntityRepository
				.save(orderReturnEntityAdapter.adapt(order, orderLine, orderReturn));

		return orderReturnEntityAdapter.adapt(savedEntity);
	}

	public Order calculeteTotalReturnedQuantitiesForAnOrder(Order order) {
		OrderLines lines = order.getLines();

		OrderEntity orderEntity = orderEntityRepository.findById(order.getId().asLong());
		List<OrderReturnEntity> orderReturns = orderReturnEntityRepository.findByOrder(orderEntity);

		for (OrderReturnEntity entity : orderReturns) {
			for (OrderLine line : lines) {
				if (entity.getOrderLine().getId().equals(line.getId().asLong())) {
					line.updateReturnedQuantites(line.getReturnedQuantites() + entity.getQuantity());
				}
			}
		}
		order.updateOrderLines(lines);
		return order;
	}

}
