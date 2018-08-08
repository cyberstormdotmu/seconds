package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBuilding;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.HeatMeter;
import enersis.envisor.entity.HeatMeterReading;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.service.HeatMeterService;

@Service("heatMeterService")
@Transactional
public class HeatMeterServiceImpl implements HeatMeterService {

	public static final String getHeatMetersQuery = "from HeatMeter u where u.status=0";
	public static final String findByFlat = "from HeatMeter u where u.flat =:flat and u.status =0";
	public static final String findBySerialNo = "from HeatMeter u where u.serialNo =:serialNo and u.status = 0";
	public static final String findByDistributionLine = "from HeatMeter as heatMeter  where heatMeter.flat in ( "
			+ "select id from Flat as flat2 where flat2.building in ("
			+ "select id from Building as building2 where building2.distributionLine =:distributionLine))";
	public static final String findByProject = "from HeatMeter as meter  where meter.flat in ( " 
			+ " from Flat as flat2 where flat2.building in ( " 
			+ " select building from DistributionLineBuilding  where distributionLine in ( "
			+ " from DistributionLine  d where d.project =:project ))))";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<HeatMeter> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getHeatMetersQuery).list();
	}

	@Override
	public void save(HeatMeter heatMeter) {
		sessionFactory.getCurrentSession().saveOrUpdate(heatMeter);

	}

	@Override
	public void delete(HeatMeter heatMeter) {
		heatMeter.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(heatMeter);
		}

	@Override
	public List<HeatMeter> findBySerialNo(String serialNo) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findBySerialNo);
		query.setParameter("serialNo", serialNo);
		return query.list();
	}

	@Override
	public List<HeatMeter> findByDistributionLine(
			DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public List<HeatMeter> findByFlat(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByFlat);
		query.setParameter("flat", flat);
		return query.list();
	}

	@Override
	public List<HeatMeter> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProject);
		query.setParameter("project", project);
		return query.list();
	}

	@Override
	public DistributionLine findDistributionLineOfReading(HeatMeterReading heatMeterReading, List<DistributionLineBuilding> distributionLineBuildings) {
		boolean isDistributionLineFound = false;
		int indexOfDistLineBuilding = 0;
		while (!isDistributionLineFound && distributionLineBuildings.size() > indexOfDistLineBuilding) {
			if (distributionLineBuildings.get(indexOfDistLineBuilding).getBuilding().getId() == heatMeterReading.getHeatMeter().getFlat().getBuilding().getId()) {
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
