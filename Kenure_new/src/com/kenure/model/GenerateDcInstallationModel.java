package com.kenure.model;

import java.util.List;

public class GenerateDcInstallationModel {
	private String siteId;
	private List<DcInstallationFileModel> responseArray;

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public List<DcInstallationFileModel> getResponseArray() {
		return responseArray;
	}

	public void setResponseArray(List<DcInstallationFileModel> responseArray) {
		this.responseArray = responseArray;
	}
}
