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
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.service.HeatCostAllocatorService;

@Service("heatCostAllocatorService")
@Transactional
public class HeatCostAllocatorServiceImpl implements HeatCostAllocatorService {

	public static final String getAllocatorsQuery = "from HeatCostAllocator u where u.status=0";
	public static final String findByRoom = "from HeatCostAllocator u where u.room =:room and u.status =0";
	public static final String findBySerialNo = "from HeatCostAllocator u where u.serialNo =:serialNo and u.status = 0";
	public static final String findByDistributionLine = "from HeatCostAllocator as allocator  where allocator.room in ( " + "select id from Room as room2 where room2.flat  in("
			+ "select id from Flat as flat2 where flat2.building in (" + "select id from Building as building2 where building2.distributionLine =:distributionLine)))";

	public static final String findByBuilding = "from HeatCostAllocator as allocator  where allocator.room in ( " + "select id from Room as room2 where room2.flat  in("
			+ "select id from Flat as flat2 where flat2.building=:building)) ";

	public static final String findByProject = "from HeatCostAllocator as allocator  where allocator.room in ( " + " from Room as room2 where room2.flat  in( "
			+ " from Flat as flat2 where flat2.building in ( " + " select building from DistributionLineBuilding  where distributionLine in ( "
			+ " from DistributionLine  d where d.project =:project ))))";

	public static final String findByFlat = "from HeatCostAllocator as allocator  where allocator.room in ( " + "select id from Room as room2 where room2.flat =:flat ) ";

	public HeatCostAllocatorServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<HeatCostAllocator> findAll() {

		return sessionFactory.getCurrentSession().createQuery(getAllocatorsQuery).list();
	}

	@Override
	public void save(HeatCostAllocator heatCostAllocator) {
		sessionFactory.getCurrentSession().saveOrUpdate(heatCostAllocator);
	}

	@Override
	public void delete(HeatCostAllocator heatCostAllocator) {

		heatCostAllocator.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(heatCostAllocator);
	}

	@Override
	public List<HeatCostAllocator> findbyRoom(Room room) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByRoom);
		query.setParameter("room", room);
		return query.list();
	}

	@Override
	public List<HeatCostAllocator> findBySerialNo(String serialNo) {
		Query query = sessionFactory.getCurrentSession().createQuery(findBySerialNo);
		query.setParameter("serialNo", serialNo);
		return query.list();
	}

	@Override
	public List<HeatCostAllocator> findByDistributionLine(DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public List<HeatCostAllocator> findByFlat(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByFlat);
		query.setParameter("flat", flat);
		return query.list();
	}

	@Override
	public List<HeatCostAllocator> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByProject);
		query.setParameter("project", project);
		return query.list();
	}

	@Override
	public List<HeatCostAllocator> findByBuilding(Building building) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByBuilding);
		query.setParameter("building", building);
		return query.list();
	}

	@Override
	public DistributionLine findDistributionLineOfReading(HeatCostAllocatorReading heatCostAllocatorReading, List<DistributionLineBuilding> distributionLineBuildings) {
		boolean isDistributionLineFound = false;
		int indexOfDistLineBuilding = 0;
		while (!isDistributionLineFound && distributionLineBuildings.size() > indexOfDistLineBuilding) {
			if (distributionLineBuildings.get(indexOfDistLineBuilding).getBuilding().getId() == heatCostAllocatorReading.getHeatCostAllocator().getRoom().getFlat().getBuilding().getId()) {
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
