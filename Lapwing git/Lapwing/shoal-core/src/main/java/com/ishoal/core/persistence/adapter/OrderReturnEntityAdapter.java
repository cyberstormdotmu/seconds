package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.OrderId;
import com.ishoal.core.domain.OrderLineId;
import com.ishoal.core.domain.OrderReturn;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import com.ishoal.core.persistence.entity.OrderReturnEntity;

public class OrderReturnEntityAdapter {

	public OrderReturnEntity adapt(OrderEntity order, OrderLineEntity orderline, OrderReturn orderReturn) {
		OrderReturnEntity entity = new OrderReturnEntity();
		entity.setOrder(order);
		entity.setOrderLine(orderline);
		entity.setQuantity(orderReturn.getQuantity());
		return entity;
	}

	public OrderReturn adapt(OrderReturnEntity entity) {
		return OrderReturn.anOrderReturn().id(entity.getId()).orderId(OrderId.from(entity.getOrder().getId()))
				.orderLineId(OrderLineId.from(entity.getOrderLine().getId())).quantity(entity.getQuantity()).build();
	}
}
