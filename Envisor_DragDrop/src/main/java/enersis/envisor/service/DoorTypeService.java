package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.DoorType;

public interface DoorTypeService {

	public List<DoorType> findAll();

	public DoorType findById(int id);
	
	public DoorType findByType(String type);

	public void save(DoorType doorType);

	public void delete(DoorType doorType);

}
