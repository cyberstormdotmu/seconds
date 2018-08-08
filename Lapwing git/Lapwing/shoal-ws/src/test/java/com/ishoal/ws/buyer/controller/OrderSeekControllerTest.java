package com.ishoal.ws.buyer.controller;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.orders.NewOrderRequest;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.orders.OrderSeekService;
import com.ishoal.ws.buyer.dto.BuyerOrderDto;
import com.ishoal.ws.buyer.dto.adapter.BuyerOrderDtoAdapter;
import com.ishoal.ws.common.dto.OrderLineDto;
import com.ishoal.ws.common.dto.OrderLineShoalCreditDto;
import com.ishoal.ws.common.dto.OrderSummaryDto;
import com.ishoal.ws.common.dto.adapter.OrderSummaryDtoAdapter;
import com.ishoal.ws.session.BuyerSession;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.ishoal.core.domain.OrderTestData.orderLineWithCredit;
import static com.ishoal.core.domain.OrderTestData.orderWithNoLines;
import static com.ishoal.core.domain.OrderTestData.orderWithOneLine;
import static com.ishoal.core.domain.Organisation.anOrganisation;
import static com.ishoal.core.domain.User.aUser;
import static com.ishoal.core.domain.Vendor.aVendor;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderSeekControllerTest {
    private static final DateTime FIRST_ORDER_CREATED_DATE = DateTime.now();
    private static final DateTime SECOND_ORDER_CREATED_DATE = DateTime.now().minusDays(1);
    public static final String FIRST_ORDER_REFERENCE = "0001-0001";
    public static final String SECOND_ORDER_REFERENCE = "0001-0002";

    @Mock
    private OrderSeekService orderService;

    @Mock
    private BuyerSession buyerSession;

    private OrderSummaryDtoAdapter orderSummaryDtoAdapter = new OrderSummaryDtoAdapter();

    private BuyerOrderDtoAdapter buyerOrderDtoAdapter = new BuyerOrderDtoAdapter();

    @Captor
    private ArgumentCaptor<NewOrderRequest> orderCaptor;

    private OrderSeekController orderController;

    private User loggedInUser;
    private Vendor vendor;

    @Before
    public void before() {
        orderController = new OrderSeekController(orderService,
                orderSummaryDtoAdapter, buyerOrderDtoAdapter);

        loggedInUser = aUser()
                .id(UserId.from(3251L))
                //.organisation(Organisation.anOrganisation().name("Test Org").build())
                .build();

        vendor = aVendor().name("HP").build();

        theOrderServiceWillFindBuyerOrders();
    }

    @Test
    public void listOrdersShouldCallTheServiceWithTheLoggedInUser() {
        listOrders();
        verify(this.orderService).findBuyerOrders(eq(this.loggedInUser));
    }

    @Test
    public void listOrdersShouldReturnAListOfOrderSummaries() {
        List<OrderSummaryDto> orders = listOrders();
        assertThat(orders, hasSize(2));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectReference() {
        assertThat(listOrdersReturningFirst().getReference(), is(firstOrder().getReference().asString()));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectCreated() {
        assertThat(listOrdersReturningFirst().getCreated(), is(firstOrder().getCreated()));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectTotalCredit() {
        assertThat(listOrdersReturningFirst().getCreditTotal().getNet(), is(firstOrder().getCreditTotal().net()));
    }

    @Test
    public void listOrdersShouldReturnOrderWithCorrectStatus() {
        assertThat(listOrdersReturningFirst().getStatus(), is(firstOrder().getStatus().getDisplayName()));
    }

    @Test
    public void fetchOrderShouldCallTheServiceWithTheLoggedInUser() {
        theOrderServiceWillFindTheBuyerOrder();

        fetchOrder();
        verify(this.orderService).findBuyerOrder(eq(loggedInUser), any(OrderReference.class));
    }

    @Test
    public void fetchOrderShouldCallTheServiceWithTheDesiredOrderReference() {
        theOrderServiceWillFindTheBuyerOrder();

        fetchOrder();
        verify(this.orderService).findBuyerOrder(any(User.class), eq(firstOrder().getReference()));
    }

    @Test
    public void fetchOrderShouldReturnNotFoundStatusWhenOrderNotFound() {
        theOrderServiceWillNotFindTheBuyerOrder();

        assertThat(fetchOrder().getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void fetchOrderShouldReturnNoBodyWhenOrderNotFound() {
        theOrderServiceWillNotFindTheBuyerOrder();

        assertThat(fetchOrder().getBody(), is(nullValue()));
    }

    @Test
    public void fetchOrderShouldReturnOKWhenOrderIsFound() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrder().getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void fetchOrderShouldReturnABodyWhenOrderIsFound() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrder().getBody(), is(not(nullValue())));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectReference() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrderReturningTheOrderFound().getSummary().getReference(), is(firstOrder().getReference().asString()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectStatus() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrderReturningTheOrderFound().getSummary().getStatus(), is(firstOrder().getStatus().getDisplayName()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectPaymentStatus() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrderReturningTheOrderFound().getSummary().getPaymentStatus(), is(firstOrder().getPaymentStatus().getDisplayName()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectOrderTotal() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrderReturningTheOrderFound().getSummary().getOrderTotal().getNet(), is(firstOrder().getTotal().net()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectCreatedDate() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrderReturningTheOrderFound().getSummary().getCreated(), is(firstOrder().getCreated()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectNumberOfOrderLines() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrderReturningTheOrderFound().getLines().size(), is(1));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectProductCode() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getProductCode(), is(lineWithCredit().getProduct().getCode().toString()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectQuantity() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getQuantity(), is(lineWithCredit().getQuantity()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectInitialUnitPrice() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getInitialUnitPrice(), is(lineWithCredit().getInitialPriceBand().getBuyerPrice()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectCurrentUnitPrice() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getCurrentUnitPrice(), is(lineWithCredit().getCurrentPriceBand().getBuyerPrice()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectNetAmount() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getAmount().getNet(), is(lineWithCredit().getAmount().net()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectVatAmount() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getAmount().getVat(), is(lineWithCredit().getAmount().vat()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectVatRate() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getVatRate(), is(lineWithCredit().getVatRate()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectNumberOfCredits() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getCredits().size(), is(1));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectCreditNetAmount() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        OrderLineShoalCreditDto credit = line.getCredits().get(0);
        assertThat(credit.getAmount().getNet(), is(creditOnLine().getAmount().net()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectCreditVatAmount() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        OrderLineShoalCreditDto credit = line.getCredits().get(0);
        assertThat(credit.getAmount().getVat(), is(creditOnLine().getAmount().vat()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectCreditCreatedDate() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        OrderLineShoalCreditDto credit = line.getCredits().get(0);
        assertThat(credit.getEffectiveDate(), is(creditOnLine().getCreated()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectCreditTotal() {
        theOrderServiceWillFindTheBuyerOrder();
        assertThat(fetchOrderReturningTheOrderFound().getSummary().getCreditTotal().getNet(),
                is(firstOrder().getCreditTotal().net()));
    }

    @Test
    public void fetchOrderReturningTheOrderFoundShouldReturnOrderWithCorrectCreditAmountOnLine() {
        theOrderServiceWillFindTheBuyerOrder();
        OrderLineDto line = fetchOrderReturningTheOrderFound().getLines().get(0);
        assertThat(line.getCreditTotal().getNet(), is(lineWithCredit().getCreditTotal().net()));
    }
    
    private BuyerOrderDto fetchOrderReturningTheOrderFound() {
        return fetchOrder().getBody();
    }

    private ResponseEntity<BuyerOrderDto> fetchOrder() {
        return orderController.fetchOrder(firstOrder().getReference().asString(), loggedInUser);
    }

    private OrderSummaryDto listOrdersReturningFirst() {
        return listOrders().get(0);
    }

    private List<OrderSummaryDto> listOrders() {
        return this.orderController.listOrders(this.loggedInUser);
    }

    private Orders orders() {
        return Orders.over(firstOrder(), secondOrder());
    }

    private Order firstOrder() {
        return orderWithNoLines()
                .buyer(loggedInUser)
                .buyerOrganisation(buildAnOrganisation())
                .vendor(vendor)
                .created(FIRST_ORDER_CREATED_DATE)
                .reference(OrderReference.from(FIRST_ORDER_REFERENCE))
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.UNPAID)
                .line(lineWithCredit())
                .build();
    }

    private Organisation buildAnOrganisation() {

        return anOrganisation().name("Test Org").build();
    }

    private Order secondOrder() {
        return orderWithOneLine()
                .buyer(loggedInUser)
                .buyerOrganisation(buildAnOrganisation())
                .vendor(vendor)
                .created(SECOND_ORDER_CREATED_DATE)
                .reference(OrderReference.from(SECOND_ORDER_REFERENCE))
                .build();
    }

    private OrderLine lineWithCredit() {
        return orderLineWithCredit().build();
    }

    private BuyerCredit creditOnLine() {
        return lineWithCredit().getCredits().iterator().next();
    }

    private void theOrderServiceWillNotFindTheBuyerOrder() {
        when(orderService.findBuyerOrder(loggedInUser, firstOrder().getReference()))
                .thenReturn(PayloadResult.error(""));
    }

    private void theOrderServiceWillFindTheBuyerOrder() {
        when(orderService.findBuyerOrder(loggedInUser, firstOrder().getReference()))
                .thenReturn(PayloadResult.success(firstOrder()));
    }

    private void theOrderServiceWillFindBuyerOrders() {
        when(this.orderService.findBuyerOrders(any(User.class))).thenReturn(orders());
    }
}