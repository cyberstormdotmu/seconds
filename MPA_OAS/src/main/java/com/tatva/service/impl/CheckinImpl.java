package com.tatva.service.impl;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.dao.ICheckinDAO;
import com.tatva.domain.AppointmentMaster;
import com.tatva.service.ICheckinService;

@Service
public class CheckinImpl implements ICheckinService {

	@Autowired
	private ICheckinDAO checkinDAO;
	
	@SuppressWarnings({ "unused", "deprecation" })
	@Override
	public boolean checkin(String passport) {
		
		boolean flag=false;
		List<AppointmentMaster> list=checkinDAO.getAppointmentMasterByPassport(passport);
		if(list!=null && list.size()>0){
			
			for(int i=0;i<list.size();i++){
			
				Time temp=list.get(i).getAppointmentTime();
				int hour=temp.getHours();
				int currentHour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				int diffHour=hour-currentHour;
				int minute=temp.getMinutes();
				int currentMinute=Calendar.getInstance().get(Calendar.MINUTE);
				int diffMinute=minute-currentMinute;
				if(diffHour==0){
					
					if(diffMinute>15 || diffMinute<0){
					}
					else{
						checkinDAO.updateAppointmentMasterByRefNo(list.get(i).getReferenceNo());
						flag=true;
					}
				}
				else if(diffHour==1){
					if(diffMinute>15 || diffMinute<0){
						checkinDAO.updateAppointmentMasterByRefNo(list.get(i).getReferenceNo());
						flag=true;
					}
				}
			}
		}
		return flag;
	}


}
