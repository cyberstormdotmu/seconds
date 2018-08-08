package com.kenure.dao;

import com.kenure.entity.Customer;
import com.kenure.entity.TariffPlan;
import com.kenure.entity.TariffTransaction;
import com.kenure.model.TariffModel;

/**
 * 
 * @author TatvaSoft
 *
 */
public interface ITariffPlanDAO {

	public TariffPlan getTariffPlanByCustomerId(int customerId);
	
	public TariffPlan getTariffPlanById(int tariffId);

	public Object updateCurrentTariff(TariffTransaction tariffTrans);

	public void deleteTariffTransaction(TariffPlan tariffPlan,TariffModel tariffArray);

	public boolean isNameTariffNameAlreadyExist(Customer customer,
			String tariffName);
	
}