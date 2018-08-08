package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Installation;
import enersis.envisor.service.InstallationService;


@Service("installationService")
@Transactional
public class InstallationServiceImpl implements InstallationService {

	public static final String getInstallationsQuery = "from Installation u where u.status=0";
	public static final String findByFlat = "from Installation u where u.flat =:flat and u.status =0";

	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<Installation> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getInstallationsQuery).list();
	}

	@Override
	public void save(Installation installation) {
		sessionFactory.getCurrentSession().saveOrUpdate(installation);
		
	}

	@Override
	public void delete(Installation installation) {
		installation.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(installation);
		
	}

	@Override
	public List<Installation> findbyFlat(Flat flat) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByFlat);
		query.setParameter("flat", flat);
		return query.list();
	}



}
