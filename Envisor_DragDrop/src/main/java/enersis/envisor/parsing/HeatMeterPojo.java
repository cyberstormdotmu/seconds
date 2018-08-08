package enersis.envisor.parsing;

import java.util.Date;

public class HeatMeterPojo {

	private int heatMeterId;
	private Date date;
	private Double value;
	
	public HeatMeterPojo() {
		
	}

	public int getHeatMeterId() {
		return heatMeterId;
	}

	public void setHeatMeterId(int heatMeterId) {
		this.heatMeterId = heatMeterId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	
}
