package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import com.ishoal.core.persistence.entity.ProductEntity;

public interface OrderLineEntityRepository extends CrudRepository<OrderLineEntity, Long> {
	
	List<OrderLineEntity> findByOrder(OrderEntity entity);

	List<OrderLineEntity> findByProduct(ProductEntity entity);
}