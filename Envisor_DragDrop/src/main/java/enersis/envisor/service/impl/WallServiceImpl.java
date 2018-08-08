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
import enersis.envisor.service.WallService;

@Service("wallService")
@Transactional
public class WallServiceImpl implements WallService {

	public static final String getWallsQuery = "from Wall w where w.status=0";
	public static final String findByNo ="from Wall w where w.no =:no and w.status = 0";
	public static final String findByProject = "";
	
	//following lines added by PCT25 - used for DragDrop functionalities to get Wall available with given given ID
	public static final String FIND_BY_ID ="from Wall w where w.id=:id and w.status =0";
	
	//following lines added by PCT25 - used for DragDrop functionalities to get Wall available with given roomID
	public static final String FIND_BY_ROOM_WITH_DRAG_DROP = "from Wall w where w.room =:room and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Wall available with given flatID
	public static final String FIND_BY_FLAT_WITH_DRAG_DROP = "from Wall w where w.room in ( "
			+ "select distinct  room from Room r where r.flat=:flat "
			+ " ) and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Wall available with given floorID
	public static final String FIND_BY_FLOOR_WITH_DRAG_DROP = "from Wall w where w.room in ( "
			+ "select distinct  room from Room r where r.flat in ( "
			+ "select distinct flat from Flat f where f.floor=:floor "
			+ " ) ) and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Wall available with given buildingID
	public static final String FIND_BY_BUILDING_WITH_DRAG_DROP = "from  Wall w where w.room in ( "
			+ "select distinct  room from Room r where r.flat in ( "
			+ "select distinct  flat from Flat f where f.floor in ( "
			+ "select distinct floor from Floor f where f.building=:building "
			+ " ) ) ) and w.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Room available with given projectID
	public static final String FIND_BY_PROJECT_WITH_DRAG_DROP = "from  Wall w where w.room in ( "
			+ "select distinct  room from Room r where r.flat in ( "
			+ "select distinct  flat from Flat f where f.floor in ( "
			+ "select distinct floor from Floor f where f.building in ( "
			+ "select distinct building from Building b where b.project=:project"
			+ " ) ) ) ) and w.status =0";

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Wall> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getWallsQuery).list();
	}

	@Override
	public Wall findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (Wall) query.list().get(0);
	}

	@Override
	public void save(Wall wall) {
		sessionFactory.getCurrentSession().saveOrUpdate(wall);
	}

	@Override
	public void delete(Wall wall) {
		wall.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(wall);
	}

	@Override
	public List<Wall> findByProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Wall> findbyRoomWithDragDrop(Room room) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("room", room);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Wall> findbyFlatWithDragDrop(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLAT_WITH_DRAG_DROP);
		query.setParameter("flat", flat);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Wall> findbyFloorWithDragDrop(Floor floor) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLOOR_WITH_DRAG_DROP);
		query.setParameter("floor", floor);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Wall> findbyBuildingWithDragDrop(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("building", building);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Wall> findByProjectWithDragDrop(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("project", project);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to populate Wall entity with list of Doors and Windows.(List of Doors and Windows are lazy loading)
	 */
	@Override
	public Wall populateDoorWindow(Wall wall) {
		Wall wall1 = (Wall) sessionFactory.getCurrentSession().merge(wall);
		int door_size = wall1.getDoors().size();
		int window_size = wall1.getWindows().size();
		return wall1;
	}

	/**
	 * Added by TatvaSoft, to populate Wall entities for given room using Criateria API.
	 * It will only give wall which has status=0 
	 */
	@Override
	public List<Wall> populateWallsWithCriateria(Room room) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Wall.class);
		criteria.add(Restrictions.eq("room", room));
		criteria.add(Restrictions.eq("status", (byte) 0));
		List<Wall> walls = criteria.list();
		return walls;
	}

}
