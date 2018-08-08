package com.ishoal.core.repository;

import com.ishoal.core.domain.PaymentGatewayCharge;
import com.ishoal.core.persistence.entity.PaymentGatewayChargesEntity;
import com.ishoal.core.persistence.repository.PaymentGatewayChargesEntityRepository;

public class PaymentGatewayChargesRepository {

	private final PaymentGatewayChargesEntityRepository paymentGatewayChargesEntityRepository;
	
	public PaymentGatewayChargesRepository(PaymentGatewayChargesEntityRepository paymentGatewayChargesEntityRepository)
	{
		this.paymentGatewayChargesEntityRepository = paymentGatewayChargesEntityRepository;
	}
	
	public PaymentGatewayCharge fetchPaymentGatewayCharges()
	{
		PaymentGatewayChargesEntity  paymentGatewayChargesEntity= paymentGatewayChargesEntityRepository.findOne(1L);
		
		return PaymentGatewayCharge.aPaymentChargesDto().name(paymentGatewayChargesEntity.getName())
		.paymentChargesPercentage(paymentGatewayChargesEntity.getPaymentChargesPercentage())
		.paymentExtraCharge(paymentGatewayChargesEntity.getPaymentExtraCharge())
		.build();
	}
}
