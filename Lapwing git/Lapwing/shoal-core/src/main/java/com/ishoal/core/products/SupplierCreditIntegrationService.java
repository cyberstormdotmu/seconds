package com.ishoal.core.products;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.adapter.ProductCategoryEntityAdapter;
import com.ishoal.core.persistence.adapter.VendorEntityAdapter;
import com.ishoal.core.persistence.entity.CategoryEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.persistence.repository.CategoryEntityRepository;
import com.ishoal.core.persistence.repository.VendorEntityRepository;

public class SupplierCreditIntegrationService {
	
	private final VendorEntityRepository vendorEntityRepository;
    private final VendorEntityAdapter adapter = new VendorEntityAdapter();
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public SupplierCreditIntegrationService(VendorEntityRepository vendorEntityRepository) {
        this.vendorEntityRepository = vendorEntityRepository;
     }

    public List<Vendor> fetchAllCategories() {

        Iterable<VendorEntity> entities = vendorEntityRepository.findAll();
        return adapter.adapt(entities);
    }
    
    public boolean findCategoryByName(String categoryName, long vendorId, String termsAndCondition){
    	boolean result = false;
        VendorEntity vendorEntity = vendorEntityRepository.findByName(categoryName);
        if(vendorId == vendorEntity.getId()){
        	vendorEntity.setName(categoryName);
        	vendorEntity.setTermsAndCondition(termsAndCondition);
        	vendorEntityRepository.save(vendorEntity);
        	result = true;
        }
        return result;
    }
}
