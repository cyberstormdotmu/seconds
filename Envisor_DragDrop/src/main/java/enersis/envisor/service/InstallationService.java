package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Installation;
import enersis.envisor.entity.Room;

public interface InstallationService {

	public List<Installation> findAll();

	public void save(Installation installation);

	public void delete(Installation installation);
	
	public List<Installation> findbyFlat(Flat flat);
}
