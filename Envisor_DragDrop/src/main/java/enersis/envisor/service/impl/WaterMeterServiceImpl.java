package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatMeter;
import enersis.envisor.entity.MeterType;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.WaterMeter;
import enersis.envisor.service.WaterMeterService;

@Service("waterMeterService")
@Transactional
public class WaterMeterServiceImpl implements WaterMeterService {

	public static final String getWaterMetersQuery = "from WaterMeter u where u.status=0";
	public static final String findByFlat = "from WaterMeter u where u.flat =:flat and u.status =0";
	public static final String findBySerialNo = "from WaterMeter u where u.serialNo =:serialNo and u.status = 0";
	public static final String findByDistributionLine = "from WaterMeter as waterMeter  where waterMeter.flat in ( "
			+ "select id from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine))";
	public static final String findByProject = "from WaterMeter as meter  where meter.flat in ( " 
			+ " from Flat as flat2 where flat2.building in ( " 
			+ " select building from DistributionLineBuilding  where distributionLine in ( "
			+ " from DistributionLine  d where d.project =:project ))))";
	public static final String findByMeterType = "from WaterMeter u where u.meterType =:meterType and u.status = 0";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<WaterMeter> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getWaterMetersQuery).list();
	}

	@Override
	public void save(WaterMeter waterMeter) {
		sessionFactory.getCurrentSession().saveOrUpdate(waterMeter);

	}

	@Override
	public void delete(WaterMeter waterMeter) {
		waterMeter.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(waterMeter);
		}

	@Override
	public List<WaterMeter> findBySerialNo(String serialNo) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findBySerialNo);
		query.setParameter("serialNo", serialNo);
		return query.list();
	}
	@Override
	public List<WaterMeter> findByDistributionLine(
			DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public List<WaterMeter> findByFlat(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByFlat);
		query.setParameter("flat", flat);
		return query.list();
	}

	@Override
	public List<WaterMeter> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProject);
		query.setParameter("project", project);
		return query.list();
	}

	@Override
	public List<WaterMeter> findByMeterType(MeterType meterType) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByMeterType);
		query.setParameter("meterType", meterType);
		return query.list();
	}

}
