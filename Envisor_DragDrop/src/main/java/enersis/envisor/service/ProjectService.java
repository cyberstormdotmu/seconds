package enersis.envisor.service;

import java.util.List;

import org.primefaces.model.DualListModel;

import enersis.envisor.entity.Project;
import enersis.envisor.entity.Window;

public interface ProjectService {

	public List<Project> findAll();

	public Project findById(short id);	// Added by TatvaSoft, to get Project entity with given ID.
	
	public void save(Project project);

	public void delete(Project project);
	
	public List<Project> findByProjectName(String projectName);
	
	public Project populateBuildings(Project project);	// Added by TatvaSoft, to populate Project entity with list of Buildings.(List of Buildings are lazy loading)
}
