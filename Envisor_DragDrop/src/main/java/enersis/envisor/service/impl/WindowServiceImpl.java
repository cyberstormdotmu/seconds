package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.entity.Wall;
import enersis.envisor.entity.Window;
import enersis.envisor.service.WindowService;

@Service("windowService")
@Transactional
public class WindowServiceImpl implements WindowService {

	public static final String getWindowsQuery = "from Window w where w.status=0";
	public static final String findByNo ="from Window w where w.no =:no and w.status = 0";
	public static final String findByProject = "";
	
	//following lines added by PCT25 - used for DragDrop functionalities to get Window available with given given ID
	public static final String FIND_BY_ID ="from Window w where w.id=:id and w.status =0";
	
	//following lines added by PCT25 - used for DragDrop functionalities to get Window available with given wallID
	public static final String FIND_BY_WALL_WITH_DRAG_DROP = "from Window w where w.wall =:wall and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Window available with given roomID
	public static final String FIND_BY_ROOM_WITH_DRAG_DROP = "from Window w where w.wall in ( "
			+ "select distinct  wall from Wall w where w.room=:room"
			+ ") and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Window available with given flatID
	public static final String FIND_BY_FLAT_WITH_DRAG_DROP = "from Window w where w.wall in ( "
			+ "select distinct  wall from Wall w where w.room in ( "
			+ "select distinct room from Room r where r.flat=:flat "
			+ " ) ) and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Window available with given floorID
	public static final String FIND_BY_FLOOR_WITH_DRAG_DROP = "from Window w where w.wall in ( "
			+ "select distinct  wall from Wall w where w.room in ( "
			+ "select distinct room from Room r where r.flat in ( "
			+ "select distinct flat from Flat f where f.floor=:floor "
			+ " ) ) ) and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Window available with given buildingID
	public static final String FIND_BY_BUILDING_WITH_DRAG_DROP = "from  Window w where w.wall in ( "
			+ "select distinct  wall from Wall w where w.room in ( "
			+ "select distinct room from Room r where r.flat in ( "
			+ "select distinct flat from Flat f where f.floor in ( "
			+ "select distinct floor from Floor f where f.building=:building "
			+ " ) ) ) ) and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Window available with given projectID
	public static final String FIND_BY_PROJECT_WITH_DRAG_DROP = "from  Window w where w.wall in ( "
			+ "select distinct  wall from Wall w where w.room in ( "
			+ "select distinct room from Room r where r.flat in ( "
			+ "select distinct flat from Flat f where f.floor in ( "
			+ "select distinct floor from Floor f where f.building in ( "
			+ "select distinct building from Building b where b.project=:project"
			+ " ) ) ) ) and w.status =0";

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Window> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getWindowsQuery).list();
	}

	@Override
	public Window findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (Window) query.list().get(0);
	}

	@Override
	public void save(Window window) {
		sessionFactory.getCurrentSession().saveOrUpdate(window);
	}

	@Override
	public void delete(Window window) {
		window.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(window);
	}

	@Override
	public List<Window> findByProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Window> findbyWallWithDragDrop(Wall wall) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_WALL_WITH_DRAG_DROP);
		query.setParameter("wall", wall);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Window> findbyRoomWithDragDrop(Room room) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("room", room);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Window> findbyFlatWithDragDrop(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLAT_WITH_DRAG_DROP);
		query.setParameter("flat", flat);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Window> findbyFloorWithDragDrop(Floor floor) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLOOR_WITH_DRAG_DROP);
		query.setParameter("floor", floor);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Window> findbyBuildingWithDragDrop(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("building", building);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Window> findByProjectWithDragDrop(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("project", project);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to populate Window entities for given wall using Criateria API.
	 * It will only give window which has status=0 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Window> populateWindowsWithCriateria(Wall wall) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Window.class);
		criteria.add(Restrictions.eq("wall", wall));
		criteria.add(Restrictions.eq("status", (byte) 0));
		List<Window> windows = criteria.list();
		return windows;
	}
}
