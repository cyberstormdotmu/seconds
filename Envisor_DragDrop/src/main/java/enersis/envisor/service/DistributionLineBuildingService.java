package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBuilding;
import enersis.envisor.entity.Project;

public interface DistributionLineBuildingService {

	public List<Building> findAll();

	public void save(DistributionLineBuilding distributionLineBuilding);

	public void delete(DistributionLineBuilding distributionLineBuilding);
	
	public List<DistributionLineBuilding> findbyDistributionLine(DistributionLine distributionLine);
	
	public List<DistributionLineBuilding> findByBuilding(Building building);
	
	public List<DistributionLineBuilding> findByProject(Project project);
}
