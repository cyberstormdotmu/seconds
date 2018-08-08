package com.ishoal.core.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ishoal.core.security.SecurePassword;

public class User {

    private final UserId id;
    private final String username;
    private SecurePassword hashedPassword;
    private final String forename;
    private final String surname;
    private final String mobileNumber;   
    private final Roles roles;
    private String registrationToken;
    private final Date authoriseDate;
    private final String westcoastAccountNumber;
    private final String lapwingAccountNumber;
    private final String buyerReferralCode;
    private final String appliedReferralCode;
    private String appliedFor;
    private Vendor vendor;

    private User(Builder builder) {
        id = builder.id;
        username = builder.username;
        hashedPassword = builder.hashedPassword;
        forename = builder.forename;
        surname = builder.surname;
        mobileNumber = builder.mobileNumber; 
        roles = builder.roles;
        registrationToken = builder.registrationToken;
        authoriseDate = builder.authoriseDate;
        lapwingAccountNumber = builder.lapwingAccountNumber;
        westcoastAccountNumber = builder.westcoastAccountNumber;
        buyerReferralCode = builder.buyerReferralCode;
        appliedReferralCode = builder.appliedReferralCode;
        appliedFor = builder.appliedFor;
        vendor = builder.vendor;
    }

	public static Builder aUser() {
        return new Builder();
    }

	public Date getAuthoriseDate() {
		return authoriseDate;
	}

	public String getRegistrationToken() {
		return registrationToken;
	}
	
    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    
    public String getAppliedFor() {
		return appliedFor;
	}

    public SecurePassword getHashedPassword() {
        return hashedPassword;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }
    
    public String getMobileNumber() {
		return mobileNumber;
	}
    
    public String getWestcoastAccountNumber() {
		return westcoastAccountNumber;
	}

	public String getLapwingAccountNumber() {
		return lapwingAccountNumber;
	}
	
	public String getBuyerReferralCode() {
		return buyerReferralCode;
	}

	public String getAppliedReferralCode() {
		return appliedReferralCode;
	}
	
	public Vendor getVendor() {
		return vendor;
	}

	public Roles getRoles() {
        return roles;
    }

    public void clearPassword() {
        this.hashedPassword = null;
    }

    public void setHashedPassword(SecurePassword hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void requestingConfirmation()
    {
    	this.registrationToken = "PENDING";
    }
    
    public void setAppliedFor(String appliedFor) {
        this.appliedFor = appliedFor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        return new EqualsBuilder().append(id, other.id).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("username", username)
                .toString();
    }

    public void addRole(Role role) {
        getRoles().add(role);
    }

    public String getEmailAddress() {

        return username;
    }

    public void clearRoles() {
        this.roles.clear();
    }

    public static final class Builder {
        private UserId id = UserId.emptyUserId;
        private String username;
        private SecurePassword hashedPassword;
        private String forename;
        private String surname;
        private String mobileNumber;
        private Roles roles = Roles.someRoles().build();
        private String registrationToken;
        private Date authoriseDate;
        private String westcoastAccountNumber;
        private String lapwingAccountNumber;
        private String buyerReferralCode;
        private String appliedReferralCode;
        private String appliedFor;
        private Vendor vendor;
        
        private Builder() {
        }

        public Builder id(UserId val) {
            id = val;
            return this;
        }

        public Builder authoriseDate(Date val) {
        	authoriseDate = val;
            return this;
        }
        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder hashedPassword(SecurePassword val) {
            hashedPassword = val;
            return this;
        }

        public Builder forename(String val) {
            forename = val;
            return this;
        }

        public Builder surname(String val) {
            surname = val;
            return this;
        }
        
        public Builder mobileNumber(String val) {
        	mobileNumber = val;
            return this;
        }
        
        public Builder registrationToken(String val) {
        	registrationToken = val;
            return this;
        }
        
        public Builder westcoastAccountNumber(String val) {
        	westcoastAccountNumber = val;
            return this;
        }
        
        public Builder lapwingAccountNumber(String val) {
        	lapwingAccountNumber = val;
            return this;
        }
        
        public Builder buyerReferralCode(String val) {
        	buyerReferralCode = val;
            return this;
        }
        
        public Builder appliedReferralCode(String val) {
        	appliedReferralCode = val;
            return this;
        }
        
        public Builder appliedFor(String val) {
        	appliedFor = val;
            return this;
        }
        
        public Builder vendor(Vendor vendor) {
			this.vendor = vendor;
			return this;
		}

        public Builder roles(Roles val) {
            roles = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
