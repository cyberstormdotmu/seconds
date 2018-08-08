package com.ishoal.app.integrationtest.data;

import com.ishoal.ws.buyer.dto.MoneyDto;
import com.ishoal.ws.buyer.dto.PriceBandDto;
import com.ishoal.ws.buyer.dto.ProductDto;
import com.ishoal.ws.buyer.dto.ProductImageDto;
import com.ishoal.ws.buyer.dto.ProductSpecDto;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ishoal.ws.buyer.dto.ProductVatRateDto.aVatRateDto;
import static com.ishoal.ws.buyer.dto.PriceBandDto.aPriceBand;
import static com.ishoal.ws.buyer.dto.ProductSpecDto.aProductSpec;

public class ProductTestData {

    public static final String PRODUCT_NAME = com.ishoal.core.domain.ProductTestData.NAME;
    public static final long PRODUCT_STOCK = com.ishoal.core.domain.ProductTestData.STOCK;
    public static final String PRODUCT_DESCRIPTION = com.ishoal.core.domain.ProductTestData.DESCRIPTION;
    public static final String VENDOR_NAME = com.ishoal.core.domain.ProductTestData.VENDOR.getName();
    public static final String ROOT_CATEGORY = com.ishoal.core.domain.ProductTestData.ROOT_CATEGORY;
    public static final String MAIN_CATEGORY = com.ishoal.core.domain.ProductTestData.MAIN_CATEGORY;
    public static final String SUB_CATEGORY = com.ishoal.core.domain.ProductTestData.SUB_CATEGORY;
    public static final String PRODUCT_VAT_CODE = "STANDARD";
    public static final String PRODUCT_TERMS_AND_CONDITIONS = com.ishoal.core.domain.ProductTestData.TERMS_AND_CONDITIONS;
    public static final BigDecimal PRODUCT_VAT_RATE = com.ishoal.core.domain.ProductTestData.VAT_RATE;
    public static final String IMAGE_URL_1 = "http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/c03889408_390x286.jpg";
    public static final String IMAGE_DESC_1 = "ShoLite Laptop";
    public static final String IMAGE_URL_2 = "http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/c03889425_1560x1144.jpg";
    public static final String IMAGE_DESC_2 = "ShoLite Laptop";


    private ProductTestData() {}

    public static ProductDto buildANewProductDto(String productCode) {

        return ProductDto.aProduct().code(productCode)
            .name(PRODUCT_NAME)
            .description(PRODUCT_DESCRIPTION)
            .vendorName(VENDOR_NAME)
            .categories(someProductCategories())
            .specifications(someProductSpecs())
            .images(someProductImages())
            .vatRate(aVatRateDto().code(com.ishoal.core.domain.ProductTestData.VAT_CODE).rate(com.ishoal.core.domain.ProductTestData.VAT_RATE).build())
            .offerStartDate(DateTime.parse("2016-01-15T00:00:00.000Z"))
            .offerEndDate(DateTime.parse("3016-01-16T23:59:59.000Z"))
            .priceBands(somePriceBands())
            .stock(PRODUCT_STOCK)
            .termsAndConditions(PRODUCT_TERMS_AND_CONDITIONS)
            .build();
    }

    public static List<ProductSpecDto> someProductSpecs() {

        List<ProductSpecDto> productSpecs = new ArrayList<>();
        productSpecs.add(aProductSpec().name("Processor").description("Intel Core i5").build());
        productSpecs.add(aProductSpec().name("Memory").description("4GB DDR3").build());
        productSpecs.add(aProductSpec().name("Drive").description("512GB Sata3 SSD").build());
        return productSpecs;
    }

    public static List<String> someProductCategories() {

        List<String> categories = new ArrayList<>();
        categories.add(SUB_CATEGORY);
        return categories;
    }

    public static List<ProductImageDto> someProductImages() {

        List<ProductImageDto> images = new ArrayList<>();
        images.add(ProductImageDto.aProductImageDto().order(1).url(
            IMAGE_URL_1).description(IMAGE_DESC_1).build());
        images.add(ProductImageDto.aProductImageDto().order(1).url(
            IMAGE_URL_2).description(IMAGE_DESC_2).build());
        return images;
    }

    private static List<PriceBandDto> somePriceBands() {
        List<PriceBandDto> priceBands = new ArrayList<>();
        priceBands.add(aPriceBand()
            .minVolume(0L)
            .maxVolume(99L)
            .buyerPrice(new MoneyDto("1000.00"))
            .build());
        priceBands.add(aPriceBand()
            .minVolume(100L)
            .maxVolume(999L)
            .buyerPrice(new MoneyDto("975.00"))
            .build());
        priceBands.add(aPriceBand()
            .minVolume(1000L)
            .maxVolume(4999L)
            .buyerPrice(new MoneyDto("950.00"))
            .build());
        return priceBands;
    }
}
