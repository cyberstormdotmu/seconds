package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.Organisation;
import com.ishoal.ws.buyer.dto.OrganisationDto;

public class OrganisationDtoAdapter {
    public OrganisationDtoAdapter() {

    }

    public OrganisationDto adapt(Organisation organisation) {

        if (organisation == null) {
            return null;
        }
        return OrganisationDto.anOrganisationDto().name(organisation.getName()).registrationNumber(
            organisation.getRegistrationNumber()).industry(organisation.getIndustry()).numberOfEmps(organisation.getNumberOfEmps()).build();
    }

	public Organisation adapt(OrganisationDto organisation) {
		 return Organisation.anOrganisation().name(organisation.getName()).registrationNumber(
		            organisation.getRegistrationNumber()).industry(organisation.getIndustry()).numberOfEmps(organisation.getNumberOfEmps()).build();
	}
}