package com.kenure.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenure.dao.INormalCustomerDAO;
import com.kenure.entity.NormalCustomer;
import com.kenure.entity.User;

@Service
public class NormalCustomerServiceImpl implements INormalCustomerService {

	@Autowired
	private INormalCustomerDAO normalCustomerDAO;
	
	@Override
	@Transactional
	public String saveNormalCustomer(User nc) {
		return normalCustomerDAO.saveNormalCustomer(nc);
	}

	@Override
	@Transactional
	public String saveNormalCustomerByNormal(NormalCustomer nc) {
		return normalCustomerDAO.saveNormalCustomerByNormal(nc);
	}

}
