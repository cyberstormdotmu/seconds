package com.kenure.model;

import java.util.List;

public class InstallationFileModel {

	private String installerName;
	
	private List<EndPointModel> groupValue;

	public String getInstallerName() {
		return installerName;
	}

	public void setInstallerName(String installerName) {
		this.installerName = installerName;
	}

	public List<EndPointModel> getGroupValue() {
		return groupValue;
	}

	public void setGroupValue(List<EndPointModel> groupValue) {
		this.groupValue = groupValue;
	}

	
	
}
