package com.kenure.dao;

/**
 * 
 * @author TatvaSoft
 *
 */
import java.util.List;

import com.kenure.entity.VPNConfiguration;

public interface IVPNDAO {

	public VPNConfiguration getVPNDetails();
	
	public List<VPNConfiguration> getVPNList();

	public VPNConfiguration getVPNDataById(String configId);

	public void updateVPNDetails(VPNConfiguration vpnConfiguration);
	
}
