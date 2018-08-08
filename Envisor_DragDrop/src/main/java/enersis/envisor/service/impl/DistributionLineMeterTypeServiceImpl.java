package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineMeterType;
import enersis.envisor.entity.Project;
import enersis.envisor.service.DistributionLineMeterTypeService;

@Service("distributionLineMeterTypeService")
@Transactional
public class DistributionLineMeterTypeServiceImpl implements
		DistributionLineMeterTypeService {

	public static final String getDistributionLineMeterTypeQuery = "from DistributionLineMeterType u where u.status=0 ";
	public static final String findByDistributionLine = "from DistributionLineMeterType u where u.distributionLine =:distributionLine and u.status =0";
	public static final String findByProject = "from DistributionLineMeterType dlmt where dlmt.distributionLine in ("
			+ "from DistributionLine as dl where dl.project=:project)";
	

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<DistributionLineMeterType> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getDistributionLineMeterTypeQuery).list();
	}

	@Override
	public void save(DistributionLineMeterType distributionLineMeterType) {
		sessionFactory.getCurrentSession().saveOrUpdate(
				distributionLineMeterType);

	}

	@Override
	public void delete(DistributionLineMeterType distributionLineMeterType) {
		distributionLineMeterType.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(
				distributionLineMeterType);
	}

	@Override
	public List<DistributionLineMeterType> findbyDistributionLine(
			DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public List<DistributionLineMeterType> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProject);
		query.setParameter("project", project);
		return query.list();
	}

}
