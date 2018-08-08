package enersis.envisor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.WallType;
import enersis.envisor.service.WallTypeService;

@Service("wallTypeService")
@Transactional
public class WallTypeServiceImpl implements WallTypeService {

	public static final String GET_ALL_WALLTYPES_QUERY = "from WallType wt ";
	public static final String FIND_BY_ID = "from WallType wt where wt.id=:id and wt.status =0";

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<WallType> findAll() {
		List<WallType> wallTypes = new ArrayList<WallType>();

		wallTypes = sessionFactory.getCurrentSession()
				.createQuery(GET_ALL_WALLTYPES_QUERY).list();
		return wallTypes;
	}

	@Override
	public WallType findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (WallType) query.list().get(0);
	}

	@Override
	public void save(WallType wallType) {
		sessionFactory.getCurrentSession().saveOrUpdate(wallType);
	}

	@Override
	public void delete(WallType wallType) {
		wallType.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(wallType);
	}

}
