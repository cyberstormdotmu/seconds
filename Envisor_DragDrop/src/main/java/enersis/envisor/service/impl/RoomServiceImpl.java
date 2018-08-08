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
import enersis.envisor.service.RoomService;

@Service("roomService")
@Transactional
public class RoomServiceImpl implements RoomService {

	
	public static final String getRoomsQuery = "from Room u where u.status=0";
	public static final String findByFlat = "from Room u where u.flat =:flat and u.status =0";
	public static final String findByOrderNo ="from Room u where u.orderNo =:orderNo and u.status = 0";
	public static final String findByName ="from Room u where u.name =:name and u.status = 0";		// Added by TatvaSoft, to get List of Room entity with given name.
	public static final String findById ="from Room u where u.id=:id and u.status =0";
	
	public static final String findByProject = " from Room as room where room.flat in ( from  Flat f where f.building in ( "
			+ "select distinct  building from DistributionLineBuilding d where d.distributionLine in ( "
			+ " from DistributionLine  d where d.project =:project )))";
	
	// Added by TatvaSoft, to get List of Room entity with given Flat entity, used for DragDrop functionalities.
	public static final String FIND_BY_FLAT_WITH_DRAG_DROP = "from Room r where r.flat =:flat and r.status =0";

	// Added by TatvaSoft, to get List of Room entity with given Floor entity, used for DragDrop functionalities.
	public static final String FIND_BY_FLOOR_WITH_DRAG_DROP = "from Room r where f.flat in ( "
			+ "select distinct  flat from Flat f where f.floor =:floor"
			+ " ) and r.status = 0";

	// Added by TatvaSoft, to get List of Room entity with given Project Building, used for DragDrop functionalities.
	public static final String FIND_BY_BUILDING_WITH_DRAG_DROP = "from  Room r where f.flat in ( "
			+ "select distinct  flat from Flat f where f.floor in ( "
			+ "select distinct  floor from Floor f where f.building =:building"
			+ " ) ) and r.status = 0";

	// Added by TatvaSoft, to get List of Room entity with given Project entity, used for DragDrop functionalities.
	public static final String FIND_BY_PROJECT_WITH_DRAG_DROP = "from  Room r where f.flat in ( "
			+ "select distinct  flat from Flat f where f.floor in ( "
			+ "select distinct  floor from Floor f where f.building in ( "
			+ "select distinct  building from Building b where b.project =:project"
			+ " ) ) ) and r.status = 0";

	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<Room> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getRoomsQuery).list();
	}

	@Override
	public void save(Room room) {
		sessionFactory.getCurrentSession().saveOrUpdate(room);
		
	}

	@Override
	public void delete(Room room) {
		room.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(room);
		
	}

	@Override
	public List<Room> findbyFlat(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByFlat);
		query.setParameter("flat", flat);
		return query.list();
	}

	@Override
	public List<Room> findByRoomOrderNo(Byte orderNo) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByOrderNo);
		query.setParameter("orderNo", orderNo);
		return query.list();
	}

	@Override
	public Room findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(findById);
		query.setParameter("id", id);
		return (Room) query.list().get(0);
	}

	@Override
	public List<Room> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByProject);
		query.setParameter("project", project);
		return query.list();
	}


	/**
	 * Added by TatvaSoft, to get List of Room entity with given name.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findByRoomName(String name) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByName);
		query.setParameter("name", name);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to get List of Room entity with given Flat entity, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findbyFlatWithDragDrop(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLAT_WITH_DRAG_DROP);
		query.setParameter("flat", flat);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to get List of Room entity with given Floor entity, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findbyFloorWithDragDrop(Floor floor) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLOOR_WITH_DRAG_DROP);
		query.setParameter("floor", floor);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to get List of Room entity with given Building, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findbyBuildingWithDragDrop(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_BUILDING_WITH_DRAG_DROP);
		query.setParameter("building", building);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to get List of Room entity with given Project entity, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findByProjectWithDragDrop(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_PROJECT_WITH_DRAG_DROP);
		query.setParameter("project", project);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to populate Room entity with list of Walls.(List of Walls are lazy loading)
	 */
	@Override
	public Room populateWall(Room room) {
		Room room1 = (Room) sessionFactory.getCurrentSession().merge(room);
		int wall_size = room1.getWalls().size();
		return room1;
	}

	/**
	 * Added by TatvaSoft, to populate Room entities for given flat using Criateria API.
	 * It will only give room which has status=0 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Room> populateRoomsWithCriateria(Flat flat) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Room.class);
		criteria.add(Restrictions.eq("flat", flat));
		criteria.add(Restrictions.eq("status", (byte) 0));
		List<Room> rooms = criteria.list();
		return rooms;
	}


}
