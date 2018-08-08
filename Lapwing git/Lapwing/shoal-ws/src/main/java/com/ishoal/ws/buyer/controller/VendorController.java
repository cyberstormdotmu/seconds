package com.ishoal.ws.buyer.controller;

import static com.ishoal.common.util.IterableUtils.mapToList;
import static com.ishoal.ws.common.dto.VendorDto.aVendor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.vendor.ManageVendorService;
import com.ishoal.ws.buyer.adapter.VendorRequestAdapter;
import com.ishoal.ws.common.dto.VendorDto;
import com.ishoal.ws.exceptionhandler.ErrorInfo;
@RestController
@RequestMapping("/ws/vendors")
public class VendorController {
    private static final Logger logger = LoggerFactory.getLogger(VendorController.class);
    private final VendorRequestAdapter vendorRequestAdapter = new VendorRequestAdapter();
    private ManageVendorService manageVendorService;
    
    public VendorController(ManageVendorService manageVendorService) {
            this.manageVendorService = manageVendorService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public List<String> getAllVendors() {
        logger.info("Fetch all vendor List");
        return mapToList(manageVendorService.fetchVendorDetails(), Vendor::getName);
    }
    
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewVendor(@RequestBody String vendorName) {
	    
	    logger.info("creating a new creditInfoDetails ",vendorName);
	    VendorDto vendorDto=aVendor().name(vendorName).build();
	    
		PayloadResult<Vendor> result = manageVendorService.saveNewVendor(vendorRequestAdapter.adapt(vendorDto));
		
		if (result.isSuccess()) {
			return ResponseEntity.ok(vendorRequestAdapter.adapt(result.getPayload()));
		} else {
			logger.warn("Failed to Add the Vendor Details. Reason: " + result.getError());
			return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
		}
		
	}
    

}
