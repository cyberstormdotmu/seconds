package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductVatRates;
import com.ishoal.core.domain.Vendor;
import com.ishoal.ws.buyer.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;

import static com.ishoal.core.domain.Offer.anOffer;

public class ProductDtoAdapter {

    private ProductImageDtoAdapter imageAdapter = new ProductImageDtoAdapter();
    private ProductSpecDtoAdapter specAdapter = new ProductSpecDtoAdapter();
    private PriceBandsDtoAdapter priceBandsAdapter = new PriceBandsDtoAdapter();
    private ProductVatRateDtoAdapter productVatRateDtoAdapter = new ProductVatRateDtoAdapter();

    public ProductDto adapt(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto.Builder builder = ProductDto.aProduct()
            .code(product.getCode().toString())
            .name(product.getName())
            .description(product.getDescription())
            .review(product.getReview())
            .suitability(product.getSuitability())
            .stock(product.getStock())
            .maximumPurchaseLimit(product.getMaximumPurchaseLimit())
            .categories(adaptCategory(product.getCategory()))
            .vendorName(product.getVendor().getName())
            .termsAndConditions(product.getTermsAndConditions())
            .images(imageAdapter.adapt(product.getImages()))
            .specifications(specAdapter.adapt(product.getProductSpecs()))
            .vatRate(productVatRateDtoAdapter.adapt(product.currentVatRate()));
            Offer offer = product.currentOffer();
            if (offer != null) {
                builder.currentVolume(offer.getCurrentVolume())
                    .offerStartDate(offer.getStartDateTime())
                    .offerEndDate(offer.getEndDateTime())
                    .priceBands(priceBandsAdapter.adapt(offer.getPriceBands()));
            }

        return builder.build();
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

    public Product adapt(ProductDto product) {

        return Product.aProduct()
            .code(ProductCode.from(product.getCode()))
            .name(product.getName())
            .description(product.getDescription())
            .review(product.getReview())
            .suitability(product.getSuitability())
            .stock(product.getStock())
            .maximumPurchaseLimit(product.getMaximumPurchaseLimit())
            .termsAndConditions(product.getTermsAndConditions())
            .vendor(Vendor.aVendor().name(product.getVendorName()).build())
            .vatRates(ProductVatRates.over(productVatRateDtoAdapter.adapt(product.getVatRate())))
            .category(adaptCategories(product.getCategories()))
            .productSpecs(specAdapter.adapt(product.getSpecifications()))
            .images(imageAdapter.adapt(product.getImages()))
            .offers(adaptOffer(product))
            .build();
    }

    private Offers adaptOffer(ProductDto product) {
        if (product.getOfferStartDate() == null
            || product.getOfferEndDate() == null) {
            return Offers.emptyOffers();
        }
        return Offers.over(anOffer()
            .startDateTime(product.getOfferStartDate())
            .endDateTime(product.getOfferEndDate())
            .priceBands(priceBandsAdapter.adapt(product.getPriceBands()))
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