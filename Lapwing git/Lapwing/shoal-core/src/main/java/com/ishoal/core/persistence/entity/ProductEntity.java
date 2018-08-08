package com.ishoal.core.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity(name = "Product")
@Table(name = "PRODUCTS")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCT_CODE", length = 64)
    private String code;

    @Column(name = "NAME", length = 64)
    private String name;

    @Column(name = "DESCRIPTION", length = 4096)
    private String description;
    
    @Column(name = "REVIEW", length = 4096)
    private String review;
    
    @Column(name = "SUITABILITY", length = 4096)
    private String suitability;

    @Column(name = "TERMS_AND_CONDITIONS", length = 4096)
    private String termsAndConditions;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDOR_ID", referencedColumnName = "ID")
    private VendorEntity vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    private CategoryEntity category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cascade(CascadeType.ALL)
    private List<ProductSpecEntity> productSpecs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cascade(CascadeType.ALL)
    private List<ProductImageEntity> images;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cascade(CascadeType.ALL)
    private List<OfferEntity> offers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cascade(CascadeType.ALL)
    private List<ProductVatRateEntity> vatRates;
    
    @Column(name = "PRODUCT_STOCK")
    private Long stock;
    
    @Column(name = "MAXIMUM_PURCHASE_LIMIT")
    private Long maximumPurchaseLimit;

    @Column(name = "PROCESS_NOTIFICATION")
    private boolean processNotification;
    
    @Column(name = "SUBMIT_NOTIFICATION")
    private boolean submitNotification;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }


	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public void setDescription(String description) {
        this.description = description;
    }

    public VendorEntity getVendor() {
        return vendor;
    }

    public void setVendor(VendorEntity vendor) {
        this.vendor = vendor;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<ProductSpecEntity> getProductSpecs() {
        return productSpecs;
    }

    public void setProductSpecs(List<ProductSpecEntity> productSpecs) {
        this.productSpecs = productSpecs;
    }

    public List<ProductImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ProductImageEntity> images) {
        this.images = images;
    }

    public List<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferEntity> offers) {
        this.offers = offers;
    }

    public List<ProductVatRateEntity> getVatRates() {
        return vatRates;
    }

    public void setVatRates(List<ProductVatRateEntity> vatRates) {
        this.vatRates = vatRates;
    }

    public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSuitability() {
        return suitability;
    }

    public void setSuitability(String suitability) {
        this.suitability = suitability;
    }

    public boolean isProcessNotification() {
        return processNotification;
    }

    public void setProcessNotification(boolean processNotification) {
        this.processNotification = processNotification;
    }

    public boolean isSubmitNotification() {
        return submitNotification;
    }

    public void setSubmitNotification(boolean submitNotification) {
        this.submitNotification = submitNotification;
    }

    public Long getMaximumPurchaseLimit() {
        return maximumPurchaseLimit;
    }

    public void setMaximumPurchaseLimit(Long maximumPurchaseLimit) {
        this.maximumPurchaseLimit = maximumPurchaseLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ProductEntity)) {
            return false;
        }

        ProductEntity that = (ProductEntity) o;

        return new EqualsBuilder()
                .append(code, that.code)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(code)
                .toHashCode();
    }
}
