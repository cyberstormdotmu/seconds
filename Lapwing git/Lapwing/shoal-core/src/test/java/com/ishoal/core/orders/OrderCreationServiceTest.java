/*package com.ishoal.core.orders;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.buyer.FetchBuyerProfileRequest;
import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.CreditType;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderId;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.PaymentMethod;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductVatRate;
import com.ishoal.core.domain.ProductVatRates;
import com.ishoal.core.domain.TaxableAmount;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.products.ProductService;
import com.ishoal.core.repository.OrderRepository;
import com.ishoal.payment.buyer.BuyerChargeResponse;
import com.ishoal.payment.buyer.BuyerPaymentService;
import com.ishoal.payment.buyer.ChargeReference;
import com.ishoal.payment.buyer.PaymentCardToken;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.ishoal.common.util.IterableUtils.stream;
import static com.ishoal.core.domain.BuyerAppliedCredit.aBuyerAppliedCredit;
import static com.ishoal.core.domain.BuyerCredit.aBuyerCredit;
import static com.ishoal.core.domain.BuyerProfile.aBuyerProfile;
import static com.ishoal.core.domain.Offer.anOffer;
import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.OrderTestData.orderWithOneLine;
import static com.ishoal.core.domain.Organisation.anOrganisation;
import static com.ishoal.core.domain.Product.aProduct;
import static com.ishoal.core.domain.ProductVatRate.aProductVatRate;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.User.aUser;
import static com.ishoal.core.domain.VatRate.aVatRate;
import static com.ishoal.core.orders.NewOrderRequest.aNewOrder;
import static com.ishoal.core.orders.NewOrderRequestOrderLine.anUnconfirmedOrderLine;
import static java.util.Comparator.comparing;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderCreationServiceTest {

    private static final ProductCode PRODUCT_CODE = ProductCode.from("HPLITE80");
    private static final long ORDER_QUANTITY = 12;
    private static final BigDecimal NET_AMOUNT = new BigDecimal("12000.00");
    private static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    private static final BigDecimal VAT_AMOUNT = new BigDecimal("2400.00");
    private static final long OFFER_ID = 52542L;
    private static final User BUYER = User.aUser().id(UserId.from(33812L)).build();
    private Order ORDER_WITH_PART_CONSUMED_CREDIT = anOrderWithPartConsumedCredit(1L);
    private Order ORDER_WITH_FULLY_CONSUMED_CREDIT = anOrderWithFullyConsumedCredit(2L);
    private Order ORDER_WITH_AVAILABLE_CREDIT = anOrderWithSpendableCredit(3L, now(), "50.00", "10.00");
    private Order ORDER_ON_OFFER_ENDING_EARLIER = anOrderWithSpendableCredit(4L, now().minusDays(2), "75.00", "15.00");

    @Mock
    private OrderRepository orderRepository;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<Orders> ordersCaptor;

    @Mock
    private ProductService productService;

    @Mock
    private ManageBuyerProfileService manageBuyerProfileService;

    @Mock
    private BuyerPaymentService buyerPaymentService;

    private VatCalculator vatCalculator = new VatCalculator();

    private OrderCreationService orderService;

    @Before
    public void before() {
        when(this.manageBuyerProfileService.fetchProfile(any(FetchBuyerProfileRequest.class))).thenReturn(
            PayloadResult.success(aBuyerProfile().organisation(anOrganisation().build()).build()));

        when(this.orderRepository.findBy(any())).thenReturn(orderWithOneLine().buyer(BUYER).build());
        when(this.orderRepository.findConfirmedOrdersFor(any())).thenReturn(Orders.over(new ArrayList<>()));
        theProductServiceWillReturnTheProduct();

        this.orderService = new OrderCreationService(this.productService, this.orderRepository,
            this.vatCalculator, this.manageBuyerProfileService, this.buyerPaymentService);
    }

    @Test
    public void createOrderPassesFullOrderToRepository() {
        create(newOrderWithOneLine());
        verify(this.orderRepository).create(any(Order.class));
    }

    @Test
    public void createdOrderHasBuyer() {
        create(newOrderWithOneLine());
        assertThat(createdOrder().getBuyer(), notNullValue());
    }

    @Test
    public void createdOrderHasAnOrganisation() {
        create(newOrderWithOneLine());
        assertThat(createdOrder().getBuyerOrganisation(), is(notNullValue()));
    }

    @Test
    public void createdOrderHasPaymentStatusUnpaid() {
        create(newOrderWithOneLine());
        assertThat(createdOrder().getPaymentStatus(), is(PaymentStatus.UNPAID));
    }

    @Test
    public void createdOrderHasCorrectNumberLines() {
        create(newOrderWithOneLine());
        assertThat(createdOrder().getLines().size(), is(1));
    }

    @Test
    public void createdOrderLineHasLookedUpProductViaProductCode() {
        create(newOrderWithOneLine());
        assertThat(createdOrderLine().getProduct().getCode(), is(PRODUCT_CODE));
    }

    @Test
    public void createdOrderLineHasInitialPriceBand() {
        create(newOrderWithOneLine());
        assertThat(createdOrderLine().getInitialPriceBand(), is(offer().getPriceBandForRequestedQuantity(ORDER_QUANTITY)));
    }

    @Test
    public void createdOrderLineHasCurrentPriceBand() {
        create(newOrderWithOneLine());
        assertThat(createdOrderLine().getCurrentPriceBand(), is(offer().getPriceBandForRequestedQuantity(ORDER_QUANTITY)));
    }

    @Test
    public void createdOrderLineHasCorrectQuantity() {
        create(newOrderWithOneLine());
        assertThat(createdOrderLine().getQuantity(), is(ORDER_QUANTITY));
    }

    @Test
    public void createdOrderLineHasCorrectNetAmount() {
        create(newOrderWithOneLine());
        assertThat(createdOrderLine().getAmount().net(), is(NET_AMOUNT));
    }

    @Test
    public void createdOrderLineHasCorrectVatAmount() {
        create(newOrderWithOneLine());
        assertThat(createdOrderLine().getAmount().vat(), is(VAT_AMOUNT));
    }

    @Test
    public void createdOrderLineHasCorrectVatRate() {
        create(newOrderWithOneLine());
        assertThat(createdOrderLine().getVatRate(), is(VAT_RATE));
    }

    @Test
    public void createdOrderLineHasCorrectOffer() {
        create(newOrderWithOneLine());
        assertThat(createdOrderLine().getOffer(), is(offer()));
    }

    @Test
    public void orderSpendingCreditHasCreditSpendRecordedOnOriginalOrder() {
        theOrderRepositoryWillReturnPaidOrdersWithCredits();
        create(newOrderWithOneLine().creditToBeApplied(new BigDecimal("20.00")));

        List<Order> updatedOrders = updatedOrders();
        Order updatedOrder = updatedOrders.get(0);
        OrderLine updatedLine = updatedOrder.getLines().iterator().next();
        BuyerCredit updatedCredit = updatedLine.getCredits().iterator().next();
        Iterator<BuyerAppliedCredit> appliedCreditIterator = updatedCredit.getAppliedCredits().iterator();

        assertCreditSpend(appliedCreditIterator.next(), "41.67", "8.33");
        assertCreditSpend(appliedCreditIterator.next(), "16.66", "3.34");
    }

    @Test
    public void orderSpendingMoreCreditThanAvailableOnSingleOriginalOrderHasCreditSpendRecordedOnAffectedOrders() {
        theOrderRepositoryWillReturnPaidOrdersWithCredits();
        create(newOrderWithOneLine().creditToBeApplied(new BigDecimal("200.00")));

        List<Order> updatedOrders = updatedOrders();

        assertTotalCreditSpent(updatedOrders.get(0), "120.00");
        assertTotalCreditSpent(updatedOrders.get(1), "40.00");
        assertTotalCreditSpent(updatedOrders.get(2), "90.00");
    }

    @Test
    public void orderSpendingMoreCreditThanIsAvailableReturnsAnErrorResult() {
        theOrderRepositoryWillReturnPaidOrdersWithCredits();
        PayloadResult<Order> result = create(newOrderWithOneLine().creditToBeApplied(new BigDecimal("1000.00")));
        assertTrue(result.isError());
    }

    @Test
    public void orderSpendingMoreCreditThanOrderGrossAmountReturnsAnErrorResult() {
        theOrderRepositoryWillReturnPaidOrdersWithCredits(anOrderWithSpendableCredit(1L, now(), "100000.00", "20000.00"));
        PayloadResult<Order> result = create(newOrderWithOneLine().creditToBeApplied(NET_AMOUNT.add(VAT_AMOUNT).add(new BigDecimal("0.01"))));
        assertTrue(result.isError());
    }

    @Test
    public void orderTakesPaymentForFullAmountWhenPayingByCard() {
        thePaymentServiceWillSuccessfullyCreateACharge();
        create(newOrderWithOneLine().paymentMethod(PaymentMethod.CARD_PAYMENT).paymentCardToken(aPaymentCardToken()));

        assertThat(createdOrder().getPayments().getTotal(), is(NET_AMOUNT.add(VAT_AMOUNT)));
    }

    @Test
    public void orderSpendingCreditTakesPaymentForRemainingAmountWhenPayingByCard() {
        theOrderRepositoryWillReturnPaidOrdersWithCredits();
        thePaymentServiceWillSuccessfullyCreateACharge();
        create(newOrderWithOneLine().creditToBeApplied(new BigDecimal("20.00")).paymentMethod(PaymentMethod.CARD_PAYMENT).paymentCardToken(aPaymentCardToken()));

        assertThat(createdOrder().getPayments().getTotal(), is(NET_AMOUNT.add(VAT_AMOUNT).subtract(new BigDecimal("20.00"))));
    }

    @Test
    public void orderMakesZeroChargeWhenPayingOnInvoice() {
        create(newOrderWithOneLine().paymentMethod(PaymentMethod.INVOICE));
        verify(buyerPaymentService, never()).createCharge(any(), any(), any());
    }

    @Test
    public void orderHasNoRecordedPaymentsWhenPayingOnInvoice() {

        PayloadResult<Order> result = create(newOrderWithOneLine().paymentMethod(PaymentMethod.INVOICE));

        assertThat(result.getPayload().getPaymentStatus(), is(PaymentStatus.UNPAID));
        assertThat(result.getPayload().getPayments().size(), is(0));
    }

    private void assertCreditSpend(BuyerAppliedCredit creditSpend, String net, String vat) {
        assertThat(creditSpend.getAmount(), is(fromNetAndVat(new BigDecimal(net), new BigDecimal(vat))));
    }

    private void assertTotalCreditSpent(Order order, String totalCreditSpent) {
        OrderLine line = order.getLines().iterator().next();
        TaxableAmount actualTotalCreditSpent = line.getCreditTotal().subtract(line.getAvailableCreditBalance());

        assertThat(actualTotalCreditSpent.gross(), is(new BigDecimal(totalCreditSpent)));
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
                .startDateTime(now().minusDays(1))
                .build();
    }

    private Offers offers() {
        return Offers.over(offer());
    }

    private Offer offer() {
        return anExistingOpenOffer().id(OfferId.from(OFFER_ID)).build();
    }

    private NewOrderRequest.Builder newOrderWithOneLine() {
        return aNewOrder().buyer(aUser().build())
                .with(anUnconfirmedOrderLine().productCode(PRODUCT_CODE).quantity(ORDER_QUANTITY).unitPrice(new BigDecimal("1000.00")).build());
    }

    private PayloadResult<Order> create(NewOrderRequest.Builder newOrder) {
        return this.orderService.create(newOrder.build());
    }

    private Order createdOrder() {
        verify(this.orderRepository).create(orderCaptor.capture());
        return orderCaptor.getValue();
    }

    private List<Order> updatedOrders() {
        verify(this.orderRepository).save(ordersCaptor.capture());
        Orders orders = ordersCaptor.getValue();

        return stream(orders).sorted(comparing((Order o) -> o.getId().asLong())).collect(Collectors.toList());
    }

    private OrderLine createdOrderLine() {
        return createdOrder().getLines().iterator().next();
    }

    private void theProductServiceWillReturnTheProduct() {
        when(productService.getProduct(PRODUCT_CODE)).thenReturn(productWithCode(PRODUCT_CODE));
    }

    private void theOrderRepositoryWillReturnPaidOrdersWithCredits() {
        theOrderRepositoryWillReturnPaidOrdersWithCredits(ORDER_WITH_AVAILABLE_CREDIT,
                ORDER_WITH_FULLY_CONSUMED_CREDIT,
                ORDER_WITH_PART_CONSUMED_CREDIT,
                ORDER_ON_OFFER_ENDING_EARLIER);
    }

    private void theOrderRepositoryWillReturnPaidOrdersWithCredits(Order... orders) {
        when(orderRepository.findBuyerPaidOrdersWithCredits(any())).thenReturn(Orders.over(orders));
    }

    private Order anOrderWithFullyConsumedCredit(long orderId) {
        return anOrder()
                .id(OrderId.from(orderId))
                .reference(OrderReference.from(Long.toString(orderId)))
                .line(anOrderLine()
                        .vatRate(new BigDecimal("20.00"))
                        .product(productWithCode(PRODUCT_CODE))
                        .offer(offerWithEndDate(now()))
                        .credit(aSpendableCredit("100.00", "20.00", "100.00", "20.00"))
                        .build())
                .build();
    }

    private Order anOrderWithPartConsumedCredit(long orderId) {
        return anOrder()
                .id(OrderId.from(orderId))
                .reference(OrderReference.from(Long.toString(orderId)))
                .line(anOrderLine()
                        .vatRate(new BigDecimal("20.00"))
                        .product(productWithCode(PRODUCT_CODE))
                        .offer(offerWithEndDate(now()))
                        .credit(aSpendableCredit("100.00", "20.00", "41.67", "8.33"))
                        .build())
                .build();
    }

    private Order anOrderWithSpendableCredit(long orderId, DateTime offerEndDate, String net, String vat) {
        return anOrder()
                .id(OrderId.from(orderId))
                .reference(OrderReference.from(Long.toString(orderId)))
                .line(anOrderLine()
                        .vatRate(new BigDecimal("20.00"))
                        .product(productWithCode(PRODUCT_CODE))
                        .offer(offerWithEndDate(offerEndDate))
                        .credit(aSpendableCredit(net, vat))
                        .build())
                .build();
    }

    private Offer offerWithEndDate(DateTime dateTime) {
        return anOffer().endDateTime(dateTime).build();
    }

    private BuyerCredit aSpendableCredit(String net, String vat, String spentNet, String spentVat) {
        BuyerAppliedCredit spentCredit = aBuyerAppliedCredit()
                .amount(fromNetAndVat(new BigDecimal(spentNet), new BigDecimal(spentVat)))
                .status(BuyerAppliedCreditStatus.CONFIRMED)
                .build();

        return aBuyerCredit()
                .spendable(true)
                .amount(fromNetAndVat(new BigDecimal(net), new BigDecimal(vat)))
                .creditType(CreditType.PRICE_MOVEMENT)
                .appliedCredit(spentCredit)
                .build();
    }

    private BuyerCredit aSpendableCredit(String net, String vat) {
        return aBuyerCredit()
                .spendable(true)
                .amount(fromNetAndVat(new BigDecimal(net), new BigDecimal(vat)))
                .creditType(CreditType.PRICE_MOVEMENT)
                .build();
    }

    private PaymentCardToken aPaymentCardToken() {
        return PaymentCardToken.from(RandomStringUtils.random(6));
    }

    private void thePaymentServiceWillSuccessfullyCreateACharge() {
        BuyerChargeResponse response = BuyerChargeResponse.aChargeResponse()
                .chargeReference(ChargeReference.from(RandomStringUtils.random(6)))
                .created(now()).build();

        when(buyerPaymentService.createCharge(any(), any(), any())).thenReturn(PayloadResult.success(response));
    }
}*/