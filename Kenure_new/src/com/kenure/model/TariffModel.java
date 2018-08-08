package com.kenure.model;

import java.util.ArrayList;
import java.util.List;


public class TariffModel{

	public TariffModel(){
		
	}
	
	private List<TariffTransactionModel> tariffArray = new ArrayList<>();
	
	private String tariffName;

	private String tariffId;

	public List<TariffTransactionModel> getTariffArray() {
		return tariffArray;
	}

	public void setTariffArray(List<TariffTransactionModel> tariffArray) {
		this.tariffArray = tariffArray;
	}

	public String getTariffName() {
		return tariffName;
	}

	public void setTariffName(String tariffName) {
		this.tariffName = tariffName;
	}

	public String getTariffId() {
		return tariffId;
	}

	public void setTariffId(String id) {
		this.tariffId = id;
	}
	
}
