package enersis.envisor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.infinispan.util.concurrent.ConcurrentHashSet;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import enersis.envisor.entity.Door;
import enersis.envisor.entity.DoorType;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Room;
import enersis.envisor.entity.Wall;
import enersis.envisor.entity.WallType;
import enersis.envisor.entity.Window;
import enersis.envisor.entity.WindowType;
import enersis.envisor.service.DoorService;
import enersis.envisor.service.DoorTypeService;
import enersis.envisor.service.RoomService;
import enersis.envisor.service.RoomTypeService;
import enersis.envisor.service.WallService;
import enersis.envisor.service.WallTypeService;
import enersis.envisor.service.WindowService;
import enersis.envisor.service.WindowTypeService;

/**
 * 
 * @author TatvaSoft
 * DragDropFlatBean : JSF Backend Bean to manage dragDropFlat.xhtml page.
 * It is used to update and synchronize dragDropFlat.xhtml page data and backend bean data.
 *
 */


@Component("dragDropRoomBean")
@ViewScoped
@ManagedBean
public class DragDropRoomBean extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = 948429809401969961L;	//Generated Serial version ID.

	/*
	private static final String windowUrl 			= 	"/resources/images/Window.png";	//Window Image Path.
	private static final String doorUrl 			= 	"/resources/images/Door.png";	//Door Image Path.
	*/
	
	private static final String wsgWindowUrl 			= 	"/resources/images/WSG_Window.png";		//	'Wooden Single Glaze' Window Image Path.
	private static final String wsgDoorUrl 			= 	"/resources/images/WSG_Door.png";		//	'Wooden Single Glaze' Door Image Path.
	
	private static final String wdgWindowUrl 			= 	"/resources/images/WDG_Window.png";		//	'Wooden Double Glaze' Window Image Path.
	private static final String wdgDoorUrl 			= 	"/resources/images/WDG_Door.png";		//	'Wooden Double Glaze' Door Image Path.

	private static final String wtWindowUrl 			= 	"/resources/images/WT_Window.png";		//	'Wooden Two' Window Image Path.
	private static final String wtDoorUrl 			= 	"/resources/images/WT_Door.png";		//	'Wooden Two' Door Image Path.

	private static final String psgWindowUrl 			= 	"/resources/images/PSG_Window.png";		//	'PVC Single Glaze' Window Image Path.
	private static final String psgDoorUrl 			= 	"/resources/images/PSG_Door.png";		//	'PVC Single Glaze' Door Image Path.

	private static final String pdgWindowUrl 			= 	"/resources/images/PDG_Window.png";		//	'PVC Single Glaze' Window Image Path.
	private static final String pdgDoorUrl 			= 	"/resources/images/PDG_Door.png";		//	'PVC Single Glaze' Door Image Path.

	private static final String ptWindowUrl 			= 	"/resources/images/PT_Window.png";		//	'PVC Two' Window Image Path.
	private static final String ptDoorUrl 			= 	"/resources/images/PT_Door.png";		//	'PVC Two' Door Image Path.

	private static final String mdWindowUrl 			= 	"/resources/images/MD_Window.png";		//	'Metal Double' Window Image Path.
	private static final String mdDoorUrl 			= 	"/resources/images/MD_Door.png";		//	'Metal Double' Door Image Path.

	private static final String mtWindowUrl 			= 	"/resources/images/MT_Window.png";		//	'Metal Two' Window Image Path.
	private static final String mtDoorUrl 			= 	"/resources/images/MT_Door.png";		//	'Metal Two' Door Image Path.
	
	
	private String roomImageUrl;	//Background Room Image Path.
	private String roomImageUrlWidth;	//Background Room Image Path.
	private String roomImageUrlHeight;	//Background Room Image Path.

	private Flat selectedFlat;	//Selected Flat Entity.
	private List<Room> rooms = null;	//List of Rooms for selectedFlat entity. This list is lazy loading from Flat entity.

	private String selectedRoomId = "";	//Selected Room ID, available from previous page.
	private List<String> selectedRoomsForSaveAs = null;	//Selected Room IDs from dropdown list for 'Copy to...' event.
	
	private Room selectedRoom;	//Selected Room Entity with selectedRoomId.
	
	private Set<Door> doors = null;	//List of Door for selectedRoom entity. This list is used while user click on 'Create Table' button. It will contain all doors(saved and non saved).
	private Set<Window> windows = null;	//List of Window for selectedRoom entity. This list is used while user click on 'Create Table' button. It will contain all windows(saved and non saved).

	private List<Wall> wallsFromDb = null;	//List of Wall from database for selectedRoom entity. This list is lazy loading from Room entity.
	private List<Door> doorsFromDb = null;	//List of Door from database for selectedRoom entity. This list is lazy loading from Room entity.
	private List<Window> windowsFromDb = null;	//List of Window from database for selectedRoom entity. This list is lazy loading from Room entity.
		
	private List<String> deleteddoorDatabaseIdList	= new ArrayList<String>();//List of deleted Door's Id from database.
	private List<String> deletedwindowDatabaseIdList	= new ArrayList<String>();//List of deleted Window's Id from database.

	private boolean isRoomSelected = false;	//Used to check whether room is selected or not for 'Copy to...' event. 
	private boolean isCreateTable = false;	//Used to check whether 'Create Table' button is clicked or not.

	private String hiddenWallsString = "";	//Used to get wallPK_wallName comma separated list on UI
	public String getHiddenWallsString() {
		return hiddenWallsString;
	}

	public void setHiddenWallsString(String hiddenWallsString) {
		this.hiddenWallsString = hiddenWallsString;
	}

	//Following code added to door/window assign to wall start
	private Wall attachToWall;
	private List<Wall> autoCompletedWalls;
	
	public Wall getAttachToWall() {
		return attachToWall;
	}

	public void setAttachToWall(Wall attachToWall) {
		this.attachToWall = attachToWall;
	}

	public List<Wall> getAutoCompletedWalls() {
		return autoCompletedWalls;
	}

	public void setAutoCompletedWalls(List<Wall> autoCompletedWalls) {
		this.autoCompletedWalls = autoCompletedWalls;
	}
	
    public List<Wall> completeWall(String query) {
        List<Wall> allWalls = wallsFromDb;
        return allWalls;
    }
	//Following code added to door/window assign to wall end
	

    @Autowired
	private BreadCrumbBean breadCrumbBean;	//Autowired to BreadCrumbBean using spring DI with IOC, used for bread crumb of DragDrop Projects flow.

	@Autowired
	private DragDropFlatBean dragDropFlatBean;	//Autowired to DragDropFlatBean using spring DI with IOC, used for get data from previous page i.e dragDropFlat.xhtml.

	@Autowired
	private RoomService roomService;	//Autowired to RoomService using spring DI with IOC, used for database operation related to Room entity.

	@Autowired
	private WallService wallService;	//Autowired to WallService using spring DI with IOC, used for database operation related to Wall entity.
	
	@Autowired
	private DoorService doorService;	//Autowired to DoorService using spring DI with IOC, used for database operation related to Door entity.

	@Autowired
	private WindowService windowService;	//Autowired to WindowService using spring DI with IOC, used for database operation related to Window entity.

	@Autowired
	private RoomTypeService roomTypeService;	//Autowired to RoomTypeService using spring DI with IOC, used for database operation related to RoomType entity.

	@Autowired
	private WallTypeService wallTypeService;	//Autowired to WallTypeService using spring DI with IOC, used for database operation related to WallType entity.
	
	@Autowired
	private DoorTypeService doorTypeService;	//Autowired to DoorTypeService using spring DI with IOC, used for database operation related to DoorType entity.

	@Autowired
	private WindowTypeService windowTypeService;	//Autowired to WindowTypeService using spring DI with IOC, used for database operation related to WindowType entity.
	

	/*
	public String getWindowurl() {
		return windowUrl;
	}

	public String getDoorurl() {
		return doorUrl;
	}
	*/
	
	public String getWsgwindowurl() {
		return wsgWindowUrl;
	}

	public String getWsgdoorurl() {
		return wsgDoorUrl;
	}

	public String getWdgwindowurl() {
		return wdgWindowUrl;
	}

	public String getWdgdoorurl() {
		return wdgDoorUrl;
	}

	public String getWtwindowurl() {
		return wtWindowUrl;
	}

	public String getWtdoorurl() {
		return wtDoorUrl;
	}

	public String getPsgwindowurl() {
		return psgWindowUrl;
	}

	public String getPsgdoorurl() {
		return psgDoorUrl;
	}

	public String getPdgwindowurl() {
		return pdgWindowUrl;
	}

	public String getPdgdoorurl() {
		return pdgDoorUrl;
	}

	public String getPtwindowurl() {
		return ptWindowUrl;
	}

	public String getPtdoorurl() {
		return ptDoorUrl;
	}

	public String getMdwindowurl() {
		return mdWindowUrl;
	}

	public String getMddoorurl() {
		return mdDoorUrl;
	}

	public String getMtwindowurl() {
		return mtWindowUrl;
	}

	public String getMtdoorurl() {
		return mtDoorUrl;
	}
	
	
	public String getRoomImageUrl() {
		return roomImageUrl;
	}

	public void setRoomImageUrl(String roomImageUrl) {
		this.roomImageUrl = roomImageUrl;
	}

	public String getRoomImageUrlWidth() {
		return roomImageUrlWidth;
	}

	public void setRoomImageUrlWidth(String roomImageUrlWidth) {
		this.roomImageUrlWidth = roomImageUrlWidth;
	}

	public String getRoomImageUrlHeight() {
		return roomImageUrlHeight;
	}

	public void setRoomImageUrlHeight(String roomImageUrlHeight) {
		this.roomImageUrlHeight = roomImageUrlHeight;
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

	
	public String getSelectedRoomId() {
		return selectedRoomId;
	}

	public void setSelectedRoomId(String selectedRoomId) {
		this.selectedRoomId = selectedRoomId;
	}

	public List<String> getSelectedRoomsForSaveAs() {
		return selectedRoomsForSaveAs;
	}

	public void setSelectedRoomsForSaveAs(List<String> selectedRoomsForSaveAs) {
		this.selectedRoomsForSaveAs = selectedRoomsForSaveAs;
	}
	

	public Room getSelectedRoom() {
		return selectedRoom;
	}

	public void setSelectedRoom(Room selectedRoom) {
		this.selectedRoom = selectedRoom;
	}

	
	public Set<Door> getDoors() {
		return doors;
	}

	public void setDoors(Set<Door> doors) {
		this.doors = doors;
	}

	public Set<Window> getWindows() {
		return windows;
	}

	public void setWindows(Set<Window> windows) {
		this.windows = windows;
	}

	
	public List<Wall> getWallsFromDb() {
		return wallsFromDb;
	}

	public void setWallsFromDb(List<Wall> wallsFromDb) {
		this.wallsFromDb = wallsFromDb;
	}

	public List<Door> getDoorsFromDb() {
		return doorsFromDb;
	}

	public void setDoorsFromDb(List<Door> doorsFromDb) {
		this.doorsFromDb = doorsFromDb;
	}

	public List<Window> getWindowsFromDb() {
		return windowsFromDb;
	}

	public void setWindowsFromDb(List<Window> windowsFromDb) {
		this.windowsFromDb = windowsFromDb;
	}

	
	public List<String> getDeleteddoorDatabaseIdList() {
		return deleteddoorDatabaseIdList;
	}

	public void setDeleteddoorDatabaseIdList(List<String> deleteddoorDatabaseIdList) {
		this.deleteddoorDatabaseIdList = deleteddoorDatabaseIdList;
	}

	public List<String> getDeletedwindowDatabaseIdList() {
		return deletedwindowDatabaseIdList;
	}

	public void setDeletedwindowDatabaseIdList(
			List<String> deletedwindowDatabaseIdList) {
		this.deletedwindowDatabaseIdList = deletedwindowDatabaseIdList;
	}

	
	public boolean getIsRoomSelected() {
		return isRoomSelected;
	}

	public void setIsRoomSelected(boolean isRoomSelected) {
		this.isRoomSelected = isRoomSelected;
	}

	public boolean getIsCreateTable() {
		return isCreateTable;
	}

	public void setIsCreateTable(boolean isCreateTable) {
		this.isCreateTable = isCreateTable;
	}

	
	public BreadCrumbBean getBreadCrumbBean() {
		return breadCrumbBean;
	}

	public void setBreadCrumbBean(BreadCrumbBean breadCrumbBean) {
		this.breadCrumbBean = breadCrumbBean;
	}

	public DragDropFlatBean getDragDropFlatBean() {
		return dragDropFlatBean;
	}

	public void setDragDropFlatBean(DragDropFlatBean dragDropFlatBean) {
		this.dragDropFlatBean = dragDropFlatBean;
	}

	public RoomService getRoomService() {
		return roomService;
	}

	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}

	public WallService getWallService() {
		return wallService;
	}

	public void setWallService(WallService wallService) {
		this.wallService = wallService;
	}

	public DoorService getDoorService() {
		return doorService;
	}

	public void setDoorService(DoorService doorService) {
		this.doorService = doorService;
	}

	public WindowService getWindowService() {
		return windowService;
	}

	public void setWindowService(WindowService windowService) {
		this.windowService = windowService;
	}

	public RoomTypeService getRoomTypeService() {
		return roomTypeService;
	}

	public void setRoomTypeService(RoomTypeService roomTypeService) {
		this.roomTypeService = roomTypeService;
	}

	public WallTypeService getWallTypeService() {
		return wallTypeService;
	}

	public void setWallTypeService(WallTypeService wallTypeService) {
		this.wallTypeService = wallTypeService;
	}

	public DoorTypeService getDoorTypeService() {
		return doorTypeService;
	}

	public void setDoorTypeService(DoorTypeService doorTypeService) {
		this.doorTypeService = doorTypeService;
	}

	public WindowTypeService getWindowTypeService() {
		return windowTypeService;
	}

	public void setWindowTypeService(WindowTypeService windowTypeService) {
		this.windowTypeService = windowTypeService;
	}
	
	/**
	 * On load page method.
	 * check validation and set list of room and load drop down list of room on load of dragDropRoom.xhtml Page.
	 * check validation and load the door,window structure of selected room.  
	 */
	public void onLoadRoom() {

		breadCrumbBean.navigateDragDropRoom();	//Load bread crumb with 'project > building > Floor > Flat > Room' page only.
		
		this.selectedFlat = dragDropFlatBean.getSelectedFlat();
		
		if(selectedFlat != null){
			this.rooms = roomService.findbyFlatWithDragDrop(selectedFlat);
    		
			this.selectedRoomId = dragDropFlatBean.getSelectedRoomId();
			
			if(selectedRoomId != null  &&   !"".equals(selectedRoomId)){
				
				this.selectedRoom = roomService.findById(Integer.valueOf(selectedRoomId));
				
				roomImageUrl = "url('../resources/images/"+selectedRoom.getImageType()+".png')"; 
				if("Rectangle".equals(selectedRoom.getImageType())){
					roomImageUrlWidth = "500px";
					roomImageUrlHeight = "300px";
				} else {
					roomImageUrlWidth = "500px";
					roomImageUrlHeight = "500px";					
				}
				
    	    	if(selectedRoom != null){
    	    		
    	    		doorsFromDb = null;
    	    		windowsFromDb = null;
    	    		
    	    		/*this.selectedRoom = roomService.populateDoorsWindows(selectedRoom);*/
    	    		wallsFromDb = wallService.populateWallsWithCriateria(selectedRoom);
    	    		if(wallsFromDb!=null && !wallsFromDb.isEmpty()){
    	    			
    	    			for (Wall wall : wallsFromDb) {
            	    		List<Door> doors = doorService.populateDoorsWithCriateria(wall);            	    		
            	    		if(doors!=null && !doors.isEmpty()){
            	    			//doorsFromDb = doors;
            	    			if(doorsFromDb==null){
            	    				doorsFromDb = new ArrayList<Door>();
            	    			}
            	    			doorsFromDb.addAll(doors);
            	    		}
            	    		
            	    		List<Window> windows = windowService.populateWindowsWithCriateria(wall);
            	    		if(windows!=null && !windows.isEmpty()){
            	    			//windowsFromDb = windows;
            	    			if(windowsFromDb==null){
            	    				windowsFromDb = new ArrayList<Window>();
            	    			}
            	    			windowsFromDb.addAll(windows);
            	    		}            	    			
						}    	    			
    	    		} else {
    	    			
    	    			String roomType = selectedRoom.getImageType();
    	    			int noOfWall = 0;
    	    			if("Square".equals(roomType) || "Rectangle".equals(roomType))
    	    				noOfWall = 4;
    	    			else if("LShape".indexOf(roomType) != -1)
    	    				noOfWall = 6;
    	    			
    	    			Set<Wall> wallsForSelectedRoom = new ConcurrentHashSet<Wall>();
    	    			for (int i = 0; i < noOfWall; i++) {
							
    	    				Wall wall = new Wall();
							wall.setRoom(selectedRoom);
							
							WallType wallType = wallTypeService.findById(3);
							wall.setWallType(wallType);
							wall.setName("Wall"+(i+1));
							
							wall.setImageType("Wall");
							wall.setImageTop(selectedRoom.getImageTop());
							wall.setImageLeft(selectedRoom.getImageLeft());
							wall.setImageWidth(5);
							wall.setImageHeight(selectedRoom.getImageHeight());			
							wall.setStatus((byte)0);
							wall.setTransactionTime(new DateTime().toDate());
							
							wallService.save(wall);
							wallsForSelectedRoom.add(wall);							
						}
    	    			
    	    			selectedRoom.setWalls(wallsForSelectedRoom);
    	    			roomService.save(selectedRoom);
    	    			
    	    			wallsFromDb = wallService.populateWallsWithCriateria(selectedRoom);
    	    		}
    	    		
    	    		hiddenWallsString = "";
    	    		for (Wall wall : wallsFromDb) {
	    				if("".equals(hiddenWallsString)){
	    					hiddenWallsString = wall.getId()+"_"+wall.getName();
	    				} else {
	    					hiddenWallsString = hiddenWallsString + "," + wall.getId()+"_"+wall.getName();
	    				}
					}
    	    		
    	    	} else {
    	    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page, No Room found with ID="+selectedRoomId+".", ""));
    	    	}        	    	
				
    		} else {
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page. Room Object is not available.", ""));
    		}   
			
    	} else {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page. Room Object is not available.", ""));
    	}    	    	
	}
	
	
	 /**
	  * On click 'Save' button method.
	  * check validation and save Room and its list of doors,windows entities in database.
	  */
	public void saveRoom() {
		if(!isCreateTable){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
		} else {
			
			Map<Wall, Set<Door>> hmWallWithDoors = new HashMap<Wall, Set<Door>>(); 
			Map<Wall, Set<Window>> hmWallWithWindows = new HashMap<Wall, Set<Window>>(); 
			
			if(deleteddoorDatabaseIdList!=null && !deleteddoorDatabaseIdList.isEmpty()){
				for (String deleteddoorDatabaseId : deleteddoorDatabaseIdList) {
					doorService.delete(doorService.findById(Integer.valueOf(deleteddoorDatabaseId)));
				}
			}

			if(deletedwindowDatabaseIdList!=null && !deletedwindowDatabaseIdList.isEmpty()){
				for (String deletedwindowDatabaseId : deletedwindowDatabaseIdList) {
					windowService.delete(windowService.findById(Integer.valueOf(deletedwindowDatabaseId)));
				}
			}

			for (Door door : doors) {
				Wall w1 = door.getWall();
				Set<Door> tmpDoors = null;
				if(hmWallWithDoors.containsKey(w1)){
					tmpDoors = hmWallWithDoors.get(w1);
				} else {
					tmpDoors = new ConcurrentHashSet<Door>();
				}
				tmpDoors.add(door);
				hmWallWithDoors.put(w1, tmpDoors);
				
				doorService.save(door);
			}		
			for (Window window : windows) {
				Wall w1 = window.getWall();
				Set<Window> tmpWindows = null;
				if(hmWallWithWindows.containsKey(w1)){
					tmpWindows = hmWallWithWindows.get(w1);
				} else {
					tmpWindows = new ConcurrentHashSet<Window>();
				}
				tmpWindows.add(window);
				hmWallWithWindows.put(w1, tmpWindows);

				windowService.save(window);
			}		
			
			Iterator<Map.Entry<Wall, Set<Door>>> itrWallWithDoor = hmWallWithDoors.entrySet().iterator();
			while(itrWallWithDoor.hasNext()){
	            Map.Entry<Wall, Set<Door>> wallEntry = itrWallWithDoor.next();
	            Wall wallToBeSave = wallEntry.getKey();
	            Set<Door> doorList = wallEntry.getValue();
	            
	            wallToBeSave.setDoors(doorList);
	            wallService.save(wallToBeSave);	            
	        }			
			if(!doors.isEmpty()){
				doors.clear();
			}

			Iterator<Map.Entry<Wall, Set<Window>>> itrWallWithWindow = hmWallWithWindows.entrySet().iterator();
			while(itrWallWithWindow.hasNext()){
	            Map.Entry<Wall, Set<Window>> wallEntry = itrWallWithWindow.next();
	            Wall wallToBeSave = wallEntry.getKey();
	            Set<Window> windowList = wallEntry.getValue();
	            
	            wallToBeSave.setWindows(windowList);
	            wallService.save(wallToBeSave);	            
	        }
			if(!windows.isEmpty()){
				windows.clear();
			}

			isCreateTable = false;
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Door(s) and/or Window(s) has been saved to "+selectedRoom.getName()+" Room", ""));			
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainForm:selectedRoomDoors");
			RequestContext.getCurrentInstance().update("mainForm:selectedRoomWindows");
		}		
	}
	
	 /**
	  * On click 'Copy to...' button method.
	  * check validation and save Room and its list of Wall entities in database for selected Flat from dropdown list for 'Copy to..' event.
	  */
	public void saveAsRoom() {

		if(selectedRoomsForSaveAs == null || selectedRoomsForSaveAs.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select Room for Copy to...", ""));
			RequestContext.getCurrentInstance().update("msgs");
		/*} else if(selectedRoomId.equals(selectedRoomIdForSaveAs)){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select another Room for Copy to...", ""));
			RequestContext.getCurrentInstance().update("msgs");*/
		} else {
			if(!isCreateTable){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
				RequestContext.getCurrentInstance().update("msgs");
			} else {
				
				String copiedToRoomsName = "";
				for (String selectedRoomIdForSaveAs : selectedRoomsForSaveAs) {
					if(!selectedRoomIdForSaveAs.equals(selectedRoomId)){
						

						Room roomForSaveAs = roomService.findById(Integer.valueOf(selectedRoomIdForSaveAs)); 
						
						roomForSaveAs = roomService.populateWall(roomForSaveAs);								
						for (Wall wall : roomForSaveAs.getWalls()) {
							wallService.delete(wall);
						}
						
						Set<Wall> wallsForRoomForSaveAs = new ConcurrentHashSet<Wall>();
						for (Wall wall : wallsFromDb) {
		    				Wall wl = new Wall();
		    				wl.setRoom(roomForSaveAs);
							wl.setWallType(wall.getWallType());
		    				wl.setName(wall.getName());
							
		    				wl.setImageType(wall.getImageType());
		    				wl.setImageTop(wall.getImageTop());
		    				wl.setImageLeft(wall.getImageLeft());
		    				wl.setImageWidth(wall.getImageWidth());
		    				wl.setImageHeight(wall.getImageHeight());			
		    				wl.setStatus((byte)0);
		    				wl.setTransactionTime(new DateTime().toDate());
							
							wallService.save(wl);
							wallsForRoomForSaveAs.add(wall);												
						}
						
						roomForSaveAs.setWalls(wallsForRoomForSaveAs);
		    			roomService.save(roomForSaveAs);
		    			
		    			if("".equals(copiedToRoomsName))
							copiedToRoomsName = roomForSaveAs.getName();
						else 
							copiedToRoomsName = copiedToRoomsName + ", " + roomForSaveAs.getName();

		    			List<Wall> wallsFromDbForRoomForSaveAs = wallService.populateWallsWithCriateria(roomForSaveAs);
		    			
		    			Map<Wall, Set<Door>> hmWallWithDoorsForSaveAs = new HashMap<Wall, Set<Door>>(); 
		    			Map<Wall, Set<Window>> hmWallWithWindowsForSaveAs = new HashMap<Wall, Set<Window>>(); 

		    			for (Door door : doors) {
							Door dr = new Door();
							
							dr.setDoorType(door.getDoorType());
							dr.setName(door.getName());
		    				dr.setImageType(door.getImageType());
		    				dr.setImageTop(door.getImageTop());
		    				dr.setImageLeft(door.getImageLeft());
		    				dr.setImageWidth(door.getImageWidth());
		    				dr.setImageHeight(door.getImageHeight());			
		    				dr.setStatus((byte)0);
		    				dr.setTransactionTime(new DateTime().toDate());					
							
		    				Wall wallOfDoor = null;
							for (Wall wall : wallsFromDbForRoomForSaveAs) {
								if(wall.getName().equals(door.getWall().getName())){
									wallOfDoor = wall;
									dr.setWall(wallOfDoor);							
									break;
								}
							}
							
							Set<Door> tmpDoors = null;
							if(hmWallWithDoorsForSaveAs.containsKey(wallOfDoor)){
								tmpDoors = hmWallWithDoorsForSaveAs.get(wallOfDoor);
							} else {
								tmpDoors = new ConcurrentHashSet<Door>();
							}
							tmpDoors.add(dr);
							hmWallWithDoorsForSaveAs.put(wallOfDoor, tmpDoors);

							doorService.save(dr);					
						}
		    			
		    			for (Window window : windows) {
							Window wndw = new Window();
							
							wndw.setWindowType(window.getWindowType());
							wndw.setName(window.getName());
							wndw.setImageType(window.getImageType());
							wndw.setImageTop(window.getImageTop());
							wndw.setImageLeft(window.getImageLeft());
							wndw.setImageWidth(window.getImageWidth());
							wndw.setImageHeight(window.getImageHeight());			
							wndw.setStatus((byte)0);
							wndw.setTransactionTime(new DateTime().toDate());					
							
		    				Wall wallOfWindow = null;
							for (Wall wall : wallsFromDbForRoomForSaveAs) {
								if(wall.getName().equals(window.getWall().getName())){
									wallOfWindow = wall;
									wndw.setWall(wallOfWindow);							
									break;
								}
							}
							
							Set<Window> tmpWindows = null;
							if(hmWallWithWindowsForSaveAs.containsKey(wallOfWindow)){
								tmpWindows = hmWallWithWindowsForSaveAs.get(wallOfWindow);
							} else {
								tmpWindows = new ConcurrentHashSet<Window>();
							}
							tmpWindows.add(wndw);
							hmWallWithWindowsForSaveAs.put(wallOfWindow, tmpWindows);

							windowService.save(wndw);					
						}

		    			Iterator<Map.Entry<Wall, Set<Door>>> itrWallWithDoor = hmWallWithDoorsForSaveAs.entrySet().iterator();
		    			while(itrWallWithDoor.hasNext()){
		    	            Map.Entry<Wall, Set<Door>> wallEntry = itrWallWithDoor.next();
		    	            Wall wallToBeSave = wallEntry.getKey();
		    	            Set<Door> doorList = wallEntry.getValue();
		    	            
		    	            wallToBeSave.setDoors(doorList);
		    	            wallService.save(wallToBeSave);	            
		    	        }			

		    			Iterator<Map.Entry<Wall, Set<Window>>> itrWallWithWindow = hmWallWithWindowsForSaveAs.entrySet().iterator();
		    			while(itrWallWithWindow.hasNext()){
		    	            Map.Entry<Wall, Set<Window>> wallEntry = itrWallWithWindow.next();
		    	            Wall wallToBeSave = wallEntry.getKey();
		    	            Set<Window> windowList = wallEntry.getValue();
		    	            
		    	            wallToBeSave.setWindows(windowList);
		    	            wallService.save(wallToBeSave);	            
		    	        }			

					}
				}
				if(!selectedRoomsForSaveAs.isEmpty())
					selectedRoomsForSaveAs.clear();
				
				
    			if(!doors.isEmpty()){
    				doors.clear();
    			}
    			
    			if(!windows.isEmpty()){
    				windows.clear();
    			}

    			isCreateTable = false;
				RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Door(s) and/or Window(s) has been copied to "+copiedToRoomsName+" Rooms.", ""));
				RequestContext.getCurrentInstance().update("msgs");
				RequestContext.getCurrentInstance().update("mainForm:selectedRoomDoors");
				RequestContext.getCurrentInstance().update("mainForm:selectedRoomWindows");
			}
		}
	}
	
	
	/**
	  * On click 'Create Table' button method.
	  * This method is called using remote command of jsf function to set Door/Window entity data.
	  * This method is called as many times as many doors/windows selected. 
	  */
	@SuppressWarnings("unchecked")
	public void setWallsList() {
		
		String doorWindowList = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("doorWindowList");
		
		if(doorWindowList != null && !"".equals(doorWindowList)){
			
			JSONParser parser = new JSONParser();
			try{
				
				JSONArray arrayOfDoorOrWindow = (JSONArray) parser.parse(doorWindowList);
				
				JSONObject jsonDoorOrWindowObj = null;
				String imageDivId = "";
				String width = "";
				String height = "";
				String top = "";
				String left = "";
				String imageType = "";
				String doorOrWindowId = "";
				String doorOrWindowName = "";
				String doorOrWindowType = "";
				String assignedWallId = "";
				
				Iterator<JSONObject> iterator = arrayOfDoorOrWindow.iterator();
				while (iterator.hasNext()) {
					
					jsonDoorOrWindowObj = (JSONObject) iterator.next();

					imageDivId = jsonDoorOrWindowObj.get("imageDivId").toString();
					width = jsonDoorOrWindowObj.get("width").toString();
					height = jsonDoorOrWindowObj.get("height").toString();
					top = jsonDoorOrWindowObj.get("top").toString();
					left = jsonDoorOrWindowObj.get("left").toString();
					imageType = jsonDoorOrWindowObj.get("imageType").toString();

					doorOrWindowId = jsonDoorOrWindowObj.get("hiddenId").toString();
					doorOrWindowName = jsonDoorOrWindowObj.get("imgName").toString().trim();
					doorOrWindowType = jsonDoorOrWindowObj.get("doorOrWindowType").toString().trim();
					
					assignedWallId = jsonDoorOrWindowObj.get("assignedWallId").toString().trim();					
					Wall wall = wallService.findById(Integer.valueOf(assignedWallId));

					if("Door".equals(imageType)){
						Door door = new Door();

						if(doorOrWindowId!=null && !"".equals(doorOrWindowId)){			
							door.setId(Integer.valueOf(doorOrWindowId));
						}
						
						door.setWall(wall);
						
						//DoorType doorType = doorTypeService.findById(1);
						DoorType doorType = doorTypeService.findByType(doorOrWindowType);
						door.setDoorType(doorType);						
						
						if(imageDivId!=null && !"".equals(imageDivId)){
							door.setImageDivId(imageDivId);			
						}
						if(doorOrWindowName!=null && !"".equals(doorOrWindowName)){
							door.setName(doorOrWindowName);			
						}
						if(imageType!=null && !"".equals(imageType)){
							door.setImageType(imageType);			
						}
						if(top!=null && !"".equals(top)){
							//top = top.substring(0,top.indexOf("px"));			
							//top = top.indexOf(".")!=-1 ? top.substring(0,top.indexOf(".")) : top;			
							door.setImageTop(Integer.valueOf(top));
						}
						if(left!=null && !"".equals(left)){
							//left = left.substring(0,left.indexOf("px"));			
							//left = left.indexOf(".")!=-1 ? left.substring(0,left.indexOf(".")) : left;			
							door.setImageLeft(Integer.valueOf(left));
						}
						if(width!=null && !"".equals(width)){			
							//width = width.substring(0,width.indexOf("px"));			
							//width = width.indexOf(".")!=-1 ? width.substring(0,width.indexOf(".")) : width;
							door.setImageWidth(Integer.valueOf(width));
						}
						if(height!=null && !"".equals(height)){
							//height = height.substring(0,height.indexOf("px"));			
							//height = height.indexOf(".")!=-1 ? height.substring(0,height.indexOf(".")) : height;			
							door.setImageHeight(Integer.valueOf(height));
						}
						
						door.setStatus((byte)0);
						door.setTransactionTime(new DateTime().toDate());

						if(doors == null){
							doors = new ConcurrentHashSet<Door>(); 
						} else {
							for (Door dr : doors) {
								String oldDoorImageDivId = dr.getImageDivId();
								if(oldDoorImageDivId!=null && !"".equals(oldDoorImageDivId)){
									if(oldDoorImageDivId.equals(door.getImageDivId())){
										doors.remove(dr);
										break;
									}					
								}
							}
						}					
						doors.add(door);

					} else if("Window".equals(imageType)){
						Window window = new Window();

						if(doorOrWindowId!=null && !"".equals(doorOrWindowId)){			
							window.setId(Integer.valueOf(doorOrWindowId));
						}

						window.setWall(wall);

						//WindowType windowType = windowTypeService.findById(1);
						WindowType windowType = windowTypeService.findByType(doorOrWindowType);
						window.setWindowType(windowType);						

						if(imageDivId!=null && !"".equals(imageDivId)){
							window.setImageDivId(imageDivId);			
						}
						if(doorOrWindowName!=null && !"".equals(doorOrWindowName)){
							window.setName(doorOrWindowName);			
						}
						if(imageType!=null && !"".equals(imageType)){
							window.setImageType(imageType);			
						}
						if(top!=null && !"".equals(top)){
							//top = top.substring(0,top.indexOf("px"));			
							//top = top.indexOf(".")!=-1 ? top.substring(0,top.indexOf(".")) : top;			
							window.setImageTop(Integer.valueOf(top));
						}
						if(left!=null && !"".equals(left)){
							//left = left.substring(0,left.indexOf("px"));			
							//left = left.indexOf(".")!=-1 ? left.substring(0,left.indexOf(".")) : left;			
							window.setImageLeft(Integer.valueOf(left));
						}
						if(width!=null && !"".equals(width)){			
							//width = width.substring(0,width.indexOf("px"));			
							//width = width.indexOf(".")!=-1 ? width.substring(0,width.indexOf(".")) : width;
							window.setImageWidth(Integer.valueOf(width));
						}
						if(height!=null && !"".equals(height)){
							//height = height.substring(0,height.indexOf("px"));			
							//height = height.indexOf(".")!=-1 ? height.substring(0,height.indexOf(".")) : height;			
							window.setImageHeight(Integer.valueOf(height));
						}
						
						window.setStatus((byte)0);
						window.setTransactionTime(new DateTime().toDate());

						if(windows == null){
							windows = new ConcurrentHashSet<Window>(); 
						} else {
							for (Window wndw : windows) {
								String oldWindowImageDivId = wndw.getImageDivId();
								if(oldWindowImageDivId!=null && !"".equals(oldWindowImageDivId)){
									if(oldWindowImageDivId.equals(window.getImageDivId())){
										windows.remove(wndw);
										break;
									}					
								}
							}
						}					
						windows.add(window);						
					}
					
				}
				isCreateTable = true;	
			} catch (ParseException e) {
				e.printStackTrace();
			}			
		}
		
	}

	/**
	  * On click 'Save' button method.
	  * This method is called using remote command of jsf function to set isCreateTable variable.
	 */
	public void setIsCreateTableVar(){
		
		String hiddenCreateTableVal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("hiddenCreateTableVal").toString();
		
		if(hiddenCreateTableVal != null && !"".equals(hiddenCreateTableVal)){
			isCreateTable = Boolean.valueOf(hiddenCreateTableVal);
			RequestContext.getCurrentInstance().update("hiddenCreateTable");
		}
	}

	
	/**
	  * On press 'Delete' button method.
	  * This method is called using remote command of jsf function to delete temporary created door from list of doors.
	  * Also delete from database if door id is available. (This is just a soft delete) 
	 */
	public void deleteTmpDoorFromRoom(){
		String dltTmpDoorDivId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dltTmpDoorDivId");
		
		if(dltTmpDoorDivId != null && !"".equals(dltTmpDoorDivId) && doors != null && !doors.isEmpty()){
			Iterator<Door> doorItr = doors.iterator();
			while(doorItr.hasNext()){
				Door dr = doorItr.next();
				String drImgDivId = dr.getImageDivId();
				if(dltTmpDoorDivId.equals(drImgDivId)){					
					doors.remove(dr);					
				}
			}		
		}	
		
		String deleteddoorDatabaseId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deleteddoorDatabaseId");		
		if(deleteddoorDatabaseId != null && !"".equals(deleteddoorDatabaseId)){
			deleteddoorDatabaseIdList.add(deleteddoorDatabaseId);
			//doorService.delete(doorService.findById(Integer.valueOf(deleteddoorDatabaseId)));
		}
	}

	/**
	  * On press 'Delete' button method.
	  * This method is called using remote command of jsf function to delete temporary created window from list of windows.
	  * Also delete from database if window id is available. (This is just a soft delete) 
	 */
	public void deleteTmpWindowFromRoom(){
		String dltTmpWindowDivId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dltTmpWindowDivId");
		
		if(dltTmpWindowDivId != null && !"".equals(dltTmpWindowDivId) && windows != null && !windows.isEmpty()){
			Iterator<Window> windowItr = windows.iterator();
			while(windowItr.hasNext()){
				Window wndw = windowItr.next();
				String wndwImgDivId = wndw.getImageDivId();
				if(dltTmpWindowDivId.equals(wndwImgDivId)){					
					windows.remove(wndw);					
				}
			}		
		}	
		
		String deletedwindowDatabaseId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deletedwindowDatabaseId");		
		if(deletedwindowDatabaseId != null && !"".equals(deletedwindowDatabaseId)){
			deletedwindowDatabaseIdList.add(deletedwindowDatabaseId);
			//windowService.delete(windowService.findById(Integer.valueOf(deletedwindowDatabaseId)));
		}
	}
}
