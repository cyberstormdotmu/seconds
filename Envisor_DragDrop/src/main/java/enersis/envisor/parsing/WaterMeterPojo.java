package enersis.envisor.parsing;

import java.util.Date;

public class WaterMeterPojo {
	
	private int waterMeterId;
	private Date date;
	private Double value;
	
	public WaterMeterPojo() {
	}

	public int getWaterMeterId() {
		return waterMeterId;
	}

	public void setWaterMeterId(int waterMeterId) {
		this.waterMeterId = waterMeterId;
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
