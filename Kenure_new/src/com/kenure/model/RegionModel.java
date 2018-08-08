package com.kenure.model;

import java.util.Set;

public class RegionModel {

	private int regionId;
	
	private String regionName;
	
	private Set<SiteModel> siteList;

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Set<SiteModel> getSiteList() {
		return siteList;
	}

	public void setSiteList(Set<SiteModel> siteList) {
		this.siteList = siteList;
	}
	
}
