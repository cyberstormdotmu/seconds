package com.kenure.service;

import com.kenure.entity.NormalCustomer;
import com.kenure.entity.User;

public interface INormalCustomerService {

	String saveNormalCustomer(User nc);

	String saveNormalCustomerByNormal(NormalCustomer nc);

}
