package com.ishoal.core.persistence.repository;

import com.ishoal.core.persistence.entity.BuyerAppliedCreditEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BuyerAppliedCreditEntityRepository extends CrudRepository<BuyerAppliedCreditEntity, Long> {

    List<BuyerAppliedCreditEntity> findByOrderSpentOnReference(String orderSpentOnReference);
}
