package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;

public interface RoomService {

	public List<Room> findAll();
	
	public Room findById(int id);

	public void save(Room room );

	public void delete(Room room);
	
	public List<Room> findbyFlat(Flat flat);
	
	public List<Room> findByRoomOrderNo(Byte orderNo);
	
	public List<Room> findByProject(Project project);

	public List<Room> findByRoomName(String name); // Added by TatvaSoft, to get List of Room entity with given name.

	public List<Room> findbyFlatWithDragDrop(Flat flat); // Added by TatvaSoft, to get List of Room entity with given Flat entity, used for DragDrop functionalities.

	public List<Room> findbyFloorWithDragDrop(Floor floor); // Added by TatvaSoft, to get List of Room entity with given Floor entity, used for DragDrop functionalities.

	public List<Room> findbyBuildingWithDragDrop(Building building); // Added by TatvaSoft, to get List of Room entity with given Building, used for DragDrop functionalities.

	public List<Room> findByProjectWithDragDrop(Project project);	// Added by TatvaSoft, to get List of Room entity with given Project entity, used for DragDrop functionalities.
	
	public Room populateWall(Room room);	// Added by TatvaSoft, to populate Room entity with list of Walls.(List of Walls are lazy loading)
	
	public List<Room> populateRoomsWithCriateria(Flat flat);	// Added by TatvaSoft, to populate Room entities for given flat using Criateria API.
}
