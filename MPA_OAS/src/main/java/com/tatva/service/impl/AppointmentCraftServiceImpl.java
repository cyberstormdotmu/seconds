package com.tatva.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.dao.IAppointmentCraftDAO;
import com.tatva.domain.AppointmentCraft;
import com.tatva.domain.composite.CompositeCraft;
import com.tatva.service.IAppoitmentCraftService;


@Service
public class AppointmentCraftServiceImpl implements IAppoitmentCraftService {

	@Autowired
	private IAppointmentCraftDAO appointmentCraftDao;



	/*
	 *  
	 * @see com.tatva.service.IAppoitmentCraftService#addCrafts(java.util.List, java.lang.String)
	 */
	@Override
	public void addCrafts(List<String>craftNumbers,String referenceGenerator) 
	{
		for(Integer i = 0 ; i < craftNumbers.size() ; i++)
		{
			AppointmentCraft appointmentCraft = new AppointmentCraft();
			CompositeCraft craftId = new CompositeCraft();
			craftId.setReferenceNo(referenceGenerator);
			craftId.setCraftNo(craftNumbers.get(i));
			appointmentCraft.setCraftId(craftId);
			appointmentCraftDao.addCrafts(appointmentCraft);		//make entry in craft tables
		}
	}
}
