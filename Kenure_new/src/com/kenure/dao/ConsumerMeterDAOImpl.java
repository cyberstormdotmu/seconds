package com.kenure.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.entity.BillingHistory;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DistrictUtilityMeter;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.LoggerUtils;

/**
 * 
 * @author TatvaSoft
 *
 */
@Repository
public class ConsumerMeterDAOImpl implements IConsumerMeterDAO {

	private static final org.slf4j.Logger logger = LoggerUtils.getInstance(ConsumerMeterDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addConsumerMeter(ConsumerMeter consumerMeter) throws Exception {
		try {
			Session session = sessionFactory.getCurrentSession();

			session.save(consumerMeter);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public ConsumerMeter getConsumerMeterById(int consumerMeterId) {

		ConsumerMeter consumerObj = null;

		try {
			Query contactQuery = sessionFactory.getCurrentSession().createQuery("from ConsumerMeter where consumerMeterId = :consumerMeterId");
			contactQuery.setParameter("consumerMeterId", consumerMeterId);
			consumerObj = (ConsumerMeter) contactQuery.list().get(0);
			Hibernate.initialize(consumerObj.getDistrictUtilityMeter());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		if (consumerObj == null) {
			logger.info("No such end point found.");
		}

		return consumerObj;
	}

	@Override
	public boolean updateConsumer(ConsumerMeter consumerMeter) {
		try{
			Session session = sessionFactory.getCurrentSession();
			session.update(consumerMeter);
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteConsumer(int consumerMeterId, int userId) {
		try{
			Session session = sessionFactory.getCurrentSession();
			ConsumerMeter consumerMeter = (ConsumerMeter) session.get(ConsumerMeter.class, consumerMeterId);

			if(consumerMeter != null){
				consumerMeter.setActive(Boolean.FALSE);
				consumerMeter.setUpdatedBy(userId);
				consumerMeter.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
				consumerMeter.setDeletedBy(userId);
				consumerMeter.setDeletedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
				session.update(consumerMeter);
			}else
				return false;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerMeter> searchConsumer(String searchRegisterId, String searchConsumerAccNo, Customer customer, Consumer consumer) {


		List<ConsumerMeter> searchedList = new ArrayList<ConsumerMeter>();
		searchedList = null ;
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeter.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			// Add step wise criteria

			if(searchRegisterId != null && searchRegisterId.trim().length() > 0){
				searchCriteria.add(Restrictions.like("registerId", searchRegisterId,MatchMode.ANYWHERE));
			}
			if(searchConsumerAccNo!=null && searchConsumerAccNo.trim().length() > 0){
				searchCriteria.createCriteria("consumer", "c");
				searchCriteria.add(Restrictions.like("c.consumerAccountNumber", searchConsumerAccNo.trim(),MatchMode.ANYWHERE));
			}
			searchedList = searchCriteria.list();

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return searchedList;
	}

	@SuppressWarnings("unchecked")
	public List<ConsumerMeterTransaction> getConsumerMeterTransactionByRegisterId(String registerId) {
		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from ConsumerMeterTransaction where registerId = :registerId AND alerts is not null");
		contactQuery.setParameter("registerId", registerId);
		return (List<ConsumerMeterTransaction>) contactQuery.list();
	}

	@Override
	public boolean updateAlertAcknowledgementStatusByMeterTransactionId(int meterTransactionId, boolean resetFlag) {

		try{
			Session session = sessionFactory.getCurrentSession();
			ConsumerMeterTransaction consumerMeterTransaction = (ConsumerMeterTransaction) session.get(ConsumerMeterTransaction.class, meterTransactionId);

			if(consumerMeterTransaction != null){
				if (!resetFlag) {
					consumerMeterTransaction.setAlerts_ack(Boolean.TRUE);					
				} else {
					consumerMeterTransaction.setAlerts_ack(Boolean.FALSE);					
				}

				session.update(consumerMeterTransaction);
			}else
				return false;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@SuppressWarnings({"unchecked" })
	@Override
	public List<ConsumerMeter> consumerMeterListByDCId(String dcId) {

		List<ConsumerMeter> searchedList = new ArrayList<>();
		searchedList = null ;
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeter.class);

			if(dcId != null && dcId.trim().length() > 0){
				DataCollector dataCollector = (DataCollector) sessionFactory.getCurrentSession().get(DataCollector.class, Integer.parseInt(dcId));
				searchCriteria.add(Restrictions.eq("dataCollector", dataCollector));
			}

			searchedList = searchCriteria.list();

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return searchedList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerMeter> consumerMeterListByDUMeterId(String duMeterId) {
		List<ConsumerMeter> searchedList = new ArrayList<>();
		searchedList = null ;
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeter.class);

			if(duMeterId != null && duMeterId.trim().length() > 0){
				DistrictUtilityMeter districtUtilityMeter = (DistrictUtilityMeter) sessionFactory.getCurrentSession().get(DistrictUtilityMeter.class, Integer.parseInt(duMeterId));
				searchCriteria.add(Restrictions.eq("districtUtilityMeter", districtUtilityMeter));
			}

			searchedList = searchCriteria.list();

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return searchedList;
	}

	@Override
	public List<ConsumerMeter> getBatteryReplacementData(String reportedBy,Integer range,String installation,String siteId,String siteName,String zipcode,Integer customerId){
		List<ConsumerMeter> meterList = new ArrayList<ConsumerMeter>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeter.class,"cm");
			criteria.add(Restrictions.eq("cm.customer.customerId", customerId));
			criteria.add(Restrictions.isNotNull("cm.registerId"));
			criteria.add(Restrictions.isNotNull("cm.endpointBatteryReplacedDate"));
			//criteria.add(Restrictions.neOrIsNotNull("cm.estimatedRemainaingBatteryLifeInYear", 0));
			

			if(!installation.trim().equals("")){
				criteria.createAlias("cm.site", "s").createAlias("s.region", "r").add(Restrictions.eq("r.regionName", installation));
			}

			if(!siteId.trim().equals("")){
				criteria.add(Restrictions.eq("cm.site.siteId", siteId));
			}

			if(!siteName.trim().equals("")){
				criteria.createAlias("cm.site", "s").add(Restrictions.like("s.siteName", siteName,MatchMode.ANYWHERE));
			}

			if(!zipcode.trim().equals("")){
				criteria.add(Restrictions.eq("cm.zipcode",zipcode));
			}

			meterList = criteria.list();
			if(!meterList.isEmpty()){
				return meterList;
			}else {
				return null;
			}

		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;

		}

	}

	@Override
	public Map<String, Date> getBatteryAlerts(List<String> registerIds) {
		List list = new ArrayList();
		Map<String, Date> list1 =  new HashMap<String, Date>();
		try{
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("Select max(id) from ConsumerMeterTransaction where registerId in(:registerIds) group by registerId");
			query.setParameterList("registerIds", registerIds);
			List<Integer> tempList = query.list();
			if(!tempList.isEmpty()){
				session = sessionFactory.getCurrentSession();
				Query meterQuery = session.createQuery("select registerId,timeStamp from ConsumerMeterTransaction where id in(:transactionIds) AND alerts like :alerts ");
				meterQuery.setParameter("alerts","%"+"battery"+"%");
				meterQuery.setParameterList("transactionIds", tempList);
				list = meterQuery.list();
				for (int i = 0; i < list.size(); i++) {
					Object[] row = (Object[]) list.get(i);
					list1.put(row[0].toString(), (Date)row[1]);
				}
			}
			if(list1.isEmpty()){
				return null;
			}else {
				return list1;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ConsumerMeter> getConsumerMeterByRegisterId(List<String> registerIds, int customerId) {
		List<ConsumerMeter> list = new ArrayList<ConsumerMeter>();
		try{
			Query query = sessionFactory.getCurrentSession().createQuery("from ConsumerMeter cm where registerId in(:registerIds) AND customer.customerId =:customerId ");
			query.setParameterList("registerIds", registerIds);
			query.setParameter("customerId",customerId);
			list = query.list();
			if(list.isEmpty()){
				return null;
			}else {
				return list;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ConsumerMeter> getConsumerMetersByCustomerId(int customerId) {
		List<ConsumerMeter> list = new ArrayList<ConsumerMeter>();
		try{
			Query query = sessionFactory.getCurrentSession().createQuery("from ConsumerMeter where customer.customerId =:customerId and registerId is not null ");
			query.setParameter("customerId",customerId);
			list = query.list();
			if(list.isEmpty()){
				return null;
			}else {
				return list;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillingHistory> getConsumerMetersBillingByRegId(String registerId) {
		List<BillingHistory> list = new ArrayList<BillingHistory>();
		try{
			Query query = sessionFactory.getCurrentSession().createQuery("FROM BillingHistory where consumerMeter.consumerMeterId = (SELECT consumerMeterId FROM ConsumerMeter where registerId = :registerId) order by billDate DESC");
			query.setParameter("registerId",registerId);
			query.setMaxResults(12);
			list = (List<BillingHistory>) query.list();
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			return list;
		}
		return list;
	}

	@Override
	public List<ConsumerMeter> getConsumerMeterByAddress(String streetName,
			String add2, String add3, String add4, String zipcode,int customerId,String consumerAccNo,int siteId) {
		List<ConsumerMeter> list = new ArrayList<ConsumerMeter>();
		try{
			Query query = sessionFactory.getCurrentSession().createQuery("select cm from ConsumerMeter cm,Consumer con where con.consumerAccountNumber=:consumerAccountNumber AND con.consumerId=cm.consumer.consumerId "
					+ "AND cm.customer.customerId =:customerId AND cm.streetName =:streetName AND cm.address1 =:address1 AND cm.address2 =:address2 AND cm.address3=:address3 AND cm.zipcode=:zipcode AND cm.endpointSerialNumber is null AND cm.site.siteId =:siteId");
			query.setParameter("consumerAccountNumber", consumerAccNo);
			query.setParameter("siteId", siteId);
			query.setParameter("customerId",customerId);
			query.setParameter("streetName",streetName);
			query.setParameter("address1",add2);
			query.setParameter("address2",add3);
			query.setParameter("address3",add4);
			query.setParameter("zipcode",zipcode);
			list = query.list();
			if(list.isEmpty()){
				return null;
			}else {
				return list;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}