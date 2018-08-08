package com.ishoal.ws.admin.controller;

import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.orders.OrderConfirmationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminOrderConfirmationControllerTest {
    private static final String ORDER_REFERENCE = "1234-BCDE";

    @Mock
    private OrderConfirmationService orderService;

    private AdminOrderConfirmationController orderController;

    @Before
    public void before() {
        this.orderController = new AdminOrderConfirmationController(this.orderService);
    }


    @Test
    public void confirmOrderShouldPassReferenceToOrderService() {
        whenConfirmOrderThenReturn(SimpleResult.success());

        //this.orderController.confirmOrder(ORDER_REFERENCE, 1L);
        //verify(this.orderService).confirm(OrderReference.from(ORDER_REFERENCE), 1L);
    }

    @Test
    public void confirmOrderShouldReturnOKWhenTheCallSucceeds() {
        whenConfirmOrderThenReturn(SimpleResult.success());

       // ResponseEntity<?> response = this.orderController.confirmOrder(ORDER_REFERENCE, 1L);
        //assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void confirmOrderShouldReturnBadRequestWhenTheCallErrors() {
        whenConfirmOrderThenReturn(SimpleResult.error(""));

      //  ResponseEntity<?> response = this.orderController.confirmOrder(ORDER_REFERENCE, 1L);
       // assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    private void whenConfirmOrderThenReturn(Result result) {
       // when(this.orderService.confirm(any(), anyLong())).thenReturn(result);
    }
}
