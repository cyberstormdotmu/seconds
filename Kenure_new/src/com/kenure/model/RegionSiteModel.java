package com.kenure.model;

import java.util.List;

public class RegionSiteModel {

	private String regionName;
	
	private List<String> siteNameList;

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public List<String> getSiteNameList() {
		return siteNameList;
	}

	public void setSiteNameList(List<String> siteNameList) {
		this.siteNameList = siteNameList;
	}
	
}
