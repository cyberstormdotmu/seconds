package com.ishoal.ws.buyer.dto;

import com.ishoal.ws.common.dto.AddressDto;
import com.ishoal.ws.common.dto.OrderLineDto;
import com.ishoal.ws.common.dto.OrderSummaryDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyerOrderDto {

    private OrderSummaryDto summary;
    private AddressDto invoiceAddress;
    private AddressDto deliveryAddress;
    private List<OrderLineDto> lines;
    private List<BuyerOrderPaymentDto> payments;

    private BuyerOrderDto(Builder builder) {
        summary = builder.summary;
        invoiceAddress = builder.invoiceAddress;
        deliveryAddress = builder.deliveryAddress;
        lines = builder.lines;
        payments = builder.payments;
    }

    public static Builder anOrder() {
        return new Builder();
    }

    public OrderSummaryDto getSummary() {
        return summary;
    }

    public AddressDto getInvoiceAddress() {
        return invoiceAddress;
    }

    public AddressDto getDeliveryAddress() {
        return deliveryAddress;
    }

    public List<OrderLineDto> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public List<BuyerOrderPaymentDto> getPayments() {
        return payments;
    }


    public static final class Builder {
        private OrderSummaryDto summary;
        private AddressDto invoiceAddress;
        private AddressDto deliveryAddress;
        private List<OrderLineDto> lines;
        private List<BuyerOrderPaymentDto> payments;

        private Builder() {
        }

        public Builder summary(OrderSummaryDto val) {
            summary = val;
            return this;
        }

        public Builder invoiceAddress(AddressDto val) {
            invoiceAddress = val;
            return this;
        }

        public Builder deliveryAddress(AddressDto val) {
            deliveryAddress = val;
            return this;
        }

        public Builder lines(List<OrderLineDto> val) {
            lines = new ArrayList<>(val);
            return this;
        }

        public Builder payments(List<BuyerOrderPaymentDto> val) {
            payments = new ArrayList<>(val);
            return this;
        }

        public BuyerOrderDto build() {
            return new BuyerOrderDto(this);
        }
    }
}
