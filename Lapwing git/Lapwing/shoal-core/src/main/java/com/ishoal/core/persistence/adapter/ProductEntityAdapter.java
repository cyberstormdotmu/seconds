package com.ishoal.core.persistence.adapter;

import static com.ishoal.core.domain.Product.aProduct;

import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductImages;
import com.ishoal.core.domain.ProductSpecs;
import com.ishoal.core.domain.ProductVatRates;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.entity.ProductEntity;

public class ProductEntityAdapter {

    private final ProductSpecsEntityAdapter specsAdapter = new ProductSpecsEntityAdapter();
    private final OfferEntityAdapter offerAdapter = new OfferEntityAdapter();
    private final ProductImagesEntityAdapter imagesAdapter = new ProductImagesEntityAdapter();
    private final ProductCategoryEntityAdapter categoryAdapter = new ProductCategoryEntityAdapter();
    private final ProductVatRateEntityAdapter vatRateAdapter = new ProductVatRateEntityAdapter();
    private final VendorEntityAdapter vendorAdapter = new VendorEntityAdapter();

    public Product adapt(ProductEntity entity) {
        if(entity == null) {
            return null;
        }
        return aProduct().code(ProductCode.from(entity.getCode()))
        	.id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .review(entity.getReview())
            .suitability(entity.getSuitability())
            .processNotification(entity.isProcessNotification())
            .submitNotification(entity.isSubmitNotification())
            .termsAndConditions(entity.getTermsAndConditions())
            .vendor(adaptVendor(entity))
            .category(adaptCategory(entity))
            .productSpecs(adaptProductSpecs(entity))
            .images(adaptImages(entity))
            .vatRates(adaptVatRates(entity))
            .offers(adaptOffers(entity))
            .stock(entity.getStock())
            .maximumPurchaseLimit(entity.getMaximumPurchaseLimit())
            .build();
    }

    public ProductEntity adapt(Product product) {

        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setCode(product.getCode().toString());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setReview(product.getReview());
        entity.setSuitability(product.getSuitability());
        entity.setProcessNotification(product.isProcessNotification());
        entity.setSubmitNotification(product.isSubmitNotification());
        entity.setTermsAndConditions(product.getTermsAndConditions());
        entity.setProductSpecs(specsAdapter.adapt(product.getProductSpecs(), entity));
        entity.setImages(imagesAdapter.adapt(product.getImages(), entity));
        entity.setVatRates(vatRateAdapter.adapt(product.getVatRates(),entity));
        entity.setOffers(offerAdapter.adapt(product.getOffers(), entity));
        entity.setStock(product.getStock());
        entity.setCategory(categoryAdapter.adapt(product.getCategory()));
        entity.setVendor(vendorAdapter.adapt(product.getVendor()));
        entity.setMaximumPurchaseLimit(product.getMaximumPurchaseLimit());
        return entity;
    }

    private Vendor adaptVendor(ProductEntity entity) {
        return vendorAdapter.adapt(entity.getVendor());
    }

    private ProductImages adaptImages(ProductEntity entity) {
        return imagesAdapter.adapt(entity.getImages());
    }

    private ProductSpecs adaptProductSpecs(ProductEntity entity) {
        return specsAdapter.adapt(entity.getProductSpecs());
    }

    private Offers adaptOffers(ProductEntity entity) {
        return offerAdapter.adapt(entity.getOffers());
    }

    private ProductCategory adaptCategory(ProductEntity entity) {
        return categoryAdapter.adapt(entity.getCategory());
    }

    private ProductVatRates adaptVatRates(ProductEntity entity) {
        return vatRateAdapter.adapt(entity.getVatRates());
    }
    
}
