package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ishoal.core.persistence.entity.BuyerProfileEntity;

public interface BuyerProfileEntityRepository extends CrudRepository<BuyerProfileEntity, Long> {

   @Query("SELECT bp FROM BuyerProfile bp WHERE bp.user.username = ?1")
    BuyerProfileEntity findByUsername(String username);

   @Query("SELECT bp FROM BuyerProfile bp WHERE bp.user.registrationToken = ?1")
   List<BuyerProfileEntity> findAllConfirm(String registrationToken);
}
