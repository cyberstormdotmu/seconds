package enersis.envisor.service;

import java.util.List;;

public interface MeasurementTypeUtilityTypeService {

	public List<MeasurementTypeUtilityTypeService> findAll();

	public void save(MeasurementTypeUtilityTypeService measurementTypeUtilityType);

	public void delete(MeasurementTypeUtilityTypeService measurementTypeUtilityType);
}
