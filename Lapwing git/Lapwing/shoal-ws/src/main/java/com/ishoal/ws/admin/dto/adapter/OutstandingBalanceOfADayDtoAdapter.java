package com.ishoal.ws.admin.dto.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ishoal.core.domain.OutstandingBalanceOfADay;
import com.ishoal.ws.admin.dto.OutstandingBalanceOfADayDto;

public class OutstandingBalanceOfADayDtoAdapter {

	public OutstandingBalanceOfADayDto adapt(OutstandingBalanceOfADay outstandingBalanceOfADay) {
		return OutstandingBalanceOfADayDto.anOutstandingBalanceOfADayDto().date(outstandingBalanceOfADay.getDate())
				.balanceToBePaidToLapwing(outstandingBalanceOfADay.getBalanceToBePaidToLapwing())
				.balanceToBeTransferredToSupplier(outstandingBalanceOfADay.getBalanceToBeTransferredToSupplier())
				.build();
	}

	public List<OutstandingBalanceOfADayDto> adapt(List<OutstandingBalanceOfADay> outstandingBalanceOfADay) {
		List<OutstandingBalanceOfADayDto> list = new ArrayList();
		for (OutstandingBalanceOfADay dto : outstandingBalanceOfADay) {
			list.add(adapt(dto));
		}
		return list;
	}
}
