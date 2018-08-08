package com.ishoal.core.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class OrderPayment {
    private final OrderPaymentId id;
    private final PaymentReference paymentReference;
    private final DateTime dateReceived;
    private final PaymentType paymentType;
    private final BigDecimal amount;
    private final String userReference;
    private final DateTime created;
    private final BigDecimal paymentGatewayCharges;
    private final PaymentRecordType paymentRecordType;

    private OrderPayment(Builder builder) {
        id = builder.id;
        paymentReference = builder.paymentReference;
        dateReceived = builder.dateReceived;
        paymentType = builder.paymentType;
        amount = builder.amount;
        userReference = builder.userReference;
        created = builder.created;
        paymentGatewayCharges = builder.paymentGatewayCharges;
        paymentRecordType = builder.paymentRecordType;
    }

    public static Builder anOrderPayment() {
        return new Builder();
    }

    public OrderPaymentId getId() {
        return id;
    }

    public PaymentReference getPaymentReference() {
        return paymentReference;
    }

    public DateTime getDateReceived() {
        return dateReceived;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUserReference() {
        return userReference;
    }

    public DateTime getCreated() {
        return created;
    }

    public BigDecimal getPaymentGatewayCharges() {
		return paymentGatewayCharges;
	}

	public PaymentRecordType getPaymentRecordType() {
		return paymentRecordType;
	}

	public static final class Builder {
        private OrderPaymentId id = OrderPaymentId.emptyOrderPaymentId;
        private PaymentReference paymentReference = PaymentReference.create();
        private DateTime dateReceived;
        private PaymentType paymentType;
        private BigDecimal amount;
        private String userReference;
        private DateTime created;
        private BigDecimal paymentGatewayCharges;
        private PaymentRecordType paymentRecordType;
        
        private Builder() {
        }

        public Builder id(OrderPaymentId val) {
            id = val;
            return this;
        }

        public Builder paymentReference(PaymentReference val) {
            paymentReference = val;
            return this;
        }

        public Builder dateReceived(DateTime val) {
            dateReceived = val;
            return this;
        }

        public Builder paymentType(PaymentType val) {
            paymentType = val;
            return this;
        }

        public Builder amount(BigDecimal val) {
            amount = val;
            return this;
        }

        public Builder userReference(String val) {
            userReference = val;
            return this;
        }

        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        public Builder paymentGatewayCharges(BigDecimal val)
        {
        	this.paymentGatewayCharges = val;
        	return this;
        }
        
        public Builder paymentRecordType(PaymentRecordType val)
        {
        	paymentRecordType=val;
        	return this;
        }
        
        public OrderPayment build() {
            return new OrderPayment(this);
        }
    }
}
