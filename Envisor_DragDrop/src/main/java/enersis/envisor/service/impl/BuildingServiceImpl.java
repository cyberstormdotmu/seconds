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
import enersis.envisor.entity.Project;
import enersis.envisor.service.BuildingService;

@Service("buildingService")
@Transactional
public class BuildingServiceImpl implements BuildingService {

	public static final String getBuildingsQuery = "from Building u where u.status=0";
	public static final String findByDistributionLine = "from Building u where u.distributionLine =:distributionLine and u.status =0";
	public static final String findByName ="from Building u where u.name =:name and u.status = 0";
	public static final String findByProject =" select distinct building from DistributionLineBuilding d where d.distributionLine in ( "
			+ " from DistributionLine  d where d.project =:project )";
	
	// Added by TatvaSoft, to get Building entity with given ID.
	public static final String FIND_BY_ID ="from Building b where b.id=:id and b.status =0";
	
	// Added by TatvaSoft, to get List of Buildings entity with given Project entity, used for DragDrop functionalities.
	public static final String FIND_BY_PROJECT_WITH_DRAG_DROP ="from Building b where b.project =:project and b.status =0";
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Building> findAll() {

		return sessionFactory.getCurrentSession()
				.createQuery(getBuildingsQuery).list();
	}

	/**
	 * Added by TatvaSoft, to get Building entity with given ID.
	 */
	@Override
	public Building findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (Building) query.list().get(0);
	}

	@Override
	public void save(Building building) {
		sessionFactory.getCurrentSession().saveOrUpdate(building);
	}

	@Override
	public void delete(Building building) {
		building.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(building);
	}

	@Override
	public List<Building> findbyDistributionLine(
			DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public List<Building> findByBuildingName(String name) {
		//	List<Project> projects= new ArrayList<Project>();
//		System.out.println("buraya girdim");
		Query query = sessionFactory.getCurrentSession().createQuery(findByName);
		query.setParameter("name", name);
//		System.out.println("proje tablosu boyutu: "+projects.size());
		return query.list();
	}

	@Override
	public List<Building> findByProject(Project project) {
		//	List<Project> projects= new ArrayList<Project>();
//		System.out.println("buraya girdim");
		Query query = sessionFactory.getCurrentSession().createQuery(findByProject);
		query.setParameter("project", project);
//		System.out.println("proje tablosu boyutu: "+projects.size());
		return query.list();
	}


	/**
	 * Added by TatvaSoft, to get List of Buildings entity with given Project entity, used for DragDrop functionalities.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Building> findByProjectWithDragDrop(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_PROJECT_WITH_DRAG_DROP);
		query.setParameter("project", project);
		return query.list();
	}

	/**
	 * Added by TatvaSoft, to populate Building entity with list of Floors.(List of Floors are lazy loading)
	 */
	@SuppressWarnings("unused")
	@Override
	public Building populateFloors(Building building) 
	{
		Building building1 = (Building) sessionFactory.getCurrentSession().merge(building);
		int floor_size = building1.getFloors().size();
		return building1;
	}

	
	/**
	 * Added by TatvaSoft, to populate Building entities for given project using Criateria API.
	 * It will only give building which has status=0 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Building> populateBuildingsWithCriateria(Project project) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Building.class);
		criteria.add(Restrictions.eq("project", project));
		criteria.add(Restrictions.eq("status", (byte) 0));
		List<Building> buildings = criteria.list();
		return buildings;
	}	

}
