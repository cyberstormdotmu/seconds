package enersis.envisor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.BillType;
import enersis.envisor.entity.MeterType;
import enersis.envisor.service.BillTypeService;

@Service("billTypeService")
@Transactional

public class BillTypeServiceImpl implements BillTypeService {

	public static final String getAllBillTypesQuery = "from BillType u ";
	@Autowired
	private SessionFactory sessionFactory;

	// private ServiceRegistry serviceRegistry;
	@Override
	public List<BillType> findAll() {
		List<BillType> billTypes = new ArrayList<BillType>();
		billTypes = sessionFactory.getCurrentSession()
				.createQuery(getAllBillTypesQuery).list();
//		System.out.println("proje tablosu boyutu: " + meterTypes.size());
		return billTypes;
	}

	@Override
	public void save(BillType billType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(BillType billType) {
		// TODO Auto-generated method stub

	}


}
