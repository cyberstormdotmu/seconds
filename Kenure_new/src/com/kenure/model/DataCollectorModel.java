package com.kenure.model;

public class DataCollectorModel {
	private Integer rowIndex;
	private Integer dcLocation;
	private String networkType;
	private Double latitude;
	private Double longitude;
	
	public Integer getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}
	public Integer getDcLocation() {
		return dcLocation;
	}
	public void setDcLocation(Integer dcLocation) {
		this.dcLocation = dcLocation;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
}
