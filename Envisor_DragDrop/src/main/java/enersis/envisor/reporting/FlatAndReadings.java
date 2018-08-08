package enersis.envisor.reporting;

import java.util.ArrayList;
import java.util.List;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.HeatMeterReading;
import enersis.envisor.entity.WaterMeterReading;

public class FlatAndReadings {
	private DistributionLine distributionLine;
	private Flat flat;
	private List<HeatCostAllocatorReading> heatCostAllocatorReadings;
	private WaterMeterReading waterMeterReading;
	private HeatMeterReading heatMeterReading;
	private Double totalAllocator=0.0;
	private Double totalWater;
	private Double totalHeat;
	
	
	
	public FlatAndReadings()
	{
		
	}
	
	public FlatAndReadings(DistributionLine distributionLine,Flat flat, List<HeatCostAllocatorReading> heatCostAllocatorReadings, WaterMeterReading waterMeterReading, HeatMeterReading heatMeterReading) {
		super();
		this.distributionLine= distributionLine;
		this.flat = flat;
		this.heatCostAllocatorReadings = heatCostAllocatorReadings;
		this.waterMeterReading = waterMeterReading;
		this.heatMeterReading = heatMeterReading;
	}
	
	public FlatAndReadings(DistributionLine distributionLine,Flat flat, List<HeatCostAllocatorReading> heatCostAllocatorReadings) {
		super();
		this.distributionLine = distributionLine;
		this.flat = flat;
		this.heatCostAllocatorReadings = heatCostAllocatorReadings;
		this.waterMeterReading = new WaterMeterReading();
		this.heatMeterReading= new HeatMeterReading();
	}
	
	public FlatAndReadings(DistributionLine distributionLine,Flat flat, HeatMeterReading heatMeterReading) {
		super();
		this.distributionLine= distributionLine;
		this.flat = flat;
		this.heatCostAllocatorReadings = new ArrayList<HeatCostAllocatorReading>();
		this.waterMeterReading = new WaterMeterReading();
		this.heatMeterReading= heatMeterReading;
	}
	
	public FlatAndReadings(DistributionLine distributionLine,Flat flat, WaterMeterReading waterMeterReading) {
		super();
		this.distributionLine = distributionLine;
		this.flat = flat;
		this.heatCostAllocatorReadings = new ArrayList<HeatCostAllocatorReading>();
		this.waterMeterReading = waterMeterReading;
		this.heatMeterReading= new HeatMeterReading();
	}

	public Flat getFlat() {
		return flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	public List<HeatCostAllocatorReading> getHeatCostAllocatorReadings() {
		return heatCostAllocatorReadings;
	}

	public void setHeatCostAllocatorReadings(List<HeatCostAllocatorReading> heatCostAllocatorReadings) {
		this.heatCostAllocatorReadings = heatCostAllocatorReadings;
	}

	public WaterMeterReading getWaterMeterReading() {
		return waterMeterReading;
	}

	public void setWaterMeterReading(WaterMeterReading waterMeterReading) {
		this.waterMeterReading = waterMeterReading;
	}

	public HeatMeterReading getHeatMeterReading() {
		return heatMeterReading;
	}

	public void setHeatMeterReading(HeatMeterReading heatMeterReading) {
		this.heatMeterReading = heatMeterReading;
	}

	public DistributionLine getDistributionLine() {
		return distributionLine;
	}

	public void setDistributionLine(DistributionLine distributionLine) {
		this.distributionLine = distributionLine;
	}

	public Double getTotalAllocator() {
		return totalAllocator;
	}

	public void setTotalAllocator(Double totalAllocator) {
		this.totalAllocator = totalAllocator;
	}

	public Double getTotalWater() {
		return totalWater;
	}

	public void setTotalWater(Double totalWater) {
		this.totalWater = totalWater;
	}

	public Double getTotalHeat() {
		return totalHeat;
	}

	public void setTotalHeat(Double totalHeat) {
		this.totalHeat = totalHeat;
	}
	
	
	
	

	
}
