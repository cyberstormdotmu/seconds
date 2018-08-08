package com.ishoal.ws.buyer.dto.adapter;

import static com.ishoal.core.orders.NewOrderRequest.aNewOrder;
import static com.ishoal.core.orders.NewOrderRequestOrderLine.anUnconfirmedOrderLine;

import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.PaymentMethod;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.User;
import com.ishoal.core.orders.AppliedVendorCredit;
import com.ishoal.core.orders.NewOrderRequest;
import com.ishoal.core.orders.NewOrderRequestOrderLine;
import com.ishoal.payment.buyer.PaymentCardToken;
import com.ishoal.ws.buyer.dto.AppliedVendorCreditDto;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestDto;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestOrderLineDto;
import com.ishoal.ws.common.dto.adapter.AddressDtoAdapter;

public class PlaceOrderRequestDtoAdapter {
    private AddressDtoAdapter addressAdapter = new AddressDtoAdapter();

    public NewOrderRequest adapt(PlaceOrderRequestDto orderDto, User buyer) {
        return aNewOrder()
                .buyer(buyer)
                .with(adapt(orderDto.getLines()))
                .creditToBeApplied(orderDto.getCreditToBeApplied())
                .invoiceAddress(addressAdapter.adapt(orderDto.getInvoiceAddress()))
                .deliveryAddress(addressAdapter.adapt(orderDto.getDeliveryAddress()))
                .paymentMethod(paymentMethod(orderDto))
                .paymentCardToken(paymentCardToken(orderDto))
                .withCredits(adaptVendorCredits(orderDto.getAppliedVendorCredits()))
                .build();
    }

    private PaymentCardToken paymentCardToken(PlaceOrderRequestDto orderDto) {
        return PaymentCardToken.from(orderDto.getPaymentCardToken());
    }

    private PaymentMethod paymentMethod(PlaceOrderRequestDto orderDto) {
        return PaymentMethod.fromDisplayName(orderDto.getPaymentMethod());
    }

    private List<NewOrderRequestOrderLine> adapt(List<PlaceOrderRequestOrderLineDto> lines) {
        return IterableUtils.mapToList(lines, this::adapt);
    }

    private NewOrderRequestOrderLine adapt(PlaceOrderRequestOrderLineDto lineDto) {
        return anUnconfirmedOrderLine().productCode(ProductCode.from(lineDto.getProductCode()))
                .quantity(lineDto.getQuantity()).unitPrice(lineDto.getUnitPrice()).build();
    }
    
    private List<AppliedVendorCredit> adaptVendorCredits(List<AppliedVendorCreditDto> vendorCredits) {
        return IterableUtils.mapToList(vendorCredits, this::adapt);
    }

    private AppliedVendorCredit adapt(AppliedVendorCreditDto vendorCredit) {
    	return AppliedVendorCredit.aAppliedVendorCreditDto().vendorName(vendorCredit.getVendorName())
    	.creditsApplied(vendorCredit.getCreditsApplied())
    	.build();
    
    }
    
    
}