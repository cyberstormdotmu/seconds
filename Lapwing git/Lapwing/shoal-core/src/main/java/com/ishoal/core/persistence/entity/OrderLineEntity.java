package com.ishoal.core.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "OrderLine")
@Table(name = "ORDER_LINES")
@EntityListeners(AuditingEntityListener.class)
public class OrderLineEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFFER_ID", referencedColumnName = "ID")
    private OfferEntity offer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INITIAL_PRICE_BAND_ID", referencedColumnName = "ID")
    private PriceBandEntity initialPriceBand;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENT_PRICE_BAND_ID", referencedColumnName = "ID")
    private PriceBandEntity currentPriceBand;
    
    @Column(name = "QUANTITY")
    private long quantity;

    @Column(name = "VAT_RATE")
    private BigDecimal vatRate;

    @Column(name = "NET_AMOUNT")
    private BigDecimal netAmount;

    @Column(name = "VAT_AMOUNT")
    private BigDecimal vatAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuyerCreditEntity> credits;

    @CreatedDate
    @Column(name = "CREATED_DATETIME")
    private DateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATETIME")
    private DateTime modifiedDateTime;

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

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public OfferEntity getOffer() {
        return offer;
    }

    public void setOffer(OfferEntity offer) {
        this.offer = offer;
    }

    public void setInitialPriceBand(PriceBandEntity priceBand) {
        this.initialPriceBand = priceBand;
    }
    
    public PriceBandEntity getInitialPriceBand() {
        return initialPriceBand;
    }
    
    public void setCurrentPriceBand(PriceBandEntity currentPriceBand) {
        this.currentPriceBand = currentPriceBand;
    }
    
    public PriceBandEntity getCurrentPriceBand() {
        return currentPriceBand;
    }
    
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    
    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
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

    public List<BuyerCreditEntity> getCredits() {
        return credits;
    }

    public void setCredits(List<BuyerCreditEntity> credits) {
        this.credits = credits;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OrderLineEntity)) {
            return false;
        }

        OrderLineEntity that = (OrderLineEntity) o;

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