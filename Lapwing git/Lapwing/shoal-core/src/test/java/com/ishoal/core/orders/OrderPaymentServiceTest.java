/*package com.ishoal.core.orders;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.core.domain.OrderTestData;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.PaymentReference;
import com.ishoal.core.domain.PaymentStatus;
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

import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.OrderPayment.anOrderPayment;
import static com.ishoal.core.domain.OrderTestData.orderWithOneLine;
import static com.ishoal.core.domain.PriceBand.aPriceBand;
import static com.ishoal.core.domain.Product.aProduct;
import static com.ishoal.core.domain.ProductVatRate.aProductVatRate;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.VatRate.aVatRate;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderPaymentServiceTest {

    private static final ProductCode PRODUCT_CODE = ProductCode.from("HPLITE80");
    private static final long ORDER_QUANTITY = 12;
    private static final BigDecimal NET_AMOUNT = new BigDecimal("12000.00");
    private static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    private static final BigDecimal VAT_AMOUNT = new BigDecimal("2400.00");
    public static final BigDecimal GROSS_AMOUNT = NET_AMOUNT.add(VAT_AMOUNT);
    public static final long OFFER_ID = 52542L;
    private static final User BUYER = User.aUser().id(UserId.from(33812L)).build();

    @Mock
    private OrderRepository orderRepository;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<Orders> ordersCaptor;

    private OrderPaymentService orderService;

    @Before
    public void before() {
        when(this.orderRepository.findBy(any())).thenReturn(orderWithOneLine().buyer(BUYER).build());
        when(this.orderRepository.findConfirmedOrdersFor(any())).thenReturn(Orders.over(new ArrayList<>()));

        this.orderService = new OrderPaymentService(this.orderRepository);
    }

    @Test
    public void recordPaymentAllowsAPartPayment() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        theOrderWillBeFound(order);

        Result result = recordPayment(order.getReference(), paymentForAmount(BigDecimal.ONE));
        assertTrue(result.isSuccess());
    }

    @Test
    public void paymentStatusIsPartPaidWhenOnlyAPartPaymentMade() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        theOrderWillBeFound(order);

        recordPayment(order.getReference(), paymentForAmount(BigDecimal.ONE));
        assertThat(order.getPaymentStatus(), is(PaymentStatus.PART_PAID));
    }

    @Test
    public void paymentStatusIsPaidWhenFullPaymentMade() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        theOrderWillBeFound(order);

        recordPayment(order.getReference(), paymentForAmount(order.getTotal().gross()));
        assertThat(order.getPaymentStatus(), is(PaymentStatus.PAID));
    }

    @Test
    public void paymentStatusIsPaidWhenBalancingPaymentMade() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        order.addPayment(paymentForAmount(BigDecimal.ONE));
        theOrderWillBeFound(order);

        recordPayment(order.getReference(), paymentForAmount(order.getUnpaidAmount()));
        assertThat(order.getPaymentStatus(), is(PaymentStatus.PAID));
    }

    @Test
    public void recordPaymentShouldReturnSuccessIfValidPaymentForValidOrder() {
        OrderReference reference = OrderReference.create();
        whenOrderRepositoryReturnsOrderWithLineForReference(reference);

        Result result = recordPayment(reference, paymentForAmount(GROSS_AMOUNT));
        assertTrue(result.isSuccess());
    }

    @Test
    public void recordPaymentShouldReturnErrorIfPaymentGreaterThanAmountUnpaid() {
        OrderReference reference = OrderReference.create();
        whenOrderRepositoryReturnsOrderWithLineForReference(reference);

        Result result = recordPayment(reference, paymentForAmount(GROSS_AMOUNT.add(new BigDecimal("0.01"))));
        assertTrue(result.isError());
    }

    @Test
    public void recordPaymentShouldNotSaveTheOrderIfPaymentGreaterThanAmountUnpaid() {
        OrderReference reference = OrderReference.create();
        whenOrderRepositoryReturnsOrderWithLineForReference(reference);

        recordPayment(reference, paymentForAmount(GROSS_AMOUNT.add(new BigDecimal("0.01"))));

        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void recordPaymentShouldReturnErrorIfOrderIsNotFound() {
        OrderReference reference = OrderReference.create();
        theOrderWillNotBeFound(reference);

        Result result = recordPayment(reference, paymentForAmount(new BigDecimal("1.00")));
        assertTrue(result.isError());
    }

    @Test
    public void recordPaymentShouldNotSaveIfOrderIsNotFound() {
        OrderReference reference = OrderReference.create();
        theOrderWillNotBeFound(reference);

        recordPayment(reference, paymentForAmount(new BigDecimal("1.00")));

        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void deletePaymentShouldReturnErrorIfOrderIsNotFound() {
        OrderReference reference = OrderReference.create();
        theOrderWillNotBeFound(reference);

        Result result = deletePayment(reference, PaymentReference.create());
        assertTrue(result.isError());
    }

    @Test
    public void deletePaymentShouldNotSaveIfOrderIsNotFound() {
        OrderReference reference = OrderReference.create();
        theOrderWillNotBeFound(reference);

        deletePayment(reference, PaymentReference.create());

        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void deletePaymentAllowsDeletionOfAPaymentThatExists() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        OrderPayment payment = paymentForAmount(BigDecimal.ONE);
        order.addPayment(payment);

        theOrderWillBeFound(order);

        Result result = deletePayment(order.getReference(), payment.getPaymentReference());
        assertTrue(result.isSuccess());
    }

    @Test
    public void unpaidAmountIsGrossAmountWhenOnlyPaymentIsDeleted() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        OrderPayment payment = paymentForAmount(BigDecimal.ONE);
        order.addPayment(payment);

        theOrderWillBeFound(order);

        deletePayment(order.getReference(), payment.getPaymentReference());
        assertThat(order.getUnpaidAmount(), is(order.getTotal().gross()));
    }

    @Test
    public void paymentStatusIsUnpaidWhenOnlyPaymentIsDeleted() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        OrderPayment payment = paymentForAmount(BigDecimal.ONE);
        order.addPayment(payment);

        theOrderWillBeFound(order);

        deletePayment(order.getReference(), payment.getPaymentReference());
        assertThat(order.getPaymentStatus(), is(PaymentStatus.UNPAID));
    }

    @Test
    public void paymentStatusIsPartPaidWhenAPaymentIsDeletedAndAnotherRemains() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        OrderPayment payment = paymentForAmount(BigDecimal.ONE);
        order.addPayment(payment);
        order.addPayment(paymentForAmount(BigDecimal.ONE));

        theOrderWillBeFound(order);

        deletePayment(order.getReference(), payment.getPaymentReference());
        assertThat(order.getPaymentStatus(), is(PaymentStatus.PART_PAID));
    }

    @Test
    public void deletePaymentShouldReturnErrorIfPaymentDoesNotExist() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        theOrderWillBeFound(order);

        Result result = deletePayment(order.getReference(), PaymentReference.create());
        assertTrue(result.isError());
    }

    @Test
    public void deletePaymentShouldNotSaveIfPaymentDoesNotExist() {
        Order order = orderWithLine(OrderTestData.orderLine()).build();
        theOrderWillBeFound(order);

        deletePayment(order.getReference(), PaymentReference.create());

        verify(orderRepository, times(0)).save(any(Order.class));
    }

    private Result recordPayment(OrderReference reference, OrderPayment payment) {
        return this.orderService.recordPayment(reference, 0L, payment);
    }

    private Result deletePayment(OrderReference orderReference, PaymentReference paymentReference) {
        return this.orderService.deletePayment(orderReference, 0L, paymentReference);
    }

    private OrderPayment paymentForAmount(BigDecimal amount) {
        return anOrderPayment()
                .amount(amount)
                .build();
    }

    private void whenOrderRepositoryReturnsOrderWithLineForReference(OrderReference reference) {
        OrderLine.Builder orderLine = anOrderLine()
                .product(productWithCode(PRODUCT_CODE))
                .offer(offer())
                .currentPriceBand(aPriceBand().build())
                .amount(fromNetAndVat(NET_AMOUNT, VAT_AMOUNT))
                .vatRate(VAT_RATE)
                .quantity(ORDER_QUANTITY);

        when(this.orderRepository.findOrderValidatingVersion(eq(reference), anyLong()))
                .thenReturn(PayloadResult.success(orderWithLine(orderLine).build()));
    }

    private void theOrderWillNotBeFound(OrderReference reference) {
        when(this.orderRepository.findOrderValidatingVersion(eq(reference), anyLong()))
                .thenReturn(PayloadResult.error(""));
    }

    private void theOrderWillBeFound(Order order) {
        when(this.orderRepository.findOrderValidatingVersion(eq(order.getReference()), anyLong()))
                .thenReturn(PayloadResult.success(order));
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
}*/