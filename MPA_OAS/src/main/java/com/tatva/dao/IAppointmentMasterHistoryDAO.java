package com.tatva.dao;

import java.util.List;

import com.tatva.domain.AppointmentMasterHistory;
/**
 * 
 * @author pci94
 *
 */
public interface IAppointmentMasterHistoryDAO {

	/**
	 * 
	 * @return list of history of appointments 
	 */
	public List<AppointmentMasterHistory> listAppointmentHistory();
}
