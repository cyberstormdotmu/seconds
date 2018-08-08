package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.ReadOutMethod;

public interface ReadOutMethodService {
	
	public List<ReadOutMethod> findAll();
	
	public void save(ReadOutMethod  readOutMethod);
	
	public void delete(ReadOutMethod  readOutMethod);
	
	public List<ReadOutMethod>  findBytype(String type);

}
