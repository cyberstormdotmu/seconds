package com.ishoal.core.repository;

import java.util.List;

import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.adapter.VendorEntityAdapter;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.persistence.repository.VendorEntityRepository;

public class VendorRepository {
	private final VendorEntityRepository vendorEntityRepository;
    private VendorEntityAdapter vendorEntityAdapter = new VendorEntityAdapter();
    
    public VendorRepository(VendorEntityRepository vendorEntityRepository) {
    	this.vendorEntityRepository = vendorEntityRepository;
    }
    
	public Vendor save(Vendor vendor) {
		VendorEntity vendorEntity = vendorEntityAdapter.adapt(vendor);
		VendorEntity vendorEntityResponse = vendorEntityRepository.save(vendorEntity);
		return  vendorEntityAdapter.adapt(vendorEntityResponse);

	}

    public List<Vendor> findVendorList() {
        Iterable<VendorEntity> vendorEntityResponse = vendorEntityRepository.findAll();
        return vendorEntityAdapter.adapt(vendorEntityResponse); 
    }

    public Vendor alreadyExist(Vendor vendorRequest) {
        VendorEntity entity= vendorEntityAdapter.adapt(vendorRequest);
        VendorEntity vendorEntityResponse = vendorEntityRepository.findByName(entity.getName());
       return vendorEntityAdapter.adapt(vendorEntityResponse);
     }

	public Vendor findVendor(String vendorName) {
		VendorEntity vendorEntityResponse = vendorEntityRepository.findByName(vendorName);
	       return vendorEntityAdapter.adapt(vendorEntityResponse);
	}
}
