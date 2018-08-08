package com.kenure.dao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.entity.BillingHistory;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DataCollectorAlerts;
import com.kenure.entity.Installer;
import com.kenure.entity.Region;
import com.kenure.entity.Site;
import com.kenure.model.ConsumerAlertListModel;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.LoggerUtils;

/**
 * 
 * @author TatvaSoft
 *
 */
@Repository
public class ReportDAOImpl implements IReportDAO{

	private Logger log = LoggerUtils.getInstance(ReportDAOImpl.class);

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getDistrictMeterTransaction(int customerId, Date startDate, Date endDate) {

		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		List districtMeterTransactionList = new ArrayList();
		districtMeterTransactionList = null;
		SQLQuery query = getCurrentSession().createSQLQuery("CALL getDistrictMeterTransactionByDates(:startDate,:endDate,:customerId)");
		query.setParameter("startDate", sdf.format(startDate)+"%");
		query.setParameter("endDate", sdf.format(endDate)+"%");
		query.setParameter("customerId", customerId);
		districtMeterTransactionList = query.list();
		return districtMeterTransactionList;
	}


	@Override
	public List<Object> getConsumerMeterTransactionByDistrictMeterId(
			int districtMeterId,int customerId, Date startDate, Date endDate) {
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

		List<Object> consumerMeterTransactionList = new ArrayList<Object>();
		List startDateList = null;
		List endDateList = null;
		SQLQuery startquery = getCurrentSession().createSQLQuery("CALL consumerMeterTransactionByStartDate_SP(:startDate,:customerId,:districtMeterId)");
		startquery.setParameter("startDate", sdf.format(startDate) +"%");
		startquery.setParameter("customerId", customerId);
		startquery.setParameter("districtMeterId", districtMeterId);
		startDateList = startquery.list();

		SQLQuery endquery = sessionFactory.getCurrentSession().createSQLQuery("CALL consumerMeterTransactionByEndDate_SP(:endDate,:customerId,:districtMeterId)");
		endquery.setParameter("endDate", sdf.format(endDate) +"%");
		endquery.setParameter("customerId", customerId);
		endquery.setParameter("districtMeterId", districtMeterId);
		endDateList = endquery.list();

		consumerMeterTransactionList.add(startDateList);
		consumerMeterTransactionList.add(endDateList);
		return consumerMeterTransactionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCollector> getDCListForDataUsageReport(int customerId,Integer siteId, String siteName, String installationName, String dcSerial) {

		List<DataCollector> dataCollectorList = null;
		int tempSiteId = 0;

		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(DataCollector.class,"dc");
		// To get unique records
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		// add criteria for customer
		searchCriteria.createAlias("dc.customer", "ct");
		searchCriteria.add(Restrictions.eq("ct.customerId",customerId));

		// If sitename exist
		if(siteName!=null && !(siteName.trim().equals(""))){
			tempSiteId = userDAO.getSiteBySiteName(siteName);
			// sitename and siteid both exist
			if(tempSiteId != 0 && siteId != null && tempSiteId == siteId){
				searchCriteria.add(Restrictions.eq("dc.site.siteId",tempSiteId));
			}else if (tempSiteId !=0 && siteId == null) { // only sitename exist
				searchCriteria.add(Restrictions.eq("dc.site.siteId",tempSiteId));
			}else{
				return null;
			}
		}
		// only siteid exist
		if(tempSiteId == 0){
			if(siteId!=null){
				searchCriteria.add(Restrictions.eq("dc.site.siteId",siteId));
			}
		}
		if(dcSerial!=null && !(dcSerial.trim().equals(""))){
			searchCriteria.add(Restrictions.like("dc.dcSerialNumber",dcSerial,MatchMode.ANYWHERE));
		}
		dataCollectorList = searchCriteria.list();
		return dataCollectorList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerMeterTransaction> getConsumerMeterTransactionByConsumerMeterRegisterId(
			Integer registerId) {
		List<ConsumerMeterTransaction> list = null;
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria cmtCriteria = session.createCriteria(ConsumerMeterTransaction.class);
			cmtCriteria.add(Restrictions.eq("consumerMeter", registerId));
			list =  cmtCriteria.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public void initalizeConsumerMeter(Consumer thisConsumer) {
		try{
			Session session = getCurrentSession();
			//initialize object
			session.refresh(thisConsumer);
			Hibernate.initialize(thisConsumer.getConsumerMeter());
			for(ConsumerMeter thisMeter:thisConsumer.getConsumerMeter()){
				Hibernate.initialize(thisMeter);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public List<ConsumerMeter> getConsumerMeterByCustomer(
			Customer customer) {
		List<ConsumerMeter> list = null;
		try{
			Session session = getCurrentSession();
			Criteria consumerMeterTransCriteria = session.createCriteria(ConsumerMeter.class);
			consumerMeterTransCriteria.add(Restrictions.eq("customer", customer));
			list = consumerMeterTransCriteria.list();
		}catch(Exception e){
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<ConsumerMeterTransaction> getConsumerTransactionByConsumerMeletList(
			List<Integer> list) {
		List<ConsumerMeterTransaction> consumerMeterTransactionList = null;
		try{
			Session session = getCurrentSession();
			Criteria consumerMeterTransCriteria = session.createCriteria(ConsumerMeterTransaction.class);
			consumerMeterTransCriteria.add(Restrictions.in("consumerMeter", list));	
			consumerMeterTransactionList = consumerMeterTransCriteria.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return consumerMeterTransactionList;
	}

	@Override
	public List<ConsumerMeterTransaction> transactionListBasedOnSingleConsumerMeterRegisterId(Integer registerId) {
		List<ConsumerMeterTransaction> consumerMeterTransactionList = null;
		if(registerId != null){
			try{
				Session session = getCurrentSession();
				Criteria consumerMeterTransCriteria = session.createCriteria(ConsumerMeterTransaction.class);
				consumerMeterTransCriteria.add(Restrictions.eq("consumerMeter", registerId));
				consumerMeterTransactionList = consumerMeterTransCriteria.list();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return consumerMeterTransactionList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List callAbnormalSO(Customer customer,int type) {
		List result = null;
		try{
			Session session = getCurrentSession();
			Query query = session.createSQLQuery("CALL abnormal_consumpton_SP(:customerId,:type,:startTimeFromMidnight,:endTimeToMidnight)");
			if(type == 7){
				query.setParameter("customerId", Integer.valueOf(customer.getCustomerId()))
				.setParameter("type", type)
				.setParameter("startTimeFromMidnight", null)
				.setParameter("endTimeToMidnight", null);

			}else{
				query.setParameter("customerId", Integer.valueOf(customer.getCustomerId()))
				.setParameter("type", type)
				.setParameter("startTimeFromMidnight", DateTimeConversionUtils.getPreviousDateBasedOnCurrentDateFromMidNight())
				.setParameter("endTimeToMidnight", DateTimeConversionUtils.getTodaysDateToMidNight());
			}
			result = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerMeterTransaction> getConsumerMeterTransactionByRegisterId(
			String registerID) {

		List<ConsumerMeterTransaction> list = null;
		try{
			Session session = getCurrentSession();
			Query query = session.createQuery("From ConsumerMeterTransaction where registerId=:registerID and timeStamp > :startDate and timeStamp < :endDate");
			query.setParameter("registerID", registerID)
			.setParameter("startDate",DateTimeConversionUtils.getPreviousDateBasedOnCurrentDateFromMidNight())
			.setParameter("endDate", DateTimeConversionUtils.getTodaysDateToMidNight());
			list = query.list();
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return list;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<BillingHistory> getBillingHistoryList(Date billingStartDate,Date billingEndDate, int customerId, boolean onlyEstimatedReadAcc) {

		List<BillingHistory> billingHistories = null;

		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(BillingHistory.class,"bh");
		// To get unique records
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		// add criteria for customer
		searchCriteria.createAlias("bh.consumerMeter", "cm");
		searchCriteria.add(Restrictions.eq("cm.customer.customerId",customerId)).add(Restrictions.between("billDate", billingStartDate, billingEndDate));

		if(onlyEstimatedReadAcc){
			searchCriteria.add(Restrictions.eq("isEstimated",true));
		}

		billingHistories = searchCriteria.list();
		return billingHistories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getConsumerMeterAlerts(String alert, Integer siteId,
			String siteName, String zipCode, int customerId,
			String consumerAccNo, String address1, Instant periodStartDate,
			Instant periodEndDate) {

		int tempSiteId = 0;
		List<ConsumerMeterTransaction> consumerMeterTransearchedList = new ArrayList<ConsumerMeterTransaction>();
		List<ConsumerMeter> consumerMeterList = new ArrayList<ConsumerMeter>();
		List<Object> searchedList = new ArrayList<Object>();
		Criteria consumerSearchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeter.class,"cm");
		consumerSearchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		consumerSearchCriteria.add(Restrictions.eq("cm.customer.customerId", customerId));
		// If sitename exist
		if(siteName!=null && !(siteName.trim().equals(""))){
			tempSiteId = userDAO.getSiteBySiteName(siteName);
			// sitename and siteid both exist
			if(tempSiteId != 0 && siteId != null && tempSiteId == siteId){
				consumerSearchCriteria.add(Restrictions.eq("cm.site.siteId",tempSiteId));
			}else if (tempSiteId !=0 && siteId == null) { // only sitename exist
				consumerSearchCriteria.add(Restrictions.eq("cm.site.siteId",tempSiteId));
			}else{
				return null;
			}
		}
		// only siteid exist
		if(tempSiteId == 0){
			if(siteId!=null){
				consumerSearchCriteria.add(Restrictions.eq("cm.site.siteId",siteId));
			}
		}
		if(zipCode!=null && !(zipCode.trim().equals(""))){
			consumerSearchCriteria.add(Restrictions.eq("cm.zipcode", zipCode));
		}
		if(consumerAccNo!=null && !(consumerAccNo.trim().equals(""))){
			consumerSearchCriteria.createAlias("cm.consumer", "cc");
			consumerSearchCriteria.add(Restrictions.eq("cc.consumerAccountNumber",consumerAccNo));
		}
		if(address1!=null && !(address1.trim().equals(""))){
			consumerSearchCriteria.add(Restrictions.eq("cm.streetName", address1));
		}
		consumerMeterList = consumerSearchCriteria.list();

		List<String> regIdList = new ArrayList<String>();
		List<ConsumerAlertListModel> consumerAlertList = new ArrayList<ConsumerAlertListModel>();
		for(int i=0;i<consumerMeterList.size();i++){
			if(consumerMeterList.get(i).getRegisterId() != null){
				regIdList.add(consumerMeterList.get(i).getRegisterId());
				ConsumerAlertListModel consumerAlert = new ConsumerAlertListModel();
				if(consumerMeterList.get(i).getConsumer() != null){
					consumerAlert.setConsumerAccountNumber(consumerMeterList.get(i).getConsumer().getConsumerAccountNumber());
				}
				if(consumerMeterList.get(i).getSite() != null){
					consumerAlert.setSiteId(consumerMeterList.get(i).getSite().getSiteId());
				}
				consumerAlert.setZipcode(consumerMeterList.get(i).getZipcode());
				consumerAlert.setRegisterId(consumerMeterList.get(i).getRegisterId());
				consumerAlertList.add(consumerAlert);
			}
		}
		if(regIdList.size()>0){
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeterTransaction.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			searchCriteria.add(Restrictions.in("registerId", regIdList));

			if(periodStartDate!=null && periodEndDate!=null){
				Date startDate = Date.from(periodStartDate);
				Date endDate = Date.from(periodEndDate);
				searchCriteria.add(Restrictions.between("timeStamp", startDate, endDate));
			}
			if(alert!=null && !alert.equals("---Please Select Alert Type---")){
				if(alert.equals("Valid Alerts")){
					searchCriteria.add(Restrictions.eq("alerts_ack",true));
				}
				if(alert.equals("New Alerts")){
					searchCriteria.add(Restrictions.eq("alerts_ack",false));
				}
			}
			consumerMeterTransearchedList = searchCriteria.list();

			searchedList.add(consumerMeterTransearchedList);
			searchedList.add(consumerAlertList);
			return searchedList;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCollectorAlerts> getNetworkAlerts(String alert,
			Integer siteId, String siteName, int customerId,
			Instant periodStartDate, Instant periodEndDate) {

		List<DataCollectorAlerts> dataCollectorList = new ArrayList<DataCollectorAlerts>();
		dataCollectorList = null;
		int tempSiteId = 0;
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(DataCollectorAlerts.class,"dca");
		// To get unique records
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		// add criteria for customer
		searchCriteria.createAlias("dca.dataCollector", "dc").createAlias("dc.customer", "ct");
		searchCriteria.add(Restrictions.eq("ct.customerId",customerId));

		if(alert!=null && !alert.equals("---Please Select Alert Type---")){
			if(alert.equals("Valid Alerts")){
				searchCriteria.add(Restrictions.eq("dca.alertAck",true));
			}
			if(alert.equals("New Alerts")){
				searchCriteria.add(Restrictions.eq("dca.alertAck",false));
			}
		}

		if(periodStartDate!=null && periodEndDate!=null){
			Date startDate = Date.from(periodStartDate);
			Date endDate = Date.from(periodEndDate);
			searchCriteria.add(Restrictions.between("alertDate", startDate, endDate));
		}

		// If sitename exist
		if(siteName!=null && !(siteName.trim().equals(""))){
			tempSiteId = userDAO.getSiteBySiteName(siteName);
			// sitename and siteid both exist
			if(tempSiteId != 0 && siteId != null && tempSiteId == siteId){
				searchCriteria.add(Restrictions.eq("dc.site.siteId",tempSiteId));
			}else if (tempSiteId !=0 && siteId == null) { // only sitename exist
				searchCriteria.add(Restrictions.eq("dc.site.siteId",tempSiteId));
			}else{
				return null;
			}
		}
		// only siteid exist
		if(tempSiteId == 0){
			if(siteId!=null){
				searchCriteria.add(Restrictions.eq("dc.site.siteId",siteId));
			}
		}

		dataCollectorList = searchCriteria.list();
		return dataCollectorList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getAggregateConsumption(Integer siteId,
			String siteName, String zipCode, int customerId,
			String consumerAccNo, String address1, Instant periodStartDate,
			Instant periodEndDate, Integer noOfOccupants) {

		int tempSiteId = 0;
		List consumerMeterTransearchedList;
		List<ConsumerMeter> consumerMeterList = new ArrayList<ConsumerMeter>();
		List<Object> searchedList = new ArrayList<Object>();
		Criteria consumerSearchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeter.class,"cm");
		consumerSearchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		consumerSearchCriteria.add(Restrictions.eq("cm.customer.customerId", customerId));
		consumerSearchCriteria.createAlias("cm.consumer", "cc");
		// If sitename exist
		if(siteName!=null && !(siteName.trim().equals(""))){
			tempSiteId = userDAO.getSiteBySiteName(siteName);
			// sitename and siteid both exist
			if(tempSiteId != 0 && siteId != null && tempSiteId == siteId){
				consumerSearchCriteria.add(Restrictions.eq("cm.site.siteId",tempSiteId));
			}else if (tempSiteId !=0 && siteId == null) { // only sitename exist
				consumerSearchCriteria.add(Restrictions.eq("cm.site.siteId",tempSiteId));
			}else{
				return null;
			}
		}
		// only siteid exist
		if(tempSiteId == 0){
			if(siteId!=null){
				consumerSearchCriteria.add(Restrictions.eq("cm.site.siteId",siteId));
			}
		}
		if(zipCode!=null && !(zipCode.trim().equals(""))){
			consumerSearchCriteria.add(Restrictions.eq("cm.zipcode", zipCode));
		}
		if(consumerAccNo!=null && !(consumerAccNo.trim().equals(""))){
			consumerSearchCriteria.add(Restrictions.eq("cc.consumerAccountNumber",consumerAccNo));
		}
		if(address1!=null && !(address1.trim().equals(""))){
			consumerSearchCriteria.add(Restrictions.eq("cm.streetName", address1));
		}
		if(noOfOccupants!=null){
			consumerSearchCriteria.add(Restrictions.eq("cm.numberOfOccupants",noOfOccupants));
		}

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("cc.consumerId"));
		consumerSearchCriteria.setProjection(projectionList);
		consumerMeterList = consumerSearchCriteria.list();

		Session session = getCurrentSession();
		Date startDate = Date.from(periodStartDate);
		Date endDate = Date.from(periodEndDate);
		for(int i=0;i<consumerMeterList.size();i++){
			String sqlQuery = "SELECT con.consumer_acc_number,sum(cmt.current_meter_reading) "
					+ "from consumer con inner join consumer_meter cm inner join consumer_meter_transaction cmt "
					+ "on con.consumer_id = cm.consumer_id and cm.register_id = cmt.register_id "
					+ "where cmt.current_reading_timestamp between :periodStartDate and :periodEndDate and "
					+ "cmt.register_id in (SELECT register_id FROM consumer_meter WHERE consumer_id = :consumerId) "
					+ "group by con.consumer_acc_number";
			SQLQuery query = session.createSQLQuery(sqlQuery);
			query.setParameter("periodStartDate", startDate);
			query.setParameter("periodEndDate", endDate);
			query.setParameter("consumerId", consumerMeterList.get(i));
			consumerMeterTransearchedList = query.list();
			searchedList.add(consumerMeterTransearchedList);
		}
		return searchedList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double getDataUsageByCustomer(Customer customer) {

		List<Double> result = null;
		try {
			String query = "SELECT sum(mb_per_month) FROM datacollector where customer_id=" +customer.getCustomerId()+";";
			result = (List<Double>) sessionFactory.getCurrentSession().createSQLQuery(query).list();
			if(result!=null && result.size()>0)
				return result.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List searchFreeReportData(Customer customer,String selectedField,String reportPeriod, Date startDate, Date endDate,
			String installationName, String siteName, String dcSerialNumber) {
		Criteria criteria = null ;	
		try{
			Session session = getCurrentSession();
			Calendar cal = Calendar.getInstance();

			if(reportPeriod != null && !reportPeriod.trim().isEmpty()){
				int reportPeriodTime = Integer.parseInt(reportPeriod.substring(0,reportPeriod.length()-1));
				if(reportPeriod.contains("M") && startDate == null){
					cal.add(Calendar.MONTH, -reportPeriodTime);
					startDate = cal.getTime();
				}else if(reportPeriod.contains("W") && startDate == null){
					cal.add(Calendar.WEEK_OF_MONTH, -reportPeriodTime);
					startDate = cal.getTime();
				}else{
					/*System.out.println("Error !");*/
				}
				if(selectedField.equalsIgnoreCase("dcCheckBox")){
					criteria = session.createCriteria(DataCollector.class);
					criteria.createAlias("site", "site");
					criteria.createAlias("site.region", "region");

					criteria.add(Restrictions.eq("customer",customer));
					if(dcSerialNumber != null && !dcSerialNumber.trim().isEmpty())
						criteria.add(Restrictions.eq("dcSerialNumber", dcSerialNumber));
					if(startDate != null)
						criteria.add(Restrictions.ge("createdTs", startDate));
					if(endDate != null)
						criteria.add(Restrictions.le("createdTs", endDate));
					if(siteName != null && !siteName.trim().isEmpty())
						criteria.add(Restrictions.eq("site.siteName", siteName));
					if(installationName != null && !installationName.trim().isEmpty())
						criteria.add(Restrictions.eq("region.regionName", installationName));

				}else if(selectedField.equalsIgnoreCase("consumerEPCheckBox")){
					criteria = session.createCriteria(ConsumerMeter.class);
					criteria.createAlias("site", "site");
					criteria.createAlias("site.region", "region");
					criteria.createAlias("dataCollector", "dc");

					criteria.add(Restrictions.eq("customer",customer));
					if(dcSerialNumber != null && !dcSerialNumber.trim().isEmpty())
						criteria.add(Restrictions.eq("dc.dcSerialNumber", dcSerialNumber));
					if(startDate != null)
						criteria.add(Restrictions.ge("createdTimeStamp", startDate));
					if(endDate != null)
						criteria.add(Restrictions.le("createdTimeStamp", endDate));
					if(siteName != null && !siteName.trim().isEmpty())
						criteria.add(Restrictions.eq("site.siteName", siteName));
					if(installationName != null && !installationName.trim().isEmpty())
						criteria.add(Restrictions.eq("region.regionName", installationName));

				}else if(selectedField.equalsIgnoreCase("siteCheckBox")){
					criteria = session.createCriteria(Site.class);
					criteria.createAlias("region", "region");

					/*if(dcSerialNumber != null && !dcSerialNumber.trim().isEmpty())
						criteria.add(Restrictions.eq("dc.dcSerialNumber", dcSerialNumber));*/
					criteria.add(Restrictions.eq("region.customer",customer));
					if(startDate != null)
						criteria.add(Restrictions.ge("createdTs", startDate));
					if(endDate != null)
						criteria.add(Restrictions.le("createdTimeStamp", endDate));
					if(siteName != null && !siteName.trim().isEmpty())
						criteria.add(Restrictions.eq("siteName", siteName));
					if(installationName != null && !installationName.trim().isEmpty())
						criteria.add(Restrictions.eq("region.regionName", installationName));

				}else if(selectedField.equalsIgnoreCase("installerCheckBox")){
					criteria = session.createCriteria(Installer.class);
					criteria.createAlias("dataCollector", "dc");
					criteria.createAlias("dc.site", "site");

					criteria.add(Restrictions.eq("customer",customer));

					if(startDate != null)
						criteria.add(Restrictions.ge("createdTS", startDate));
					if(endDate != null)
						criteria.add(Restrictions.le("createdTS", endDate));
					/*if(siteName != null && !siteName.trim().isEmpty())
						criteria.add(Restrictions.eq("siteName", siteName));*/
					if(installationName != null && !installationName.trim().isEmpty())
						criteria.add(Restrictions.eq("installerName", installationName));
					if(dcSerialNumber != null && !dcSerialNumber.trim().isEmpty())
						criteria.add(Restrictions.eq("dc.dcSerialNumber", dcSerialNumber));
					if(siteName != null && !siteName.trim().isEmpty())
						criteria.add(Restrictions.eq("site.siteName", siteName));

				}else if(selectedField.equalsIgnoreCase("consumerCheckBox")){
					criteria = session.createCriteria(Consumer.class);
					criteria.createAlias("consumerMeter", "consumerMeter");
					criteria.createAlias("consumerMeter.site", "site");
					criteria.createAlias("site.datacollector", "dc");
					criteria.createAlias("dc.installer", "installer");

					criteria.add(Restrictions.eq("customer",customer));

					if(startDate != null)
						criteria.add(Restrictions.ge("createdTimeStamp", startDate));
					if(endDate != null)
						criteria.add(Restrictions.le("createdTimeStamp", endDate));

					if(installationName != null && !installationName.trim().isEmpty())
						criteria.add(Restrictions.eq("installer.installerName", installationName));
					if(dcSerialNumber != null && !dcSerialNumber.trim().isEmpty())
						criteria.add(Restrictions.eq("dc.dcSerialNumber", dcSerialNumber));
					if(siteName != null && !siteName.trim().isEmpty())
						criteria.add(Restrictions.eq("site.siteName", siteName));

				}else{
					/*System.out.println("Error");*/
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return criteria.list();
	}

	@Override
	public void initalizeSiteByregion(Region n) {
		Session session = getCurrentSession();
		if(n != null){
			session.refresh(n);
			Hibernate.initialize(n.getSite());
		}
	}

	@Override
	public void initializeDatacollectorBySite(Site site) {
		Session session = getCurrentSession();
		session.refresh(site);
		Hibernate.initialize(site.getDatacollector());
	}
	
	@Override
	public Double getRevenueForIndividualConsumerMeter(
			ConsumerMeter consumerMeter, Date startLimitDate, Date endDate) {
		/*try{
			Session session = getCurrentSession();
			List<ConsumerMeterTransaction> transactionList = null;
			if(startLimitDate != null){
				Criteria criteria = session.createCriteria(ConsumerMeterTransaction.class);
				minMaxTransacton = new ArrayList<ConsumerMeterTransaction>();
				criteria.add(Restrictions.eq("registerId", consumerMeter.getRegisterId()));
				criteria.add(Restrictions.ge("timeStamp", startLimitDate));
				criteria.addOrder(Order.asc("timeStamp"));
				criteria.setMaxResults(1);
				transactionList = criteria.list();
				if(transactionList != null && !transactionList.isEmpty()){
					minMaxTransacton.add(0,transactionList.get(0));
				}
			}if(endDate != null && !minMaxTransacton.isEmpty()){
				Criteria criteria = session.createCriteria(ConsumerMeterTransaction.class);
				criteria.add(Restrictions.eq("registerId", consumerMeter.getRegisterId()));
				criteria.add(Restrictions.le("timeStamp", endDate));
				criteria.addOrder(Order.desc("timeStamp"));
				criteria.setMaxResults(1);

				transactionList = criteria.list();
				if(transactionList != null && !transactionList.isEmpty()){
					minMaxTransacton.add(1,transactionList.get(0));
				}
				
				dataUsage += (minMaxTransacton.get(1).getMeterReading()-minMaxTransacton.get(0).getMeterReading());
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}*/
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(BillingHistory.class);
		try{
			criteria.add(Restrictions.eq("consumerMeter", consumerMeter));
			if(startLimitDate != null)
				criteria.add(Restrictions.ge("billingStartDate", startLimitDate));
			if(endDate != null)
				criteria.add(Restrictions.le("billingEndDate", endDate));
		}catch(Exception e){
			e.printStackTrace();
		}
		Double totalRevenue = 0.0D;
		List<BillingHistory> billingHistoryList = criteria.list();
		
		int begin,size=billingHistoryList.size();
		for(begin=0;begin<size;begin++){
			BillingHistory bg = billingHistoryList.get(begin);
			if(bg != null && bg.getTotalAmount() != null)
				totalRevenue += bg.getTotalAmount();
		}
		
		/*System.out.println(" Revenue for consumer meter id >"+consumerMeter.getConsumerMeterId()+"  "+totalRevenue);*/
		return totalRevenue;
	}

	@Override
	public void initializeConsumerMeterByDC(DataCollector dc) {
		Session session = getCurrentSession();
		session.refresh(dc);
		Hibernate.initialize(dc.getConsumerMeters());
	}

	@Override
	public List<BillingHistory> getUsageForIndividualConsumerMeter(ConsumerMeter consumerMeter,
			Date startLimitDate, Date endDate) {
		
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(BillingHistory.class);
		try{
			criteria.add(Restrictions.eq("consumerMeter", consumerMeter));
			if(startLimitDate != null)
				criteria.add(Restrictions.ge("billingStartDate", startLimitDate));
			if(endDate != null)
				criteria.add(Restrictions.le("billingEndDate", endDate));
		}catch(Exception e){
			e.printStackTrace();
		}
		List<BillingHistory> billingHistoryList = criteria.list();
		billingHistoryList.stream().forEach(n -> {
			session.evict(n);
		});
		
		return billingHistoryList;
	}

	@Override
	public void initializeConsumerByCustomer(Customer customer) {
		try{
			Session session = getCurrentSession();
			session.refresh(customer);
			Hibernate.initialize(customer.getConsumer());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void initializeRegionByCustomer(Customer customer) {
		try{
			Session session = getCurrentSession();
			session.refresh(customer);
			Hibernate.initialize(customer.getRegion());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void initializeConsumerMeterByConsumer(Consumer consumer) {
		try{
			Session session = getCurrentSession();
			session.refresh(consumer);
			Hibernate.initialize(consumer.getConsumerMeter());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
