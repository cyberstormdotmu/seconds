package com.ishoal.ws.admin.dto.adapter;

import static com.ishoal.core.domain.Offer.anOffer;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.persister.entity.Loadable;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductVatRates;
import com.ishoal.core.domain.Products;
import com.ishoal.core.domain.Vendor;
import com.ishoal.ws.admin.dto.AdminProductDto;
import com.ishoal.ws.buyer.dto.adapter.ProductImageDtoAdapter;
import com.ishoal.ws.buyer.dto.adapter.ProductSpecDtoAdapter;
import com.ishoal.ws.buyer.dto.adapter.ProductVatRateDtoAdapter;

public class AdminProductDtoAdapter {

    private ProductImageDtoAdapter imageAdapter = new ProductImageDtoAdapter();
    private ProductSpecDtoAdapter specAdapter = new ProductSpecDtoAdapter();
    private AdminPriceBandsDtoAdapter adminPriceBandsAdapter = new AdminPriceBandsDtoAdapter();
    private ProductVatRateDtoAdapter productVatRateDtoAdapter = new ProductVatRateDtoAdapter();

    public AdminProductDto adapt(Product product) {
        if (product == null) {
            return null;
        }
        AdminProductDto.Builder builder = AdminProductDto.anAdminProduct()
            .code(product.getCode().toString())
            .name(product.getName())
            .description(product.getDescription())
            .review(product.getReview())
            .suitability(product.getSuitability())
            .processNotification(product.isProcessNotification())
            .submitNotification(product.isSubmitNotification())
            .stock(product.getStock())
            .maximumPurchaseLimit(product.getMaximumPurchaseLimit())
            .termsAndConditions(product.getTermsAndConditions())
            .categories(adaptCategory(product.getCategory()))
            .vendorName(product.getVendor().getName())
            .images(imageAdapter.adapt(product.getImages()))
            .specifications(specAdapter.adapt(product.getProductSpecs()))
            .vatRate(productVatRateDtoAdapter.adapt(product.currentVatRate()));
            Offer offer = product.currentOffer();
            if (offer != null) {
                builder.currentVolume(offer.getCurrentVolume())
                    .offerStartDate(offer.getStartDateTime())
                    .offerEndDate(offer.getEndDateTime())
                    .priceBands(adminPriceBandsAdapter.adapt(offer.getPriceBands()));
            }

        return builder.build();
    }
    
    public AdminProductDto adaptFindProducts(Product product) {
        if (product == null) {
            return null;
        }
        AdminProductDto.Builder builder = AdminProductDto.anAdminProduct()
            .id(Long.toString(product.getId()))
            .code(product.getCode().toString())
            .name(product.getName())
            .description(product.getDescription())
            .review(product.getReview())
            .suitability(product.getSuitability())
            .processNotification(product.isProcessNotification())
            .submitNotification(product.isSubmitNotification())
            .stock(product.getStock())
            .maximumPurchaseLimit(product.getMaximumPurchaseLimit())
            .termsAndConditions(product.getTermsAndConditions())
            .categories(adaptCategory(product.getCategory()))
            .vendorName(product.getVendor().getName())
            .images(imageAdapter.adapt(product.getImages()))
            .specifications(specAdapter.adapt(product.getProductSpecs()))
            .vatRate(productVatRateDtoAdapter.adapt(product.currentVatRate()));
            Offers offers = product.getOffers();
            if (offers != null) {
                for (Offer offer : offers) {
                    builder.currentVolume(offer.getCurrentVolume())
                    .offerStartDate(offer.getStartDateTime())
                    .offerEndDate(offer.getEndDateTime())
                    .priceBands(adminPriceBandsAdapter.adapt(offer.getPriceBands()));
                }
            }
        return builder.build();
    }
    
    public List<AdminProductDto> adapt(Products products) {
        return IterableUtils.mapToList(products, this::adaptFindProducts);
    }

    private List<String> adaptCategory(ProductCategory category) {

        List<String> categories = new ArrayList<>();
        addToCategoryList(category, categories);
        return categories;
    }

    private void addToCategoryList(ProductCategory category, List<String> categoryList) {

        if (category != null) {
            addToCategoryList(category.getParent(), categoryList);
            categoryList.add(category.getName());
        }
    }

