package com.tatva.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.IAppointmentMasterDAO;
import com.tatva.domain.AppointmentMaster;
import com.tatva.domain.AppointmentMasterHistory;
import com.tatva.domain.GlobalAttribute;
import com.tatva.utils.DaoUtils;
import com.tatva.utils.DateUtil;
import com.tatva.utils.MPAConstants;

/**
 * interface {@link IAppointmentMasterDAO}
 * @author pci94
 *
 */
@Repository
public class AppointmentmasterDAOImpl implements IAppointmentMasterDAO {

	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#registerAppointment(com.tatva.domain.AppointmentMaster)
	 */
	public void registerAppointment(AppointmentMaster appointmentMaster) {
		
		sessionFactory.getCurrentSession().saveOrUpdate(appointmentMaster);
	}

	@SuppressWarnings("unchecked")
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#listAppointmentsByPassport(java.lang.String)
	 */
	public List<AppointmentMaster> listAppointmentsByPassport(String passport) {
		
		// retrieve the list of appointments by passport number
		
		return sessionFactory.getCurrentSession().createQuery("from AppointmentMaster as a where a.idNo= :passport and a.appointmentStatus = :status")
												 .setParameter("passport", passport)
												 .setParameter("status", "pending")
												 .list();
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#cancelAppointment(java.lang.String)
	 */
	public void cancelAppointment(String refNo) {
		//delete the appointment by reference number if it exists
		AppointmentMaster appointmentMaster=(AppointmentMaster) sessionFactory.getCurrentSession().get(AppointmentMaster.class, refNo);
		
		if(appointmentMaster!=null){
			
			AppointmentMasterHistory appointmentMasterHistory=new AppointmentMasterHistory();
			
			appointmentMasterHistory.setReferenceNo(appointmentMaster.getReferenceNo());
			appointmentMasterHistory.setIdNo(appointmentMaster.getIdNo());
			appointmentMasterHistory.setIdType(appointmentMaster.getIdType());
			appointmentMasterHistory.setAppointmentDate(appointmentMaster.getAppointmentDate());
			appointmentMasterHistory.setAppointmentTime(appointmentMaster.getAppointmentTime());
			appointmentMasterHistory.setAppointmentType(appointmentMaster.getAppointmentType());
			appointmentMasterHistory.setContactNo(appointmentMaster.getContactNo());
			appointmentMasterHistory.setEmail(appointmentMaster.getEmail());
			appointmentMasterHistory.setCompany(appointmentMaster.getCompany());
			appointmentMasterHistory.setRemark(appointmentMaster.getRemark());
			appointmentMasterHistory.setAppointmentStatus(MPAConstants.CANCEL_STATUS);
			appointmentMasterHistory.setCheckInDate(appointmentMaster.getCheckInDate());
			appointmentMasterHistory.setCheckInTime(appointmentMaster.getCheckInTime());
			appointmentMasterHistory.setProcessDate(appointmentMaster.getProcessDate());
			appointmentMasterHistory.setProcessTime(appointmentMaster.getProcessTime());
			appointmentMasterHistory.setName(appointmentMaster.getName());
			appointmentMasterHistory.setPreviousAppointmentDate(appointmentMaster.getAppointmentDate());
			appointmentMasterHistory.setPreviousAppointmentTime(appointmentMaster.getAppointmentTime());
			
			sessionFactory.getCurrentSession().save(appointmentMasterHistory);
			
			sessionFactory.getCurrentSession().delete(appointmentMaster);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#rescheduleAppointment(java.lang.String)
	 */
	public AppointmentMaster rescheduleAppointment(String refNo) {
		//return the Appointment by reference number
		AppointmentMaster appointmentMaster=(AppointmentMaster) sessionFactory.getCurrentSession().get(AppointmentMaster.class, refNo);
		return appointmentMaster;
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#rescheduleAppointment(com.tatva.domain.AppointmentMaster)
	 */
	public void rescheduleAppointment(AppointmentMaster appointmentMaster) {
		// Reschedule the appointment if exists
		AppointmentMaster appMaster=(AppointmentMaster)sessionFactory.getCurrentSession().get(AppointmentMaster.class, appointmentMaster.getReferenceNo());
		if(appMaster!=null){
			
			AppointmentMasterHistory appointmentMasterHistory=new AppointmentMasterHistory();
			
			appointmentMasterHistory.setReferenceNo(appMaster.getReferenceNo());
			appointmentMasterHistory.setIdNo(appMaster.getIdNo());
			appointmentMasterHistory.setIdType(appMaster.getIdType());
			appointmentMasterHistory.setAppointmentDate(appointmentMaster.getAppointmentDate());
			appointmentMasterHistory.setAppointmentTime(appointmentMaster.getAppointmentTime());
			appointmentMasterHistory.setAppointmentType(appMaster.getAppointmentType());
			appointmentMasterHistory.setContactNo(appMaster.getContactNo());
			appointmentMasterHistory.setEmail(appMaster.getEmail());
			appointmentMasterHistory.setCompany(appMaster.getCompany());
			appointmentMasterHistory.setRemark(appMaster.getRemark());
			appointmentMasterHistory.setAppointmentStatus(MPAConstants.RESCHEDULED);
			appointmentMasterHistory.setCheckInDate(appMaster.getCheckInDate());
			appointmentMasterHistory.setCheckInTime(appMaster.getCheckInTime());
			appointmentMasterHistory.setProcessDate(appMaster.getProcessDate());
			appointmentMasterHistory.setProcessTime(appMaster.getProcessTime());
			appointmentMasterHistory.setName(appMaster.getName());
			appointmentMasterHistory.setPreviousAppointmentDate(appMaster.getAppointmentDate());
			appointmentMasterHistory.setPreviousAppointmentTime(appMaster.getAppointmentTime());
			
			sessionFactory.getCurrentSession().save(appointmentMasterHistory);
			
			appMaster.setAppointmentDate(appointmentMaster.getAppointmentDate());
			appMaster.setAppointmentTime(appointmentMaster.getAppointmentTime());
			sessionFactory.getCurrentSession().saveOrUpdate(appMaster);
		}
	}

	
	
	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#listAppointment()
	 */
	public List<AppointmentMaster> listAppointment() {
		// retrieve the list of appointment
		

		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    
	    Date dateWithoutTime = cal.getTime();
	    
		Criteria criteria=sessionFactory.openSession().createCriteria(AppointmentMaster.class);
		criteria.add(Restrictions.isNotNull("appointmentStatus"));
		criteria.add(Restrictions.eq("appointmentDate",dateWithoutTime));
		List<AppointmentMaster> appointmentList = criteria.list();
		return appointmentList;
	}

	
	
	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#listAppointment(java.util.Date)
	 */
	public List<AppointmentMaster> listAppointment(Date date) {
		
		// retrieve the list of appointments of the selected date.
		try {		
			Session session= sessionFactory.openSession();
			Query query = session.createQuery("from AppointmentMaster where appointmentDate = :appointmentDate");
			query.setParameter("appointmentDate", date);
			List<AppointmentMaster>  appointmentScheduledList= query.list();
			return appointmentScheduledList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
	 }	}
	

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#listCancelledAppointments(java.util.Date)
	 */
	public List<AppointmentMaster> listCancelledAppointments(Date date) {
		// retrieve list of appointments that are cancelled on the particular date
		try {		
			Session session= sessionFactory.openSession();
			Query query = session.createQuery("from AppointmentMaster where lastAction = :lastAction AND appointmentDate = :appointmentDate");
			query.setParameter("lastAction", MPAConstants.DELETED);
			query.setParameter("appointmentDate", date);
			
			List<AppointmentMaster>  appointmentCancelledList= query.list();
			return appointmentCancelledList;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
	 }
	}

	
	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#getAppointmentMasterByRefNo(java.lang.String)
	 */
	public AppointmentMaster getAppointmentMasterByRefNo(String referenceNo) {
		
		// retrieve Appointment by the reference number
		AppointmentMaster appointmentMaster=null;
		try {		
			Session session= sessionFactory.getCurrentSession();
			Query query = session.createQuery("from AppointmentMaster where referenceNo = :referenceNo");
			query.setParameter("referenceNo", referenceNo);
			List<AppointmentMaster>  userMasterList= query.list();
			appointmentMaster=userMasterList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appointmentMaster;
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#listAppointmentsByDate(java.lang.String)
	 */
	public List<AppointmentMaster> listAppointmentsByDate(String Date ) {
		// retrieve list of appointments by date
		java.util.Date d=DateUtil.convertDateFromStringtoDate(Date);
		java.sql.Date d1=new java.sql.Date(d.getTime());
		String a1=d1.toString();
		Session session = sessionFactory.getCurrentSession();
		
		return (List<AppointmentMaster>) session.createQuery("from AppointmentMaster a where a.appointmentDate = '"+a1+"' and a.appointmentStatus = '"+MPAConstants.CANCEL_STATUS+"' ").list();
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#checkAttributes()
	 */
	public List<GlobalAttribute> checkAttributes() {
		// retrieve list of global attributes for checking their value.
		return sessionFactory.openSession().createQuery("from GlobalAttribute").list();
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#selectNoShowAppointments(java.util.Date)
	 */
	public List<AppointmentMaster> selectNoShowAppointments(Date date) {
		// retrieve list of no show appointments on selected date 
		try {		
			Session session= sessionFactory.openSession();
			Query query = session.createQuery("from AppointmentMaster where appointmentStatus = :appointmentStatus AND appointmentDate = :appointmentDate");
			query.setParameter("appointmentStatus", MPAConstants.NOSHOW);
			query.setParameter("appointmentDate", date);			
			List<AppointmentMaster>  appointmentNoShowList = query.list();
			return appointmentNoShowList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
	 }
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#selectLateAppointments(java.util.Date)
	 */
	public List<AppointmentMaster> selectLateAppointments(Date date) {
		// retrieve list of late appointments on the selected date
		try {
			Criteria criteria=sessionFactory.openSession().createCriteria(AppointmentMaster.class);
			List<AppointmentMaster> appointmentList = criteria.list();
			List<AppointmentMaster> lateappointmentList = new ArrayList<AppointmentMaster>();
			/*
			 * check whether the appointment is checked in late or not
			 * late checked in appointment will be added to the  lateappointmentList list
			 */
			for (AppointmentMaster appointmentMaster : appointmentList) {
				
				if(appointmentMaster.getAppointmentDate().equals(date)){
				if(appointmentMaster.getCheckInDate() != null){
				if(appointmentMaster.getCheckInDate().compareTo(appointmentMaster.getAppointmentDate()) >0){
						lateappointmentList.add(appointmentMaster);
				}
				else if(appointmentMaster.getCheckInDate().compareTo(appointmentMaster.getAppointmentDate()) == 0){

					if(appointmentMaster.getCheckInTime().compareTo(appointmentMaster.getAppointmentTime()) > 0){

						lateappointmentList.add(appointmentMaster);
					}
				}
				}
			}
				}
			return lateappointmentList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#getWaitingTime(java.lang.String)
	 */
	public List<AppointmentMaster> getWaitingTime(String date) {
		// retrieve list of appointments by date for getting waiting time report
		return sessionFactory.getCurrentSession().createQuery("from AppointmentMaster where appointmentDate='"+date+"'").list();
		
	}
	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#getServiceTime(java.lang.String)
	 */
	public List<AppointmentMaster> getServiceTime(String date) {
		// retrieve list of appointments by date for getting service time report
		return sessionFactory.getCurrentSession().createQuery("from AppointmentMaster where appointmentDate='"+date+"'").list();
		
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#listAppointments(int, int)
	 */
	public List<AppointmentMaster> listAppointments(int offset, int rows) {
		/*
		 *  retrieve the list of appointments that starts with value of parameter offset 
		 *  number of records will be the value of rows parameter.
		 */
		try
		{
			Session session = sessionFactory.openSession();
			
			
			Calendar cal = Calendar.getInstance();
		    cal.set(Calendar.HOUR_OF_DAY, 0);
		    cal.set(Calendar.MINUTE, 0);
		    cal.set(Calendar.SECOND, 0);
		    cal.set(Calendar.MILLISECOND, 0);
		    
		    Date dateWithoutTime = cal.getTime();
			
			Criteria crit = session.createCriteria(AppointmentMaster.class);
			crit.setFirstResult(offset);
			crit.setMaxResults(rows);
			crit.add(Restrictions.isNotNull("appointmentStatus"));
			crit.add(Restrictions.eq("appointmentDate",dateWithoutTime));
			
			return crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#listOrderdAppointment(int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<AppointmentMaster> listOrderdAppointment(int offset, int rows,
			String property, String orderValue) {
		/*
		 * retrieve the list of appointments in order.
		 */
		try
		{
			Session session = sessionFactory.openSession();
			
			
			Calendar cal = Calendar.getInstance();
		    cal.set(Calendar.HOUR_OF_DAY, 0);
		    cal.set(Calendar.MINUTE, 0);
		    cal.set(Calendar.SECOND, 0);
		    cal.set(Calendar.MILLISECOND, 0);
		    
		    Date dateWithoutTime = cal.getTime();
			
			Criteria crit = session.createCriteria(AppointmentMaster.class);
			crit.setFirstResult(offset);
			crit.setMaxResults(rows);
			crit.add(Restrictions.ne("appointmentStatus", null));
			crit.add(Restrictions.eq("appointmentDate",dateWithoutTime));
			
			/*
			 * if the property is not empty then it will check the order value for that property
			 * 	if order value is ascending then sorting will be done in descending order and vice versa
			 * 
			 *  if property is empty then sorting will be done in ascending order
			 */
			if(StringUtils.isNotEmpty(property))
			{
				if(StringUtils.isEmpty(orderValue) || StringUtils.equals(orderValue, "ASC"))
				{
					crit.addOrder(Order.asc(property));
				}
				else if(StringUtils.equals(orderValue, "DSC"))
				{
					crit.addOrder(Order.desc(property));
				}
			}
			return crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#searchAppointment(com.tatva.domain.AppointmentMaster, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<AppointmentMaster> searchAppointment(AppointmentMaster appointmentMaster,
													String appointmentTypeRel,String appointmentDateStringRel,
													String appointmentStatusRel) {
		
		Session session=sessionFactory.openSession();
		
		/*
		 * get the property value by which user wants to search
		 */
		String appointmentType=appointmentMaster.getAppointmentType();
		String appointmentDateString=appointmentMaster.getAppointmentDateString();
		String appointmentStatus=appointmentMaster.getAppointmentStatus();
		Date date=DateUtil.convertDateFromStringtoDate(appointmentDateString);
		
		/*
		 *  Criteria for search by property
		 */
		Criteria criteria=session.createCriteria(AppointmentMaster.class);
		
		if(StringUtils.isNotEmpty(appointmentType)){
			DaoUtils.createCriteria("appointmentType", appointmentTypeRel, appointmentType, criteria);
		}
		
		if(StringUtils.isNotEmpty(appointmentStatus)){
			DaoUtils.createCriteria("appointmentStatus", appointmentStatusRel, appointmentStatus, criteria);
		}
		
		if(StringUtils.isNotEmpty(appointmentDateString)){
			DaoUtils.createCriteria("appointmentDate", appointmentDateStringRel, date, criteria);
		}
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterDAO#listSearchOrder(int, int, java.lang.String, java.lang.String, com.tatva.domain.AppointmentMaster, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<AppointmentMaster> listSearchOrder(int offset, int rows,
													String property, String orderValue,
													AppointmentMaster appointmentMaster, String appointmentTypeRel,
													String appointmentDateStringRel,String appointmentStatusRel) {
		try
		{
			Session session = sessionFactory.openSession();
			
			/*
			 * get the property value by which user wants to search
			 */
			String appointmentType=appointmentMaster.getAppointmentType();
			Date appointmentDate=appointmentMaster.getAppointmentDate();
			String appointmentStatus=appointmentMaster.getAppointmentStatus();
			

			/*
			 *  Criteria for search by property 
			 */
			Criteria crit = session.createCriteria(AppointmentMaster.class);
			crit.setFirstResult(offset);
			crit.setMaxResults(rows);
			
			if(StringUtils.isNotEmpty(appointmentType)){
				DaoUtils.createCriteria("appointmentType", appointmentTypeRel, appointmentType, crit);
			}
			
			if(appointmentDate!=null){
				DaoUtils.createCriteria("appointmentDate", appointmentDateStringRel, appointmentDate, crit);
			}
			
			if(StringUtils.isNotEmpty(appointmentStatus)){
				DaoUtils.createCriteria("appointmentStatus", appointmentStatusRel, appointmentStatus, crit);
			}
			
			
			/*
			 * if the property is not empty then it will check the order value for that property
			 * 	if order value is ascending then sorting will be done in descending order and vice versa
			 * 
			 *  if property is empty then sorting will be done in ascending order 
			 */
			if(StringUtils.isNotEmpty(property))
			{
				if(StringUtils.isEmpty(orderValue) || StringUtils.equals(orderValue, "ASC"))
				{
					crit.addOrder(Order.asc(property));
				}
				else if(StringUtils.equals(orderValue, "DSC"))
				{
					crit.addOrder(Order.desc(property));
				}
			}
			return crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}