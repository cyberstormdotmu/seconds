package com.ishoal.core.vendor;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.repository.VendorRepository;

public class ManageVendorService {
    private VendorRepository vendorRepository;

    public ManageVendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public PayloadResult<Vendor> saveNewVendor(Vendor vendorRequest) {
        PayloadResult<Vendor> payload = null;
        Vendor vendor = null;
        Vendor exist = null;
        exist = vendorRepository.alreadyExist(vendorRequest);
        if (exist == null) {
            vendor = vendorRepository.save(vendorRequest);
            if (vendor != null) {
                payload = PayloadResult.success(vendor);
            } else {
                payload = PayloadResult.error("Error in saving Vendor Data");
            }
        } else {
            payload = PayloadResult.error("Vendor Already Exist" + vendorRequest.getName());
        }
        return payload;
    }

    public List<Vendor> fetchVendorDetails() {

        return vendorRepository.findVendorList();
    }

	public Vendor fetchVendor(String vendorName) {
		
		return vendorRepository.findVendor(vendorName);
	}

}
