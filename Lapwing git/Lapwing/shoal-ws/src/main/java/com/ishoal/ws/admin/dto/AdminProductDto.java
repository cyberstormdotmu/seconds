package com.ishoal.ws.admin.dto;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.ishoal.ws.buyer.dto.ProductImageDto;
import com.ishoal.ws.buyer.dto.ProductSpecDto;
import com.ishoal.ws.buyer.dto.ProductVatRateDto;

public class AdminProductDto {
    private String id;
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
    private List<AdminPriceBandDto> priceBands;
    private Long stock;
    private Long maximumPurchaseLimit;
    private String termsAndConditions;
    private boolean processNotification;
    private boolean submitNotification;
    
    private AdminProductDto() {
        super();
    }

    private AdminProductDto(Builder builder) {
        id = builder.id;
        code = builder.code;
        name = builder.name;
        description = builder.description;
        categories = builder.categories;
        specifications = builder.specifications;
        images = builder.images;
        vendorName = builder.vendorName;
        offerStartDate = builder.offerStartDate;
        offerEndDate = builder.offerEndDate;
        currentVolume = builder.currentVolume;
        vatRate = builder.vatRate;
        priceBands = builder.priceBands;
        stock = builder.stock;
        termsAndConditions=builder.termsAndConditions;
        review=builder.review;
        suitability=builder.suitability;
        processNotification=builder.processNotification;
        submitNotification=builder.submitNotification;
        maximumPurchaseLimit=builder.maximumPurchaseLimit;
        
    }
    
    public String getId() {
        return id;
    }

    public static Builder anAdminProduct() {
        return new Builder();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public Long getStock() {
		return stock;
	}
    
    public String getTermsAndConditions() {
		return termsAndConditions;
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

    public List<AdminPriceBandDto> getPriceBands() {
        return priceBands;
    }
    
	public String getReview() {
        return review;
    }

    public String getSuitability() {
        return suitability;
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
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }

    public static final class Builder {
        private String id;
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
        private List<AdminPriceBandDto> priceBands = new ArrayList<>();
        private Long stock;
        private String termsAndConditions;
        private String review;
        private String suitability;
        private boolean processNotification;
        private boolean submitNotification;
        private Long maximumPurchaseLimit;
        
        private Builder() {

        }

        public Builder id(String val) {

            id = val;
            return this;
        }
        
        public Builder code(String val) {

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
        public Builder stock(Long val) {

            stock = val;
            return this;
        }
        
        public Builder maximumPurchaseLimit(Long val) {

            maximumPurchaseLimit = val;
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

        public Builder priceBands(List<AdminPriceBandDto> val) {

            priceBands = val;
            return this;
        }

        public AdminProductDto build() {

            return new AdminProductDto(this);
        }
    }
}
