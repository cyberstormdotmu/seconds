package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBuilding;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.HeatMeter;
import enersis.envisor.entity.HeatMeterReading;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;

public interface HeatMeterService {

	public List<HeatMeter> findAll();

	public void save(HeatMeter heatMeter);

	public void delete(HeatMeter heatMeter);
	
	public List<HeatMeter> findBySerialNo(String serialNo);
	
	public List<HeatMeter> findByDistributionLine(DistributionLine distributionLine);
	
	public List<HeatMeter> findByFlat(Flat flat);
	
	public List<HeatMeter> findByProject(Project project);
	
	public DistributionLine findDistributionLineOfReading(HeatMeterReading heatMeterReading, List<DistributionLineBuilding> distributionLineBuildings);
	
}
