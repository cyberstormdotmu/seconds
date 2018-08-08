package com.ishoal.core.domain;

import java.math.BigDecimal;

import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static com.ishoal.core.domain.Product.aProduct;
import static com.ishoal.core.domain.ProductCategoryTestData.aProductCategoryWithParent;
import static com.ishoal.core.domain.ProductImageTestData.mainImage;
import static com.ishoal.core.domain.ProductImageTestData.secondaryImage;
import static com.ishoal.core.domain.ProductImages.someProductImages;
import static com.ishoal.core.domain.ProductSpecTestData.driveSpec;
import static com.ishoal.core.domain.ProductSpecTestData.memorySpec;
import static com.ishoal.core.domain.ProductSpecTestData.processorSpec;
import static com.ishoal.core.domain.ProductSpecs.someProductSpecs;
import static com.ishoal.core.domain.ProductVatRateTestData.vatRatesWith20PercentRate;

public class ProductTestData {
    public static final String NAME = "Hp laptop";
    public static final String DESCRIPTION = "The HP EliteBook 840 thin and light notebook";
    public static final String ROOT_CATEGORY = "Products";
    public static final String MAIN_CATEGORY = "Laptops";
    public static final String SUB_CATEGORY = "Power User";
    public static final Vendor VENDOR = Vendor.aVendor().name("HP").build();
    public static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    public static final String VAT_CODE = "STANDARD";
    public static final String TERMS_AND_CONDITIONS = "OFFER valid for 1 week only";
    public static final long STOCK = 40000L;

    public static Product.Builder productBuilder(String code) {
        return productBuilder(ProductCode.from(code));
    }

    public static Product.Builder productBuilderWithNoOffers(String code) {
        return productBuilderWithNoOffers(ProductCode.from(code));
    }

    public static Product.Builder productBuilderWithNoOffers(ProductCode code) {
        return aProduct().code(code)
            .name(NAME)
            .description(DESCRIPTION)
            .vendor(VENDOR)
            .category(aProductCategoryWithParent())
            .vatRates(vatRatesWith20PercentRate())
            .productSpecs(productSpecBuilder())
            .images(productImageBuilder())
            .stock(STOCK)
        	.termsAndConditions(TERMS_AND_CONDITIONS);
    }



    public static Product.Builder productBuilder(ProductCode code) {
        return aProduct().code(code)
                .name(NAME)
                .description(DESCRIPTION)
                .vendor(VENDOR)
                .category(aProductCategoryWithParent())
                .vatRates(vatRatesWith20PercentRate())
                .productSpecs(productSpecBuilder())
                .images(productImageBuilder())
                .offers(Offers.over(anExistingOpenOffer().build()))
                .stock(STOCK)
            	.termsAndConditions(TERMS_AND_CONDITIONS);
    }

    public static ProductSpecs productSpecBuilder() {

        return someProductSpecs()
                .productSpec(processorSpec())
                .productSpec(memorySpec())
                .productSpec(driveSpec())
                .build();
    }

    public static ProductImages productImageBuilder() {
        return someProductImages()
            .productImage(mainImage())
            .productImage(secondaryImage())
            .build();
    }
}
