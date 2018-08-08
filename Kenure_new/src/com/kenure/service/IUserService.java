package com.kenure.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;
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
import com.kenure.model.UserLoginCredentials;

/**
 * 
 * @author TatvaSoft
 *
 */
public interface IUserService {

	public User authenticateUser(UserLoginCredentials loginParameters); // return
																		// type
																		// will
																		// be
																		// changed
																		// accordingly
																		// new
																		// Development.

	public void addUser();

	public boolean deleteUser(int dependantId, int roleId, int modifyingId );

	public List<User> getUserList();

	public void insertNewUser(Customer newUser);

	public User availableUserName(String userName);

	public ContactDetails getDetailsByUserID(int userID);

	public void updatePassword(User user, String password);

	public List<Customer> searchCustomer(String customerSearchCriteria,
			Date portalPlanActiveDate, Date portalPlanExpiryDate,
			Date dataPlanActiveDate, Date dataPlanExpiryDate);

	public List<DataPlan> getDataPlanList();

	public List<Customer> getCustomerList();

	public List<Customer> getActiveCustomerList();

	public User getUserDetailsByUserID(int userID);

	public Customer getCustomerDetailsByUser(int userId);

	public Consumer getConsumerDetailsByUserId(int userId);

	public boolean updateUser(Customer customer);

	public void updateCustomerDetails(ContactDetails contactdetails);

	public void addNewDataPlan(DataPlan dataplan);

	public DataPlan getDataPlanById(int dataPlanId);

	public void updateDataPlan(DataPlan dataplan);

	public List<DataPlan> searchDataplan(int mbPerMonth);

	public List<DataCollector> getSpareDataCollectorList();

	public DataCollector getSpareDataCollectorById(int datacollectorId);

	public void updateDataCollector(DataCollector datacollector);

	public void addNewDataCollector(DataCollector datacollector);

	public List<DataCollector> searchDataCollector(String customer,
			String dcSerialNumber);

	public void refreshSession(Region region);

	public void addNewSite(Site site);

	public List<DataCollector> getDCBySerialNumber(String[] dcList);

	public void addNewCollector(DataCollector thisCollector);

	public Region getSelectedRegionByName(String selectedRegion,
			Customer customer);

	public void addNewBDC(BoundryDataCollector bdc);

	public Installer getInstallerByNameAndCustomer(String installerName,
			Customer customer);

	public List<ConsumerMeter> getConsumerList(Integer id, String role);

	public Customer getOnlyCustomerByUserId(int userId);

	public Customer getCustomerDetailsByCustomerName(String customerName);

	public List<DataCollector> searchDataCollectorByCustomer(String dcIp,
			String dcSerialNumber, int customerId);

	public boolean deleteSite(int siteId, int customerId);

	List<ConsumerMeter> getConsumerListByCustomerId(int customerId);

	List<ConsumerMeter> searchConsumerByStreetName(int customerId,
			String streetName);

	public List<Installer> getInstallerByCustomerId(int customerId);

	public boolean assignInstallerToConumers(List<ConsumerMeter> consumerList,
			int installerId);

	public List<ConsumerMeter> getConsumersByInstaller(int installerId);

	public Site getSiteDataBySiteId(String siteId);

	public List<BoundryDataCollector> getAllBDCFromSiteId(
			Customer currentCustomer, String regionName,
			String[] bdc, Site siteId, String[] assignedDc, String installer);

	public List<Site> searchSiteByNameOrRegion(String siteSearchCriteria,
			String siteSearchRegion);

	public ContactDetails getContactDetailsByCustomerID(int userId);

	public Customer getCustomerDetailsByCustomerId(int customerId);

	public boolean checkForCustomerCode(String customerCode);

	public boolean updateDCsBySiteId(String sitId, Integer mri, String strDate,Customer customer, List<String> siteList,DataPlan newDataPlan);

	public List<DataCollector> getDCByInstallerId(int installerId);

	public List<DataCollector> searchDataCollectorByCustomerAndIp(String dcIp,
			String customerId);

	public boolean assignSelectedDCToInstaller(int dcId, int installerId);

	public void addNewRegion(Region region);

	public List<Region> getRegionList();

	public Region getRegionById(int regionId);

	public void updateRegion(Region regionObj);

	public List<Region> searchRegion(String regionName, int customerId);

