package com.kenure.model;

import java.util.Date;
import java.util.Set;

public class SiteModel{

	private int siteId;
	 
	private String siteName;
	
	private Set<DCModel> dc;
	
	private String currentStatus;
	
	private Date commissioningStartTime;

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Date getCommissioningStartTime() {
		return commissioningStartTime;
	}

	public void setCommissioningStartTime(Date commissioningStartTime) {
		this.commissioningStartTime = commissioningStartTime;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Set<DCModel> getDc() {
		return dc;
	}

	public void setDc(Set<DCModel> dc) {
		this.dc = dc;
	}
	
}
