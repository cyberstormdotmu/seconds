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
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.entity.CategoryEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.products.CategoryService;
import com.ishoal.core.products.SupplierCreditIntegrationService;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/admin")
public class SupplierCreditIntegrationController {
	
private final SupplierCreditIntegrationService supplierCreditIntegrationService;
    
    private static final Logger logger = LoggerFactory.getLogger(SupplierCreditIntegrationController.class);
    
    public SupplierCreditIntegrationController(SupplierCreditIntegrationService supplierCreditIntegrationService) {
        this.supplierCreditIntegrationService = supplierCreditIntegrationService;
    }
 
    @RequestMapping(method = RequestMethod.GET, value="/supplierCreditIntegrationList")
    public List<Vendor> findAllSupplierCredit() {
        logger.info("Admin request to get all supplier credits.");
        List<Vendor> vendorList;
        vendorList = supplierCreditIntegrationService.fetchAllCategories();
        return vendorList;
    }
    
    @RequestMapping(method = RequestMethod.PUT, value="/supplierCreditIntegrationList")
    public ResponseEntity<?> editTermsAndCondition(@RequestParam(value = "vendorId") long vendorId,@RequestParam(value = "vendorName") String vendorName,
            @RequestParam(value = "termsAndCondition") String termsAndCondition){
        
    	ResponseEntity<?> response = null;
        boolean result = supplierCreditIntegrationService.findCategoryByName(vendorName, vendorId,termsAndCondition);
        if(result){
        	logger.info("vendor terms and conditions successfully updated.","");
            response = ResponseEntity.ok().build();
        }
        else {
            logger.warn("vendor could not be added.", "", "vendor could not be added.");
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("vendor could not be added."));
        }
        return response;
    }
}
