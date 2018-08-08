package com.ishoal.ws.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.persistence.entity.CategoryEntity;
import com.ishoal.core.products.CategoryService;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/admin")
public class AdminManageCategoriesController {
    
    private final CategoryService categoryService;
    
    private static final Logger logger = LoggerFactory.getLogger(AdminManageCategoriesController.class);
    
    public AdminManageCategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
 
    @RequestMapping(method = RequestMethod.GET, value="/productCategoryList")
    public List<ProductCategory> findAllOffers(@RequestParam(value = "id") long categoryId) {
        logger.info("Admin request to get all categories.");
        List<ProductCategory> categories;
        
        if (categoryId > 0) {
            categories = categoryService.fetchParentCategoryListById(categoryId);
        } else {
            categories = categoryService.fetchAllCategories();
        }
        return categories;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/deleteCategory")
    public ResponseEntity<?> deleteProductCategory(@RequestParam(value = "id") long categoryId){
        
        boolean result =  categoryService.deleteCategoryById(categoryId);
        
        ResponseEntity<?> response;
        if(result) {
            logger.info("Category Successfully deleted.","");
            response = ResponseEntity.ok().build();
        } else {
            logger.warn("Category is already used. Could not be deleted.", "", "Category is already used. Could not be deleted.");
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Category is already used. Could not be deleted."));
        }
        return response;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/addEditCategory")
    public ResponseEntity<?> addProductCategory(@RequestParam(value = "categoryId") long categoryId,@RequestParam(value = "categoryName") String categoryName,
            @RequestParam(value = "parentCategoryName") String parentCategoryName){
        
        ResponseEntity<?> response = null;
        
        if(categoryId > 0){
            
            CategoryEntity categoryEntity = categoryService.findCategoryByName(categoryName);
            
            if(categoryEntity == null){
                boolean result =  categoryService.editCategory(categoryId,categoryName,parentCategoryName);
                if(result) {
                    logger.info("Category successfully updated.","");
                    response = ResponseEntity.ok().build();
                } else {
                    logger.warn("Category could not be added.", "", "Category could not be added.");
                    response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Category could not be added."));
                }
            }else if(categoryEntity != null && categoryEntity.getId() == categoryId){
                boolean result =  categoryService.editCategory(categoryId,categoryName,parentCategoryName);
                if(result) {
                    logger.info("Category successfully updated.","");
                    response = ResponseEntity.ok().build();
                } else {
                    logger.warn("Category could not be added.", "", "Category could not be added.");
                    response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Category could not be added."));
                }
            }else{
                logger.warn("Category is already exists.", "", "Category is already exists.");
                response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Category is already exists."));
            }
            return response;
        
        }else{
            CategoryEntity categoryEntity = categoryService.findCategoryByName(categoryName);
            
            if(categoryEntity == null){
                boolean result =  categoryService.addCategory(categoryName,parentCategoryName);
                if(result) {
                    logger.info("Category successfully added.","");
                    response = ResponseEntity.ok().build();
                } else {
                    logger.warn("Category could not be added.", "", "Category could not be added.");
                    response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Category could not be added."));
                }
            }else{
                logger.warn("Category is already exists.", "", "Category is already exists.");
                response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Category is already exists."));
            }
            return response;
        }
    }
    
    
    
}
