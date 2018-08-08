package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.WindowType;


public interface WindowTypeService {

	public List<WindowType> findAll();

	public WindowType findById(int id);
	
	public WindowType findByType(String type);

	public void save(WindowType windowType);

	public void delete(WindowType windowType);

}
