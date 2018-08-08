package com.kenure.model;

import java.util.List;

public class WhatIfTariffModel {

	private String regionName;
	
	private List<TariffTransactionModel> tariffModel;
	
	private String month;
	
	private Integer consumptionVar;
	
	private String siteName;
	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public List<TariffTransactionModel> getTariffModel() {
		return tariffModel;
	}

	public void setTariffModel(List<TariffTransactionModel> tariffModel) {
		this.tariffModel = tariffModel;
	}

	public Integer getConsumptionVar() {
		return consumptionVar;
	}

	public void setConsumptionVar(Integer consumptionVar) {
		this.consumptionVar = consumptionVar;
	}
	
}
