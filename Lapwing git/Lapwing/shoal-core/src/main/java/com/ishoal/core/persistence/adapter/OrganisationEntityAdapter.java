package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.OrganisationId;
import com.ishoal.core.persistence.entity.OrganisationEntity;

import static com.ishoal.core.domain.Organisation.anOrganisation;

public class OrganisationEntityAdapter {

    public Organisation adapt(OrganisationEntity entity) {
        if(entity == null) {
            return null;
        }
        return anOrganisation().id(OrganisationId.from(entity.getId()))
                .name(entity.getName())
                .registrationNumber(entity.getRegistrationNumber())
                .industry(entity.getIndustry())
                .numberOfEmps(entity.getNumberOfEmps())
                .build();
    }

    public OrganisationEntity adapt(Organisation organisation) {

        if (organisation == null) {
            return null;
        }

        OrganisationEntity entity = new OrganisationEntity();
        if (organisation.getId() != null) {
            entity.setId(organisation.getId().asLong());
        }
        entity.setName(organisation.getName());
        entity.setRegistrationNumber(organisation.getRegistrationNumber());
        entity.setIndustry(organisation.getIndustry());
        entity.setNumberOfEmps(organisation.getNumberOfEmps());
        return entity;
    }
}
