package com.ishoal.core.repository;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.config.ShoalCoreTestConfiguration;
import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.CreditMovementType;
import com.ishoal.core.domain.CreditType;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.PaymentReference;
import com.ishoal.core.domain.PaymentType;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.PriceBands;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.User;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static com.ishoal.core.domain.BuyerAppliedCredit.aBuyerAppliedCredit;
import static com.ishoal.core.domain.BuyerCredit.aBuyerCredit;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.OrderPayment.anOrderPayment;
import static com.ishoal.core.domain.OrderTestData.orderWithNoLines;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/test.properties")
@ContextConfiguration(classes = { ShoalCoreTestConfiguration.class })
@Sql("/sql/clear-down-data.sql")
@Sql("/sql/user-test-data.sql")
@Sql("/sql/buyer-profile-test-data.sql")
@Sql("/sql/categories-test-data.sql")
@Sql("/sql/vendor-test-data.sql")
@Sql("/sql/vat-test-data.sql")
@Sql("/sql/product-test-data.sql")
@Sql("/sql/offer-test-data.sql")
@Sql("/sql/order-test-data.sql")
@Transactional
public class OrderRepositoryTest {

    private static final OrderReference ORDER_REFERENCE_UNCONFIRMED = OrderReference
            .from("F38V-8HG-MBA-K40E");

    private static final ProductCode PRODUCT_CODE_LAPTOP = ProductCode.from("HPELITE840");
    private static final long ORDER_QUANTITY = 17;

    private static final String USER_TOM = "tom@bosl.co.uk";
    private static final String USER_FRED = "fred@srgc.com";
    private static final String USER_OLI = "oliver.squires@ishoal.com";
    private static final BigDecimal NET_AMOUNT = new BigDecimal("5000.00");
    private static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    private static final BigDecimal VAT_AMOUNT = new BigDecimal("1000.00");
    private static final BigDecimal CREDIT_AMOUNT = new BigDecimal("200.00");
    private static final BigDecimal CREDIT_VAT_AMOUNT = new BigDecimal("40.00");
    private static final BigDecimal CREDIT_APPLIED_AMOUNT = new BigDecimal("140.00");
    private static final BigDecimal CREDIT_APPLIED_VAT_AMOUNT = new BigDecimal("28.00");
    public static final BigDecimal PAYMENT_AMOUNT = new BigDecimal("6000.00");
    public static final DateTime PAYMENT_DATE = DateTime.now();
    public static final String PAYMENT_USER_REFERENCE = "Full payment";
    public static final PaymentReference PAYMENT_REFERENCE = PaymentReference.from("ANISFDTH");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerProfileRepository profileRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void findOrderValidatingVersionReturnsTheOrderWhenFound() {
        PayloadResult<Order> result = orderRepository.findOrderValidatingVersion(ORDER_REFERENCE_UNCONFIRMED, 0);
        assertThat(result.getPayload(), is(notNullValue()));
    }

    @Test
    public void findOrderValidatingVersionReturnsErrorResultWhenWrongVersion() {
        PayloadResult<Order> result = orderRepository.findOrderValidatingVersion(ORDER_REFERENCE_UNCONFIRMED, 1);
        assertFalse(result.isSuccess());
    }

