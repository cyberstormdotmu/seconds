package com.ishoal.core.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.ishoal.core.persistence.entity.PaymentGatewayChargesEntity;

public interface PaymentGatewayChargesEntityRepository extends CrudRepository<PaymentGatewayChargesEntity, Long> {

	PaymentGatewayChargesEntity findByName(String name);
}