package com.kenure.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenure.dao.IConsumerMeterTransactionDAO;

@Service
public class ConsumerMeterTransactionServiceImpl implements IConsumerMeterTransactionService {
	
	@Autowired
	private IConsumerMeterTransactionDAO cmtDAO;

	@SuppressWarnings("rawtypes")
	@Override @Transactional
	public List getNWConsumptionCMTList(String periodTime, String periodType, String duMeterId) {
		return cmtDAO.getNWConsumptionCMTList(periodTime, periodType, duMeterId);
	}

	@SuppressWarnings("rawtypes")
	@Override @Transactional
	public List getConsumptionCMTList(String periodTime, String periodType, String baseTypeId, String baseType) {
		return cmtDAO.getConsumptionCMTList(periodTime, periodType, baseTypeId, baseType);
		
	}

}
