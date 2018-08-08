package com.kenure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author TatvaSoft
 *
 */
@Entity
@Table(name="data_plan")
public class DataPlan {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="data_plan_id")
	private int dataPlanId;

	@Column(name="mb_per_month",nullable=false)
	private int mbPerMonth;

	@Column(name="created_date")
	private Date createdDate;
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getDataPlanId() {
		return dataPlanId;
	}

	public void setDataPlanId(int dataPlanId) {
		this.dataPlanId = dataPlanId;
	}

	public int getMbPerMonth() {
		return mbPerMonth;
	}

	public void setMbPerMonth(int mbPerMonth) {
		this.mbPerMonth = mbPerMonth;
	}
	
	
}