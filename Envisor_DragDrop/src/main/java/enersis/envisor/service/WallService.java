package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.entity.Wall;

public interface WallService {

	public List<Wall> findAll();
	
	public Wall findById(int id);

	public void save(Wall wall );

	public void delete(Wall wall);
		
	public List<Wall> findByProject(Project project);

	public List<Wall> findbyRoomWithDragDrop(Room room); //added by PCT25 - used for DragDrop functionalities

	public List<Wall> findbyFlatWithDragDrop(Flat flat); //added by PCT25 - used for DragDrop functionalities

	public List<Wall> findbyFloorWithDragDrop(Floor floor); //added by PCT25 - used for DragDrop functionalities

	public List<Wall> findbyBuildingWithDragDrop(Building building); //added by PCT25 - used for DragDrop functionalities

	public List<Wall> findByProjectWithDragDrop(Project project);	//added by PCT25 - used for DragDrop functionalities

	public Wall populateDoorWindow(Wall wall);	// Added by TatvaSoft, to populate Wall entity with list of Doors and Windows.(List of Doors and Windows are lazy loading)
	
	public List<Wall> populateWallsWithCriateria(Room room);	// Added by TatvaSoft, to populate Wall entities for given room using Criateria API.
}
