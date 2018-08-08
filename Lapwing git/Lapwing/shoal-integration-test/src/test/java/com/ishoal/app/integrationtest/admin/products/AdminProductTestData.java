package com.ishoal.app.integrationtest.admin.products;

import com.ishoal.app.integrationtest.data.ProductTestData;
import com.ishoal.ws.admin.dto.AdminPriceBandDto;
import com.ishoal.ws.admin.dto.AdminProductDto;
import com.ishoal.ws.buyer.dto.MoneyDto;
import com.ishoal.ws.buyer.dto.ProductImageDto;
import com.ishoal.ws.buyer.dto.ProductSpecDto;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.ishoal.ws.admin.dto.AdminPriceBandDto.adminPriceBand;
import static com.ishoal.ws.buyer.dto.ProductVatRateDto.aVatRateDto;

public class AdminProductTestData {

    public static final String PRODUCT_NAME = com.ishoal.core.domain.ProductTestData.NAME;
    public static final String PRODUCT_DESCRIPTION = com.ishoal.core.domain.ProductTestData.DESCRIPTION;
    public static final String VENDOR_NAME = com.ishoal.core.domain.ProductTestData.VENDOR.getName();
    public static final DateTime OFFER_START_DATE = DateTime.now().withTime(0, 0, 0, 0);
    public static final DateTime OFFER_END_DATE = DateTime.now().plusDays(30).withTime(0, 0, 0, 0);

    public static final DateTime INVALID_OFFER_START_DATE = DateTime.now().minusDays(1).withTime(0, 0, 0, 0);
    public static final DateTime INVALID_OFFER_END_DATE = DateTime.now().plusDays(30).withTime(0, 0, 0, 0);
    public static final long PRODUCT_STOCK = com.ishoal.core.domain.ProductTestData.STOCK;
   public static final String PRODUCT_TERMS_AND_CONDITIONS = com.ishoal.core.domain.ProductTestData.TERMS_AND_CONDITIONS;
    private AdminProductTestData() {}

    public static AdminProductDto.Builder buildAnAdminProductDto(String productCode) {

        return AdminProductDto.anAdminProduct().code(productCode)
            .name(PRODUCT_NAME)
            .description(PRODUCT_DESCRIPTION)
            .vendorName(VENDOR_NAME)
            .categories(someProductCategories())
            .specifications(someProductSpecs())
            .images(someProductImages())
            .vatRate(aVatRateDto().code(com.ishoal.core.domain.ProductTestData.VAT_CODE).rate(com.ishoal.core.domain.ProductTestData.VAT_RATE).build())
            .offerStartDate(OFFER_START_DATE)
            .offerEndDate(OFFER_END_DATE)
            .priceBands(somePriceBands())
            .stock(PRODUCT_STOCK)
           .termsAndConditions(PRODUCT_TERMS_AND_CONDITIONS);
    }

    public static AdminProductDto.Builder buildAnAdminProductDtoWithInvalidOffer(String productCode) {
       return buildAnAdminProductDto(productCode)
           .offerStartDate(INVALID_OFFER_START_DATE)
           .offerEndDate(INVALID_OFFER_END_DATE);
    }

    private static List<ProductSpecDto> someProductSpecs() {

        return ProductTestData.someProductSpecs();
    }

    private static List<String> someProductCategories() {

        return ProductTestData.someProductCategories();
    }

    private static List<ProductImageDto> someProductImages() {

        return ProductTestData.someProductImages();
    }

    private static List<AdminPriceBandDto> somePriceBands() {
        List<AdminPriceBandDto> priceBands = new ArrayList<>();
        priceBands.add(adminPriceBand()
            .minVolume(0L)
            .maxVolume(99L)
            .buyerPrice(new MoneyDto("1000.00"))
            .vendorPrice(new MoneyDto("900.00"))
            .shoalMargin(new MoneyDto("5.00"))
            .distributorMargin(new MoneyDto("2.00"))
            .build());
        priceBands.add(adminPriceBand()
            .minVolume(100L)
            .maxVolume(999L)
            .buyerPrice(new MoneyDto("975.00"))
            .vendorPrice(new MoneyDto("875.00"))
            .shoalMargin(new MoneyDto("5.00"))
            .distributorMargin(new MoneyDto("2.00"))
            .build());
        priceBands.add(adminPriceBand()
            .minVolume(1000L)
            .maxVolume(null)
            .buyerPrice(new MoneyDto("950.00"))
            .vendorPrice(new MoneyDto("850.00"))
            .shoalMargin(new MoneyDto("5.00"))
            .distributorMargin(new MoneyDto("2.00"))
            .build());
        return priceBands;
    }
}