    public Product adapt(AdminProductDto product) {
        if(product.getId()!=""){
              return Product.aProduct()
                    .id(Long.parseLong(product.getId()))
                    .code(ProductCode.from(product.getCode()))
                    .name(product.getName())
                    .description(product.getDescription())
                    .review(product.getReview())
                    .suitability(product.getSuitability())
                    .processNotification(product.isProcessNotification())
                    .submitNotification(product.isSubmitNotification())
                    .vendor(Vendor.aVendor().name(product.getVendorName()).build())
                    .vatRates(ProductVatRates.over(productVatRateDtoAdapter.adapt(product.getVatRate())))
                    .category(adaptCategories(product.getCategories()))
                    .productSpecs(specAdapter.adapt(product.getSpecifications()))
                    .images(imageAdapter.adapt(product.getImages()))
                    .offers(adaptOffer(product))
                    .stock(product.getStock())
                    .maximumPurchaseLimit(product.getMaximumPurchaseLimit())
                    .termsAndConditions(product.getTermsAndConditions())
                    .build();
        }
            else{
                return Product.aProduct()
                        .code(ProductCode.from(product.getCode()))
                        .name(product.getName())
                        .description(product.getDescription())
                        .review(product.getReview())
                        .suitability(product.getSuitability())
                        .processNotification(product.isProcessNotification())
                        .submitNotification(product.isSubmitNotification())
                        .vendor(Vendor.aVendor().name(product.getVendorName()).build())
                        .vatRates(ProductVatRates.over(productVatRateDtoAdapter.adapt(product.getVatRate())))
                        .category(adaptCategories(product.getCategories()))
                        .productSpecs(specAdapter.adapt(product.getSpecifications()))
                        .images(imageAdapter.adapt(product.getImages()))
                        .offers(adaptOffer(product))
                        .stock(product.getStock())
                        .maximumPurchaseLimit(product.getMaximumPurchaseLimit())
                        .termsAndConditions(product.getTermsAndConditions())
                        .build();
                    } 
           
    }
    
    public List<Product> adaptForMultipleProducts(List<AdminProductDto> productDtoList) {

        List<Product> productList = new ArrayList<>();
        
        for(AdminProductDto product : productDtoList){
            productList.add(Product.aProduct()
                    .code(ProductCode.from(product.getCode()))
                    .name(product.getName())
                    .description(product.getDescription())
                    .review(product.getReview())
                    .suitability(product.getSuitability())
                    .processNotification(product.isProcessNotification())
                    .submitNotification(product.isSubmitNotification())
                    .vendor(Vendor.aVendor().name(product.getVendorName()).build())
                    .vatRates(ProductVatRates.over(productVatRateDtoAdapter.adapt(product.getVatRate())))
                    .category(adaptCategories(product.getCategories()))
                    .productSpecs(specAdapter.adapt(product.getSpecifications()))
                    .images(imageAdapter.adapt(product.getImages()))
                    .offers(adaptOffer(product))
                    .stock(product.getStock())
                    .maximumPurchaseLimit(product.getMaximumPurchaseLimit())
                    .termsAndConditions(product.getTermsAndConditions())
                    .build());
        }
        return productList;    
    }

    private Offers adaptOffer(AdminProductDto product) {
        if (product.getOfferStartDate() == null
            || product.getOfferEndDate() == null) {
            return Offers.emptyOffers();
        }
        return Offers.over(anOffer()
            .startDateTime(product.getOfferStartDate())
            .endDateTime(product.getOfferEndDate())
            .priceBands(adminPriceBandsAdapter.adapt(product.getPriceBands()))
            .build());
    }

    private ProductCategory adaptCategories(List<String> categories) {

        ProductCategory.Builder productCategory = ProductCategory.aProductCategory();
        takeTheFirstCategoryOnly(categories, productCategory);
        return productCategory.build();
    }

    private void takeTheFirstCategoryOnly(List<String> categories, ProductCategory.Builder productCategory) {

        if (!categories.isEmpty()) {
            productCategory.name(categories.get(0));
        }
    }
}
