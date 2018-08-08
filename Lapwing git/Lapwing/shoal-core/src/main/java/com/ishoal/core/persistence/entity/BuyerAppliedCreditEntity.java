package com.ishoal.core.persistence.entity;

import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.CreditMovementType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import java.math.BigDecimal;

@Entity(name = "BuyerAppliedCredit")
@Table(name = "BUYER_APPLIED_CREDITS")
@EntityListeners(AuditingEntityListener.class)
public class BuyerAppliedCreditEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYER_CREDIT_ID", referencedColumnName = "ID")
    private BuyerCreditEntity buyerCredit;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BuyerAppliedCreditStatus status;

    @Column(name = "SPEND_TYPE")
    @Enumerated(EnumType.STRING)
    private CreditMovementType spendType;

    @Column(name = "NET_AMOUNT")
    private BigDecimal netAmount;

    @Column(name = "VAT_AMOUNT")
    private BigDecimal vatAmount;

    @Column(name = "GROSS_AMOUNT")
    private BigDecimal grossAmount;

    @Column(name = "ORIGINAL_ORDER_REFERENCE")
    private String originalOrderReference;

    @Column(name = "ORIGINAL_ORDER_LINE_PRODUCT_CODE")
    private String originalOrderLineProductCode;

    @Column(name = "ORDER_SPENT_ON_REFERENCE")
    private String orderSpentOnReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDOR_CREDIT_ID",referencedColumnName = "ID" )
    private BuyerVendorCreditEntity vendorCredit; 
    
    @CreatedDate
    @Column(name = "CREATED_DATETIME")
    private DateTime created;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATETIME")
    private DateTime modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BuyerCreditEntity getBuyerCredit() {
        return buyerCredit;
    }

    public void setBuyerCredit(BuyerCreditEntity buyerCredit) {
        this.buyerCredit = buyerCredit;
    }

    public BuyerAppliedCreditStatus getStatus() {
        return status;
    }

    public void setStatus(BuyerAppliedCreditStatus status) {
        this.status = status;
    }

    public CreditMovementType getSpendType() {
        return spendType;
    }

    public void setSpendType(CreditMovementType spendType) {
        this.spendType = spendType;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getOriginalOrderReference() {
        return originalOrderReference;
    }

    public void setOriginalOrderReference(String originalOrderReference) {
        this.originalOrderReference = originalOrderReference;
    }

    public String getOriginalOrderLineProductCode() {
        return originalOrderLineProductCode;
    }

    public void setOriginalOrderLineProductCode(String originalOrderLineProductCode) {
        this.originalOrderLineProductCode = originalOrderLineProductCode;
    }

    public String getOrderSpentOnReference() {
        return orderSpentOnReference;
    }

    public void setOrderSpentOnReference(String orderSpentOnReference) {
        this.orderSpentOnReference = orderSpentOnReference;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getModified() {
        return modified;
    }

    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    public BuyerVendorCreditEntity getVendorCredit() {
		return vendorCredit;
	}

	public void setVendorCredit(BuyerVendorCreditEntity vendorCredit) {
		this.vendorCredit = vendorCredit;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BuyerAppliedCreditEntity)) {
            return false;
        }

        BuyerAppliedCreditEntity that = (BuyerAppliedCreditEntity) o;

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
