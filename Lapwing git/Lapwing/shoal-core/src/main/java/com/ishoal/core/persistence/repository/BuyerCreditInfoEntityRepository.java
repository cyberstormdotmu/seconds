package com.ishoal.core.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ishoal.core.persistence.entity.BuyerCreditInfoEntity;
@Repository
public interface BuyerCreditInfoEntityRepository extends CrudRepository<BuyerCreditInfoEntity, Long> {
	
	@Query("SELECT n FROM BuyerCreditInfo n WHERE n.buyer.id =:id")
	BuyerCreditInfoEntity findByBuyerId(@Param("id")long id);
}
