package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.Period;
import enersis.envisor.entity.Project;
import enersis.envisor.service.HeatCostAllocatorReadingService;

@Service("heatCostAllocatorReadingService")
@Transactional
public class HeatCostAllocatorReadingServiceImpl implements
		HeatCostAllocatorReadingService {

	public static final String getReadingsQuery = "from HeatCostAllocatorReading u where u.status=0";
	public static final String findByAllocator = "from HeatCostAllocatorReading u where u.heatCostAllocator =:heatCostAllocator and u.status =0";
	public static final String findlastByAllocator = "from HeatCostAllocatorReading u where u.heatCostAllocator =:heatCostAllocator and u.status = 0 order by u.date";
	public static final String findByPeriod = "from HeatCostAllocatorReading u where u.period =:period and u.status = 0";
	public static final String findByDistributionLine = "from HeatCostAllocatorReading as reading where reading.heatCostAllocator in ( "
			+ "select id from HeatCostAllocator as allocator where allocator.room in ( "
			+ "select id from Room as room2 where room2.flat  in("
			+ "select id from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine))))";
	public static final String findByDistributionLineAndPeriod = "from HeatCostAllocatorReading as reading where reading.heatCostAllocator in ( "
			+ "select id from HeatCostAllocator as allocator where allocator.room in ( "
			+ "select id from Room as room2 where room2.flat  in("
			+ "select id from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine)))) "
			+ "and reading.period =:period and reading.status = 0 Order by reading.heatCostAllocator.serialNo";

	public static final String findByBuildingAndPeriod = "from HeatCostAllocatorReading as reading where reading.heatCostAllocator in ( "
			+ "select id from HeatCostAllocator as allocator where allocator.room in ( "
			+ "select id from Room as room2 where room2.flat  in("
			+ "select id from Flat as flat2 where flat2.building=:building))) "
			+ "and reading.period =:period and reading.status = 0 Order by reading.heatCostAllocator.serialNo";
	
	public static final String findByAllocatorAndPeriod = "from HeatCostAllocatorReading u where u.heatCostAllocator =:heatCostAllocator and u.period =:period and u.status = 0";

	
	@Autowired
	private SessionFactory sessionFactory;

	public HeatCostAllocatorReadingServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<HeatCostAllocatorReading> findAll() {
		return sessionFactory.getCurrentSession().createQuery(getReadingsQuery)
				.list();
	}

	@Override
	public void save(HeatCostAllocatorReading heatCostAllocatorReading) {
		sessionFactory.getCurrentSession().saveOrUpdate(
				heatCostAllocatorReading);
	}

	@Override
	public void delete(HeatCostAllocatorReading heatCostAllocatorReading) {
		heatCostAllocatorReading.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(
				heatCostAllocatorReading);
	}

	@Override
	public List<HeatCostAllocatorReading> findbyAllocator(
			HeatCostAllocator allocator) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByAllocator);
		query.setParameter("heatCostAllocator", allocator);
		return query.list();
	}

	@Override
	public HeatCostAllocatorReading findLastByAllocator(
			HeatCostAllocator allocator) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findlastByAllocator);
		query.setParameter("heatCostAllocator", allocator);
		return (HeatCostAllocatorReading) query.list().get(0);
	}

	@Override
	public List<HeatCostAllocatorReading> findByDistributionLine(
			DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public HeatCostAllocatorReading findByAllocatorAndPeriod(
			HeatCostAllocator heatCostAllocator, Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByAllocatorAndPeriod);
		query.setParameter("heatCostAllocator", heatCostAllocator);
		query.setParameter("period", period);
		return (HeatCostAllocatorReading) query.list().get(0);
	}

	@Override
	public List<HeatCostAllocatorReading> findByPeriod(Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByPeriod);
		query.setParameter("period", period);
		return query.list();
	}
	
	@Override
	public List<HeatCostAllocatorReading> findByDistributionLineAndPeriod(
			DistributionLine distributionLine, Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLineAndPeriod);
		query.setParameter("distributionLine", distributionLine);
		query.setParameter("period", period);
		return query.list();
	}

	@Override
	public List<HeatCostAllocatorReading> findByBuildingAndPeriod(Building building, Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLineAndPeriod);
		query.setParameter("building", building);
		query.setParameter("period", period);
		return query.list();
	}

//	@Override
//	public List<HeatCostAllocatorReading> findByProjectAndPeriod(Project project, Period period) {
//		Query query = sessionFactory.getCurrentSession().createQuery(
//				findByDistributionLineAndPeriod);
//		query.setParameter("project", project);
//		query.setParameter("period", period);
//		return query.list();
//	}

}
