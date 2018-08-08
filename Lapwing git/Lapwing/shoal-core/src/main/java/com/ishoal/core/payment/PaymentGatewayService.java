package com.ishoal.core.payment;

import com.ishoal.core.domain.PaymentGatewayCharge;
import com.ishoal.core.repository.PaymentGatewayChargesRepository;

public class PaymentGatewayService {

	private final PaymentGatewayChargesRepository paymentGatewayChargesRepository;

	public PaymentGatewayService(PaymentGatewayChargesRepository paymentGatewayChargesRepository)
	{
		this.paymentGatewayChargesRepository=paymentGatewayChargesRepository;
	}
	
	public PaymentGatewayCharge fetchPaymentCharges()
	{
		return paymentGatewayChargesRepository.fetchPaymentGatewayCharges();
	}
	
}
