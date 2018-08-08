package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderPaymentEntity;

public interface OrderPaymentEntityRepository extends CrudRepository<OrderPaymentEntity, Long> {

	List<OrderPaymentEntity> findByOrder(OrderEntity entity);

}
