package com.kenure.dao;

import java.util.List;

import com.kenure.entity.Customer;
import com.kenure.entity.DistrictUtilityMeter;
/**
 * 
 * @author TatvaSoft
 *
 */
public interface IDistrictUtilityMeterDAO {

	DistrictUtilityMeter getDUMeterByDUSerialNo(String duSerialNo);

	List<DistrictUtilityMeter> getDUMetersByCustomer(Customer customer);

}
