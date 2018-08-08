package com.kenure.dao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.entity.BatteryLife;
import com.kenure.entity.BillingHistory;
import com.kenure.entity.BoundryDataCollector;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.ContactDetails;
import com.kenure.entity.Country;
import com.kenure.entity.Currency;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DataCollectorAlerts;
import com.kenure.entity.DataPlan;
import com.kenure.entity.DatacollectorMessageQueue;
import com.kenure.entity.DistrictMeterTransaction;
import com.kenure.entity.DistrictUtilityMeter;
import com.kenure.entity.Installer;
import com.kenure.entity.MaintenanceTechnician;
import com.kenure.entity.NormalCustomer;
import com.kenure.entity.Region;
import com.kenure.entity.Role;
import com.kenure.entity.Site;
import com.kenure.entity.SiteInstallationFiles;
import com.kenure.entity.TariffPlan;
import com.kenure.entity.TariffTransaction;
import com.kenure.entity.User;
import com.kenure.model.ConsumerAlertListModel;
import com.kenure.model.UserLoginCredentials;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.LoggerUtils;

/**
 * 
 * @author TatvaSoft
 *
 */
@Repository
public class UserDAOImpl implements IUserDAO {

	private Logger log = LoggerUtils.getInstance(UserDAOImpl.class);

	@Autowired
	private IReportDAO reportDAO;

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Region getRegionByName(Customer customer,String regionName) {

		Region region = null;

		Query query = getCurrentSession().createQuery("from Region where regionName=:regionName and customer=:customer");
		query.setParameter("regionName", regionName)
		.setParameter("customer", customer);

		if(query.list() != null && !query.list().isEmpty())
			region = (Region) query.list().get(0);

		return region;

	}

