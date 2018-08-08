package com.kenure.model;

import java.util.List;

public class RepeaterInstallationFileModal {
	private String installerName;
	private List<RepeaterModal> groupValue;

	public String getInstallerName() {
		return installerName;
	}

	public void setInstallerName(String installerName) {
		this.installerName = installerName;
	}

	public List<RepeaterModal> getGroupValue() {
		return groupValue;
	}

	public void setGroupValue(List<RepeaterModal> groupValue) {
		this.groupValue = groupValue;
	}
}
