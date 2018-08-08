package enersis.envisor.utils;

public class EnergyHeader {

	String allocationType;
	Double energy;
	Double priceBySection;
	Double valueBySection;
	String unit;
	Double unitPrice;
	
	
	
	public EnergyHeader() {
	}
	
	public String getAllocationType() {
		return allocationType;
	}
	public void setAllocationType(String allocationType) {
		this.allocationType = allocationType;
	}
	public Double getEnergy() {
		return energy;
	}
	public void setEnergy(Double energy) {
		this.energy = energy;
	}
	public Double getPriceBySection() {
		return priceBySection;
	}
	public void setPriceBySection(Double priceBySection) {
		this.priceBySection = priceBySection;
	}
	public Double getValueBySection() {
		return valueBySection;
	}
	public void setValueBySection(Double valueBySection) {
		this.valueBySection = valueBySection;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
}
