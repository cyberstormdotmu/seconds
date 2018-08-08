package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBuilding;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.Period;
import enersis.envisor.entity.WaterMeter;
import enersis.envisor.entity.WaterMeterReading;
import enersis.envisor.service.WaterMeterReadingService;
@Service("waterMeterReadingService")
@Transactional
public class WaterMeterReadingServiceImpl implements WaterMeterReadingService {

	public static final String getReadingsQuery = "from WaterMeterReading u where u.status=0";
	public static final String findByWaterMeter = "from WaterMeterReading u where waterMeter =:waterMeter and u.status =0";
	public static final String findlastByWaterMeter = "from WaterMeterReading u where u.waterMeter =:waterMeter and u.status = 0 order by u.date";
	public static final String findByPeriod = "from WaterMeterReading u where u.period =:period and u.status = 0";
	public static final String findByDistributionLine = "from WaterMeterReading as reading where reading.waterMeter in ( "
			+ "select id from WaterMeter as waterMeter where waterMeter.flat in ( "
			+ "select id from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine)))";
	public static final String findByDistributionLineAndPeriod = "from WaterMeterReading as reading where reading.waterMeter in ( "
			+ "select id from WaterMeter as waterMeter where waterMeter.flat in ( "
			+ "select id from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine))) and reading.period =:period and reading.status = 0 Order by reading.waterMeter.serialNo";

	public static final String findByMeterAndPeriod = "from WaterMeterReading u where u.waterMeter =:waterMeter and u.period =:period and u.status = 0";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<WaterMeterReading> findAll() {
		return sessionFactory.getCurrentSession().createQuery(getReadingsQuery)
				.list();
	}

	@Override
	public void save(WaterMeterReading waterMeterReading) {
		sessionFactory.getCurrentSession().saveOrUpdate(
				waterMeterReading);
	}

	@Override
	public void delete(WaterMeterReading waterMeterReading) {
		waterMeterReading.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(
				waterMeterReading);
	}

	@Override
	public List<WaterMeterReading> findbyWaterMeter(WaterMeter waterMeter) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByWaterMeter);
		query.setParameter("waterCostAllocator", waterMeter);
		return query.list();
	}

	@Override
	public WaterMeterReading findLastByWaterMeter(WaterMeter waterMeter) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findlastByWaterMeter);
		query.setParameter("waterCostAllocator", waterMeter);
		return (WaterMeterReading) query.list().get(0);
	}

	@Override
	public List<WaterMeterReading> findByDistributionLine(
			DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public WaterMeterReading findByWaterMeterAndPeriod(WaterMeter waterMeter,
			Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByMeterAndPeriod);
		query.setParameter("waterMeter", waterMeter);
		query.setParameter("period", period);
		return (WaterMeterReading) query.list().get(0);
	}

	@Override
	public List<WaterMeterReading> findByPeriod(Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByPeriod);
		query.setParameter("period", period);
		return query.list();
	}

	@Override
	public List<WaterMeterReading> findByDistributionLineAndPeriod(
			DistributionLine distributionLine, Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLineAndPeriod);
		query.setParameter("distributionLine", distributionLine);
		query.setParameter("period", period);
		return query.list();
	}

	@Override
	public DistributionLine findDistributionLineOfReading(WaterMeterReading waterMeterReading, List<DistributionLineBuilding> distributionLineBuildings) {
		boolean isDistributionLineFound = false;
		int indexOfDistLineBuilding = 0;
		while (!isDistributionLineFound && distributionLineBuildings.size() > indexOfDistLineBuilding) {
			if (distributionLineBuildings.get(indexOfDistLineBuilding).getBuilding().getId() == waterMeterReading.getWaterMeter().getFlat().getBuilding().getId()) {
				isDistributionLineFound = true;
			} else {
				indexOfDistLineBuilding++;
			}
		}
		if (isDistributionLineFound) {
			return distributionLineBuildings.get(indexOfDistLineBuilding).getDistributionLine();
		} else {
			System.out.println("bu okuma verisine ait bir daðýtým hattý bulunamamýþtýr");
			return null;
		}
	}




}
