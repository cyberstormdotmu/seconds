package com.ishoal.core.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity(name = "OfferPriceBand")
@Table(name = "OFFER_PRICE_BANDS")
public class PriceBandEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFFER_ID", referencedColumnName = "ID")
    private OfferEntity offer;
    
    @Column(name = "MIN_VOLUME", nullable = false)
    private long minVolume;

    @Column(name = "MAX_VOLUME", nullable = true)
    private Long maxVolume;

    @Column(name = "BUYER_PRICE", nullable = false)
    private BigDecimal buyerPrice;

    @Column(name = "VENDOR_PRICE", nullable = false)
    private BigDecimal vendorPrice;

    @Column(name = "LAPWING_MARGIN", nullable = false)
    private BigDecimal shoalMargin;

    @Column(name = "DISTRIBUTOR_MARGIN", nullable = false)
    private BigDecimal distributorMargin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMinVolume() {
        return minVolume;
    }

    public Long getMaxVolume() {
        return maxVolume;
    }

    public BigDecimal getBuyerPrice() {
        return buyerPrice;
    }

    public BigDecimal getVendorPrice() {
        return vendorPrice;
    }

    public BigDecimal getShoalMargin() {
        return shoalMargin;
    }

    public BigDecimal getDistributorMargin() {
        return distributorMargin;
    }

    public void setOffer(OfferEntity offer) {

        this.offer = offer;
    }

    public void setMinVolume(long minVolume) {

        this.minVolume = minVolume;
    }

    public void setMaxVolume(Long maxVolume) {

        this.maxVolume = maxVolume;
    }

    public void setBuyerPrice(BigDecimal buyerPrice) {

        this.buyerPrice = buyerPrice;
    }

    public void setVendorPrice(BigDecimal vendorPrice) {

        this.vendorPrice = vendorPrice;
    }

    public void setShoalMargin(BigDecimal shoalMargin) {

        this.shoalMargin = shoalMargin;
    }

    public void setDistributorMargin(BigDecimal distributorMargin) {

        this.distributorMargin = distributorMargin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PriceBandEntity)) {
            return false;
        }

        PriceBandEntity that = (PriceBandEntity) o;

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
