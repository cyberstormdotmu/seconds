package com.tatva.service;

import com.tatva.model.AppointmentForm;



public interface ITransactionMasterService {

	public void addTransactions(AppointmentForm appointmentform , String referenceGenerator);
	
}
