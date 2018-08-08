/*package com.ishoal.core.orders;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductVatRate;
import com.ishoal.core.domain.ProductVatRates;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.products.ProductService;
import com.ishoal.core.repository.BuyerAppliedCreditRepository;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.PriceBand.aPriceBand;
import static com.ishoal.core.domain.Product.aProduct;
import static com.ishoal.core.domain.ProductVatRate.aProductVatRate;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.VatRate.aVatRate;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderConfirmationServiceTest {

    private static final ProductCode PRODUCT_CODE = ProductCode.from("HPLITE80");
    private static final long ORDER_QUANTITY = 12;
    private static final long OTHER_OFFER_ORDER_QUANTITY = 3;
    private static final BigDecimal NET_AMOUNT = new BigDecimal("12000.00");
    private static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    private static final BigDecimal VAT_AMOUNT = new BigDecimal("2400.00");
    public static final OfferId OFFER_ID = OfferId.from(52542L);
    public static final OfferId OTHER_OFFER_ID = OfferId.from(65633L);
    private static final User BUYER = User.aUser().id(UserId.from(33812L)).build();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BuyerAppliedCreditRepository appliedCreditRepository;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<Orders> ordersCaptor;

    @Mock
    private ProductService productService;

    @Mock
    private PriceMovementProcessor priceMovementProcessor;

    private OrderConfirmationService orderService;

    @Before
    public void before() {
        theOrderRepositoryWillReturnNoConfirmedOrdersForTheOffer();
        theProductServiceWillReturnTheProduct();

        this.orderService = new OrderConfirmationService(this.productService, this.orderRepository, this.priceMovementProcessor, this.appliedCreditRepository);
    }

    @Test
    public void confirmOrderShouldReturnAnErrorResultIfTheOrderCannotBeFound() {
        theOrderRepositoryWillNotFindTheOrder();
        assertFalse(confirmOrder(OrderReference.create()).isSuccess());
    }

    @Test
    public void confirmOrderShouldReturnASuccessResultWhenSuccessful() {
        Order order = orderWithOneLine(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);
        assertTrue(confirmOrder(order.getReference()).isSuccess());
    }

    @Test
    public void confirmOrderShouldNotifyOrderRepositoryOrderHasBeenConfirmed() {
        Order order = orderWithOneLine(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);

        confirmOrder(order.getReference());

        verify(this.orderRepository).save(ordersCaptor.capture());
        Orders savedOrders = ordersCaptor.getValue();
        Order savedOrder = savedOrders.iterator().next();
        assertThat(savedOrder.getStatus(), is(OrderStatus.CONFIRMED));
    }

    @Test
    public void confirmOrderShouldNotifyProductServiceVolumeHasIncreased() {
        Order order = orderWithOneLine(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);

        confirmOrder(order.getReference());
        verify(this.productService).increaseCurrentVolume(offer(), ORDER_QUANTITY);
    }

    @Test
    public void confirmOrderShouldNotifyProductServiceVolumeHasIncreasedOnAllOffersInTheOrder() {
        Order order = orderWithTwoLines(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);

        confirmOrder(order.getReference());
        verify(this.productService).increaseCurrentVolume(offer(), ORDER_QUANTITY);
        verify(this.productService).increaseCurrentVolume(otherOffer(), OTHER_OFFER_ORDER_QUANTITY);
    }

    @Test
    public void confirmOrderShouldFindOtherConfirmedOrdersForTheSameOffer() {
        Order order = orderWithOneLine(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);

        confirmOrder(order.getReference());

        verify(this.orderRepository).findConfirmedOrdersFor(offerIds(OFFER_ID));
    }
    
    @Test
    public void confirmOrderShouldFindOtherConfirmedOrdersForAllOffersInTheOrder() {
        Order order = orderWithTwoLines(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);

        confirmOrder(order.getReference());

        verify(this.orderRepository).findConfirmedOrdersFor(offerIds(OFFER_ID, OTHER_OFFER_ID));
    }

    @Test
    public void confirmOrderShouldPassAffectedOrdersToPriceMovementProcessor() {
        Order order = orderWithOneLine(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);

        Order confirmedOrder = orderWithOneLine(OrderStatus.CONFIRMED);
        theOrderRepositoryWillReturnConfirmedOrdersForTheOffer(offerIds(OFFER_ID), confirmedOrder);

        confirmOrder(order.getReference());

        verify(this.priceMovementProcessor).process(ordersCaptor.capture(), any(), any());
        assertOrders(ordersCaptor.getValue(), order, confirmedOrder);
    }

    @Test
    public void confirmOrderShouldSaveTheAffectedOrders() {
        Order order = orderWithOneLine(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);

        Order confirmedOrder = orderWithOneLine(OrderStatus.CONFIRMED);
        theOrderRepositoryWillReturnConfirmedOrdersForTheOffer(Collections.singleton(OFFER_ID), confirmedOrder);

        confirmOrder(order.getReference());

        verify(this.orderRepository).save(ordersCaptor.capture());
        assertOrders(ordersCaptor.getValue(), order, confirmedOrder);
    }

    @Test
    public void confirmOrderShouldOnlySaveAffectedOrdersOnceWhenMultipleOrderLines() {
        Order order = orderWithTwoLines(OrderStatus.PROCESSING);
        theOrderRepositoryWillFindTheOrder(order);

        Order confirmedOrder = orderWithTwoLines(OrderStatus.CONFIRMED);
        theOrderRepositoryWillReturnConfirmedOrdersForTheOffer(offerIds(OFFER_ID, OTHER_OFFER_ID), confirmedOrder);

        confirmOrder(order.getReference());

        verify(this.orderRepository).save(ordersCaptor.capture());
        assertOrders(ordersCaptor.getValue(), order, confirmedOrder);
    }

    private void assertOrders(Orders orders, Order... expectedOrders) {
        assertThat(orders.size(), is(expectedOrders.length));

        List<Order> orderList = orders.stream().collect(Collectors.toList());
        for(Order order : expectedOrders) {
            assertTrue(orderList.contains(order));
        }
    }

    private Result confirmOrder(OrderReference reference) {
        return this.orderService.confirm(reference, 0L);
    }

    private Order.Builder order(OrderStatus status, OrderLine.Builder... orderLines) {
        Order.Builder order = anOrder().buyer(BUYER).status(status);

        for(OrderLine.Builder orderLine : orderLines) {
            order.line(orderLine.build());
        }

        return order;
    }

    private Order orderWithOneLine(OrderStatus orderStatus) {
        return order(orderStatus, orderLine(offer(), ORDER_QUANTITY)).build();
    }

    private Order orderWithTwoLines(OrderStatus orderStatus) {
        return order(orderStatus, orderLine(offer(), ORDER_QUANTITY), orderLine(otherOffer(), OTHER_OFFER_ORDER_QUANTITY)).build();
    }

    private OrderLine.Builder orderLine(Offer offer, long orderQuantity) {
        return anOrderLine()
                    .product(productWithCode(PRODUCT_CODE))
                    .offer(offer)
                    .currentPriceBand(aPriceBand().build())
                    .amount(fromNetAndVat(NET_AMOUNT, VAT_AMOUNT))
                    .vatRate(VAT_RATE)
                    .quantity(orderQuantity);
    }

    private Product productWithCode(ProductCode code) {
        return aProduct()
                .code(code)
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
        return anExistingOpenOffer().id(OFFER_ID).build();
    }

    private Offer otherOffer() {
        return anExistingOpenOffer().id(OTHER_OFFER_ID).build();
    }

    private void theProductServiceWillReturnTheProduct() {
        when(productService.getProduct(PRODUCT_CODE)).thenReturn(productWithCode(PRODUCT_CODE));
    }

    private void theOrderRepositoryWillFindTheOrder(Order order) {
        when(this.orderRepository.findOrderValidatingVersion(eq(order.getReference()), anyLong()))
                .thenReturn(PayloadResult.success(order));
    }

    private void theOrderRepositoryWillNotFindTheOrder() {
        when(this.orderRepository.findOrderValidatingVersion(any(), anyLong()))
                .thenReturn(PayloadResult.error(""));
    }

    private void theOrderRepositoryWillReturnNoConfirmedOrdersForTheOffer() {
        when(this.orderRepository.findConfirmedOrdersFor(any())).thenReturn(Orders.over(new ArrayList<>()));
    }

    private void theOrderRepositoryWillReturnConfirmedOrdersForTheOffer(Set<OfferId> offerIds, Order order) {
        when(this.orderRepository.findConfirmedOrdersFor(offerIds)).thenReturn(Orders.over(order));
    }

    private Set<OfferId> offerIds(OfferId... offerIds) {
        return new HashSet<>(Arrays.asList(offerIds));
    }
}*/