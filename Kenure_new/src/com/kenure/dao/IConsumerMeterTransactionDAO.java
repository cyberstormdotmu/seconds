package com.kenure.dao;

import java.util.List;

/**
 * 
 * @author TatvaSoft
 *
 */
public interface IConsumerMeterTransactionDAO {
	
	@SuppressWarnings("rawtypes")
	public List getNWConsumptionCMTList(String periodTime, String periodType, String duMeterId);

	@SuppressWarnings("rawtypes")
	public List getConsumptionCMTList(String periodTime, String periodType, String baseTypeId, String baseType);
}
