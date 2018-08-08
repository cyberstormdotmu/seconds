package com.tatva.service;

import java.util.List;


public interface IAppoitmentCraftService {

	/**
	 * * service method for adding the craft details
	 * @param craftNumbers
	 * @param referenceGenerator
	 */
	public void addCrafts(List<String>craftNumbers , String referenceGenerator);
	
}
