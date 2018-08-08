package com.tatva.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.ITransactionMasterDAO;
import com.tatva.domain.TransactionMaster;

/**
 * {@link ITransactionMasterDAO}
 * @author pci94
 *
 */
@Repository
public class TransactionMasterDAOImpl implements ITransactionMasterDAO {

	@Autowired
	private SessionFactory sessionFactory;

	
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ITransactionMasterDAO#addTransaction(com.tatva.domain.TransactionMaster)
	 */
	public void addTransaction(TransactionMaster transactionMaster) {
		// save the transaction details to the TransactionMaster
		sessionFactory.getCurrentSession().save(transactionMaster);
		
	}

}
