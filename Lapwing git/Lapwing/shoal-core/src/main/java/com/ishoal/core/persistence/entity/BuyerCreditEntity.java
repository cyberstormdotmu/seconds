package com.ishoal.core.persistence.entity;

import com.ishoal.core.domain.CreditType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Entity(name = "BuyerCredit")
@Table(name = "BUYER_CREDITS")
@EntityListeners(AuditingEntityListener.class)
public class BuyerCreditEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_LINE_ID", referencedColumnName = "ID")
    private OrderLineEntity orderLine;

    @Column(name = "CREDIT_TYPE")
    @Enumerated(EnumType.STRING)
    private CreditType creditType;

    @Column(name = "NET_AMOUNT")
    private BigDecimal netAmount;

    @Column(name = "VAT_AMOUNT")
    private BigDecimal vatAmount;

    @Column(name = "REASON")
    private String reason;

    @CreatedDate
    @Column(name = "CREATED_DATETIME")
    private DateTime created;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "buyerCredit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuyerAppliedCreditEntity> appliedBuyerCredits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderLine(OrderLineEntity orderLine) {
        this.orderLine = orderLine;
    }

    public OrderLineEntity getOrderLine() {
        return orderLine;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }
    
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setAppliedBuyerCredits(List<BuyerAppliedCreditEntity> buyerCredits) {
        this.appliedBuyerCredits = buyerCredits;
    }

    public List<BuyerAppliedCreditEntity> getAppliedBuyerCredits() {
        return appliedBuyerCredits != null ? appliedBuyerCredits : Collections.emptyList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BuyerCreditEntity)) {
            return false;
        }

        BuyerCreditEntity that = (BuyerCreditEntity) o;

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