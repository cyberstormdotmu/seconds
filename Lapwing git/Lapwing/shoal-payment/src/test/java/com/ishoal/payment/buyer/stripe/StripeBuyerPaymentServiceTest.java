package com.ishoal.payment.buyer.stripe;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.payment.buyer.BuyerChargeResponse;
import com.ishoal.payment.buyer.BuyerPaymentService;
import com.ishoal.payment.buyer.PaymentCardToken;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.net.APIResource;
import com.stripe.net.APIResource.RequestMethod;
import com.stripe.net.RequestOptions;
import com.stripe.net.StripeResponseGetter;

@RunWith(MockitoJUnitRunner.class)
public class StripeBuyerPaymentServiceTest {

    @Mock
    private StripeResponseGetter mockStripeResponseGetter;

    private BuyerPaymentService paymentService;

    private String apiKey;

    @Before
    public void before() {
        this.apiKey = UUID.randomUUID().toString();
        APIResource.setStripeResponseGetter(this.mockStripeResponseGetter);
        this.paymentService = new StripeBuyerPaymentService(this.apiKey);
    }

    @Test
    public void shouldReturnSuccessIfStripeCallReturnsSuccessfully() {
        whenCreateChargeReturns(new Charge());
        assertThat(createCharge().isSuccess(), is(true));
    }

    @Test
    public void shouldReturnFailureIfStripeCallThrowsException() {
        whenCreateChargeThrowsException();
        assertThat(createCharge().isError(), is(true));
    }

    private PayloadResult<BuyerChargeResponse> createCharge() {
        return this.paymentService.createCharge(randomPaymentCardToken(), randomOrderReference(), randomAmount());
    }

    @SuppressWarnings("unchecked")
    private void whenCreateChargeReturns(Charge charge) {
        try {
            when(this.mockStripeResponseGetter.request(eq(RequestMethod.POST), anyString(), any(Map.class),
                    eq(Charge.class), eq(APIResource.RequestType.NORMAL), isNull(RequestOptions.class)))
                            .thenReturn(charge);
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException e) {
            throw new IllegalStateException("Won't happen here", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void whenCreateChargeThrowsException() {
        try {
            when(this.mockStripeResponseGetter.request(eq(RequestMethod.POST), anyString(), any(Map.class),
                    eq(Charge.class), eq(APIResource.RequestType.NORMAL), isNull(RequestOptions.class)))
                            .thenThrow(new APIException("error from stripe", UUID.randomUUID().toString(),
                                    Integer.valueOf(1), new IllegalStateException()));
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException e) {
            throw new IllegalStateException("Won't happen here", e);
        }
    }

    private BigDecimal randomAmount() {
        String amount = RandomUtils.nextInt(1, 10000) + "." + RandomUtils.nextInt(0, 100);
        return new BigDecimal(amount);
    }

    private OrderReference randomOrderReference() {
        return OrderReference.create();
    }

    private PaymentCardToken randomPaymentCardToken() {
        return PaymentCardToken.from(UUID.randomUUID().toString());
    }
}