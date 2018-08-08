package com.tatva.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tatva.domain.AppointmentMaster;
import com.tatva.domain.AppointmentMasterHistory;
import com.tatva.model.RescheduledReport;
import com.tatva.service.IReschduledReportService;
import com.tatva.utils.ListUtils;

@Service
public class ReschduledReportServiceImpl implements IReschduledReportService {

	
	List<RescheduledReport> reschduledReport  = new ArrayList<>();
	
	
	@Override
	public List<RescheduledReport> selectRescheduledAppointments(List<AppointmentMasterHistory> appointmentMasterHistoryList ,List<AppointmentMaster> appointmentMasterList){
		//Integer count = 0;
		for(Integer i = 0 ; i<appointmentMasterList.size() ; i ++)
		{
			for(Integer j = 0 ; j < appointmentMasterHistoryList.size() ; j++)
			{
				if(appointmentMasterList.get(i).getReferenceNo().equals(appointmentMasterHistoryList.get(j).getReferenceNo()))
				{
					if(appointmentMasterHistoryList.get(j).getPreviousAppointmentDate() != null){
						RescheduledReport rescheduleReportTemp = new RescheduledReport();
						rescheduleReportTemp.setAppointmentType(appointmentMasterList.get(i).getAppointmentType());
						rescheduleReportTemp.setTransactionType(ListUtils.convertTransactionMasterListToString(appointmentMasterList.get(i).getTransacMaster()));
						rescheduleReportTemp.setTransactionType(ListUtils.convertAppointMentCraftListToString(appointmentMasterList.get(i).getAppointmentCraft()));
						rescheduleReportTemp.setPreviousDate(appointmentMasterHistoryList.get(j).getPreviousAppointmentDate());
						rescheduleReportTemp.setPreviousTime(appointmentMasterHistoryList.get(j).getPreviousAppointmentTime());
						rescheduleReportTemp.setRescheduledDate(appointmentMasterList.get(i).getAppointmentDate());
						rescheduleReportTemp.setRescheduledTime(appointmentMasterList.get(i).getAppointmentTime());
						rescheduleReportTemp.setContactNumber(appointmentMasterList.get(i).getContactNo());
						rescheduleReportTemp.setReferenceNo(appointmentMasterList.get(i).getReferenceNo());
						rescheduleReportTemp.setRemark(appointmentMasterList.get(i).getRemark());
						reschduledReport.add(rescheduleReportTemp);
					}
					
				}
				
				
			}
			
		}
		
		
		return reschduledReport;
	}

}
