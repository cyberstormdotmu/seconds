package com.tatva.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.dao.IGlobalAttributeDAO;
import com.tatva.domain.GlobalAttribute;
import com.tatva.service.IGlobalAttributeService;

@Service
public class GlobalAttributeImpl implements IGlobalAttributeService{

	@Autowired
	private IGlobalAttributeDAO globalAttributesDAO; 
	
	public void insertGlobalAttribute(GlobalAttribute globalAttribute) {
		globalAttributesDAO.save(globalAttribute);
	}

	@Override
	public List<GlobalAttribute> list() {
		List<GlobalAttribute> list= globalAttributesDAO.list();
		return list;
	}

	
}
