package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ishoal.core.persistence.entity.BuyerWithdrawCreditEntity;

@Repository
public interface BuyerWithdrawCreditEntityRepository extends CrudRepository<BuyerWithdrawCreditEntity, Long> {

	@Query("SELECT bwc from BuyerWithdrawCredit bwc JOIN bwc.buyer b JOIN b.user u WHERE u.id = ?1")
	List<BuyerWithdrawCreditEntity> findBuyerWithdrawLapwingCredits(Long buyerId);

    BuyerWithdrawCreditEntity findByid(Long id);
	
	List<BuyerWithdrawCreditEntity> findBywithdrawStatus(String withdrawStatus);
	
}
