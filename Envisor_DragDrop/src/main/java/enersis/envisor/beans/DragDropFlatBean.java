package enersis.envisor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Room;
import enersis.envisor.entity.RoomType;
import enersis.envisor.entity.Wall;
import enersis.envisor.entity.Window;
import enersis.envisor.service.DoorService;
import enersis.envisor.service.DoorTypeService;
import enersis.envisor.service.FlatService;
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


@Component("dragDropFlatBean")
@ViewScoped
@ManagedBean
public class DragDropFlatBean extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = -4785741274426849862L;	//Generated Serial version ID.

	private static final String rectangleShapeUrl 	= 	"/resources/images/R.png";	//Rectangle shape room Image Path.
	private static final String squareShapeUrl 		= 	"/resources/images/S.png";	//Square shape room Image Path.
	/*private static final String lShapeUrl 			= 	"/resources/images/L-TRANS.gif";*/	//L shape room Image Path.
	private static final String lShapeRightTopUrl 			= 	"/resources/images/L-TRANS_RightTop.png";	//L shape room Image Path.
	private static final String lShapeRightBottomUrl 			= 	"/resources/images/L-TRANS_RightBottom.png";	//L shape room Image Path.
	private static final String lShapeLeftTopUrl 			= 	"/resources/images/L-TRANS_LeftTop.png";	//L shape room Image Path.
	private static final String lShapeLeftBottomUrl 			= 	"/resources/images/L-TRANS_LeftBottom.png";	//L shape room Image Path.
	
	private Floor selectedFloor;	//Selected Building Entity.
	private List<Flat> flats = null;	//List of Floor for selectedBuilding entity. This list is lazy loading from Building entity.

	private String selectedFlatId = "";	//Selected Floor ID, available from previous page.
	private List<String> selectedFlatsForSaveAs = null;	//Selected Flat IDs from dropdown list for 'Copy to...' event.
	
	private Flat selectedFlat;	//Selected Floor Entity with selectedFloorId.
	
	private Set<Room> rooms = null;	//List of Room for selectedFlat entity. This list is used while user click on 'Create Table' button. It will contain all rooms(saved and non saved).

	private List<Room> roomsFromDb = null;	//List of Room from database for selectedFlat entity. This list is lazy loading from Flat entity.
	
	private List<String> deletedroomDatabaseIdList	= new ArrayList<String>();//List of deleted Room's Id from database.
		
	private String selectedRoomId = "";	//Selected flat Id used for sending selected flat ID to next page i.e dragDropFlat.xhtml

	private boolean isFlatSelected = false;	//Used to check whether floor is selected or not for 'Copy to...' event. 
	private boolean isCreateTable = false;	//Used to check whether 'Create Table' button is clicked or not.

	@Autowired
	private BreadCrumbBean breadCrumbBean;	//Autowired to BreadCrumbBean using spring DI with IOC, used for bread crumb of DragDrop Projects flow.

	@Autowired
	private DragDropFloorBean dragDropFloorBean;	//Autowired to DragDropFloorBean using spring DI with IOC, used for get data from previous page i.e dragDropFloor.xhtml.

	@Autowired
    private FlatService flatService;	//Autowired to FlatService using spring DI with IOC, used for database operation related to Flat entity.
	
	@Autowired
	private RoomService roomService;	//Autowired to RoomService using spring DI with IOC, used for database operation related to Room entity.
	
	@Autowired
	private RoomTypeService roomTypeService;	//Autowired to RoomTypeService using spring DI with IOC, used for database operation related to RoomType entity.
	
	@Autowired
	private WallService wallService;	//Autowired to WallService using spring DI with IOC, used for database operation related to Wall entity.
	
	@Autowired
	private DoorService doorService;	//Autowired to DoorService using spring DI with IOC, used for database operation related to Door entity.

	@Autowired
	private WindowService windowService;	//Autowired to WindowService using spring DI with IOC, used for database operation related to Window entity.

	@Autowired
	private WallTypeService wallTypeService;	//Autowired to WallTypeService using spring DI with IOC, used for database operation related to WallType entity.
	
	@Autowired
	private DoorTypeService doorTypeService;	//Autowired to DoorTypeService using spring DI with IOC, used for database operation related to DoorType entity.

	@Autowired
	private WindowTypeService windowTypeService;	//Autowired to WindowTypeService using spring DI with IOC, used for database operation related to WindowType entity.


	private String hiddenRoomTypesString = "";	//Used to get RoomType.PK_Type comma separated list on UI
	private List<RoomType> roomTypeList = null;

	public String getHiddenRoomTypesString() {
		return hiddenRoomTypesString;
	}
	public void setHiddenRoomTypesString(String hiddenRoomTypesString) {
		this.hiddenRoomTypesString = hiddenRoomTypesString;
	}
	
	public List<RoomType> getRoomTypeList() {
		return roomTypeList;
	}
	public void setRoomTypeList(List<RoomType> roomTypeList) {
		this.roomTypeList = roomTypeList;
	}
	

	/*public String getLshapeurl() {
		return lShapeUrl;
	}*/
	
	public String getLshaperighttopurl() {
		return lShapeRightTopUrl;
	}
	public String getLshaperightbottomurl() {
		return lShapeRightBottomUrl;
	}
	public String getLshapelefttopurl() {
		return lShapeLeftTopUrl;
	}
	public String getLshapeleftbottomurl() {
		return lShapeLeftBottomUrl;
	}
	
	public String getSquareshapeurl() {
		return squareShapeUrl;
	}
	public String getRectangleshapeurl() {
		return rectangleShapeUrl;
	}
	
	
	public Floor getSelectedFloor() {
		return selectedFloor;
	}
	public void setSelectedFloor(Floor selectedFloor) {
		this.selectedFloor = selectedFloor;
	}
	public List<Flat> getFlats() {
		return flats;
	}
	public void setFlats(List<Flat> flats) {
		this.flats = flats;
	}
	
	
	public String getSelectedFlatId() {
		return selectedFlatId;
	}
	public void setSelectedFlatId(String selectedFlatId) {
		this.selectedFlatId = selectedFlatId;
	}
	public List<String> getSelectedFlatsForSaveAs() {
		return selectedFlatsForSaveAs;
	}
	public void setSelectedFlatsForSaveAs(List<String> selectedFlatsForSaveAs) {
		this.selectedFlatsForSaveAs = selectedFlatsForSaveAs;
	}
	
	
	public Flat getSelectedFlat() {
		return selectedFlat;
	}
	public void setSelectedFlat(Flat selectedFlat) {
		this.selectedFlat = selectedFlat;
	}
	public Set<Room> getRooms() {
		return rooms;
	}
	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}
	
	
	public List<Room> getRoomsFromDb() {
		return roomsFromDb;
	}
	public void setRoomsFromDb(List<Room> roomsFromDb) {
		this.roomsFromDb = roomsFromDb;
	}


	public List<String> getDeletedroomDatabaseIdList() {
		return deletedroomDatabaseIdList;
	}

	public void setDeletedroomDatabaseIdList(List<String> deletedroomDatabaseIdList) {
		this.deletedroomDatabaseIdList = deletedroomDatabaseIdList;
	}

	
	public String getSelectedRoomId() {
		return selectedRoomId;
	}
	public void setSelectedRoomId(String selectedRoomId) {
		this.selectedRoomId = selectedRoomId;
	}

	
	public boolean getIsFlatSelected() {
		return isFlatSelected;
	}
	public void setIsFlatSelected(boolean isFlatSelected) {
		this.isFlatSelected = isFlatSelected;
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

	
	public DragDropFloorBean getDragDropFloorBean() {
		return dragDropFloorBean;
	}
	
	public void setDragDropFloorBean(DragDropFloorBean dragDropFloorBean) {
		this.dragDropFloorBean = dragDropFloorBean;
	}
	
	
	public FlatService getFlatService() {
		return flatService;
	}
	public void setFlatService(FlatService flatService) {
		this.flatService = flatService;
	}
	public RoomService getRoomService() {
		return roomService;
	}
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}
	
	public RoomTypeService getRoomTypeService() {
		return roomTypeService;
	}
	public void setRoomTypeService(RoomTypeService roomTypeService) {
		this.roomTypeService = roomTypeService;
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
	 * check validation and set list of flat and load drop down list of flat on load of dragDropFlat.xhtml Page.
	 * check validation and load the room structure of selected flat.  
	 */
	public void onLoadFlat() {
		
		breadCrumbBean.navigateDragDropFlat();	//Load bread crumb with 'project > building > Floor > Flat' page only.
		
		this.selectedFloor = dragDropFloorBean.getSelectedFloor();
		
		if(selectedFloor != null){
			this.flats = flatService.findbyFloorWithDragDrop(selectedFloor);
    		
			this.selectedFlatId = dragDropFloorBean.getSelectedFlatId();
			
			if(selectedFlatId != null  &&   !"".equals(selectedFlatId)){
				
				this.selectedFlat = flatService.findById(Integer.valueOf(selectedFlatId));
    	    	
    	    	if(selectedFlat != null){        	    	
    	    		/*this.selectedFlat = flatService.populateRooms(selectedFlat);*/
    	    		roomsFromDb = roomService.populateRoomsWithCriateria(selectedFlat);
    	    		
    	    		if(roomTypeList==null){    	    			
    	    			roomTypeList = roomTypeService.findAll();    	    			
    	    		}
    	    		hiddenRoomTypesString = "";
    	    		for (RoomType roomType : roomTypeList) {
	    				if("".equals(hiddenRoomTypesString)){
	    					hiddenRoomTypesString = roomType.getId()+"_"+roomType.getType();
	    				} else {
	    					hiddenRoomTypesString = hiddenRoomTypesString + "," + roomType.getId()+"_"+roomType.getType();
	    				}
					}    	    			    	    		

    	    	} else {
    	    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page, No Flat found with ID="+selectedFlatId+".", ""));
    	    	}        	    	
				
    		} else {
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page. Flat Object is not available.", ""));
    		}   
			
    	} else {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page. Floor Object is not available.", ""));
    	}    	    	
	}
	
	 /**
	  * On click 'Save' button method.
	  * check validation and save Flat and its list of Room entities in database.
	  */
	public void saveFlat() {
		if(!isCreateTable){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
		} else {

			if(deletedroomDatabaseIdList!=null && !deletedroomDatabaseIdList.isEmpty()){
				for (String deletedroomDatabaseId : deletedroomDatabaseIdList) {
					roomService.delete(roomService.findById(Integer.valueOf(deletedroomDatabaseId)));
				}
			}

			for (Room room : rooms) {
				System.out.println(room);
				roomService.save(room);				
			}		
			
			selectedFlat.setRooms(rooms);
			flatService.save(selectedFlat);
			selectedFlat = flatService.populateRooms(selectedFlat);
			/*rooms = flatService.populateRooms(selectedFlat).getRooms();*/
			if(!rooms.isEmpty()){
				rooms.clear();
			}
			isCreateTable = false;
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rooms has been saved to "+selectedFlat.getName()+" Flat", ""));			
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainForm:selectedFlatRooms");
		}
	}
	
	 /**
	  * On click 'Copy to...' button method.
	  * check validation and save Flat and its list of Room entities in database for selected Flat from dropdown list for 'Copy to..' event.
	  */
	public void saveAsFlat() {
		if(selectedFlatsForSaveAs == null || selectedFlatsForSaveAs.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select Flat for Copy to...", ""));
			RequestContext.getCurrentInstance().update("msgs");
		/*} else if(selectedFlatId.equals(selectedFlatIdForSaveAs)){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select another Flat for Copy to...", ""));
			RequestContext.getCurrentInstance().update("msgs");*/
		} else {
			if(!isCreateTable){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
				RequestContext.getCurrentInstance().update("msgs");
			} else {
				
				String copiedToFlatsName = "";
				for (String selectedFlatIdForSaveAs : selectedFlatsForSaveAs) {
					if(!selectedFlatIdForSaveAs.equals(selectedFlatId)){
						
						Flat flatForSaveAs = flatService.findById(Integer.valueOf(selectedFlatIdForSaveAs)); 
						
						flatForSaveAs = flatService.populateRooms(flatForSaveAs);
						
						for (Room room : flatForSaveAs.getRooms()) {
							room = roomService.populateWall(room);
							
							for (Wall wall : room.getWalls()) {
								wall = wallService.populateDoorWindow(wall);
								
								for (Door door : wall.getDoors()) {									
									doorService.delete(door);
								}
								
								for (Window window : wall.getWindows()) {									
									windowService.delete(window);
								}

								wallService.delete(wall);
							}
							
							roomService.delete(room);
						}
						
						Set<Room> roomsForFlatForSaveAs = new ConcurrentHashSet<Room>();
						for (Room room : rooms) {
							
							room = roomService.populateWall(room);
							
							if(room.getStatus() == 0){
								Room rm = new Room();
								rm.setFlat(flatForSaveAs);
								rm.setOrderNo(room.getOrderNo());
								rm.setName(room.getName());
								rm.setImageType(room.getImageType());
								rm.setImageTop(room.getImageTop());
								rm.setImageLeft(room.getImageLeft());
								rm.setImageWidth(room.getImageWidth());
								rm.setImageHeight(room.getImageHeight());			
								rm.setStatus(room.getStatus());
								rm.setTransactionTime(new DateTime().toDate());
								
								//rm.setRoomType(roomType);
								
								/*
								Set<Wall> walls = new ConcurrentHashSet<Wall>();
								for (Wall wall : room.getWalls()) {
									
									wall = wallService.populateDoorWindow(wall);
									
									if(wall.getStatus() == 0){
										Wall wl = new Wall();
										wl.setName(wall.getName());
										wl.setImageType(wall.getImageType());
										wl.setImageTop(wall.getImageTop());
										wl.setImageLeft(wall.getImageLeft());
										wl.setImageWidth(wall.getImageWidth());
										wl.setImageHeight(wall.getImageHeight());			
										wl.setStatus(wall.getStatus());
										wl.setTransactionTime(new DateTime().toDate());
										
										Set<Door> doors = new ConcurrentHashSet<Door>();
										Set<Window> windows = new ConcurrentHashSet<Window>();
										
										for (Door door : wall.getDoors()) {		
											
											if(door.getStatus() == 0){
												Door d = new Door();
												d.setName(door.getName());
												d.setImageType(door.getImageType());
												d.setImageTop(door.getImageTop());
												d.setImageLeft(door.getImageLeft());
												d.setImageWidth(door.getImageWidth());
												d.setImageHeight(door.getImageHeight());			
												d.setStatus(door.getStatus());
												d.setTransactionTime(new DateTime().toDate());
												
												d.setDoorType(door.getDoorType());
												
												doorService.save(d);
												doors.add(d);												
											}											
										}
										
										for (Window window : wall.getWindows()) {
											
											if(window.getStatus() == 0){
												Window w = new Window();
												w.setName(window.getName());
												w.setImageType(window.getImageType());
												w.setImageTop(window.getImageTop());
												w.setImageLeft(window.getImageLeft());
												w.setImageWidth(window.getImageWidth());
												w.setImageHeight(window.getImageHeight());			
												w.setStatus(window.getStatus());
												w.setTransactionTime(new DateTime().toDate());
												
												w.setWindowType(window.getWindowType());
												
												windowService.save(w);
												windows.add(w);												
											}
										}
										
										wl.setDoors(doors);
										wl.setWindows(windows);									
										
										wallService.save(wl);
										walls.add(wl);										
									}
									
								}
								
								rm.setWalls(walls);
								*/
								
								roomService.save(rm);
								roomsForFlatForSaveAs.add(rm);
							}			
						}
						flatForSaveAs.setRooms(roomsForFlatForSaveAs);
						flatService.save(flatForSaveAs);

						if("".equals(copiedToFlatsName))
							copiedToFlatsName = flatForSaveAs.getName();
						else 
							copiedToFlatsName = copiedToFlatsName + ", " + flatForSaveAs.getName();
					}
				}
				if(!selectedFlatsForSaveAs.isEmpty())
					selectedFlatsForSaveAs.clear();
				
				/*selectedFlatId = selectedFlatIdForSaveAs;
				selectedFlat = flatService.populateRooms(flatService.findById(Integer.valueOf(selectedFlatIdForSaveAs)));*/
				/*rooms = roomsForFlatForSaveAs;*/
				if(!rooms.isEmpty()){
					rooms.clear();
				}								
				isCreateTable = false;
				RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rooms has been copied to "+copiedToFlatsName+" Flat.", ""));
				RequestContext.getCurrentInstance().update("msgs");
				RequestContext.getCurrentInstance().update("mainForm:selectedFlatRooms");

			}
		}
	}
	
	/**
	  * On click 'Create Table' button method.
	  * This method is called using remote command of jsf function to set Room entity data.
	  * This method is called as many times as many rooms selected. 
	  */
	@SuppressWarnings("unchecked")
	public void setRoomsList() {
		
		String roomList = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roomList");
		
		if(roomList != null && !"".equals(roomList)){
			
			JSONParser parser = new JSONParser();
			try {
				
				JSONArray arrayOfRoom = (JSONArray) parser.parse(roomList);
				
				JSONObject jsonRoomObj = null;
				String imageDivId = "";
				String width = "";
				String height = "";
				String top = "";
				String left = "";
				String imageType = "";
				String roomId = "";
				String roomName = "";
				
				Iterator<JSONObject> iterator = arrayOfRoom.iterator();
				while (iterator.hasNext()) {
					jsonRoomObj = (JSONObject) iterator.next();
					
					imageDivId = jsonRoomObj.get("imageDivId").toString();
					width = jsonRoomObj.get("width").toString();
					height = jsonRoomObj.get("height").toString();
					top = jsonRoomObj.get("top").toString();
					left = jsonRoomObj.get("left").toString();
					imageType = jsonRoomObj.get("imageType").toString();

					roomId = jsonRoomObj.get("roomId").toString();
					roomName = jsonRoomObj.get("roomName").toString().trim();
					
					Room room = new Room();
					if(roomId!=null && !"".equals(roomId)){			
						room.setId(Integer.valueOf(roomId));
					}

					room.setFlat(selectedFlat);
					room.setOrderNo((byte)1);

					if(imageDivId!=null && !"".equals(imageDivId)){
						room.setImageDivId(imageDivId);			
					}
					if(roomName!=null && !"".equals(roomName)){
						room.setName(roomName);			
					}
					if(imageType!=null && !"".equals(imageType)){
						room.setImageType(imageType);			
					}
					if(top!=null && !"".equals(top)){
						//top = top.substring(0,top.indexOf("px"));			
						//top = top.indexOf(".")!=-1 ? top.substring(0,top.indexOf(".")) : top;			
						room.setImageTop(Integer.valueOf(top));
					}
					if(left!=null && !"".equals(left)){
						//left = left.substring(0,left.indexOf("px"));			
						//left = left.indexOf(".")!=-1 ? left.substring(0,left.indexOf(".")) : left;			
						room.setImageLeft(Integer.valueOf(left));
					}
					if(width!=null && !"".equals(width)){			
						//width = width.substring(0,width.indexOf("px"));			
						//width = width.indexOf(".")!=-1 ? width.substring(0,width.indexOf(".")) : width;
						room.setImageWidth(Integer.valueOf(width));
					}
					if(height!=null && !"".equals(height)){
						//height = height.substring(0,height.indexOf("px"));			
						//height = height.indexOf(".")!=-1 ? height.substring(0,height.indexOf(".")) : height;			
						room.setImageHeight(Integer.valueOf(height));
					}
					
					room.setStatus((byte)0);
					room.setTransactionTime(new DateTime().toDate());

					if(rooms == null){
						rooms = new ConcurrentHashSet<Room>(); 
					} else {
						for (Room rm : rooms) {
							String oldRoomImageDivId = rm.getImageDivId();
							if(oldRoomImageDivId!=null && !"".equals(oldRoomImageDivId)){
								if(oldRoomImageDivId.equals(room.getImageDivId())){
									rooms.remove(rm);
									break;
								}					
							}
						}
					}					
					rooms.add(room);
				}
				isCreateTable = true;							
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}		
	}
	
	 /**
	  * On double click 'Room Image' method.
	  * This method is available for Rooms which are saved in database only.
	  * @return link for next page redirection with required data. 
	  */
	public String goToSelectedRoom(){
		this.selectedRoomId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedRoomId");
		return "dragDropRoom?faces-redirect=true&includeViewParams=true";
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
	  * This method is called using remote command of jsf function to delete temporary created room from list of rooms.
	  * Also delete from database if room id is available. (This is just a soft delete) 
	 */
	public void deleteTmpRoomFromRooms(){
		String dltTmpRoomDivId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dltTmpRoomDivId");
		
		if(dltTmpRoomDivId != null && !"".equals(dltTmpRoomDivId) && rooms != null && !rooms.isEmpty()){
			Iterator<Room> roomItr = rooms.iterator();
			while(roomItr.hasNext()){
				Room rm = roomItr.next();
				String rmImgDivId = rm.getImageDivId();
				if(dltTmpRoomDivId.equals(rmImgDivId)){					
					rooms.remove(rm);					
				}
			}		
		}	
		
		String deletedroomDatabaseId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deletedroomDatabaseId");		
		if(deletedroomDatabaseId != null && !"".equals(deletedroomDatabaseId)){
			deletedroomDatabaseIdList.add(deletedroomDatabaseId);
			//roomService.delete(roomService.findById(Integer.valueOf(deletedroomDatabaseId)));
		}
	}
}
