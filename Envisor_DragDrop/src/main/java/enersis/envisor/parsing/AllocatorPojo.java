package enersis.envisor.parsing;

import java.util.Date;

import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.Room;

public class AllocatorPojo {

	private int heatCostAllocatorId;
	private Date date;
	private int value;
	
	
	
	
	public AllocatorPojo() {
	}
	public int getHeatCostAllocatorId() {
		return heatCostAllocatorId;
	}
	public void setHeatCostAllocatorId(int heatCostAllocatorId) {
		this.heatCostAllocatorId = heatCostAllocatorId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
