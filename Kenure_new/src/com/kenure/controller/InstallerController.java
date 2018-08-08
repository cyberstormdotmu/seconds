package com.kenure.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ContactDetails;
import com.kenure.entity.DataCollector;
import com.kenure.entity.Installer;
import com.kenure.entity.User;
import com.kenure.service.ICommissioningService;
import com.kenure.service.IConsumerMeterService;
import com.kenure.service.IDistrictUtilityMeterService;
import com.kenure.service.IUserService;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.DownloadFilesUtills;
import com.kenure.utils.LoggerUtils;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

@Controller
public class InstallerController {

	@Autowired
	IUserService userService;

	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private ICommissioningService commissioningService;
	
	@Autowired
	private IDistrictUtilityMeterService meterService;
	
	@Autowired
	private IConsumerMeterService consumerMeterService;
	
	private static final Logger logger = LoggerUtils.getInstance(LoginController.class);

	// TO REDIRECT UPLOAD DOWNLOAD PAGE
	@RequestMapping(value="/uploadDownloadDCRedirect",method=RequestMethod.GET)
	public ModelAndView uploadDownloadRedirect(HttpServletRequest request, HttpServletResponse response){

		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		logger.info("Redirecting to uploadDownloadRedirect for user"+currentUser.getUserId());
		ModelAndView model = new ModelAndView("uploadDownloadDCInstaller");
		if(currentUser.getRole().getRoleName().equalsIgnoreCase("installer")){
			Installer ins = userService.getInstallerByUserId(currentUser.getUserId());
			model.addObject("installer",ins);
		}
		return model;
	}


