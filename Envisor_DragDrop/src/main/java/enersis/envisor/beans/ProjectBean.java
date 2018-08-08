package enersis.envisor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.joda.time.DateTime;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineMeterType;
import enersis.envisor.entity.MeasurementType;
import enersis.envisor.entity.MeasurementTypeProject;
import enersis.envisor.entity.Project;
import enersis.envisor.service.DistributionLineMeterTypeService;
import enersis.envisor.service.DistributionLineService;
import enersis.envisor.service.MeasurementTypeProjectService;
import enersis.envisor.service.MeasurementTypeService;
import enersis.envisor.service.ProjectService;

@Component("projectBean")
 @ViewScoped
// @SessionScoped
// @RequestScoped
//@Scope("view")
@ManagedBean
public class ProjectBean extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = 1811131281321430390L;

	private List<Project> projects = new ArrayList<Project>(); // general
	private List<MeasurementType> selectedMeasurementTypes = new ArrayList<MeasurementType>();
	private List<MeasurementType> measurementTypes = new ArrayList<MeasurementType>();

	private Project selectedProject = new Project();

	// project pojo fields----------------------------------------------------
	private Project project;
	private Byte projectType;
	private String projectCode;
	private String name;
	private String projectName;
	private String surname;
	private String address;
	private String authorized;
	private String phone;
	private String email;
	private Date registryDate;
	private String explanations = new String();
	private String dataServices;
	private String operator;
	// ______________________________________________________________________

	@Autowired
	private ProjectService projectService;

	@Autowired
	private DistributionLineService distributionLineService;

	@Autowired
	private DistributionLineMeterTypeService distributionLineMeterTypeService;

	@Autowired
	private MeasurementTypeService measurementTypeService;

	@Autowired
	private MeasurementTypeProjectService measurementTypeProjectService;

	// ___________________________________________________________________________________________

	@PostConstruct
	public void postConstruct() {

		System.out.println("ProjectBean - > postConstruct()");
		projects = projectService.findAll();
		measurementTypes = measurementTypeService.findAll();
		selectedProject = new Project();
		registryDate = new DateTime().toDate();
	}

	public void deleteProjectWithRelations() {

		// Projenin Daðýtým Hatlarý siliniyor
		List<DistributionLine> distributionLines = distributionLineService
				.findByProject(selectedProject);
		List<DistributionLineMeterType> distributionLineMeterTypes = new ArrayList<DistributionLineMeterType>();
		for (DistributionLine distributionLine2 : distributionLines) {
			System.out.println("daðýtým hattý: " + distributionLine2.getName());
			distributionLineMeterTypes = distributionLineMeterTypeService
					.findbyDistributionLine(distributionLine2);
			for (DistributionLineMeterType distributionLineMeterType2 : distributionLineMeterTypes) {
				distributionLineMeterTypeService
						.delete(distributionLineMeterType2);
			}
			distributionLineMeterTypes.clear();
			distributionLineService.delete(distributionLine2);
		}
		// _________________________________________________________________________________________________________________________

		// Projenin Ölçüm tipleri ile baðlantýsý siliniyor
		List<MeasurementTypeProject> measurementTypeProjects = measurementTypeProjectService
				.findByProject(selectedProject);

		for (MeasurementTypeProject measurementTypeProject : measurementTypeProjects) {
			measurementTypeProjectService.delete(measurementTypeProject);
		}
		// ____________________________________________________________________________________

		// Son Olarak proje siliniyor
		projectService.delete(selectedProject);
		// ________________________

		projects = projectService.findAll();

		// Proje Silindi growlu
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Proje, tüm daðýtým hatlarýyla birlikte silindi"));
	}

	public void updateDataTable() {
		System.out.println("update  table");
		projects = projectService.findAll();
	}

	// public void onRowSelect(SelectEvent event) {
	// System.out.println("seçtim projeyi");
	// FacesMessage msg = new FacesMessage("Car Selected", ((Project)
	// event.getObject()).getProjectName());
	// FacesContext.getCurrentInstance().addMessage(null, msg);
	// // setSelectedProject((Project) event.getObject());
	// }

	// public void onRowUnselect(UnselectEvent event) {
	// FacesMessage msg = new FacesMessage("Car Unselected", ((Project)
	// event.getObject()).getProjectName());
	// FacesContext.getCurrentInstance().addMessage(null, msg);
	// }

	public Project sendSelectedProjectToDLBean() {
		Project projectToSend = new Project();
		System.out.println("selectedProject  gönderiliyor");
		projectToSend = selectedProject;
		// selectedProject.setId((short) 0);
		// selectedProject.setProjectCode(null);
		// selectedProject.setProjectName(null);
		// selectedProject.setName(null);
		// selectedProject.setSurname(null);
		// selectedProject.setAddress(null);
		// selectedProject.setAuthorized(null);
		// selectedProject.setPhone(null);
		// selectedProject.setEmail(null);
		// selectedProject.setRegistryDate(null);
		// selectedProject.setExplanations(null);
		// selectedProject.setDataServices(null);
		// selectedProject.setOperator(null);
		// selectedProject.setTransactionTime(null);
		// selectedProject.setStatus((byte) 0);
		return projectToSend;
	}

	public void saveProject() {
		System.out.println("proje kaydetcem");
		Project temppProject = new Project();
		temppProject.setProjectType(projectType);
		temppProject.setProjectCode(projectCode);
		temppProject.setName(name);
		temppProject.setProjectName(projectName);
		temppProject.setSurname(surname);
		temppProject.setAddress(address);
		temppProject.setAuthorized(authorized);
		temppProject.setPhone(phone);
		temppProject.setEmail(email);
		temppProject.setRegistryDate(registryDate);
		temppProject.setExplanations(explanations);
		temppProject.setDataServices(dataServices);
		temppProject.setOperator(operator);
		temppProject.setTransactionTime(new DateTime().toDate());
		temppProject.setStatus((byte) 0);
		// System.out.println("burdayým: executebuttonclick");

		projectService.save(temppProject);
		project = temppProject;
		bindMeasurementType(temppProject);
		projects.clear();
		projects = projectService.findAll();
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Proje kaydedildi "));
	}

	public void bindMeasurementType(Project project) {

		// System.out.println("ölçüm tipi: " + selectedMeasurementTypes.size());
		// System.out.println("seçili projenin yürütücü adý: " +
		// selectedProject.getName());
		System.out.println("burdayým");

		for (MeasurementType measurementType : selectedMeasurementTypes) {
			MeasurementTypeProject measurementTypeProject = new MeasurementTypeProject();
			measurementTypeProject.setMeasurementType(measurementType);
			measurementTypeProject.setProject(project);
			measurementTypeProject.setTransactionTime(new DateTime().toDate());
			measurementTypeProject.setStatus((byte) 0);
			measurementTypeProjectService.save(measurementTypeProject);
		}
		// FacesContext context = FacesContext.getCurrentInstance();
		// context.addMessage(null, new FacesMessage("Baþarýlý!",
		// "Ölçüm Tipleri Eklendi "));
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		// System.out.println("proje   beande selected  proje set edildi:!!!!!!!!!!!!!!!!!!!!!1 "+selectedProject.getProjectName());
		this.selectedProject = selectedProject;
	}

	public List<Project> getProjects() {
		// System.out.println("project beande projeler alýnýyor");
		return projects;
	}

	public void setProjects(List<Project> projects) {
		// System.out.println("project beande projeler set ediliyor");
		this.projects = projects;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Byte getProjectType() {
		return projectType;
	}

	public void setProjectType(Byte projectType) {
		this.projectType = projectType;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthorized() {
		return authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getRegistryDate() {
		return registryDate;
	}

	public void setRegistryDate(Date registryDate) {
		this.registryDate = registryDate;
	}

	public String getExplanations() {
		return explanations;
	}

	public void setExplanations(String explanations) {
		this.explanations = explanations;
	}

	public String getDataServices() {
		return dataServices;
	}

	public void setDataServices(String dataServices) {
		this.dataServices = dataServices;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<MeasurementType> getSelectedMeasurementTypes() {
		return selectedMeasurementTypes;
	}

	public void setSelectedMeasurementTypes(
			List<MeasurementType> selectedMeasurementTypes) {
		for (MeasurementType measurementType : selectedMeasurementTypes) {
			System.out.println("seçilen tip:" + measurementType.getType());
		}
		this.selectedMeasurementTypes = selectedMeasurementTypes;
	}

	public List<MeasurementType> getMeasurementTypes() {
		return measurementTypes;
	}

	public void setMeasurementTypes(List<MeasurementType> measurementTypes) {
		this.measurementTypes = measurementTypes;
	}

}
