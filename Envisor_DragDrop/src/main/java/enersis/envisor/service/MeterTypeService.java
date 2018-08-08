package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.MeterType;

public interface MeterTypeService {

	public List<MeterType> findAll();

	public void save(MeterType meterType);

	public void delete(MeterType meterType);
}
