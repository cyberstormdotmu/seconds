package com.tatva.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.dao.ITransactionLogDAO;
import com.tatva.domain.TransactionLogForm;
import com.tatva.service.ITransactionLogService;

@Service
public class TransactionLogServiceImpl implements ITransactionLogService {

	
	@Autowired
	private ITransactionLogDAO transactionLogDAO;
	
	@Override
	public void saveTransactionLog(TransactionLogForm transactionLogForm) {
		transactionLogDAO.save(transactionLogForm);
	}
	
	
}
