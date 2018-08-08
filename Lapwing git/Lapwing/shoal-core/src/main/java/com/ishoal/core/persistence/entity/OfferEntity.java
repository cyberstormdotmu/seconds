package com.ishoal.core.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import java.util.List;

@Entity(name = "Offer")
@Table(name = "OFFERS")
public class OfferEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "OFFER_REFERENCE")
    private String offerReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private ProductEntity product;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offer")
    @OrderBy("minVolume ASC")
    @Cascade(CascadeType.ALL)
    private List<PriceBandEntity> priceBands;

    @Column(name = "START_DATE_TIME")
    private DateTime startDateTime;

    @Column(name = "END_DATE_TIME")
    private DateTime endDateTime;

    @Column(name = "CURRENT_VOLUME")
    private long currentVolume;

    @Version
    @Column(name = "VERSION")
    private long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfferReference() {
        return offerReference;
    }

    public void setOfferReference(String offerReference) {
        this.offerReference = offerReference;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {

        this.product = product;
    }

    public List<PriceBandEntity> getPriceBands() {
        return priceBands;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {

        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setCurrentVolume(long currentVolume) {
        this.currentVolume = currentVolume;
    }
    
    public long getCurrentVolume() {
        return currentVolume;
    }

    public void setPriceBands(List<PriceBandEntity> priceBands) {

        this.priceBands = priceBands;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OfferEntity)) {
            return false;
        }

        OfferEntity that = (OfferEntity) o;

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