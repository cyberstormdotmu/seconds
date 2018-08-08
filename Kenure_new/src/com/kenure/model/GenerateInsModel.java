package com.kenure.model;

import java.util.List;

public class GenerateInsModel {

	private String siteId;
	
	private List<InstallationFileModel> responseArray;

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public List<InstallationFileModel> getResponseArray() {
		return responseArray;
	}

	public void setResponseArray(List<InstallationFileModel> responseArray) {
		this.responseArray = responseArray;
	}
	
}
