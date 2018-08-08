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

import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.Room;
import enersis.envisor.service.BuildingService;
import enersis.envisor.service.FlatService;
import enersis.envisor.service.HeatCostAllocatorService;
import enersis.envisor.service.RoomService;

@Component("roomBean")
@ViewScoped
//@Scope("view")
// @SessionScoped
// @RequestScoped
@ManagedBean
public class RoomBean extends AbstractBacking implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8305933681751603518L;

	private List<Room> rooms = new ArrayList<Room>();
	private Room selectedRoom = new Room();
	private Flat selectedFlat = new Flat();
	private List<String> flatMatches = new ArrayList<String>();
	private String autocompleteString = "";
	private Byte flatToBindRoom;
	private Flat selectedFlatForRoom;
	private int passparam = 0;
	
	private Byte orderNo;
	private Short baseArea;
	private Short windowArea;
	private Byte glassTypeId;
	private Byte wallTypeId;
	
	

	@Autowired
	private HeatCostAllocatorService heatCostAllocatorService;
	
	
	@Autowired
	private BuildingService buildingService;

	@Autowired
	private FlatService flatService;

	@Autowired
	private RoomService roomService;

	@PostConstruct
	public void postConstruct() {
		rooms = roomService.findAll();
	}

	public void deleteWithRelations() {

		List<HeatCostAllocator> heatCostAllocators= heatCostAllocatorService.findbyRoom(selectedRoom);
		
		for (HeatCostAllocator heatCostAllocator : heatCostAllocators) {
			heatCostAllocatorService.delete(heatCostAllocator);
		}
		roomService.delete(selectedRoom);
		// Oda Silindi growlu
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Oda, tüm alt deðerleriyle birlikte silindi"));
	}

	public List<String> autoComplete(String query) {
		flatMatches.clear();
		List<Flat> aCFlats = flatService.findAll();
		for (Flat flat : aCFlats) {
			if (flat.getNo().contains(query)  ) {
				flatMatches.add(flat.getNo() );
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
			rooms = roomService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedFlat = flatService.findByFlatNo(Byte.parseByte(autocompleteString) ).get(0);
			System.out.println("auto complete sonrasý seçilmiþ bina"
					+ selectedFlat.getNo());
			rooms = roomService.findbyFlat(selectedFlat);
		}

	}

	public void bindRoom() {
		Room roomToSave = new Room();
//		if (forDistline.getProjectCode() == null) {
//			distributionLine.setProject(selectedProjectforDistLine);
//			;
//		} else {
//			distributionLine.setProject(forDistline);
//		}
		roomToSave.setFlat(flatService.findByFlatNo(flatToBindRoom).get(0));
		roomToSave.setOrderNo(orderNo);
		roomToSave.setBaseArea(baseArea);
		roomToSave.setWindowArea(windowArea);
		roomToSave.setGlassTypeId(glassTypeId);
		roomToSave.setWallTypeId(wallTypeId);
		roomToSave.setStatus((byte) 0);
		roomToSave.setTransactionTime(new DateTime().toDate());
		roomService.save(roomToSave);
		System.out.println("id:"+ roomToSave.getId());
	}

	public void updateSelectedFlat(Flat flat) {
		System.out.println("burdayým daire beani");
		setSelectedFlatForRoom(flat);
	}

	public List<Room> getRooms() {

		// autocompleteString="";
		System.out.println("rooms'u getiriyorum");
		System.out.println("selected flat: " + selectedFlat.getNo());
		if (passparam == 10) {
			rooms = roomService.findbyFlat(selectedFlat);
			autocompleteString = "";
			selectedFlat.setNo("0");
			selectedFlat.setId((short) 0);
			passparam = 0;
			return rooms;
		} else {
			if (selectedFlat.getNo() == "0") {
//				System.out.println("flats null geldi");
				rooms = roomService.findAll();
				return rooms;
			} else {
				rooms = roomService.findbyFlat(selectedFlat);
				return rooms;
			}
		}
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

	public List<String> getFlatMatches() {
		return flatMatches;
	}

	public void setFlatMatches(List<String> flatMatches) {
		this.flatMatches = flatMatches;
	}

	public String getAutocompleteString() {
		return autocompleteString;
	}

	public void setAutocompleteString(String autocompleteString) {
		this.autocompleteString = autocompleteString;
	}

	public Byte getFlatToBindRoom() {
		return flatToBindRoom;
	}

	public void setFlatToBindRoom(Byte flatToBindRoom) {
		this.flatToBindRoom = flatToBindRoom;
	}

	public Flat getSelectedFlatForRoom() {
		return selectedFlatForRoom;
	}

	public void setSelectedFlatForRoom(Flat selectedFlatForRoom) {
		this.selectedFlatForRoom = selectedFlatForRoom;
	}

	public Byte getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Byte orderNo) {
		this.orderNo = orderNo;
	}

	public Short getBaseArea() {
		return baseArea;
	}

	public void setBaseArea(Short baseArea) {
		this.baseArea = baseArea;
	}

	public Short getWindowArea() {
		return windowArea;
	}

	public void setWindowArea(Short windowArea) {
		this.windowArea = windowArea;
	}

	public Byte getGlassTypeId() {
		return glassTypeId;
	}

	public void setGlassTypeId(Byte glassTypeId) {
		this.glassTypeId = glassTypeId;
	}

	public Byte getWallTypeId() {
		return wallTypeId;
	}

	public void setWallTypeId(Byte wallTypeId) {
		this.wallTypeId = wallTypeId;
	}

	public BuildingService getBuildingService() {
		return buildingService;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
}
