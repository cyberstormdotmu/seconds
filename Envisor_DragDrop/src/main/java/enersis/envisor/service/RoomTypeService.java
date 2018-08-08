package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.RoomType;

public interface RoomTypeService {

	public List<RoomType> findAll();

	public RoomType findById(int id);
	
	public void save(RoomType roomType);

	public void delete(RoomType roomType);

}
