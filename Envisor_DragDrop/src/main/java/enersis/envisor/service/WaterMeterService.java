package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.MeterType;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.WaterMeter;

public interface WaterMeterService {

	public List<WaterMeter> findAll();

	public void save(WaterMeter waterMeter);

	public void delete(WaterMeter waterMeter);
	
	public List<WaterMeter> findBySerialNo(String serialNo);
	
	public List<WaterMeter> findByDistributionLine(DistributionLine distributionLine);
	
	public List<WaterMeter> findByFlat(Flat flat);
	
	public List<WaterMeter> findByProject (Project project);
	
	public List<WaterMeter> findByMeterType(MeterType meterType);
	
	
}
