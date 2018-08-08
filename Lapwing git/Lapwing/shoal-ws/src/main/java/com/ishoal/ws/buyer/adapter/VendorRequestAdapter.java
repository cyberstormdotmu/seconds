package com.ishoal.ws.buyer.adapter;

import static com.ishoal.core.domain.Vendor.aVendor;

import com.ishoal.core.domain.Vendor;
import com.ishoal.ws.common.dto.VendorDto;

public class VendorRequestAdapter {

	public Vendor adapt(VendorDto vendor) {
		if (vendor == null) {
			return null;
		}
		return aVendor()
				.id(vendor.getId())
				.name(vendor.getName()).build();
	}

	public VendorDto adapt(Vendor vendor) {
		if (vendor == null) {
			return null;
		}
		return VendorDto.aVendor()
				.id(vendor.getId())
				.name(vendor.getName()).build();
	}
   
}
