package com.kenure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="battery_life")
public class BatteryLife {

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="battery_life_id")
		private int batteryLifeId;
		
		@Column(name="number_of_child_nodes")
		private Integer numberOfChildNodes;
		
		@Column(name="estimated_battery_life_in_years")
		private Double estimatedBatteryLifeInYears;

		public int getBatteryLifeId() {
			return batteryLifeId;
		}

		public void setBatteryLifeId(int batteryLifeId) {
			this.batteryLifeId = batteryLifeId;
		}

		public Integer getNumberOfChildNodes() {
			return numberOfChildNodes;
		}

		public void setNumberOfChildNodes(Integer numberOfChildNodes) {
			this.numberOfChildNodes = numberOfChildNodes;
		}

		public Double getEstimatedBatteryLifeInYears() {
			return estimatedBatteryLifeInYears;
		}

		public void setEstimatedBatteryLifeInYears(Double estimatedBatteryLifeInYears) {
			this.estimatedBatteryLifeInYears = estimatedBatteryLifeInYears;
		}

}
