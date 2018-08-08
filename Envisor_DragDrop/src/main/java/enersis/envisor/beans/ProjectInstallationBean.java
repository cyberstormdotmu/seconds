package enersis.envisor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineMeterType;
import enersis.envisor.entity.MeasurementType;
import enersis.envisor.entity.MeasurementTypeProject;
import enersis.envisor.entity.MeterType;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.ReadOutMethod;
import enersis.envisor.service.DistributionLineMeterTypeService;
import enersis.envisor.service.DistributionLineService;
import enersis.envisor.service.MeasurementTypeProjectService;
import enersis.envisor.service.MeasurementTypeService;
import enersis.envisor.service.MeterTypeService;
import enersis.envisor.service.ProjectService;
import enersis.envisor.service.ReadOutMethodService;

@Component("projectInstallationBean")
// @Scope("view")
// @SessionScoped
@ViewScoped
@ManagedBean
public class ProjectInstallationBean extends AbstractBacking implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -56574387397142623L;
	// private static final long serialVersionUID = 1L;

	// table list
	// objects--------------------------------------------------------------
	private List<DistributionLine> addedDistributionLines = new ArrayList<DistributionLine>();
	private List<Project> projects = new ArrayList<Project>(); // general
																// project list.
																// comes from
																// findall();
	private List<Project> filteredProjects = null;
	// private List<Project> filteredProjects = new ArrayList<Project>(); //
	// search helper list.
	private List<MeasurementType> measurementTypes = new ArrayList<MeasurementType>();
	private List<MeterType> meterTypes = new ArrayList<MeterType>(); // meter types for dist.lines
	private List<ReadOutMethod> readOutMethods = new ArrayList<ReadOutMethod>(); //readoutmethods for dist.lines

	// __________________________________________________________________________________________

	// project pojo fields----------------------------------------------------
	private Project project;
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
	private boolean envisorability;
	// ______________________________________________________________________

	// distribution line pojo
	// fields--------------------------------------------------------------------
	private String nameDistLine;
	private String operationDistLine;
	private String readOutMethodDistLine;
	// ________________________________________________________________________________

	// Selection
	// objects-------------------------------------------------------------------------------
	private Project selectedProject;
	private List<MeasurementType> selectedMeasurementTypes = new ArrayList<MeasurementType>();
	private List<MeterType> selectedMeterTypes = new ArrayList<MeterType>();
	// ______________________________________________________________

	private MeterType metertype;

	// Service Class
	// injections------------------------------------------------------------------
	@Autowired
	private ReadOutMethodService readOutMethodService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private MeasurementTypeProjectService measurementTypeProjectService;
	@Autowired
	private MeasurementTypeService measurementTypeService;
	@Autowired
	private MeterTypeService meterTypeService;
	@Autowired
	private DistributionLineService distributionLineService;
	@Autowired
	private DistributionLineMeterTypeService distributionLineMeterTypeService;

	// ___________________________________________________________________________________________

	@PostConstruct
	public void postConstruct() {
		addedDistributionLines = new ArrayList<DistributionLine>();
		projects = projectService.findAll();
		filteredProjects = projectService.findAll();
		measurementTypes = measurementTypeService.findAll();
		meterTypes = meterTypeService.findAll();
		readOutMethods = readOutMethodService.findAll();
		selectedProject = new Project();

		project = new Project();
		projectCode = new String();
		name = new String();
		projectName = new String();
		surname = new String();
		address = new String();
		authorized = new String();
		phone = new String();
		email = new String();
		registryDate = new DateTime().toDate();
		explanations = new String();
		dataServices = new String();
		operator = new String();
		envisorability = false;
//		System.out.println("Sayfa yeniden yüklendi");
	}

	public void saveProject() {

		Project temppProject = new Project();
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
		projects.clear();
		projects = projectService.findAll();
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Proje kaydedildi "));
	}

	public void bindMeasurementType() {

		// System.out.println("ölçüm tipi: " + selectedMeasurementTypes.size());
		// System.out.println("seçili projenin yürütücü adý: " +
		// selectedProject.getName());

		for (MeasurementType measurementType : selectedMeasurementTypes) {
			MeasurementTypeProject measurementTypeProject = new MeasurementTypeProject();
			measurementTypeProject.setMeasurementType(measurementType);
			// test amaçlý proje seçimi. proje kaydedilmemiþse tablodan seçili
			// olan proje üzerinde iþlem yapýlýyor
			if (project.getProjectCode() == null) {
//				System.out.println("ölçüm tipi baðlantý kontrolü");
//				System.out.println("selected project:"+ selectedProject.getProjectName());
				measurementTypeProject.setProject(selectedProject);
			} else {
				measurementTypeProject.setProject(project);
			}

			// ______________________________________________________________________________________________-
			// measurementTypeProject.setProject(selectedProject);
			measurementTypeProject.setTransactionTime(new DateTime().toDate());
			measurementTypeProjectService.save(measurementTypeProject);
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Ölçüm Tipleri Eklendi "));
	}

	public void bindDistributionLine() {

//		System.out.println("project: " + project.getProjectName());
//		System.out.println("distr name: " + nameDistLine);
//		System.out.println("readout" + readOutMethodDistLine);

		DistributionLine distributionLine = new DistributionLine();
		if (project.getProjectCode() == null) {
			distributionLine.setProject(selectedProject);;
		} else {
			distributionLine.setProject(project);
		}
		distributionLine.setName(nameDistLine);
		distributionLine.setOperation(operationDistLine);
		distributionLine.setStatus((byte) 0);
		distributionLine.setTransactionTime(new DateTime().toDate());
		distributionLineService.save(distributionLine);
		addedDistributionLines.add(distributionLine);

		List<DistributionLineMeterType> distributionLineMeterTypeList = new ArrayList<DistributionLineMeterType>();
		for (MeterType meterType : selectedMeterTypes) {
			DistributionLineMeterType distributionLineMeterType = new DistributionLineMeterType();

			System.out.println("Ölçüm tipi:" + meterType.getName());
			distributionLineMeterType.setDistributionLine(distributionLine);
			distributionLineMeterType.setMeterType(meterType);
			distributionLineMeterType.setStatus((byte) 0);
			distributionLineMeterType.setTransactionTime(new DateTime()
					.toDate());
			distributionLineMeterTypeList.add(distributionLineMeterType);
		}
		System.out.println("sayz" + distributionLineMeterTypeList.size());
		for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypeList) {
			distributionLineMeterTypeService.save(distributionLineMeterType);
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Daðýtým Hattý Eklendi"));
	}

	public String getMeterTypesofDistributionLineAsString(
			DistributionLine distributionLine) {

		List<MeterType> meterTypesByDistributionLine = new ArrayList<MeterType>();
		List<DistributionLineMeterType> distributionLineMeterTypes = new ArrayList<DistributionLineMeterType>();
		distributionLineMeterTypes = distributionLineMeterTypeService
				.findbyDistributionLine(distributionLine);
		System.out
				.println("relation Size:" + distributionLineMeterTypes.size());
		for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypes) {
			meterTypesByDistributionLine.add(distributionLineMeterType
					.getMeterType());
		}
		String meterTypesAsString = "";
		for (MeterType meterType : meterTypesByDistributionLine) {
//			System.out.println("abbreviation:" + meterType.getAbbreviation());
			if (meterTypesAsString.equals("")) {
				meterTypesAsString = meterType.getAbbreviation();
			} else {
				meterTypesAsString = "" + meterTypesAsString + "+"
						+ meterType.getAbbreviation();
			}

		}
////		System.out.println("meterTypesByDistributionLine: "
//				+ meterTypesByDistributionLine.size());
//		System.out.println("DistributionLine is this:"
//				+ distributionLine.getName());
//		System.out.println("Concatenation: " + meterTypesAsString);
		return meterTypesAsString;
	}

	// GETTER SETTERS GENERATED FOR
	// PRIMEFACES________________________________________________

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
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

	public boolean isEnvisorability() {
		return envisorability;
	}

	public void setEnvisorability(boolean envisorability) {
		this.envisorability = envisorability;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		System.out.println("proje satýrý seçildi");
		this.selectedProject = selectedProject;
	}

	public List<MeasurementType> getSelectedMeasurementTypes() {
		return selectedMeasurementTypes;
	}

	public void setSelectedMeasurementTypes(
			List<MeasurementType> selectedMeasurementTypes) {
		this.selectedMeasurementTypes = selectedMeasurementTypes;
	}

	public List<MeasurementType> getMeasurementTypes() {
		return measurementTypes;
	}

	public void setMeasurementTypes(List<MeasurementType> measurementTypes) {
		this.measurementTypes = measurementTypes;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<Project> getFilteredProjects() {
		return filteredProjects;
	}

	public void setFilteredProjects(List<Project> filteredProjects) {

		// for (Project project : filteredProjects) {
		// System.out.println("Proje adý:" +project.getProjectName());
		// }
		this.filteredProjects = filteredProjects;
	}

	public String getNameDistLine() {
		return nameDistLine;
	}

	public void setNameDistLine(String nameDistLine) {
		this.nameDistLine = nameDistLine;
	}

	public String getOperationDistLine() {
		return operationDistLine;
	}

	public void setOperationDistLine(String operationDistLine) {
		this.operationDistLine = operationDistLine;
	}

	public String getReadOutMethodDistLine() {
		return readOutMethodDistLine;
	}

	public void setReadOutMethodDistLine(String readOutMethodDistLine) {
		this.readOutMethodDistLine = readOutMethodDistLine;
	}

	public List<MeterType> getMeterTypes() {
		return meterTypes;
	}

	public void setMeterTypes(List<MeterType> meterTypes) {
		this.meterTypes = meterTypes;
	}

	public List<MeterType> getSelectedMeterTypes() {
		return selectedMeterTypes;
	}

	public void setSelectedMeterTypes(List<MeterType> selectedMeterTypes) {
		this.selectedMeterTypes = selectedMeterTypes;
	}

	public MeterType getMetertype() {
		return metertype;
	}

	public void setMetertype(MeterType metertype) {
		this.metertype = metertype;
	}

	public List<DistributionLine> getAddedDistributionLines() {
		return addedDistributionLines;
	}

	public void setAddedDistributionLines(
			List<DistributionLine> addedDistributionLines) {
		this.addedDistributionLines = addedDistributionLines;
	}

	public List<ReadOutMethod> getReadOutMethods() {
		return readOutMethods;
	}

	public void setReadOutMethods(List<ReadOutMethod> readOutMethods) {
		this.readOutMethods = readOutMethods;
	}

}
