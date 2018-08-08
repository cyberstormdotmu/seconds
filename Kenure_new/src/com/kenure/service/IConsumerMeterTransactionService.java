package com.kenure.service;

import java.util.List;

/**
 * 
 * @author TatvaSoft
 *
 */
public interface IConsumerMeterTransactionService {
	
	public List getNWConsumptionCMTList(String periodTime, String periodType, String duMeterId);

	public List getConsumptionCMTList(String periodTime, String periodType, String baseTypeId, String baseType);
}
