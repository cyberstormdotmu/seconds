package com.kenure.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.controller.InstallerController;
import com.kenure.dao.ICommissioningDAO;
import com.kenure.dao.IUserDAO;
import com.kenure.entity.BoundryDataCollector;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.Installer;
import com.kenure.entity.Site;
import com.kenure.entity.SiteInstallationFiles;
import com.kenure.model.DcInstallationFileModel;
import com.kenure.model.InstallationFileModel;
import com.kenure.model.RepeaterInstallationFileModal;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.LoggerUtils;
import com.kenure.utils.MailSender;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

@Service
@Transactional
public class CommissioningServiceImpl implements ICommissioningService {

	@Autowired
	IUserDAO userDAO;

	@Autowired
	ICommissioningDAO commissioningDAO;

	@Autowired
	private MailSender emailSender;
	
	@Autowired
	private IReportService reportService;
	
	@Autowired
	ServletContext servletContext;
	
	static Map<String,JsonObject> dcIpMap;
	
	private org.slf4j.Logger log = LoggerUtils.getInstance(this.getClass());

	@Override
	public List<Installer> getAssignInstallerData(Customer currentCustomer) {
		return commissioningDAO.getAssignInstallerData(currentCustomer);
	}

	@Override
	public List<DataCollector> getSpareDataCollectorListByCustomer(Customer customer) {
		return commissioningDAO.getSpareDataCollectorListByCustomer(customer);
	}

	@Override
	public List<SiteInstallationFiles> getSiteInstallationFilesBySiteId(Site site) {
		return commissioningDAO.getSiteInstallationFilesBySiteId(site);
	}



