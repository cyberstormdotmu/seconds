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
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.service.FlatService;


@Service("flatService")
@Transactional
public class FlatServiceImpl implements FlatService{

	public static final String getFlatsQuery = "from Flat u where u.status=0";
	public static final String findByBuilding = "from Flat u where u.building =:building and u.status =0";
	public static final String findByNo ="from Flat u where u.no =:no and u.status = 0";
	public static final String findByName ="from Flat u where u.name =:name and u.status = 0";		// Added by TatvaSoft, to get List of Flats entity with given name.
	public static final String findByDistributionLine = "from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine)";
	
	public static final String findByProject = " from  Flat f where f.building in ( select distinct  building from DistributionLineBuilding d where d.distributionLine in ( "
			+ " from DistributionLine  d where d.project =:project ))";

	// Added by TatvaSoft, to get List of Flats entity with given ID, used for DragDrop functionalities.
	public static final String FIND_BY_ID ="from Flat f where f.id=:id and f.status =0";

	// Added by TatvaSoft, to get List of Flats entity with given Floor entity, used for DragDrop functionalities.
	public static final String FIND_BY_FLOOR_WITH_DRAG_DROP = "from Flat f where f.floorId =:floorId and f.status = 0";

	// Added by TatvaSoft, to get List of Flats entity with given Building, used for DragDrop functionalities.
	public static final String FIND_BY_BUILDING_WITH_DRAG_DROP = "from  Flat f where f.floorId in ( "
			+ "select distinct  floor from Floor f where f.building =:building"
			+ " ) and f.status = 0";

	// Added by TatvaSoft, to get List of Flats entity with given Project entity, used for DragDrop functionalities.
	public static final String FIND_BY_PROJECT_WITH_DRAG_DROP = "from  Flat f where f.floorId in ( "
			+ "select distinct  floor from Floor f where f.building in ( "
			+ "select distinct  building from Building b where b.project =:project"
			+ " ) ) and f.status = 0";

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Flat> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getFlatsQuery).list();
	}

	@Override
	public Flat findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (Flat) query.list().get(0);
	}

	@Override
	public void save(Flat flat) {
		sessionFactory.getCurrentSession().saveOrUpdate(flat);
		
	}

	@Override
	public void delete(Flat flat) {
		flat.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(flat);
	}

	@Override
	public List<Flat> findbyBuilding(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByBuilding);
		query.setParameter("building", building);
		return query.list();
	}

	@Override
	public List<Flat> findByFlatNo(byte no) {
		//	List<Project> projects= new ArrayList<Project>();
//		System.out.println("buraya girdim");
		Query query = sessionFactory.getCurrentSession().createQuery(findByNo);
		query.setParameter("no", no);
//		System.out.println("proje tablosu boyutu: "+projects.size());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Flat> findByFlatName(String name) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByName);
		query.setParameter("name", name);
		return query.list();
	}

	@Override
	public List<Flat> findByDistributionLine(DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public List<Flat> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProject);
		query.setParameter("project", project);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to get List of Flats entity with given Floor entity, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Flat> findbyFloorWithDragDrop(Floor floor) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_FLOOR_WITH_DRAG_DROP);
		query.setParameter("floorId", floor);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to get List of Flats entity with given Project Building, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Flat> findbyBuildingWithDragDrop(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_BUILDING_WITH_DRAG_DROP);
		query.setParameter("building", building);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to get List of Flats entity with given Project entity, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Flat> findByProjectWithDragDrop(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				FIND_BY_PROJECT_WITH_DRAG_DROP);
		query.setParameter("project", project);
		return query.list();
	}
	
	/**
	 * Added by TatvaSoft, to populate Flat entity with list of Rooms.(List of Rooms are lazy loading)
	 */
	@Override
	public Flat populateRooms(Flat flat){
		Flat flat1 = (Flat) sessionFactory.getCurrentSession().merge(flat);
		int room_size = flat1.getRooms().size();
		return flat1;
	}

	/**
	 * Added by TatvaSoft, to populate Flat entities for given floor using Criateria API.
	 * It will only give flat which has status=0 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Flat> populateFlatsWithCriateria(Floor floor) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Flat.class);
		criteria.add(Restrictions.eq("floorId", floor));
		criteria.add(Restrictions.eq("status", (byte) 0));
		List<Flat> flats = criteria.list();
		return flats;
	}
}
