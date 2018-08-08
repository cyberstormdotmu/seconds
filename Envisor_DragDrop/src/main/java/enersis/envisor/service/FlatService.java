package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;

public interface FlatService {
	
	public List<Flat> findAll();

	public Flat findById(int id);
	
	public void save(Flat flat);

	public void delete(Flat flat);
	
	public List<Flat> findbyBuilding(Building building);
	
	public List<Flat> findByFlatNo(byte no);
	
	public List<Flat> findByDistributionLine(DistributionLine distributionLine);
	
	public List<Flat> findByProject(Project project);

	public List<Flat> findByFlatName(String name); // Added by TatvaSoft, to get List of Flats entity with given name.

	public List<Flat> findbyFloorWithDragDrop(Floor floor); // Added by TatvaSoft, to get List of Flats entity with given Floor entity, used for DragDrop functionalities.

	public List<Flat> findbyBuildingWithDragDrop(Building building); // Added by TatvaSoft, to get List of Flats entity with given Building, used for DragDrop functionalities.

	public List<Flat> findByProjectWithDragDrop(Project project);	// Added by TatvaSoft, to get List of Flats entity with given Project entity, used for DragDrop functionalities.

	public Flat populateRooms(Flat flat);	// Added by TatvaSoft, to populate Flat entity with list of Rooms.(List of Rooms are lazy loading)

	public List<Flat> populateFlatsWithCriateria(Floor floor);	// Added by TatvaSoft, to populate Flat entities for given floor using Criateria API.
}

