package enersis.envisor.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.MeterType;
import enersis.envisor.entity.Project;
import enersis.envisor.service.MeterTypeService;

@Service("meterTypeService")
@Transactional
public class MeterTypeServiceImpl implements MeterTypeService, Serializable {

	private static final long serialVersionUID = 775989479733526432L;

	public static final String getAllMeterTypesQuery = "from MeterType u where u.status=0 ";
	@Autowired
	private SessionFactory sessionFactory;

	// private ServiceRegistry serviceRegistry;
	@Override
	public List<MeterType> findAll() {
		List<MeterType> meterTypes = new ArrayList<MeterType>();
		meterTypes = sessionFactory.getCurrentSession()
				.createQuery(getAllMeterTypesQuery).list();
//		System.out.println("proje tablosu boyutu: " + meterTypes.size());
		return meterTypes;
	}

	@Override
	public void save(MeterType meterType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(MeterType meterType) {
		// TODO Auto-generated method stub

	}

}
