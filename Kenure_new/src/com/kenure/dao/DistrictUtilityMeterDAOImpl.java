package com.kenure.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.entity.Customer;
import com.kenure.entity.DistrictUtilityMeter;
import com.kenure.utils.LoggerUtils;
/**
 * 
 * @author TatvaSoft
 *
 */
@Repository
public class DistrictUtilityMeterDAOImpl implements IDistrictUtilityMeterDAO{
	
	private static final Logger logger = LoggerUtils.getInstance(DistrictUtilityMeterDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public DistrictUtilityMeter getDUMeterByDUSerialNo(String duSerialNo) {
		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from DistrictUtilityMeter where districtUtilityMeterSerialNumber = :districtUtilityMeterSerialNumber");
		contactQuery.setParameter("districtUtilityMeterSerialNumber", duSerialNo);
		if(!contactQuery.list().isEmpty())
			return (DistrictUtilityMeter) contactQuery.list().get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DistrictUtilityMeter> getDUMetersByCustomer(Customer customer) {
		
		List<DistrictUtilityMeter> searchedList = new ArrayList<>();
		searchedList = null ;
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(DistrictUtilityMeter.class);
		
			if(customer != null){
				searchCriteria.add(Restrictions.eq("customer", customer));
			}
			
			searchedList = searchCriteria.list();

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return searchedList;
	}

}
