package com.ishoal.core.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="BankAccount")
@Table(name = "BANK_ACCOUNTS")
public class BankAccountEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_name")
    private String accountName;
    @Column(name = "sortcode")
    private String sortcode;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "building_name")
    private String buildingName;
    @Column(name = "street_address")
    private String streetAddress;
    @Column(name = "locality")
    private String locality;
    @Column(name = "post_town")
    private String postTown;
    @Column(name = "post_code")
    private String postCode;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getAccountName() {

        return accountName;
    }

    public void setAccountName(String accountName) {

        this.accountName = accountName;
    }

    public String getSortcode() {

        return sortcode;
    }

    public void setSortcode(String sortcode) {

        this.sortcode = sortcode;
    }

    public String getAccountNumber() {

        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {

        this.accountNumber = accountNumber;
    }

    public String getBankName() {

        return bankName;
    }

    public void setBankName(String bankName) {

        this.bankName = bankName;
    }

    public String getBuildingName() {

        return buildingName;
    }

    public void setBuildingName(String buildingName) {

        this.buildingName = buildingName;
    }

    public String getStreetAddress() {

        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {

        this.streetAddress = streetAddress;
    }

    public String getLocality() {

        return locality;
    }

    public void setLocality(String locality) {

        this.locality = locality;
    }

    public String getPostTown() {

        return postTown;
    }

    public void setPostTown(String postTown) {

        this.postTown = postTown;
    }

    public String getPostCode() {

        return postCode;
    }

    public void setPostCode(String postCode) {

        this.postCode = postCode;
    }
}