	public List<Region> getRegionListByCustomerId(int customerId);

	public List<DistrictMeterTransaction> getDUMeterListByCustomerId(int customerId);

	public void addDUMeter(DistrictUtilityMeter districtUtilityMeter);

	public DistrictUtilityMeter getDUMeterListById(int districtUtilityMeterId);

	public void updateDUMeter(DistrictUtilityMeter districtUtilityMeter);

	public boolean checkForRegion(String regionName);

	public boolean checkForDUMeter(String dUMeterSerialNumber);

	public List<DistrictMeterTransaction> searchDUMeter(String DUMeterSerialNumber,int customerId);

	public boolean deleteDUMeter(int DUMeterId);

	public List<Installer> getInstallersByCustomerId(int customerId);
	
	public boolean insertInstaller(Installer installer);

	public Installer getInstallerById(int installerId);

	public Boolean deleteInstaller(int installerId);

	public List<Installer> searchInstallerByCriteria(String criteria,
			int customerId);

	public boolean saveTariffTransaction(TariffTransaction tariffTrans);

	public List<TariffPlan> getTariffDataByCustomer(Customer currentCustomer);

	public List<TariffPlan> searchTariffByNameAndCustomer(
			Customer currentCustomer, String tariffName);

	public boolean deleteTariffwithId(String id);

	public List<Site> searchBysiteIdOrRegionName(int sitId, String regionName);

	public List<Consumer> getConsumerUserListByCustomerId(int customerId);

	public List<TariffPlan> getTariffPlanList(int customerId);

	public boolean checkForConsumerAccNumber(String consumerAccountNumber);

	public void insertNewConsumer(Consumer consumer);

	public List<Consumer> searchConsumerByAccNum(String consumerAccNumInput,
			int customerId);

	public TariffPlan getTariffPlanById(int tariffPlanId);

	public Role getRoleById(int roleId);

	public Consumer getConsumerByConsumerID(int consumerId);

	public boolean updateConsumerUser(Consumer consumer);

	public void updateConsumerMeterUsingConsumerIdAndTariffId(
			Consumer consumer, TariffPlan oldPlan, TariffPlan tp);

	public void insertDataCollector(DataCollector datacollector);

	public boolean insertTechnician(MaintenanceTechnician technician);

	public List<MaintenanceTechnician> getTechnicianListByCustomerId(
			int customerId);

	public MaintenanceTechnician getTechnicianById(int technicianId);

	public ContactDetails getContactDetailsByID(int technicianId);

	public List<MaintenanceTechnician> searchTechnicianByCriteria(
			String criteria, int customerId);

	public boolean checkForDataPlan(int mbPerMonth);
	
	public List<ConsumerMeter> searchConsumerMeterByRegisterId(int customerId,
			int registerId);
	public List<Currency> getCurrencyList();

	public List<Country> getCountryList();

	public void saveSite(Site site);

	public void updateAdminDetails(ContactDetails details);

	public com.kenure.entity.Currency getCurrencyById(String currencyId);

	public Country getCountryById(String countryId);

	public List<Country> searchCountryByName(String searchCountryName);
	
	public List<Currency> searchCurrencyByName(String searchCurrencyName);

	public boolean checkForCountryName(String countryName);

	public boolean checkForCountryCode(String countryCode);

	public void addNewCountry(Country country);

	public void updateCountry(Country countryobj);

	public Country getCountryByName(String countryName);

	public boolean checkForCurrencyName(String currencyName);

	public void addNewCurrency(Currency currency);

	public void updateCurrency(Currency currencyobj);
	
	public Installer getInstallerByUserId(int userId);

	public boolean updateInstallerDetails(ContactDetails contactDetails);
	
	public Role getRoleByName(String name);

	public Consumer getConsumerByConsumerAccNo(String consumerAccNo);
	
	public ConsumerMeter getConsumerMetetByRegisterId(String registerId,int installerId);
	
	public boolean updateConsumerMeter(ConsumerMeter consumerMeter);

	public Consumer getValidConsumer(String customerCode,
			String consumerAccountNumber, String zipcode);

	public Customer getCustomerByCustomerCode(String customerCode);

	public boolean updateUserEntity(User editNormalUser);

	public List<User> searchUserListByUserId(String userId,Customer superCustomer);

	public boolean checkForZipcode(String zipcode, String consumerAccountNumber);
	
