package com.ishoal.core.persistence.repository;

import com.ishoal.core.persistence.entity.VendorEntity;
import org.springframework.data.repository.CrudRepository;

public interface VendorEntityRepository extends CrudRepository<VendorEntity, Long> {

    VendorEntity findByName(String vendorName);
}
