package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OrganisationId {
    public static final OrganisationId emptyOrganisationId = new OrganisationId(null);

    private Long id;

    private OrganisationId(Long id) {
        this.id = id;
    }

    public Long getId() {
		return id;
	}

	public static OrganisationId from(Long id) {
        if(id == null) {
            return emptyOrganisationId;
        }
        return new OrganisationId(id);
    }

    public Long asLong() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(asLong());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganisationId)) {
            return false;
        }
        OrganisationId other = (OrganisationId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }
}