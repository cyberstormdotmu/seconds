package com.ishoal.core.domain;

public class Product {
    private final Long id;
    private final ProductCode code;
    private final String name;
    private final String description;
    private final String review;
    private final String suitability;
    private final String termsAndConditions;
    private final Vendor vendor;
    private final ProductCategory category;
    private final ProductSpecs productSpecs;
    private final ProductImages images;
    private final Offers offers;
    private final ProductVatRates vatRates;
    private final Long stock;
    private final Long maximumPurchaseLimit;
    private final boolean processNotification;
    private final boolean submitNotification;

    private Product(Builder builder) {
        code = builder.code;
        name = builder.name;
        description = builder.description;
        termsAndConditions = builder.termsAndConditions;
        vendor = builder.vendor;
        category = builder.category;
        productSpecs = builder.productSpecs;
        images = builder.images;
        offers = builder.offers;
        vatRates = builder.vatRates;
        stock = builder.stock;
        id = builder.id;
        review = builder.review;
        suitability = builder.suitability;
        processNotification = builder.processNotification;
        submitNotification = builder.submitNotification;
        maximumPurchaseLimit = builder.maximumPurchaseLimit;
    }

    public static Builder aProduct() {
        return new Builder();
    }

    public ProductCode getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public Long getStock() {
        return stock;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public Offers getOffers() {
        return offers;
    }

    public String getReview() {
        return review;
    }

    public String getSuitability() {
        return suitability;
    }

    public boolean isValidForSave() {
        boolean hasValidOffer = !offers.isEmpty() && offers.allValidForSave();
        return hasValidOffer && !getProductSpecs().isEmpty() && !getImages().isEmpty() && !getVatRates().isEmpty();
    }
    
    public boolean isValidForUpdate() {
        boolean hasValidOffer = !offers.isEmpty();
        return hasValidOffer && !getProductSpecs().isEmpty() && !getImages().isEmpty() && !getVatRates().isEmpty();
    }

    public boolean isActive() {
        return offers.hasActive();
    }

    public boolean isExpired() {
        return offers.allExpired();
    }

    public Offer currentOffer() {
        return offers.current();
    }

    public Offer findOffer(OfferReference offerReference) {
        return offers.find(offerReference);
    }

    public ProductSpecs getProductSpecs() {
        return productSpecs;
    }

    public ProductImages getImages() {
        return images;
    }

    public VatRate currentVatRate() {
        return vatRates.current();
    }

    public ProductVatRates getVatRates() {

        return vatRates;
    }

    public Long getId() {
        return id;
    }

    public boolean isProcessNotification() {
        return processNotification;
    }

    public boolean isSubmitNotification() {
        return submitNotification;
    }

    public Long getMaximumPurchaseLimit() {
        return maximumPurchaseLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        Product other = (Product) o;
        return code.equals(other.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static final class Builder {
        private String termsAndConditions;
        private ProductCode code;
        private String name;
        private String description;
        private Vendor vendor;
        private ProductCategory category;
        private ProductSpecs productSpecs = ProductSpecs.emptyProductSpecs();
        private ProductImages images = ProductImages.emptyProductImages();
        private Offers offers = Offers.over();
        private ProductVatRates vatRates = ProductVatRates.emptyVatRates();
        private Long stock;
        private Long id;
        private String review;
        private String suitability;
        private boolean processNotification;
        private boolean submitNotification;
        private Long maximumPurchaseLimit;
        private Builder() {

        }

        public Builder code(ProductCode val) {
            code = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder processNotification(boolean val) {
            processNotification = val;
            return this;
        }

        public Builder submitNotification(boolean val) {
            submitNotification = val;
            return this;
        }

        public Builder termsAndConditions(String val) {
            termsAndConditions = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder review(String val) {
            review = val;
            return this;
        }

        public Builder suitability(String val) {
            suitability = val;
            return this;
        }

        public Builder stock(Long val) {
            stock = val;
            return this;
        }
        
        public Builder maximumPurchaseLimit(Long val) {
            maximumPurchaseLimit = val;
            return this;
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder vendor(Vendor val) {
            vendor = val;
            return this;
        }

        public Builder category(ProductCategory val) {
            category = val;
            return this;
        }

        public Builder productSpecs(ProductSpecs val) {
            productSpecs = val;
            return this;
        }

        public Builder images(ProductImages val) {
            images = val;
            return this;
        }

        public Builder offers(Offers offers) {
            this.offers = offers;
            return this;
        }

        public Builder vatRates(ProductVatRates vatRates) {
            this.vatRates = vatRates;
            return this;
        }

        public Product build() {
            return new Product(this);
        }

    }
}
