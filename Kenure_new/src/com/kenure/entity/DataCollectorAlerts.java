package com.kenure.entity;

import java.util.Date;

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
@Table(name="datacollector_alerts")
public class DataCollectorAlerts {

	@Id
	@Column(name="datacollector_alerts_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int datacollectorAlertsId;
	
	@Column(name="alert")
	private String alert;
	
	@Column(name="alert_ack", nullable=false)
	private boolean alertAck;
	
	@Column(name="alert_date")
	private Date alertDate;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="datacollector_id")
	private DataCollector dataCollector;
	
	public int getDatacollectorAlertsId() {
		return datacollectorAlertsId;
	}

	public void setDatacollectorAlertsId(int datacollectorAlertsId) {
		this.datacollectorAlertsId = datacollectorAlertsId;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public boolean isAlertAck() {
		return alertAck;
	}

	public void setAlertAck(boolean alertAck) {
		this.alertAck = alertAck;
	}

	public Date getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}

	public DataCollector getDataCollector() {
		return dataCollector;
	}

	public void setDataCollector(DataCollector dataCollector) {
		this.dataCollector = dataCollector;
	}
	
}
