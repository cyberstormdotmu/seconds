package enersis.envisor.entityOld;

// Generated Feb 3, 2015 10:24:48 AM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * UtilityType generated by hbm2java
 */
@Entity
@Table(name = "UtilityType")
public class UtilityType implements java.io.Serializable {

	private byte id;
	private String name;
	private String unit;
	private BigDecimal unitPrice;
	private BigDecimal efficiency;
	private Set<MeasurementTypeUtilityType> measurementTypeUtilityTypes = new HashSet<MeasurementTypeUtilityType>(
			0);

	public UtilityType() {
	}

	public UtilityType(byte id, String name, String unit) {
		this.id = id;
		this.name = name;
		this.unit = unit;
	}

	public UtilityType(byte id, String name, String unit, BigDecimal unitPrice,
			BigDecimal efficiency,
			Set<MeasurementTypeUtilityType> measurementTypeUtilityTypes) {
		this.id = id;
		this.name = name;
		this.unit = unit;
		this.unitPrice = unitPrice;
		this.efficiency = efficiency;
		this.measurementTypeUtilityTypes = measurementTypeUtilityTypes;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 15)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "unit", nullable = false, length = 10)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "unitPrice", precision = 13)
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "efficiency", precision = 4)
	public BigDecimal getEfficiency() {
		return this.efficiency;
	}

	public void setEfficiency(BigDecimal efficiency) {
		this.efficiency = efficiency;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "utilityType")
	public Set<MeasurementTypeUtilityType> getMeasurementTypeUtilityTypes() {
		return this.measurementTypeUtilityTypes;
	}

	public void setMeasurementTypeUtilityTypes(
			Set<MeasurementTypeUtilityType> measurementTypeUtilityTypes) {
		this.measurementTypeUtilityTypes = measurementTypeUtilityTypes;
	}

}
