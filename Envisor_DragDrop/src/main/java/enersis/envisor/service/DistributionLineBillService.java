package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Bill;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBill;
import enersis.envisor.entity.Project;

public interface DistributionLineBillService {
	
	public List<DistributionLineBill> findAll();

	public void save(DistributionLineBill distributionLineBill);

	public void delete(DistributionLineBill distributionLineBill);
	
	public List<DistributionLineBill> findbyDistributionLine(DistributionLine distributionLine);
	
	public List<DistributionLineBill> findByBill(Bill bill);
	
	public List<DistributionLineBill> findByProject(Project project);

}