	@Override
	public List<Object> generateInstFiles(List<InstallationFileModel> insList, Customer customer, String siteId) {
		int loop= 0;

		// Response List
		List<Object> responseList = new ArrayList<Object>();	
		// Getting site dependency by siteId
		Site site = userDAO.getSiteDataBySiteId(siteId); 
		// Checking for existence of directory if not than we will create new
		String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
		String EpInsFilePath = resourcesPath+File.separator+ApplicationConstants.EPINSTALLATIONFILEPATH;
		InstallerController.createMediaDirectoresIfNotExists(EpInsFilePath);
		// Now lets read again master file and gather its content accordingly installer name
		File tempServerFile = new File(resourcesPath+File.separator+ApplicationConstants.MASTERFILEPATH+File.separator+site.getRouteFileName());
		Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();

		try(CSVReader csvReader = new CSVReader(new FileReader(tempServerFile.getAbsolutePath()),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER,1)) {
			String[] line;
			List<String[]> mapList;
			while((line = csvReader.readNext()) != null){
				if(map.containsKey(line[3])){
					mapList = map.get(line[3]);
					mapList.add(line);
					map.put(line[3], mapList);
					/*map.put(line[4],map.get(line[4])+1);*/
				}else{
					List<String[]> list = new ArrayList<String[]>();
					list.add(line);
					map.put(line[3],list);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> generatedFileList = new ArrayList<String>();

		for(loop=0;loop<insList.size();loop++){
			InstallationFileModel currentInsFileModel = insList.get(loop);

			// Getting Installer Dependency
			//Installer ins = userDAO.getInstallerByNameAndCustomer(insList.get(loop).getInstallerName(), customer);
			String date = DateTimeConversionUtils.getTodaysDateAsString();
			String fileName = customer.getCustomerCode()+"_"+site.getSiteId()+"_"+insList.get(loop).getInstallerName()+"_"+date+".csv";
			File finalServerFile = new File(EpInsFilePath+File.separator+fileName);
			log.info("File Name will be >> "+fileName);

			// before creating lets check for this file already exists ?
			checkForExistedFile(customer,site,insList.get(loop).getInstallerName(),EpInsFilePath,"");

			// After deleting from file structure .. we have to delete it from DB too
			String fileFilterName = customer.getCustomerCode()+"_"+site.getSiteId()+"_"+insList.get(loop).getInstallerName();
			List<SiteInstallationFiles> listSIF  = commissioningDAO.getSiteInsFilesBySiteInsAndFilePrefix(site,fileFilterName);

			if(listSIF != null && listSIF.size() > 0){
				for(int i = 0;i<listSIF.size();i++){
					SiteInstallationFiles sif = listSIF.get(i);
					sif.setInstaller(null);
					sif.setSite(null);
					commissioningDAO.updateSIF(sif);
					commissioningDAO.deleteSIF(sif);
				}
			}

			if(currentInsFileModel.getGroupValue().size() > 0){
				try(CSVWriter csvWriter = new CSVWriter(new FileWriter(finalServerFile.getAbsolutePath()),CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.NO_ESCAPE_CHARACTER)) {
					int innerLoop = 0;
					int totalEndPoint = 0;
					String[] headerLine = ("Serial,RegisterID,AccountNo,Street Name,Address2,Address3,Address4,Zipcode"
							+ ",Latitude,Longitude,Reading,ReadingTime,BillingLeft,BillingRight,DecimalPos,Leakage,Backflow,"
							+ "UsageThreshold,UsageInterval,KValue,Direction,UtilityCode,RepeaterNodes,RepeaterLevels,EndpointMode,"
							+ "FWVersion,SiteID,Scheduled Date,District utility id").split(",");
					csvWriter.writeNext(headerLine);
					for(innerLoop=0;innerLoop<currentInsFileModel.getGroupValue().size();innerLoop++){
						String thisStreet = currentInsFileModel.getGroupValue().get(innerLoop).getStreet();
						if(map.containsKey(thisStreet)){
							csvWriter.writeAll(map.get(thisStreet));
							totalEndPoint = totalEndPoint + currentInsFileModel.getGroupValue().get(innerLoop).getEndpoint() ;
						}
					}
					// File is created.So we have to maintain its entry in DB.
					SiteInstallationFiles sf = new SiteInstallationFiles();
					sf.setFileName(fileName);
					sf.setIsFileUploaded(Boolean.FALSE);
					sf.setIsFileVerified(Boolean.FALSE); // setting true but have to discuss how its working !!
					sf.setNoOfEndPoints(totalEndPoint);
					// Setting up relationship of SiteInsFiles
					sf.setSite(site);
					/*sf.setInstaller(ins);*/
					userDAO.saveSiteInsFiles(sf);
					generatedFileList.add(fileName);
				} catch (IOException e) {
					log.warn(e.getMessage());
				}
			}

		}
		if(generatedFileList.size() > 0){
			site.setTag(4);
			userDAO.updateSite(site);
			responseList.add(generatedFileList);
			responseList.add(site.getTag());
		}
		return responseList;
	}

	private void checkForExistedFile(Customer customer, Site site,
			String installerName, String installationfilepath, String prefix) {

		if(installerName != null){
			String fileFilterName = prefix+customer.getCustomerCode()+"_"+site.getSiteId()+"_"+installerName+"_";

			File root = new File(installationfilepath);
			File[] files = root.listFiles((dir,filename) -> filename.startsWith(fileFilterName));

			for (File f: files){
				f.delete();
			}
		}

	}

	@Override
	public List<SiteInstallationFiles> getSIFBySiteAndInstaller(Site site,Installer ins) {
		return commissioningDAO.getSIFBySiteAndInstaller(site,ins);
	}

	@Override
	public void updateInstallerForSIF(List<String> fileNameList,Site site,Customer currentCustomer,String prefix,int tag) {
		// First Get Current Site's all SIF
		try{
			if(fileNameList != null && fileNameList.size() > 0){
				for(int i=0;i<fileNameList.size();i++){
					String fileNameFilter = (prefix+currentCustomer.getCustomerCode()+"_"+site.getSiteId()+"_");
					String insName = fileNameList.get(i);
					fileNameFilter = fileNameFilter + insName + "_";
					SiteInstallationFiles sif = new SiteInstallationFiles();
					sif = getSIFIndexofListSIF(site, fileNameFilter,null);
					Installer ins = userDAO.getInstallerByNameAndCustomer(insName, currentCustomer);
					sif.setInstaller(ins);
					commissioningDAO.updateSIF(sif);
				}
			}
			// Setting up new phase
			site.setTag(tag);
			if(tag==7)
				site.setCurrentStatus("installation");
			userDAO.mergeSite(site);
		}catch(Exception e){
			log.warn("Error while setting Assignig Installer");
		}
	}


	private SiteInstallationFiles getSIFIndexofListSIF(Site site,String fileNameFilter,Installer ins){
		return commissioningDAO.getSIFBySiteFilterFileNameAndInstaller(site,fileNameFilter,ins);
	}

	@Override
	public void deleteSIF(SiteInstallationFiles n) {
		n.setSite(null);
		n.setInstaller(null);
		commissioningDAO.deleteSIF(n);
	}

	@Override
	public void deleteINSFilesFromDirectory(Site site, Customer customer,
			String installerName,String installationFilePath,String prefix) {
		checkForExistedFile(customer, site, installerName,installationFilePath,prefix);
	}

	@Override
	public List<DataCollector> getDataCollectorBySiteList(
			List<Site> siteIdList) {
		return commissioningDAO.getDataCollectorBySiteList(siteIdList);
	}

	@Override
	public void assignBDCToSite(String siteId, List<String> bdcDcNumber,Customer customer) {
		Site site = userDAO.getSiteDataBySiteId(siteId);
		bdcDcNumber.stream().forEach(n ->{
			BoundryDataCollector bdc = new BoundryDataCollector();
			DataCollector dc = commissioningDAO.getDcByDCSerialNumberAndCustomer(n,customer);
			bdc.setSite(site);
			bdc.setDatacollector(dc);
			userDAO.addNewBDC(bdc);
		});

	}

	@Override
	public List<Object> generateBDCDataBasedOnCustomerAndSite(Customer currentCustomer,Site site){

		List<Site> siteIdList = new ArrayList<Site>();
		List<Object> responseList = new ArrayList<Object>();
		reportService.initializeRegionByCustomer(currentCustomer);;
		currentCustomer.getRegion().stream().forEach(region ->{
			region.getSite().stream().filter(filt -> (filt.getCurrentStatus()!=null 
					&& filt.getSiteId() != site.getSiteId() 
					&& filt.getCurrentStatus().equalsIgnoreCase("commissioned"))).forEach(filtSite -> {
						siteIdList.add(filtSite);
					});
		});

		List<DataCollector> customerDCList =  getDataCollectorBySiteList(siteIdList);
		List<JsonObject> customerDCJsonList = new ArrayList<JsonObject>();

		List<BoundryDataCollector> currentSitesBDC = new ArrayList<>(site.getBoundrydatacollector());
		List<JsonObject> currentSitesBDCJsonList = new ArrayList<JsonObject>();

		if(customerDCList!=null){
			// All DC thaose are able to BDC
			customerDCList.stream().forEach(n ->{
				JsonObject jObject = new JsonObject();
				jObject.addProperty("dcSerialNumber", n.getDcSerialNumber());
				if(n.getNetworkId() != null)
					jObject.addProperty("networkID",n.getNetworkId());
				else
					jObject.addProperty("networkID",0);
				customerDCJsonList.add(jObject);
			});
		}else{
			JsonObject jObject = new JsonObject();
			jObject.addProperty("noDataFound", "noDataFound");
			customerDCJsonList.add(jObject);
		}
		// Current Sites already assigned BDC
		currentSitesBDC.stream().forEach(n ->{
			JsonObject jObject = new JsonObject();
			jObject.addProperty("dcSerialNumber", n.getDatacollector().getDcSerialNumber());
			if(n.getDatacollector().getNetworkId() != null)
				jObject.addProperty("networkID",n.getDatacollector().getNetworkId());
			else
				jObject.addProperty("networkID",0);
			currentSitesBDCJsonList.add(jObject);
		});

		// Lets remove already assigned DC From All DC
		customerDCJsonList.removeAll(currentSitesBDCJsonList);
		
		customerDCJsonList.sort((JsonObject j1,JsonObject j2) -> j1.get("dcSerialNumber").getAsString().compareTo(j2.get("dcSerialNumber").getAsString()));
		currentSitesBDCJsonList.sort((JsonObject j1,JsonObject j2) -> j1.get("dcSerialNumber").getAsString().compareTo(j2.get("dcSerialNumber").getAsString()));
		
		responseList.add(customerDCJsonList);
		responseList.add(currentSitesBDCJsonList);
		
		return responseList;
	}

	@Override
	public List<Object> initDcLocaionInstallation(Customer currentCustomer, Site site) {
		List<Installer> insList = getAssignInstallerData(currentCustomer); 
		List<String> installernamesList = new ArrayList<String>();
		List<Object> responseList = new ArrayList<>();

		insList.forEach(n -> {
			if(n.isActive())
				installernamesList.add(n.getInstallerName());			
		});
		responseList.add(site.getTag());
		responseList.add(installernamesList);
		return responseList;
	}

	@Override
	public Site removeBDCFromSite(String siteId, List<String> bdcDcNumber,
			Customer currentCustomer) {
		Site site = userDAO.getSiteDataBySiteId(siteId);
		try{
			Set<BoundryDataCollector> dcSet = site.getBoundrydatacollector();
			dcSet.stream().forEach(n -> {
				if(bdcDcNumber.contains(n.getDatacollector().getDcSerialNumber())){
					dcSet.remove(n);
				}
			});
			site.setBoundrydatacollector(dcSet);
			bdcDcNumber.stream().forEach(n ->{
				DataCollector dc = commissioningDAO.getDcByDCSerialNumberAndCustomer(n,currentCustomer);
				BoundryDataCollector bdc = commissioningDAO.getBDCBySiteAndDC(site,dc);
				bdc.setSite(null);
				bdc.setDatacollector(null);
				userDAO.deleteBoundryDC(bdc);
			});
			return site;
		}catch(Exception e){
			e.printStackTrace();
		}
		return site;
	}

	@Override
	public List<Object> generateDcInstallationFiles(List<DcInstallationFileModel> insList,Customer customer, String siteId,Date scheduleDate) {
		int loop= 0;

		// Response List
		List<Object> responseList = new ArrayList<Object>();	
		// Getting site dependency by siteId
		Site site = userDAO.getSiteDataBySiteId(siteId); 
		// Checking for existence of directory if not than we will create new
		String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
		String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
		InstallerController.createMediaDirectoresIfNotExists(DcInsFilePath);

		List<String> generatedFileList = new ArrayList<String>();

		for(loop=0;loop<insList.size();loop++){
			DcInstallationFileModel currentInsFileModel = insList.get(loop);

			// Getting Installer Dependency
			//Installer ins = userDAO.getInstallerByNameAndCustomer(insList.get(loop).getInstallerName(), customer);
			String date = DateTimeConversionUtils.getTodaysDateAsString();
			String fileName = "DC_"+customer.getCustomerCode()+"_"+site.getSiteId()+"_"+insList.get(loop).getInstallerName()+"_"+date+".csv";
			File finalServerFile = new File(DcInsFilePath+File.separator+fileName);
			log.info("File Name will be >> "+fileName);

			// before creating lets check for this file already exists ?
			checkForExistedFile(customer,site,insList.get(loop).getInstallerName(),DcInsFilePath,"DC_");

			// After deleting from file structure .. we have to delete it from DB too
			String fileFilterName = "DC_"+customer.getCustomerCode()+"_"+site.getSiteId()+"_"+insList.get(loop).getInstallerName();
			List<SiteInstallationFiles> listSIF  = commissioningDAO.getSiteInsFilesBySiteInsAndFilePrefix(site,fileFilterName);

			if(listSIF != null && listSIF.size() > 0){
				for(int i = 0;i<listSIF.size();i++){
					SiteInstallationFiles sif = listSIF.get(i);
					sif.setInstaller(null);
					sif.setSite(null);
					commissioningDAO.updateSIF(sif);
					commissioningDAO.deleteSIF(sif);
				}
			}

			if(currentInsFileModel.getGroupValue().size() > 0){
				try(CSVWriter csvWriter = new CSVWriter(new FileWriter(finalServerFile.getAbsolutePath()),CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.NO_ESCAPE_CHARACTER)) {
					int innerLoop = 0;
					int totalDataCollector = 0;
					String[] line = new String[13];
					String [] HeaderLine = "DC Location,Network Type,Latitude,Longitude,Customer Code,Site ID,DC Serial No,IP Address,Network ID,Username,Password,Signal Strength,Scheduled Date".split(",");
					csvWriter.writeNext(HeaderLine);
					for(innerLoop=0;innerLoop<currentInsFileModel.getGroupValue().size();innerLoop++){
							line[0]= currentInsFileModel.getGroupValue().get(innerLoop).getDcLocation().toString();
							line[1]= currentInsFileModel.getGroupValue().get(innerLoop).getNetworkType();
							line[2]= currentInsFileModel.getGroupValue().get(innerLoop).getLatitude().toString();
							line[3]= currentInsFileModel.getGroupValue().get(innerLoop).getLongitude().toString();
							line[4]= customer.getCustomerCode();
							line[5]= siteId;
							line[12]=scheduleDate!=null?scheduleDate.toString():null;
							csvWriter.writeNext(line);
							totalDataCollector = totalDataCollector + 1 ;
					}
					// File is created.So we have to maintain its entry in DB.
					SiteInstallationFiles sf = new SiteInstallationFiles();
					sf.setFileName(fileName);
					sf.setIsFileUploaded(Boolean.FALSE);
					sf.setIsFileVerified(Boolean.FALSE); // setting true but have to discuss how its working !!
					sf.setNoOfDatacollectors(totalDataCollector);
					// Setting up relationship of SiteInsFiles
					sf.setSite(site);
					/*sf.setInstaller(ins);*/
					userDAO.saveSiteInsFiles(sf);
					generatedFileList.add(fileName);
				} catch (IOException e) {
					log.warn(e.getMessage());
				}
			}
		}
		if(generatedFileList.size() > 0){
			site.setTag(6);
			userDAO.updateSite(site);
			responseList.add(generatedFileList);
			responseList.add(site.getTag());
		}
		return responseList;
	}

	@Override
	public List<Object> generateRepeaterInstallationFiles(List<RepeaterInstallationFileModal> insList,Customer customer, String siteId) {
		int loop= 0;
		// Response List
		List<Object> responseList = new ArrayList<Object>();	
		// Getting site dependency by siteId
		Site site = userDAO.getSiteDataBySiteId(siteId); 
		// Checking for existence of directory if not than we will create new
		String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
		String RepInsFilePath = resourcesPath+File.separator+ApplicationConstants.REPINSTALLATIONFILEPATH;
		InstallerController.createMediaDirectoresIfNotExists(RepInsFilePath);

		List<String> generatedFileList = new ArrayList<String>();
		String prefix = "REP_";
		for(loop=0;loop<insList.size();loop++){
			RepeaterInstallationFileModal currentInsFileModel = insList.get(loop);

			// Getting Installer Dependency
			//Installer ins = userDAO.getInstallerByNameAndCustomer(insList.get(loop).getInstallerName(), customer);
			String date = DateTimeConversionUtils.getTodaysDateAsString();
			String fileName = prefix+customer.getCustomerCode()+"_"+site.getSiteId()+"_"+insList.get(loop).getInstallerName()+"_"+date+".csv";
			File finalServerFile = new File(RepInsFilePath+File.separator+fileName);
			log.info("File Name will be >> "+fileName);

			// before creating lets check for this file already exists ?
			checkForExistedFile(customer,site,insList.get(loop).getInstallerName(),RepInsFilePath,prefix);

			// After deleting from file structure .. we have to delete it from DB too
			String fileFilterName = prefix+customer.getCustomerCode()+"_"+site.getSiteId()+"_"+insList.get(loop).getInstallerName();
			List<SiteInstallationFiles> listSIF  = commissioningDAO.getSiteInsFilesBySiteInsAndFilePrefix(site,fileFilterName);

			if(listSIF != null && listSIF.size() > 0){
				for(int i = 0;i<listSIF.size();i++){
					SiteInstallationFiles sif = listSIF.get(i);
					sif.setInstaller(null);
					sif.setSite(null);
					commissioningDAO.updateSIF(sif);
					commissioningDAO.deleteSIF(sif);
				}
			}

			if(currentInsFileModel.getGroupValue().size() > 0){
				try(CSVWriter csvWriter = new CSVWriter(new FileWriter(finalServerFile.getAbsolutePath()),CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.NO_ESCAPE_CHARACTER)) {
					int innerLoop = 0;
					String[] line = new String[5];
					for(innerLoop=0;innerLoop<currentInsFileModel.getGroupValue().size();innerLoop++){
							line[0]= currentInsFileModel.getGroupValue().get(innerLoop).getRepeaterLocation().toString();
							line[1]= currentInsFileModel.getGroupValue().get(innerLoop).getLatitude().toString();
							line[2]= currentInsFileModel.getGroupValue().get(innerLoop).getLongitude().toString();
							line[3]= currentInsFileModel.getGroupValue().get(innerLoop).getSlots().toString();
							line[4]= currentInsFileModel.getGroupValue().get(innerLoop).getLevels().toString();
							csvWriter.writeNext(line);
					}
					// File is created.So we have to maintain its entry in DB.
					SiteInstallationFiles sf = new SiteInstallationFiles();
					sf.setFileName(fileName);
					sf.setIsFileUploaded(Boolean.FALSE);
					sf.setIsFileVerified(Boolean.FALSE); // setting true but have to discuss how its working !!
					// Setting up relationship of SiteInsFiles
					sf.setSite(site);
					userDAO.saveSiteInsFiles(sf);
					generatedFileList.add(fileName);
				} catch (IOException e) {
					log.warn(e.getMessage());
				}
			}
		}
		if(generatedFileList.size() > 0){
			site.setTag(8);
			userDAO.updateSite(site);
			responseList.add(generatedFileList);
			responseList.add(site.getTag());
		}
		return responseList;
	}
	
	@Override
	public List<SiteInstallationFiles> getInstalledDCsFileNamesBySite(Site site) {
		return commissioningDAO.getInstalledDCsFileNamesBySite(site);
	}

	@Override
	public List<Object> configureAndTestDataCollectors(List<String> fileNameList, Customer customer, String siteId, Date scheduledDate) {
		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> jObjetcList = new ArrayList<JsonObject>();
		dcIpMap = new ConcurrentHashMap<String, JsonObject>();
		String prefix = "DC_";
		Long unixScheduledTime = scheduledDate!=null?scheduledDate.getTime():-1;
		for(String fileName:fileNameList){
			if(fileName.startsWith(prefix)){
				String installerName = fileName.substring(0,fileName.lastIndexOf("_"));
				installerName = installerName.substring((prefix+customer.getCustomerCode()+"_"+siteId+"_").length());
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
				File file = new File(DcInsFilePath+File.separator+fileName);
				try(CSVReader csvReader = new CSVReader(new FileReader(file.getAbsolutePath()),CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,1)) {
					String[] line;
					while((line = csvReader.readNext()) != null){
						JsonObject json = new JsonObject();
						json.addProperty("dcSerialNumber",line[6]);
						json.addProperty("installerName",installerName);
						json.addProperty("dcIp",line[7]);
						json.addProperty("batteryVoltage",line[1].trim().equalsIgnoreCase("mains")?line[1]:"-");
						json.addProperty("isConnectionOk","In Progress");
						json.addProperty("isConfigOk","In Progress");
						jObjetcList.add(json);
						JsonObject dcjson = new JsonObject();
						dcjson.addProperty("dcSerialNumber",line[6]);
						dcjson.addProperty("dcIp",line[7]);
						dcjson.addProperty("port",49207);
						dcjson.addProperty("username", line[9]);
						dcjson.addProperty("password", line[10]);
						dcjson.addProperty("customerCode", line[4]);
						dcjson.addProperty("networkId", line[8]);
						dcjson.addProperty("counter", 3);
						dcjson.addProperty("unixScheduledTime", unixScheduledTime);
						dcjson.addProperty("dcStage", "INSTALLATION");
						dcIpMap.put(line[7].trim(),dcjson);
					}
				} catch (FileNotFoundException e) {
					log.warn("Warning - {} occured in method configureAndTestDataCollectors",e.getMessage());
				} catch (IOException e) {
					log.error("Error occured in method configureAndTestDataCollectors - {}",e.getMessage());
				}
			}
		}
		if(dcIpMap!=null && dcIpMap.size()>0){
			try{
				Thread myThread = new Thread(){
					boolean loopFlag = false;
					@Override
					public void run(){
						do{
							executeService();
						}while(loopFlag);
						
						System.out.println("Executor Thread Final Finished! - THE END");
					}
					private void executeService(){
						//int mapLength = dcIpMap.size();
						//ExecutorService executor = Executors.newFixedThreadPool(mapLength);
						WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
						ExecutorService executor = (ExecutorService) ctx.getBean("fixThreadExecutor");
						for (Entry<String, JsonObject> entry : CommissioningServiceImpl.dcIpMap.entrySet()) {
							System.out.println("Thread started for "+entry.getKey()+" IP");
							executor.execute(new DCConnection(entry.getValue(),commissioningDAO));
						}
						executor.shutdown();
				        while (!executor.isTerminated()) {
				        }
				        System.out.println("Current Executor Thread Terminated!");
				        System.out.println("Finished all IP threads - map Size - "+ CommissioningServiceImpl.dcIpMap.size());
				        log.info("Finished all threads");
				        log.info("map Size - "+ CommissioningServiceImpl.dcIpMap.size());
				        if(CommissioningServiceImpl.dcIpMap.size() == 0 && CommissioningServiceImpl.dcIpMap.isEmpty()) {
				        	loopFlag = false;
				        }else{
				        	try {
				        		System.out.println("New Executor Thread will start again after 2 minutes because map is not empty!");
								Thread.sleep(120000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				        	loopFlag = true;
				        }
					}
				};
				myThread.start();
			}catch(RejectedExecutionException re){
				log.error(re.getMessage());
			}
		}
		if(jObjetcList!=null && jObjetcList.size()>0){
			Site site = userDAO.getSiteDataBySiteId(siteId);
			site.setTag(9);
			userDAO.updateSite(site);
			objectList.add(jObjetcList);
			objectList.add(site.getTag());
			return objectList;
		}
		return null;
	}

	@Override
	public List<JsonObject> getInstalledDCsList(List<String> filenameList,
			Customer customer, String siteId, String dcStage) {
		List<JsonObject> jObjetcList = new ArrayList<JsonObject>();
		String prefix = "DC_";
		for(String fileName:filenameList){
			if(fileName.startsWith(prefix)){
				String installerName = fileName.substring(0,fileName.lastIndexOf("_"));
				installerName = installerName.substring((prefix+customer.getCustomerCode()+"_"+siteId+"_").length());
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
				File file = new File(DcInsFilePath+File.separator+fileName);
				try(CSVReader csvReader = new CSVReader(new FileReader(file.getAbsolutePath()),CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,1)) {
					String[] line;
					while((line = csvReader.readNext()) != null){
						DataCollector dc  = commissioningDAO.getDcByDCSerialNumberAndCustomer(line[6].trim(), customer);
						if(dc!=null){
							JsonObject json = new JsonObject();
							if(dcStage.equals("INSTALLATION")){
								json.addProperty("dcSerialNumber",dc.getDcSerialNumber());
								json.addProperty("installerName",installerName);
								json.addProperty("dcIp",dc.getDcIp());
								json.addProperty("batteryVoltage",dc.getBatteryVoltage()!=null?dc.getBatteryVoltage().toString():"-");
								
								if(dc.getIsConnectionOk()!=null)
									json.addProperty("isConnectionOk",(dc.getIsConnectionOk()==true)?"Yes":"No");
								else
									json.addProperty("isConnectionOk","In Progress");
								
								if(dc.getIsConfigOk()!=null)
									json.addProperty("isConfigOk",(dc.getIsConfigOk()==true)?"Yes":"No");
								else
									json.addProperty("isConfigOk","In Progress");
								
								json.addProperty("port",49207);
								json.addProperty("username", line[9]);
								json.addProperty("password", line[10]);
							}else if(dcStage.equals("LEVEL1-COMMISSIONING")){
								json.addProperty("dcSerialNumber",dc.getDcSerialNumber());
								json.addProperty("dcIp",dc.getDcIp());
								if(dc.getIsLevel1CommStarted()!=null)
									json.addProperty("isLevel1CommStarted",(dc.getIsLevel1CommStarted()==true)?"Yes":"No");
								else
									json.addProperty("isLevel1CommStarted","In Progress");
							}else if(dcStage.equals("LEVELN-COMMISSIONING")){
								json.addProperty("dcSerialNumber",dc.getDcSerialNumber());
								json.addProperty("dcIp",dc.getDcIp());
								if(dc.getIsLevelnCommStarted()!=null)
									json.addProperty("isLevelnCommStarted",(dc.getIsLevelnCommStarted()==true)?"Yes":"No");
								else
									json.addProperty("isLevelnCommStarted","In Progress");
							}
							jObjetcList.add(json);
						}
					}
				} catch (FileNotFoundException e) {
					log.warn("Warning - {} occured in method configureAndTestDataCollectors",e.getMessage());
				} catch (IOException e) {
					log.error("Error occured in method configureAndTestDataCollectors - {}",e.getMessage());
				}
			}
		}
		if(jObjetcList!=null && jObjetcList.size()>0){
			return jObjetcList;
		}
		return null;
	}

	@Override
	public List<Object> StartLevel1OrNComm(Site site,String dcStage) {
		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> jObjetcList = new ArrayList<JsonObject>();
		
		List<SiteInstallationFiles> sifList = commissioningDAO.getInstalledDCsFileNamesBySite(site);
		if(sifList!=null){
			dcIpMap = new ConcurrentHashMap<String, JsonObject>();
			for(SiteInstallationFiles sif : sifList){
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
				File file = new File(DcInsFilePath+File.separator+sif.getFileName());
				try(CSVReader csvReader = new CSVReader(new FileReader(file.getAbsolutePath()),CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,1)) {
					String[] line;
					while((line = csvReader.readNext()) != null){
						JsonObject json = new JsonObject();
						json.addProperty("dcSerialNumber",line[6]);
						json.addProperty("dcIp",line[7]);
						if(dcStage.equals("LEVEL1-COMMISSIONING"))
							json.addProperty("isLevel1CommStarted","In Progress");
						else if(dcStage.equals("LEVELN-COMMISSIONING"))
							json.addProperty("isLevelnCommStarted","In Progress");
						jObjetcList.add(json);
						JsonObject dcjson = new JsonObject();
						dcjson.addProperty("dcSerialNumber",line[6]);
						dcjson.addProperty("dcIp",line[7]);
						dcjson.addProperty("port",49207);
						dcjson.addProperty("username", line[9]);
						dcjson.addProperty("password", line[10]);
						dcjson.addProperty("counter", 5);
						if(dcStage.equals("LEVEL1-COMMISSIONING"))
							dcjson.addProperty("dcStage", "LEVEL1-COMMISSIONING");
						else if(dcStage.equals("LEVELN-COMMISSIONING"))
							dcjson.addProperty("dcStage", "LEVELN-COMMISSIONING");
						
						dcIpMap.put(line[7].trim(),dcjson);
					}
				} catch (FileNotFoundException e) {
					log.warn("Warning - {} occured in method getInstalledDCsFileNamesBySiteId",e.getMessage());
				} catch (IOException e) {
					log.error("Error occured in method getInstalledDCsFileNamesBySiteId - {}",e.getMessage());
				}
			}
		}
		if(dcIpMap!=null && dcIpMap.size()>0){
			try{
				Thread myThread = new Thread(){
					boolean loopFlag = false;
					@Override
					public void run(){
						do{
							executeService();
						}while(loopFlag);
						
						System.out.println("Executor Thread Final Finished! - THE END");
					}
					private void executeService(){
						WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
						ExecutorService executor = (ExecutorService) ctx.getBean("fixThreadExecutor");
						for (Entry<String, JsonObject> entry : CommissioningServiceImpl.dcIpMap.entrySet()) {
							System.out.println("Thread started for "+entry.getKey()+" IP");
							executor.execute(new DCConnection(entry.getValue(),commissioningDAO));
						}
						executor.shutdown();
				        while (!executor.isTerminated()) {
				        }
				        System.out.println("Current Executor Thread Terminated!");
				        System.out.println("Finished all IP threads - map Size - "+ CommissioningServiceImpl.dcIpMap.size());
				        log.info("Finished all threads");
				        log.info("map Size - "+ CommissioningServiceImpl.dcIpMap.size());
				        if(CommissioningServiceImpl.dcIpMap.size() == 0 && CommissioningServiceImpl.dcIpMap.isEmpty()) {
				        	loopFlag = false;
				        }else{
				        	try {
				        		System.out.println("New Executor Thread will start again after 2 minutes because map is not empty!");
								Thread.sleep(120000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				        	loopFlag = true;
				        }
					}
				};
				myThread.start();
			}catch(RejectedExecutionException re){
				log.error(re.getMessage());
			}
		}
		if(jObjetcList!=null && jObjetcList.size()>0){
			if(dcStage.equals("LEVEL1-COMMISSIONING"))
				site.setTag(11);
			else if(dcStage.equals("LEVELN-COMMISSIONING"))
				site.setTag(13);
			userDAO.updateSite(site);
			objectList.add(jObjetcList);
			objectList.add(site.getTag());
			return objectList;
		}
		return null;
	}

}
