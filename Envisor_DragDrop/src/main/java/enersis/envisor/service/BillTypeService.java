package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.BillType;

public interface BillTypeService {
	
	public List<BillType> findAll();

	public void save(BillType billType);

	public void delete(BillType billType);

}
