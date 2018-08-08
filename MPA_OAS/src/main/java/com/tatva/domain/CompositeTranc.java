package com.tatva.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class CompositeTranc implements Serializable{


	/*
	 COLUMN VARIABLE DECLARATION*
	 * */

	@Column(name = "REF_NO")
	private String referenceNo;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name = "TRANSACTION_SUB_TYPE")
	private String transactionSubType;


	
	
	
	/*
	 COLUMN getter setter starts*
	 * */
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionSubType() {
		return transactionSubType;
	}
	public void setTransactionSubType(String transactionSubType) {
		this.transactionSubType = transactionSubType;
	}

}