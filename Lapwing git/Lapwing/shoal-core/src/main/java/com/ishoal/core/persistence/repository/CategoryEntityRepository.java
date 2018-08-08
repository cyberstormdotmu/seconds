package com.ishoal.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ishoal.core.persistence.entity.CategoryEntity;

public interface CategoryEntityRepository  extends CrudRepository<CategoryEntity, Long> {

    CategoryEntity findByName(String categoryName);
    
    CategoryEntity findById(long categoryId);
    
    /*@Query(value = "WITH RECURSIVE categoriesCTE(id, category_name, parent) AS (SELECT id, category_name, parent.id FROM categories WHERE parent.id = ?1"+
            " UNION ALL SELECT mgr.id, mgr.category_name, mgr.parent.id FROM categoriesCTE AS usr"+
            " INNER JOIN categories AS mgr ON usr.id = mgr.parent.id) SELECT id FROM categoriesCTE AS u", nativeQuery = true)*/
    @Query(value = "WITH RECURSIVE categoriesCTE AS (SELECT id, category_name, parent_id FROM categories WHERE parent_id = ?1"+
            " UNION ALL SELECT mgr.id, mgr.category_name, mgr.parent_id AS steps FROM categoriesCTE AS usr"+
            " INNER JOIN categories AS mgr ON usr.id = mgr.parent_id) SELECT id FROM categoriesCTE AS u", nativeQuery = true)
    List<Integer> fetchParentCategoryListById(long categoryId);
 
    @Query(value = "SELECT id, category_name, parent_id FROM categories WHERE id NOT IN :categoryIds", nativeQuery = true)
    List<CategoryEntity> fetchParentCategoryListWithValidation(@Param("categoryIds") List<Integer> categoryIds);
    
}
