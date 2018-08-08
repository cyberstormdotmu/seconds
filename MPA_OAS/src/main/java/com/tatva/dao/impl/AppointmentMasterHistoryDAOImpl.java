package com.tatva.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.IAppointmentMasterHistoryDAO;
import com.tatva.domain.AppointmentMasterHistory;

/**
 * interface {@link IAppointmentMasterHistoryDAO}
 * @author pci94
 *
 */
@Repository
public class AppointmentMasterHistoryDAOImpl implements IAppointmentMasterHistoryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	
	
	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IAppointmentMasterHistoryDAO#listAppointmentHistory()
	 */
	public List<AppointmentMasterHistory> listAppointmentHistory() {
		Criteria criteria=sessionFactory.openSession().createCriteria(AppointmentMasterHistory.class);
		List<AppointmentMasterHistory> appointmentHistoryList = criteria.list();
		return appointmentHistoryList;
	}
}
