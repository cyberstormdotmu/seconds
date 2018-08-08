package com.kenure.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.entity.ContactDetails;
import com.kenure.entity.MaintenanceTechnician;
import com.kenure.entity.VPNConfiguration;
import com.kenure.utils.LoggerUtils;

/**
 * 
 * @author TatvaSoft
 *
 */
@Repository
public class VPNDAOImpl implements IVPNDAO {

	private Logger logger = LoggerUtils.getInstance(VPNDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public VPNConfiguration getVPNDetails() {

		try{

			String vpnHQLQuery = "from VPNConfiguration";
			Query vpnQuery = sessionFactory.getCurrentSession().createQuery(vpnHQLQuery);

			@SuppressWarnings("unchecked")
			List<VPNConfiguration> vpnList = vpnQuery.list();

			if(vpnList.size() > 1){
				logger.warn("Multiple VPN configuration available ... Returning 1st DB connection");
				return vpnList.get(0);
			}else if(vpnList.size() == 1){
				return vpnList.get(0);
			}else{
				logger.warn("No configuration available in VPN DB table");
				return null;
			}

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<VPNConfiguration> getVPNList() {

		List<VPNConfiguration> vpnList = null;

		try{

			String vpnHQLQuery = "from VPNConfiguration";
			Query vpnQuery = sessionFactory.getCurrentSession().createQuery(vpnHQLQuery);

			vpnList = vpnQuery.list();

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return vpnList;
	}


	@Override
	public VPNConfiguration getVPNDataById(String configId) {
		
		VPNConfiguration vpnConfiguration = null;
		
		try{
			Session session = sessionFactory.getCurrentSession();
			vpnConfiguration = (VPNConfiguration) session.get(VPNConfiguration.class, Integer.parseInt(configId));
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return vpnConfiguration;
	}
	
	@Override
	public void updateVPNDetails(VPNConfiguration vpnConfiguration) {
		
		try {
			Session session = sessionFactory.getCurrentSession();

			session.update(vpnConfiguration);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
