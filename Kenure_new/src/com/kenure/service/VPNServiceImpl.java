package com.kenure.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenure.dao.IVPNDAO;
import com.kenure.entity.VPNConfiguration;

/**
 * 
 * @author TatvaSoft
 *
 */
@Service
@Transactional
public class VPNServiceImpl implements IVPNService {

	@Autowired
	private IVPNDAO vpnDAO;
	
	@Override @Transactional
	public VPNConfiguration getVPNDetails() {
		return vpnDAO.getVPNDetails();
	}
	
	@Override @Transactional
	public List<VPNConfiguration> getVPNList() {
		return vpnDAO.getVPNList();
	}

	@Override
	public VPNConfiguration getVPNDataById(String configId) {
		return vpnDAO.getVPNDataById(configId);
		
	}

	@Override
	public void updateVPNDetails(VPNConfiguration vpnConfiguration) {
		vpnDAO.updateVPNDetails(vpnConfiguration);
	}
}
