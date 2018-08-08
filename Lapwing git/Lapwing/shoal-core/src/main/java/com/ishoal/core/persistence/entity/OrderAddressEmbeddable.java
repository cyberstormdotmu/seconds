package com.ishoal.core.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderAddressEmbeddable {

    @Column(name = "IS_INVOICE_ADDRESS")
    private boolean isInvoiceAddress;

    @Column(name = "IS_DELIVERY_ADDRESS")
    private boolean isDeliveryAddress;

    @Column(name = "ORGANISATION_NAME")
    private String organisationName;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "BUILDING_NAME")
    private String buildingName;

    @Column(name = "STREET_ADDRESS")
    private String streetAddress;

    @Column(name = "LOCALITY")
    private String locality;

    @Column(name = "POST_TOWN")
    private String postTown;

    @Column(name = "POSTCODE")
    private String postcode;

    public boolean isInvoiceAddress() {
        return isInvoiceAddress;
    }

    public void setInvoiceAddress(boolean invoiceAddress) {
        isInvoiceAddress = invoiceAddress;
    }

    public boolean isDeliveryAddress() {
        return isDeliveryAddress;
    }

    public void setDeliveryAddress(boolean deliveryAddress) {
        isDeliveryAddress = deliveryAddress;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
