package com.ishoal.payment.buyer.stripe;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.payment.buyer.BuyerChargeResponse;
import com.ishoal.payment.buyer.BuyerPaymentService;
import com.ishoal.payment.buyer.ChargeReference;
import com.ishoal.payment.buyer.PaymentCardToken;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.ishoal.payment.buyer.BuyerChargeResponse.aChargeResponse;

public class StripeBuyerPaymentService implements BuyerPaymentService {
    private static final Logger logger = LoggerFactory.getLogger(StripeBuyerPaymentService.class);

    public StripeBuyerPaymentService(String stripeApiKey) {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PayloadResult<BuyerChargeResponse> createCharge(PaymentCardToken paymentCardToken, OrderReference orderReference, BigDecimal amount) {

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("source", paymentCardToken.asString());
        chargeParams.put("currency", "gbp");
        chargeParams.put("amount", amount.unscaledValue().longValueExact());
        chargeParams.put("description", "Order " + orderReference);
        chargeParams.put("statement_descriptor", "Order " + orderReference);

        try {
            Charge charge = Charge.create(chargeParams);

            return PayloadResult.success(aChargeResponse()
                    .chargeReference(ChargeReference.from(charge.getId()))
                    .created(new DateTime(charge.getCreated()))
                    .build());
        } catch (StripeException e) {
            logger.warn("Stripe returned an error: {}", e.getMessage(), e);
            return PayloadResult.error(e.getMessage());
        }
    }

}
