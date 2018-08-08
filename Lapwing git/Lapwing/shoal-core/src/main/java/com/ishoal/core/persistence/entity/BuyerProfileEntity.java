package com.ishoal.core.persistence.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name="BuyerProfile")
@Table(name = "BUYER_PROFILES")
@EntityListeners(AuditingEntityListener.class)
public class BuyerProfileEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID")
    private OrganisationEntity organisation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONTACT_ID", referencedColumnName = "ID")
    private ContactEntity contact;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BANK_ACCOUNT_ID", referencedColumnName = "ID")
    private BankAccountEntity bankAccount;

    @Column(name = "COMPLETED")
    private boolean isCompleted;

    @CreatedDate
    @Column(name = "CREATED_DATETIME")
    private DateTime created;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATETIME")
    private DateTime modified;

    @Version
    @Column(name = "VERSION")
    private long version;
    
    
    @OneToMany(fetch= FetchType.LAZY , mappedBy = "buyer" )
    private List<AddressEntity> addresses;

    public Long getId() {

        return id;
    }
	
	public List<AddressEntity> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressEntity> addresses) {
		this.addresses = addresses;
	}

	public void setId(Long id) {

        this.id = id;
    }

    public UserEntity getUser() {

        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public OrganisationEntity getOrganisation() {

        return organisation;
    }

    public void setOrganisation(OrganisationEntity organisation) {

        this.organisation = organisation;
    }

    public ContactEntity getContact() {

        return contact;
    }

    public void setContact(ContactEntity contact) {

        this.contact = contact;
    }

 

    public BankAccountEntity getBankAccount() {

        return bankAccount;
    }

    public void setBankAccount(BankAccountEntity bankAccount) {

        this.bankAccount = bankAccount;
    }

    public boolean isCompleted() {

        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public DateTime getCreated() {

        return created;
    }

    public DateTime getModified() {

        return modified;
    }

    public long getVersion() {

        return version;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BuyerProfileEntity that = (BuyerProfileEntity) o;

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
}
