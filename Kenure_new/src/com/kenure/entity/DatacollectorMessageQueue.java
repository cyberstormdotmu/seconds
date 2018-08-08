package com.kenure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="datacollector_message_queue")
public class DatacollectorMessageQueue {

	@Id
	@Column(name="datacollector_message_queue_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer datacollectorMessageQueueId;
	
	@Column(name="datacollector_id")
	private Integer datacollectorId;
	
	@Column(name="register_id")
	private String registerId;
	
	@Column(name="type")
	private String type;
	
	@Column(name="time_added")
	private Date timeAdded;

	@Column(name="message_color")
	private String messageColor;
	
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

	public Integer getDatacollectorMessageQueueId() {
		return datacollectorMessageQueueId;
	}

	public void setDatacollectorMessageQueueId(Integer datacollectorMessageQueueId) {
		this.datacollectorMessageQueueId = datacollectorMessageQueueId;
	}

	public Integer getDatacollectorId() {
		return datacollectorId;
	}

	public void setDatacollectorId(Integer datacollectorId) {
		this.datacollectorId = datacollectorId;
	}

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTimeAdded() {
		return timeAdded;
	}

	public void setTimeAdded(Date timeAdded) {
		this.timeAdded = timeAdded;
	}

	public String getMessageColor() {
		return messageColor;
	}

	public void setMessageColor(String messageColor) {
		this.messageColor = messageColor;
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
	
	
}