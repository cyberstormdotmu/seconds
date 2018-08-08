package com.kenure.model;

import java.util.List;

public class DcInstallationFileModel {
	
	private String installerName;
	private List<DataCollectorModel> groupValue;

	public String getInstallerName() {
		return installerName;
	}

	public void setInstallerName(String installerName) {
		this.installerName = installerName;
	}

	public List<DataCollectorModel> getGroupValue() {
		return groupValue;
	}

	public void setGroupValue(List<DataCollectorModel> groupValue) {
		this.groupValue = groupValue;
	}
}
