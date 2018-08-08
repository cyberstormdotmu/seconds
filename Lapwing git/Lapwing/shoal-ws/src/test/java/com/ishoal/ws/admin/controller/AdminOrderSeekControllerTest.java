/*package com.ishoal.ws.admin.controller;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.orders.OrderSeekService;
import com.ishoal.ws.admin.dto.AdminOrderDto;
import com.ishoal.ws.common.dto.OrderLineDto;
import com.ishoal.ws.common.dto.OrderLineShoalCreditDto;
import com.ishoal.ws.admin.dto.AdminOrderPaymentDto;
import com.ishoal.ws.common.dto.OrderSummaryDto;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ishoal.core.domain.OrderTestData.orderLine;
import static com.ishoal.core.domain.OrderTestData.orderLineWithCredit;
import static com.ishoal.core.domain.OrderTestData.orderPayment;
import static com.ishoal.core.domain.OrderTestData.orderWithNoLines;
import static com.ishoal.core.domain.Organisation.anOrganisation;
import static com.ishoal.core.domain.User.aUser;
import static com.ishoal.core.domain.Vendor.aVendor;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminOrderSeekControllerTest {
    private static final DateTime CREATED_DATE = DateTime.now();
    private static final String BUYER_USERNAME = "buyer@purchaser.org";
    private static final String ORDER_REFERENCE = "1234-BCDE";
    private static final String SECOND_ORDER_REFERENCE = "DEF-456";
    private static final String BUYER_ORGANISATION_NAME = "BOSL Ltd";
    private static final String VENDOR_NAME = "HP";

    @Mock
    private OrderSeekService orderService;

    private AdminOrderSeekController orderController;

    @Before
    public void before() {
        this.orderController = new AdminOrderSeekController(this.orderService);
    }

    @Test
    public void listOrdersShouldReturnAListOfOrderSummaries() {
        whenListOfOrdersRequestedTheyWillBeReturned();
        List<OrderSummaryDto> orders = listOrders();
        assertThat(orders.size(), is(2));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectReference() {
        whenListOfOrdersRequestedOneWillBeReturned();
        OrderSummaryDto order = listOrders().get(0);
        assertThat(order.getReference(), is(ORDER_REFERENCE));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectStatus() {
        whenListOfOrdersRequestedOneWillBeReturned();
        OrderSummaryDto order = listOrders().get(0);
        assertThat(order.getStatus(), is("Processing"));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectBuyerOrganisation() {
        whenListOfOrdersRequestedOneWillBeReturned();
        OrderSummaryDto order = listOrders().get(0);
        assertThat(order.getBuyerOrganisationName(), is(BUYER_ORGANISATION_NAME));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectOrderNetTotal() {
        whenListOfOrdersRequestedOneWillBeReturned();
        OrderSummaryDto order = listOrders().get(0);
        assertThat(order.getOrderTotal().getNet(), is(firstOrder().getTotal().net()));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectOrderVatTotal() {
        whenListOfOrdersRequestedOneWillBeReturned();
        OrderSummaryDto order = listOrders().get(0);
        assertThat(order.getOrderTotal().getVat(), is(firstOrder().getTotal().vat()));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectOrderGrossTotal() {
        whenListOfOrdersRequestedOneWillBeReturned();
        OrderSummaryDto order = listOrders().get(0);
        assertThat(order.getOrderTotal().getGross(), is(firstOrder().getTotal().gross()));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectCreatedDate() {
        whenListOfOrdersRequestedOneWillBeReturned();
        OrderSummaryDto order = listOrders().get(0);
        assertThat(order.getCreated(), is(firstOrder().getCreated()));
    }

    @Test
    public void fetchOrderShouldReturnNotFoundWhenOrderDoesNotExist() {
        whenFindOrderItWillNotBeFound();
        ResponseEntity<AdminOrderDto> response = orderController.fetchOrder(SECOND_ORDER_REFERENCE);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void fetchOrderShouldReturnOKWhenOrderExists() {
        whenFindOrderItWillBeReturned();
        ResponseEntity<AdminOrderDto> response = orderController.fetchOrder(SECOND_ORDER_REFERENCE);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void fetchOrderShouldReturnOne() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder(), is(not(nullValue())));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectReference() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getReference(), is(secondOrder().getReference().asString()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectStatus() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getStatus(), is(secondOrder().getStatus().getDisplayName()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectPaymentStatus() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getPaymentStatus(), is(secondOrder().getPaymentStatus().getDisplayName()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectBuyerOrganisation() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getBuyerOrganisationName(), is(secondOrder().getBuyerOrganisation().getName()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectVendor() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getVendorName(), is(secondOrder().getVendor().getName()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectOrderTotal() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getOrderTotal().getNet(), is(secondOrder().getTotal().net()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreatedDate() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getCreated(), is(secondOrder().getCreated()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectNumberOfOrderLines() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getLines(), hasSize(1));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectProductCode() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getProductCode(), is(lineWithCredit().getProduct().getCode().toString()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectProductName() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getProductName(), is(lineWithCredit().getProduct().getName()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectQuantity() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getQuantity(), is(lineWithCredit().getQuantity()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectInitialUnitPrice() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getInitialUnitPrice(), is(lineWithCredit().getInitialPriceBand().getBuyerPrice()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCurrentUnitPrice() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getCurrentUnitPrice(), is(lineWithCredit().getCurrentPriceBand().getBuyerPrice()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectNetAmount() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getAmount().getNet(), is(lineWithCredit().getAmount().net()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectVatAmount() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getAmount().getVat(), is(lineWithCredit().getAmount().vat()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectVatRate() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getVatRate(), is(lineWithCredit().getVatRate()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectGrossAmount() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getAmount().getGross(), is(lineWithCredit().getAmount().gross()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectNumberOfCredits() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getCredits(), hasSize(1));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditNetAmount() {
        whenFindOrderItWillBeReturned();
        OrderLineShoalCreditDto credit = fetchOrderAndGetFirstCredit();
        assertThat(credit.getAmount().getNet(), is(creditOnLine().getAmount().net()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditVatAmount() {
        whenFindOrderItWillBeReturned();
        OrderLineShoalCreditDto credit = fetchOrderAndGetFirstCredit();
        assertThat(credit.getAmount().getVat(), is(creditOnLine().getAmount().vat()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditGrossAmount() {
        whenFindOrderItWillBeReturned();
        OrderLineShoalCreditDto credit = fetchOrderAndGetFirstCredit();
        assertThat(credit.getAmount().getGross(), is(creditOnLine().getAmount().gross()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditCreatedDate() {
        whenFindOrderItWillBeReturned();
        OrderLineShoalCreditDto credit = fetchOrderAndGetFirstCredit();
        assertThat(credit.getEffectiveDate(), is(creditOnLine().getCreated()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditNetTotal() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getCreditTotal().getNet(), is(secondOrder().getCreditTotal().net()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditVatTotal() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getCreditTotal().getVat(), is(secondOrder().getCreditTotal().vat()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditGrossTotal() {
        whenFindOrderItWillBeReturned();
        assertThat(fetchOrder().getSummary().getCreditTotal().getGross(), is(secondOrder().getCreditTotal().gross()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditNetAmountOnLine() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getCreditTotal().getNet(), is(lineWithCredit().getCreditTotal().net()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditVatAmountOnLine() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getCreditTotal().getVat(), is(lineWithCredit().getCreditTotal().vat()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectCreditGrossAmountOnLine() {
        whenFindOrderItWillBeReturned();
        OrderLineDto line = fetchOrderAndGetFirstLine();
        assertThat(line.getCreditTotal().getGross(), is(lineWithCredit().getCreditTotal().gross()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectUnpaidAmount() {
        whenFindOrderItWillBeReturned();

        AdminOrderDto order = fetchOrder();
        assertThat(order.getSummary().getUnpaidAmount(), is(secondOrder().getUnpaidAmount()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectNumberOfPayments() {
        whenFindOrderItWillBeReturned();

        AdminOrderDto order = fetchOrder();
        assertThat(order.getPayments(), hasSize(1));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectPaymentDateReceived() {
        whenFindOrderItWillBeReturned();

        AdminOrderPaymentDto payment = fetchOrderAndGetFirstPayment();
        assertThat(payment.getDateReceived(), is(payment().getDateReceived()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectPaymentType() {
        whenFindOrderItWillBeReturned();

        AdminOrderPaymentDto payment = fetchOrderAndGetFirstPayment();
        assertThat(payment.getPaymentType(), is(payment().getPaymentType().getDisplayName()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectPaymentAmount() {
        whenFindOrderItWillBeReturned();

        AdminOrderPaymentDto payment = fetchOrderAndGetFirstPayment();
        assertThat(payment.getAmount(), is(payment().getAmount()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectPaymentReference() {
        whenFindOrderItWillBeReturned();

        AdminOrderPaymentDto payment = fetchOrderAndGetFirstPayment();
        assertThat(payment.getPaymentReference(), is(payment().getPaymentReference().asString()));
    }

    @Test
    public void fetchOrderShouldReturnOrderWithCorrectPaymentUserReference() {
        whenFindOrderItWillBeReturned();

        AdminOrderPaymentDto payment = fetchOrderAndGetFirstPayment();
        assertThat(payment.getUserReference(), is(payment().getUserReference()));
    }


    private AdminOrderDto fetchOrder() {
        return orderController.fetchOrder(SECOND_ORDER_REFERENCE).getBody();
    }

    private AdminOrderPaymentDto fetchOrderAndGetFirstPayment() {
        return fetchOrder().getPayments().get(0);
    }

    private OrderLineDto fetchOrderAndGetFirstLine() {
        return fetchOrder().getLines().get(0);
    }

    private OrderLineShoalCreditDto fetchOrderAndGetFirstCredit() {
        OrderLineDto line = fetchOrderAndGetFirstLine();
        return line.getCredits().get(0);
    }

    private List<OrderSummaryDto> listOrders() {
        return orderController.listOrders(Collections.singleton(OrderStatus.PROCESSING));
    }

    private void whenListOfOrdersRequestedTheyWillBeReturned() {
        when(orderService.findByStatus(Collections.singleton(OrderStatus.PROCESSING))).thenReturn(orders(firstOrder(), secondOrder()));
    }

    private void whenListOfOrdersRequestedOneWillBeReturned() {
        when(orderService.findByStatus(Collections.singleton(OrderStatus.PROCESSING))).thenReturn(orders(firstOrder()));
    }

    private void whenFindOrderItWillBeReturned() {
        when(orderService.findOrder(OrderReference.from(SECOND_ORDER_REFERENCE)))
                .thenReturn(PayloadResult.success(secondOrder()));
    }

    private void whenFindOrderItWillNotBeFound() {
        when(orderService.findOrder(OrderReference.from(SECOND_ORDER_REFERENCE)))
                .thenReturn(PayloadResult.error(""));
    }

    private Orders orders(Order... orders) {
        return Orders.over(Arrays.asList(orders));
    }

    private Order firstOrder() {
        return orderWithNoLines()
                .buyer(user())
                .buyerOrganisation(buyerOrganisation())
                .vendor(vendor())
                .created(CREATED_DATE)
                .reference(OrderReference.from(ORDER_REFERENCE))
                        .status(OrderStatus.PROCESSING)
                        .paymentStatus(PaymentStatus.UNPAID)
                        .line(orderLine().build())
                        .build();
    }

    private Order secondOrder() {
        return orderWithNoLines()
                .buyer(user())
                .buyerOrganisation(buyerOrganisation())
                .vendor(vendor())
                .created(CREATED_DATE)
                .reference(OrderReference.from(SECOND_ORDER_REFERENCE))
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.PART_PAID)
                .payment(orderPayment())
                .line(lineWithCredit())
                .build();
    }

    private OrderPayment payment() {
        return orderPayment();
    }

    private OrderLine lineWithCredit() {
        return orderLineWithCredit().build();
    }

    private BuyerCredit creditOnLine() {
        return lineWithCredit().getCredits().iterator().next();
    }

    private User user() {
        return aUser().id(UserId.from(67L)).username(BUYER_USERNAME).build();
    }

    private Organisation buyerOrganisation() {
        return anOrganisation().name(BUYER_ORGANISATION_NAME).build();
    }

    private Vendor vendor() {
        return aVendor().name(VENDOR_NAME).build();
    }

}
*/