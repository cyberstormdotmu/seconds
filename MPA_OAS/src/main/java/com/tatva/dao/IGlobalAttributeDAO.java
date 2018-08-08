package com.tatva.dao;

import java.util.List;

import com.tatva.domain.GlobalAttribute;

/**
 * 
 * @author pci94
 *
 */
public interface IGlobalAttributeDAO {

	/**
	 * 
	 * @return list of Global Attributes For Which Apply Date is the today
	 */
	public List<GlobalAttribute> checkAttributes();
	
	/**
	 * 
	 * @param globalAttribute
	 * save the changes in Global Attribute
	 */
	public void save(GlobalAttribute globalAttribute);
	
	/**
	 * 
	 * @return list of Global Attributes
	 */
	public List<GlobalAttribute> list();

}
