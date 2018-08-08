package com.kenure.dao;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.entity.Customer;
import com.kenure.entity.TariffPlan;
import com.kenure.entity.TariffTransaction;
import com.kenure.model.TariffModel;
import com.kenure.model.TariffTransactionModel;

/**
 * 
 * @author TatvaSoft
 *
 */
@Repository
public class TariffPlanDAOImpl implements ITariffPlanDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public TariffPlan getTariffPlanByCustomerId(int customerId) {
		return null;
	}

	@Override
	public TariffPlan getTariffPlanById(int tariffId) {
		Query contactQuery = sessionFactory.getCurrentSession().createQuery("from TariffPlan where tariffPlanId = :tariffId");
		contactQuery.setParameter("tariffId", tariffId);
		Hibernate.initialize(((TariffPlan) contactQuery.list().get(0)).getTariffTransaction());
		return (TariffPlan) contactQuery.list().get(0);
	}

	@Override
	public Object updateCurrentTariff(TariffTransaction tariffTrans) {
		return null;
	}

	@Override
	public void deleteTariffTransaction(TariffPlan tariffPlan,TariffModel tariffArray) {
		Session session = sessionFactory.getCurrentSession();
		Hibernate.initialize(tariffPlan.getTariffTransaction());

		tariffPlan.getTariffTransaction().forEach(n ->{
			session.delete(n);
		});

		TariffTransactionModel x ;
		for(int i=0;i<tariffArray.getTariffArray().size();i++){
			TariffTransaction tariffTrans = new TariffTransaction();
			x = tariffArray.getTariffArray().get(i);
			tariffTrans.setEndBand(x.getEnd());
			tariffTrans.setStartBand(x.getStart());
			tariffTrans.setRate(x.getCost().doubleValue());
			tariffTrans.setTariffPlan(tariffPlan);
			session.save(tariffTrans);
		}
	}

	@Override
	public boolean isNameTariffNameAlreadyExist(Customer customer,
			String tariffPlanName) {

		Query hqlQuery = sessionFactory.getCurrentSession().createQuery("from TariffPlan where customer = :customer and tariffPlanName = :tariffPlanName");
		hqlQuery.setParameter("customer", customer);
		hqlQuery.setParameter("tariffPlanName", tariffPlanName);

		if(hqlQuery.list().size() > 0)
			return true;
		return false;
		
	}

}