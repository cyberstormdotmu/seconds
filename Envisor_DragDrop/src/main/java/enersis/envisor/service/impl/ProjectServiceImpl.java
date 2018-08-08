package enersis.envisor.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.poifs.property.Child;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.Building;
import enersis.envisor.entity.Project;
import enersis.envisor.service.ProjectService;

@Service("projectService")
@Transactional
public class ProjectServiceImpl implements ProjectService, Serializable{

	public static final String getAllProjectsQuery ="from Project u where u.status=0 ";
	
	public static final String findByProjectName ="from Project u where u.projectName =:projectName and u.status = 0";
	
	// Added by TatvaSoft, to get Project entity with given ID.
	public static final String FIND_BY_ID ="from Project p where p.id=:id and p.status =0";
	
	private static final long serialVersionUID = -646906627964732554L;
	@Autowired
	private SessionFactory sessionFactory;
//	private ServiceRegistry serviceRegistry;
	@Override
	public List<Project> findAll() {
		List<Project> projects= new ArrayList<Project>();
//		System.out.println("buraya girdim");
		
		projects = sessionFactory.getCurrentSession().createQuery(getAllProjectsQuery).list();
		return	projects;
	}

	
	/**
	 * Added by TatvaSoft, to get Project entity with given ID.
	 */
	@Override
	public Project findById(short id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (Project) query.list().get(0);
	}

	@Override
	public void save(Project project) {
		sessionFactory.getCurrentSession().saveOrUpdate(project);
	}

	@Override
	public void delete(Project project) {
		project.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(project);
	}


	@Override
	public List<Project> findByProjectName(String projectName) {
//		List<Project> projects= new ArrayList<Project>();
//		System.out.println("buraya girdim");
		Query query = sessionFactory.getCurrentSession().createQuery(findByProjectName);
		query.setParameter("projectName", projectName);
		System.out.println("proje tablosu boyutu: ");
		return query.list();
	}

	
	/**
	 * Added by TatvaSoft, to populate Project entity with list of Buildings.(List of Buildings are lazy loading)
	 */
	@SuppressWarnings("unused")
	@Override
	public Project populateBuildings(Project project) {
		Project project1 = (Project) sessionFactory.getCurrentSession().merge(project);
		int building_size = project1.getBuildings().size();
		return project1;
	}
}
