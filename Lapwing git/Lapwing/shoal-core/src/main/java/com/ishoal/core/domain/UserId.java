package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserId {
    public static final UserId emptyUserId = new UserId(null);

    private Long id;

    public UserId(Long id) {
        this.id = id;
    }

    
    public static UserId from(Long id) {
        if(id == null) {
            return emptyUserId;
        }
        return new UserId(id);
    }

    public Long asLong() {
        return id;
    }
    
    
    public Long getId() {
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
        if (!(o instanceof UserId)) {
            return false;
        }
        UserId other = (UserId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }    
}