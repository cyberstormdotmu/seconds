package com.kenure.model;

public class KDLEmailModel {

	// Common
	private String subject;
	private String name;
	private String projectPath;
	
	// New Registration
	private String userName;
	private String password;
	
	// Fortgot Password 
	
	// For Dc
	private String dcSerialNo;
	private Integer dcNetworkId;
	private Double dcLatitude;
	private Double dcLongitude;
	
	// For Endpoint
	private int endPointRegId;
	private Double endLatitude;
	private Double endLongitude;
	private String endAddress1;
	private String endAddress2;
	private String endAddress3;
	private String strretName;
	private String zip;
	
	// For Installer
	private String resource;
	private String resourceName;
	
	// For Site
	
	
	// Getter-Setter from here
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProjectPath() {
		return projectPath;
	}
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	public String getDcSerialNo() {
		return dcSerialNo;
	}
	public void setDcSerialNo(String dcSerialNo) {
		this.dcSerialNo = dcSerialNo;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Integer getDcNetworkId() {
		return dcNetworkId;
	}
	public void setDcNetworkId(Integer dcNetworkId) {
		this.dcNetworkId = dcNetworkId;
	}
	public Double getDcLatitude() {
		return dcLatitude;
	}
	public void setDcLatitude(Double dcLatitude) {
		this.dcLatitude = dcLatitude;
	}
	public Double getDcLongitude() {
		return dcLongitude;
	}
	public void setDcLongitude(Double dcLongitude) {
		this.dcLongitude = dcLongitude;
	}
	public int getEndPointRegId() {
		return endPointRegId;
	}
	public void setEndPointRegId(int endPointRegId) {
		this.endPointRegId = endPointRegId;
	}
	public Double getEndLatitude() {
		return endLatitude;
	}
	public void setEndLatitude(Double endLatitude) {
		this.endLatitude = endLatitude;
	}
	public Double getEndLongitude() {
		return endLongitude;
	}
	public void setEndLongitude(Double endLongitude) {
		this.endLongitude = endLongitude;
	}
	public String getEndAddress1() {
		return endAddress1;
	}
	public void setEndAddress1(String endAddress1) {
		this.endAddress1 = endAddress1;
	}
	public String getEndAddress2() {
		return endAddress2;
	}
	public void setEndAddress2(String endAddress2) {
		this.endAddress2 = endAddress2;
	}
	public String getEndAddress3() {
		return endAddress3;
	}
	public void setEndAddress3(String endAddress3) {
		this.endAddress3 = endAddress3;
	}
	public String getStrretName() {
		return strretName;
	}
	public void setStrretName(String strretName) {
		this.strretName = strretName;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
}
