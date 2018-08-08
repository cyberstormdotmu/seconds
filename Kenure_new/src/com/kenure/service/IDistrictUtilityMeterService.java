package com.kenure.service;

import java.util.List;

import com.kenure.entity.Customer;
import com.kenure.entity.DistrictUtilityMeter;

/**
 * 
 * @author TatvaSoft
 *
 */
public interface IDistrictUtilityMeterService {

	DistrictUtilityMeter getDUMeterByDUSerialNo(String duSerialNo);
	
	List<DistrictUtilityMeter> getDUMetersByCustomer(Customer customer);

}
