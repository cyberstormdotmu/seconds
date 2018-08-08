package com.tatva.dao.impl;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.ICheckinDAO;
import com.tatva.domain.AppointmentMaster;
import com.tatva.utils.DateUtil;
import com.tatva.utils.MPAConstants;

/**
 * {@link ICheckinDAO}
 * @author pci94
 *
 */
@Repository
public class CheckinDaoImpl implements ICheckinDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ICheckinDAO#getAppointmentMasterByPassport(java.lang.String)
	 */
	public List<AppointmentMaster> getAppointmentMasterByPassport(String passport) {
		
		/*
		 * retrieve list of appointments by passport number of the same day 
		 */
		return sessionFactory.getCurrentSession().createQuery("from AppointmentMaster where idNo='"+passport+"' AND appointmentDate='"+DateUtil.convertInDashedFormat(new Date())+"'").list();
	}

	@SuppressWarnings("rawtypes")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ICheckinDAO#updateAppointmentMasterByRefNo(java.lang.String)
	 */
	public void updateAppointmentMasterByRefNo(String refNo) {
		
		//get the appointment by reference number
		AppointmentMaster appointmentMaster=(AppointmentMaster)sessionFactory.getCurrentSession().get(AppointmentMaster.class, refNo);
		
		Date d=new Date();
		java.sql.Date dd = new java.sql.Date(d.getTime());
		Time t = new Time(dd.getTime());
		
		/*
		 * generate the queue number
		 * queue number is the combination of timeStamp and its count for the check in process on that day
		 */
		
		String timeStamp=Calendar.getInstance().get(Calendar.YEAR)+""+(Calendar.getInstance().get(Calendar.MONTH)+1)+""+Calendar.getInstance().get(Calendar.DATE);
		
		//list of appointments that have checked in on that day
		List list=sessionFactory.getCurrentSession().createQuery("from AppointmentMaster where checkInDate='"+DateUtil.convertInDashedFormat(new Date())+"'").list();
		
		if(list!=null){
			timeStamp+=(list.size()+1);
		}
		
		/*
		 * set the check in time,check in date and queue number
		 *	update the appointment
		 */
		appointmentMaster.setCheckInTime(t);
		appointmentMaster.setCheckInDate(new Date());
		appointmentMaster.setQueueNo(timeStamp);
		appointmentMaster.setAppointmentStatus(MPAConstants.APPOINTMENT_CHECKED_IN);
		sessionFactory.getCurrentSession().saveOrUpdate(appointmentMaster);
	}

	
}
