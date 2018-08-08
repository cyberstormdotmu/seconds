package com.ishoal.payment.buyer;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class FakeBuyerPaymentService implements BuyerPaymentService {
    private static final Logger logger = LoggerFactory.getLogger(FakeBuyerPaymentService.class);


    @Override
    public PayloadResult<BuyerChargeResponse> createCharge(PaymentCardToken paymentCardToken, OrderReference orderReference, BigDecimal amount) {

        logger.debug("createCharge for token {}, orderReference {}, amount {}", paymentCardToken, orderReference, amount);

        BuyerChargeResponse response = BuyerChargeResponse.aChargeResponse()
                .chargeReference(ChargeReference.from(RandomStringUtils.randomAlphanumeric(10)))
                .created(DateTime.now())
                .build();

        return PayloadResult.success(response);
    }
}