	@SuppressWarnings("unchecked")
	@Override
	public User authenticateUser(UserLoginCredentials loginCredentials) {

		User currentUser = null;

		try {
			String authenticationHQL = "from User where userName =  :userId and password = :password"; // Authentication
			// query

			Query query = sessionFactory.getCurrentSession().createQuery(
					authenticationHQL);
			query.setParameter("userId", loginCredentials.getUserName());
			query.setParameter("password", loginCredentials.getPassword());

			List<User> userList = query.list(); // Generic type of List may be
			// return type of function

			if (!userList.isEmpty()) {

				if(userList.get(0).getRole().getRoleName().equalsIgnoreCase("customer")){
					currentUser = userList.get(0);
				}else if (userList.get(0).getActiveStatus()) {
					currentUser = userList.get(0);
				}else
					log.warn("True credentials but Deleted Account");
			}
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return currentUser;
	}

	@Override
	public boolean deleteUser(int dependantId, int roleId, int modifyingId) {

		try {
			if (roleId == 2) {
				Session session = sessionFactory.getCurrentSession();
				Customer customer = (Customer) session.get(Customer.class,
						dependantId);

				if (customer != null) {
					customer.setActiveStatus(false);
					customer.setStatus("INACTIVE");
					customer.setUpdatedBy(modifyingId);
					customer.setUpdatedTS(DateTimeConversionUtils.getDateInUTC(new Date()));
					customer.setDeletedBy(modifyingId);
					customer.setDeletedTS(DateTimeConversionUtils.getDateInUTC(new Date()));
					customer.getUser().setActiveStatus(false);
					customer.getUser().setDeletedBy(modifyingId);
					customer.getUser().setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					customer.getUser().setUpdatedBy(modifyingId);
					customer.getUser().setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

					List<NormalCustomer> nCustomerList = new ArrayList<>(customer.getNormalCustomer());
					nCustomerList.stream().forEach(n -> {
						if(n.getUser() != null ){
							n.getUser().setActiveStatus(false);
							n.getUser().setDeletedBy(modifyingId);
							n.getUser().setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
							n.getUser().setUpdatedBy(modifyingId);
							n.getUser().setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
						}
					});

					session.update(customer);
				} else {
					return false;
				}
			}

			if (roleId == 3) {
				Session session = sessionFactory.getCurrentSession();
				Consumer consumer = (Consumer) session.get(Consumer.class,dependantId);

				if (consumer != null) {
					consumer.setActive(false);
					consumer.setUpdatedBy(modifyingId);
					consumer.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
					consumer.setDeletedBy(modifyingId);
					consumer.setDeletedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
					consumer.getUser().setActiveStatus(false);
					consumer.getUser().setDeletedBy(modifyingId);
					consumer.getUser().setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					consumer.getUser().setUpdatedBy(modifyingId);
					consumer.getUser().setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					session.update(consumer);
				} else {
					return false;
				}
			}

			if (roleId == 4) {
				Session session = sessionFactory.getCurrentSession();
				Installer installer = (Installer) session.get(Installer.class,
						dependantId);

				if (installer != null) {
					installer.setActive(false);
					installer.setUpdatedBy(modifyingId);
					installer.setUpdatedTS(DateTimeConversionUtils.getDateInUTC(new Date()));
					installer.setDeletedBy(modifyingId);
					installer.setDeletedTS(DateTimeConversionUtils.getDateInUTC(new Date()));
					installer.getUser().setActiveStatus(false);
					installer.getUser().setDeletedBy(modifyingId);
					installer.getUser().setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					installer.getUser().setUpdatedBy(modifyingId);
					installer.getUser().setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					session.update(installer);
				} else {
					return false;
				}
			}

			if (roleId == 5) {
				Session session = sessionFactory.getCurrentSession();
				MaintenanceTechnician technician = (MaintenanceTechnician) session
						.get(MaintenanceTechnician.class, dependantId);

				if (technician != null) {
					technician.setActiveStatus(false);
					technician.setUpdatedBy(modifyingId);
					technician.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					technician.setDeletedBy(modifyingId);
					technician.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					session.update(technician);
				} else {
					return false;
				}
			}

			if (roleId == 6) {
				Session session = getCurrentSession();
				User user = getUserDetailsByUserID(dependantId);
				if (user != null) {
					//user.getNormalCustomer().setSuperCustomer(null);
					//session.delete(user);
					user.setActiveStatus(Boolean.FALSE);
					user.setUpdatedBy(modifyingId);
					user.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					user.setDeletedBy(modifyingId);
					user.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
					session.update(user);
				} else {
					return false;
				}
			}

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserList() {
		List<User> userList = null;
		try {
			String listHQLQuery = "from User";
			// String listHQLQuery =
			// "from User where activeStatus = :activeStatus";
			Query listQuery = sessionFactory.getCurrentSession().createQuery(
					listHQLQuery);
			// listQuery.setParameter("activeStatus", Boolean.TRUE);
			userList = listQuery.list();
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return userList;
	}

	@Override
	public void addNewUser(Customer newCustomer) {
		try {
			Session session = sessionFactory.getCurrentSession();

			session.save(newCustomer);
			/*
			 * // customerDetails.setUser(newCustomer.getUser().getUserId());
			 * customerDetails.setUser(newCustomer.getUser());
			 * session.save(customerDetails);
			 */

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@Override
	public void updateAdminDetails(ContactDetails details) {
		try {
			Session session = sessionFactory.getCurrentSession();

			session.update(details);

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public User availableUserName(String userName) {
		// check available user names
		User foundUser = null;
		Session session = getCurrentSession();
		Query query = session.createQuery("from User where username=:userName");
		query.setParameter("userName", userName);
		List<User> result = query.list();

		if (result != null && !result.isEmpty()) {
			foundUser = result.get(0);
		}
		return foundUser;
	}

	@Override
	public ContactDetails getDetailsByUserID(int userID) {

		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.get(User.class, userID);
		/*
		 * Query contactQuery = sessionFactory.getCurrentSession().createQuery(
		 * "from ContactDetails where user=:user");
		 * contactQuery.setParameter("user", user);
		 */
		return user.getDetails();
	}

	@Override
	public void updatePassword(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.update(user);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> searchUser(String customerSearchCriteria,
			Date portalPlanActiveDate, Date portalPlanExpiryDate,
			Date dataPlanActiveDate, Date dataPlanExpiryDate) {

		List<Customer> searchedList = new ArrayList<Customer>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(Customer.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			// Add step wise criteria

			if (customerSearchCriteria != null && !customerSearchCriteria.trim().isEmpty()) {
					searchCriteria.add(Restrictions.like("customerName", "%"
							+ customerSearchCriteria + "%"));
			}

			if (portalPlanActiveDate != null) {
				searchCriteria.add(Restrictions.ge("portalPlanStartDate",
						DateTimeConversionUtils
						.getPreviousDate(portalPlanActiveDate)));
			}

			if (portalPlanExpiryDate != null) {
				searchCriteria.add(Restrictions.lt("portalPlanExpiryDate",
						DateTimeConversionUtils
						.getNextDate(portalPlanExpiryDate)));
			}

			if (dataPlanActiveDate != null) {
				searchCriteria.add(Restrictions.ge("dataPlanActivatedDate",
						DateTimeConversionUtils
						.getPreviousDate(dataPlanActiveDate)));
			}

			if (dataPlanExpiryDate != null) {
				searchCriteria.add(Restrictions
						.lt("dataPlanExpiryDate", DateTimeConversionUtils
								.getNextDate(dataPlanExpiryDate)));
			}

			searchedList = searchCriteria.list();

			return searchedList;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;
	}

	@Override
	public List<DataPlan> getDataPlan() {

		@SuppressWarnings("unchecked")
		List<DataPlan> dataPlanList = sessionFactory.getCurrentSession()
		.createQuery("from DataPlan").list();
		return dataPlanList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> getCustomerList() {

		Query custometList = sessionFactory.getCurrentSession().createQuery(
				"from Customer");
		// custometList.setParameter("activeStatus", Boolean.TRUE);
		return custometList.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> getActiveCustomerList() {

		Query custometList = sessionFactory.getCurrentSession().createQuery(
				"from Customer where activeStatus = :activeStatus");
		custometList.setParameter("activeStatus", Boolean.TRUE);
		return custometList.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerMeter> getConsumerList(Integer id, String role) {

		List<ConsumerMeter> consumerMeterList = new ArrayList<>();
		if(role.equalsIgnoreCase("customer")){
			Query consumerList = sessionFactory.getCurrentSession().createQuery(
					"from ConsumerMeter where customer_id = :customerId");
			consumerList.setParameter("customerId", id);
			consumerMeterList = consumerList.list();
		}
		if(role.equalsIgnoreCase("consumer")){
			Query consumerList = sessionFactory.getCurrentSession().createQuery(
					"from ConsumerMeter where consumer_id = :consumerId");
			consumerList.setParameter("consumerId", id);
			consumerMeterList = consumerList.list();
		}
		return consumerMeterList;

	}

	@Override
	public User getUserDetailsByUserID(int userID) {

		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from User where userId = :userID");
		contactQuery.setParameter("userID", userID);
		return (User) contactQuery.list().get(0);
	}

	@Override
	public Customer getCustomerDetailsByUser(int userId) {
		Session session = sessionFactory.getCurrentSession();

		User user = (User) session.get(User.class, userId);
		Customer customer = null;
		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from Customer where user=:user");
		contactQuery.setParameter("user", user);
		if (!contactQuery.list().isEmpty()) {
			customer = (Customer) contactQuery.list().get(0);
			Hibernate.initialize(customer.getDataCollector());
			Hibernate.initialize(customer.getTariffPlan());
			customer.getRegion()
			.stream()
			.filter(filtSite -> !filtSite.getSite().isEmpty())
			.forEach(
					x -> {
						x.getSite()
						.stream()
						.filter(filtDC -> !filtDC
								.getDatacollector().isEmpty())
								.forEach(
										getdc -> {
											Hibernate.initialize(getdc
													.getDatacollector());
										});
					});
			Hibernate.initialize(customer.getInstaller());
			Hibernate.initialize(customer.getDistrictUtilityMeter());
			Hibernate.initialize(customer.getNormalCustomer());
			customer.getConsumer().size(); // initialize consumer object so that we can use it in controller
			Hibernate.initialize(customer.getConsumer());
			customer.getConsumer().stream()
			.forEach(n -> { Hibernate.initialize(n.getConsumerMeter()); 
			});

		}
		return customer;
	}

	@Override
	public Customer initializeNormalCustomer(int userId) {
		Session session = sessionFactory.getCurrentSession();

		User user = (User) session.get(User.class, userId);
		Customer customer = null;
		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from Customer where user=:user");
		contactQuery.setParameter("user", user);
		if (!contactQuery.list().isEmpty()) {
			customer = (Customer) contactQuery.list().get(0);
			Hibernate.initialize(customer.getNormalCustomer());
		}
		return customer;
	}

	@Override
	public Customer initializeSiteAndDCForCustomer(int userId) {
		Session session = sessionFactory.getCurrentSession();

		User user = (User) session.get(User.class, userId);
		Customer customer = null;
		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from Customer where user=:user");
		contactQuery.setParameter("user", user);
		if (!contactQuery.list().isEmpty()) {
			customer = (Customer) contactQuery.list().get(0);
			Hibernate.initialize(customer.getDataCollector());
			customer.getRegion()
			.stream()
			.filter(filtSite -> !filtSite.getSite().isEmpty())
			.forEach(
					x -> {
						x.getSite()
						.stream()
						.filter(filtDC -> !filtDC
								.getDatacollector().isEmpty())
								.forEach(
										getdc -> {
											Hibernate.initialize(getdc
													.getDatacollector());
										});
					});
		}
		return customer;
	}

	@Override
	public Consumer getConsumerDetailsByUserId(int userId) {
		Session session = sessionFactory.getCurrentSession();

		User user = (User) session.get(User.class, userId);
		Consumer consumer = null;
		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from Consumer where user=:user");
		contactQuery.setParameter("user", user);
		if (!contactQuery.list().isEmpty()) {
			consumer = (Consumer) contactQuery.list().get(0);
			Hibernate.initialize(consumer.getConsumerMeter()); 
		}
		return consumer;
	}

	@Override
	public boolean updateUser(Customer customer) {

		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(customer);
			return true;
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.warn(e.getMessage());*/
			return false;
		}
	}

	@Override
	public Role getRoleById(int roleId) {

		Query query = sessionFactory.getCurrentSession().createQuery(
				"from Role where roleId = :roleId");
		query.setParameter("roleId", roleId);

		return (Role) query.list().get(0);

	}

	@Override
	public void addNewDataPlan(DataPlan dataplan) {
		try {

			sessionFactory.getCurrentSession().save(dataplan);

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@Override
	public DataPlan getDataPlanById(int dataPlanId) {

		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from DataPlan where dataPlanId = :dataPlanId");
		contactQuery.setParameter("dataPlanId", dataPlanId);
		return (DataPlan) contactQuery.list().get(0);
	}

	@Override
	public void updateDataPlan(DataPlan dataplan) {

		sessionFactory.getCurrentSession().update(dataplan);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataPlan> searchDataplan(int mbPerMonth) {

		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from DataPlan where mbPerMonth = :mbPerMonth");
		contactQuery.setParameter("mbPerMonth", mbPerMonth);

		if (!contactQuery.list().isEmpty()) {
			return contactQuery.list();
		}
		return null;
	}

	@Override
	public void updateCustomerDetails(ContactDetails contactdetails) {

		sessionFactory.getCurrentSession().update(contactdetails);
	}

	@Override
	public void refreshSession(Region region) {
		sessionFactory.getCurrentSession().refresh(region);
	}

	@Override
	public void addNewSite(Site site) {
		try {
			sessionFactory.getCurrentSession().save(site);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@Override
	public List<DataCollector> getDCBySerialNumber(String[] dcList) {
		List<DataCollector> collectors = new ArrayList<>();
		try {
			Session session = sessionFactory.getCurrentSession();
			Query dcQuery = session
					.createQuery("from DataCollector where dcSerialNumber=:dcSerialNumber");
			if (dcList.length > 0) {
				for (int i = 0; i < dcList.length; i++) {
					dcQuery.setParameter("dcSerialNumber", dcList[i]);
					if (!dcQuery.list().isEmpty()
							&& dcQuery.list().get(0) != null)
						collectors.add((DataCollector) dcQuery.list().get(0));
				}
			}
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return collectors;
	}

	@Override
	public void addNewCollector(DataCollector thisCollector) {
		try {
			/* sessionFactory.getCurrentSession().save(thisCollector.getSite()); */
			sessionFactory.getCurrentSession().saveOrUpdate(thisCollector);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@Override
	public Region getSelectedRegionByNameAndCustomer(String selectedRegion,	Customer customer) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("from Region where regionName=:selectedRegion and customer=:customer");
		query.setParameter("selectedRegion", selectedRegion);
		query.setParameter("customer", customer);

		if (query.list().get(0) != null)
			return (Region) query.list().get(0);
		return null;
	}

	@Override
	public void addNewBDC(BoundryDataCollector bdc) {
		Session session = getCurrentSession();
		/*
		 * session.refresh(bdc.getDatacollector());
		 * session.refresh(bdc.getSite());
		 */
		session.save(bdc);
		/* Hibernate.initialize(bdc.getSite().getDatacollector()); */
		/* session.flush(); */
	}

	@Override
	public Installer getInstallerByNameAndCustomer(String installerName,
			Customer customer) {
		Installer ins = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Installer where installerName=:installerName and customer=:customer");
		query.setParameter("installerName", installerName);
		query.setParameter("customer", customer);
		if(!query.list().isEmpty()){
			ins = (Installer)query.list().get(0);
		}	
		return ins;
	}

	@Override
	public List<DataCollector> getSpareDataCollectorList() {
		try {
			@SuppressWarnings("unchecked")
			List<DataCollector> dataCollectorList = sessionFactory
			.getCurrentSession()
			.createQuery("from DataCollector where site is null")
			.list();

			return dataCollectorList;
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return null;

	}

	@Override
	public DataCollector getSpareDataCollectorById(int datacollectorId) {

		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from DataCollector where datacollectorId = :datacollectorId");
		contactQuery.setParameter("datacollectorId", datacollectorId);
		return (DataCollector) contactQuery.list().get(0);

	}

	@Override
	public void updateDataCollector(DataCollector datacollector) {
		Session session = sessionFactory.getCurrentSession();

		session.update(datacollector);
	}

	@Override
	public void addNewDataCollector(DataCollector datacollector) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(datacollector);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCollector> searchDataCollector(String customer,
			String dcSerialNumber) {
		List<DataCollector> searchedList = new ArrayList<DataCollector>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(DataCollector.class, "dc");
			searchCriteria.add(Restrictions.isNull("dc.site"));
			if (customer != null) {
				searchCriteria.createAlias("dc.customer", "c");
				searchCriteria.add(Restrictions.like("c.customerName",
						customer, MatchMode.ANYWHERE));
			}
			if (dcSerialNumber != null) {
				searchCriteria.add(Restrictions.like("dc.dcSerialNumber",
						dcSerialNumber, MatchMode.ANYWHERE));
			}

			searchedList = searchCriteria.list();
			if (searchedList.isEmpty()) {
				return null;
			} else {
				return searchedList;
			}
		} catch (Exception e) {
			log.error("Error {}",e);
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCollector> searchDataCollectorByCustomer(String dcIp,
			String dcSerialNumber, int customerId) {
		// Tatva
		List<DataCollector> dcList = new ArrayList<DataCollector>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				DataCollector.class, "dc");
		criteria.add(Restrictions.eq("dc.customer.customerId", customerId));
		if (dcSerialNumber != null) {
			criteria.add(Restrictions.like("dc.dcSerialNumber", dcSerialNumber,
					MatchMode.ANYWHERE));
		}
		if (dcIp != null) {
			criteria.add(Restrictions.like("dc.dcIp", dcIp, MatchMode.ANYWHERE));
		}

		dcList = criteria.list();

		/*
		 * Query contactQuery = sessionFactory.getCurrentSession().createQuery(
		 * "from DataCollector where  customer_id=:customerId AND dcSerialNumber = :dcSerialNumber AND dcIp=:dcIp"
		 * ); contactQuery.setParameter("dcSerialNumber", dcSerialNumber);
		 * contactQuery.setParameter("dcIp", dcIp);
		 * contactQuery.setParameter("customerId", customerId);
		 * 
		 * if(contactQuery.list().size() >0){ return contactQuery.list(); }
		 */

		if (dcList.isEmpty()) {
			return null;
		} else {
			return dcList;
		}

	}

	@Override
	public Customer getOnlyCustomerByUserId(int userId) {
		Customer customer = null;
		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from Customer where user.userId = :userId");
		contactQuery.setParameter("userId", userId);
		if(!contactQuery.list().isEmpty()){
			customer =  (Customer) contactQuery.list().get(0);
		}
		return customer;
	}

	@Override
	public Consumer getOnlyConsumerByUserId(int userId) {

		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from Consumer where user.userId = :userId");
		contactQuery.setParameter("userId", userId);
		return (Consumer) contactQuery.list().get(0);

	}

	@Override
	public Customer getCustomerDetailsByCustomerName(String customerName) {

		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from Customer where customerName = :customerName");
		contactQuery.setParameter("customerName", customerName);
		if (!contactQuery.list().isEmpty()) {
			return (Customer) contactQuery.list().get(0);
		}
		return null;
	}

	@Override
	public Site getSiteDataBySiteId(String siteId) {
		Site site = (Site) sessionFactory.getCurrentSession().get(Site.class,
				Integer.parseInt(siteId));
		Hibernate.initialize(site.getBoundrydatacollector());
		Hibernate.initialize(site.getDatacollector());
		if (site.getDatacollector().stream().findFirst().isPresent())
			Hibernate.initialize(site.getDatacollector().stream().findFirst()
					.get().getInstaller());

		if (site.getBoundrydatacollector() != null) {
			site.getBoundrydatacollector()
			.stream()
			.filter(filtDC -> (filtDC.getDatacollector() != null))
			.forEach(
					getdc -> {
						Hibernate.initialize(getdc.getDatacollector()
								.getInstaller());
					});
		}
		return site;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BoundryDataCollector> getAllBDCFromSiteId(Customer customer,
			String regionName, String[] bdc, Site site, String[] assignedDc,
			String installer) {
		Session session = sessionFactory.getCurrentSession();

		// Set new assigned DC
		List<DataCollector> dcList = new ArrayList<DataCollector>();

		if (assignedDc != null) {
			dcList = getDCBySerialNumber(assignedDc);
			for (int i = 0; i < assignedDc.length; i++) {
				DataCollector thisCollector = dcList.get(i);
				thisCollector.setCustomer(customer);

				if (thisCollector.getCreatedTs() == null) {
					thisCollector.setCreatedTs(new Date());
				} else {
					thisCollector.setUpdatedTs(new Date());
				}
				thisCollector.setSite(site);
				if (installer != null) {
					Installer ins = getInstallerByNameAndCustomer(installer,
							customer);
					thisCollector.setInstaller(ins);
				}
				addNewCollector(thisCollector);
			}
		}
		// Getting region by name
		Region siteRegion = getRegionByName(customer,regionName);
		if (siteRegion != null) {
			site.setRegion(siteRegion);
		}

		session.update(site);
		Query getBDCBySiteQuery = session
				.createQuery("from BoundryDataCollector where site=:site");
		getBDCBySiteQuery.setParameter("site", site);

		List<BoundryDataCollector> currentSiteAllBDC = getBDCBySiteQuery.list();

		if (!currentSiteAllBDC.isEmpty()) {
			int size = getBDCBySiteQuery.list().size();
			for (int i = 0; i < size; i++) {
				BoundryDataCollector bdcList = currentSiteAllBDC.get(i);
				bdcList.setSite(null);
				bdcList.setDatacollector(null);
				session.update(bdcList);
				session.delete(bdcList);
			}
		}

		// Setting up new bdc from edit field

		// First we get BoundryDataCollector entity from their serial number

		Query getDCFromSerialNumber = session
				.createQuery("from DataCollector where dcSerialNumber=:dcSerialNumber");

		if (bdc != null) {
			for (int i = 0; i <= bdc.length - 1; i++) {
				BoundryDataCollector bdcList = new BoundryDataCollector();
				getDCFromSerialNumber.setParameter("dcSerialNumber", bdc[i]);
				DataCollector dc = (DataCollector) getDCFromSerialNumber.list()
						.get(0);
				bdcList.setDatacollector(dc);
				bdcList.setSite(site);
				session.saveOrUpdate(bdcList);
			}
		}

		return currentSiteAllBDC;
	}

	@Override
	public ContactDetails getContactDetailsByCustomerID(int customerId) {
		Customer customer = (Customer) sessionFactory.getCurrentSession().get(
				Customer.class, customerId);
		return customer.getUser().getDetails();
	}

	@Override
	public Customer getCustomerDetailsByCustomerId(int customerId) {
		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from Customer where customerId = :customerID");
		contactQuery.setParameter("customerID", customerId);
		return (Customer) contactQuery.list().get(0);
	}

	@Override
	public boolean deleteSite(int siteId, int customerId) {

		try {
			Session session = sessionFactory.getCurrentSession();
			// Site site = (Site) session.get(Site.class,siteId);
			Site site = null;

			String customerHQL = "from Site where siteId=:siteId";
			Query siteQuery = sessionFactory.getCurrentSession().createQuery(customerHQL);
			siteQuery.setParameter("siteId", siteId);

			if (!siteQuery.list().isEmpty() ) {
				site = (Site) siteQuery.list().get(0);
				site.setActiveStatus(false);
				site.setUpdatedBy(customerId);
				site.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
				site.setDeletedBy(customerId);
				site.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

				if(site.getDatacollector() != null && !site.getDatacollector().isEmpty()){
					Iterator<DataCollector> datacollectorIterator = site.getDatacollector().iterator();
					while(datacollectorIterator.hasNext()){
						DataCollector datacollector = (DataCollector)datacollectorIterator.next();
						datacollector.setActive(Boolean.FALSE);
						datacollector.setUpdatedBy(customerId);
						datacollector.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
						datacollector.setDeletedBy(customerId);
						datacollector.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

						if(datacollector.getConsumerMeters() != null && !datacollector.getConsumerMeters().isEmpty()){
							Iterator<ConsumerMeter> meterIterator = datacollector.getConsumerMeters().iterator();
							while(meterIterator.hasNext()){
								ConsumerMeter consumerMeter = (ConsumerMeter) meterIterator.next();
								consumerMeter.setActive(Boolean.FALSE);
								consumerMeter.setUpdatedBy(customerId);
								consumerMeter.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
								consumerMeter.setDeletedBy(customerId);
								consumerMeter.setDeletedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
							}
							datacollector.setConsumerMeters(datacollector.getConsumerMeters());
						}
					}
					site.setDatacollector(site.getDatacollector());
				}

				session.update(site);
			} else {
				return false;
			}

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Site> searchSiteByNameOrRegion(String siteSearchCriteria,
			String siteSearchRegion) {

		List<Site> searchedList = new ArrayList<Site>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(Site.class);

			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			if (siteSearchCriteria != null
					&& !siteSearchCriteria.trim().isEmpty()) {
				searchCriteria.add(Restrictions.like("siteName",
						siteSearchCriteria, MatchMode.ANYWHERE));
			}

			if (siteSearchRegion != null && !siteSearchRegion.trim().isEmpty()) {
				searchCriteria.createCriteria("region", "r");
				searchCriteria.add(Restrictions.like("r.regionName",
						siteSearchRegion, MatchMode.ANYWHERE));
			}

			searchedList = searchCriteria.list();

			return searchedList;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;

	}

	@Override
	public boolean checkForCustomerCode(String customerCode) {

		String query = "from Customer where customerCode=:customerCode";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("customerCode", customerCode);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;
	}

	@Override
	public void addNewRegion(Region region) {
		try {
			sessionFactory.getCurrentSession().save(region);

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@Override
	public List<Region> getRegionList() {

		@SuppressWarnings("unchecked")
		List<Region> regionList = sessionFactory.getCurrentSession()
		.createQuery("from Region").list();
		return regionList;
	}

	@Override
	public Region getRegionById(int regionId) {

		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from Region where regionId = :regionId");
		contactQuery.setParameter("regionId", regionId);
		return (Region) contactQuery.list().get(0);
	}

	@Override
	public void updateRegion(Region regionObj) {
		sessionFactory.getCurrentSession().update(regionObj);
	}

	@Override
	public boolean checkForRegion(String regionName) {

		String query = "from Region where regionName=:regionName";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("regionName", regionName);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Region> searchRegion(String regionName, int customerId) {

		List<Region> searchedList = new ArrayList<Region>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(Region.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			searchedList = searchCriteria
					.add(Restrictions.like("regionName", regionName,
							MatchMode.ANYWHERE))
							.add(Restrictions.eq("customer.customerId", customerId))
							.list();

			return searchedList;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;
	}

	@Override
	public List<Region> getRegionListByCustomerId(int customerId) {

		Query query = sessionFactory.getCurrentSession().createQuery(
				"from Region where customer.customerId = :customerId");
		query.setParameter("customerId", customerId);

		@SuppressWarnings("unchecked")
		List<Region> regionList = query.list();

		if(regionList != null && !regionList.isEmpty())
			regionList.get(0).getSite();

		return regionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DistrictMeterTransaction> getDUMeterListByCustomerId(int customerId) {

		/*Query query = sessionFactory
				.getCurrentSession()
				.createQuery("from DistrictUtilityMeter where customer.customerId = :customerId");
		query.setParameter("customerId", customerId);
		 */
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DistrictMeterTransaction.class, "dmt");
		criteria.createAlias("districtUtilityMeter", "dm").add(Restrictions.eq("dm.customer.customerId", customerId));
		List<DistrictMeterTransaction> DUMeterList = criteria.list();
		return DUMeterList;
	}

	@Override
	public void addDUMeter(DistrictUtilityMeter districtUtilityMeter) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(districtUtilityMeter);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@Override
	public DistrictUtilityMeter getDUMeterListById(int districtUtilityMeterId) {

		Query contactQuery = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from DistrictUtilityMeter where districtUtilityMeterId = :districtUtilityMeterId");
		contactQuery.setParameter("districtUtilityMeterId",
				districtUtilityMeterId);
		return (DistrictUtilityMeter) contactQuery.list().get(0);
	}

	@Override
	public void updateDUMeter(DistrictUtilityMeter districtUtilityMeter) {
		try{
			sessionFactory.getCurrentSession().update(districtUtilityMeter);
		} catch(Exception e){
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}
	}

	@Override
	public boolean checkForDUMeter(String districtUtilityMeterSerialNumber) {

		String query = "from DistrictUtilityMeter where districtUtilityMeterSerialNumber=:districtUtilityMeterSerialNumber";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("districtUtilityMeterSerialNumber",
				districtUtilityMeterSerialNumber);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;

	}

	@Override
	public DistrictUtilityMeter getDUMeterBySerialNumber(String districtUtilityMeterSerialNumber) {

		DistrictUtilityMeter districtUtilityMeter = null;

		try{
			String query = "from DistrictUtilityMeter where districtUtilityMeterSerialNumber=:districtUtilityMeterSerialNumber";
			Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
			hqlDuery.setParameter("districtUtilityMeterSerialNumber",districtUtilityMeterSerialNumber);
			districtUtilityMeter = (DistrictUtilityMeter) hqlDuery.list().get(0);

		} catch(Exception e) {
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}
		if(districtUtilityMeter != null && districtUtilityMeter.getDistrictMeterTransactions() != null && 
				!districtUtilityMeter.getDistrictMeterTransactions().isEmpty())
			Hibernate.initialize(districtUtilityMeter.getDistrictMeterTransactions());
		return districtUtilityMeter;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DistrictMeterTransaction> searchDUMeter(String DUMeterSerialNumber,int customerId) {

		List<DistrictMeterTransaction> searchedList = new ArrayList<DistrictMeterTransaction>();

		searchedList = null;
		try {

			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DistrictMeterTransaction.class, "dmt");
			//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.createAlias("districtUtilityMeter", "dm").add(Restrictions.eq("dm.customer.customerId", customerId))
			.add(Restrictions.like("dm.districtUtilityMeterSerialNumber",	DUMeterSerialNumber, MatchMode.ANYWHERE));
			searchedList = criteria.list();
			/*Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(DistrictUtilityMeter.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			searchedList = searchCriteria.add(Restrictions.like("districtUtilityMeterSerialNumber",	DUMeterSerialNumber, MatchMode.ANYWHERE)).add(Restrictions.eq("customer.customerId", customerId)).list();

			return searchedList;
			 */
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;
	}

	@Override
	public boolean deleteDUMeter(int dUMeterId) {

		try {
			Session session = sessionFactory.getCurrentSession();
			DistrictUtilityMeter districtUtilityMeter = (DistrictUtilityMeter) session
					.get(DistrictUtilityMeter.class, dUMeterId);

			if (districtUtilityMeter != null) {

				session.delete(districtUtilityMeter);

			} else
				return false;
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
			return false;
		}
		return true;
	}

	@Override
	public List<ConsumerMeter> getConsumerListByCustomerId(int customerId) {

		Customer c = (Customer) sessionFactory.getCurrentSession().get(
				Customer.class, customerId);
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(ConsumerMeter.class, "con")
				.createAlias("con.user", "usr")
				.createAlias("usr.details", "dtl")
				.add(Restrictions.eq("con.customer", c));

		List<ConsumerMeter> list = criteria.list();

		return list;
	}

	@Override
	public List<Installer> getInstallerByCustomerId(int customerId) {

		List<Installer> list = null;

		Query q = sessionFactory.getCurrentSession().createQuery(
				"from Installer where customer.customerId=:customerId");
		q.setParameter("customerId", customerId);

		list = q.list();
		return list;
	}

	@Override
	public boolean assignInstallerToConumers(List<ConsumerMeter> consumerList,
			int installerId) {
		int modifications = 0;
		boolean result = false;
		Iterator<ConsumerMeter> conIterator = consumerList.iterator();

		while (conIterator.hasNext()) {
			ConsumerMeter con = conIterator.next();

			Query q = sessionFactory
					.getCurrentSession()
					.createQuery(
							"update ConsumerMeter set installer.installerId=:installerId where consumerMeterId=:consumerMeterId");
			q.setInteger("installerId", installerId);
			q.setParameter("consumerMeterId", con.getConsumerMeterId());
			modifications = q.executeUpdate();
		}

		if (modifications == 0) {
			result = false;
		}
		if (modifications == 1) {
			result = true;
		}
		return result;

	}

	/*
	 * @Override public List<ConsumerMeter> searchConsumerByStreetName(int
	 * customerId, String streetName) {
	 * 
	 * Criteria criteria =
	 * sessionFactory.getCurrentSession().createCriteria(ConsumerMeter
	 * .class,"con") .add(Restrictions.eq("con.customer.customerId",
	 * customerId))
	 * .add(Restrictions.eq("con.registerId",Integer.parseInt(streetName)))
	 * .createAlias("con.user", "usr") .createAlias("usr.details", "dtl")
	 * .add(Restrictions.eq("dtl.streetName",streetName))
	 * .add(Restrictions.isNull("con.installer"))
	 * .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	 * 
	 * List<ConsumerMeter> list = criteria.list();
	 * 
	 * return list; }
	 */

	public List<ConsumerMeter> searchConsumerMeterByRegisterId(int customerId,
			int registerId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(ConsumerMeter.class, "con")
				.add(Restrictions.eq("con.customer.customerId", customerId))
				.add(Restrictions.eq("con.registerId", registerId))
				.add(Restrictions.isNull("con.installer"))
				.add(Restrictions.isNull("con.consumer"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<ConsumerMeter> list = criteria.list();

		return list;
	}

	@Override
	public List<ConsumerMeter> getConsumersByInstaller(int installerId) {

		List<ConsumerMeter> con = null;
		Session session = sessionFactory.getCurrentSession();
		// get all EP of perticular installer who has no DC (not commissioned)
		Query query = session
				.createQuery("from ConsumerMeter where installer.installerId=:installerId AND dataCollector is null");
		query.setParameter("installerId", installerId);
		con = query.list();
		return con;
	}

	@Override
	public List<DataCollector> getDCByInstallerId(int installerId) {

		List<DataCollector> dcList = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from DataCollector where installer_id=:installerId AND site is not null AND networkId is null");
		query.setParameter("installerId", installerId);
		dcList = query.list();
		return dcList;
	}

	@Override
	public List<DataCollector> searchDataCollectorByCustomerAndIp(String dcIp,
			String customerId) {
		Query contactQuery = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from DataCollector where dcIp = :dcIp AND customer.customerId=:customerId AND installer is null AND site is not null AND networkId is null");

		contactQuery.setParameter("dcIp", dcIp);
		contactQuery.setParameter("customerId", Integer.parseInt(customerId));

		if (!contactQuery.list().isEmpty()) {
			return contactQuery.list();
		}
		return null;
	}

	@Override
	public boolean assignSelectedDCToInstaller(int datacollectorId,
			int installerId) {

		int modifications = 0;
		boolean result = false;
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"update DataCollector set installer.installerId=:installerId where datacollectorId=:datacollectorId");
		q.setInteger("installerId", installerId);
		q.setParameter("datacollectorId", datacollectorId);
		modifications = q.executeUpdate();

		if (modifications == 0) {
			result = false;
		}
		if (modifications == 1) {
			result = true;
		}
		return result;

	}

	@Override
	public boolean saveTariffTransaction(TariffTransaction tariffTrans) {
		try {
			Session session = sessionFactory.openSession();
			session.save(tariffTrans);
			return true;
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TariffPlan> getTariffDataByCustomer(Customer currentCustomer) {

		Session session = getCurrentSession();
		String hqlQuery = "from TariffPlan where customer=:customer";

		Query query = session.createQuery(hqlQuery);
		query.setParameter("customer", currentCustomer);

		List<TariffPlan> tariffPlanList = query.list();

		return tariffPlanList;
	}

	@Override
	public List<TariffPlan> searchTariffByNameAndCustomer(
			Customer currentCustomer, String tariffName) {

		Session session = getCurrentSession();

		Criteria searchTariffCriteria = session
				.createCriteria(TariffPlan.class);

		searchTariffCriteria.add(Restrictions.eq("customer", currentCustomer));
		searchTariffCriteria.add(Restrictions.like("tariffPlanName", tariffName
				+ "%"));

		List<TariffPlan> tariffPlanList = searchTariffCriteria.list();

		return tariffPlanList;
	}

	@Override
	public boolean deleteTariffwithId(String id) {
		try {
			Session session = getCurrentSession();
			TariffPlan tp = (TariffPlan) session.get(TariffPlan.class,
					Integer.parseInt(id));
			if (tp != null) {

				String tariffTransactionDelete = "DELETE FROM TariffTransaction "
						+ "WHERE tariffPlan = :tp";
				Query tariffTransactionDeleteQuery = session
						.createQuery(tariffTransactionDelete);
				tariffTransactionDeleteQuery.setParameter("tp", tp);
				tariffTransactionDeleteQuery.executeUpdate();
				String tariffDelete = "DELETE FROM TariffPlan "
						+ "WHERE tariffPlanId = :id";
				Query tariffDeleteQuery = session.createQuery(tariffDelete);
				tariffDeleteQuery.setParameter("id", Integer.parseInt(id));
				tariffDeleteQuery.executeUpdate();
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.warn(e.getMessage());*/
			return false;
		}
		return true;
	}

	@Override
	public void updateDCsBySiteId(String siteId, int mri, int nri,
			Map<Integer, Double> mbPermonthAcrossASite) {
		Session session = getCurrentSession();
		Site site = (Site) session.get(Site.class, Integer.parseInt(siteId));
		for (DataCollector dc : site.getDatacollector()) {
			dc.setMeterReadingInterval(mri);
			dc.setNetworkReadingInterval(nri);
			if (dc.getTotalEndpoints() != null)
				dc.setMbPerMonth(mbPermonthAcrossASite.get(dc.getDatacollectorId()));
		}
		session.update(site);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Site> searchBysiteIdOrRegionName(int sitId, String regionName) {
		List<Site> searchedList = new ArrayList<Site>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(Site.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			// Add step wise criteria

			if (sitId > 0) {
				searchCriteria.add(Restrictions.eq("siteId", sitId));
			}
			if (regionName != null && regionName.length() > 0) {
				searchCriteria.createCriteria("region", "r");
				searchCriteria.add(Restrictions.like("r.regionName", regionName
						+ "%"));
			}

			if (!searchCriteria.list().isEmpty()) {
				for (Object obj : searchCriteria.list()) {
					Site site = (Site) obj;
					Hibernate.initialize(site.getDatacollector());

				}
			}
			searchedList = searchCriteria.list();
			return searchedList;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;
	}

	/*
	 * @Override public List<Installer> getInstallersByCustomerId(int
	 * customerId) {
	 * 
	 * Query query = sessionFactory.getCurrentSession().createQuery(
	 * "from Installer where customer.customerId=:customerId");
	 * query.setParameter("customerId",customerId);
	 * 
	 * List<Installer> installers = query.list();
	 * 
	 * 
	 * return installers; }
	 */
	@Override
	public boolean insertInstaller(Installer installer) {

		Boolean result = false;

		try {
			Session session = sessionFactory.getCurrentSession();

			session.save(installer);

			result = true;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}

		return result;
	}

	@Override
	public Installer getInstallerById(int installerId) {

		Query query = sessionFactory.getCurrentSession().createQuery(
				"From Installer where installerId=:installerId");
		query.setParameter("installerId", installerId);
		Installer ins = null;
		ins = (Installer) query.list().get(0);

		if (ins != null) {
			return ins;
		}
		return null;
	}

	@Override
	public Boolean deleteInstaller(int installerId) {

		try {
			Session session = sessionFactory.getCurrentSession();
			Installer installer = (Installer) session.get(Installer.class,
					installerId);

			if (installer != null) {
				session.delete(installer);
			} else
				return false;
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
			return false;
		}
		return true;
	}

	@Override
	public List<Installer> searchInstallerByCriteria(String criteriaString,
			int customerId) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Installer.class, "ins")
				.add(Restrictions.eq("ins.customer.customerId", customerId))
				.createAlias("ins.user", "usr")
				.createAlias("usr.details", "dtl");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (criteriaString.contains(" ")) {
			log.info("Contains space");
			String[] nameArray = criteriaString.split(" ");
			criteria.add(Restrictions.like("dtl.firstName", nameArray[0],
					MatchMode.ANYWHERE));
			criteria.add(Restrictions.like("dtl.lastname", nameArray[1],
					MatchMode.ANYWHERE));
		} else {
			criteria.add(Restrictions.or(Restrictions.like("dtl.firstName",
					criteriaString, MatchMode.ANYWHERE), Restrictions.like(
							"dtl.lastname", criteriaString, MatchMode.ANYWHERE)));
		}

		List<Installer> list = null;
		list = criteria.list();
		if (!list.isEmpty()) {
			return list;
		}
		return null;
	}

	@Override
	public void insertDataCollector(DataCollector datacollector) {
		Session session = getCurrentSession();
		session.save(datacollector);
	}

	@Override
	public boolean insertTechnician(MaintenanceTechnician technician) {

		Boolean result = false;

		try {
			Session session = sessionFactory.getCurrentSession();

			session.save(technician);

			result = true;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}

		return false;
	}

	@Override
	public void updateTechnician(MaintenanceTechnician technician) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(technician);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}


	@Override
	public void updateInstaller(Installer installer) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(installer);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());
			e.printStackTrace();*/
		}
	}

	@Override
	public List<MaintenanceTechnician> getTechnicianListByCustomerId(
			int customerId) {

		List<MaintenanceTechnician> list = null;

		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from MaintenanceTechnician where customer.customerId=:customerId");
		q.setParameter("customerId", customerId);

		list = q.list();

		return list;
	}

	@Override
	public MaintenanceTechnician getTechnicianById(int maintenanceTechnicianId) {

		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"From MaintenanceTechnician where maintenanceTechnicianId=:maintenanceTechnicianId");
		query.setParameter("maintenanceTechnicianId", maintenanceTechnicianId);
		MaintenanceTechnician technician = null;
		technician = (MaintenanceTechnician) query.list().get(0);

		if (technician != null) {
			return technician;
		}
		return null;
	}

	@Override
	public ContactDetails getContactDetailsByID(int contactDetailsId) {

		Query query = sessionFactory.getCurrentSession().createQuery(
				"From ContactDetails where contactDetailsId=:contactDetailsId");
		query.setParameter("contactDetailsId", contactDetailsId);
		ContactDetails con = null;
		con = (ContactDetails) query.list().get(0);

		if (con != null) {
			return con;
		}
		return null;

	}

	@Override
	public List<MaintenanceTechnician> searchTechnicianByCriteria(
			String searchCriteria, int customerId) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(MaintenanceTechnician.class, "mt")
				.add(Restrictions.eq("mt.customer.customerId", customerId))
				.createAlias("mt.contactdetails", "dtl");

		if (searchCriteria.contains(" ")) {
			log.info("Contains space");
			String[] nameArray = searchCriteria.split(" ");
			criteria.add(Restrictions.like("dtl.firstName", nameArray[0],
					MatchMode.ANYWHERE));
			criteria.add(Restrictions.like("dtl.lastname", nameArray[1],
					MatchMode.ANYWHERE));
		} else {
			criteria.add(Restrictions.or(Restrictions.like("dtl.firstName",
					searchCriteria, MatchMode.ANYWHERE), Restrictions.like(
							"dtl.lastname", searchCriteria, MatchMode.ANYWHERE)));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<MaintenanceTechnician> list = null;
		list = criteria.list();
		if (!list.isEmpty()) {
			return list;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Consumer> getConsumerUserListByCustomerId(int customerId) {

		List<Consumer> result = null;

		Query consumerList = sessionFactory.getCurrentSession().createQuery(
				"from Consumer where customer.customerId = :customerId");
		consumerList.setParameter("customerId", customerId);

		result = consumerList.list();
		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TariffPlan> getTariffPlanList(int customerId) {

		List<TariffPlan> result = null;

		Query tariffPlanList = sessionFactory.getCurrentSession().createQuery(
				"from TariffPlan where customer.customerId = :customerId");
		tariffPlanList.setParameter("customerId", customerId);

		result = tariffPlanList.list();
		return result;

	}

	@Override
	public boolean checkForConsumerAccNumber(String consumerAccountNumber) {

		String query = "from Consumer where consumerAccountNumber=:consumerAccountNumber";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("consumerAccountNumber", consumerAccountNumber);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;
	}

	@Override
	public void insertNewConsumer(Consumer consumer) {

		try {
			Session session = sessionFactory.getCurrentSession();

			session.save(consumer);

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Consumer> searchConsumerByAccNum(String consumerAccNumInput,
			int customerId) {

		List<Consumer> searchedList = new ArrayList<Consumer>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(Consumer.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			searchedList = searchCriteria
					.add(Restrictions.like("consumerAccountNumber",
							consumerAccNumInput, MatchMode.ANYWHERE))
							.add(Restrictions.eq("customer.customerId", customerId))
							.list();

			return searchedList;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;

	}

	@Override
	public TariffPlan getTariffPlanById(int tariffPlanId) {

		Query query = sessionFactory.getCurrentSession().createQuery(
				"from TariffPlan where tariffPlanId = :tariffPlanId");
		query.setParameter("tariffPlanId", tariffPlanId);

		return (TariffPlan) query.list().get(0);

	}

	@Override
	public Consumer getConsumerByConsumerID(int consumerId) {

		Query query = sessionFactory.getCurrentSession().createQuery(
				"from Consumer where consumerId = :consumerId");
		query.setParameter("consumerId", consumerId);

		return (Consumer) query.list().get(0);

	}

	@Override
	public boolean updateConsumerUser(Consumer consumer) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(consumer);
			return true;
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
			return false;
		}
	}

	@Override
	public void updateConsumerMeterUsingConsumerIdAndTariffId(
			Consumer consumer, TariffPlan oldPlan, TariffPlan tp) {

		Session session = getCurrentSession();
		Query query = session
				.createQuery("from ConsumerMeter where consumer=:consumer");
		query.setParameter("consumer", consumer);

		List<ConsumerMeter> list = query.list();

		list.forEach(n -> {
			n.setTariffPlan(tp);
			session.update(n);
		});

	}

	@Override
	public boolean checkForDataPlan(int mbPerMonth) {

		String query = "from DataPlan where mbPerMonth=:mbPerMonth";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("mbPerMonth", mbPerMonth);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;

	}

	@Override
	public List<Currency> getCurrencyList() {

		Session session = getCurrentSession();
		String hqlQuery = "from Currency";
		Query query = session.createQuery(hqlQuery);

		List<Currency> list = new ArrayList<Currency>();
		list = query.list();
		return list;
	}

	@Override
	public List<Country> getCountryList() {

		Session session = getCurrentSession();
		String hqlQuery = "from Country";
		Query query = session.createQuery(hqlQuery);

		List<Country> list = new ArrayList<Country>();
		list = query.list();
		return list;
	}

	@Override
	public void saveSite(Site site) {
		Session session = getCurrentSession();
		session.save(site);
	}

	@Override
	public List<ConsumerMeter> searchConsumerByStreetName(int customerId,
			String streetName) {
		return null;
	}

	@Override
	public Currency getCurrencyById(String currencyId) {
		Session session = getCurrentSession();
		Currency currency = (Currency) session.get(Currency.class,
				Integer.parseInt(currencyId));
		return currency;
	}

	@Override
	public Country getCountryById(String countryId) {
		Session session = getCurrentSession();
		Country country = (Country) session.get(Country.class,
				Integer.parseInt(countryId));
		return country;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Country> searchCountryByName(String searchCountryName) {

		List<Country> searchedList = new ArrayList<Country>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(Country.class);

			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			searchCriteria.add(Restrictions.like("countryName",
					searchCountryName, MatchMode.ANYWHERE));

			searchedList = searchCriteria.list();

			return searchedList;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Currency> searchCurrencyByName(String searchCurrencyName) {

		List<Currency> searchedList = new ArrayList<Currency>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(Currency.class);

			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			searchCriteria.add(Restrictions.like("currencyName",
					searchCurrencyName, MatchMode.ANYWHERE));

			searchedList = searchCriteria.list();

			return searchedList;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;

	}

	@Override
	public boolean checkForCountryName(String countryName) {

		String query = "from Country where countryName=:countryName";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("countryName", countryName);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;

	}

	@Override
	public boolean checkForCountryCode(String countryCode) {

		String query = "from Country where countryCode=:countryCode";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("countryCode", countryCode);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;

	}

	@Override
	public void addNewCountry(Country country) {

		try {
			sessionFactory.getCurrentSession().save(country);

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}

	}

	@Override
	public void updateCountry(Country countryobj) {

		sessionFactory.getCurrentSession().update(countryobj);

	}

	@Override
	public Country getCountryByName(String countryName) {

		Query query = getCurrentSession().createQuery(
				"from Country where countryName=:countryName");
		query.setParameter("countryName", countryName);
		return (Country) query.list().get(0);

	}

	@Override
	public boolean checkForCurrencyName(String currencyName) {

		String query = "from Currency where currencyName=:currencyName";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("currencyName", currencyName);

		if (!hqlDuery.list().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void addNewCurrency(Currency currency) {

		try {
			sessionFactory.getCurrentSession().save(currency);

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}

	}

	@Override
	public void updateCurrency(Currency currencyobj) {

		sessionFactory.getCurrentSession().update(currencyobj);

	}

	@Override
	public Installer getInstallerByUserId(int userId) {

		Session session = sessionFactory.getCurrentSession();

		User user = (User) session.get(User.class, userId);
		Installer installer = null;

		Query contactQuery = session
				.createQuery(
				"from Installer where user=:user");
		contactQuery.setParameter("user", user);
		if (!contactQuery.list().isEmpty()) {
			installer = (Installer) contactQuery.list().get(0);
		}
		return installer;

	}

	@Override
	public boolean updateInstallerDetails(ContactDetails contactDetails) {

		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(contactDetails);
			return true;
		} catch (Exception e) {
			log.error("Error {}",e);
			return false;
		}

	}

	@Override
	public Role getRoleByname(String name) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from Role where roleName = :roleName");
		query.setParameter("roleName", name.toLowerCase());
		return (Role) query.list().get(0);
	}

	@Override
	public Consumer getConsumerByConsumerAccNo(String consumerAccNo) {
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from Consumer where consumerAccountNumber = :consumerAccountNumber");
		query.setParameter("consumerAccountNumber", consumerAccNo);
		if (!query.list().isEmpty()) {
			return (Consumer) query.list().get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConsumerMeter getConsumerMetetByRegisterId(String registerId,int installerId) {
		try{
			Query query = null;
			List<ConsumerMeter> list = new ArrayList<>();
			if(installerId != 0){
				query = sessionFactory.getCurrentSession().createQuery("from ConsumerMeter where registerId = :registerId and installer.installerId=:installerId ");
				query.setParameter("registerId",registerId);
				query.setParameter("installerId", installerId);
			}else if(installerId == 0){
				query = sessionFactory.getCurrentSession().createQuery("from ConsumerMeter where registerId = :registerId");
				query.setParameter("registerId",registerId);
			}
			if(query != null){
				list = query.list();
				if(!list.isEmpty()){
					ConsumerMeter consumerMeter = (ConsumerMeter) query.list().get(0);
					Hibernate.initialize(consumerMeter.getDataCollector());
					return consumerMeter;
				}
			}
			return null;
		} catch (Exception e) {
			log.error("Error {}",e);
			log.error("Could not Get Endpoint");
			return null;
		}
	}

	@Override
	public boolean updateConsumerMeter(ConsumerMeter consumerMeter) {

		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(consumerMeter);
			return true;
		} catch (Exception e) {
			log.error("Error {}",e);
			return false;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public Consumer getValidConsumer(String customerCode,
			String consumerAccountNumber, String zipcode) {

		Query contactQuery = sessionFactory.getCurrentSession().createQuery(
				"from Consumer where consumerAccountNumber = :consumerAccountNumber AND"
						+ " customer.customerCode = :customerCode ");
		contactQuery.setParameter("customerCode", customerCode);
		contactQuery.setParameter("consumerAccountNumber",
				consumerAccountNumber);

		if (!contactQuery.list().isEmpty()) {

			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ConsumerMeter where consumer.consumerAccountNumber = :consumerAccountNumber"
									+ " AND zipcode = :zipcode");
			query.setParameter("zipcode", zipcode);
			query.setParameter("consumerAccountNumber", consumerAccountNumber);

			if (!query.list().isEmpty())
				return (Consumer) contactQuery.list().get(0);
			else
				return null;
		}
		return null;
	}

	@Override
	public Customer getCustomerByCustomerCode(String customerCode) {

		Query query = sessionFactory.getCurrentSession().createQuery(
				"from Customer where customerCode = :customerCode");
		query.setParameter("customerCode", customerCode);

		if (!query.list().isEmpty()) {
			return (Customer) query.list().get(0);
		} else {
			return null;
		}

	}

	@Override
	public boolean updateUserEntity(User editNormalUser) {
		try {
			getCurrentSession().update(editNormalUser);
			return true;
		} catch (Exception e) {
			log.error("Error {}",e);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> searchUserListByUserId(String userId,Customer superCustomer) {
		Criteria searchCriteria = sessionFactory.getCurrentSession()
				.createCriteria(User.class)
				.createAlias("normalCustomer", "normalCustomer");
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (userId != null && !userId.trim().isEmpty() && superCustomer != null) {
			searchCriteria.add(Restrictions
					.like("userName", "%" + userId + "%"));
			searchCriteria.add(Restrictions.eq("normalCustomer.superCustomer",
					superCustomer));
		}
		List<User> list = new ArrayList<User>();
		list = searchCriteria.list();
		return list;
	}

	@Override
	public boolean checkForZipcode(String zipcode, String consumerAccountNumber) {

		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from ConsumerMeter where consumer.consumerAccountNumber = :consumerAccountNumber"
								+ " AND zipcode = :zipcode");
		query.setParameter("zipcode", zipcode);
		query.setParameter("consumerAccountNumber", consumerAccountNumber);

		if (!query.list().isEmpty())
			return true;
		else
			return false;
	}

	@Override
	public void updateSiteForSiteManagement(Site site) {
		Session session = getCurrentSession();

		Hibernate.initialize(site);
		site.getDatacollector().forEach(n -> {
			Hibernate.initialize(n);
			session.update(n);
		});
		/*
		 * site.getBoundrydatacollector().forEach(n ->{ Hibernate.initialize(n);
		 * });
		 */
		session.saveOrUpdate(site);
	}

	@Override
	public void deleteBoundryDC(BoundryDataCollector siteBD) {
		try{
			Session session = getCurrentSession();
			session.update(siteBD);
			session.delete(siteBD);
			session.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Site loadSiteDataBySiteId(String siteId) {
		Session session = getCurrentSession();
		/*Site site = (Site) session.get(Site.class, Integer.parseInt(siteId));*/
		Site site = (Site) session.load(Site.class, Integer.parseInt(siteId));
		Hibernate.initialize(site.getBoundrydatacollector());
		Hibernate.initialize(site.getDatacollector());
		site.getBoundrydatacollector().forEach(n -> {
			n.setSite(null);
			n.setDatacollector(null);
			session.update(n);
			session.delete(n);
			Hibernate.initialize(site.getDatacollector());
		});
		site.getBoundrydatacollector().clear();
		return site;
	}

	@Override
	public void addNewEditedBDC(BoundryDataCollector bdc) {
		Session session = getCurrentSession();
		session.refresh(bdc.getDatacollector());
		session.refresh(bdc.getSite());
		session.saveOrUpdate(bdc);
		//Hibernate.initialize(bdc.getSite().getDatacollector());
	}

	// **********************************

	@SuppressWarnings("unchecked")
	@Override
	public List<BatteryLife> getBatteryLifeList() {

		Session session = getCurrentSession();
		String hqlQuery = "from BatteryLife";
		Query query = session.createQuery(hqlQuery);

		List<BatteryLife> list = new ArrayList<BatteryLife>();
		list = query.list();

		return list;

	}

	@Override
	public void addNewBatteryLife(BatteryLife batteryLife) {

		try {
			sessionFactory.getCurrentSession().save(batteryLife);

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}

	}

	@Override
	public BatteryLife getBatteryLifeById(String batteryLifeId) {

		Session session = getCurrentSession();
		BatteryLife BatteryLife = (BatteryLife) session.get(BatteryLife.class,
				Integer.parseInt(batteryLifeId));
		return BatteryLife;
	}

	@Override
	public void updateBatteryLife(BatteryLife batteryLife) {

		sessionFactory.getCurrentSession().update(batteryLife);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BatteryLife> searchBatteryByChildNode(String childNodeInput) {

		List<BatteryLife> searchedList = new ArrayList<BatteryLife>();
		searchedList = null;
		try {

			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(BatteryLife.class);

			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			searchCriteria.add(Restrictions.eq("numberOfChildNodes",Integer.parseInt(childNodeInput)));

			searchedList = searchCriteria.list();

			return searchedList;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return searchedList;

	}

	@Override
	public boolean deleteBattery(int batteryLifeId) {

		try {
			Session session = sessionFactory.getCurrentSession();
			BatteryLife batteryLife = (BatteryLife) session.get(
					BatteryLife.class, batteryLifeId);

			if (batteryLife != null) {

				session.delete(batteryLife);

			} else
				return false;
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
			return false;
		}
		return true;
	}

	@Override
	public boolean checkForChildNodes(String numberOfChildNodes) {

		String query = "from BatteryLife where numberOfChildNodes=:numberOfChildNodes";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("numberOfChildNodes", Integer.parseInt(numberOfChildNodes));

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;
	}

	@Override
	public List<String> getCustomerNotifications(int customerId) {
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"SELECT ct.registerId FROM ConsumerMeterTransaction ct,ConsumerMeter cm where ct.registerId=cm.registerId AND cm.customer.customerId=:customerId AND ct.alerts is not null group by ct.registerId");
		query.setParameter("customerId", customerId);
		List<String> ls = null;
		ls = query.list();

		if (ls != null && !ls.isEmpty()) {
			return ls;
		} else {
			return null;
		}

	}

	@Override
	public List<String> getConsumerAlertNotifications(Integer consumerId) {
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"SELECT ct.registerId FROM ConsumerMeterTransaction ct,ConsumerMeter cm where ct.registerId=cm.registerId AND cm.consumer.consumerId=:consumerId AND ct.alerts is not null group by ct.registerId");
		query.setParameter("consumerId", consumerId);
		List<String> ls = null;
		ls = query.list();

		if (ls != null && !ls.isEmpty()) {
			return ls;
		} else {
			return null;
		}

	}

	@Override
	public boolean checkForSerialNumber(String dcSerialNumber) {
		String query = "from DataCollector where dcSerialNumber=:dcSerialNumber";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("dcSerialNumber", dcSerialNumber);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;
	}

	@Override
	public boolean checkForSimcardNumber(String dcSimcardNo) {
		String query = "from DataCollector where dcSimcardNo=:dcSimcardNo";
		Query hqlDuery = sessionFactory.getCurrentSession().createQuery(query);
		hqlDuery.setParameter("dcSimcardNo", dcSimcardNo);

		if (!hqlDuery.list().isEmpty())
			return false;
		else
			return true;

	}

	@Override
	public void saveSiteInsFiles(SiteInstallationFiles sf) {
		try {
			sessionFactory.getCurrentSession().save(sf);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getSiteBySiteName(String siteName) {

		int siteId;

		List<Site> searchedList = new ArrayList<Site>();
		searchedList = null;
		try {
			Criteria searchCriteria = sessionFactory.getCurrentSession()
					.createCriteria(Site.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			searchCriteria.add(Restrictions.like("siteName", siteName,
					MatchMode.ANYWHERE));
			searchedList = searchCriteria.list();
			Site siteObj = searchedList.get(0);
			siteId = siteObj.getSiteId();
			return siteId;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return 0;
	}

	@Override
	public void updateSite(Site site) {
		try {
			Session session = getCurrentSession();
			session.update(site);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.warn(e.getMessage());*/
		}
	}

	@Override
	public void mergeSite(Site site) {
		try {
			getCurrentSession().merge(site);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.warn(e.getMessage());*/
		}
	}

	@Override
	public List<String> getInstallationFileName(int installerId,String flag) {
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(SiteInstallationFiles.class);
			searchCriteria.add(Restrictions.eq("installer.installerId", installerId));
			searchCriteria.add(Restrictions.eq("isFileUploaded", false));
			searchCriteria.add(Restrictions.eq("isFileVerified", false));
			if(flag.equals("DC")){
				searchCriteria.add(Restrictions.and(Restrictions.isNotNull("noOfDatacollectors"),Restrictions.gt("noOfDatacollectors", 0)));
				searchCriteria.add(Restrictions.isNull("noOfEndPoints"));
			}
			if(flag.equals("EP")){
				searchCriteria.add(Restrictions.and(Restrictions.isNotNull("noOfEndPoints"),Restrictions.gt("noOfEndPoints", 0)));
				searchCriteria.add(Restrictions.isNull("noOfDatacollectors"));
			}
			List<SiteInstallationFiles> files = searchCriteria.list();
			List<String> filesList = new ArrayList<String>();
			if(!files.isEmpty()){
				for(SiteInstallationFiles siteFile:files){
					filesList.add(siteFile.getFileName());
				}
				return filesList;
			}else {
				return null;
			}
		}catch(Exception e){
			log.error("Error {}",e);
			/*	e.printStackTrace();*/
		}
		return null;

	}

	@Override
	public boolean updateSiteInstallationFilesStatus(String fileName,String flag,int installerId) {
		try {
			Query query = null;
			if(flag.equals("EP")){
				query = sessionFactory.getCurrentSession().createQuery("update SiteInstallationFiles set isFileVerified=:isFileVerified,isFileUploaded=:isFileUploaded where fileName=:fileName AND isFileUploaded=:isFileUploadedFalseFlag AND isFileVerified=:isFileVerifiedFalseFlag AND installer.installerId=:installerId AND noOfDatacollectors is null AND noOfEndPoints is not null");
				query.setParameter("isFileUploaded", true);
				query.setParameter("isFileVerified", true);
				query.setParameter("isFileUploadedFalseFlag", false);
				query.setParameter("isFileVerifiedFalseFlag", false);
			}else if (flag.equals("DC") ) {
				query = sessionFactory.getCurrentSession().createQuery("update SiteInstallationFiles set isFileVerified=:isFileVerified,isFileUploaded=:isFileUploaded where fileName=:fileName AND isFileUploaded=:isFileUploadedFalseFlag AND isFileVerified=:isFileVerifiedFalseFlag AND installer.installerId=:installerId AND noOfEndPoints is null AND noOfDatacollectors is not null");
				query.setParameter("isFileUploaded", true);
				query.setParameter("isFileVerified", true);
				query.setParameter("isFileUploadedFalseFlag", false);
				query.setParameter("isFileVerifiedFalseFlag", false);
			}else if (flag.equals("EP.false")) {
				query = sessionFactory.getCurrentSession().createQuery("update SiteInstallationFiles set isFileVerified=:isFileVerified,isFileUploaded=:isFileUploaded where fileName=:fileName AND isFileUploaded=:isFileUploadedFalseFlag AND isFileVerified=:isFileVerifiedFalseFlag AND installer.installerId=:installerId AND noOfDatacollectors is null AND noOfEndPoints is not null");
				query.setParameter("isFileUploaded", false);
				query.setParameter("isFileVerified", false);
				query.setParameter("isFileUploadedFalseFlag", true);
				query.setParameter("isFileVerifiedFalseFlag", true);
			}else if(flag.equals("DC.false")){
				query = sessionFactory.getCurrentSession().createQuery("update SiteInstallationFiles set isFileVerified=:isFileVerified,isFileUploaded=:isFileUploaded where fileName=:fileName AND isFileUploaded=:isFileUploadedFalseFlag AND isFileVerified=:isFileVerifiedFalseFlag AND installer.installerId=:installerId AND noOfEndPoints is null AND noOfDatacollectors is not null");
				query.setParameter("isFileUploaded", false);
				query.setParameter("isFileVerified", false);
				query.setParameter("isFileUploadedFalseFlag", true);
				query.setParameter("isFileVerifiedFalseFlag", true);
			}
			int update = 0;
			if(query != null){
				query.setParameter("fileName", fileName);
				query.setParameter("installerId",installerId);
				update = query.executeUpdate();
			}
			if(update > 0){
				return true;
			}else {
				return false;
			}

		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.info(e.getMessage());*/
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerMeter> getAssetInspectionScheduleForEndpoints(
			Integer siteId, String siteName, int customerId) {

		List<ConsumerMeter> consumerMeters = new ArrayList<ConsumerMeter>();
		consumerMeters = null;
		int tempSiteId = 0;
		Criteria searchCriteria = sessionFactory.getCurrentSession()
				.createCriteria(ConsumerMeter.class);
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		searchCriteria.add(Restrictions.eq("customer.customerId",customerId));
		// If sitename exist
		if(siteName!=null && !(siteName.trim().equals(""))){
			tempSiteId = getSiteBySiteName(siteName);
			// sitename and siteid both exist
			if(tempSiteId != 0 && siteId != null && tempSiteId == siteId){
				searchCriteria.add(Restrictions.eq("site.siteId", tempSiteId));
			}else if (tempSiteId !=0 && siteId == null) { // only sitename exist
				searchCriteria.add(Restrictions.eq("site.siteId",tempSiteId));
			}else{
				return null;
			}
		}
		// only siteid exist
		if(tempSiteId == 0){
			if(siteId!=null){
				searchCriteria.add(Restrictions.eq("site.siteId",siteId));
			}
		}
		consumerMeters = searchCriteria.list();
		return consumerMeters;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCollector> getAssetInspectionScheduleForDataCollectors(
			Integer siteId, String siteName, int customerId) {

		int tempSiteId = 0;
		List<DataCollector> dataCollectorList = new ArrayList<DataCollector>();
		dataCollectorList = null;
		Criteria searchCriteria = sessionFactory.getCurrentSession()
				.createCriteria(DataCollector.class);
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		searchCriteria.add(Restrictions.eq("customer.customerId",customerId));
		// If sitename exist
		if(siteName!=null && !(siteName.trim().equals(""))){
			tempSiteId = getSiteBySiteName(siteName);
			// sitename and siteid both exist
			if(tempSiteId != 0 && siteId != null && tempSiteId == siteId){
				searchCriteria.add(Restrictions.eq("site.siteId", tempSiteId));
			}else if (tempSiteId !=0 && siteId == null) { // only sitename exist
				searchCriteria.add(Restrictions.eq("site.siteId",tempSiteId));
			}else{
				return null;
			}
		} 
		// only siteid exist
		if(tempSiteId == 0){
			if(siteId!=null){
				searchCriteria.add(Restrictions.eq("site.siteId",siteId));
			}
		}
		dataCollectorList = searchCriteria.list();
		return dataCollectorList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getConsumerMeterAlerts(String selectedAlertType, String alert,
			Integer siteId, String siteName,String zipCode, int customerId, Instant periodStartDate, Instant periodEndDate) {

		int tempSiteId = 0;
		List<ConsumerMeterTransaction> consumerMeterTransearchedList = new ArrayList<ConsumerMeterTransaction>();
		List<ConsumerMeter> consumerMeterList = new ArrayList<ConsumerMeter>();
		List<Object> searchedList = new ArrayList<Object>();
		Criteria consumerSearchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeter.class);
		consumerSearchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		consumerSearchCriteria.add(Restrictions.eq("customer.customerId", customerId));
		// If sitename exist
		if(siteName!=null && !(siteName.trim().equals(""))){
			tempSiteId = getSiteBySiteName(siteName);
			// sitename and siteid both exist
			if(tempSiteId != 0 && siteId != null && tempSiteId == siteId){
				consumerSearchCriteria.add(Restrictions.eq("site.siteId",tempSiteId));
			}else if (tempSiteId !=0 && siteId == null) { // only sitename exist
				consumerSearchCriteria.add(Restrictions.eq("site.siteId",tempSiteId));
			}else{
				return null;
			}
		}
		// only siteid exist
		if(tempSiteId == 0){
			if(siteId!=null){
				consumerSearchCriteria.add(Restrictions.eq("site.siteId",siteId));
			}
		}
		if(zipCode!=null && !(zipCode.trim().equals(""))){
			consumerSearchCriteria.add(Restrictions.eq("zipcode", zipCode));
		}
		consumerMeterList = consumerSearchCriteria.list();

		List<String> regIdList = new ArrayList<String>();
		List<ConsumerAlertListModel> consumerAlertList = new ArrayList<ConsumerAlertListModel>();
		for(int i=0;i<consumerMeterList.size();i++){
			if(consumerMeterList.get(i).getRegisterId() != null){
				regIdList.add(consumerMeterList.get(i).getRegisterId());
				ConsumerAlertListModel consumerAlert = new ConsumerAlertListModel();
				consumerAlert.setZipcode(consumerMeterList.get(i).getZipcode());
				consumerAlert.setRegisterId(consumerMeterList.get(i).getRegisterId());
				consumerAlertList.add(consumerAlert);
			}
		}
		if(!regIdList.isEmpty()){
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeterTransaction.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			searchCriteria.add(Restrictions.in("registerId", regIdList));

			if(periodStartDate!=null && periodEndDate!=null){
				Date startDate = Date.from(periodStartDate);
				Date endDate = Date.from(periodEndDate);
				searchCriteria.add(Restrictions.between("timeStamp", startDate, endDate));
			}
			if(selectedAlertType != null && !selectedAlertType.equals("---Please Select Alert Type---")){
				searchCriteria.add(Restrictions.like("alerts", selectedAlertType,MatchMode.ANYWHERE));
			}

			if(alert!=null && !alert.equals("---Please Select Alert Type---")){
				if(alert.equals("Acknowledged")){
					searchCriteria.add(Restrictions.eq("alerts_ack",true));
				}
				if(alert.equals("Not Acknowledged")){
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
	public List<DataCollectorAlerts> getNetworkAlerts(String selectedAlertType,
			String alert, Integer siteId, String siteName,
			String dcSerialNo,int customerId,Instant periodStartDate, Instant periodEndDate) {

		List<DataCollectorAlerts> dataCollectorList = new ArrayList<DataCollectorAlerts>();
		dataCollectorList = null;
		int tempSiteId = 0;

		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(DataCollectorAlerts.class,"dca");
		// To get unique records
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		// add criteria for customer
		searchCriteria.createAlias("dca.dataCollector", "dc").createAlias("dc.customer", "ct");
		searchCriteria.add(Restrictions.eq("ct.customerId",customerId));
		if(selectedAlertType != null && !selectedAlertType.equals("---Please Select Alert Type---")){
			searchCriteria.add(Restrictions.like("dca.alert", selectedAlertType,MatchMode.ANYWHERE));
		}
		if(alert!=null && !alert.equals("---Please Select Alert Type---")){
			if(alert.equals("Acknowledged")){
				searchCriteria.add(Restrictions.eq("dca.alertAck",true));
			}
			if(alert.equals("Not Acknowledged")){
				searchCriteria.add(Restrictions.eq("dca.alertAck",false));
			}
		}
		// If sitename exist
		if(siteName!=null && !(siteName.trim().equals(""))){
			tempSiteId = getSiteBySiteName(siteName);
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
		if(dcSerialNo!=null && !(dcSerialNo.trim().equals(""))){
			searchCriteria.add(Restrictions.eq("dc.dcSerialNumber",dcSerialNo));
		}
		if(periodStartDate!=null && periodEndDate!=null){
			Date startDate = Date.from(periodStartDate);
			Date endDate = Date.from(periodEndDate);
			searchCriteria.add(Restrictions.between("alertDate", startDate, endDate));
		}
		dataCollectorList = searchCriteria.list();
		return dataCollectorList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Site> getSiteListByRegion(Region region) {

		List<Site> siteList = new ArrayList<Site>();
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(Site.class);
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		searchCriteria.add(Restrictions.eq("region", region));
		siteList = searchCriteria.list();
		return siteList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateAlertAckForConsumerMeter(String registerId, Date dateFlagged) {

		Session session = getCurrentSession();
		Query hqlQuery = session.createQuery("from ConsumerMeterTransaction where registerId=:registerId and timeStamp=:dateFlagged");
		hqlQuery.setParameter("registerId", registerId);
		hqlQuery.setParameter("dateFlagged", dateFlagged);

		List<ConsumerMeterTransaction> list = hqlQuery.list();

		ConsumerMeterTransaction consumerMeterTransaction = list.get(0);
		if(!consumerMeterTransaction.isAlerts_ack()){
			consumerMeterTransaction.setAlerts_ack(true);
			session.update(consumerMeterTransaction);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateAlertAckForDC(String dcSerialNo) {
		Session session = getCurrentSession();
		Query hqlQuery = session.createQuery("from DataCollector where dcSerialNumber=:dcSerialNo");
		hqlQuery.setParameter("dcSerialNo", dcSerialNo);
		List<DataCollector> list = hqlQuery.list();
		DataCollector dataCollector = list.get(0);

		Query query = session.createQuery("from DataCollectorAlerts where dataCollector=:dataCollector");
		query.setParameter("dataCollector", dataCollector);
		List<DataCollectorAlerts> dataCollectorAlertsList = query.list();
		DataCollectorAlerts dataCollectorAlerts = dataCollectorAlertsList.get(0);
		if(!dataCollectorAlerts.isAlertAck()){
			dataCollectorAlerts.setAlertAck(true);
			session.update(dataCollectorAlerts);
		}
	}

	@Override
	public DistrictMeterTransaction getDistrictMeterTransactionById(int districtMeterTransactionId) {

		DistrictMeterTransaction districtMeterTransaction = null;

		try{
			Session session = getCurrentSession();
			districtMeterTransaction = (DistrictMeterTransaction) session.get(DistrictMeterTransaction.class , districtMeterTransactionId);
		} catch(Exception e){
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}

		return districtMeterTransaction;
	}

	@Override
	public void updateDUMeterTransaction(DistrictMeterTransaction districtMeterTransaction) {
		try{
			sessionFactory.getCurrentSession().update(districtMeterTransaction);
		} catch(Exception e){
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCollector> getDataCollectorBySite(Site site) {

		List<DataCollector> dcList = new ArrayList<DataCollector>();
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(DataCollector.class);
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		searchCriteria.add(Restrictions.eq("site", site));
		dcList = searchCriteria.list();
		return dcList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DatacollectorMessageQueue> getDCMessageQueueByDCId(int datacollectorId) {

		List<DatacollectorMessageQueue> messageQueues = null;

		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from DatacollectorMessageQueue where datacollectorId = :datacollectorId");
		contactQuery.setParameter("datacollectorId",datacollectorId);
		messageQueues = contactQuery.list(); 

		return 	messageQueues;

	}

	@Override
	public void addNewDCMessage(DatacollectorMessageQueue messageQueue) {
		try {
			sessionFactory.getCurrentSession().save(messageQueue);
		} catch (Exception e) {
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
	}

	@Override
	public void updateBatteryEstimatedLife(String operator,	String batteryPercentage) {

		try{
			String query = "update battery_life set estimated_battery_life_in_years = estimated_battery_life_in_years"+operator+"(estimated_battery_life_in_years*("+batteryPercentage+"/100))";
			sessionFactory.getCurrentSession().createSQLQuery(query).executeUpdate();

			String query1 = "update consumer_meter set estimated_remainaing_battery_life_in_year = estimated_remainaing_battery_life_in_year"+operator+"(estimated_remainaing_battery_life_in_year*("+batteryPercentage+"/100))";
			sessionFactory.getCurrentSession().createSQLQuery(query1).executeUpdate();
		}catch(Exception e){
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillingHistory> getBillingHistoryByConsumerMeterId(String consumerMeterId) {
		List<BillingHistory> billingHistoryList = new ArrayList<BillingHistory>();
		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from BillingHistory where consumer_meter_id = :consumerMeterId");
		contactQuery.setParameter("consumerMeterId",consumerMeterId);
		billingHistoryList = contactQuery.list(); 
		return billingHistoryList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getCustomerDashboardData(String customerId,	String startDate, String endDate) {

		List<Object> objectList = new ArrayList<Object>();
		//List objectList = new ArrayList<>();
		objectList = null;

		SQLQuery query = getCurrentSession().createSQLQuery("CALL getCustomerDashboardData(:startDate,:endDate,:customerId)");
		query.setParameter("customerId", customerId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		objectList = query.list();

		return objectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerMeter> getConsumerMeterListByConsumerId(Integer consumerId) {

		List<ConsumerMeter> meterList = new ArrayList<ConsumerMeter>();
		meterList = null;
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ConsumerMeter.class);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			searchCriteria.add(Restrictions.eq("consumer.consumerId", consumerId));
			meterList = searchCriteria.list();
		}catch(Exception e){
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}

		return meterList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getConsumerDashboardData(String startDate,String endDate, int consumerMeterId, String registerId) {

		List<Object> objectList = new ArrayList<Object>();
		//List objectList = new ArrayList<>();
		objectList = null;

		SQLQuery query = getCurrentSession().createSQLQuery("CALL getConsumerDashboardData(:startDate,:endDate,:consumerMeterId,:registerId)");
		query.setParameter("consumerMeterId", consumerMeterId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("registerId", registerId);

		objectList = query.list();

		return objectList;

	}

	@Override
	public List<Object> getTariffTransactionDataByTariffId(int tariffPlanId, Integer readingDiff) {

		List<Object> objectList = new ArrayList<Object>();
		try {
			String sql = "SELECT tariff_transaction_id,rate FROM tariff_transaction where "+ readingDiff +" between start_band and end_band and tariff_plan_id=" + tariffPlanId;
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			Object object =  query.list().get(0);
			Object[] row = (Object[])object;

			objectList.add((Object) row[0]);
			objectList.add((Object) row[1]);

		} catch (Exception e) {
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}

		return objectList;

	}

	@Override
	public Site getSiteByCustomerAndSiteName(Customer customer, String siteName) {

		Session session = getCurrentSession();
		List<Site> siteList = null;
		Criteria criteria = session.createCriteria(Site.class);
		try{
			criteria.createAlias("region", "region");

			criteria.add(Restrictions.eq("region.customer", customer));
			criteria.add(Restrictions.eq("siteName", siteName));
		}catch(Exception e){
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}
		siteList = criteria.list();
		if(siteList != null && !siteList.isEmpty()){
			reportDAO.initializeDatacollectorBySite(siteList.get(0));
			return siteList.get(0);
		}	
		return null;
	}

	@Override
	public TariffPlan getTariffByCustomerAndTariffName(Customer customer,
			String tariffName) {
		try{
			Session session = getCurrentSession();
			Criteria criteria = session.createCriteria(TariffPlan.class);

			if(customer != null && tariffName != null && !tariffName.isEmpty()){
				criteria.add(Restrictions.eq("customer", customer));
				criteria.add(Restrictions.eq("tariffPlanName", tariffName));
			}
			List<TariffPlan> list = criteria.list();

			if(list.size() > 1)
				log.error("Error in getTariffByCustomerAndTariffName() method");

			if(list != null && !list.isEmpty()){
				return list.get(0);
			}
		}catch(Exception e){
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}
		return null;
	}

	@Override
	public List<ConsumerMeter> getConsumerMeterByListOfSiteId(List<Integer> siteId) {
		List<ConsumerMeter> list = null;
		try{
			Session session = getCurrentSession();
			Query q = session.createQuery("FROM ConsumerMeter entity WHERE site.siteId IN (:list)");
			q.setParameterList("list", siteId);
			list = q.list();
		}catch(Exception e){
			log.error("Error {}",e);
			/*log.error(e.getMessage());*/
		}
		return list;
	}

	@Override
	public boolean inactiveDCandMeterBySiteId(Integer siteId, int customerId, Date modifiedDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");

		boolean isSuccess = false;
		try {
			String dcQuery = "update DataCollector set active=false, updated_by=:customerId, updated_ts = :modifiedDate, deleted_by = :customerId, deleted_ts = :modifiedDate where site_id = :siteId";
			Query query2 = sessionFactory.getCurrentSession().createQuery(dcQuery);
			query2.setParameter("siteId", siteId);
			query2.setParameter("customerId", customerId);
			query2.setParameter("modifiedDate", modifiedDate);

			String meterQuery = "update ConsumerMeter set active=false, updated_by=:customerId, updated_ts = :modifiedDate, deleted_by = :customerId, deleted_ts = :modifiedDate where site_id = :siteId";
			Query query1 = sessionFactory.getCurrentSession().createQuery(meterQuery);
			query1.setParameter("siteId", siteId);
			query1.setParameter("customerId", customerId);
			query1.setParameter("modifiedDate", modifiedDate);

			isSuccess = true;

		} catch (Exception e) {
			log.error("Error {}",e);
			/*e.printStackTrace();*/
		}
		return isSuccess;
	}
}
