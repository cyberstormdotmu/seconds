package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.HeatMeter;
import enersis.envisor.entity.HeatMeterReading;
import enersis.envisor.entity.Period;
import enersis.envisor.service.HeatMeterReadingService;
@Service("heatMeterReadingService")
@Transactional
public class HeatMeterReadingServiceImpl implements HeatMeterReadingService {

	public static final String getReadingsQuery = "from HeatMeterReading u where u.status=0";
	public static final String findByHeatMeter = "from HeatMeterReading u where heatMeter =:heatMeter and u.status =0";
	public static final String findlastByHeatMeter = "from HeatMeterReading u where u.heatMeter =:heatMeter and u.status = 0 order by u.date";
	public static final String findByPeriod = "from HeatMeterReading u where u.period =:period and u.status = 0";
	public static final String findByDistributionLine = "from HeatMeterReading as reading where reading.heatMeter in ( "
			+ "select id from HeatMeter as heatMeter where heatMeter.flat in ( "
			+ "select id from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine)))";
	public static final String findByDistributionLineAndPeriod = "from HeatMeterReading as reading where reading.heatMeter in ( "
			+ "select id from HeatMeter as heatMeter where heatMeter.flat in ( "
			+ "select id from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine))) and reading.period =:period and reading.status = 0 Order by reading.heatMeter.serialNo";

	public static final String findByAllocatorAndPeriod = "from HeatMeterReading u where u.heatMeter =:heatMeter and u.period =:period and u.status = 0";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<HeatMeterReading> findAll() {
		return sessionFactory.getCurrentSession().createQuery(getReadingsQuery)
				.list();
	}

	@Override
	public void save(HeatMeterReading heatMeterReading) {
		sessionFactory.getCurrentSession().saveOrUpdate(
				heatMeterReading);
	}

	@Override
	public void delete(HeatMeterReading heatMeterReading) {
		heatMeterReading.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(
				heatMeterReading);
	}

	@Override
	public List<HeatMeterReading> findbyHeatMeter(HeatMeter heatMeter) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByHeatMeter);
		query.setParameter("heatCostAllocator", heatMeter);
		return query.list();
	}

	@Override
	public HeatMeterReading findLastByHeatMeter(HeatMeter heatMeter) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findlastByHeatMeter);
		query.setParameter("heatCostAllocator", heatMeter);
		return (HeatMeterReading) query.list().get(0);
	}

	@Override
	public List<HeatMeterReading> findByDistributionLine(
			DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public HeatMeterReading findByHeatMeterAndPeriod(HeatMeter heatMeter,
			Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByAllocatorAndPeriod);
		query.setParameter("heatCostAllocator", heatMeter);
		query.setParameter("period", period);
		return (HeatMeterReading) query.list().get(0);
	}

	@Override
	public List<HeatMeterReading> findByPeriod(Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByPeriod);
		query.setParameter("period", period);
		return query.list();
	}

	@Override
	public List<HeatMeterReading> findByDistributionLineAndPeriod(
			DistributionLine distributionLine, Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLineAndPeriod);
		query.setParameter("distributionLine", distributionLine);
		query.setParameter("period", period);
		return query.list();
	}

}
