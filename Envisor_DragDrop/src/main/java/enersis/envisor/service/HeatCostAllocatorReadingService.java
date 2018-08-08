package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.Period;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;

public interface HeatCostAllocatorReadingService {

	public List<HeatCostAllocatorReading> findAll();

	public void save(HeatCostAllocatorReading heatCostAllocatorReading);

	public void delete(HeatCostAllocatorReading heatCostAllocatorReading);
	
	public List<HeatCostAllocatorReading> findbyAllocator(HeatCostAllocator allocator);
	
	public HeatCostAllocatorReading findLastByAllocator(HeatCostAllocator allocator);
	
	public List<HeatCostAllocatorReading> findByDistributionLine(DistributionLine distributionLine);
	
	public HeatCostAllocatorReading findByAllocatorAndPeriod(HeatCostAllocator heatCostAllocator, Period period);
	
	public List<HeatCostAllocatorReading> findByPeriod(Period period);
	
	public List<HeatCostAllocatorReading> findByDistributionLineAndPeriod(DistributionLine distributionLine, Period period );
	
	public List<HeatCostAllocatorReading> findByBuildingAndPeriod(Building building, Period period);
	
//	public List<HeatCostAllocatorReading> findByProjectAndPeriod(Project project,Period period);
	
}
