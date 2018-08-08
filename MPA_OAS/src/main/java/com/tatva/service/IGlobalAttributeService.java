package com.tatva.service;

import java.util.List;

import com.tatva.domain.GlobalAttribute;

public interface IGlobalAttributeService {

	public void insertGlobalAttribute(GlobalAttribute globalAttribute);

	public List<GlobalAttribute> list();

}
