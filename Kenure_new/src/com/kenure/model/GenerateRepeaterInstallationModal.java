package com.kenure.model;

import java.util.List;

public class GenerateRepeaterInstallationModal {

	private String siteId;
	private List<RepeaterInstallationFileModal> responseArray;

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public List<RepeaterInstallationFileModal> getResponseArray() {
		return responseArray;
	}

	public void setResponseArray(List<RepeaterInstallationFileModal> responseArray) {
		this.responseArray = responseArray;
	}
}
