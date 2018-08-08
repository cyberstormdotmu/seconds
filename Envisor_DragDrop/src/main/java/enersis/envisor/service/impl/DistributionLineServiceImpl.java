package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Project;
import enersis.envisor.service.DistributionLineService;

@Service("distributionLineService")
@Transactional
public class DistributionLineServiceImpl implements DistributionLineService {

	public static final String findByProjectQuery = "from DistributionLine u where u.project =:project and u.status =0";
	public static final String getDistributionLinesQuery = "from DistributionLine u where  u.status =0";
	public static final String findByName ="from DistributionLine u where u.name =:name and u.status = 0";
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<DistributionLine> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getDistributionLinesQuery).list();
	}

	@Override
	public void save(DistributionLine distributionLine) {
		sessionFactory.getCurrentSession().saveOrUpdate(distributionLine);	
		
	}

	@Override
	public void delete(DistributionLine distributionLine) {
		distributionLine.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(distributionLine);
	}

	@Override
	public List<DistributionLine> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProjectQuery);
		query.setParameter("project", project);
		return query.list();
		
	}
	
	@Override
	public List<DistributionLine> findByDistributionLineName(String name) {
//		List<Project> projects= new ArrayList<Project>();
//		System.out.println("buraya girdim");
		Query query = sessionFactory.getCurrentSession().createQuery(findByName);
		query.setParameter("name", name);
//		System.out.println("proje tablosu boyutu: "+projects.size());
		return query.list();
	}

}
