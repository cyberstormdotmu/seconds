package com.ishoal.core.persistence.adapter;

import static com.ishoal.common.util.IterableUtils.mapToList;
import static com.ishoal.core.domain.ProductCategory.aProductCategory;
import static com.ishoal.core.persistence.entity.CategoryEntity.aCategory;

import java.util.List;

import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.persistence.entity.CategoryEntity;


public class ProductCategoryEntityAdapter {

    public ProductCategory adapt(CategoryEntity entity) {
        if(entity == null) {
            return null;
        }

        return aProductCategory()
            .name(entity.getName())
            .parent(adapt(entity.getParent()))
            .id(entity.getId())
            .build();
    }

    public List<ProductCategory> adapt(Iterable<CategoryEntity> entities) {

        return mapToList(entities, this::adapt);
    }

	public CategoryEntity adapt(ProductCategory category) {
		 if(category == null) {
	            return null;
	        }
		 return aCategory()
		            .name(category.getName())
		            .parent(adapt(category.getParent()))
		            .id((category.getId()))
		            .build();
	    }
	
}
