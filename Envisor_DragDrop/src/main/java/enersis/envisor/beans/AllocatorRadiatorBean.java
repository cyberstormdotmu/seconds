package enersis.envisor.beans;

import java.io.Serializable;
import java.math.BigDecimal;
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

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.Room;
import enersis.envisor.service.BuildingService;
import enersis.envisor.service.DistributionLineService;
import enersis.envisor.service.FlatService;
import enersis.envisor.service.RoomService;
import enersis.envisor.service.HeatCostAllocatorService;

@Component("allocatorRadiatorBean")
@ViewScoped
//@Scope("view")
// @SessionScoped
// @RequestScoped
@ManagedBean
public class AllocatorRadiatorBean extends AbstractBacking implements
		Serializable {
	private static final long serialVersionUID = 166396037801654605L;
	private List<HeatCostAllocator> heatCostAllocators = new ArrayList<HeatCostAllocator>();
	private List<Room> rooms = new ArrayList<Room>();
	private HeatCostAllocator selectedHeatCostAllocator = new HeatCostAllocator();
	private Room selectedRoom = new Room();
	private List<String> roomMatches = new ArrayList<String>();
	private String autocompleteString = "";
	private String distributionLineAutoCompleteString = "";
	private Byte roomToBindHeatCostAllocator;
	private Room selectedRoomForHeatCostAllocator;
	private int passparam = 0;
	private String flatAutoCompleteString = "";
	private List<String> distributionLineMatches = new ArrayList<String>();
	private List<String> flatMatches = new ArrayList<String>();
	private List<DistributionLine> distributionLines = new ArrayList<DistributionLine>();
	// private Room selectedRoom = new Room();
	private Flat selectedFlat = new Flat();

	private Room room;
	private String serialNo;
	private String radiatorBrand;
	private String radiatorType;
	private Double kges;
	private int measurement;

	@Autowired
	private BuildingService buildingService;

	@Autowired
	private FlatService flatService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private HeatCostAllocatorService heatCostAllocatorService;

	@Autowired
	private DistributionLineService distributionLineService;

	@PostConstruct
	public void postConstruct() {
//		heatCostAllocators = heatCostAllocatorService.findAll();
	}

	public void deleteWithRelations() {

		heatCostAllocatorService.delete(selectedHeatCostAllocator);
		// Oda Silindi growlu
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Oda, tüm alt deðerleriyle birlikte silindi"));
	}

	public void onDistributionLineSelect(SelectEvent event) {
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (distributionLineAutoCompleteString.equals("")) {
			System.out.println("equal");
			heatCostAllocators = heatCostAllocatorService.findAll();

		} else {
			heatCostAllocators = heatCostAllocatorService
					.findByDistributionLine(distributionLineService
							.findByDistributionLineName(
									distributionLineAutoCompleteString).get(0));
			System.out.println(heatCostAllocators.size());
		}

	}

	public List<String> distributionLineAutoComplete(String query) {
		query.toLowerCase();
		distributionLineMatches.clear();
		List<DistributionLine> aCDistributionLines;
		if (distributionLines.size() == 0) {
			aCDistributionLines = distributionLineService.findAll();
		} else {
			aCDistributionLines = distributionLines;
		}
		for (DistributionLine distributionLine : aCDistributionLines) {
			if (distributionLine.getName().toLowerCase().contains(query)) {
				distributionLineMatches.add(distributionLine.getName());
			}
		}
		return distributionLineMatches;
	}

	public List<String> autoComplete(String query) {
		roomMatches.clear();
		List<Room> aCRooms;
		if (rooms.size() == 0) {
			aCRooms = roomService.findAll();
		} else {
			aCRooms = rooms;
		}
		// List<Room> aCRooms = roomService.findAll();
		for (Room room : aCRooms) {
			if (Byte.toString(room.getOrderNo()).contains(query)) {
				roomMatches.add(Byte.toString(room.getOrderNo()));
			}
		}
		return roomMatches;
	}

	public List<String> flatAutoComplete(String query) {
		flatMatches.clear();
		List<Flat> aCFlats = flatService.findAll();
		for (Flat flat : aCFlats) {
			if (flat.getNo().contains(query)) {
				flatMatches.add(flat.getNo());
			}
		}
		return flatMatches;
	}

	public void onSelect(SelectEvent event) {
		System.out.println("onSelect: seçilen Oda " + autocompleteString
				+ "gan");
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (autocompleteString.equals("")) {
			System.out.println("equal");
			heatCostAllocators = heatCostAllocatorService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedRoom = roomService.findByRoomOrderNo(
					Byte.parseByte(autocompleteString)).get(0);
			System.out.println("auto complete sonrasý seçilmiþ bina"
					+ selectedRoom.getOrderNo());
			heatCostAllocators = heatCostAllocatorService
					.findbyRoom(selectedRoom);
		}

	}

	public void onFlatSelect(SelectEvent event) {
		System.out.println("onSelect: seçilen Daire " + flatAutoCompleteString
				+ "gan");
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (flatAutoCompleteString.equals("")) {
			System.out.println("equal");
			rooms = roomService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedFlat = flatService.findByFlatNo(
					Byte.parseByte(flatAutoCompleteString)).get(0);
			System.out.println("auto complete sonrasý seçilmiþ daire"
					+ selectedFlat.getNo());
			rooms = roomService.findbyFlat(selectedFlat);
		}

	}

	public void bindHeatCostAllocator() {
		HeatCostAllocator heatCostAllocatorToSave = new HeatCostAllocator();

		heatCostAllocatorToSave.setRoom(roomService.findByRoomOrderNo(
				roomToBindHeatCostAllocator).get(0));
		heatCostAllocatorToSave.setSerialNo(serialNo);
		heatCostAllocatorToSave.setRadiatorBrand(radiatorBrand);
		heatCostAllocatorToSave.setRadiatorType(radiatorType);
		heatCostAllocatorToSave.setKges(kges);
		heatCostAllocatorToSave.setMeasurement(measurement);
		heatCostAllocatorToSave.setStatus((byte) 0);
		heatCostAllocatorToSave.setTransactionTime(new DateTime().toDate());
		heatCostAllocatorService.save(heatCostAllocatorToSave);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Pay ölçer kaydedildi "));
	}

	public void updateSelectedRoom(Room room) {
		System.out.println("burdayým daire beani");
		setSelectedRoomForHeatCostAllocator(room);
	}

	public List<HeatCostAllocator> getHeatCostAllocators() {
		return heatCostAllocators;
//		// autocompleteString="";
//		System.out.println("heatCostAllocators'u getiriyorum");
//		System.out.println("selected room: " + selectedRoom.getOrderNo());
//		if (passparam == 10) {
//			heatCostAllocators = heatCostAllocatorService
//					.findbyRoom(selectedRoom);
//			autocompleteString = "";
//			selectedRoom.setOrderNo((byte) 0);
//			selectedRoom.setId((short) 0);
//			passparam = 0;
//			return heatCostAllocators;
//		} else {
//			if (selectedRoom.getOrderNo() == 0) {
//				// System.out.println("rooms null geldi");
//				heatCostAllocators = heatCostAllocatorService.findAll();
//				return heatCostAllocators;
//			} else {
//				heatCostAllocators = heatCostAllocatorService
//						.findbyRoom(selectedRoom);
//				return heatCostAllocators;
//			}
//		}

	}

	public HeatCostAllocator getSelectedHeatCostAllocator() {
		return selectedHeatCostAllocator;
	}

	public void setSelectedHeatCostAllocator(
			HeatCostAllocator selectedHeatCostAllocator) {
		this.selectedHeatCostAllocator = selectedHeatCostAllocator;
	}

	public Room getSelectedRoom() {
		return selectedRoom;
	}

	public void setSelectedRoom(Room selectedRoom) {
		this.selectedRoom = selectedRoom;
	}

	public List<String> getRoomMatches() {
		return roomMatches;
	}

	public void setRoomMatches(List<String> roomMatches) {
		this.roomMatches = roomMatches;
	}

	public String getAutocompleteString() {
		return autocompleteString;
	}

	public void setAutocompleteString(String autocompleteString) {
		this.autocompleteString = autocompleteString;
	}

	public Byte getRoomToBindHeatCostAllocator() {
		return roomToBindHeatCostAllocator;
	}

	public void setRoomToBindHeatCostAllocator(Byte roomToBindHeatCostAllocator) {
		this.roomToBindHeatCostAllocator = roomToBindHeatCostAllocator;
	}

	public Room getSelectedRoomForHeatCostAllocator() {
		return selectedRoomForHeatCostAllocator;
	}

	public void setSelectedRoomForHeatCostAllocator(
			Room selectedRoomForHeatCostAllocator) {
		this.selectedRoomForHeatCostAllocator = selectedRoomForHeatCostAllocator;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	public void setHeatCostAllocators(List<HeatCostAllocator> heatCostAllocators) {
		this.heatCostAllocators = heatCostAllocators;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getRadiatorBrand() {
		return radiatorBrand;
	}

	public void setRadiatorBrand(String radiatorBrand) {
		this.radiatorBrand = radiatorBrand;
	}

	public String getRadiatorType() {
		return radiatorType;
	}

	public void setRadiatorType(String radiatorType) {
		this.radiatorType = radiatorType;
	}

	public Double getKges() {
		return kges;
	}

	public void setKges(Double kges) {
		this.kges = kges;
	}

	public int getMeasurement() {
		return measurement;
	}

	public void setMeasurement(int measurement) {
		this.measurement = measurement;
	}

	public RoomService getRoomService() {
		return roomService;
	}

	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}

	public String getFlatAutoCompleteString() {
		return flatAutoCompleteString;
	}

	public void setFlatAutoCompleteString(String flatAutoCompleteString) {
		this.flatAutoCompleteString = flatAutoCompleteString;
	}

	public Flat getSelectedFlat() {
		return selectedFlat;
	}

	public void setSelectedFlat(Flat selectedFlat) {
		this.selectedFlat = selectedFlat;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public String getDistributionLineAutoCompleteString() {
		return distributionLineAutoCompleteString;
	}

	public void setDistributionLineAutoCompleteString(
			String distributionLineAutoCompleteString) {
		this.distributionLineAutoCompleteString = distributionLineAutoCompleteString;
	}

	public List<String> getDistributionLineMatches() {
		return distributionLineMatches;
	}

	public void setDistributionLineMatches(List<String> distributionLineMatches) {
		this.distributionLineMatches = distributionLineMatches;
	}

	public List<DistributionLine> getDistributionLines() {
		return distributionLines;
	}

	public void setDistributionLines(List<DistributionLine> distributionLines) {
		this.distributionLines = distributionLines;
	}

}
