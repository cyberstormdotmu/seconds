package com.kenure.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenure.dao.ITariffPlanDAO;
import com.kenure.entity.Customer;
import com.kenure.entity.TariffPlan;
import com.kenure.entity.TariffTransaction;
import com.kenure.model.TariffModel;

@Service
@Transactional
public class TariffPlanServiceImpl implements ITariffPlanService {

	@Autowired
	private ITariffPlanDAO tariffPlanDAO;

	@Override @Transactional
	public TariffPlan getTariffPlanByCustomerId(int customerId) {
		return null;
	}

	@Override @Transactional
	public TariffPlan getTariffPlanById(int tariffId) {
		return tariffPlanDAO.getTariffPlanById(tariffId);
	}

	@Override @Transactional
	public Object updateCurrentTariff(TariffTransaction tariffTrans) {
		return tariffPlanDAO.updateCurrentTariff(tariffTrans);
	}

	@Override @Transactional
	public void deleteTariffTransaction(TariffPlan tariffPlan,TariffModel tariffArray) {
		tariffPlanDAO.deleteTariffTransaction(tariffPlan,tariffArray);
	}

	@Override @Transactional
	public boolean isNameTariffNameAlreadyExist(Customer customer,
			String tariffName) {
		return tariffPlanDAO.isNameTariffNameAlreadyExist(customer,tariffName);
	}
}