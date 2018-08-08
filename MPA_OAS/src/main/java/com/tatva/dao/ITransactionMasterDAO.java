package com.tatva.dao;

import com.tatva.domain.TransactionMaster;

/**
 * 
 * @author pci94
 *
 */
public interface ITransactionMasterDAO {

	/**
	 * 
	 * @param transactionMaster
	 * add the transaction details in the TransactionMaster at the time of appointment is registered.
	 */
	public void addTransaction(TransactionMaster transactionMaster);
	
}
