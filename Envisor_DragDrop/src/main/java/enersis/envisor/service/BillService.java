package enersis.envisor.service;

import java.util.Date;
import java.util.List;

import enersis.envisor.entity.Bill;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Period;

public interface BillService {

	public List<Bill> findAll();

	public void save(Bill bill );

	public void delete(Bill bill);
	
	public List<Bill> findbyDistributionLine(DistributionLine distributionLine);
	
	public List<Bill> findByFileName(String fileName);
	
	public List<Bill> findByDate(Date date);
	
	public List<Bill> findUnbindedBills();
	
	public List<Bill> findByPeriod(Period period);
	
//	public List<Bill> findByChargeBetween(Integer start, Integer finish);
}
