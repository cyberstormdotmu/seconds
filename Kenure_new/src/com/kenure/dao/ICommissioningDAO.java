package com.kenure.dao;

import java.util.List;

import com.kenure.entity.BoundryDataCollector;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.Installer;
import com.kenure.entity.Site;
import com.kenure.entity.SiteInstallationFiles;

public interface ICommissioningDAO {

	List<Installer> getAssignInstallerData(Customer currentCustomer);

	List<DataCollector> getSpareDataCollectorListByCustomer(Customer customer);

	List<SiteInstallationFiles> getSiteInstallationFilesBySiteId(Site site);

	List<SiteInstallationFiles> getSiteInsFilesBySiteInsAndFilePrefix(Site site,String fileFilterName);

	void updateSIF(SiteInstallationFiles sif);

	void deleteSIF(SiteInstallationFiles sif);

	List<SiteInstallationFiles> getSIFBySiteAndInstaller(Site site,Installer ins);

	SiteInstallationFiles getSIFBySiteFilterFileNameAndInstaller(Site site,
			String fileNameFilter, Installer object);

	List<DataCollector> getDataCollectorBySiteList(List<Site> siteIdList);

	DataCollector getDcByDCSerialNumberAndCustomer(String n, Customer customer);

	BoundryDataCollector getBDCBySiteAndDC(Site site, DataCollector dc);

	void updateDataCollectorConfigFields(String dcSerialNumber, String ip,
			Boolean isConnectionOk, Boolean isConfigOk, Integer batteryVoltage);

	List<SiteInstallationFiles> getInstalledDCsFileNamesBySite(Site site);

	void updateDataCollectorCommissioningFields(String dcSerialNumber,
			String ip, Boolean isLevel1CommStarted, Boolean isLevelnCommStarted);

}
