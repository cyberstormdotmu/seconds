package com.ishoal.ws.buyer.dto;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ProductDto {
	private long id;
    private String code;
    private String name;
    private String description;
    private String review;
    private String suitability;
    private List<String> categories;
    private List<ProductSpecDto> specifications;
    private List<ProductImageDto> images;
    private String vendorName;
    private ProductVatRateDto vatRate;
    private DateTime offerStartDate;
    private DateTime offerEndDate;
    private Long currentVolume;
    private List<PriceBandDto> priceBands;
    private String termsAndConditions;
    private Long stock;
    private Long maximumPurchaseLimit;
    private boolean processNotification;
    private boolean submitNotification;


	private ProductDto() {
        super();
    }

    private ProductDto(Builder builder) {

        code = builder.code;
        name = builder.name;
        description = builder.description;
        categories = builder.categories;
        specifications = builder.specifications;
        termsAndConditions=builder.termsAndConditions;
        images = builder.images;
        vendorName = builder.vendorName;
        offerStartDate = builder.offerStartDate;
        offerEndDate = builder.offerEndDate;
        currentVolume = builder.currentVolume;
        vatRate = builder.vatRate;
        priceBands = builder.priceBands;
        stock = builder.stock;
        id=builder.id;
        review = builder.review;
        suitability = builder.suitability;
        maximumPurchaseLimit=builder.maximumPurchaseLimit;
        
    }

    public static Builder aProduct() {
        return new Builder();
    }

    public String getCode() {
        return code;
    }

    public Long getStock() {
		return stock;
	}

	
	public Long getMaximumPurchaseLimit() {
        return maximumPurchaseLimit;
    }

    public void setStock(Long stock) {
		this.stock = stock;
	}

	public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public long getId() {
		return id;
	}
    public List<String> getCategories() {
        return categories;
    }

    public List<ProductSpecDto> getSpecifications() {
        return specifications;
    }

    public List<ProductImageDto> getImages() {
        return images;
    }

    public String getVendorName() {
        return vendorName;
    }

    public DateTime getOfferStartDate() {
        return offerStartDate;
    }

    public DateTime getOfferEndDate() {
        return offerEndDate;
    }

    public Long getCurrentVolume() {
        return currentVolume;
    }

    public ProductVatRateDto getVatRate() {
        return vatRate;
    }

    public List<PriceBandDto> getPriceBands() {
        return priceBands;
    }

    public String getReview() {
        return review;
    }

    public String getSuitability() {
        return suitability;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public boolean isProcessNotification() {
        return processNotification;
    }

    public boolean isSubmitNotification() {
        return submitNotification;
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }



	
	public static final class Builder {
        private String code;
        private String name;
        private String description;
        private List<String> categories = new ArrayList<>();
        private List<ProductSpecDto> specifications = new ArrayList<>();
        private List<ProductImageDto> images = new ArrayList<>();
        private String vendorName;
        private DateTime offerStartDate;
        private DateTime offerEndDate;
        private Long currentVolume;
        private ProductVatRateDto vatRate;
        private List<PriceBandDto> priceBands = new ArrayList<>();
        private String termsAndConditions;
        private Long stock;
        private long id;
        private String review;
        private String suitability;
        private boolean processNotification;
        private boolean submitNotification;
        private Long maximumPurchaseLimit;
              
        private Builder() {

        }

        public Builder code(String val) {

            code = val;
            return this;
        }

        public Builder id(long val) {

            id = val;
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

        public Builder categories(List<String> val) {

            categories = val;
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
        
        public Builder termsAndConditions(String val) {

        	termsAndConditions = val;
            return this;
        }

        public Builder specifications(List<ProductSpecDto> val) {

            specifications = val;
            return this;
        }

        public Builder images(List<ProductImageDto> val) {

            images = val;
            return this;
        }

        public Builder vendorName(String val) {

            vendorName = val;
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
              
        public Builder offerStartDate(DateTime val) {

            offerStartDate = val;
            return this;
        }

        public Builder offerEndDate(DateTime val) {

            offerEndDate = val;
            return this;
        }

        public Builder currentVolume(Long val) {

            currentVolume = val;
            return this;
        }

        public Builder vatRate(ProductVatRateDto val) {

            vatRate = val;
            return this;
        }

        public Builder priceBands(List<PriceBandDto> val) {

            priceBands = val;
            return this;
        }

       
		public ProductDto build() {

            return new ProductDto(this);
        }
    }
}
