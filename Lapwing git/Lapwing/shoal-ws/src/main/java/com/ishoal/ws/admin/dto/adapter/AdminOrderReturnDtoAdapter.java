package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.core.domain.OrderReturn;
import com.ishoal.ws.admin.dto.AdminOrderReturnDto;

public class AdminOrderReturnDtoAdapter {

	
	public AdminOrderReturnDto adapt(OrderReturn orderReturn)
	{
		return AdminOrderReturnDto.anAdminOrderReturnDto().orderId(orderReturn.getOrderId().asLong())
		.orderLineId(orderReturn.getOrderLineId().asLong())
		.quantity(orderReturn.getQuantity())
		.build();
	}
}
