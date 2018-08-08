package enersis.envisor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import utils.MeterExtendedDistributionLine;
import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineMeterType;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.MeasurementTypeProject;
import enersis.envisor.entity.MeterType;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.service.BuildingService;
import enersis.envisor.service.DistributionLineMeterTypeService;
import enersis.envisor.service.DistributionLineService;
import enersis.envisor.service.FlatService;
import enersis.envisor.service.MeasurementTypeProjectService;
import enersis.envisor.service.ProjectService;
import enersis.envisor.service.RoomService;

@Component("buildingBean")
@ViewScoped
// @SessionScoped
// @RequestScoped
// @Scope("view")
@ManagedBean
public class BuildingBean extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = -5945871982820036781L;

	private List<Building> buildings = new ArrayList<Building>();
	private DistributionLine selectedDistributionLine;
	private Project selectedProjectforDistLine = new Project();
	private Building selectedBuilding;

	private int passparam = 0;

	private String autocompleteString = "";

	private DistributionLine selectedDistLineForBuilding;

	private String distributionLineToBindBuilding = "";

	private String projectToBindBuilding = "";

	// Building fields for
	// saving------------------------------------------------------------------

	private String name;
	private Byte flatCount;
	// ______________________________________________________________________________________________

	private List<String> distributionLineMatches = new ArrayList<String>();
	private List<String> projectMatches = new ArrayList<String>();

	private List<DistributionLine> distributionLinesByProject = new ArrayList<DistributionLine>();
	private List<DistributionLine> selectedDistributionLinesToChoose = new ArrayList<DistributionLine>();

	@Autowired
	private ProjectService projectService;

	@Autowired
	private DistributionLineService distributionLineService;

	@Autowired
	private DistributionLineMeterTypeService distributionLineMeterTypeService;

	@Autowired
	private MeasurementTypeProjectService measurementTypeProjectService;

	@Autowired
	private BuildingService buildingService;

	@Autowired
	private FlatService flatService;

	@Autowired
	private RoomService roomService;

	// ___________________________________________________________________________________________

	@PostConstruct
	public void postConstruct() {
		buildings = buildingService.findAll();
		selectedDistributionLine = new DistributionLine();
	}

	public void deleteWithRelations() {

		List<Flat> flatstoDelete = flatService.findbyBuilding(selectedBuilding);
		List<Room> roomsToDelete = new ArrayList<Room>();
		for (Flat flat : flatstoDelete) {
			roomsToDelete = roomService.findbyFlat(flat);
			for (Room room : roomsToDelete) {
				roomService.delete(room);
			}

		}
		// Bina Silindi growlu
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Daðýtým Hattý,  tüm alt deðerleriyle birlikte silindi"));
	}

	public List<String> autoComplete(String query) {
		query.toLowerCase();
		distributionLineMatches.clear();
		List<DistributionLine> aCDistributionLines = distributionLineService
				.findAll();
		for (DistributionLine distributionLine : aCDistributionLines) {
			if (distributionLine.getName().toLowerCase().contains(query)) {
				distributionLineMatches.add(distributionLine.getName());
			}
		}
		return distributionLineMatches;
	}

	public List<String> autoCompleteProject(String query) {
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

	public void onProjectSelect(SelectEvent event) {

		selectedProjectforDistLine = projectService.findByProjectName(
				projectToBindBuilding).get(0);
		distributionLinesByProject = distributionLineService
				.findByProject(selectedProjectforDistLine);
		projectToBindBuilding = "";

	}

	public void onSelect(SelectEvent event) {
		System.out.println("onProjectSelect: seçilen proje "
				+ autocompleteString + "gan");
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (autocompleteString.equals("")) {
			System.out.println("equal");
			buildings = buildingService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedDistributionLine = distributionLineService
					.findByDistributionLineName(autocompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ proje"
					+ selectedDistributionLine.getName());
			buildings = buildingService
					.findbyDistributionLine(selectedDistributionLine);
		}

	}

	public void bindBuilding() {
		Building buildingToSave = new Building();

		// buildingToSave.setDistributionLine(distributionLineService.findByDistributionLineName(distributionLineToBindBuilding).get(0)
		// );
		buildingToSave.setFlatCount(flatCount);
		buildingToSave.setName(name);
		buildingToSave.setStatus((byte) 0);
		buildingToSave.setTransactionTime(new DateTime().toDate());
		buildingService.save(buildingToSave);
	}

	public void updateSelectedDistributionLine(DistributionLine distributionLine) {
		System.out.println("burdayým bina beani");
		setSelectedDistLineForBuilding(distributionLine);
	}

	public DistributionLine getSelectedDistributionLine() {
		return selectedDistributionLine;
	}

	public void setSelectedDistributionLine(
			DistributionLine selectedDistributionLine) {
		this.selectedDistributionLine = selectedDistributionLine;
	}

	public List<Building> getBuildings() {
		// autocompleteString="";
		System.out.println("buildings'i getiriyorum");
		System.out.println("selected distname:  "
				+ selectedDistributionLine.getName());
		if (passparam == 10) {
			buildings = buildingService
					.findbyDistributionLine(selectedDistributionLine);
			autocompleteString = "";
			selectedDistributionLine.setName(null);
			selectedDistributionLine.setId((short) 0);
			passparam = 0;
			return buildings;
		} else {
			if (selectedDistributionLine.getName() == null) {
				System.out.println("distline null geldi");
				buildings = buildingService.findAll();
				return buildings;
			} else {
				buildings = buildingService
						.findbyDistributionLine(selectedDistributionLine);
				return buildings;
			}
		}
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	public Building getSelectedBuilding() {
		return selectedBuilding;
	}

	public void setSelectedBuilding(Building selectedBuilding) {
		this.selectedBuilding = selectedBuilding;
	}

	public List<String> getDistributionLineMatches() {
		return distributionLineMatches;
	}

	public void setDistributionLineMatches(List<String> distributionLineMatches) {
		this.distributionLineMatches = distributionLineMatches;
	}

	public int getPassparam() {
		return passparam;
	}

	public void setPassparam(int passparam) {
		this.passparam = passparam;
	}

	public String getAutocompleteString() {
		return autocompleteString;
	}

	public void setAutocompleteString(String autocompleteString) {
		this.autocompleteString = autocompleteString;
	}

	public DistributionLine getSelectedDistLineForBuilding() {
		return selectedDistLineForBuilding;
	}

	public void setSelectedDistLineForBuilding(
			DistributionLine selectedDistLineForBuilding) {
		this.selectedDistLineForBuilding = selectedDistLineForBuilding;
	}

	public String getDistriburtionLineToBindBuilding() {
		return distributionLineToBindBuilding;
	}

	public void setDistributionLineToBindBuilding(
			String distributionLineToBindBuilding) {
		this.distributionLineToBindBuilding = distributionLineToBindBuilding;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getFlatCount() {
		return flatCount;
	}

	public void setFlatCount(Byte flatCount) {
		this.flatCount = flatCount;
	}

	public String getProjectToBindBuilding() {
		return projectToBindBuilding;
	}

	public void setProjectToBindBuilding(String projectToBindBuilding) {
		this.projectToBindBuilding = projectToBindBuilding;
	}

	public List<DistributionLine> getDistributionLinesByProject() {
		return distributionLinesByProject;
	}

	public void setDistributionLinesByProject(
			List<DistributionLine> distributionLinesByProject) {
		this.distributionLinesByProject = distributionLinesByProject;
	}

	public List<DistributionLine> getSelectedDistributionLinesToChoose() {
		return selectedDistributionLinesToChoose;
	}

	public void setSelectedDistributionLinesToChoose(
			List<DistributionLine> selectedDistributionLinesToChoose) {
		this.selectedDistributionLinesToChoose = selectedDistributionLinesToChoose;
	}

}
