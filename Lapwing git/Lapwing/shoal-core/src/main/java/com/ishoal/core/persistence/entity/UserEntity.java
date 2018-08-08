package com.ishoal.core.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity(name = "User")
@Table(name = "USERS",
uniqueConstraints = @UniqueConstraint(columnNames = "USER_NAME")
)
public class UserEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FORENAME")
    private String forename;

    @Column(name = "SURNAME")
    private String surname;
    
    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;
    
    @Column(name = "REGISTRATION_TOKEN")
    private String registrationToken;
    
    @Column(name = "WESTCOAST_ACCOUNT_NUMBER")
    private String westcoastAccountNumber;
    
    @Column(name = "LAPWING_ACCOUNT_NUMBER")
    private String lapwingAccountNumber;
    
    @Column(name = "AUTHORISED_DATE")
    private Date authoriseDate;
	
    @Column(name = "BUYER_REFERRAL_CODE")
    private String buyerReferralCode;
    
    @Column(name = "APPLIED_REFERRAL_CODE")
    private String appliedReferralCode;
    
    @Column(name = "APPLIED_FOR")
    private String appliedFor;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "VENDOR_ID", referencedColumnName = "ID")
    private VendorEntity vendor;
    
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "userId", orphanRemoval = true)
    private List<UserRoleEntity> roles = new ArrayList<UserRoleEntity>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getAppliedFor() {
		return appliedFor;
	}

	public void setAppliedFor(String appliedFor) {
		this.appliedFor = appliedFor;
	}
	
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMobileNumber() {
    	return mobileNumber;
    }
    
    public void setMobileNumber(String mobileNumber) {
    	this.mobileNumber = mobileNumber;
    }
    
    public String getRegistrationToken() {
		return registrationToken;
	}
    
    public void setRegistrationToken(String registrationToken) {
		this.registrationToken = registrationToken;
	}
    
    public Date getAuthoriseDate() {
		return authoriseDate;
	}

	public void setAuthoriseDate(Date authoriseDate) {
		this.authoriseDate = authoriseDate;
	}
	
    public String getWestcoastAccountNumber() {
		return westcoastAccountNumber;
	}

	public void setWestcoastAccountNumber(String westcoastAccountNumber) {
		this.westcoastAccountNumber = westcoastAccountNumber;
	}

	public String getLapwingAccountNumber() {
		return lapwingAccountNumber;
	}

	public void setLapwingAccountNumber(String lapwingAccountNumber) {
		this.lapwingAccountNumber = lapwingAccountNumber;
	}
	
	public String getBuyerReferralCode() {
		return buyerReferralCode;
	}

	public void setBuyerReferralCode(String buyerReferralCode) {
		this.buyerReferralCode = buyerReferralCode;
	}

	public String getAppliedReferralCode() {
		return appliedReferralCode;
	}

	public void setAppliedReferralCode(String appliedReferralCode) {
		this.appliedReferralCode = appliedReferralCode;
	}

	public VendorEntity getVendor() {
		return vendor;
	}

	public void setVendor(VendorEntity vendor) {
		this.vendor = vendor;
	}

	public List<UserRoleEntity> getRoles() {
        if (this.roles == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.roles);
    }

    public void addRoles(List<UserRoleEntity> roles) {
        for (UserRoleEntity userRole : roles) {
            userRole.setUser(this);
            this.roles.add(userRole);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserEntity)) {
            return false;
        }

        UserEntity that = (UserEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }


}
