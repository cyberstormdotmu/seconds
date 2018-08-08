package com.ishoal.core.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ishoal.core.domain.PaymentRecordType;
import com.ishoal.core.domain.PaymentType;

@Entity(name = "OrderPayment")
@Table(name = "ORDER_PAYMENTS")
@EntityListeners(AuditingEntityListener.class)
public class OrderPaymentEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private OrderEntity order;


    @Column(name = "PAYMENT_REFERENCE")
    private String paymentReference;

    @Column(name = "RECEIVED_DATE")
    private DateTime dateReceived;

    @Column(name = "PAYMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "USER_REFERENCE")
    private String userReference;

    @Column(name="payment_gateway_charges")
    private BigDecimal paymentGatewayCharges;
    
	@CreatedDate
    @Column(name = "CREATED_DATETIME")
    private DateTime created;

	@Column(name = "PAYMENT_RECORD_TYPE")
	@Enumerated(EnumType.STRING)
	private PaymentRecordType paymentRecordType;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public DateTime getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(DateTime dateReceived) {
        this.dateReceived = dateReceived;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUserReference() {
        return userReference;
    }

    public void setUserReference(String userReference) {
        this.userReference = userReference;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public BigDecimal getPaymentGatewayCharges() {
		return paymentGatewayCharges;
	}
    
    public void setPaymentGatewayCharges(BigDecimal paymentGatewayCharges) {
		this.paymentGatewayCharges = paymentGatewayCharges;
	}
    
	public PaymentRecordType getPaymentRecordType() {
		return paymentRecordType;
	}

	public void setPaymentRecordType(PaymentRecordType paymentRecordType) {
		this.paymentRecordType = paymentRecordType;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OrderPaymentEntity)) {
            return false;
        }

        OrderPaymentEntity that = (OrderPaymentEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
