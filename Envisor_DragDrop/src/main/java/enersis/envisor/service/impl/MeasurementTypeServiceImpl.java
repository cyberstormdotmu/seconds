package enersis.envisor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.MeasurementType;
import enersis.envisor.service.MeasurementTypeService;

@Service("measurementTypeService")
@Transactional
public class MeasurementTypeServiceImpl implements MeasurementTypeService {
	
	public static final String getAllMeasurementTypesQuery ="from MeasurementType u where u.status=0 ";
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public List<MeasurementType> findAll() {
		List<MeasurementType> measurementTypes = new ArrayList<MeasurementType>();
		
		measurementTypes = sessionFactory.getCurrentSession().createQuery(getAllMeasurementTypesQuery).list();
//		System.out.println("measurement tablosu boyutu: "+measurementTypes.size());
		return	measurementTypes;
	}

	@Override
	public void save(MeasurementType measurementType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(MeasurementType measurementType) {
		// TODO Auto-generated method stub
		
	}

}
