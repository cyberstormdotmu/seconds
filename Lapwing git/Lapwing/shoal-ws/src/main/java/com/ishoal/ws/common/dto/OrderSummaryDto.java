package com.ishoal.ws.common.dto;

import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.PaymentStatus;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class OrderSummaryDto {

    private String reference;
    private String status;
    private String buyerOrganisationName;
    private String vendorName;
    private TaxableAmountDto orderTotal;
    private String paymentStatus;
    private DateTime invoiceDate;
    private DateTime dueDate;
    private BigDecimal unpaidAmount;
    private TaxableAmountDto creditTotal;
    private DateTime created;

    private OrderSummaryDto() {
        super();
    }

    private OrderSummaryDto(Builder builder) {
        this();
        reference = builder.reference;
        status = builder.status.getDisplayName();
        buyerOrganisationName = builder.buyerOrganisationName;
        vendorName = builder.vendorName;
        orderTotal = builder.orderTotal;
        paymentStatus = builder.paymentStatus.getDisplayName();
        invoiceDate = builder.invoiceDate;
        dueDate = builder.dueDate;
        unpaidAmount = builder.unpaidAmount;
        creditTotal = builder.creditTotal;
        created = builder.created;
    }

    public static Builder anOrderSummary() {
        return new Builder();
    }

    public String getReference() {
        return reference;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getBuyerOrganisationName() {
        return buyerOrganisationName;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public TaxableAmountDto getOrderTotal() {
        return orderTotal;
    }

    public DateTime getInvoiceDate() {
        return invoiceDate;
    }

    public DateTime getDueDate() {
        return dueDate;
    }

    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }

    public TaxableAmountDto getCreditTotal() {
        return creditTotal;
    }

    public DateTime getCreated() {
        return created;
    }

    public static final class Builder {
        private String reference;
        private OrderStatus status;
        private String buyerOrganisationName;
        private String vendorName;
        private TaxableAmountDto orderTotal;
        private PaymentStatus paymentStatus;
        private DateTime invoiceDate;
        private DateTime dueDate;
        private BigDecimal unpaidAmount;
        private TaxableAmountDto creditTotal;
        private DateTime created;

        private Builder() {
        }

        public Builder reference(String val) {
            reference = val;
            return this;
        }

        public Builder status(OrderStatus val) {
            status = val;
            return this;
        }

        public Builder buyerOrganisationName(String val) {
            buyerOrganisationName = val;
            return this;
        }

        public Builder vendorName(String val) {
            vendorName = val;
            return this;
        }

        public Builder orderTotal(TaxableAmountDto val) {
            orderTotal = val;
            return this;
        }

        public Builder creditTotal(TaxableAmountDto val) {
            creditTotal = val;
            return this;
        }

        public Builder created(DateTime val) {
            created = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Builder invoiceDate(DateTime val) {
            invoiceDate = val;
            return this;
        }

        public Builder dueDate(DateTime val) {
            dueDate = val;
            return this;
        }

        public Builder unpaidAmount(BigDecimal val) {
            unpaidAmount = val;
            return this;
        }

        public OrderSummaryDto build() {
            return new OrderSummaryDto(this);
        }

    }
}
