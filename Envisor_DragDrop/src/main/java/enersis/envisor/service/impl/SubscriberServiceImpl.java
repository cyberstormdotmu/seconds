package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.Installation;
import enersis.envisor.entity.Subscriber;
import enersis.envisor.service.SubscriberService;

@Service("subscriberService")
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

	
	public static final String getSubScribersQuery = "from Room u where u.status=0";
	public static final String findByInstallation = "from Subscriber u where u.installation =:installation and u.status =0";
	
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<Subscriber> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getSubScribersQuery).list();
	}

	@Override
	public void save(Subscriber subscriber) {
		sessionFactory.getCurrentSession().saveOrUpdate(subscriber);
		
	}

	@Override
	public void delete(Subscriber subscriber) {
		subscriber.setStatus((byte) 0);
		sessionFactory.getCurrentSession().saveOrUpdate(subscriber);
		
	}

	@Override
	public List<Subscriber> findbyInstallation(Installation installation) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByInstallation);
		query.setParameter("installation", installation);
		return query.list();
	}


}
