package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderReturnEntity;

public interface OrderReturnEntityRepository extends CrudRepository<OrderReturnEntity, Long> {

	List<OrderReturnEntity> findByOrder(OrderEntity order);
	
}
