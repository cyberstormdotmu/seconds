package com.ishoal.core.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Hibernate;

import com.ishoal.common.util.CustomCollectors;
import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.OfferReference;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.Products;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.adapter.OfferEntityAdapter;
import com.ishoal.core.persistence.adapter.ProductEntityAdapter;
import com.ishoal.core.persistence.entity.CategoryEntity;
import com.ishoal.core.persistence.entity.OfferEntity;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.ProductImageEntity;
import com.ishoal.core.persistence.entity.ProductSpecEntity;
import com.ishoal.core.persistence.entity.ProductVatRateEntity;
import com.ishoal.core.persistence.entity.VatRateEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.persistence.repository.CategoryEntityRepository;
import com.ishoal.core.persistence.repository.ProductEntityRepository;
import com.ishoal.core.persistence.repository.ProductImagesEntityRepository;
import com.ishoal.core.persistence.repository.ProductSpecsEntityRepository;
import com.ishoal.core.persistence.repository.VatRateEntityRepository;
import com.ishoal.core.persistence.repository.VendorEntityRepository;

public class ProductRepository {
    private final ProductEntityRepository entityRepository;
    private final CategoryEntityRepository categoryEntityRepository;
    private final VendorEntityRepository vendorEntityRepository;
    private final VatRateEntityRepository vatRateEntityRepository;
    private final ProductSpecsEntityRepository productSpecsEntityRepository;
    private final ProductImagesEntityRepository productImagesEntityRepository;
    private final OfferEntityAdapter offerAdapter = new OfferEntityAdapter();

    private final ProductEntityAdapter adapter = new ProductEntityAdapter();

    public ProductRepository(ProductEntityRepository entityRepository,
            CategoryEntityRepository categoryEntityRepository, VendorEntityRepository vendorEntityRepository,
            VatRateEntityRepository vatRateEntityRepository, ProductSpecsEntityRepository productSpecsEntityRepository, 
            ProductImagesEntityRepository productImagesEntityRepository) {
        this.entityRepository = entityRepository;
        this.categoryEntityRepository = categoryEntityRepository;
        this.vendorEntityRepository = vendorEntityRepository;
        this.vatRateEntityRepository = vatRateEntityRepository;
        this.productSpecsEntityRepository = productSpecsEntityRepository;
        this.productImagesEntityRepository = productImagesEntityRepository;
    }

    public Product findByCode(ProductCode code) {
        ProductEntity entity = entityRepository.findByCode(code.toString());
        return adapter.adapt(entity);
    }

    public Product findById(ProductCode id) {
        ProductEntity entity = entityRepository.findOne(Long.parseLong(id.toString()));
        return adapter.adapt(entity);
    }

    public Products findByCategory(String categoryName) {
        List<CategoryEntity> categories = findCategoryAndSubCategories(categoryName);

        List<ProductEntity> productEntities = findProductsInCategories(categories);

        return IterableUtils.mapToCollection(productEntities, adapter::adapt, Products::over);
    }

    public Product findProductForOffer(OfferReference offerReference) {
        ProductEntity entity = entityRepository.findProductForOffer(offerReference.asString());
        return adapter.adapt(entity);
    }

    public Products findAll() {
        return IterableUtils.mapToCollection(entityRepository.findAll(), adapter::adapt, Products::over);
    }

    public Products findAllSupplierOffers(User user) {
        VendorEntity vendorEntity = vendorEntityRepository.findByName(user.getVendor().getName());
        return IterableUtils.mapToCollection(entityRepository.findByVendor(vendorEntity), adapter::adapt, Products::over);
    }

    private List<ProductEntity> findProductsInCategories(List<CategoryEntity> categories) {
        return categories.stream()
                .map(category -> entityRepository.findByCategory(category))
                .collect(CustomCollectors.toMergedList());
    }

    private List<CategoryEntity> findCategoryAndSubCategories(String categoryName) {
        List<CategoryEntity> categories = new ArrayList<>();
        CategoryEntity category = categoryEntityRepository.findByName(categoryName);
        addCategoryToList(category, categories);
        return categories;
    }

    private void addCategoryToList(CategoryEntity category, List<CategoryEntity> categories) {
        if(category != null) {
            categories.add(category);
            category.getChildren().forEach(child -> addCategoryToList(child, categories));
        }
    }

    public Product save(Product product) {
        ProductEntity productEntity = adapter.adapt(product);
        ProductEntity updatedProductEntity = null;
        CategoryEntity category = categoryEntityRepository.findByName(product.getCategory().getName());
        VendorEntity vendorEntity = vendorEntityRepository.findByName(product.getVendor().getName());
        productEntity.setCategory(category);
        productEntity.setVendor(vendorEntity);

        VatRateEntity vatRateEntity = vatRateEntityRepository.findByVatCode(product.getVatRates().current().getCode());

        ProductVatRateEntity productVatRateEntity = new ProductVatRateEntity();
        productVatRateEntity.setVatRate(vatRateEntity);
        productVatRateEntity.setProduct(productEntity);
        productEntity.setVatRates(Arrays.asList(productVatRateEntity));

        if (productEntity.getId() != null){
            ProductEntity oldProductEntity = entityRepository.findOne(productEntity.getId());
            deleteProductImagesEntity(oldProductEntity.getImages());
            deleteProductSpecsEntity(oldProductEntity.getProductSpecs());
            productEntity.setOffers(updateProductOffers(productEntity.getOffers(), oldProductEntity));
            if (oldProductEntity != null && exists(oldProductEntity)) {
                updatedProductEntity = entityRepository.save(productEntity);
            }
        }
        else
        {
            updatedProductEntity = entityRepository.save(productEntity);
        }

        return adapter.adapt(updatedProductEntity);
    }

    private List<OfferEntity> updateProductOffers(List<OfferEntity> offers, ProductEntity productEntity) {
        List<OfferEntity> offerEntities= offerAdapter.adapt(offers, productEntity);
        return offerEntities;
    }

    private boolean exists(ProductEntity entity) {
        return entity != null && entity.getId() != null;
    }
    public void updateStock(long id, long quantity) {
        ProductEntity productEntity = entityRepository.findOne(id);
        Hibernate.initialize(productEntity.getStock());
        productEntity.setStock(productEntity.getStock()-quantity);
        this.entityRepository.save(productEntity);
    }

    public void deleteProductSpecsEntity(List<ProductSpecEntity> productSpecsEntity) {
        for (ProductSpecEntity entity : productSpecsEntity) {
            this.productSpecsEntityRepository.delete(entity.getId());
        }
    }

    public void deleteProductImagesEntity(List<ProductImageEntity> productImageEntity) {
        for (ProductImageEntity entity : productImageEntity) {
            this.productImagesEntityRepository.delete(entity.getId());;
        }
    }

    public void deleteProduct(long id) {
        this.entityRepository.delete(id);
    }

    public Product findByProductId(long productId) {
        ProductEntity entity = entityRepository.findOne(productId);
        return adapter.adapt(entity);
    }

}