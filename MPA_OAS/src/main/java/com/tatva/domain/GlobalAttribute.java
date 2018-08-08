package com.tatva.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="global_attributes")
public class GlobalAttribute implements Serializable {

	@Id
	@Column(name="attribute_name")
	private String attributeName;
	
	@Column(name="apply_date")
	private Date applyDate;
	
	@Transient
	private String applyDateString;
	
	@Column(name="old_value")
	private String oldValue;
	
	@Column(name="new_value")
	private String newValue;
	
	@Column(name="update_userid")
	private String updateUserId;
	
	@Column(name="last_action")
	private String lastAction;
	
	@Column(name="time_stamp")
	private Timestamp timeStamp;
	
	@Transient
	private String tempValue;
	
	@Transient
	private List<String> slots=new ArrayList<String>();
	
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.setSlots(slots);
		this.newValue = newValue;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getLastAction() {
		return lastAction;
	}

	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public List<String> getSlots() {
		List<String> slots=new ArrayList<String>();
		slots.add("1");
		slots.add("2");
		slots.add("3");
		slots.add("4");
		slots.add("5");
		slots.add("6");
		return slots;
	}

	public void setSlots(List<String> slots) {
		this.slots = slots;
	}

	public String getApplyDateString() {
		return applyDateString;
	}

	public void setApplyDateString(String applyDateString) {
		this.applyDateString = applyDateString;
	}

	public String getTempValue() {
		return tempValue;
	}

	public void setTempValue(String tempValue) {
		this.tempValue = tempValue;
	}
	
}
