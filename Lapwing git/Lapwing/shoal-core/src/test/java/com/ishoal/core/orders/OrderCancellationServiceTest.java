/*package com.ishoal.core.orders;

import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.OrderTestData.orderWithOneLine;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.repository.BuyerAppliedCreditRepository;
import com.ishoal.core.repository.OrderRepository;

@RunWith(MockitoJUnitRunner.class)
public class OrderCancellationServiceTest {
    private static final User BUYER = User.aUser().id(UserId.from(33812L)).build();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BuyerAppliedCreditRepository appliedCreditRepository;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<Orders> ordersCaptor;

    private OrderCancellationService orderService;
   
   
    @Before
    public void before() {
        when(this.orderRepository.findBy(any())).thenReturn(orderWithOneLine().buyer(BUYER).build());

        this.orderService = new OrderCancellationService(this.orderRepository, this.appliedCreditRepository);
    }

    @Test
    public void cancelOrderShouldNotifyOrderRepositoryOrderHasBeenCancelled() {
        OrderReference reference = OrderReference.create();
        theOrderRepositoryWillFindAnOrderWithReferenceAndStatus(reference, OrderStatus.PROCESSING);
        this.orderService.cancel(reference, 1L);

        verify(this.orderRepository).save(orderCaptor.capture());
        Order order = orderCaptor.getValue();
        assertThat(order.getStatus(), is(OrderStatus.CANCELLED));
    }

    @Test
    public void cancelOrderShouldFailWhenTheOrderIsAlreadyCancelled() {
        OrderReference reference = OrderReference.create();
        theOrderRepositoryWillFindAnOrderWithReferenceAndStatus(reference, OrderStatus.CANCELLED);

        Result result = this.orderService.cancel(reference, 1L);
        assertTrue(result.isError());
    }

    @Test
    public void cancelOrderShouldFailWhenTheOrderIsConfirmed() {
        OrderReference reference = OrderReference.create();
        theOrderRepositoryWillFindAnOrderWithReferenceAndStatus(reference, OrderStatus.CONFIRMED);

        Result result = this.orderService.cancel(reference, 1L);
        assertTrue(result.isError());
    }

    @Test
    public void cancelOrderShouldFailWhenTheOrderIsNotFound() {
        OrderReference reference = OrderReference.create();
        theOrderRepositoryWillNotFindTheOrder();

        Result result = this.orderService.cancel(reference, 1L);
        assertTrue(result.isError());
    }

    private void theOrderRepositoryWillFindAnOrderWithReferenceAndStatus(OrderReference reference, OrderStatus status) {
        when(this.orderRepository.findOrderValidatingVersion(eq(reference), anyLong()))
                .thenReturn(PayloadResult.success(anOrder().status(status).build()));
    }

    private void theOrderRepositoryWillNotFindTheOrder() {
        when(this.orderRepository.findOrderValidatingVersion(any(), anyLong()))
                .thenReturn(PayloadResult.error(""));
    }
}*/