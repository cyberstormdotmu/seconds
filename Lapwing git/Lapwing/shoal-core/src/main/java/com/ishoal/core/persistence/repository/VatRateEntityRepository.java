package com.ishoal.core.persistence.repository;

import com.ishoal.core.persistence.entity.VatRateEntity;
import org.springframework.data.repository.CrudRepository;

public interface VatRateEntityRepository extends CrudRepository<VatRateEntity, Long> {

    VatRateEntity findByVatCode(String vatCode);
}
