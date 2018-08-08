package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.UtilityType;

public interface UtilityTypeService {

	public List<UtilityType> findAll();

	public void save(UtilityType utilityType);

	public void delete(UtilityType utilityType);
}
