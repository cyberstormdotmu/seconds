package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Window;

/**
 * 
 * @author TatvaSoft
 * Hibernate service class used to get and set data of Floor table.
 *
 */

public interface FloorService {

	public List<Floor> findAll();	// To get List of Floor entity available in database.

	public Floor findById(int id);	// To get Floor entity with given ID.
	
	public void save(Floor floor);	// To save Floor entity in database. 	

	public void delete(Floor floor);	// To delete Floor entity in database. (Its a soft delete, just make status field value=1) 
	
	public List<Floor> findbyBuilding(Building building);	// To get List of Floor entity with given Building entity.
	
	public List<Floor> findByFloorName(String name);	// To get List of Floor entity with given name.
	
	public List<Floor> findByProject(Project project);	// To get List of Floor entity with given Project entity.

	public List<Floor> findbyBuildingWithDragDrop(Building building); 	// To get List of Floor entity with given Building entity, used for DragDrop functionalities.

	public List<Floor> findByProjectWithDragDrop(Project project);		// To get List of Floor entity with given Project entity, used for DragDrop functionalities.
	
	public Floor populateFlats(Floor floor);	// To populate Floor entity with list of Flats.(List of Flats are lazy loading)
	
	public List<Floor> populateFloorsWithCriateria(Building building);	// Added by TatvaSoft, to populate Floor entities for given building using Criateria API.
}
