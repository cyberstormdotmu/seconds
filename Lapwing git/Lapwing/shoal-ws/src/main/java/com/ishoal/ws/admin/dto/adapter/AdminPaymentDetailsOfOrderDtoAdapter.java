package com.ishoal.ws.admin.dto.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ishoal.core.domain.AdminPaymentDetailsOfOrder;
import com.ishoal.ws.admin.dto.AdminPaymentDetailsOfOrderDto;

public class AdminPaymentDetailsOfOrderDtoAdapter {

	public AdminPaymentDetailsOfOrderDto adapt(AdminPaymentDetailsOfOrder domain)
	{
		return AdminPaymentDetailsOfOrderDto.anAdminPaymentDetailsOfOrderDto()
		.time(domain.getTime())
		.orderReference(domain.getOrderReference())
		.totalPricePaid(domain.getTotalpricePaid())
		.minimumPrice(domain.getMinimumPrice())
		.lapwingCreditsUsed(domain.getLapwingCreditsUsed())
		.cardPaymentUsed(domain.getCardPaymentUsed())
		.supplierCreditsUsed(domain.getSupplierCreditsUsed())
		.totalDiscountMonies(domain.getTotalDiscountMonies())
		.silverwingMargin(domain.getSilverwingMargin())
		.balanceToBeTransferredToSupplier(domain.getBalanceToBeTransferredToSupplier())
		.balanceToBePaidToLapwingexpected(domain.getBalanceToBePaidToLapwingexpected())
		.balanceToBePaidToLapwingactual(domain.getBalanceToBePaidToLapwingactual())
		.silverwingMarginRetainedBySilverwing(domain.getSilverwingMarginRetainedBySilverwing())
		.silverwingMarginPayableToSilverwing(domain.getSilverwingMarginPayableToSilverwing())
		.netMoneyExchangeSilverwingToSupplier(domain.getNetMoneyExchangeSilverwingToSupplier())
		.netMoneyExchangeSupplierToSilverwing(domain.getNetMoneyExchangeSupplierToSilverwing())
		.build();
	}
	public List<AdminPaymentDetailsOfOrderDto> adapt(List<AdminPaymentDetailsOfOrder> domain)
	{
		List<AdminPaymentDetailsOfOrderDto> list = new ArrayList<>();
		for(AdminPaymentDetailsOfOrder payment : domain )
		{
			list.add(adapt(payment));
		}
		return list;
	}
}
