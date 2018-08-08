package enersis.envisor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Room;
import enersis.envisor.service.BuildingService;
import enersis.envisor.service.DistributionLineMeterTypeService;
import enersis.envisor.service.DistributionLineService;
import enersis.envisor.service.FlatService;
import enersis.envisor.service.MeasurementTypeProjectService;
import enersis.envisor.service.ProjectService;
import enersis.envisor.service.RoomService;

@Component("flatBean")
@ViewScoped
//@Scope("view")
// @SessionScoped
// @RequestScoped
@ManagedBean
public class FlatBean extends AbstractBacking implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4875190339711114067L;

	private List<Flat> flats = new ArrayList<Flat>();
	private Room selectedRoom = new Room();
	private Flat selectedFlat = new Flat();
	private List<String> buildingMatches = new ArrayList<String>();
	private String autocompleteString = "";
	private Building selectedBuilding = new Building();
	private String buildingToBindFlat;
	private Building selectedBuildingForFlat;
	private int passparam = 0;

	private byte floor;
	private short area;
	private String no;

//	@Autowired
//	private ProjectService projectService;

//	@Autowired
//	private DistributionLineService distributionLineService;

//	@Autowired
//	private DistributionLineMeterTypeService distributionLineMeterTypeService;

//	@Autowired
//	private MeasurementTypeProjectService measurementTypeProjectService;

	@Autowired
	private BuildingService buildingService;

	@Autowired
	private FlatService flatService;

	@Autowired
	private RoomService roomService;

	@PostConstruct
	public void postConstruct() {
		flats = flatService.findAll();
	}

	public void deleteWithRelations() {

		List<Room> roomsToDelete = roomService.findbyFlat(selectedFlat);
		for (Room room : roomsToDelete) {
			roomService.delete(room);
		}
		flats = flatService.findAll();
		
		// Oda Silindi growlu
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Daire, tüm alt deðerleriyle birlikte silindi"));
	}

	public List<String> autoComplete(String query) {
		query.toLowerCase();
		System.out.println("query: " +query);
		buildingMatches.clear();
		List<Building> aCBuildings = buildingService.findAll();
		System.out.println("bina sayýsý: "+ aCBuildings.size());
		for (Building building : aCBuildings) {
			if (building.getName().toLowerCase().contains(query)) {
				buildingMatches.add(building.getName());
			}
		}
		return buildingMatches;
	}

	public void onSelect(SelectEvent event) {
		System.out.println("onSelect: seçilen Bina " + autocompleteString
				+ "gan");
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (autocompleteString.equals("")) {
			System.out.println("equal");
			flats = flatService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedBuilding = buildingService.findByBuildingName(
					autocompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ bina"
					+ selectedBuilding.getName());
			flats = flatService.findbyBuilding(selectedBuilding);
		}

	}

	public void bindFlat() {
		Flat flatToSave = new Flat();

		flatToSave.setBuilding(buildingService.findByBuildingName(buildingToBindFlat).get(0));
		flatToSave.setNo(no);
		flatToSave.setFloor(floor);
		flatToSave.setArea(area);
		flatToSave.setStatus((byte) 0);
		flatToSave.setTransactionTime(new DateTime().toDate());
		flatService.save(flatToSave);
	}

	public void updateSelectedBuilding(Building building) {
		System.out.println("burdayým daire beani");
		setSelectedBuildingForFlat(building);
	}

	public List<Flat> getFlats() {

		// autocompleteString="";
		System.out.println("flats'i getiriyorum");
		System.out.println("selected building: " + selectedBuilding.getName());
		if (passparam == 10) {
			flats = flatService.findbyBuilding(selectedBuilding);
			autocompleteString = "";
			selectedBuilding.setName(null);
			selectedBuilding.setId((short) 0);
			passparam = 0;
			return flats;
		} else {
			if (selectedBuilding.getName() == null) {
				System.out.println("distline null geldi");
				flats = flatService.findAll();
				return flats;
			} else {
				flats = flatService.findbyBuilding(selectedBuilding);
				return flats;
			}
		}
	}
	
	
	public List<Flat> flatsByBuilding(Building building)
	{
		System.out.println("buralarda");
//		List<Flat> flatsBuBuilding= new ArrayList<Flat>();
		return flatService.findbyBuilding(building);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void setFlats(List<Flat> flats) {
		this.flats = flats;
	}

	public Room getSelectedRoom() {
		return selectedRoom;
	}

	public void setSelectedRoom(Room selectedRoom) {
		this.selectedRoom = selectedRoom;
	}

	public Flat getSelectedFlat() {
		return selectedFlat;
	}

	public void setSelectedFlat(Flat selectedFlat) {
		this.selectedFlat = selectedFlat;
	}

	public List<String> getBuildingMatches() {
		return buildingMatches;
	}

	public void setBuildingMatches(List<String> buildingMatches) {
		this.buildingMatches = buildingMatches;
	}

	public String getAutocompleteString() {
		return autocompleteString;
	}

	public void setAutocompleteString(String autocompleteString) {
		this.autocompleteString = autocompleteString;
	}

	public Building getSelectedBuilding() {
		return selectedBuilding;
	}

	public void setSelectedBuilding(Building selectedBuilding) {
		this.selectedBuilding = selectedBuilding;
	}

	public String getBuildingToBindFlat() {
		return buildingToBindFlat;
	}

	public void setBuildingToBindFlat(String buildingToBindFlat) {
		this.buildingToBindFlat = buildingToBindFlat;
	}

	public byte getFloor() {
		return floor;
	}

	public void setFloor(byte floor) {
		this.floor = floor;
	}

	public short getArea() {
		return area;
	}

	public void setArea(short area) {
		this.area = area;
	}

	public Building getSelectedBuildingForFlat() {
		return selectedBuildingForFlat;
	}

	public void setSelectedBuildingForFlat(Building selectedBuildingForFlat) {
		this.selectedBuildingForFlat = selectedBuildingForFlat;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getPassparam() {
		return passparam;
	}

	public void setPassparam(int passparam) {
		this.passparam = passparam;
	}

}
