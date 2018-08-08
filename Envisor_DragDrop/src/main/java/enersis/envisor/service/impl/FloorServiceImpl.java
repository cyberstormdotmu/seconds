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
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.service.FloorService;

/**
 * 
 * @author TatvaSoft
 * Hibernate service implemation of FloorService interface, used to get and set data of Floor table.
 *
 */

@Service("floorService")
@Transactional
public class FloorServiceImpl implements FloorService {

	public static final String getFloorQuery = "from Floor f where f.status=0";
	public static final String findByName ="from Floor f where f.name =:name and f.status = 0";
	public static final String findByBuilding = "";
	public static final String findByProject = "";

	// To get Floor entity with given id, used for DragDrop functionalities.
	public static final String FIND_BY_ID ="from Floor f where f.id=:id and f.status =0";

	// To get List of Floors entity with given Building entity, used for DragDrop functionalities.
	public static final String FIND_BY_BUILDING_WITH_DRAG_DROP = "from Floor f where f.building =:building and f.status =0";

	// To get List of Floor entity with given Project entity, used for DragDrop functionalities.
	public static final String FIND_BY_PROJECT_WITH_DRAG_DROP = " from  Floor f where f.building in ( "
			+ "select distinct  building from Building b where b.project =:project"
			+ " ) and f.status =0";


	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Floor> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getFloorQuery).list();
	}

	@Override
	public Floor findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (Floor) query.list().get(0);
	}

	@Override
	public void save(Floor floor) {
		sessionFactory.getCurrentSession().saveOrUpdate(floor);
	}

	@Override
	public void delete(Floor floor) {
		floor.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(floor);
	}

	@Override
	public List<Floor> findbyBuilding(Building building) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Floor> findByFloorName(String name) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByName);
		query.setParameter("name", name);
		return query.list();
	}

	@Override
	public List<Floor> findByProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * To get List of Floor entity with given Building entity, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Floor> findbyBuildingWithDragDrop(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_BUILDING_WITH_DRAG_DROP);
		query.setParameter("building", building);
		return query.list();
	}

	/**
	 * To get List of Floor entity with given Project entity, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Floor> findByProjectWithDragDrop(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_PROJECT_WITH_DRAG_DROP);
		query.setParameter("project", project);
		return query.list();
	}

	/**
	 * To populate Floor entity with list of Flats.(List of Flats are lazy loading)
	 */
	@Override
	public Floor populateFlats(Floor floor) {
		Floor floor1 = (Floor) sessionFactory.getCurrentSession().merge(floor);
		int flat_size = floor1.getFlats().size();
		return floor1;
	}

	/**
	 * Added by TatvaSoft, to populate Floor entities for given building using Criateria API.
	 * It will only give floor which has status=0 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Floor> populateFloorsWithCriateria(Building building) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Floor.class);
		criteria.add(Restrictions.eq("building", building));
		criteria.add(Restrictions.eq("status", (byte) 0));
		List<Floor> floors = criteria.list();
		return floors;
	}
	
}
