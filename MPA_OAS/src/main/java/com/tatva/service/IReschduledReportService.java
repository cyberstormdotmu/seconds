package com.tatva.service;

import java.util.List;

import com.tatva.domain.AppointmentMaster;
import com.tatva.domain.AppointmentMasterHistory;
import com.tatva.model.RescheduledReport;

public interface IReschduledReportService {

	public List<RescheduledReport> selectRescheduledAppointments(List<AppointmentMasterHistory> appointmentMasterhistory , List<AppointmentMaster> appointmentMaster);
	
}
