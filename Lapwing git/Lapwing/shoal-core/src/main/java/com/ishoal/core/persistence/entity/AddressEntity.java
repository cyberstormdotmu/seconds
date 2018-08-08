package com.ishoal.core.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="Address")
@Table(name = "addresses")
public class AddressEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "department_name")
    private String departmentName;
    @Column(name = "building_name")
    private String buildingName;
    @Column(name = "street_address")
    private String streetAddress;
    @Column(name = "locality")
    private String locality;
    @Column(name = "post_town")
    private String postTown;
    @Column(name = "postcode")
    private String postcode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="buyer_id", referencedColumnName="ID")
    private BuyerProfileEntity buyer;


    public Long getId() {

        return id;
    }


	public BuyerProfileEntity getBuyer() {
		return buyer;
	}


	public void setBuyer(BuyerProfileEntity buyer) {
		this.buyer = buyer;
	}


	public void setId(long id) {

        this.id = id;
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
