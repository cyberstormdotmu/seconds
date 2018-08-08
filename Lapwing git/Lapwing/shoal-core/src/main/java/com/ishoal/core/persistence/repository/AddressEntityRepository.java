package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ishoal.core.persistence.entity.AddressEntity;

public interface AddressEntityRepository extends CrudRepository<AddressEntity, Long> {
	
	@Query("SELECT a from Address a JOIN a.buyer b WHERE b.id = ?1")
	List<AddressEntity> findAddressByUserId(Long userId);

	AddressEntity findById(Long id);
		
	
	@Query("SELECT a from Address a where a.departmentName =:departmentName AND a.buyer.id=:buyer_id")
	AddressEntity findByDepartmentName(@Param("departmentName")String departmentName,@Param("buyer_id") Long buyer_id);

}