    @Test
    public void findOrderValidatingVersionReturnsErrorResultWhenOrderNotFound() {
        PayloadResult<Order> result = orderRepository.findOrderValidatingVersion(OrderReference.create(), 0);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldBeAbleToFindNewlyCreatedOrderByReference() {
        assertThat(createAndReturnPersistedOrder(validOrder()), is(notNullValue()));
    }

    @Test
    public void persistedOrderShouldHaveCorrectBuyer() {
        assertThat(createAndReturnPersistedOrder(validOrder()).getBuyer().getUsername(), is(USER_TOM));
    }

    @Test
    public void persistedOrderShouldHaveCorrectStatus() {
        assertThat(createAndReturnPersistedOrder(validOrder().status(OrderStatus.CONFIRMED)).getStatus(),
                is(OrderStatus.CONFIRMED));
    }

    @Test
    public void anOrderCanBeSaved() {
        Order order = createAndReturnPersistedOrder(validOrder());
        order.cancel();

        orderRepository.save(order);
        Order savedOrder = persistedOrder(order.getReference());

        assertThat(savedOrder.getStatus(), is(OrderStatus.CANCELLED));
    }

    @Test
    public void multipleOrdersCanBeSaved() {
        Order firstOrder = createAndReturnPersistedOrder(validOrder());
        firstOrder.cancel();

        Order secondOrder = createAndReturnPersistedOrder(validOrder());
        secondOrder.cancel();

        orderRepository.save(Orders.over(firstOrder, secondOrder));

        Order savedOrder = persistedOrder(firstOrder.getReference());
        assertThat(savedOrder.getStatus(), is(OrderStatus.CANCELLED));
    }

    @Test
    public void persistedOrderShouldHaveCreatedDate() {
        assertThat(createAndReturnPersistedOrder(validOrder()).getCreated(), is(notNullValue()));
    }

    @Test
    public void persistedOrderShouldHaveModifiedDate() {
        assertThat(createAndReturnPersistedOrder(validOrder()).getModified(), is(notNullValue()));
    }

    @Test
    public void persistedOrderShouldHaveCorrectNumberOfLines() {
        assertThat(createAndReturnPersistedOrder(validOrder()).getLines().size(), is(1));
    }

    @Test
    public void persistedOrderLineShouldHaveId() {
        assertThat(createAndReturnPersistedOrderLine().getId(), is(notNullValue()));
    }

    @Test
    public void persistedOrderLineShouldHaveCorrectProduct() {
        assertThat(createAndReturnPersistedOrderLine().getProduct().getCode(), is(PRODUCT_CODE_LAPTOP));
    }

    @Test
    public void persistedOrderLineShouldHaveQuantity() {
        assertThat(createAndReturnPersistedOrderLine().getQuantity(), is(ORDER_QUANTITY));
    }

    @Test
    public void persistedOrderLineShouldHaveInitialPriceBand() {
        assertThat(createAndReturnPersistedOrderLine().getInitialPriceBand(), is(equalTo(firstPriceBand())));
    }

    @Test
    public void persistedOrderLineShouldHaveCurrentPriceBand() {
        assertThat(createAndReturnPersistedOrderLine().getCurrentPriceBand(), is(equalTo(firstPriceBand())));
    }

    @Test
    public void persistedOrderLineShouldHaveNetAmount() {
        assertThat(createAndReturnPersistedOrderLine().getAmount().net(), is(NET_AMOUNT));
    }

    @Test
    public void persistedOrderLineShouldHaveVatAmount() {
        assertThat(createAndReturnPersistedOrderLine().getAmount().vat(), is(VAT_AMOUNT));
    }

    @Test
    public void persistedOrderLineShouldHaveVatRate() {
        assertThat(createAndReturnPersistedOrderLine().getVatRate(), is(VAT_RATE));
    }

    @Test
    public void findConfirmedOrdersByOfferIdShouldFindOrders() {
        assertThat(this.orderRepository.findConfirmedOrdersFor(Collections.singleton(offerId())).size(), is(3));
    }

    @Test
    public void persistedOrderWithCreditShouldHaveTheCorrectCreditAmount() {
        assertThat(createAndReturnPersistedBuyerCreditLine().getAmount().net(), is(CREDIT_AMOUNT));
    }

    @Test
    public void persistedOrderWithCreditShouldHaveTheCorrectCreditVatAmount() {
        assertThat(createAndReturnPersistedBuyerCreditLine().getAmount().vat(), is(CREDIT_VAT_AMOUNT));
    }

    @Test
    public void persistedOrderWithCreditShouldHaveTheCorrectAppliedCreditAmount() {
        assertThat(createAndReturnPersistedBuyerCreditLine().getAppliedCredits().getTotal().gross(),
            is(CREDIT_APPLIED_AMOUNT.add(CREDIT_APPLIED_VAT_AMOUNT)));
    }

    @Test
    public void persistedOrderWithPaymentShouldHaveTheCorrectPaymentReference() {
        assertThat(createAndReturnPersistedPayment().getPaymentReference(), is(PAYMENT_REFERENCE));
    }

    @Test
    public void persistedOrderWithPaymentShouldHaveTheCorrectPaymentAmount() {
        assertThat(createAndReturnPersistedPayment().getAmount(), is(PAYMENT_AMOUNT));
    }

    @Test
    public void persistedOrderWithPaymentShouldHaveTheCorrectPaymentType() {
        assertThat(createAndReturnPersistedPayment().getPaymentType(), is(PaymentType.BANK_TRANSFER));
    }

    @Test
    public void persistedOrderWithPaymentShouldHaveTheCorrectPaymentDate() {
        assertThat(createAndReturnPersistedPayment().getDateReceived(), is(PAYMENT_DATE));
    }

    @Test
    public void persistedOrderWithPaymentShouldHaveTheCorrectUserReference() {
        assertThat(createAndReturnPersistedPayment().getUserReference(), is(PAYMENT_USER_REFERENCE));
    }

    @Test
    public void persistedOrderWithPaymentShouldHaveAPaymentId() {
        assertThat(createAndReturnPersistedPayment().getId(), is(notNullValue()));
    }

    @Test
    public void persistedOrderWithPaymentShouldHaveACreatedDate() {
        assertThat(createAndReturnPersistedPayment().getCreated(), is(notNullValue()));
    }

    @Test
    public void findBuyerOrdersReturnsNoneWhenTheBuyerHasNone() {
        Orders orders = orderRepository.findBuyerOrders(userOli());
        assertThat(orders.size(), is(0));
    }

    @Test
    public void findBuyerOrdersReturnsThemWhenTheBuyerHasSome() {
        Orders orders = orderRepository.findBuyerOrders(userTom());
        assertThat(orders.size(), is(2));
    }

    @Test
    public void findBuyerPaidOrdersWithCreditsReturnsNoneWhenTheBuyerHasNone() {
        Orders orders = orderRepository.findBuyerPaidOrdersWithCredits(userTom());
        assertThat(orders.size(), is(0));
    }

    @Test
    public void findBuyerPaidOrdersWithCreditsReturnsThemWhenTheBuyerHasSome() {
        Orders orders = orderRepository.findBuyerPaidOrdersWithCredits(userFred());
        assertThat(orders.size(), is(1));
    }

    @Test
    public void findByStatusProcessingReturnsTheOrderThatIsProcessing() {
        Orders orders = orderRepository.findByStatus(singleton(OrderStatus.PROCESSING));
        assertThat(orders.size(), is(1));
    }

    @Test
    public void findByStatusConfirmedReturnsTheOrdersThatAreConfirmed() {
        Orders orders = orderRepository.findByStatus(singleton(OrderStatus.CONFIRMED));
        assertThat(orders.size(), is(3));
    }

    private BuyerCredit createAndReturnPersistedBuyerCreditLine() {
        return createAndReturnPersistedOrderLine(validOrderWithCredit()).getCredits().iterator().next();
    }

    private Order.Builder validOrder() {
        return orderWithNoLines()
                .buyer(userTom())
                .buyerOrganisation(tomsCompany())
                .vendor(laptop().getVendor())
                .line(orderLine().build());
    }

    private Order.Builder validOrderWithCredit() {
        return orderWithNoLines()
                .buyer(userTom())
                .buyerOrganisation(tomsCompany())
                .vendor(laptop().getVendor())
                .line(orderLine().credit(credit()).build());
    }

    private Order.Builder validOrderWithPayment() {
        return validOrder()
                .payment(payment());
    }

    private OrderPayment payment() {
        return anOrderPayment()
                .paymentReference(PAYMENT_REFERENCE)
                .amount(PAYMENT_AMOUNT)
                .dateReceived(PAYMENT_DATE)
                .paymentType(PaymentType.BANK_TRANSFER)
                .userReference(PAYMENT_USER_REFERENCE)
                .build();
    }

    private OrderLine.Builder orderLine() {
        return anOrderLine()
                .product(laptop())
                .offer(laptop().currentOffer())
                .quantity(ORDER_QUANTITY)
                .amount(fromNetAndVat(NET_AMOUNT, VAT_AMOUNT))
                .vatRate(VAT_RATE)
                .initialPriceBand(firstPriceBand())
                .currentPriceBand(firstPriceBand());
    }

    private BuyerCredit credit() {
        return aBuyerCredit()
                .creditType(CreditType.PRICE_MOVEMENT)
                .amount(fromNetAndVat(CREDIT_AMOUNT, CREDIT_VAT_AMOUNT))
                .appliedCredit(aBuyerAppliedCredit()
                        .amount(fromNetAndVat(CREDIT_APPLIED_AMOUNT, CREDIT_APPLIED_VAT_AMOUNT))
                        .status(BuyerAppliedCreditStatus.RESERVED)
                        .spendType(CreditMovementType.SPEND)
                        .build())
                .build();
    }

    private User userTom() {
        return this.userRepository.findByUsernameIgnoreCase(USER_TOM);
    }

    private Organisation tomsCompany() {
        return this.profileRepository.fetchBuyerProfile(userTom()).getOrganisation();
    }

    private User userFred() {
        return this.userRepository.findByUsernameIgnoreCase(USER_FRED);
    }

    private User userOli() {
        return this.userRepository.findByUsernameIgnoreCase(USER_OLI);
    }

    private Product laptop() {
        return this.productRepository.findByCode(PRODUCT_CODE_LAPTOP);
    }

    private OfferId offerId() {
        return laptop().currentOffer().getId();
    }

    private PriceBand firstPriceBand() {
        return priceBands().get(0);
    }

    private PriceBands priceBands() {
        return laptop().currentOffer().getPriceBands();
    }

    private OrderLine createAndReturnPersistedOrderLine() {
        return createAndReturnPersistedOrderLine(validOrder());
    }

    private OrderLine createAndReturnPersistedOrderLine(Order.Builder builder) {
        return createAndReturnPersistedOrder(builder).getLines().iterator().next();
    }

    private OrderPayment createAndReturnPersistedPayment() {
        return createAndReturnPersistedOrder(validOrderWithPayment()).getPayments().iterator().next();
    }

    private Order createAndReturnPersistedOrder(Order.Builder builder) {
        OrderReference reference = createOrder(builder).getReference();
        return persistedOrder(reference);
    }

    private Order createOrder(Order.Builder builder) {
        Order order = builder.build();
        this.orderRepository.create(order);
        return order;
    }

    private Order persistedOrder(OrderReference reference) {
        return this.orderRepository.findBy(reference);
    }
}