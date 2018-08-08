package com.tatva.dao;

import com.tatva.domain.TransactionLogForm;

/**
 * 
 * @author pci94
 *
 */
public interface ITransactionLogDAO {

	/**
	 * 
	 * @param transactionLogForm
	 * insert all the transactions in transaction log table  
	 */
	public void save(TransactionLogForm transactionLogForm);

}
