package com.tatva.dao;

import java.util.List;

import com.tatva.domain.AppointmentMaster;

/**
 * 
 * @author pci94
 *
 */
public interface ICheckinDAO {

	/**
	 * 
	 * @param passport
	 * @return list of appointments of a particular passport number for today 
	 */
	public List<AppointmentMaster> getAppointmentMasterByPassport(String passport);
	
	/**
	 * 
	 * @param refNo
	 *  update the appointment at the time of check in
	 */
	public void updateAppointmentMasterByRefNo(String refNo);
}
