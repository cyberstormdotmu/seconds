package com.ishoal.payment.buyer;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;

import java.math.BigDecimal;

public interface BuyerPaymentService {

    PayloadResult<BuyerChargeResponse> createCharge(PaymentCardToken paymentCardToken, OrderReference orderReference, BigDecimal amount);

}
