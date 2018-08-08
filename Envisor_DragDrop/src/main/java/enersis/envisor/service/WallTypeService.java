package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.WallType;

public interface WallTypeService {

	public List<WallType> findAll();

	public WallType findById(int id);

	public void save(WallType wallType);

	public void delete(WallType wallType);

}
