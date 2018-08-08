package com.tatva.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.IAppointmentCraftDAO;
import com.tatva.domain.AppointmentCraft;

/**
 * 
 * @author pci94
 *
 */
@Repository
public class AppointmentCraftDAOImpl implements IAppointmentCraftDAO {


	@Autowired
	private SessionFactory sessionFactory;

	
	@Override
	/*
	 * @see com.tatva.dao.IAppointmentCraftDAO#addCrafts(com.tatva.domain.AppointmentCraft)
	 */
	public void addCrafts(AppointmentCraft appointmentCraft) {
		// save the crafts 
		sessionFactory.getCurrentSession().save(appointmentCraft);
	}

}
