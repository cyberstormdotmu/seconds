package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.entity.Wall;
import enersis.envisor.entity.Window;

public interface WindowService {

	public List<Window> findAll();
	
	public Window findById(int id);

	public void save(Window window);

	public void delete(Window window);
	
	public List<Window> findByProject(Project project);

	public List<Window> findbyWallWithDragDrop(Wall wall); //added by PCT25 - used for DragDrop functionalities

	public List<Window> findbyRoomWithDragDrop(Room room); //added by PCT25 - used for DragDrop functionalities

	public List<Window> findbyFlatWithDragDrop(Flat flat); //added by PCT25 - used for DragDrop functionalities

	public List<Window> findbyFloorWithDragDrop(Floor floor); //added by PCT25 - used for DragDrop functionalities

	public List<Window> findbyBuildingWithDragDrop(Building building); //added by PCT25 - used for DragDrop functionalities

	public List<Window> findByProjectWithDragDrop(Project project);	//added by PCT25 - used for DragDrop functionalities

	public List<Window> populateWindowsWithCriateria(Wall wall);	// Added by TatvaSoft, to populate Window entities for given wall using Criateria API.
}
