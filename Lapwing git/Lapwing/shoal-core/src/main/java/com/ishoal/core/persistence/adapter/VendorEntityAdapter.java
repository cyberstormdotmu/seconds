package com.ishoal.core.persistence.adapter;

import static com.ishoal.common.util.IterableUtils.mapToList;
import static com.ishoal.core.domain.Vendor.aVendor;

import java.util.List;

import com.ishoal.core.domain.Vendor;
import com.ishoal.core.domain.VendorId;
import com.ishoal.core.persistence.entity.VendorEntity;

public class VendorEntityAdapter {

    public Vendor adapt(VendorEntity entity) {
        if(entity == null) {
            return null;
        }
        return aVendor()
                .id(VendorId.from(entity.getId()))
                .name(entity.getName())
                .termsAndCondition(entity.getTermsAndCondition())
                .build();
    }

    public VendorEntity adapt(Vendor vendor) {
        VendorEntity entity = new VendorEntity();
        entity.setId(vendor.getId().asLong());
        entity.setName(vendor.getName());
        entity.setTermsAndCondition(vendor.getTermsAndCondition());

        return entity;
    }

    public List<Vendor> adapt(Iterable<VendorEntity> vendorEntityResponse) {
        return mapToList(vendorEntityResponse, this::adapt);
    }

}