	@RequestMapping(value="/downloadDCForInstaller",method=RequestMethod.POST)
	public void downloadDCForInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam("installerId") String installerId,@RequestParam(value="includeHeader",required=false) boolean includeHeader) throws IOException{
		logger.info("downloading installation file of DC for Installer "+installerId);
		List<String> fileNames = userService.getInstallationFileName(Integer.parseInt(installerId),"DC");
		DownloadFilesUtills.downloadFiles(fileNames,response,servletContext,"DC");

	}


	@RequestMapping(value = "/uploadDCForInstaller", method = RequestMethod.POST)
	public @ResponseBody ModelAndView uploadDCForInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam("file") MultipartFile file,@RequestParam(value = "includeHeader",required=false)
	boolean includeHeader,@RequestParam("installerId") String installerId,RedirectAttributes redirectAttribute) throws IOException {
		logger.info("uploading file to server ");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/uploadDownloadDCRedirect");
		
		if(file.getOriginalFilename().toLowerCase().lastIndexOf(".csv")!=-1){
			List<String> fileNames = userService.getInstallationFileName(Integer.parseInt(installerId),"DC"); 
			String uploadedFileName = file.getOriginalFilename();
			logger.info("File uploaded by installer = "+file.getOriginalFilename());
			if (!file.isEmpty()) {

				String serverFileName = null;
				//check both file name match
				if(fileNames != null){
					for(String fileName:fileNames){
						if(uploadedFileName.equals(fileName)){
							serverFileName = fileName;
							break;
						}
					}
				}else{
					logger.info("Failed to upload File because generated file is not exist!");
					redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because generated file is not exist!");
					return mav;
				}
				if(serverFileName != null){
					logger.info("File Exist in Server");
					
					String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
					//Take Server file
					String filePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
					File serverFile = new File(filePath+File.separator+uploadedFileName);
					
					// Take uploaded file
					String tempFilePath =  resourcesPath+File.separator+ApplicationConstants.TEMPFILEPATH+File.separator+"temp_upload_"+installerId+".csv";
					File uploadedFile = new File(tempFilePath);
					boolean tempFileUploaded = uploadedFile.createNewFile();
					
					if(tempFileUploaded){
						// Temporary upload file to server
						FileOutputStream fos = new FileOutputStream(uploadedFile);
						fos.write(file.getBytes());
						fos.close();
					}
					
					// Compare both file
					CSVReader tempuploadedCsvReader = null;
					CSVReader tempserverCsvReader = new CSVReader(new FileReader(serverFile));
					   if(includeHeader){
						   tempuploadedCsvReader = new CSVReader(new FileReader(uploadedFile),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
					    }else{
					    	tempuploadedCsvReader = new CSVReader(new FileReader(uploadedFile));
					    }
				
					String[] tempLine;
					boolean fileVerifiedAndUploaded = false;
					boolean isSparedDC = false;
					boolean fileContentSame = false;
					while((tempLine = tempuploadedCsvReader.readNext()) != null){
						logger.info("Consumer Account ID"+tempLine[0]);
						String[] serverFileLine = tempserverCsvReader.readNext();
						
						if(serverFileLine[0].trim().equals("DC Location")){
							serverFileLine = tempserverCsvReader.readNext();
						}
						
						// check if customet id and network id are same
						if(serverFileLine[4].equals(tempLine[4]) && serverFileLine[8].equals(tempLine[8]) && tempLine[6] != null){
							fileContentSame = true;
							
						}else{
							fileContentSame = false;
							if(tempuploadedCsvReader != null){
								tempuploadedCsvReader.close();
								uploadedFile.delete();
							}
							if(tempserverCsvReader != null){
								tempserverCsvReader.close();
							}
							logger.info("Failed to upload File because the file was not proper!");
							redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was not proper!");
							return mav;
						}
						if(tempuploadedCsvReader != null){
							tempuploadedCsvReader.close();
						}
						if(tempserverCsvReader != null){
							tempserverCsvReader.close();
						}
					}
					if(fileContentSame){
						
						CSVReader uploadedCsvReader = null;
						CSVReader serverCsvReader = new CSVReader(new FileReader(serverFile));
						   if(includeHeader){
							   uploadedCsvReader = new CSVReader(new FileReader(uploadedFile),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
						    }else{
						    	uploadedCsvReader = new CSVReader(new FileReader(uploadedFile));
						    }
					
						String[] line;
						while((line = uploadedCsvReader.readNext()) != null){
							logger.info("Consumer Account ID"+line[0]);
							String[] serverFileLine = serverCsvReader.readNext();
							
							if(serverFileLine[0].trim().equals("DC Location")){
								serverFileLine = serverCsvReader.readNext();
							}
							
							// Get Spared DC of Customer and check if exist
							List<DataCollector> DCList = new ArrayList<DataCollector>();
							DCList = commissioningService.getSpareDataCollectorListByCustomer(userService.getInstallerById(Integer.parseInt(installerId)).getCustomer());
							String[] dcSerial = {line[6]};
							if(DCList != null){
								// check if dc exist
								List<DataCollector> dc = null;
								dc = userService.getDCBySerialNumber(dcSerial);
								if(dc.size() != 0){
									for (DataCollector dataCollector : DCList) {
										if(dataCollector.getDcSerialNumber().equals(dc.get(0).getDcSerialNumber())){
											isSparedDC = true;
										}
									}
								}
								if (isSparedDC) {
									// Update necessary detail
									// make file verified and updated
									fileVerifiedAndUploaded = userService.updateSiteInstallationFilesStatus(serverFileName,"DC",Integer.parseInt(installerId));
									
									// update value of configured DC
									if(fileVerifiedAndUploaded){
										DataCollector dcToBeUpdated = dc.get(0);
										//Username
										if(!line[9].trim().equals("")){
											dcToBeUpdated.setDcUserId(line[9]);
										}
										//password
										if(!line[10].trim().equals("")){
											dcToBeUpdated.setDcUserPassword(line[10]);
										}
										
										//lat
										if(!line[2].trim().equals("")){
											dcToBeUpdated.setLatitude(Double.parseDouble(line[2]));
										}
										//longitude
										if(!line[3].trim().equals("")){
											dcToBeUpdated.setLongitude(Double.parseDouble(line[3]));
										}
										//installer
										dcToBeUpdated.setInstaller(userService.getInstallerById(Integer.parseInt(installerId)));
										
										// set update detail
										dcToBeUpdated.setUpdatedBy(loggedUser.getUserId());
										dcToBeUpdated.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
										
										//site
										if(!line[5].trim().equals("")){
											dcToBeUpdated.setSite(userService.getSiteDataBySiteId(line[5]));
										}
										//assest inspection date
										dcToBeUpdated.setAssetInspectionDate(DateTimeConversionUtils.getDateInUTC(new Date()));
										
										/*//installed date
										dcToBeUpdated.setInstalledDate(DateTimeConversionUtils.getDateInUTC(new Date()));
										*/
										
										//Network id
										if(!line[8].trim().equals("")){
											dcToBeUpdated.setNetworkId(Integer.parseInt(line[8]));
										}
										
										/*//Signal strength
										if(!line[11].trim().equals("")){
											dcToBeUpdated.setNetworkId(Integer.parseInt(line[11]));
										}
										//Scgeduled date
										if(!line[12].trim().equals("")){
											dcToBeUpdated.setNetworkId(Integer.parseInt(line[12]));
										}*/
										
										userService.updateDataCollector(dcToBeUpdated);
										Path from = uploadedFile.toPath();
										Path to = serverFile.toPath();
										if(serverCsvReader != null){
											serverCsvReader.close();
											tempserverCsvReader.close();
											serverFile.delete();
										}
										if(uploadedCsvReader != null){
											uploadedCsvReader.close();
										}
										
										Files.copy(from,to,StandardCopyOption.REPLACE_EXISTING);
										redirectAttribute.addFlashAttribute("successMsg","File Uploaded Successfully!");
									}
								}else{
									logger.info("Failed to upload File because the file was not proper!");
									redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was not proper!");
									return mav;
								}
							}
						}
						// Delete uploaded file
						if(uploadedCsvReader != null){
							uploadedCsvReader.close();
							uploadedFile.delete();
						}
						if(serverCsvReader != null){
							serverCsvReader.close();
						}
					}
					
				}else {
					logger.info("Failed to upload File because the file was empty or its not a csv file!");
					redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was empty or its not a csv file!");
					return mav;
				}
			}else{
				logger.info("Failed to upload File because generated file is not exist!");
				redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because generated file is not exist!");
				return mav;
			}
			return mav;
		}else {
			logger.info("Failed to upload File because the file was empty or its not a csv file!");
			redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was empty or its not a csv file!");
			return mav;
		}
	}

	//uploadDownloadEndpointRedirect 
	// TO REDIRECT UPLOAD DOWNLOAD PAGE
	@RequestMapping(value="/uploadDownloadEndpointRedirect",method=RequestMethod.GET)
	public ModelAndView uploadDownloadEndpointRedirect(HttpServletRequest request, HttpServletResponse response){

		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		logger.info("Redirecting to uploadDownloadRedirect for user"+currentUser.getUserId());
		ModelAndView model = new ModelAndView("uploadDownloadEndpointInstaller");
		if(currentUser.getRole().getRoleName().equalsIgnoreCase("installer")){
			Installer ins = userService.getInstallerByUserId(currentUser.getUserId());
			model.addObject("installer",ins);
		}
		return model;
	}

	@RequestMapping(value="/downloadEndpointForInstaller",method=RequestMethod.POST)
	public void downloadEndpointForInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam("installerId") String installerId,@RequestParam(value="includeHeader",required=false) boolean includeHeader) throws IOException{
		logger.info("downloading installation file for Installer "+installerId);
		List<String> fileNames = userService.getInstallationFileName(Integer.parseInt(installerId),"EP"); 
		DownloadFilesUtills.downloadFiles(fileNames,response,servletContext,"EP");
	}


	@RequestMapping(value = "/uploadEndpointForInstaller", method = RequestMethod.POST)
	public @ResponseBody ModelAndView uploadEndpointForInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam("file") MultipartFile file,@RequestParam(value = "includeHeader",required=false)
	boolean includeHeader,@RequestParam("installerId") String installerId,RedirectAttributes redirectAttribute) throws IOException {
		logger.info("uploading file to server for endpoint");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/uploadDownloadEndpointRedirect");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		if(file.getOriginalFilename().toLowerCase().lastIndexOf(".csv")!=-1){
					List<String> fileNames = userService.getInstallationFileName(Integer.parseInt(installerId),"EP"); 
					String uploadedFileName = file.getOriginalFilename();
					logger.info("File uploaded by installer = "+file.getOriginalFilename());
					if(fileNames != null){
						if (!file.isEmpty()) {

						String serverFileName = null;
						//check both file name match
						for(String fileName:fileNames){
							if(uploadedFileName.equals(fileName)){
								serverFileName = fileName;
								break;
							}
						}
						if(serverFileName != null){
							logger.info("File Exist in Server");
							
							//Take Server file
							String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
							String filePath =  resourcesPath+File.separator+ApplicationConstants.EPINSTALLATIONFILEPATH;
							File serverFile = new File(filePath+File.separator+uploadedFileName);
							
							// Take uploaded file
							String tempFilePath =  resourcesPath+File.separator+ApplicationConstants.TEMPFILEPATH+File.separator+"temp_upload_"+installerId+".csv";
							File uploadedFile = new File(tempFilePath);
							boolean tempFileUploaded = uploadedFile.createNewFile();
							
							if(tempFileUploaded){
								// Temporary upload file to server
								FileOutputStream fos = new FileOutputStream(uploadedFile);
								fos.write(file.getBytes());
								fos.close();
							}
							
							// Compare both file
							CSVReader tempuploadedCsvReader = null;
							CSVReader tempserverCsvReader = new CSVReader(new FileReader(serverFile));
							   if(includeHeader){
								   tempuploadedCsvReader = new CSVReader(new FileReader(uploadedFile),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
							    }else{
							    	tempuploadedCsvReader = new CSVReader(new FileReader(uploadedFile));
							    }
						
							
							boolean fileVerifiedAndUploaded = false;
							boolean fileContentSame = false;
							String[] tempLine;
							while((tempLine = tempuploadedCsvReader.readNext()) != null){
								String[] serverFileLine = tempserverCsvReader.readNext();
								
								if(serverFileLine[0].trim().equals("Serial")){
									serverFileLine = tempserverCsvReader.readNext();
								}
								if(tempLine[3].trim().equals("")||tempLine[4].trim().equals("")||
										tempLine[5].trim().equals("")||tempLine[6].trim().equals("")||
										tempLine[7].trim().equals("")||tempLine[26].trim().equals("")||tempLine[2].trim().equals("")||tempLine[15].trim().equals("")){
									if(tempuploadedCsvReader != null){
										tempuploadedCsvReader.close();
										uploadedFile.delete();
									}
									logger.info("Failed to upload File because the file was not proper!");
									redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was not proper!");
									return mav;
								}else{
									// check file to file fields
									if(serverFileLine[3].equals(tempLine[3]) && serverFileLine[4].equals(tempLine[4]) && 
											serverFileLine[5].equals(tempLine[5]) && serverFileLine[6].equals(tempLine[6]) && serverFileLine[7].equals(tempLine[7]) && serverFileLine[15].trim().equals(tempLine[15].trim())){
										String customerId = String.valueOf(userService.getInstallerById(Integer.parseInt(installerId)).getCustomer().getCustomerId());
										if(tempLine[21].trim().equals(customerId)){
											fileContentSame = true;
										}
									}else{
										fileContentSame = false;
										if(tempuploadedCsvReader != null){
											tempuploadedCsvReader.close();
											uploadedFile.delete();
											tempserverCsvReader.close();
										}
										logger.info("Failed to upload File because the file was not proper!");
										redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was not proper!");
										return mav;
									}
									if(tempuploadedCsvReader != null){
										tempuploadedCsvReader.close();
										//uploadedFile.delete();
									}
									if(tempserverCsvReader != null){
										tempserverCsvReader.close();
									}
								}
							}
							if(fileContentSame){
								String[] line;
								CSVReader uploadedCsvReader = null;
								CSVReader serverCsvReader = new CSVReader(new FileReader(serverFile));
								   if(includeHeader){
									   uploadedCsvReader = new CSVReader(new FileReader(uploadedFile),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
								    }else{
								    	uploadedCsvReader = new CSVReader(new FileReader(uploadedFile));
								    }
							
								
								while((line = uploadedCsvReader.readNext()) != null){
									String[] serverFileLine = serverCsvReader.readNext();
									
									if(serverFileLine[0].trim().equals("Serial")){
										serverFileLine = serverCsvReader.readNext();
									}
									
									fileVerifiedAndUploaded = userService.updateSiteInstallationFilesStatus(serverFileName,"EP",Integer.parseInt(installerId));
									List<ConsumerMeter> meterList = null;
									if(fileVerifiedAndUploaded){
											meterList = consumerMeterService.getConsumerMeterByAddress(line[3].trim(), line[4].trim(), 
													line[5].trim(), line[6].trim(), line[7].trim(), userService.getInstallerById(Integer.parseInt(installerId)).getCustomer().getCustomerId(),
													line[2].trim(),Integer.parseInt(line[26].trim()));
										
									}
									if(meterList == null || meterList.size() > 1){
										fileVerifiedAndUploaded = userService.updateSiteInstallationFilesStatus(serverFileName,"EP.false",Integer.parseInt(installerId));
										if(uploadedCsvReader != null){
											uploadedCsvReader.close();
											uploadedFile.delete();
											serverCsvReader.close();
										}
										logger.info("Failed to upload File because the file was not proper!");
										redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was not proper!");
										return mav;
									}else{
										ConsumerMeter meterToBeUpdated = meterList.get(0);
										//Serial no.
										if(!line[0].trim().equals("")){
											meterToBeUpdated.setEndpointSerialNumber(line[0]);
										}
										//RegisterId
										if(!line[1].trim().equals("")){
											meterToBeUpdated.setRegisterId(line[1]);
										}
										
										//lat 
										if(!line[8].trim().equals("")){
											meterToBeUpdated.setLatitude(Double.parseDouble(line[8]));
										}
										//long 
										if(!line[9].trim().equals("")){
											meterToBeUpdated.setLongitude(Double.parseDouble(line[9]));
										}
										//reading 
										if(!line[10].trim().equals("")){
											meterToBeUpdated.setLastMeterReading(Integer.parseInt(line[10]));
										}
										//billing left
										if(!line[12].trim().equals("")){
											meterToBeUpdated.setLeftBillingDigit(Integer.parseInt(line[12]));
										}
										//billing right
										if(!line[13].trim().equals("")){
											meterToBeUpdated.setRightBillingDigit(Integer.parseInt(line[13]));
										}
										//Decimal pos
										if(!line[14].trim().equals("")){
											meterToBeUpdated.setDecimalPosition(Integer.parseInt(line[14]));
										}
										//Leakage
										if(!line[15].trim().equals("")){
											meterToBeUpdated.setLeakageThreshold(Integer.parseInt(line[15]));
										}
										//Backflow
										if(!line[16].trim().equals("")){
											meterToBeUpdated.setBackflowLimit(Integer.parseInt(line[16]));
										}
										//usageThreshoald
										if(!line[17].trim().equals("")){
											meterToBeUpdated.setUsageThreshold(Integer.parseInt(line[17]));
										}
										//usageInterval
										if(!line[18].trim().equals("")){
											meterToBeUpdated.setUsageInterval(Integer.parseInt(line[18]));
										}
										//Kvalue
										if(!line[19].trim().equals("")){
											meterToBeUpdated.setKvalue(Integer.parseInt(line[19]));
										}
										//Direction
										if(!line[20].trim().equals("")){
											meterToBeUpdated.setDirection(Integer.parseInt(line[20]));
										}
										//Repeater nodes
										if(!line[22].trim().equals("")){
											meterToBeUpdated.setRequiredRepeaterNodes(Integer.parseInt(line[22]));
										}
										//repeater level
										if(!line[23].trim().equals("")){
											meterToBeUpdated.setRequireRepeaterLevels(Integer.parseInt(line[23]));
										}
										//is repeater
										if(!line[24].trim().equals("")){
											if(line[24].trim().equals("1")){
												meterToBeUpdated.setRepeater(true);
											}else if(line[24].trim().equals("0")) {
												meterToBeUpdated.setRepeater(false);
											}
										}
										//FW Version
										if(!line[25].trim().equals("")){
											meterToBeUpdated.setFirmwareVersion(line[25].trim());
										}
										
										// assest insp. date
										meterToBeUpdated.setAssetInspectionDate(DateTimeConversionUtils.getDateInUTC(new Date()));
										
										// Meter installed date
										meterToBeUpdated.setEndpointInstalledDate(DateTimeConversionUtils.getDateInUTC(new Date()));
										
										boolean meterUpdated=userService.updateConsumerMeter(meterToBeUpdated);
										if(meterUpdated){
											Path from = uploadedFile.toPath();
											Path to = serverFile.toPath();
											if(serverCsvReader != null){
												serverCsvReader.close();
												tempserverCsvReader.close();
												serverFile.delete();
											}
											if(uploadedCsvReader != null){
												uploadedCsvReader.close();
											}
											
											Files.copy(from,to,StandardCopyOption.REPLACE_EXISTING);
											redirectAttribute.addFlashAttribute("successMsg","File Uploaded Successfully!");
											logger.info("Endpoint Updated Successfully!");
											redirectAttribute.addFlashAttribute("successMsg", "File uploaded successfully");
										}else{
											fileVerifiedAndUploaded = userService.updateSiteInstallationFilesStatus(serverFileName,"EP.false",Integer.parseInt(installerId));
											if(serverCsvReader != null){
												serverCsvReader.close();
												tempserverCsvReader.close();
											}
											if(uploadedCsvReader != null){
												uploadedCsvReader.close();
											}
										}
									}
								}
							}
						}else {
							logger.info("Failed to upload File because the file was empty or its not a csv file!");
							redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was empty or its not a csv file!");
							return mav;
						}
						}
					}else{
						logger.info("Failed to upload File because generated file is not exist!");
						redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because generated file is not exist!");
						return mav;
					}
					return mav;
				}else {
					logger.info("Failed to upload File because the file was empty or its not a csv file!");
					redirectAttribute.addFlashAttribute("errorMsg", "Failed to upload File because the file was empty or its not a csv file!");
					return mav;
				}
	}

	public static boolean createMediaDirectoresIfNotExists(String directoryPath)
	{
		boolean result = false;
		File dir = new File(directoryPath);
		if(!dir.exists())
		{
			try
			{
				logger.info("Creating directory: " + dir);
				result = dir.mkdirs();
			}
			catch(Exception e)
			{
				logger.error("Failed to create directory: " + dir);
				result = false;
			}
		}
		else
		{
			result = true;
		}

		return result;
	}



	@RequestMapping(value="/installerProfileInit",method=RequestMethod.POST)
	public ResponseEntity<String> installerProfileInit(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		Installer installer = userService.getInstallerByUserId(loggedUser.getUserId());

		List<Object> responseList = new ArrayList<Object>();

		if(installer != null){

			User user = userService.getUserDetailsByUserID(installer.getUser().getUserId());

			ContactDetails contactDetails = userService.getDetailsByUserID(installer.getUser().getUserId());
			//Customer customer = userService.getCustomerDetailsByCustomerID(installer.getCustomer().getCustomerId());

			JsonObject jObject = new JsonObject();
			jObject.addProperty("installerId", installer.getInstallerId());
			jObject.addProperty("firstName", contactDetails.getFirstName());
			jObject.addProperty("lastName", contactDetails.getLastname());
			jObject.addProperty("userName", user.getUserName());
			jObject.addProperty("cell_number1", contactDetails.getCell_number1());
			jObject.addProperty("cell_number2", contactDetails.getCell_number2());
			jObject.addProperty("cell_number3", contactDetails.getCell_number3());
			jObject.addProperty("email1", contactDetails.getEmail1());
			jObject.addProperty("email2", contactDetails.getEmail2());
			jObject.addProperty("email3", contactDetails.getEmail3());
			jObject.addProperty("address1", contactDetails.getAddress1());
			jObject.addProperty("address2", contactDetails.getAddress2());
			jObject.addProperty("address3", contactDetails.getAddress3());
			jObject.addProperty("streetName", contactDetails.getStreetName());
			jObject.addProperty("zipcode", contactDetails.getZipcode());

			responseList.add(jObject);

			return 	new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOINSTALLERFOUND),HttpStatus.OK);
	}


	@RequestMapping(value="/updateInstallerProfile", method=RequestMethod.POST)
	public ResponseEntity<String> updateInstallerProfile(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="installerId") String installerId,
			@RequestParam(value="firstName") String firstName,
			@RequestParam(value="lastName") String lastName,
			@RequestParam(value="userName") String userName,
			@RequestParam(value="address1") String address1,
			@RequestParam(required=false ,value="address2") String address2,
			@RequestParam(required=false ,value="address3") String address3,
			@RequestParam(value="streetName") String streetName,
			@RequestParam(value="zipcode") String zipcode,
			@RequestParam(value="cell_number1") String cell_number1,
			@RequestParam(required=false ,value="cell_number2") String cell_number2,
			@RequestParam(required=false ,value="cell_number3") String cell_number3,
			@RequestParam(value="email1") String email1,
			@RequestParam(required=false ,value="email2") String email2,
			@RequestParam(required=false ,value="email3") String email3){

		Installer installer = userService.getInstallerById(Integer.parseInt(installerId));
		
		ContactDetails contactDetails = installer.getUser().getDetails();
		contactDetails.setFirstName(firstName);
		contactDetails.setLastname(lastName);
		contactDetails.setAddress1(address1);
		contactDetails.setAddress2(address2);
		contactDetails.setAddress3(address3);
		contactDetails.setStreetName(streetName);
		contactDetails.setEmail1(email1);
		contactDetails.setEmail2(email2);
		contactDetails.setEmail3(email3);
		contactDetails.setCell_number1(cell_number1);
		contactDetails.setCell_number2(cell_number2);
		contactDetails.setCell_number3(cell_number3);
		contactDetails.setZipcode(zipcode);
		contactDetails.setUpdatedBy(Integer.parseInt(installerId));
		contactDetails.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
		
		userService.updateInstaller(installer);

		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
	}
}