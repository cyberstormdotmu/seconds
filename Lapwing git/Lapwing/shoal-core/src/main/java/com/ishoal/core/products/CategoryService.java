
package com.ishoal.core.products;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.persistence.adapter.ProductCategoryEntityAdapter;
import com.ishoal.core.persistence.entity.CategoryEntity;
import com.ishoal.core.persistence.repository.CategoryEntityRepository;

public class CategoryService {
	
    private final CategoryEntityRepository categoryEntityRepository;
    private final ProductCategoryEntityAdapter adapter = new ProductCategoryEntityAdapter();
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryEntityRepository categoryEntityRepository) {
        this.categoryEntityRepository = categoryEntityRepository;
     }

    public List<ProductCategory> fetchAllCategories() {

        Iterable<CategoryEntity> entities = categoryEntityRepository.findAll();
        return adapter.adapt(entities);
    }

    public List<ProductCategory> fetchParentCategoryListById(long categoryId) {
        
       List<Integer> categoryIds = categoryEntityRepository.fetchParentCategoryListById(categoryId);
       categoryIds.add((int) categoryId);
       /*String categoryIdStr = "( "+categoryId+",";
       if(categoryIds != null){
           for(int i = 0; i < categoryIds.size() ; i++){
               if(categoryIds.get(i) != null){
                   if(i == categoryIds.size() - 1){
                       categoryIdStr =  categoryIdStr + "" + categoryIds.get(i) + ")";
                   }else{
                       categoryIdStr = categoryIdStr + "" + categoryIds.get(i) + ",";
                   }
               }
           }
       }*/
       List<CategoryEntity> categoryEntities = categoryEntityRepository.fetchParentCategoryListWithValidation(categoryIds);
        
        return adapter.adapt(categoryEntities);
    }
    
    public boolean deleteCategoryById(long categoryId) {
        
        CategoryEntity categoryEntity = categoryEntityRepository.findById(categoryId);
        boolean result = false;
        
        try {
            if(categoryEntity != null){
                categoryEntityRepository.delete(categoryId);
                result = true;
                logger.info("Category deteled successfully.");
            }
            return result;
        } catch (Exception e) {
            logger.info("Category can't deleted.");
            return result;
        }
        
    }

    public CategoryEntity findCategoryByName(String categoryName){
        CategoryEntity categoryEntity = categoryEntityRepository.findByName(categoryName);
        return categoryEntity;
    }
    
    public boolean addCategory(String categoryName, String parentCategoryName) {

        boolean result = false;
        
        try {
            
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName(categoryName);
            
            if(parentCategoryName != null && parentCategoryName.trim() != ""){
                CategoryEntity parentEntity = findCategoryByName(parentCategoryName);
                categoryEntity.setParent(parentEntity);
            }else{
                categoryEntity.setParent(null);
            }
            categoryEntityRepository.save(categoryEntity);
            result = true;
        } catch (Exception e) {
            logger.info("Category can't added.");
            result = false;
        }
        return result;
    }

    public boolean editCategory(long categoryId, String categoryName, String parentCategoryName) {

        boolean result = false;
        try {
            CategoryEntity categoryEntity = categoryEntityRepository.findById(categoryId);
            categoryEntity.setName(categoryName);
            
            if(parentCategoryName != null && parentCategoryName.trim() != ""){
                CategoryEntity parentEntity = findCategoryByName(parentCategoryName);
                categoryEntity.setParent(parentEntity);
            }else{
                categoryEntity.setParent(null);
            }
            categoryEntityRepository.save(categoryEntity);
            result = true;
        } catch (Exception e) {
            logger.info("Category can't added.");
            result = false;
        }
        return result;

    }

}
   
	
