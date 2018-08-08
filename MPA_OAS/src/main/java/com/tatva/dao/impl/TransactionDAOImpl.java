package com.tatva.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.ITransactionLogDAO;
import com.tatva.domain.TransactionLogForm;

/**
 * {@link ITransactionLogDAO}
 * @author pci94
 *
 */
@Repository
public class TransactionDAOImpl implements ITransactionLogDAO {

	@Autowired
    private SessionFactory sessionFactory;

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ITransactionLogDAO#save(com.tatva.domain.TransactionLogForm)
	 */
	public void save(TransactionLogForm transactionLogForm) {
		
		//save all the transaction logs 
		sessionFactory.getCurrentSession().saveOrUpdate(transactionLogForm);
	}

	
}
