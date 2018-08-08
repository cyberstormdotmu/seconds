package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.Door;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.entity.Wall;

public interface DoorService {

	public List<Door> findAll();
	
	public Door findById(int id);

	public void save(Door door);

	public void delete(Door door);
	
	public List<Door> findByProject(Project project);

	public List<Door> findbyWallWithDragDrop(Wall wall); //added by PCT25 - used for DragDrop functionalities

	public List<Door> findbyRoomWithDragDrop(Room room); //added by PCT25 - used for DragDrop functionalities

	public List<Door> findbyFlatWithDragDrop(Flat flat); //added by PCT25 - used for DragDrop functionalities

	public List<Door> findbyFloorWithDragDrop(Floor floor); //added by PCT25 - used for DragDrop functionalities

	public List<Door> findbyBuildingWithDragDrop(Building building); //added by PCT25 - used for DragDrop functionalities

	public List<Door> findByProjectWithDragDrop(Project project);	//added by PCT25 - used for DragDrop functionalities

	public List<Door> populateDoorsWithCriateria(Wall wall);	// Added by TatvaSoft, to populate Door entities for given wall using Criateria API.
	
}
