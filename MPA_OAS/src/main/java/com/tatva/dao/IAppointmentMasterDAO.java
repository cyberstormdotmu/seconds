package com.tatva.dao;

import java.util.Date;
import java.util.List;

import com.tatva.domain.AppointmentMaster;
import com.tatva.domain.GlobalAttribute;
/***
 * 
 * @author pci94
 *
 */
public interface IAppointmentMasterDAO {

	/***
	 * 
	 * @param appointmentMaster
	 */
	public void registerAppointment(AppointmentMaster appointmentMaster);
	
	/***
	 * 
	 * @param passport
	 * @return list of appointments by passport number
	 */
	public List<AppointmentMaster> listAppointmentsByPassport(String passport);
	
	/**
	 * 
	 * @param refNo
	 * cancel the appointment by reference number
	 */
	public void cancelAppointment(String refNo);
	
	/**
	 * 
	 * @param refNo
	 * @return appointment by reference number that client wants to reschedule
	 */
	public AppointmentMaster rescheduleAppointment(String refNo);
	
	/**
	 * 
	 * @param appointmentMaster
	 * 	this will update the appointment by rescheduling it
	 */
	public void rescheduleAppointment(AppointmentMaster appointmentMaster);
	
	/**
	 * 
	 * @return complete list of appointments 
	 */
	public List<AppointmentMaster> listAppointment();
	
	/**
	 * 
	 * @param date
	 * @return list of appointments of particular date
	 */
	public List<AppointmentMaster> listAppointment(Date date);
	
	/**
	 * 
	 * @param date
	 * @return list of appointments that are cancelled on the particular date
	 */
	public List<AppointmentMaster> listCancelledAppointments(Date date);
	
	/**
	 * 
	 * @param date
	 * @return list of no show appointments on selected date 
	 */
	public List<AppointmentMaster> selectNoShowAppointments(Date date);
	
	/**
	 * 
	 * @param date
	 * @return list of late appointments on the selected date
	 */
	public List<AppointmentMaster> selectLateAppointments(Date date);
	
	/**
	 * 
	 * @param refNo
	 * @return Appointment by the reference number
	 */
	public AppointmentMaster getAppointmentMasterByRefNo(String refNo);
	
	/**
	 * 
	 * @param Date
	 * @return list of appointments by date
	 */
	public List<AppointmentMaster> listAppointmentsByDate(String Date);
	
	/**
	 * 
	 * @param date
	 * @return list of appointments by date for getting waiting time report
	 */
	public List<AppointmentMaster> getWaitingTime(String date);
	
	/**
	 * 
	 * @param date
	 * @return list of appointments by date for getting service time report
	 */
	public List<AppointmentMaster> getServiceTime(String date);
	
	/**
	 * 
	 * @return list of global attributes for checking their value.
	 */
	public List<GlobalAttribute> checkAttributes();
	
	/**
	 * 
	 * @param offset
	 * @param rows
	 * @return list of appointments by offset and rows for grid view
	 */
	public List<AppointmentMaster> listAppointments(int offset, int rows);
	
	/**
	 * 
	 * @param offset
	 * @param rows
	 * @param property
	 * @param orderValue
	 * @return list of appointment with pagination and ordering by particular row in grid
	 */
	public List<AppointmentMaster> listOrderdAppointment(int offset, int rows, String property, String orderValue);
	
	
	/**
	 * 
	 * @param appointmentMaster
	 * @param appointmentTypeRel
	 * @param appointmentDateStringRel
	 * @param appointmentStatusRel
	 * @return search list of Appointments
	 */
	public List<AppointmentMaster> searchAppointment(AppointmentMaster appointmentMaster,String appointmentTypeRel, String appointmentDateStringRel, String appointmentStatusRel);
	
	/**
	 * 
	 * @param offset
	 * @param rows
	 * @param property
	 * @param orderValue
	 * @param appointmentMaster
	 * @param appointmentTypeRel
	 * @param appointmentDateStringRel
	 * @param appointmentStatusRel
	 * @return order the search list
	 */
	public List<AppointmentMaster> listSearchOrder(int offset, int rows, String property, String orderValue,AppointmentMaster appointmentMaster,String appointmentTypeRel, String appointmentDateStringRel, String appointmentStatusRel);
}
