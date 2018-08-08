package enersis.envisor.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import enersis.envisor.entity.ReadOutMethod;
import enersis.envisor.service.ReadOutMethodService;


@Service("readOutMethodService")
@Transactional
public class ReadOutMethodServiceImpl implements ReadOutMethodService{

	public static final String getReadOutMethodsQuery = "from ReadOutMethod u where u.status=0 ";
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public List<ReadOutMethod> findAll() {
		return sessionFactory.getCurrentSession().createQuery(getReadOutMethodsQuery).list();
	}

	@Override
	public void save(ReadOutMethod readOutMethod) {
		sessionFactory.getCurrentSession().saveOrUpdate(readOutMethod);
		
	}

	@Override
	public void delete(ReadOutMethod readOutMethod) {
		readOutMethod.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(readOutMethod);
		
	}

	@Override
	public List<ReadOutMethod> findBytype(String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
