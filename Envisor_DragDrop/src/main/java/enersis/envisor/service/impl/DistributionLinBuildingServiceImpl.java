package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBuilding;
import enersis.envisor.entity.Project;
import enersis.envisor.service.DistributionLineBuildingService;



@Service("DistributionLineBuildingService")
@Transactional
public class DistributionLinBuildingServiceImpl implements DistributionLineBuildingService{

	public static final String getDistributionLineBuildings = "from DistributionLineBuilding dblb where dblb.status=0";
	public static final String findByBuilding = "from DistributionLineBuilding dblb where dblb.building =:building and dblb.status =0";
	public static final String findByDistributionLine = "from DistributionLineBuilding dblb where dblb.distributionLine =:distributionLine and dblb.status =0";
	public static final String findByProject ="from DistributionLineBuilding dblb where dblb.distributionLine in ("
			+ "from DistributionLine as dl where dl.project=:project)";
	
	
	@Autowired
	private SessionFactory sessionFactory;


	@Override
	public List<Building> findAll() {

		return sessionFactory.getCurrentSession()
				.createQuery(getDistributionLineBuildings).list();
	}


	@Override
	public void save(DistributionLineBuilding distributionLineBuilding) {
		sessionFactory.getCurrentSession().saveOrUpdate(distributionLineBuilding);
		
	}


	@Override
	public void delete(DistributionLineBuilding distributionLineBuilding) {
		distributionLineBuilding.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(distributionLineBuilding);
		
	}


	@Override
	public List<DistributionLineBuilding> findbyDistributionLine(
			DistributionLine  distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}


	@Override
	public List<DistributionLineBuilding> findByBuilding(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByBuilding);
		query.setParameter("building", building);
		return query.list();
	}


	@Override
	public List<DistributionLineBuilding> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProject);
		query.setParameter("project", project);
		return query.list();
	}
}
