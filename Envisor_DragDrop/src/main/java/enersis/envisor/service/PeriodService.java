package enersis.envisor.service;

import java.util.Date;
import java.util.List;

import enersis.envisor.entity.Bill;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Period;
import enersis.envisor.entity.Project;

public interface PeriodService {

	public List<Period> findAll();

	public void save(Period period );

	public void delete(Period period);
	
	public List<Period> findbyDistributionLine(DistributionLine distributionLine);
	
	public List<Period> findByBill(Bill bill);
	
	public List<Period> findByDistLine_Bill(DistributionLine distributionLine,Bill bill);
	
	public Period findByDistLine_Date(DistributionLine distributionLine,Date date);
	
	public Period findByProject_Date(Project project,Date date);
	
	public List<Period> findByProject(Project project);
}
