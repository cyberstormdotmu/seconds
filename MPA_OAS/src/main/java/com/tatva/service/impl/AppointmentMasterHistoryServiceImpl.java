package com.tatva.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.dao.IAppointmentMasterHistoryDAO;
import com.tatva.domain.AppointmentMasterHistory;
import com.tatva.service.IAppointmentMasterHistoryService;

@Service
public class AppointmentMasterHistoryServiceImpl implements IAppointmentMasterHistoryService {

	@Autowired
	public IAppointmentMasterHistoryDAO appointmentMasterHistoryDao;

	
	
	@Override
	public List<AppointmentMasterHistory> selectAllAppointmentsHistory() {
		
		return appointmentMasterHistoryDao.listAppointmentHistory();
	}
}