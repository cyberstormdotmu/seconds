package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.HeatMeter;
import enersis.envisor.entity.HeatMeterReading;
import enersis.envisor.entity.Period;

public interface HeatMeterReadingService {

	public List<HeatMeterReading> findAll();

	public void save(HeatMeterReading heatMeterReading);

	public void delete(HeatMeterReading heatMeterReading);
	
	public List<HeatMeterReading> findbyHeatMeter(HeatMeter heatMeter);
	
	public HeatMeterReading findLastByHeatMeter(HeatMeter heatMeter);
	
	public List<HeatMeterReading> findByDistributionLine(DistributionLine distributionLine);
	
	public HeatMeterReading findByHeatMeterAndPeriod(HeatMeter heatMeter, Period period);
	
	public List<HeatMeterReading> findByPeriod(Period period);
	
	public List<HeatMeterReading> findByDistributionLineAndPeriod(DistributionLine distributionLine, Period period );
}
