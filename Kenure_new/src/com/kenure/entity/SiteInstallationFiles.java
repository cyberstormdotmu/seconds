package com.kenure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="site_installation_files")
public class SiteInstallationFiles {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="site_installation_files_id")
	private int siteInstallationFileId;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="no_of_endpoints")
	private Integer noOfEndPoints;
	
	@Column(name="no_of_datacollectors")
	private Integer noOfDatacollectors;
	
	@Column(name="file_uploaded")
	private Boolean isFileUploaded;
	
	@Column(name="file_verified")
	private Boolean isFileVerified;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="site_id")
	private Site site;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="installer_id")
	private Installer installer;
	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Installer getInstaller() {
		return installer;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}

	public int getSiteInstallationFileId() {
		return siteInstallationFileId;
	}

	public void setSiteInstallationFileId(int siteInstallationFileId) {
		this.siteInstallationFileId = siteInstallationFileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getNoOfEndPoints() {
		return noOfEndPoints;
	}

	public void setNoOfEndPoints(Integer noOfEndPoints) {
		this.noOfEndPoints = noOfEndPoints;
	}

	public Boolean getIsFileUploaded() {
		return isFileUploaded;
	}

	public void setIsFileUploaded(Boolean isFileUploaded) {
		this.isFileUploaded = isFileUploaded;
	}

	public Boolean getIsFileVerified() {
		return isFileVerified;
	}

	public void setIsFileVerified(Boolean isFileVerified) {
		this.isFileVerified = isFileVerified;
	}

	public Integer getNoOfDatacollectors() {
		return noOfDatacollectors;
	}

	public void setNoOfDatacollectors(Integer noOfDatacollectors) {
		this.noOfDatacollectors = noOfDatacollectors;
	}
}
