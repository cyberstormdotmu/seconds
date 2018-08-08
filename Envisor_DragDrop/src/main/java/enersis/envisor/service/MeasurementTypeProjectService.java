package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.MeasurementTypeProject;
import enersis.envisor.entity.Project;

public interface MeasurementTypeProjectService {

	public List<MeasurementTypeProject> findAll();

	public void save(MeasurementTypeProject measurementTypeProject);

	public void delete(MeasurementTypeProject measurementTypeProject);
	
	public List<MeasurementTypeProject> findByProject(Project project);
}
