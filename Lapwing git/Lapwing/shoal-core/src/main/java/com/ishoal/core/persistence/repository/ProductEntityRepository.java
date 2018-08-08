package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ishoal.core.persistence.entity.CategoryEntity;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.VendorEntity;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByCode(String code);

    List<ProductEntity> findByCategory(CategoryEntity category);

    @Query("SELECT p FROM Product p JOIN p.offers o WHERE o.offerReference = ?1")
    ProductEntity findProductForOffer(String offerReference);

    List<ProductEntity> findByVendor(VendorEntity vendorEntity);
   
}
