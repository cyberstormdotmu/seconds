/*package com.ishoal.ws.buyer.controller;

import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.Organisation.anOrganisation;
import static com.ishoal.core.domain.User.aUser;
import static com.ishoal.ws.buyer.dto.PlaceOrderRequestOrderLineDto.anOrderLine;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.SimpleResult;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.PaymentMethod;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.orders.NewOrderRequest;
import com.ishoal.core.orders.NewOrderRequestOrderLine;
import com.ishoal.core.orders.OrderCreationService;
import com.ishoal.core.products.CreateProductService;
import com.ishoal.core.products.ProductService;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestDto;
import com.ishoal.ws.buyer.dto.adapter.PlaceOrderRequestDtoAdapter;
import com.ishoal.ws.buyer.dto.validator.PlaceOrderRequestValidator;
import com.ishoal.ws.common.dto.AddressDto;
import com.ishoal.ws.common.dto.adapter.OrderSummaryDtoAdapter;
import com.ishoal.ws.session.BuyerSession;

@RunWith(MockitoJUnitRunner.class)
public class OrderCreationControllerTest {
    private static final String PRODUCT_CODE = "HPELITE80";
   
   
    @Mock
    private OrderCreationService orderService;

    @Mock
    private BuyerSession buyerSession;

    private PlaceOrderRequestDtoAdapter placeOrderRequestDtoAdapter = new PlaceOrderRequestDtoAdapter();

    @Mock
    private PlaceOrderRequestValidator placeOrderRequestValidator;

    private OrderSummaryDtoAdapter orderSummaryDtoAdapter = new OrderSummaryDtoAdapter();

    @Captor
    private ArgumentCaptor<NewOrderRequest> orderCaptor;

    private OrderCreationController orderController;

    private User loggedInUser;

    @Before
    public void before() {
        orderController = new OrderCreationController(orderService, placeOrderRequestDtoAdapter, placeOrderRequestValidator,
                orderSummaryDtoAdapter);

        loggedInUser = aUser()
                .id(UserId.from(3251L))
                .build();

        theOrderValidatorWillReturnSuccess();
    }

    @Test
    public void aCreatedResponseIsReturnedWhenTheOrderIsSuccessfullyPlaced() {
        theOrderServiceWillCreateAnOrder();
        ResponseEntity<?> responseEntity = place(aPlaceOrderRequest());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    public void anErrorResponseIsReturnedWhenTheOrderFailsValidation() {
        theOrderValidatorWillReturnError();
        ResponseEntity<?> responseEntity = place(aPlaceOrderRequest());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void anErrorResponseIsReturnedWhenTheOrderServiceErrors() {
        theOrderServiceWillReturnAnError();
        ResponseEntity<?> responseEntity = place(aPlaceOrderRequest());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void placingOrderShouldCreateAnUnconfirmedOrder() {
        theOrderServiceWillCreateAnOrder();
        place(aPlaceOrderRequest());
        verify(this.orderService).create(any(NewOrderRequest.class));
    }

    @Test
    public void orderShouldBePlacedWithLoggedInUserAsBuyer() {
        theOrderServiceWillCreateAnOrder();
        place(aPlaceOrderRequest());
        assertThat(orderPlaced().getBuyer(), is(this.loggedInUser));
    }

    @Test
    public void orderPlacedShouldHaveCorrectNumberLines() {
        theOrderServiceWillCreateAnOrder();
        place(aPlaceOrderRequest().line(anOrderLine()).line(anOrderLine()));
        assertThat(orderPlaced().getLines().size(), is(2));
    }

    @Test
    public void orderLinePlacedShouldHaveQuantity() {
        theOrderServiceWillCreateAnOrder();
        place(orderWithLine());
        assertThat(orderLinePlaced().getQuantity(), is(11L));
    }

    @Test
    public void orderLinePlacedShouldHaveProductCode() {
        theOrderServiceWillCreateAnOrder();
        place(orderWithLine());
        assertThat(orderLinePlaced().getProductCode(), is(ProductCode.from(PRODUCT_CODE)));
    }

    @Test
    public void shouldManageOrderWithInvoicePayment() {
        theOrderServiceWillCreateAnOrder();
        place(orderWithLine().paymentMethod(PaymentMethod.INVOICE.getDisplayName()));
        assertThat(orderPlaced().getPaymentMethod(), is(PaymentMethod.INVOICE));
    }

    private PlaceOrderRequestDto.Builder orderWithLine() {
        return aPlaceOrderRequest().line(anOrderLine().productCode(PRODUCT_CODE).quantity(11).unitPrice(new BigDecimal("930.00")).stock(400L));
    }

    private ResponseEntity<?> place(PlaceOrderRequestDto.Builder request) {
        return this.orderController.placeOrder(request.build(), buyerSession, this.loggedInUser);
    }

    private NewOrderRequest orderPlaced() {
        verify(this.orderService).create(this.orderCaptor.capture());
        return this.orderCaptor.getValue();
    }

    private NewOrderRequestOrderLine orderLinePlaced() {
        return orderPlaced().getLines().iterator().next();
    }

    private void theOrderServiceWillCreateAnOrder() {
        when(this.orderService.create(any(NewOrderRequest.class))).thenAnswer(invocation -> {

                Order order = anOrder()
                        .vendor(Vendor.aVendor().name("HP").build())
                        .buyerOrganisation(anOrganisation().name("Test Org").build())
                        .build();

                return PayloadResult.success(order);
            }
        );
    }

    private void theOrderServiceWillReturnAnError() {
        when(this.orderService.create(any(NewOrderRequest.class))).thenReturn(PayloadResult.error("Failed"));
    }

    private void theOrderValidatorWillReturnSuccess() {
        when(placeOrderRequestValidator.validate(any(), any())).thenReturn(SimpleResult.success());
    }

    private void theOrderValidatorWillReturnError() {
        when(placeOrderRequestValidator.validate(any(), any())).thenReturn(SimpleResult.error("Invalid"));
    }

    private PlaceOrderRequestDto.Builder aPlaceOrderRequest() {
        return PlaceOrderRequestDto.aPlaceOrderRequest()
                .invoiceAddress(defaultAddress())
                .deliveryAddress(defaultAddress())
                .paymentMethod(PaymentMethod.CARD_PAYMENT.getDisplayName())
                .paymentCardToken(RandomStringUtils.randomAlphanumeric(10));
    }

    private AddressDto defaultAddress() {
        return AddressDto.anAddressDto().build();
    }
}*/