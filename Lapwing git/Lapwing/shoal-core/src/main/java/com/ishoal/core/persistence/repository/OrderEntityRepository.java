package com.ishoal.core.persistence.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.UserEntity;
import com.ishoal.core.persistence.entity.VendorEntity;

public interface OrderEntityRepository extends CrudRepository<OrderEntity, Long> {

    OrderEntity findByReference(String reference);
    
    OrderEntity findById(Long id);
    
    @Query("SELECT o FROM Order o JOIN o.lines line JOIN line.offer off WHERE off.id = ?1 AND o.status = ?2")
    List<OrderEntity> findOrdersForOfferWithStatus(long offerId, OrderStatus status);

    @Query("SELECT o FROM Order o JOIN o.lines line JOIN line.offer off WHERE off.offerReference = ?1")
    List<OrderEntity> findOrdersForOffer(String offerReference);

    List<OrderEntity> findByStatus(Set<OrderStatus> orderStatuses);
    
    List<OrderEntity> findByStatusAndBuyer(Set<OrderStatus> orderStatuses, UserEntity user);

    @Query("SELECT o FROM Order o JOIN o.buyer b WHERE b.id = ?1")
    List<OrderEntity> findOrdersForBuyer(long userId);

    @Query("SELECT o FROM Order o JOIN o.lines l JOIN l.credits c JOIN o.buyer b WHERE b.id = ?1 AND o.paymentStatus = 'PAID'")
    List<OrderEntity> findPaidOrdersWithCreditsForBuyer(long userId);

	List<OrderEntity> findByStatusAndVendor(Set<OrderStatus> orderStatuses, VendorEntity vendorEntity);

	List<OrderEntity> findByStatusAndBuyerAndVendor(Set<OrderStatus> orderStatuses, UserEntity user, VendorEntity vendorEntity);
}