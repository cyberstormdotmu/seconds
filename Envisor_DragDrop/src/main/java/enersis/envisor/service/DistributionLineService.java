package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Project;

public interface DistributionLineService {

	public List<DistributionLine> findAll();
	
	public void save(DistributionLine distributionLine);
	
	public void delete(DistributionLine distributionLine);
	
	public List<DistributionLine>  findByProject(Project project);
	
	public List<DistributionLine> findByDistributionLineName(String name);
	
	
	
}
