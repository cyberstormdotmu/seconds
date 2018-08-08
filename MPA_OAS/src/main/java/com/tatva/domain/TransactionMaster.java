package com.tatva.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tatva.domain.composite.CompositeTranc;

@Entity
@Table(name = "TRANSACTION_MASTER")
public class TransactionMaster {


	// ----------------------------------------------------------------------------------------------------------------	
	/*
	 Composite Key declaration & getter-setters*
	 * */

	@EmbeddedId
	
	private CompositeTranc transacId;	

	/* 
 		getter setter*
	 * */
	public CompositeTranc getTransacId() {
		return transacId;
	}

	public void setTransacId(CompositeTranc transacId) {
		this.transacId = transacId;
	}

	// ----------------------------------------------------------------------------------------------------------------

	/*
	 TABLE COLUMNS Variable Dec GETTERS & SETTERS STARTS*
	 * */

	@Column(name = "OTHER_SUB_TYPE")
	private String otherSubType;

	@Column(name = "TRANSACTION_QTY")
	private Short transactionQuantity;

	@Column(name = "UPDATE_USERID")
	private String updateUserId;

	@Column(name = "LAST_ACTION")
	private String lastAction;

	@Column(name = "TIME_STAMP")
	private Timestamp timestamp;

	/*
	 getter-setters*
	 * */

	public String getOtherSubType() {
		return otherSubType;
	}

	public void setOtherSubType(String otherSubType) {
		this.otherSubType = otherSubType;
	}

	public Short getTransactionQuantity() {
		return transactionQuantity;
	}

	public void setTransactionQuantity(Short transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	// ----------------------------------------------------------------------------------------------------------------	


	/*
	 Different Entity Mapping Declaration and getters-setters*
	 * */	

	@MapsId("REF_NO")
	@JoinColumn(name = "REF_NO" , nullable = false)
	@OneToOne
	private AppointmentMaster appMaster;



	/*
	 Mapping Files Getters & Setters*
	 * */


	public AppointmentMaster getAppMaster() {
		return appMaster;
	}

	public void setAppMaster(AppointmentMaster appMaster) {
		this.appMaster = appMaster;
	}

}

//----------------------------------------------------------------------------------------------------------------

/*
	New class to embaded column variables in one composite key*
 * */



//------------------------------------------------------------------------------------------------------------------