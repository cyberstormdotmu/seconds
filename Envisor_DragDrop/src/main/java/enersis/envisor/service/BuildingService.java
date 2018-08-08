package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Project;

public interface BuildingService {
	
	public List<Building> findAll();

	public Building findById(int id);	// Added by TatvaSoft, to get Building entity with given ID.
	
	public void save(Building building );

	public void delete(Building building);
	
	public List<Building> findbyDistributionLine(DistributionLine distributionLine);
	
	public List<Building> findByBuildingName(String name);
	
	public List<Building> findByProject(Project project);
	
	public List<Building> findByProjectWithDragDrop(Project project);	// Added by TatvaSoft, to get List of Buildings entity with given Project entity, used for DragDrop functionalities.
	
	public Building populateFloors(Building building);	// Added by TatvaSoft, to populate Building entity with list of Floors.(List of Floors are lazy loading)
	
	public List<Building> populateBuildingsWithCriateria(Project project);	// Added by TatvaSoft, to populate Building entities for given project using Criateria API.

}
