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
import enersis.envisor.entity.Door;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.entity.Wall;
import enersis.envisor.entity.Window;
import enersis.envisor.service.DoorService;

@Service("doorService")
@Transactional
public class DoorServiceImpl implements DoorService {

	public static final String getDoorsQuery = "from Door d where d.status=0";
	public static final String findByNo ="from Door d where d.no =:no and d.status = 0";
	public static final String findByFlat = "";
	public static final String findByProject = "";
	
	//following lines added by PCT25 - used for DragDrop functionalities to get Door available with given given ID
	public static final String FIND_BY_ID ="from Door d where d.id=:id and d.status =0";
	
	//following lines added by PCT25 - used for DragDrop functionalities to get Door available with given wallID
	public static final String FIND_BY_WALL_WITH_DRAG_DROP = "from Door d where d.wall =:wall and d.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Door available with given roomID
	public static final String FIND_BY_ROOM_WITH_DRAG_DROP = "from Door d where d.wall in ( "
			+ "select distinct  wall from Wall w where w.room=:room"
			+ ") and d.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Door available with given flatID
	public static final String FIND_BY_FLAT_WITH_DRAG_DROP = "from Door d where d.wall in ( "
			+ "select distinct  wall from Wall w where w.room in ( "
			+ "select distinct room from Room r where r.flat=:flat "
			+ " ) ) and d.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Door available with given floorID
	public static final String FIND_BY_FLOOR_WITH_DRAG_DROP = "from Door d where d.wall in ( "
			+ "select distinct  wall from Wall w where w.room in ( "
			+ "select distinct room from Room r where r.flat in ( "
			+ "select distinct flat from Flat f where f.floor=:floor "
			+ " ) ) ) and d.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Door available with given buildingID
	public static final String FIND_BY_BUILDING_WITH_DRAG_DROP = "from  Door d where d.wall in ( "
			+ "select distinct  wall from Wall w where w.room in ( "
			+ "select distinct room from Room r where r.flat in ( "
			+ "select distinct flat from Flat f where f.floor in ( "
			+ "select distinct floor from Floor f where f.building=:building "
			+ " ) ) ) ) and d.status =0";

	//following lines added by PCT25 - used for DragDrop functionalities to get Door available with given projectID
	public static final String FIND_BY_PROJECT_WITH_DRAG_DROP = "from  Door d where d.wall in ( "
			+ "select distinct  wall from Wall w where w.room in ( "
			+ "select distinct room from Room r where r.flat in ( "
			+ "select distinct flat from Flat f where f.floor in ( "
			+ "select distinct floor from Floor f where f.building in ( "
			+ "select distinct building from Building b where b.project=:project"
			+ " ) ) ) ) and d.status =0";

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Door> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getDoorsQuery).list();
	}

	@Override
	public Door findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (Door) query.list().get(0);
	}

	@Override
	public void save(Door door) {
		sessionFactory.getCurrentSession().saveOrUpdate(door);
	}

	@Override
	public void delete(Door door) {
		door.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(door);
	}

	@Override
	public List<Door> findByProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Door> findbyWallWithDragDrop(Wall wall) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_WALL_WITH_DRAG_DROP);
		query.setParameter("wall", wall);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Door> findbyRoomWithDragDrop(Room room) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("room", room);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Door> findbyFlatWithDragDrop(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLAT_WITH_DRAG_DROP);
		query.setParameter("flat", flat);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Door> findbyFloorWithDragDrop(Floor floor) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLOOR_WITH_DRAG_DROP);
		query.setParameter("floor", floor);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Door> findbyBuildingWithDragDrop(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("building", building);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Door> findByProjectWithDragDrop(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_ROOM_WITH_DRAG_DROP);
		query.setParameter("project", project);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to populate Door entities for given wall using Criateria API.
	 * It will only give door which has status=0 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Door> populateDoorsWithCriateria(Wall wall) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Door.class);
		criteria.add(Restrictions.eq("wall", wall));
		criteria.add(Restrictions.eq("status", (byte) 0));
		List<Door> doors = criteria.list();
		return doors;
	}

}
