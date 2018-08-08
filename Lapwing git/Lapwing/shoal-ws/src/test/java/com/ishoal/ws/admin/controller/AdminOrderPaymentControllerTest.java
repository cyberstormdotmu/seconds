/*package com.ishoal.ws.admin.controller;

import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.PaymentReference;
import com.ishoal.core.domain.PaymentType;
import com.ishoal.core.orders.OrderPaymentService;
import com.ishoal.ws.admin.dto.AdminOrderPaymentDto;
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

import java.math.BigDecimal;

import static com.ishoal.core.domain.OrderTestData.PAYMENT_REFERENCE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminOrderPaymentControllerTest {
    private static final DateTime CREATED_DATE = DateTime.now();
    private static final String BUYER_USERNAME = "buyer@purchaser.org";
    private static final String ORDER_REFERENCE = "1234-BCDE";
    private static final String SECOND_ORDER_REFERENCE = "DEF-456";

    @Mock
    private OrderPaymentService orderService;

    @Captor
    private ArgumentCaptor<OrderPayment> paymentCaptor;

    private AdminOrderPaymentController orderController;

    @Before
    public void before() {
        this.orderController = new AdminOrderPaymentController(this.orderService);
    }

    @Test
    public void recordPaymentShouldCallTheOrderService() {
        whenRecordPaymentThenReturn(SimpleResult.success());

        this.orderController.recordPayment(ORDER_REFERENCE, 1L, orderPaymentDto());
        verify(this.orderService).recordPayment(eq(OrderReference.from(ORDER_REFERENCE)), eq(1L), paymentCaptor.capture());
    }

    @Test
    public void recordPaymentShouldReturnOKWhenTheCallSucceeds() {
        whenRecordPaymentThenReturn(SimpleResult.success());

        ResponseEntity<?> response = this.orderController.recordPayment(ORDER_REFERENCE, 1L, orderPaymentDto());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void recordPaymentShouldReturnBadRequestWhenTheCallErrors() {
        whenRecordPaymentThenReturn(SimpleResult.error(""));

        ResponseEntity<?> response = this.orderController.recordPayment(ORDER_REFERENCE, 1L, orderPaymentDto());
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void deletePaymentShouldCallTheOrderService() {
        whenDeletePaymentThenReturn(SimpleResult.success());

        this.orderController.deletePayment(ORDER_REFERENCE, 1L, PAYMENT_REFERENCE);
        verify(this.orderService).deletePayment(OrderReference.from(ORDER_REFERENCE), 1L, PaymentReference.from(PAYMENT_REFERENCE));
    }

    @Test
    public void deletePaymentShouldReturnOKWhenTheCallSucceeds() {
        whenDeletePaymentThenReturn(SimpleResult.success());

        ResponseEntity<?> response = this.orderController.deletePayment(ORDER_REFERENCE, 1L, PAYMENT_REFERENCE);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void deletePaymentShouldReturnBadRequestWhenTheCallErrors() {
        whenDeletePaymentThenReturn(SimpleResult.error(""));

        ResponseEntity<?> response = this.orderController.deletePayment(ORDER_REFERENCE, 1L, PAYMENT_REFERENCE);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }


    private void whenRecordPaymentThenReturn(Result result) {
        when(this.orderService.recordPayment(any(), anyLong(), any())).thenReturn(result);
    }

    private void whenDeletePaymentThenReturn(Result result) {
        when(this.orderService.deletePayment(any(), anyLong(), any())).thenReturn(result);
    }

    private AdminOrderPaymentDto orderPaymentDto() {
        return AdminOrderPaymentDto.anOrderPayment()
                .paymentType(PaymentType.BANK_TRANSFER)
                .amount(new BigDecimal("1.00"))
                .dateReceived(DateTime.now())
                .userReference("Stage payment")
                .build();
    }
}
*/