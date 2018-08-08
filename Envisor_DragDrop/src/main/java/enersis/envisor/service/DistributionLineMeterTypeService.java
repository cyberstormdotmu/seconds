package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineMeterType;
import enersis.envisor.entity.Project;
public interface DistributionLineMeterTypeService {

	public List<DistributionLineMeterType> findAll();

	public void save(DistributionLineMeterType distributionLineMeterType);

	public void delete(DistributionLineMeterType distributionLineMeterType);
	
	public List<DistributionLineMeterType> findbyDistributionLine(DistributionLine distributionLine);
	
	public List<DistributionLineMeterType> findByProject(Project project);
}