	public Region getRegionByName(Customer customer,String regionName);

	public void updateSiteForSiteManagement(Site site);

	public void deleteBoundryDC(BoundryDataCollector siteBD);

	public Site loadSiteDataBySiteIdAndDeleteBDC(String siteId);

	public void addNewEditedBDC(BoundryDataCollector bdc);

	public List<BatteryLife> getBatteryLifeList();

	public void addNewBatteryLife(BatteryLife batteryLife);

	public BatteryLife getBatteryLifeById(String batteryLifeId);

	public void updateBatteryLife(BatteryLife batteryLife);

	public List<BatteryLife> searchBatteryByChildNode(String childNodeInput);

	public boolean deleteBattery(int batteryLifeId);
 
	public List<String> getCustomerNotifications(int customerId);

	public boolean checkForChildNodes(String numberOfChildNodes);

	public boolean checkForSerialNumber(String dcSerialNumber);

	public boolean checkForSimcardNumber(String dcSimcardNo);
	
	public int getSiteBySiteName(String siteName);

	public void updateSite(Site site);
	
	public List<String> getInstallationFileName(int installerId,String flag);
	
	public List<ConsumerMeter> getAssetInspectionScheduleForEndpoints(
			Integer siteId, String siteName, String inspectionDueWithin, String inspectionInterval, String reportType, int customerId);

	public List<DataCollector> getAssetInspectionScheduleForDataCollectors(
			Integer siteId, String siteName, String inspectionDueWithin, String inspectionInterval, String reportType, int customerId);

	public List<Object> getConsumerMeterAlerts(
			String reportType, Integer reportPeriod, Date startDate,
			Date endDate, String selectedAlertType, String alert,
			Integer siteId, String siteName, String installationName,
			int customerId);

	public List<DataCollectorAlerts> getNetworkAlerts(String reportType, Integer reportPeriod, Date startDate,
			Date endDate,String selectedAlertType,
			String alert, Integer siteId, String siteName,
			String dcSerialNo, int customerId);

	public List<Site> getSiteListByRegion(Region region);

	public void updateAlertAckForConsumerMeter(String registerId, Date dateFlagged);

	public void updateAlertAckForDC(String dcSerialNo);
	
	public void mergeSite(Site site);
	
	public boolean updateSiteInstallationFilesStatus(String fileName,String flag,int installerId);

	public JsonObject getHeaderDetailsBySiteId(Site site, Customer customer);

	public DistrictUtilityMeter getDUMeterBySerialNumber(String districtUtilityMeterSerialNumber);

	public DistrictMeterTransaction getDistrictMeterTransactionById(int districtMeterTransactionId);

	public void updateDUMeterTransaction(DistrictMeterTransaction districtMeterTransaction);

	public List<DataCollector> getDataCollectorBySite(Site site);

	public List<JsonObject> getSiteWithDCs(Region region);

	public List<DatacollectorMessageQueue> getDCMessageQueueByDCId(int datacollectorId);

	public void addNewDCMessage(DatacollectorMessageQueue messageQueue);

	public void updateBatteryEstimatedLife(String operator,	String batteryPercentage);

	public List<BillingHistory> getBillingHistoryByConsumerMeterId(
			String consumerMeterId);

	public List<Object> getCustomerDashboardData(String customerId,	String format, String format2);

	public List<ConsumerMeter> getConsumerMeterListByConsumerId(Integer consumerId);

	public List<Object> getConsumerDashboardData(String startDate, String endDate,int consumerMeterId, String registerId);

	public List<Object> getTariffTransactionDataByTariffId(int tariffPlanId, Integer readingDiff);

	public Site getSiteByCustomerAndSiteName(Customer customer, String siteName);

	public void updateTechnician(MaintenanceTechnician technician);

	public void updateInstaller(Installer installer);

	public List<String> getConsumerAlertNotifications(Integer consumerId);

	public List<ConsumerMeter> getConsumerMeterByListOfSiteId(
			List<Integer> siteId);

	public Customer initializeNormalCustomer(int userId);

	public Customer initializeSiteAndDCForCustomer(int userId);

	public void initializeConsumerMeters(Set<DataCollector> datacollector);

	public boolean inactiveDCandMeterBySiteId(Integer siteId, int customerId, Date modifiedDate);

	public Consumer getOnlyConsumerByUserId(int userId);

}
