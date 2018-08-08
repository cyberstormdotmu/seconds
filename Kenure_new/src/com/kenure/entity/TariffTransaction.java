package com.kenure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="tariff_transaction")
public class TariffTransaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tariff_transaction_id")
	private int tariffTransactionId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tariff_plan_id")
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private TariffPlan tariffPlan;

	@Column(name="start_band")
	private Integer startBand;
	
	@Column(name="end_band")
	private Integer endBand;
 
	@Column(name="rate")
	private Double rate;

	public int getTariffTransactionId() {
		return tariffTransactionId;
	}

	public void setTariffTransactionId(int tariffTransactionId) {
		this.tariffTransactionId = tariffTransactionId;
	}

	public TariffPlan getTariffPlan() {
		return tariffPlan;
	}

	public void setTariffPlan(TariffPlan tariffPlan) {
		this.tariffPlan = tariffPlan;
	}

	public Integer getStartBand() {
		return startBand;
	}

	public void setStartBand(Integer startBand) {
		this.startBand = startBand;
	}

	public Integer getEndBand() {
		return endBand;
	}

	public void setEndBand(Integer endBand) {
		this.endBand = endBand;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

}

