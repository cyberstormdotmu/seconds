package com.kenure.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.kenure.dao.IReportDAO;
import com.kenure.dao.IUserDAO;
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
import com.kenure.entity.Region;
import com.kenure.entity.Role;
import com.kenure.entity.Site;
import com.kenure.entity.TariffPlan;
import com.kenure.entity.TariffTransaction;
import com.kenure.entity.User;
import com.kenure.model.ConsumerAlertListModel;
import com.kenure.model.UserLoginCredentials;
import com.kenure.utils.MD5Encoder;
import com.kenure.utils.MailSender;

/**
 * 
 * @author TatvaSoft
 *
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService{

	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IReportDAO reportDAO;

	@Autowired
	@Qualifier("mailSender")
	private MailSender emailSender;

	double esitmatedMbPerMonthAcrossAllSitesofAcustomer = 0d;


	@Override @Transactional
	public User authenticateUser(UserLoginCredentials loginCredentials) {
		loginCredentials.setPassword(MD5Encoder.MD5Encryptor(loginCredentials.getPassword())); // Encrypted password set in same instance
		return userDAO.authenticateUser(loginCredentials);
	}

	@Override @Transactional
	public void addUser() {

		//userDAO.add
	}

	@Override @Transactional
	public boolean deleteUser(int dependantId, int roleId, int modifyingId ) {
		return userDAO.deleteUser(dependantId, roleId, modifyingId);
	}

	@Override @Transactional
	public boolean deleteSite(int siteId, int customerId) {
		return userDAO.deleteSite(siteId,customerId);
	}

	@Override @Transactional
	public List<User> getUserList() {

		return userDAO.getUserList();
	}

	@Override @Transactional
	public void insertNewUser(Customer newUser) {
		userDAO.addNewUser(newUser);
	}

	@Override @Transactional
	public User availableUserName(String userName) {
		return userDAO.availableUserName(userName);
	}

	@Override @Transactional
	public ContactDetails getDetailsByUserID(int userID) {
		return userDAO.getDetailsByUserID(userID);
	}

	@Override @Transactional
	public void updatePassword(User user, String password) {
		//for MD5 encryption
		user.setPassword(MD5Encoder.MD5Encryptor(password));
		userDAO.updatePassword(user);
	}

	@Override @Transactional
	public List<Customer> searchCustomer(String customerSearchCriteria, Date portalPlanActiveDate, Date portalPlanExpiryDate, Date dataPlanActiveDate, Date dataPlanExpiryDate) {
		return userDAO.searchUser(customerSearchCriteria, portalPlanActiveDate, portalPlanExpiryDate, dataPlanActiveDate, dataPlanExpiryDate);
	}

	@Override @Transactional
	public List<DataPlan> getDataPlanList() {

		return userDAO.getDataPlan();
	}

	@Override @Transactional
	public List<Customer> getCustomerList() {
		return userDAO.getCustomerList();
	}

	@Override @Transactional
	public List<ConsumerMeter> getConsumerList(Integer id, String role) {
		return userDAO.getConsumerList(id,role);
	}

	@Override @Transactional
	public User getUserDetailsByUserID(int userID) {
		return userDAO.getUserDetailsByUserID(userID);
	}

	@Override @Transactional
	public Customer getCustomerDetailsByUser(int userId) {
		return userDAO.getCustomerDetailsByUser(userId);
	}

	@Override @Transactional
	public boolean updateUser(Customer customer) {
		return userDAO.updateUser(customer);

	}
	/*public void updateCustomerDetails(ContactDetails contactdetails) {
		userDAO.updateCustomerDetails(contactdetails);

	}*/

	@Override @Transactional
	public void addNewDataPlan(DataPlan dataplan) {
		userDAO.addNewDataPlan(dataplan);

	}

	@Override @Transactional
	public DataPlan getDataPlanById(int dataPlanId) {
		return userDAO.getDataPlanById(dataPlanId);
	}

	@Override @Transactional
	public void updateDataPlan(DataPlan dataplan) {
		userDAO.updateDataPlan(dataplan);

	}

	@Override @Transactional
	public void updateCustomerDetails(ContactDetails contactdetails) {
		userDAO.updateCustomerDetails(contactdetails);

	}

	@Override @Transactional
	public List<DataCollector> getSpareDataCollectorList() {
		return userDAO.getSpareDataCollectorList();
	}

	@Override @Transactional
	public void refreshSession(Region customerRegion) {
		userDAO.refreshSession(customerRegion);
	}

	@Override @Transactional
	public DataCollector getSpareDataCollectorById(int datacollectorId) {
		return userDAO.getSpareDataCollectorById(datacollectorId);
	}

	@Override @Transactional
	public void updateDataCollector(DataCollector datacollector) {
		userDAO.updateDataCollector(datacollector);

	}

	@Override @Transactional
	public void addNewDataCollector(DataCollector datacollector) {
		userDAO.addNewDataCollector(datacollector);

	}

	@Override @Transactional
	public List<DataCollector> searchDataCollector(String customer, String dcSerialNumber) {
		return userDAO.searchDataCollector(customer, dcSerialNumber);
	}



	@Override @Transactional
	public void addNewSite(Site site) {
		userDAO.addNewSite(site);
	}

	@Override @Transactional
	public List<DataCollector> getDCBySerialNumber(String[] dcList) {
		return userDAO.getDCBySerialNumber(dcList);
	}

	@Override @Transactional
	public void addNewCollector(DataCollector thisCollector) {
		userDAO.addNewCollector(thisCollector);
	}

	@Override @Transactional
	public Region getSelectedRegionByName(String selectedRegion,Customer customer) {
		return userDAO.getSelectedRegionByNameAndCustomer(selectedRegion,customer);
	}

	@Override @Transactional
	public void addNewBDC(BoundryDataCollector bdc) {
		userDAO.addNewBDC(bdc);
	}

	@Override @Transactional
	public Installer getInstallerByNameAndCustomer(String installerName,
			Customer customer) {
		return userDAO.getInstallerByNameAndCustomer(installerName,
				customer);
	}

	@Override @Transactional
	public Customer getOnlyCustomerByUserId(int userID) {
		return userDAO.getOnlyCustomerByUserId(userID);
	}

	@Override @Transactional
	public Customer getCustomerDetailsByCustomerName(String customerName) {
		return userDAO.getCustomerDetailsByCustomerName(customerName);
	}

	@Override @Transactional
	public List<DataCollector> searchDataCollectorByCustomerAndIp(String dcIp, String customerId) {
		return userDAO.searchDataCollectorByCustomerAndIp(dcIp, customerId);
	}

	@Override @Transactional
	public List<DataCollector> searchDataCollectorByCustomer(String dcIp, String dcSerialNumber,int customerId) {
		return userDAO.searchDataCollectorByCustomer(dcIp, dcSerialNumber,customerId);
	}

	@Override @Transactional
	public Site getSiteDataBySiteId(String siteId) {
		return userDAO.getSiteDataBySiteId(siteId);
	}

	@Override @Transactional
	public List<BoundryDataCollector> getAllBDCFromSiteId(Customer currentCustomer,String regionName,String[] bdc,Site site,String[] assignedDc,String installer) {
		return userDAO.getAllBDCFromSiteId(currentCustomer,regionName,bdc,site,assignedDc,installer);
	}

	@Override @Transactional
	public List<Site> searchSiteByNameOrRegion(String siteSearchCriteria,String siteSearchRegion) {
		return userDAO.searchSiteByNameOrRegion(siteSearchCriteria,siteSearchRegion);
	}

	@Override @Transactional
	public ContactDetails getContactDetailsByCustomerID(int customerId) {
		return userDAO.getContactDetailsByCustomerID(customerId);
	}

	@Override @Transactional
	public Customer getCustomerDetailsByCustomerId(int customerId) {
		return userDAO.getCustomerDetailsByCustomerId(customerId);
	}

	@Override @Transactional
	public List<Customer> getActiveCustomerList() {
		return userDAO.getActiveCustomerList();
	}

	@Override @Transactional
	public List<DataPlan> searchDataplan(int mbPerMonth) {
		return userDAO.searchDataplan(mbPerMonth);
	}

	@Override @Transactional
	public boolean checkForCustomerCode(String customerCode) {
		return userDAO.checkForCustomerCode(customerCode);
	}

	@Override @Transactional
	public void addNewRegion(Region region) {
		userDAO.addNewRegion(region);
	}

	@Override @Transactional
	public List<Region> getRegionList() {
		return userDAO.getRegionList();
	}

	@Override @Transactional
	public Region getRegionById(int regionId) {
		return userDAO.getRegionById(regionId);
	}

	@Override @Transactional
	public void updateRegion(Region regionObj) {
		userDAO.updateRegion(regionObj);

	}

	@Override @Transactional
	public List<Region> searchRegion(String regionName, int customerId) {
		return userDAO.searchRegion(regionName,customerId);
	}

	@Override @Transactional
	public List<Region> getRegionListByCustomerId(int customerId) {
		return userDAO.getRegionListByCustomerId(customerId);
	}

	@Override @Transactional
	public boolean checkForRegion(String regionName) {
		return userDAO.checkForRegion(regionName);
	}

	@Override @Transactional
	public List<DistrictMeterTransaction> getDUMeterListByCustomerId(int customerId) {
		return userDAO.getDUMeterListByCustomerId(customerId);
	}

	@Override @Transactional
	public void addDUMeter(DistrictUtilityMeter districtUtilityMeter) {
		userDAO.addDUMeter(districtUtilityMeter);

	}

	@Override @Transactional
	public DistrictUtilityMeter getDUMeterListById(int districtUtilityMeterId) {
		return userDAO.getDUMeterListById(districtUtilityMeterId);
	}

	@Override @Transactional
	public void updateDUMeter(DistrictUtilityMeter districtUtilityMeter) {
		userDAO.updateDUMeter(districtUtilityMeter);

	}

	@Override @Transactional
	public boolean checkForDUMeter(String dUMeterSerialNumber) {
		return userDAO.checkForDUMeter(dUMeterSerialNumber);
	}

	@Override @Transactional
	public DistrictUtilityMeter getDUMeterBySerialNumber(String districtUtilityMeterSerialNumber){
		return userDAO.getDUMeterBySerialNumber(districtUtilityMeterSerialNumber);
	}
	
	@Override @Transactional
	public List<DistrictMeterTransaction> searchDUMeter(String districtUtilityMeterSerialNumber,int customerId) {
		return userDAO.searchDUMeter(districtUtilityMeterSerialNumber,customerId);
	}



	@Override @Transactional
	public List<ConsumerMeter> getConsumerListByCustomerId(int customerId) {

		return userDAO.getConsumerListByCustomerId(customerId);
	}

	@Override @Transactional
	public List<Installer> getInstallerByCustomerId(int customerId) {

		return userDAO.getInstallerByCustomerId(customerId);
	}

	@Override @Transactional
	public boolean assignInstallerToConumers(List<ConsumerMeter> consumerList,
			int installerId) {

		return userDAO.assignInstallerToConumers(consumerList, installerId);
	}

	@Override @Transactional
	public List<ConsumerMeter> searchConsumerByStreetName(int customerId,
			String streetName) {

		return userDAO.searchConsumerByStreetName(customerId, streetName);
	}

	@Override @Transactional
	public 	List<ConsumerMeter> getConsumersByInstaller(int installerId) {

		return userDAO.getConsumersByInstaller(installerId);
	}

	@Override @Transactional
	public List<DataCollector> getDCByInstallerId(int installerId) {

		return userDAO.getDCByInstallerId(installerId);
	}

	@Override @Transactional
	public boolean assignSelectedDCToInstaller(int dcId, int installerId) {

		return userDAO.assignSelectedDCToInstaller(dcId, installerId);
	}

	@Override @Transactional
	public boolean deleteDUMeter(int DUMeterId) {
		return userDAO.deleteDUMeter(DUMeterId);
	}

	@Override @Transactional
	public boolean saveTariffTransaction(TariffTransaction tariffTrans) {
		return userDAO.saveTariffTransaction(tariffTrans);
	}

	@Override @Transactional
	public List<TariffPlan> getTariffDataByCustomer(Customer currentCustomer) {
		return userDAO.getTariffDataByCustomer(currentCustomer);
	}

	@Override @Transactional
	public List<TariffPlan> searchTariffByNameAndCustomer(
			Customer currentCustomer, String tariffName) {
		return userDAO.searchTariffByNameAndCustomer(currentCustomer,tariffName);
	}

	@Override @Transactional
	public boolean deleteTariffwithId(String id) {
		return userDAO.deleteTariffwithId(id);
	}

	@Override @Transactional
	public boolean updateDCsBySiteId(String siteId,Integer mri,String strDate,Customer customer, List<String> siteIdList,DataPlan newDataPlan) {
		int nri=0;
		double frequencyPerDayAcrossASite;
		frequencyPerDayAcrossASite = 0.00d;
		String dummyMriToDisplay = null;
		if(mri!=null){
			if(mri>=1440){
				dummyMriToDisplay = String.valueOf(mri/1440)+" "+"Days";
				mri = 1440;
			}
			else if(mri>60 && mri<1440){
				dummyMriToDisplay = String.valueOf(mri/60)+" "+"Hours";
				nri = 60;
			}else{
				dummyMriToDisplay = String.valueOf(mri)+" "+"Mins";
				nri = mri;
			}
			frequencyPerDayAcrossASite = (double)1440/mri;
		}
		if(strDate!=null){
			dummyMriToDisplay = strDate;
			mri=1440;
			nri = 60;
			frequencyPerDayAcrossASite = (double)1440/mri;
		}
		
		if(siteId!=null){
			Site site = userDAO.getSiteDataBySiteId(siteId);
			site.setDummyMriToDisplay(dummyMriToDisplay);
			List<DataCollector> dcList = new ArrayList<DataCollector>(site.getDatacollector());
			int totalEPAcrossASite = 0;
			Map<Integer,Double> mbPerMonthAcrossDcsOfASite = new HashMap<Integer, Double>();
			for(DataCollector dc : dcList){
				if(dc.getTotalEndpoints()!=null && dc.getInstaller()!=null && dc.getDcIp()!=null && dc.getDcSerialNumber()!=null && dc.getDcSimcardNo()!=null){
					totalEPAcrossASite+=dc.getTotalEndpoints();
					double tempBytesPermonthAcrossADC = 0d;
					double mbPermonthAcrossADC = 0d;
					tempBytesPermonthAcrossADC = dc.getTotalEndpoints() * 23 * frequencyPerDayAcrossASite * 365;
					mbPermonthAcrossADC = (tempBytesPermonthAcrossADC + (tempBytesPermonthAcrossADC * 5/100))/1024/1024;
					mbPerMonthAcrossDcsOfASite.put(dc.getDatacollectorId(), mbPermonthAcrossADC);
				}
			}
			double tempBytesPermonthAcrossASite = 0d;
			double mbPermonthAcrossASite = 0d;
			tempBytesPermonthAcrossASite = totalEPAcrossASite * 23 * frequencyPerDayAcrossASite * 365;
			mbPermonthAcrossASite = (tempBytesPermonthAcrossASite + (tempBytesPermonthAcrossASite * 5/100))/1024/1024;
			esitmatedMbPerMonthAcrossAllSitesofAcustomer = mbPermonthAcrossASite;

			customer.getRegion().stream().filter(filterSite -> filterSite.getSite()!=null).forEach(x -> {
				x.getSite().stream().filter(filterDc -> filterDc.getDatacollector()!=null).forEach(y -> {
					if(y.getSiteId() != Integer.parseInt(siteId)){
						y.getDatacollector().stream().forEach(z-> {
							if(z.getTotalEndpoints()!=null && z.getInstaller()!=null && z.getDcIp()!=null && z.getDcSerialNumber()!=null && z.getDcSimcardNo()!=null && z.getMbPerMonth() != null)
								esitmatedMbPerMonthAcrossAllSitesofAcustomer += z.getMbPerMonth();
						});
					}
				});
			});

			if(esitmatedMbPerMonthAcrossAllSitesofAcustomer<customer.getDataPlan().getMbPerMonth()){
				userDAO.updateDCsBySiteId(siteId,mri,nri,mbPerMonthAcrossDcsOfASite);
				userDAO.updateSite(site);
				esitmatedMbPerMonthAcrossAllSitesofAcustomer = 0.0d;
				return true;
			}else{
				esitmatedMbPerMonthAcrossAllSitesofAcustomer = 0.0d;
				return false;
			}
		}else if(siteIdList!=null && siteIdList.size()>0){
			Map<String, Map<Integer,Double>> mbPerMonthAcrossAllSelectedSites = new HashMap<String, Map<Integer,Double>>();
			for(String siteIdFromList : siteIdList){
				Site site = userDAO.getSiteDataBySiteId(siteIdFromList);
				List<DataCollector> dcList = new ArrayList<DataCollector>(site.getDatacollector());
				int totalEPAcrossASite = 0;
				Map<Integer,Double> mbPerMonthAcrossDcsOfASite = new HashMap<Integer, Double>();
				for(DataCollector dc : dcList){
					if(dc.getTotalEndpoints()!=null && dc.getInstaller()!=null && dc.getDcIp()!=null && dc.getDcSerialNumber()!=null && dc.getDcSimcardNo()!=null){
						totalEPAcrossASite+=dc.getTotalEndpoints();
						double tempBytesPermonthAcrossADC = 0d;
						double mbPermonthAcrossADC = 0d;
						tempBytesPermonthAcrossADC = dc.getTotalEndpoints() * 23 * frequencyPerDayAcrossASite * 365;
						mbPermonthAcrossADC = (tempBytesPermonthAcrossADC + (tempBytesPermonthAcrossADC * 5/100))/1024/1024;
						mbPerMonthAcrossDcsOfASite.put(dc.getDatacollectorId(), mbPermonthAcrossADC);
					}
				}
				mbPerMonthAcrossAllSelectedSites.put(siteIdFromList,mbPerMonthAcrossDcsOfASite);
				double tempBytesPermonthAcrossASite = 0d;
				double mbPermonthAcrossASite = 0d;
				tempBytesPermonthAcrossASite = totalEPAcrossASite * 23 * frequencyPerDayAcrossASite * 365;
				mbPermonthAcrossASite = (tempBytesPermonthAcrossASite + (tempBytesPermonthAcrossASite * 5/100))/1024/1024;
				esitmatedMbPerMonthAcrossAllSitesofAcustomer += mbPermonthAcrossASite;
				//mbPerMonthAcrossAllSelectedSites.put(siteIdFromList, mbPermonthAcrossASite);
			}
			customer.getRegion().stream().filter(filterSite -> filterSite.getSite()!=null).forEach(x -> {
				x.getSite().stream().filter(filterDc -> filterDc.getDatacollector()!=null).forEach(y -> {
					if(!siteIdList.contains(Integer.toString(y.getSiteId()))){
						y.getDatacollector().stream().forEach(dc-> {
							if(dc.getTotalEndpoints()!=null && dc.getInstaller()!=null && dc.getDcIp()!=null && dc.getDcSerialNumber()!=null && dc.getDcSimcardNo()!=null && dc.getMbPerMonth() != null)
								esitmatedMbPerMonthAcrossAllSitesofAcustomer += dc.getMbPerMonth();
						});
					}
				});
			});
			
			if(esitmatedMbPerMonthAcrossAllSitesofAcustomer<customer.getDataPlan().getMbPerMonth()){
				for(String siteIdToUpdateFromList : siteIdList){
					Map<Integer,Double> mbPermonthAcrossASite = mbPerMonthAcrossAllSelectedSites.get(siteIdToUpdateFromList);
					Site site  = userDAO.getSiteDataBySiteId(siteIdToUpdateFromList);
					site.setDummyMriToDisplay(dummyMriToDisplay);
					userDAO.updateSite(site);
					userDAO.updateDCsBySiteId(siteIdToUpdateFromList,mri,nri,mbPermonthAcrossASite);
				}
				esitmatedMbPerMonthAcrossAllSitesofAcustomer = 0.0d;
				return true;
			}else{
				esitmatedMbPerMonthAcrossAllSitesofAcustomer = 0.0d;
				return false;
			}
		}else if(newDataPlan!=null){
			customer.getRegion().stream().filter(filterSite -> filterSite.getSite()!=null).forEach(x -> {
				x.getSite().stream().filter(filterDc -> filterDc.getDatacollector()!=null).forEach(y -> {
						y.getDatacollector().stream().forEach(dc-> {
							if(dc.getTotalEndpoints()!=null && dc.getInstaller()!=null && dc.getDcIp()!=null && dc.getDcSerialNumber()!=null && dc.getDcSimcardNo()!=null && dc.getMbPerMonth() != null)
								esitmatedMbPerMonthAcrossAllSitesofAcustomer += dc.getMbPerMonth();
						});
				});
			});
			if(esitmatedMbPerMonthAcrossAllSitesofAcustomer<=newDataPlan.getMbPerMonth()){
				esitmatedMbPerMonthAcrossAllSitesofAcustomer = 0.0d;
				return true;
			}
			esitmatedMbPerMonthAcrossAllSitesofAcustomer = 0.0d;
			return false;
		}else{
			return false;
		}
	}

	@Override @Transactional
	public List<Site> searchBysiteIdOrRegionName(int sitId, String regionName) {
		return userDAO.searchBysiteIdOrRegionName(sitId, regionName);
	}

	@Override @Transactional
	public List<Consumer> getConsumerUserListByCustomerId(int customerId) {
		return userDAO.getConsumerUserListByCustomerId(customerId);
	}

	@Override @Transactional
	public List<TariffPlan> getTariffPlanList(int customerId) {
		return userDAO.getTariffPlanList(customerId);
	}

	@Override @Transactional
	public boolean checkForConsumerAccNumber(String consumerAccountNumber) {
		return userDAO.checkForConsumerAccNumber(consumerAccountNumber);
	}

	@Override @Transactional
	public void insertNewConsumer(Consumer consumer) {
		userDAO.insertNewConsumer(consumer);
	}

	@Override @Transactional
	public List<Consumer> searchConsumerByAccNum(String consumerAccNumInput,int customerId) {
		return userDAO.searchConsumerByAccNum(consumerAccNumInput,customerId);
	}

	@Override @Transactional
	public TariffPlan getTariffPlanById(int tariffPlanId) {
		return userDAO.getTariffPlanById(tariffPlanId);
	}

	@Override @Transactional
	public Role getRoleById(int roleId) {
		return userDAO.getRoleById(roleId);
	}

	@Override @Transactional
	public Consumer getConsumerByConsumerID(int consumerId) {
		return userDAO.getConsumerByConsumerID(consumerId);
	}

	@Override @Transactional
	public boolean updateConsumerUser(Consumer consumer) {
		return userDAO.updateConsumerUser(consumer);
	}

	@Override @Transactional
	public void updateConsumerMeterUsingConsumerIdAndTariffId(
			Consumer consumer, TariffPlan oldPlan , TariffPlan tp) {
		userDAO.updateConsumerMeterUsingConsumerIdAndTariffId(consumer,oldPlan,tp); 
	}

	@Override @Transactional
	public Consumer getConsumerDetailsByUserId(int userId) {
		return userDAO.getConsumerDetailsByUserId(userId);
	}

	@Override @Transactional
	public List<Installer> getInstallersByCustomerId(int customerId) {
		return userDAO.getInstallerByCustomerId(customerId);
	}

	@Override @Transactional
	public boolean insertInstaller(Installer installer) {

		return userDAO.insertInstaller(installer);
	}

	@Override @Transactional
	public Installer getInstallerById(int installerId) {

		return userDAO.getInstallerById(installerId);
	}

	@Override @Transactional
	public Boolean deleteInstaller(int installerId) {

		return userDAO.deleteInstaller(installerId);
	}

	@Override @Transactional
	public List<Installer> searchInstallerByCriteria(String criteria,int customerId) {
		return userDAO.searchInstallerByCriteria(criteria,customerId);
	}

	@Override @Transactional
	public void insertDataCollector(DataCollector datacollector) {
		userDAO.insertDataCollector(datacollector);
	}
	@Override @Transactional
	public boolean insertTechnician(MaintenanceTechnician technician) {
		return userDAO.insertTechnician(technician);
	}

	@Override @Transactional
	public List<MaintenanceTechnician> getTechnicianListByCustomerId(
			int customerId) {
		return userDAO.getTechnicianListByCustomerId(customerId);
	}

	@Override @Transactional
	public MaintenanceTechnician getTechnicianById(int technicianId) {
		return userDAO.getTechnicianById(technicianId);
	}

	@Override @Transactional
	public ContactDetails getContactDetailsByID(int contactDetailsId) {
		return userDAO.getContactDetailsByID(contactDetailsId);
	}

	@Override @Transactional
	public List<MaintenanceTechnician> searchTechnicianByCriteria(
			String criteria, int customerId) {
		return userDAO.searchTechnicianByCriteria(criteria, customerId);
	}

	@Override @Transactional
	public boolean checkForDataPlan(int mbPerMonth) {
		return userDAO.checkForDataPlan(mbPerMonth);
	}

	@Override @Transactional
	public List<Currency> getCurrencyList() {
		return userDAO.getCurrencyList();
	}
	@Override @Transactional
	public void updateAdminDetails(ContactDetails details){
		userDAO.updateAdminDetails(details);
	}
	@Override @Transactional
	public List<ConsumerMeter> searchConsumerMeterByRegisterId(int customerId,
			int registerId) {
		return userDAO.searchConsumerMeterByRegisterId(customerId, registerId);
	}

	@Override @Transactional
	public List<Country> getCountryList() {
		return userDAO.getCountryList();
	}

	@Override @Transactional
	public void saveSite(Site site) {
		userDAO.saveSite(site);
	}

	@Override @Transactional
	public com.kenure.entity.Currency getCurrencyById(String currencyId) {
		return userDAO.getCurrencyById(currencyId);
	}

	@Override @Transactional
	public Country getCountryById(String countryId) {
		return userDAO.getCountryById(countryId);
	}

	@Override @Transactional
	public List<Country> searchCountryByName(String searchCountryName) {
		return userDAO.searchCountryByName(searchCountryName);
	}

	@Override @Transactional
	public List<Currency> searchCurrencyByName(String searchCurrencyName) {
		return userDAO.searchCurrencyByName(searchCurrencyName);
	}

	@Override @Transactional
	public boolean checkForCountryName(String countryName) {
		return userDAO.checkForCountryName(countryName);
	}

	@Override @Transactional
	public boolean checkForCountryCode(String countryCode) {
		return userDAO.checkForCountryCode(countryCode);
	}

	@Override @Transactional
	public void addNewCountry(Country country) {
		userDAO.addNewCountry(country);

	}

	@Override @Transactional
	public void updateCountry(Country countryobj) {
		userDAO.updateCountry(countryobj);

	}

	@Override @Transactional
	public Country getCountryByName(String countryName) {
		return userDAO.getCountryByName(countryName);

	}

	@Override @Transactional
	public boolean checkForCurrencyName(String currencyName) {
		return userDAO.checkForCurrencyName(currencyName);
	}

	@Override @Transactional
	public void addNewCurrency(Currency currency) {
		userDAO.addNewCurrency(currency);
	}

	@Override @Transactional
	public void updateCurrency(Currency currencyobj) {
		userDAO.updateCurrency(currencyobj);
	}

	@Override @Transactional
	public Installer getInstallerByUserId(int userId) {
		return userDAO.getInstallerByUserId(userId);
	}

	@Override @Transactional
	public boolean updateInstallerDetails(ContactDetails contactDetails){

		return userDAO.updateInstallerDetails(contactDetails);
	}

	@Override @Transactional
	public Role getRoleByName(String name) {
		return userDAO.getRoleByname(name);
	}

	@Override @Transactional
	public Consumer getConsumerByConsumerAccNo(String consumerAccNo) {
		return userDAO.getConsumerByConsumerAccNo(consumerAccNo);
	}

	@Override @Transactional
	public ConsumerMeter getConsumerMetetByRegisterId(String registerId,int installerId) {
		return userDAO.getConsumerMetetByRegisterId(registerId,installerId);
	}

	@Override @Transactional
	public boolean updateConsumerMeter(ConsumerMeter consumerMeter) {
		return userDAO.updateConsumerMeter(consumerMeter);
	}


	@Override @Transactional
	public Consumer getValidConsumer(String customerCode,
			String consumerAccountNumber, String zipcode) {
		return userDAO.getValidConsumer(customerCode,consumerAccountNumber,zipcode);
	}

	@Override @Transactional
	public Customer getCustomerByCustomerCode(String customerCode) {
		return userDAO.getCustomerByCustomerCode(customerCode);
	}

	@Override @Transactional
	public boolean updateUserEntity(User editNormalUser) {
		return userDAO.updateUserEntity(editNormalUser);
	}

	@Override @Transactional
	public List<User> searchUserListByUserId(String userId,Customer superCustomer) {
		return userDAO.searchUserListByUserId(userId,superCustomer);
	}

	@Override @Transactional
	public boolean checkForZipcode(String zipcode, String consumerAccountNumber) {
		return userDAO.checkForZipcode(zipcode , consumerAccountNumber);
	}

	@Override @Transactional
	public Region getRegionByName(Customer customer,String regionName){
		return userDAO.getRegionByName(customer,regionName);
	}

	@Override @Transactional
	public void updateSiteForSiteManagement(Site site) {
		userDAO.updateSiteForSiteManagement(site);
	}

	@Override @Transactional
	public void deleteBoundryDC(BoundryDataCollector siteBD) {
		userDAO.deleteBoundryDC(siteBD);
	}

	@Override @Transactional
	public Site loadSiteDataBySiteIdAndDeleteBDC(String siteId) {
		return userDAO.loadSiteDataBySiteId(siteId);
	}

	@Override @Transactional
	public void addNewEditedBDC(BoundryDataCollector bdc) {
		userDAO.addNewEditedBDC(bdc);
	}

	@Override @Transactional
	public List<BatteryLife> getBatteryLifeList() {
		return userDAO.getBatteryLifeList();
	}

	@Override @Transactional
	public void addNewBatteryLife(BatteryLife batteryLife) {
		userDAO.addNewBatteryLife(batteryLife);
	}

	@Override @Transactional
	public BatteryLife getBatteryLifeById(String batteryLifeId) {
		return userDAO.getBatteryLifeById(batteryLifeId);
	}

	@Override @Transactional
	public void updateBatteryLife(BatteryLife batteryLife) {
		userDAO.updateBatteryLife(batteryLife);
		
	}

	@Override @Transactional
	public List<BatteryLife> searchBatteryByChildNode(String childNodeInput) {
		return userDAO.searchBatteryByChildNode(childNodeInput);
	}

	@Override @Transactional
	public boolean deleteBattery(int batteryLifeId) {
		return userDAO.deleteBattery(batteryLifeId);
	}

	@Override @Transactional
	public boolean checkForChildNodes(String numberOfChildNodes) {
		return userDAO.checkForChildNodes(numberOfChildNodes);
	}

	@Override @Transactional
	public List<String> getCustomerNotifications(int customerId) {
		return userDAO.getCustomerNotifications(customerId);
	}

	@Override @Transactional
	public List<String> getConsumerAlertNotifications(Integer consumerId) {
		return userDAO.getConsumerAlertNotifications(consumerId);
	}
	
	@Override
	public boolean checkForSerialNumber(String dcSerialNumber) {
		return userDAO.checkForSerialNumber(dcSerialNumber);
	}

	@Override
	public boolean checkForSimcardNumber(String dcSimcardNo) {
		return userDAO.checkForSimcardNumber(dcSimcardNo);
	}

	@Override
	public int getSiteBySiteName(String siteName) {
		return userDAO.getSiteBySiteName(siteName);
	}

	@Override
	public void updateSite(Site site) {
		userDAO.updateSite(site);
	}

	@Override
	public void mergeSite(Site site) {
		userDAO.mergeSite(site);
	}

	@Override
	public List<String> getInstallationFileName(int installerId,String flag) {
		return userDAO.getInstallationFileName(installerId,flag);
	}
	@Override
	public List<ConsumerMeter> getAssetInspectionScheduleForEndpoints(Integer siteId, String siteName,
			String inspectionDueWithin, String inspectionInterval, String reportType, int customerId) {
		List<ConsumerMeter> consumerMeters = userDAO.getAssetInspectionScheduleForEndpoints(siteId,siteName,customerId);
		Instant calculatedDueDate = null;
		//Add number of weeks to the current date and calculate expected end date according to the value entered
		if(reportType.equalsIgnoreCase("Week")){
			calculatedDueDate = ZonedDateTime.now().plus(Integer.parseInt(inspectionDueWithin),ChronoUnit.WEEKS).toInstant();
		}
		//Add number of months to the current date and calculate expected end date according to the value entered
		if(reportType.equalsIgnoreCase("Month")){
			calculatedDueDate = ZonedDateTime.now().plus(Integer.parseInt(inspectionDueWithin),ChronoUnit.MONTHS).toInstant();
		}
	  Instant oldInspectionDate;
	  Instant nextInspectionDate;
	  ZonedDateTime inspectionDate;
	  Iterator<ConsumerMeter> consumerMeterIterator = consumerMeters.iterator();
	  List<ConsumerMeter> consumerMeterList = new ArrayList<ConsumerMeter>();
	  while(consumerMeterIterator.hasNext()){
		  
		  ConsumerMeter consumerMeter = new ConsumerMeter();
		  try {
			consumerMeter = (ConsumerMeter) consumerMeterIterator.next().clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		  if(consumerMeter.getAssetInspectionDate() != null){
			  oldInspectionDate = consumerMeter.getAssetInspectionDate().toInstant(); //get old inspection date stored in the database
			  inspectionDate = ZonedDateTime.ofInstant(oldInspectionDate, ZoneId.systemDefault());
			  //Add number of months to the old inspection date according to the interval selected in the drop down menu
			  nextInspectionDate = inspectionDate.plus(Integer.parseInt(inspectionInterval.substring(0, 2)), ChronoUnit.MONTHS).toInstant();
			  //Check whether next estimated inspection date is before the calculated due date
			  if(nextInspectionDate.isBefore(calculatedDueDate)){
				  consumerMeter.setAssetInspectionDate(Date.from(nextInspectionDate));
				  consumerMeterList.add(consumerMeter);
			  }
		  }
	  }
		return consumerMeterList;
	}

	@Override
	public List<DataCollector> getAssetInspectionScheduleForDataCollectors(Integer siteId, String siteName,
			String inspectionDueWithin, String inspectionInterval, String reportType, int customerId) {
		
		List<DataCollector> dataCollectorList = userDAO.getAssetInspectionScheduleForDataCollectors(siteId,siteName,customerId);
		Instant calculatedDueDate = null;
		//Add number of weeks to the current date and calculate expected end date according to the value entered
		if(reportType.equalsIgnoreCase("Week")){
			calculatedDueDate = ZonedDateTime.now().plus(Integer.parseInt(inspectionDueWithin),ChronoUnit.WEEKS).toInstant();
		}
		//Add number of months to the current date and calculate expected end date according to the value entered
		if(reportType.equalsIgnoreCase("Month")){
			calculatedDueDate = ZonedDateTime.now().plus(Integer.parseInt(inspectionDueWithin),ChronoUnit.MONTHS).toInstant();
		}
	  Instant oldInspectionDate;
	  Instant nextInspectionDate;
	  ZonedDateTime inspectionDate;
	  
	  Iterator<DataCollector> dcIterator = dataCollectorList.iterator();
	  List<DataCollector> dcList = new ArrayList<DataCollector>();
	  while(dcIterator.hasNext()){
		  
		  DataCollector dataCollector = new DataCollector();
		  try {
			  dataCollector = (DataCollector) dcIterator.next().clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		  if(dataCollector.getAssetInspectionDate() != null){
			  oldInspectionDate = dataCollector.getAssetInspectionDate().toInstant(); //get old inspection date stored in the database
			  inspectionDate = ZonedDateTime.ofInstant(oldInspectionDate, ZoneId.systemDefault());
			  //Add number of months to the old inspection date according to the interval selected in the drop down menu
			  nextInspectionDate = inspectionDate.plus(Integer.parseInt(inspectionInterval.substring(0, 2)), ChronoUnit.MONTHS).toInstant();
			  //Check whether next estimated inspection date is before the calculated due date
			  if(nextInspectionDate.isBefore(calculatedDueDate)){
				  dataCollector.setAssetInspectionDate(Date.from(nextInspectionDate));
				  dcList.add(dataCollector);
			  }
		  }
	  }
	  return dcList;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getConsumerMeterAlerts(
			String reportType, Integer reportPeriod, Date startDate,
			Date endDate, String selectedAlertType, String alert,
			Integer siteId, String siteName,
			String zipCode, int customerId) {
		
		
		Instant periodStartDate = null;
		Instant periodEndDate = null;
		
		//Minus number of days to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Day") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.DAYS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Weeks to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Week") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.WEEKS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Month") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.MONTHS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Select Dates") && startDate !=null && endDate !=null){
			periodStartDate = startDate.toInstant();
			periodEndDate = endDate.toInstant();
		}
		
				
		List<Object> searchedList = userDAO.getConsumerMeterAlerts(selectedAlertType,alert,siteId,siteName,zipCode,customerId,periodStartDate,periodEndDate);
		if(searchedList!=null){
			List<ConsumerMeterTransaction> consumerAlertList = (List<ConsumerMeterTransaction>) searchedList.get(0);
			List<ConsumerAlertListModel> consumerAlertModelList = (List<ConsumerAlertListModel>) searchedList.get(1);
			Iterator<ConsumerMeterTransaction> conMeterTranIterator = consumerAlertList.iterator();
			List<ConsumerMeterTransaction> consumerMeterTransactionList = new ArrayList<ConsumerMeterTransaction>();
			List<Object> objectList = new ArrayList<Object>();
		
			while(conMeterTranIterator.hasNext()){
				ConsumerMeterTransaction consumerMeterTransaction = conMeterTranIterator.next();
				consumerMeterTransactionList.add(consumerMeterTransaction);
			}
			objectList.add(consumerMeterTransactionList);
			objectList.add(consumerAlertModelList);
			return objectList;
		}
		
		return null;
	}

	@Override
	public List<DataCollectorAlerts> getNetworkAlerts(String reportType, Integer reportPeriod, Date startDate,
			Date endDate,String selectedAlertType,
			String alert, Integer siteId, String siteName,String dcSerialNo, int customerId) {
		
		Instant periodStartDate = null;
		Instant periodEndDate = null;
		
		//Minus number of days to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Day") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.DAYS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Weeks to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Week") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.WEEKS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Month") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.MONTHS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Select Dates") && startDate !=null && endDate !=null){
			periodStartDate = startDate.toInstant();
			periodEndDate = endDate.toInstant();
		}
		List<DataCollectorAlerts> searchedList = userDAO.getNetworkAlerts(selectedAlertType,alert,siteId,siteName,dcSerialNo,customerId,periodStartDate,periodEndDate);
		return searchedList;
	}

	@Override
	public List<Site> getSiteListByRegion(Region region) {
		return userDAO.getSiteListByRegion(region);
	}

	@Override
	public void updateAlertAckForConsumerMeter(String registerId, Date dateFlagged) {
		userDAO.updateAlertAckForConsumerMeter(registerId,dateFlagged);
	}

	@Override
	public void updateAlertAckForDC(String dcSerialNo) {
		userDAO.updateAlertAckForDC(dcSerialNo);
		
	}

	@Override
	public boolean updateSiteInstallationFilesStatus(String fileName,String flag,int installerId) {
		return userDAO.updateSiteInstallationFilesStatus(fileName,flag,installerId);
	}

	@Override
	public JsonObject getHeaderDetailsBySiteId(Site site, Customer customer) {
		
		JsonObject json = new JsonObject();
		json.addProperty("custCode", customer.getCustomerCode());
		json.addProperty("siteName", site.getSiteName());
		json.addProperty("siteID", site.getSiteId());
		json.addProperty("regionName", site.getRegion().getRegionName());
		json.addProperty("status", site.getCurrentStatus());
		if (site.getDatacollector().size() > 0)
			json.addProperty("totalDC", site.getDatacollector().size());
		else
			json.addProperty("totalDC", "0");
		if(site.getRouteFileLastUpdate()!=null){
			json.addProperty("updatedDateTime", site.getRouteFileLastUpdate().toString());
			json.addProperty("updatedBy", site.getRouteFileLastUpdateByName());
		}
		return json;
	}

	@Override
	public DistrictMeterTransaction getDistrictMeterTransactionById(int districtMeterTransactionId) {
		return userDAO.getDistrictMeterTransactionById(districtMeterTransactionId);
	}

	@Override
	public void updateDUMeterTransaction(DistrictMeterTransaction districtMeterTransaction) {
		userDAO.updateDUMeterTransaction(districtMeterTransaction);
	}

	@Override
	public List<DataCollector> getDataCollectorBySite(Site site) {
		return userDAO.getDataCollectorBySite(site);
	}

	@Override
	public List<JsonObject> getSiteWithDCs(Region region) {
		
		List<JsonObject> siteJsonList = new ArrayList<JsonObject>();
		List<Site> siteList = userDAO.getSiteListByRegion(region);
		
		if(siteList != null){
			Iterator<Site> siteIterator = siteList.iterator();
			
			while(siteIterator.hasNext()){
				Site site = siteIterator.next();
				Hibernate.initialize(site.getDatacollector());
				if(site.getDatacollector() != null && site.getDatacollector().size() > 0){
					JsonObject json = new JsonObject();
					json.addProperty("siteId", site.getSiteId());
					json.addProperty("siteName", site.getSiteName());
					siteJsonList.add(json);
				}
			}
		}
		return siteJsonList;
	}

	@Override
	public List<DatacollectorMessageQueue> getDCMessageQueueByDCId(int datacollectorId) {
		return userDAO.getDCMessageQueueByDCId(datacollectorId);
	}

	@Override
	public void addNewDCMessage(DatacollectorMessageQueue messageQueue) {
		userDAO.addNewDCMessage(messageQueue);
	}

	@Override
	public void updateBatteryEstimatedLife(String operator,	String batteryPercentage) {
		userDAO.updateBatteryEstimatedLife(operator,batteryPercentage);
	}

	@Override
	public List<BillingHistory> getBillingHistoryByConsumerMeterId(
			String consumerMeterId) {
		return userDAO.getBillingHistoryByConsumerMeterId(consumerMeterId);
	}

	@Override
	public List<Object> getCustomerDashboardData(String customerId, String startDate, String endDate) {
		return userDAO.getCustomerDashboardData(customerId, startDate, endDate);
	}

	@Override
	public List<ConsumerMeter> getConsumerMeterListByConsumerId(Integer consumerId) {
		return userDAO.getConsumerMeterListByConsumerId(consumerId);
	}

	@Override
	public List<Object> getConsumerDashboardData(String startDate,String endDate, int consumerMeterId, String registerId) {
		return userDAO.getConsumerDashboardData(startDate, endDate, consumerMeterId, registerId);
	}

	@Override
	public List<Object> getTariffTransactionDataByTariffId(int tariffPlanId, Integer readingDiff) {
		return userDAO.getTariffTransactionDataByTariffId(tariffPlanId, readingDiff);
	}

	@Override
	public Site getSiteByCustomerAndSiteName(Customer customer, String siteName) {
		return userDAO.getSiteByCustomerAndSiteName(customer,siteName);
	}

	@Override
	public void updateTechnician(MaintenanceTechnician technician) {
		userDAO.updateTechnician(technician);
		
	}

	@Override
	public void updateInstaller(Installer installer) {
		userDAO.updateInstaller(installer);
		
	}

	@Override
	public List<ConsumerMeter> getConsumerMeterByListOfSiteId(
			List<Integer> siteId) {
		return userDAO.getConsumerMeterByListOfSiteId(siteId);
	}

	@Override
	public Customer initializeNormalCustomer(int userId) {
		return userDAO.initializeNormalCustomer(userId);
	}

	@Override
	public Customer initializeSiteAndDCForCustomer(int userId) {
		return userDAO.initializeSiteAndDCForCustomer(userId);
	}

	@Override @Transactional
	public void initializeConsumerMeters(Set<DataCollector> datacollector) {
		datacollector.stream().forEach( n -> {
			reportDAO.initializeConsumerMeterByDC(n);
		});
	}

	@Override
	public boolean inactiveDCandMeterBySiteId(Integer siteId, int customerId, Date modifiedDate) {
		return userDAO.inactiveDCandMeterBySiteId(siteId,customerId,modifiedDate);
	}

	@Override
	public Consumer getOnlyConsumerByUserId(int userId) {
		return userDAO.getOnlyConsumerByUserId(userId);
	}

}
