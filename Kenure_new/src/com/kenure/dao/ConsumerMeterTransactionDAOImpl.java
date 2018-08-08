package com.kenure.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.utils.LoggerUtils;

/**
 * 
 * @author TatvaSoft
 *
 */
@Repository
public class ConsumerMeterTransactionDAOImpl implements IConsumerMeterTransactionDAO {

	private static final Logger logger = LoggerUtils.getInstance(ConsumerMeterTransactionDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List getNWConsumptionCMTList(String periodTime, String periodType, String duMeterId) {
		List searchedList = new ArrayList<>();
		
		try{
			Query query = sessionFactory.getCurrentSession().createSQLQuery("CALL nwConsumpstion(:periodTime, :periodType, :duMeterId)")
					.setParameter("periodTime", Integer.parseInt(periodTime))
					.setParameter("periodType", periodType)
					.setParameter("duMeterId", Integer.parseInt(duMeterId));

			searchedList = query.list();
	
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return searchedList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getConsumptionCMTList(String periodTime, String periodType, String baseTypeId, String baseType) {
		List searchedList = new ArrayList<>();
		
		try{
			Query query = sessionFactory.getCurrentSession().createSQLQuery("CALL consumptionBasedOnType(:periodTime, :periodType, :baseTypeId, :baseType)")
					.setParameter("periodTime", Integer.parseInt(periodTime))
					.setParameter("periodType", periodType)
					.setParameter("baseTypeId", baseTypeId)
					.setParameter("baseType", baseType);

			searchedList = query.list();
	
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return searchedList;
	}

}
