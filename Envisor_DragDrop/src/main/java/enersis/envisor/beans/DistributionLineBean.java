package enersis.envisor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineMeterType;
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

@Component("distributionLineBean")
@ViewScoped
// @SessionScoped
// @RequestScoped
//@Scope("view")
@ManagedBean
public class DistributionLineBean extends AbstractBacking implements
		Serializable {

	private static final long serialVersionUID = -2532992269658797999L;

	private List<DistributionLine> distributionLines = new ArrayList<DistributionLine>(); // general

	private DistributionLine selectedDistributionLine;

	private Project selectedProjectforDistLine = new Project();

	private int passparam = 0;

	private String autocompleteString = "";

	private List<String> projectMatches = new ArrayList<String>();

	private String projectToBindDistLine = "";

	// table list
	// objects--------------------------------------------------------------
	private List<DistributionLine> addedDistributionLines = new ArrayList<DistributionLine>();
	private List<ReadOutMethod> readOutMethods = new ArrayList<ReadOutMethod>(); // readoutmethods
																					// for
																					// dist.lines
	private List<MeterType> meterTypes = new ArrayList<MeterType>(); // meter
																		// types
																		// for
																		// dist.lines

	// distribution line pojo
	// fields--------------------------------------------------------------------
	private String nameDistLine;
	private String operationDistLine;
	private String readOutMethodDistLine;
	// ________________________________________________________________________________

	private List<MeterType> selectedMeterTypes = new ArrayList<MeterType>();

	// daðýtým hattý, ölçüm tipleri çiftleri
	private Map<DistributionLine, String> distmeter = new HashMap<DistributionLine, String>();

	@Autowired
	private ProjectService projectService;

	@Autowired
	private DistributionLineService distributionLineService;

	@Autowired
	private DistributionLineMeterTypeService distributionLineMeterTypeService;

	@Autowired
	private MeasurementTypeProjectService measurementTypeProjectService;

	@Autowired
	private MeasurementTypeService measurementTypeService;
	@Autowired
	private MeterTypeService meterTypeService;
	@Autowired
	private ReadOutMethodService readOutMethodService;

	// ___________________________________________________________________________________________

	@PostConstruct
	public void postConstruct() {
		distributionLines = distributionLineService.findAll();
		mapLinesWithMeters();
		selectedDistributionLine = new DistributionLine();
		meterTypes = meterTypeService.findAll();
		readOutMethods = readOutMethodService.findAll();
//		System.out.println("post con");
	}

	public void deleteWithRelations() {
		List<DistributionLineMeterType> distributionLineMeterTypes = new ArrayList<DistributionLineMeterType>();
		// System.out.println("daðýtým hattý: " +
		// selectedDistributionLine.getName());
		distributionLineMeterTypes = distributionLineMeterTypeService
				.findbyDistributionLine(selectedDistributionLine);
		for (DistributionLineMeterType distributionLineMeterType2 : distributionLineMeterTypes) {
			distributionLineMeterTypeService.delete(distributionLineMeterType2);
		}
		distributionLineMeterTypes.clear();
		distributionLineService.delete(selectedDistributionLine);

		// _________________________________________________________________________________________________________________________

		distributionLines = distributionLineService.findAll();

		// Proje Silindi growlu
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Daðýtým Hattý, tüm alt deðerleriyle birlikte silindi"));
	}

	public void mapLinesWithMeters() {
		for (DistributionLine distributionLine : distributionLines) {
			distmeter.put(distributionLine,
					getMeterTypesofDistributionLineAsString(distributionLine));
		}
	}

	public String getMeterTypesofDistributionLineAsString(
			DistributionLine distributionLine) {

		List<MeterType> meterTypesByDistributionLine = new ArrayList<MeterType>();
		List<DistributionLineMeterType> distributionLineMeterTypes = new ArrayList<DistributionLineMeterType>();
		distributionLineMeterTypes = distributionLineMeterTypeService
				.findbyDistributionLine(distributionLine);
		for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypes) {
			meterTypesByDistributionLine.add(distributionLineMeterType
					.getMeterType());
		}
		String meterTypesAsString = "";
		for (MeterType meterType : meterTypesByDistributionLine) {
			// System.out.println("abbreviation:" +
			// meterType.getAbbreviation());
			if (meterTypesAsString.equals("")) {
				meterTypesAsString = meterType.getAbbreviation();
			} else {
				meterTypesAsString = "" + meterTypesAsString + "+"
						+ meterType.getAbbreviation();
			}

		}
		// System.out.println("meterTypesByDistributionLine: "
		// + meterTypesByDistributionLine.size());
		// System.out.println("DistributionLine is this:"
		// + distributionLine.getName());
		// System.out.println("Concatenation: " + meterTypesAsString);
		return meterTypesAsString;
	}

	public void updateSelectedProject(Project project) {
		System.out.println("burdayým daðýtým hattý beani");
		setSelectedProjectforDistLine(project);
	}

	// public void updateTableByProject() {
	// System.out.println("sayfa açýlýþta tablo güncelleme");
	// distributionLines = distributionLineService
	// .findByProject(selectedProjectforDistLine);
	// }

	public List<String> autoComplete(String query) {
		query.toLowerCase();
		projectMatches.clear();
		List<Project> aCProjects = projectService.findAll();
		for (Project project : aCProjects) {
			if (project.getProjectName().toLowerCase().contains(query)) {
				projectMatches.add(project.getProjectName());
			}
		}
		// }

		return projectMatches;
	}

	public void onSelect(SelectEvent event) {
		System.out.println("onProjectSelect: seçilen proje "
				+ autocompleteString);
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (autocompleteString.equals("")) {
			System.out.println("equal");
			distributionLines = distributionLineService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedProjectforDistLine = projectService.findByProjectName(
					autocompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ proje"
					+ selectedProjectforDistLine.getProjectName());
			distributionLines = distributionLineService
					.findByProject(selectedProjectforDistLine);
			autocompleteString = "";
		}

	}

	public void pageClean() {
		selectedDistributionLine = distributionLineService.findAll().get(0);
	}

	public String getLinesMeters(DistributionLine distributionLine) {
		// System.out.println("daðýtým-ölçüm iliþkilerini hazýrlýyorum");
		return distmeter.get(distributionLine);
	}

	public void updateDataTable() {
		System.out.println("update table");
		distributionLines = distributionLineService.findAll();
	}

	public void bindDistributionLine() {

		Project forDistline = new Project();

		forDistline = projectService.findByProjectName(projectToBindDistLine)
				.get(0);
		// System.out.println("project: " + project.getProjectName());
		// System.out.println("distr name: " + nameDistLine);
		// System.out.println("readout" + readOutMethodDistLine);

		DistributionLine distributionLine = new DistributionLine();
		if (forDistline.getProjectCode() == null) {
			distributionLine.setProject(selectedProjectforDistLine);
			;
		} else {
			distributionLine.setProject(forDistline);
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

	public List<DistributionLine> getDistributionLines() {
		// autocompleteString="";
//		System.out.println("distributionLines'ý getiriyorum");
//		System.out.println("distline beandeki selected projectname:  "
//				+ selectedProjectforDistLine.getProjectName());
		if (passparam == 10) {
			distributionLines = distributionLineService
					.findByProject(selectedProjectforDistLine);
			autocompleteString = "";
			// selectedProjectforDistLine.setProjectName(null);
			// selectedProjectforDistLine.setId((short) 0);
			passparam = 0;
			return distributionLines;
		} else {
			if (passparam == 20) {
//				System.out.println("passaparam=20");
				distributionLines = distributionLineService.findAll();
				passparam = 0;
				return distributionLines;
			}
			else
			{
				passparam = 0;
				return distributionLines;
			}
			// if (selectedProjectforDistLine.getProjectName() == null) {
			// System.out.println("proje null geldi");
			
			// // } else {
			// distributionLines = distributionLineService
			// .findByProject(selectedProjectforDistLine);
			// return distributionLines;
			// // }

		}
		// return distributionLines;
	}

	public void setDistributionLines(List<DistributionLine> distributionLines) {
		this.distributionLines = distributionLines;
	}

	public DistributionLine getSelectedDistributionLine() {
		return selectedDistributionLine;
	}

	public void setSelectedDistributionLine(
			DistributionLine selectedDistributionLine) {
		this.selectedDistributionLine = selectedDistributionLine;
	}

	public Project getSelectedProjectforDistLine() {
		return selectedProjectforDistLine;
	}

	public void setSelectedProjectforDistLine(Project selectedProjectforDistLine) {
		System.out.println("seçili proje budur: "
				+ selectedProjectforDistLine.getProjectName());
		this.selectedProjectforDistLine = selectedProjectforDistLine;
	}

	public String getAutocompleteString() {
		return autocompleteString;
	}

	public void setAutocompleteString(String autocompleteString) {
		System.out.println("auto complete set ediliyor" + autocompleteString);
		this.autocompleteString = autocompleteString;
	}

	public int getPassparam() {
		return passparam;
	}

	public void setPassparam(int passparam) {
		this.passparam = passparam;
	}

	public List<String> getProjectMatches() {
		return projectMatches;
	}

	public void setProjectMatches(List<String> projectMatches) {
		this.projectMatches = projectMatches;
	}

	public List<DistributionLine> getAddedDistributionLines() {
		return addedDistributionLines;
	}

	public void setAddedDistributionLines(
			List<DistributionLine> addedDistributionLines) {
		this.addedDistributionLines = addedDistributionLines;
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

	public List<MeterType> getSelectedMeterTypes() {
		return selectedMeterTypes;
	}

	public void setSelectedMeterTypes(List<MeterType> selectedMeterTypes) {
		this.selectedMeterTypes = selectedMeterTypes;
	}

	public Map<DistributionLine, String> getDistmeter() {
		return distmeter;
	}

	public void setDistmeter(Map<DistributionLine, String> distmeter) {
		this.distmeter = distmeter;
	}

	public List<ReadOutMethod> getReadOutMethods() {
		return readOutMethods;
	}

	public void setReadOutMethods(List<ReadOutMethod> readOutMethods) {
		this.readOutMethods = readOutMethods;
	}

	public List<MeterType> getMeterTypes() {
		return meterTypes;
	}

	public void setMeterTypes(List<MeterType> meterTypes) {
		this.meterTypes = meterTypes;
	}

	public void setProjectToBindDistLine(String projectToBindDistLine) {
		this.projectToBindDistLine = projectToBindDistLine;
	}

	public String getProjectToBindDistLine() {
		return projectToBindDistLine;
	}
}
