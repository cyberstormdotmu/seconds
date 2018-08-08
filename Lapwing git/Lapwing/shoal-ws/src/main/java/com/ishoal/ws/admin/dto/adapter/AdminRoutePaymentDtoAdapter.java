package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.core.domain.AdminRoutePayment;
import com.ishoal.ws.admin.dto.AdminRoutePaymentDto;

public class AdminRoutePaymentDtoAdapter {

	private RoutePaymentDetailsDtoAdapter routePaymentDetailsDtoAdapter = new RoutePaymentDetailsDtoAdapter();

	public AdminRoutePaymentDto adapt(AdminRoutePayment adminRoutePayment) {
		return AdminRoutePaymentDto.anAdminRoutePaymentDto().paymentDetails(routePaymentDetailsDtoAdapter.adapt(adminRoutePayment.getPaymentDetails()))
				.amountReceived(adminRoutePayment.getAmountReceived())
				.amountPending(adminRoutePayment.getAmountPending())
				.lapwingCreditsUsed(adminRoutePayment.getLapwingCreditsUsed())
				.lapwingCreditsEarned(adminRoutePayment.getLapwingCreditsEarned())
				.totalLapwingMargin(adminRoutePayment.getTotalLapwingMargin())
				.discountMoniesIncVat(adminRoutePayment.getDiscountMoniesIncVat())
				.supplierBalance(adminRoutePayment.getSupplierBalance())
				.supplierKeeps(adminRoutePayment.getSupplierKeeps())
				.lapwingBalance(adminRoutePayment.getLapwingBalance())
				.lapwingKeeps(adminRoutePayment.getLapwingKeeps())
				.build();

	}

}
