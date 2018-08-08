package com.kenure.dao;

import com.kenure.entity.NormalCustomer;
import com.kenure.entity.User;

public interface INormalCustomerDAO {

	String saveNormalCustomer(User nc);

	String saveNormalCustomerByNormal(NormalCustomer nc);

}
