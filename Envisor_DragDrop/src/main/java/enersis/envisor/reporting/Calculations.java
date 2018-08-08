package enersis.envisor.reporting;

import java.util.List;

import enersis.envisor.entity.Bill;

public class Calculations {
	
	
	//pay ölçer deðiþkenleri
	private Double energyUsageOfMeters;
	private Double energyUsageOfCommonAreas;
	private Double energyChargeOfMeters;
	private Double energyChargeOfCommonAreas;
	private Double totalConsumptionOfMeters;
	private Double totalConsumptionOfCommonAreas;
	private String unitOfMeters;
	private String unitOfCommonAreas;
	private Double unitValueOfMeters;
	private Double unitValueOfCommonAreas;
	//sýcak su sayacý deðiþkenleri
//	private Double energyUsageOfMeters;
//	private Double energyUsageOfCommonAreas;
//	private Double energyChargeOfMeters;
//	private Double energyChargeOfCommonAreas;
//	private Double totalConsumptionOfMeters;
//	private Double totalConsumptionOfCommonAreas;
//	private String unitOfMeters;
//	private String unitOfCommonAreas;
//	private Double unitValueOfMeters;
//	private Double unitValueOfCommonAreas;
	private Bill bill;
	public Integer TYPE;
	
	
	
	
	
//	private List<Bill> bills;
//	public Integer TYPEOFDISTRIBUTIONLINE =999999999;
//	public Integer COMBINATIONOFDISTRIBUTIONLINE;
	
	
	
	public Calculations() {
//		this.TYPEOFDISTRIBUTIONLINE=99999;
	}

	public Calculations(Double energyUsageOfMeters, Double energyUsageOfCommonAreas, Double energyChargeOfMeters, Double energyChargeOfCommonAreas, Double totalConsumptionOfMeters,
			Double totalConsumptionOfCommonAreas, String unitOfMeters, String unitOfCommonAreas, Double unitValueOfMeters, Double unitValueOfCommonAreas) {
		super();
		this.energyUsageOfMeters = energyUsageOfMeters;
		this.energyUsageOfCommonAreas = energyUsageOfCommonAreas;
		this.energyChargeOfMeters = energyChargeOfMeters;
		this.energyChargeOfCommonAreas = energyChargeOfCommonAreas;
		this.totalConsumptionOfMeters = totalConsumptionOfMeters;
		this.totalConsumptionOfCommonAreas = totalConsumptionOfCommonAreas;
		this.unitOfMeters = unitOfMeters;
		this.unitOfCommonAreas = unitOfCommonAreas;
		this.unitValueOfMeters = unitValueOfMeters;
		this.unitValueOfCommonAreas = unitValueOfCommonAreas;
	}

	public Double getEnergyUsageOfMeters() {
		return energyUsageOfMeters;
	}

	public void setEnergyUsageOfMeters(Double energyUsageOfMeters) {
		this.energyUsageOfMeters = energyUsageOfMeters;
	}

	public Double getEnergyUsageOfCommonAreas() {
		return energyUsageOfCommonAreas;
	}

	public void setEnergyUsageOfCommonAreas(Double energyUsageOfCommonAreas) {
		this.energyUsageOfCommonAreas = energyUsageOfCommonAreas;
	}

	public Double getEnergyChargeOfMeters() {
		return energyChargeOfMeters;
	}

	public void setEnergyChargeOfMeters(Double energyChargeOfMeters) {
		this.energyChargeOfMeters = energyChargeOfMeters;
	}

	public Double getEnergyChargeOfCommonAreas() {
		return energyChargeOfCommonAreas;
	}

	public void setEnergyChargeOfCommonAreas(Double energyChargeOfCommonAreas) {
		this.energyChargeOfCommonAreas = energyChargeOfCommonAreas;
	}

	public Double getTotalConsumptionOfMeters() {
		return totalConsumptionOfMeters;
	}

	public void setTotalConsumptionOfMeters(Double totalConsumptionOfMeters) {
		this.totalConsumptionOfMeters = totalConsumptionOfMeters;
	}

	public Double getTotalConsumptionOfCommonAreas() {
		return totalConsumptionOfCommonAreas;
	}

	public void setTotalConsumptionOfCommonAreas(Double totalConsumptionOfCommonAreas) {
		this.totalConsumptionOfCommonAreas = totalConsumptionOfCommonAreas;
	}

	public String getUnitOfMeters() {
		return unitOfMeters;
	}

	public void setUnitOfMeters(String unitOfMeters) {
		this.unitOfMeters = unitOfMeters;
	}

	public String getUnitOfCommonAreas() {
		return unitOfCommonAreas;
	}

	public void setUnitOfCommonAreas(String unitOfCommonAreas) {
		this.unitOfCommonAreas = unitOfCommonAreas;
	}

	public Double getUnitValueOfMeters() {
		return unitValueOfMeters;
	}

	public void setUnitValueOfMeters(Double unitValueOfMeters) {
		this.unitValueOfMeters = unitValueOfMeters;
	}

	public Double getUnitValueOfCommonAreas() {
		return unitValueOfCommonAreas;
	}

	public void setUnitValueOfCommonAreas(Double unitValueOfCommonAreas) {
		this.unitValueOfCommonAreas = unitValueOfCommonAreas;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

//	public List<Bill> getBills() {
//		return bills;
//	}
//
//	public void setBills(List<Bill> bills) {
//		this.bills = bills;
//	}
	
	
	
}
