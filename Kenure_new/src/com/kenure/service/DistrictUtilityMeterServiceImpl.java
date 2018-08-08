package com.kenure.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenure.dao.IDistrictUtilityMeterDAO;
import com.kenure.entity.Customer;
import com.kenure.entity.DistrictUtilityMeter;

@Service
public class DistrictUtilityMeterServiceImpl implements IDistrictUtilityMeterService{

	@Autowired
	private IDistrictUtilityMeterDAO duDAO;
	
	@Override
	@Transactional
	public DistrictUtilityMeter getDUMeterByDUSerialNo(String duSerialNo) {
		return duDAO.getDUMeterByDUSerialNo(duSerialNo);
	}

	@Override
	@Transactional
	public List<DistrictUtilityMeter> getDUMetersByCustomer(Customer customer) {
		return duDAO.getDUMetersByCustomer(customer);
	}

}
