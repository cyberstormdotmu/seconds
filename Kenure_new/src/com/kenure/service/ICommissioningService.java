package com.kenure.service;

import java.util.Date;
import java.util.List;

import com.google.gson.JsonObject;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.Installer;
import com.kenure.entity.Site;
import com.kenure.entity.SiteInstallationFiles;
import com.kenure.model.DcInstallationFileModel;
import com.kenure.model.InstallationFileModel;
import com.kenure.model.RepeaterInstallationFileModal;

public interface ICommissioningService {

	List getAssignInstallerData(Customer currentCustomer);

	List<DataCollector> getSpareDataCollectorListByCustomer(Customer customer);

	List<Object> generateInstFiles(List<InstallationFileModel> insList, Customer customer, String siteId);
	List<SiteInstallationFiles> getSiteInstallationFilesBySiteId(Site site);

	List<SiteInstallationFiles> getSIFBySiteAndInstaller(Site site,Installer ins);

	void updateInstallerForSIF(List<String> fileNameList,Site site, Customer currentCustomer, String prefix, int tag);

	void deleteSIF(SiteInstallationFiles n);

	void deleteINSFilesFromDirectory(Site site, Customer customer,String installerName, String dcinstallationfilepath, String prefix);

	List<DataCollector> getDataCollectorBySiteList(List<Site> siteIdList);

	void assignBDCToSite(String siteId, List<String> bdcDcNumber, Customer currentCustomer);

	public List<Object> initDcLocaionInstallation(Customer currentCustomer, Site site);
	
	public List<Object> generateBDCDataBasedOnCustomerAndSite(Customer currentCustomer,Site site);

	public Site removeBDCFromSite(String siteId, List<String> bdcDcNumber,
			Customer currentCustomer);

	List<Object> generateDcInstallationFiles(List<DcInstallationFileModel> responseArray,Customer currentCustomer, String siteId, Date scheduleDate);

	List<Object> generateRepeaterInstallationFiles(
			List<RepeaterInstallationFileModal> responseArray,
			Customer currentCustomer, String siteId);

	List<Object> configureAndTestDataCollectors(
			List<String> fileNameList, Customer customer, String siteId, Date scheduledDate);

	List<JsonObject> getInstalledDCsList(List<String> filenameList,
			Customer customer, String siteId,String dcStage);

	List<Object> StartLevel1OrNComm(Site site,String dcStage);

	List<SiteInstallationFiles> getInstalledDCsFileNamesBySite(Site site);

}
