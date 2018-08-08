package com.kenure.dao;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.entity.BoundryDataCollector;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.Installer;
import com.kenure.entity.Site;
import com.kenure.entity.SiteInstallationFiles;
import com.kenure.utils.LoggerUtils;

@Repository
public class CommissioningDAOImpl implements ICommissioningDAO{

	private Logger log = LoggerUtils.getInstance(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unused")
	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<Installer> getAssignInstallerData(Customer currentCustomer) {
		Session session = getCurrentSession();
		try{
			String hqlQuery = "from Installer where customer=:customer";
			Query query = session.createQuery(hqlQuery);
			query.setParameter("customer", currentCustomer);
			return query.list();
		}catch(Exception e){
			log.warn(e.getMessage());
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCollector> getSpareDataCollectorListByCustomer(Customer customer) {

		try{	
			List<DataCollector> dataCollectorList = null; 
			String hqlQuery = "from DataCollector where site is null AND customer=:customer";
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("customer", customer);

			dataCollectorList = query.list();
			return dataCollectorList;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiteInstallationFiles> getSiteInstallationFilesBySiteId(Site site) {
		try{	
			List<SiteInstallationFiles> siteInstallationFiles = null; 
			String hqlQuery = "from SiteInstallationFiles where site=:site";
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("site", site);

			siteInstallationFiles = query.list();
			
			return siteInstallationFiles;
			
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiteInstallationFiles> getSiteInsFilesBySiteInsAndFilePrefix(
			Site site, String fileFilterName) {

		List<SiteInstallationFiles> searchedList = new ArrayList<SiteInstallationFiles>();
		try{
			Criteria sifCriteria = sessionFactory.getCurrentSession().createCriteria(SiteInstallationFiles.class);
			sifCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			sifCriteria.add(Restrictions.isNull("installer"));
			// Add step wise criteria
			if(site != null){
				sifCriteria.add(Restrictions.eq("site", site));
			}
			if(fileFilterName != null){
				sifCriteria.add(Restrictions.like("fileName", fileFilterName+"%"));
			}
			searchedList =  sifCriteria.list();
		}catch(Exception e){
			log.warn(e.getMessage());
			return searchedList;
		}
		return searchedList;
	}

	@Override
	public void updateSIF(SiteInstallationFiles sif) {
		try{
			getCurrentSession().update(sif);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void deleteSIF(SiteInstallationFiles sif) {
		try{
			getCurrentSession().delete(sif);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public List<SiteInstallationFiles> getSIFBySiteAndInstaller(Site site,Installer ins) {
		List<SiteInstallationFiles> list = new ArrayList<SiteInstallationFiles>();
		
		Criteria sifCriteria = getCurrentSession().createCriteria(SiteInstallationFiles.class);
		try{
			
			if(site != null){
				sifCriteria.add(Restrictions.eq("site", site));
			}
			if(ins != null){
				sifCriteria.add(Restrictions.eq("installer", ins));
			}else{
				sifCriteria.add(Restrictions.isNull("installer"));
			}
			/*Session session = getCurrentSession();
			String sifQuery = "from SiteInstallationFiles where site=:site";
			Query query = session.createQuery(sifQuery);
			query.setParameter("site", site);*/
			list = sifCriteria.list();
		}catch(Exception e){
			log.info(e.getMessage());
		}
		return list;	
	}

	@Override
	public SiteInstallationFiles getSIFBySiteFilterFileNameAndInstaller(
			Site site, String fileNameFilter, Installer installer) {
		Criteria sifCriteria = sessionFactory.getCurrentSession().createCriteria(SiteInstallationFiles.class);
		try{
			sifCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);	
			
			sifCriteria.add(Restrictions.eq("site", site));
			sifCriteria.add(Restrictions.like("fileName", fileNameFilter+"%"));
			if(installer == null){
				sifCriteria.add(Restrictions.isNull("installer"));
			}else{
				sifCriteria.add(Restrictions.eq("installer", installer));
			}
			
		}catch(Exception e){
			log.warn(e.getMessage());
		}
		
		if(sifCriteria.list().size() > 1){
			log.error("Error more than two rows with same search !!");
		}
		
		return (SiteInstallationFiles) sifCriteria.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCollector> getDataCollectorBySiteList(
			List<Site> siteIdList) {
		List<DataCollector> list = null;
		Criteria dcCriteria = sessionFactory.getCurrentSession().createCriteria(DataCollector.class);
		try{
			if(siteIdList!=null && siteIdList.size()>0){
				dcCriteria.add(Restrictions.in("site", siteIdList));
				list = dcCriteria.list();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataCollector getDcByDCSerialNumberAndCustomer(String dcSerialNumber,
			Customer customer) {
		Criteria dcCriteria = getCurrentSession().createCriteria(DataCollector.class);
		List<DataCollector> dcList = new ArrayList<>();
		try{
			dcCriteria.add(Restrictions.eq("customer", customer));
			dcCriteria.add(Restrictions.eq("dcSerialNumber", dcSerialNumber));
		}catch(Exception e){
			e.printStackTrace();
		}
		dcList = dcCriteria.list();
		if(dcList.size() > 1){
			log.error("dummy entry !!!");
		}
		if(dcList!=null && dcList.size()>0)
			return dcList.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BoundryDataCollector getBDCBySiteAndDC(Site site, DataCollector dc) {
		Criteria bdcCriteria = getCurrentSession().createCriteria(BoundryDataCollector.class);
		List<BoundryDataCollector> dcList = new ArrayList<>();
		try{
			bdcCriteria.add(Restrictions.eq("site", site));
			bdcCriteria.add(Restrictions.eq("datacollector", dc));
		}catch(Exception e){
			log.error(e.getMessage());
		}
		dcList = bdcCriteria.list();
		
		if(dcList.size() > 1)
			log.error("Dummy entry in BoundryDataCollector Table with siteId {} and DataCollector Id {}",site.getSiteId(),dc.getDatacollectorId());
		
		return dcList.get(0);
	}

	@Override
	public void updateDataCollectorConfigFields(String dcSerialNumber,String ip, Boolean isConnectionOk, Boolean isConfigOk,Integer batteryVoltage) {
		try{
			Session session = sessionFactory.openSession();
			Query query = session.createQuery("from DataCollector where dcSerialNumber = :dcSerialNumber and dcIp = :dcIp");
			query.setParameter("dcSerialNumber", dcSerialNumber);
			query.setParameter("dcIp", ip);
			DataCollector dc = (DataCollector)query.list().get(0);
			if(isConfigOk!=null)
				dc.setIsConfigOk(isConfigOk);
			if(isConnectionOk!=null)
				dc.setIsConnectionOk(isConnectionOk);
			if(batteryVoltage!=null)
				dc.setBatteryVoltage(batteryVoltage);
			session.update(dc);
			session.flush();
			/*Session session = sessionFactory.openSession();
			String hql = null;
			hql = "UPDATE DataCollector set isConnectionOk = :isConnectionOk,isConfigOk = :isConfigOk,batteryVoltage = :batteryVoltage WHERE dcSerialNumber = :dcSerialNumber AND dcIp = :dcIp";
			Query query = session.createQuery(hql);
			query.setParameter("isConnectionOk", isConnectionOk);
			query.setParameter("isConfigOk", isConfigOk);
			query.setParameter("dcSerialNumber", dcSerialNumber);
			query.setParameter("dcIp", ip);
			query.setParameter("batteryVoltage", batteryVoltage);
			query.executeUpdate();*/
		}catch(Exception e){
			log.error("Error occured in /updateDataCollectorConfigFields method while updating Datacollector table - {}",e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiteInstallationFiles> getInstalledDCsFileNamesBySite(Site site) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(SiteInstallationFiles.class);
		criteria.add(Restrictions.eq("site", site));
		criteria.add(Restrictions.like("fileName", "DC_%"));
		criteria.add(Restrictions.eq("isFileUploaded", Boolean.TRUE));
		criteria.add(Restrictions.eq("isFileVerified", Boolean.TRUE));
		criteria.add(Restrictions.isNull("noOfEndPoints"));
		criteria.add(Restrictions.isNotNull("noOfDatacollectors"));
		criteria.add(Restrictions.isNotNull("installer"));
		List<SiteInstallationFiles> result = criteria.list();
		if(result!=null&& result.size()>0)
			return result;
		return null;
	}

	@Override
	public void updateDataCollectorCommissioningFields(String dcSerialNumber,
			String ip, Boolean isLevel1CommStarted, Boolean isLevelnCommStarted) {
		try{
			Session session = sessionFactory.openSession();
			Query query = session.createQuery("from DataCollector where dcSerialNumber = :dcSerialNumber and dcIp = :dcIp");
			query.setParameter("dcSerialNumber", dcSerialNumber);
			query.setParameter("dcIp", ip);
			DataCollector dc = (DataCollector)query.list().get(0);
			if(isLevel1CommStarted!=null)
				dc.setIsLevel1CommStarted(isLevel1CommStarted);
			if(isLevelnCommStarted!=null)
				dc.setIsLevelnCommStarted(isLevelnCommStarted);
			session.update(dc);
			session.flush();
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error occured in /updateDataCollectorCommissioningFields method while updating Datacollector table - {}",e.getMessage());
		}
	}
}