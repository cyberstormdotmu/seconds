package enersis.envisor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.RoomType;
import enersis.envisor.service.RoomTypeService;

@Service("roomTypeService")
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {

	public static final String GET_ALL_ROOMTYPES_QUERY = "from RoomType rt ";
	public static final String FIND_BY_ID ="from RoomType rt where rt.id=:id and rt.status =0";
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<RoomType> findAll() {
		List<RoomType> roomTypes = new ArrayList<RoomType>();

		roomTypes = sessionFactory.getCurrentSession()
				.createQuery(GET_ALL_ROOMTYPES_QUERY).list();
		return roomTypes;
	}

	@Override
	public RoomType findById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery(FIND_BY_ID);
		query.setParameter("id", id);
		return (RoomType) query.list().get(0);
	}

	@Override
	public void save(RoomType roomType) {
		sessionFactory.getCurrentSession().saveOrUpdate(roomType);
	}

	@Override
	public void delete(RoomType roomType) {
		roomType.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(roomType);
	}


}
