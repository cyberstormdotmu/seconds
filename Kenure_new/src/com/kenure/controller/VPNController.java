package com.kenure.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.entity.BatteryLife;
import com.kenure.entity.User;
import com.kenure.entity.VPNConfiguration;
import com.kenure.service.IVPNService;
import com.kenure.utils.LoggerUtils;

/**
 * 
 * @author TatvaSoft
 * 
 * Controlls all vpn related request.
 * 
 */
@Controller
public class VPNController {

	private Logger logger = LoggerUtils.getInstance(VPNController.class);

	@Autowired
	private IVPNService vpnService;

	private VPNConfiguration vpnConfig = null;


	@RequestMapping(value="/vpndetails",method=RequestMethod.GET)
	public void getVPNDetails(){
		if((vpnConfig = vpnService.getVPNDetails()) != null){
			logger.info("Printing VPN details >"+vpnConfig.getVpnDomainName());
		}else{
			logger.info("No VPN related details found.");
		}
	}


	@RequestMapping(value = "/portalConfig",method = RequestMethod.GET)
	public ResponseEntity<String> getPortalConfigDetails(HttpServletRequest request, HttpServletResponse response) {

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> vpnDetailsList = new ArrayList<JsonObject>();

		List<VPNConfiguration> vpnList = null;

		if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){
			
			logger.info("Fetching vpn system configuration details.");
			vpnList = vpnService.getVPNList();

			if (vpnList != null && vpnList.size() > 0) {

				Iterator<VPNConfiguration> vpnIterator = vpnList.iterator();

				while(vpnIterator.hasNext()){
					VPNConfiguration vpn = (VPNConfiguration) vpnIterator.next();

					JsonObject jsonObj = new JsonObject();

					jsonObj.addProperty("configId", vpn.getConfigId());
					jsonObj.addProperty("vpnServerName", vpn.getVpnServerName());
					jsonObj.addProperty("vpnUserName", vpn.getVpnUserName());
					jsonObj.addProperty("vpnPassword", vpn.getVpnPassword());
					jsonObj.addProperty("vpnDomainName", vpn.getVpnDomainName());
					jsonObj.addProperty("noOfBytesPerEndpointRead", vpn.getNoOfBytesPerEndpointRead());
					jsonObj.addProperty("noOfBytesPerPacket", vpn.getNoOfBytesPerPacket());

					jsonObj.addProperty("abnormalThreshold", vpn.getAbnormalThreshold());
					
					vpnDetailsList.add(jsonObj);
				}
			}
			objectList.add(vpnDetailsList);
		}

		return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/saveUpdatePortalConfig",method = RequestMethod.POST)
	public ResponseEntity<String> saveUpdatePortalConfig(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="configId") String configId,
			@RequestParam(value="vpnServerName") String vpnServerName,
			@RequestParam(value="vpnUserName") String vpnUserName,
			@RequestParam(value="vpnPassword") String vpnPassword,
			@RequestParam(value="vpnDomainName") String vpnDomainName,
			@RequestParam(value="noOfBytesPerEndpointRead") String noOfBytesPerEndpointRead,
			@RequestParam(value="noOfBytesPerPacket") String noOfBytesPerPacket,
			@RequestParam(value="abnormalThreshold") Double abnormalThreshold) {

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){
			
			VPNConfiguration vpnConfiguration = vpnService.getVPNDataById(configId);
			vpnConfiguration.setVpnServerName(vpnServerName);
			vpnConfiguration.setVpnUserName(vpnUserName);
			vpnConfiguration.setVpnPassword(vpnPassword);
			vpnConfiguration.setVpnDomainName(vpnDomainName);
			vpnConfiguration.setNoOfBytesPerEndpointRead(noOfBytesPerEndpointRead);
			vpnConfiguration.setNoOfBytesPerPacket(noOfBytesPerPacket);
			vpnConfiguration.setAbnormalThreshold(abnormalThreshold);
			
			vpnService.updateVPNDetails(vpnConfiguration);
			return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
		
	}
	
	
}