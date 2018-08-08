package enersis.envisor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.DoorType;
import enersis.envisor.service.DoorTypeService;

@Service("doorTypeService")
@Transactional
public class DoorTypeServiceImpl implements DoorTypeService {

	public static final String GET_ALL_DOORTYPES_QUERY = "from DoorType dt ";
	public static final String FIND_BY_ID ="from DoorType dt where dt.id=:id and dt.status =0";
	public static final String FIND_BY_TYPE ="from DoorType dt where dt.type=:type and dt.status =0";

	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<DoorType> findAll() {
		List<DoorType> doorTypes = new ArrayList<DoorType>();

		doorTypes = sessionFactory.getCurrentSession()
				.createQuery(GET_ALL_DOORTYPES_QUERY).list();
		return doorTypes;
	}

	@Override
	public DoorType findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (DoorType) query.list().get(0);
	}

	@Override
	public DoorType findByType(String type) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_TYPE);
		query.setString("type", type);
		System.out.println(query.list().size());
		return (DoorType) query.list().get(0);
	}

	@Override
	public void save(DoorType doorType) {
		sessionFactory.getCurrentSession().saveOrUpdate(doorType);
	}

	@Override
	public void delete(DoorType doorType) {
		doorType.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(doorType);
	}

}
