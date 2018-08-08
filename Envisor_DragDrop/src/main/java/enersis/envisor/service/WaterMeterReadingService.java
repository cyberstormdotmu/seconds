package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBuilding;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.Period;
import enersis.envisor.entity.WaterMeter;
import enersis.envisor.entity.WaterMeterReading;

public interface WaterMeterReadingService {
	
	public List<WaterMeterReading> findAll();

	public void save(WaterMeterReading waterMeterReading);

	public void delete(WaterMeterReading waterMeterReading);
	
	public List<WaterMeterReading> findbyWaterMeter(WaterMeter waterMeter);
	
	public WaterMeterReading findLastByWaterMeter(WaterMeter waterMeter);
	
	public List<WaterMeterReading> findByDistributionLine(DistributionLine distributionLine);
	
	public WaterMeterReading findByWaterMeterAndPeriod(WaterMeter waterMeter, Period period);
	
	public List<WaterMeterReading> findByPeriod(Period period);
	
	public List<WaterMeterReading> findByDistributionLineAndPeriod(DistributionLine distributionLine, Period period );
	
	public DistributionLine findDistributionLineOfReading(WaterMeterReading waterMeterReading, List<DistributionLineBuilding> distributionLineBuildings);

}
