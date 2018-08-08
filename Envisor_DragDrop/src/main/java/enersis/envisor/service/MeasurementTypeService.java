package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.MeasurementType;

public interface MeasurementTypeService {

	public List<MeasurementType> findAll();

	public void save(MeasurementType measurementType);

	public void delete(MeasurementType measurementType);
}
