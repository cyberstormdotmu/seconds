package enersis.envisor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.WindowType;
import enersis.envisor.service.WindowTypeService;

@Service("windowTypeService")
@Transactional
public class WindowTypeServiceImpl implements WindowTypeService {

	public static final String GET_ALL_WINDOWTYPES_QUERY = "from WindowType wt ";
	public static final String FIND_BY_ID ="from WindowType wt where wt.id=:id and wt.status =0";
	public static final String FIND_BY_TYPE ="from WindowType wt where wt.type=:type and wt.status =0";

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<WindowType> findAll() {
		List<WindowType> windowTypes = new ArrayList<WindowType>();

		windowTypes = sessionFactory.getCurrentSession()
				.createQuery(GET_ALL_WINDOWTYPES_QUERY).list();
		return windowTypes;
	}

	@Override
	public WindowType findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (WindowType) query.list().get(0);
	}

	@Override
	public WindowType findByType(String type) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_TYPE);
		query.setString("type", type);
		System.out.println(query.list().size());
		return (WindowType) query.list().get(0);
	}

	@Override
	public void save(WindowType windowType) {
		sessionFactory.getCurrentSession().saveOrUpdate(windowType);
	}

	@Override
	public void delete(WindowType windowType) {
		windowType.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(windowType);
	}

}
