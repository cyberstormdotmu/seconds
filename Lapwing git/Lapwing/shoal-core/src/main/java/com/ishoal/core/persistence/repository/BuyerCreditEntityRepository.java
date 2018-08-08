package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ishoal.core.persistence.entity.BuyerCreditEntity;

public interface BuyerCreditEntityRepository extends CrudRepository<BuyerCreditEntity, Long>{

    @Query("SELECT c FROM BuyerCredit c JOIN c.orderLine l WHERE l.id = ?1")
    List<BuyerCreditEntity> findCreditsForOrderLine(Long orderLineId);

    @Query("SELECT c FROM BuyerCredit c JOIN c.orderLine l JOIN l.order o JOIN o.buyer b WHERE b.id = ?1")
    List<BuyerCreditEntity> findCreditsForBuyer(Long buyerId);
}