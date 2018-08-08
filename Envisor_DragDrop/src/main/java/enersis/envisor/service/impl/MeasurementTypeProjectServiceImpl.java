package enersis.envisor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.MeasurementTypeProject;
import enersis.envisor.entity.Project;
import enersis.envisor.service.MeasurementTypeProjectService;

@Service("measurementTypeProjectService")
@Transactional
public class MeasurementTypeProjectServiceImpl implements
		MeasurementTypeProjectService {
	public static final String getAllMeasurementTypeProjectsQuery = "from MeasurementType u where u.status=0";
	public static final String findByProject = "from MeasurementTypeProject u where u.project =:project and u.status =0";
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<MeasurementTypeProject> findAll() {
		List<MeasurementTypeProject> measurementTypeProjects = new ArrayList<MeasurementTypeProject>();

		measurementTypeProjects = sessionFactory.getCurrentSession()
				.createQuery(getAllMeasurementTypeProjectsQuery).list();
//		System.out.println("measurement tablosu boyutu: "+ measurementTypeProjects.size());
		return measurementTypeProjects;
	}

	@Override
	public void save(MeasurementTypeProject measurementTypeProject) {
		sessionFactory.getCurrentSession().saveOrUpdate(measurementTypeProject);

	}

	@Override
	public void delete(MeasurementTypeProject measurementTypeProject) {
		measurementTypeProject.setStatus((byte) 0);
		sessionFactory.getCurrentSession().saveOrUpdate(measurementTypeProject);
	}

	@Override
	public List<MeasurementTypeProject> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProject);
		query.setParameter("project", project);
		return query.list();
	}

}
