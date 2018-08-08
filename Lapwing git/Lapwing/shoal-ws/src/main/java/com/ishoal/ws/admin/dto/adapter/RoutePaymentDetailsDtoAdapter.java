package com.ishoal.ws.admin.dto.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ishoal.core.domain.RoutePaymentDetails;
import com.ishoal.ws.admin.dto.RoutePaymentDetailsDto;

public class RoutePaymentDetailsDtoAdapter {

	public RoutePaymentDetailsDto adapt(RoutePaymentDetails routePaymentDetails) {
		return RoutePaymentDetailsDto.aRoutePaymentDetailsDto().unitPrice(routePaymentDetails.getUnitPrice())
				.quantitiesConfirmed(routePaymentDetails.getQuantitiesConfirmed())
				.numberOfPaymentsReceived(routePaymentDetails.getNumberOfPaymentsReceived())
				.numberOfPaymentsPending(routePaymentDetails.getNumberOfPaymentsPending())
				.numberOfPartialPayments(routePaymentDetails.getNumberOfPartialPayments())
				.discountMoniesPerOrder(routePaymentDetails.getDiscountMoniesPerOrder())
				.paidQuantities(routePaymentDetails.getPaidQuantities())
				.unpaiQuantities(routePaymentDetails.getUnpaiQuantities())
				.partpaidQuantities(routePaymentDetails.getPartpaidQuantities())
				.numberOfOrderPlaced(routePaymentDetails.getnumberOfOrderPlaced())
				.build();
	}

	public List<RoutePaymentDetailsDto> adapt(List<RoutePaymentDetails> routePaymentDetails) {
		List<RoutePaymentDetailsDto> result= new ArrayList<>();
		for(RoutePaymentDetails detail : routePaymentDetails)
		{
			result.add(adapt(detail));
		}
		return result;
	}
}
