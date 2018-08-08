package com.kenure.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="site")
@BatchSize(size = 4)
public class Site {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="site_id")
	private Integer siteId;
	
	@Column(name="site_name")
	private String siteName;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_ts")
	private Date createdTs;

	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_ts")
	private Date updatedTs;

	@Column(name="deleted_by")
	private Integer deletedBy;
	
	@Column(name="deleted_ts")
	private Date deletedTs;
	
	@Column(name="current_status")
	private String currentStatus;

	@Column(name="commissioning_start_time")
	private Date commissioningStartTime;

	@Column(name="commissioning_end_time")
	private Date commissioningEndTime;

	@Column(name="commissioning_type")
	private String commissioningType;

	@Column(name="schedule_time")
	private Date scheduleTime;

	@Column(name="tag")
	private Integer tag;

	@Column(name="active",nullable = true,columnDefinition = "BIT", length = 1)
	private boolean activeStatus = true;
	
	@Column(name="route_file_name")
	private String routeFileName;

	@Column(name="route_file_last_update")
	private Date routeFileLastUpdate;

	@Column(name="route_file_last_update_by_name")
	private String routeFileLastUpdateByName;

	@Column(name="no_of_endpoint_files")
	private Integer noOfEndpointFile;

	@Column(name="no_of_dc_files")
	private Integer noOfDcFile;

	@Column(name="level_1_comm_start_time")
	private Date level1CommStartTime;

	@Column(name="level_n_comm_start_time")
	private Date levelNCommStartTime;

	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="region_id")
	private Region region;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="site")
	private Set<SiteInstallationFiles> siteInstallationfiles;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="site",cascade=CascadeType.MERGE,orphanRemoval=true)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.ALL})
	private Set<DataCollector> datacollector;

	@OneToMany(fetch=FetchType.LAZY,mappedBy="site",cascade={CascadeType.MERGE,CascadeType.ALL},orphanRemoval=true)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.ALL})
	private Set<BoundryDataCollector> boundrydatacollector;
	
	@Column(name="mri_to_display")
	private String dummyMriToDisplay;

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}

	public Integer getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Integer deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedTs() {
		return deletedTs;
	}

	public void setDeletedTs(Date deletedTs) {
		this.deletedTs = deletedTs;
	}

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

	public Date getCommissioningEndTime() {
		return commissioningEndTime;
	}

	public void setCommissioningEndTime(Date commissioningEndTime) {
		this.commissioningEndTime = commissioningEndTime;
	}

	public String getCommissioningType() {
		return commissioningType;
	}

	public void setCommissioningType(String commissioningType) {
		this.commissioningType = commissioningType;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getRouteFileName() {
		return routeFileName;
	}

	public void setRouteFileName(String routeFileName) {
		this.routeFileName = routeFileName;
	}

	public Date getRouteFileLastUpdate() {
		return routeFileLastUpdate;
	}

	public void setRouteFileLastUpdate(Date routeFileLastUpdate) {
		this.routeFileLastUpdate = routeFileLastUpdate;
	}

	public String getRouteFileLastUpdateByName() {
		return routeFileLastUpdateByName;
	}

	public void setRouteFileLastUpdateByName(String routeFileLastUpdateByName) {
		this.routeFileLastUpdateByName = routeFileLastUpdateByName;
	}

	public Integer getNoOfEndpointFile() {
		return noOfEndpointFile;
	}

	public void setNoOfEndpointFile(Integer noOfEndpointFile) {
		this.noOfEndpointFile = noOfEndpointFile;
	}

	public Integer getNoOfDcFile() {
		return noOfDcFile;
	}

	public void setNoOfDcFile(Integer noOfDcFile) {
		this.noOfDcFile = noOfDcFile;
	}

	public Date getLevel1CommStartTime() {
		return level1CommStartTime;
	}

	public void setLevel1CommStartTime(Date level1CommStartTime) {
		this.level1CommStartTime = level1CommStartTime;
	}

	public Date getLevelNCommStartTime() {
		return levelNCommStartTime;
	}

	public void setLevelNCommStartTime(Date levelNCommStartTime) {
		this.levelNCommStartTime = levelNCommStartTime;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Set<SiteInstallationFiles> getSiteInstallationfiles() {
		return siteInstallationfiles;
	}

	public void setSiteInstallationfiles(
			Set<SiteInstallationFiles> siteInstallationfiles) {
		this.siteInstallationfiles = siteInstallationfiles;
	}

	public Set<DataCollector> getDatacollector() {
		return datacollector;
	}

	public void setDatacollector(Set<DataCollector> datacollector) {
		this.datacollector = datacollector;
	}

	public Set<BoundryDataCollector> getBoundrydatacollector() {
		return boundrydatacollector;
	}

	public void setBoundrydatacollector(
			Set<BoundryDataCollector> boundrydatacollector) {
		this.boundrydatacollector = boundrydatacollector;
	}

	public String getDummyMriToDisplay() {
		return dummyMriToDisplay;
	}

	public void setDummyMriToDisplay(String dummyMriToDisplay) {
		this.dummyMriToDisplay = dummyMriToDisplay;
	}
	
}