package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBuilding;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;

public interface HeatCostAllocatorService {
	public List<HeatCostAllocator> findAll();

	public void save(HeatCostAllocator heatCostAllocator);

	public void delete(HeatCostAllocator heatCostAllocator);
	
	public List<HeatCostAllocator> findbyRoom(Room room);
	
	public List<HeatCostAllocator> findBySerialNo(String serialNo);
	
	public List<HeatCostAllocator> findByDistributionLine(DistributionLine distributionLine);
	
	public List<HeatCostAllocator> findByFlat(Flat flat);
	
	public List<HeatCostAllocator> findByProject(Project project);
	
	public List<HeatCostAllocator> findByBuilding(Building building);
	
	public DistributionLine findDistributionLineOfReading(HeatCostAllocatorReading heatCostAllocatorReading, List<DistributionLineBuilding> distributionLineBuildings);
}
