package com.tatva.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tatva.domain.AppointmentMaster;
import com.tatva.model.AppointmentForm;
import com.tatva.model.ServiceTimeModel;
import com.tatva.model.WaitingTimeModel;

public interface IAppointmentMasterService {


	public Boolean registerValidate(AppointmentForm appointmentForm);
	
	/**
	 * make the entry in appintment_master service 
	 * @param appointmentForm
	 * @param referenceGenerator
	 */
	public void registerAppointment(AppointmentForm appointmentForm , String referenceGenerator);
	public void registerAppointment(AppointmentMaster appointmentMaster);
	public List<AppointmentForm> listAppointments(String passport);
	public void cancelAppointment(String refNo);
	public AppointmentForm rescheduleAppointment(String refNo);
	public void rescheduleAppointment(AppointmentForm appointmentForm);
	public List<AppointmentMaster> selectAllAppointments();
	public List<AppointmentMaster> selectAllAppointments(Date date);
	public List<AppointmentMaster> selectcancelledAppointments(Date date);
	public List<AppointmentMaster> selectLateAppointments(Date date) ;
	public List<AppointmentMaster> selectNoShowAppointments(Date date);
	public AppointmentMaster getById(String refNo);
	public Set<String> availableTime(String Date,String appointmentType);
	public List<WaitingTimeModel> getWaitingTime(String date);
	public List<ServiceTimeModel> getServiceTime(String date);
	public List<AppointmentMaster> listAppointments(int offset, int rows);
	public List<AppointmentMaster> getListPage(List<AppointmentMaster> list, int offset, int rows);
	public int getTotalUsers();
	public List<AppointmentMaster> listOrderdAppointment(int offset, int rows, String property, String orderValue);
	public List<AppointmentMaster> searchAppointment(AppointmentMaster appointmentMaster,String appointmentTypeRel, String appointmentDateStringRel, String appointmentStatusRel);
	public List<AppointmentMaster> listSearchOrder(int offset, int rows, String property, String orderValue,AppointmentMaster appointmentMaster,String appointmentTypeRel, String appointmentDateStringRel, String appointmentStatusRel);

}
