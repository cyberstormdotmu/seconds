package com.kenure.service;

import java.util.List;

import com.kenure.entity.VPNConfiguration;

/**
 * 
 * @author TatvaSoft
 *
 */
public interface IVPNService {

	public VPNConfiguration getVPNDetails();
	
	public List<VPNConfiguration> getVPNList();

	public VPNConfiguration getVPNDataById(String configId);
	
	public void updateVPNDetails(VPNConfiguration vpnConfiguration);
	
}
