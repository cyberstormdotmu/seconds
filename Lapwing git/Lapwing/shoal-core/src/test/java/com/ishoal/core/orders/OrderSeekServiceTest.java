package com.ishoal.core.orders;

import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.OrderTestData;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductVatRate;
import com.ishoal.core.domain.ProductVatRates;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.repository.OrderRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.OrderTestData.orderWithOneLine;
import static com.ishoal.core.domain.PriceBand.aPriceBand;
import static com.ishoal.core.domain.Product.aProduct;
import static com.ishoal.core.domain.ProductVatRate.aProductVatRate;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.VatRate.aVatRate;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderSeekServiceTest {

    private static final ProductCode PRODUCT_CODE = ProductCode.from("HPLITE80");
    private static final long ORDER_QUANTITY = 12;
    private static final BigDecimal NET_AMOUNT = new BigDecimal("12000.00");
    private static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    private static final BigDecimal VAT_AMOUNT = new BigDecimal("2400.00");
    public static final long OFFER_ID = 52542L;
    private static final User BUYER = User.aUser().id(UserId.from(33812L)).build();
    private static final User OTHER_USER = User.aUser().id(UserId.from(2299L)).build();

    @Mock
    private OrderRepository orderRepository;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<Orders> ordersCaptor;

    private OrderSeekService orderSeekService;

    /*@Before
    public void before() {
        when(this.orderRepository.findBy(any())).thenReturn(orderWithOneLine().buyer(BUYER).build());
        when(this.orderRepository.findConfirmedOrdersFor(any())).thenReturn(Orders.over(new ArrayList<>()));

        this.orderSeekService = new OrderSeekService(this.orderRepository);
    }*/

    @Test
    public void findByStatusShouldFindAndAdaptTheOrders() {
        whenOrderRepositoryReturnsOrdersWithStatus();
        Orders orders = orderSeekService.findByStatus(Collections.singleton(OrderStatus.PROCESSING));
        assertThat(orders.size(), is(2));
    }

    @Test
    public void findOrderDelegatesToTheRepository() {
        OrderReference reference = OrderReference.create();
        orderSeekService.findOrder(reference);

        verify(orderRepository).findBy(reference);
    }

    @Test
    public void findBuyerOrdersDelegatesToTheRepository() {
        orderSeekService.findBuyerOrders(BUYER);
        verify(orderRepository).findBuyerOrders(BUYER);
    }

    @Test
    public void findBuyerOrderReturnsErrorResultIfTheOrderIsNotFound() {
        OrderReference reference = OrderReference.create();
        when(this.orderRepository.findBy(reference)).thenReturn(null);

        assertTrue(orderSeekService.findBuyerOrder(BUYER, reference).isError());
    }

    @Test
    public void findBuyerOrderReturnsSuccessResultIfTheOrderIsForTheBuyer() {
        OrderReference reference = OrderReference.create();
        whenOrderRepositoryReturnsOrderWithLineForReference(reference);

        assertTrue(orderSeekService.findBuyerOrder(BUYER, reference).isSuccess());
    }

    @Test
    public void findBuyerOrderReturnsItIfTheOrderIsForTheBuyer() {
        OrderReference reference = OrderReference.create();
        whenOrderRepositoryReturnsOrderWithLineForReference(reference);

        assertThat(orderSeekService.findBuyerOrder(BUYER, reference).getPayload(), is(not(nullValue())));
    }

    @Test
    public void findBuyerOrderReturnsErrorResultIfTheOrderIsNotForTheBuyer() {
        OrderReference reference = OrderReference.create();
        whenOrderRepositoryReturnsOrderWithLineForReference(reference);

        assertTrue(orderSeekService.findBuyerOrder(OTHER_USER, reference).isError());
    }

    private void whenOrderRepositoryReturnsOrdersWithStatus() {
        Order firstOrder = orderWithLine(OrderTestData.orderLine()).build();
        Order secondOrder = orderWithLine(OrderTestData.orderLine()).build();

        when(this.orderRepository.findByStatus(Collections.singleton(OrderStatus.PROCESSING)))
                .thenReturn(Orders.over(firstOrder, secondOrder));
    }

    private void whenOrderRepositoryReturnsOrderWithLineForReference(OrderReference reference) {
        OrderLine.Builder orderLine = anOrderLine()
                .product(productWithCode(PRODUCT_CODE))
                .offer(offer())
                .currentPriceBand(aPriceBand().build())
                .amount(fromNetAndVat(NET_AMOUNT, VAT_AMOUNT))
                .vatRate(VAT_RATE)
                .quantity(ORDER_QUANTITY);

        when(this.orderRepository.findBy(reference)).thenReturn(orderWithLine(orderLine).build());
    }

    private Order.Builder orderWithLine(OrderLine.Builder orderLine) {
        return anOrder().buyer(BUYER).line(orderLine.build());
    }

    private Product productWithCode(ProductCode code) {
        return aProduct()
                .code(code)
                .stock(40000L)
                .vatRates(vatRates())
                .offers(offers())
                .build();
    }

    private ProductVatRates vatRates() {
        return ProductVatRates.over(vatRate());
    }

    private ProductVatRate vatRate() {
        return aProductVatRate()
                .vatRate(aVatRate().rate(new BigDecimal("20.00")).build())
                .startDateTime(DateTime.now().minusDays(1))
                .build();
    }

    private Offers offers() {
        return Offers.over(offer());
    }

    private Offer offer() {
        return anExistingOpenOffer().id(OfferId.from(OFFER_ID)).build();
    }

}