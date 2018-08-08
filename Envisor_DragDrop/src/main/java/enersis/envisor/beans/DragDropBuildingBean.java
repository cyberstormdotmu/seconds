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

import enersis.envisor.entity.Building;
import enersis.envisor.entity.Floor;
import enersis.envisor.entity.Project;
import enersis.envisor.service.BuildingService;
import enersis.envisor.service.FloorService;

/**
 * 
 * @author TatvaSoft
 * DragDropBuildingBean : JSF Backend Bean to manage dragDropBuilding.xhtml page.
 * It is used to update and synchronize dragDropBuilding.xhtml page data and backend bean data.
 *
 */


@Component("dragDropBuildingBean")
@ViewScoped
@ManagedBean
public class DragDropBuildingBean extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = -4390208829231101084L;	//Generated Serial version ID.

	private static final String katCatiImageUrl 	= 	"/resources/images/KatCati.png";	//Roof Floor Image Path.	
	private static final String katDuplexImageUrl 	= 	"/resources/images/KatDuplex.png";	//Duplex Floor Image Path.
	private static final String katNormalImageUrl 	= 	"/resources/images/KatNormal.png";	//Normal Floor Image Path.
	private static final String katGirisImageUrl 	= 	"/resources/images/KatGiris.png";	//Ground Floor Image Path.
	private static final String katBodrumImageUrl 	= 	"/resources/images/KatBodrum.png";	//Basement Floor Image Path.
	
	private Project selectedProject;	//Selected Project Entity.
	private List<Building> buildings = null;	//List of Building for selectedProject entity.	This list is lazy loading from Project entity. used in 'Copy to...' event.
	
	private String selectedBuildingId = "";	//Selected Building ID, available from previous page.
	
	private List<String> selectedBuildingsForSaveAs = null;	//Selected Buildings ID from dropdown list for 'Copy to...' event.

	private Building selectedBuilding;	//Selected Building Entity with selectedBuildingId.
	
	private Set<Floor> floors = null;	//List of Floor for selectedBuilding entity. This list is used while user click on 'Create Table' button. It will contain all floors(saved and non saved).

	private List<Floor> floorsFromDb = null;	//List of Floor from database for selectedBuilding entity. This list is lazy loading from Building entity.
	
	private List<String> deletedfloorDatabaseIdList = new ArrayList<String>();	//List of deleted Floor's Id from database.
		
	private String selectedFloorId = "";	//Selected floor Id used for sending selected Floor ID to next page i.e dragDropFloor.xhtml 
	
	private boolean isBuilindgSelected = false;	//Used to check whether building is selected or not for 'Copy to...' event.
	private boolean isCreateTable = false;	//Used to check whether 'Create Table' button is clicked or not.
	
	
	@Autowired
	private BreadCrumbBean breadCrumbBean;	//Autowired to BreadCrumbBean using spring DI with IOC, used for bread crumb of DragDrop Projects flow.

	@Autowired
	private DragDropProjectBean dragDropProjectBean;	//Autowired to DragDropProjectBean using spring DI with IOC, used for get data from previous page i.e dragDropProject.xhtml.
	
	@Autowired
    private BuildingService binaService;	//Autowired to BuildingService using spring DI with IOC, used for database operation related to Building entity.

	@Autowired
    private FloorService floorService;	//Autowired to FloorService using spring DI with IOC, used for database operation related to Floor entity.

	public String getKatcatiimageurl() {
		return katCatiImageUrl;
	}

	public String getKatdupleximageurl() {
		return katDuplexImageUrl;
	}

	public String getKatnormalimageurl() {
		return katNormalImageUrl;
	}

	public String getKatgirisimageurl() {
		return katGirisImageUrl;
	}

	public String getKatbodrumimageurl() {
		return katBodrumImageUrl;
	}

	
	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public List<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}
	
	
	public String getSelectedBuildingId() {
		return selectedBuildingId;
	}

	public void setSelectedBuildingId(String selectedBuildingId) {
		this.selectedBuildingId = selectedBuildingId;
	}

	public List<String> getSelectedBuildingsForSaveAs() {
		return selectedBuildingsForSaveAs;
	}

	public void setSelectedBuildingsForSaveAs(List<String> selectedBuildingsForSaveAs) {
		this.selectedBuildingsForSaveAs = selectedBuildingsForSaveAs;
	}

	
	public Building getSelectedBuilding() {
		return selectedBuilding;
	}
	
	public void setSelectedBuilding(Building selectedBuilding) {
		this.selectedBuilding = selectedBuilding;
	}

	public Set<Floor> getFloors() {
		return floors;
	}

	public void setFloors(Set<Floor> floors) {
		this.floors = floors;
	}

	
	public List<Floor> getFloorsFromDb() {
		return floorsFromDb;
	}

	public void setFloorsFromDb(List<Floor> floorsFromDb) {
		this.floorsFromDb = floorsFromDb;
	}


	public List<String> getDeletedfloorDatabaseIdList() {
		return deletedfloorDatabaseIdList;
	}

	public void setDeletedfloorDatabaseIdList(
			List<String> deletedfloorDatabaseIdList) {
		this.deletedfloorDatabaseIdList = deletedfloorDatabaseIdList;
	}


	public String getSelectedFloorId() {
		return selectedFloorId;
	}

	public void setSelectedFloorId(String selectedFloorId) {
		this.selectedFloorId = selectedFloorId;
	}

	
	public boolean getIsBuilindgSelected() {
		return isBuilindgSelected;
	}

	public void setIsBuilindgSelected(boolean isBuilindgSelected) {
		this.isBuilindgSelected = isBuilindgSelected;
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

	
	public DragDropProjectBean getDragDropProjectBean() {
		return dragDropProjectBean;
	}

	public void setDragDropProjectBean(DragDropProjectBean dragDropProjectBean) {
		this.dragDropProjectBean = dragDropProjectBean;
	}


	public BuildingService getBinaService() {
		return binaService;
	}

	public void setBinaService(BuildingService binaService) {
		this.binaService = binaService;
	}
	
	public FloorService getFloorService() {
		return floorService;
	}

	public void setFloorService(FloorService floorService) {
		this.floorService = floorService;
	}
	
		
	/**
	 * On load page method.
	 * check validation and set list of building and load drop down list of building on load of dragDropBuilding.xhtml Page.
	 * check validation and load the floor structure of selected building.  
	 */
	public void onLoadBuilding() {
		
		breadCrumbBean.navigateDragDropBuilding();	//Load bread crumb with 'project > building' page only.
		
		this.selectedProject = dragDropProjectBean.getSelectedProject();
		
		if(selectedProject != null){
			this.buildings = binaService.findByProjectWithDragDrop(selectedProject);
			
			this.selectedBuildingId = dragDropProjectBean.getSelectedBuildingId();
			
			if(selectedBuildingId != null  &&   !"".equals(selectedBuildingId)){
				
				this.selectedBuilding = binaService.findById(Integer.valueOf(selectedBuildingId));

				if(selectedBuilding != null){        	    	
					/*this.selectedBuilding = binaService.populateFloors(this.selectedBuilding);*/
					floorsFromDb = floorService.populateFloorsWithCriateria(selectedBuilding);
    	    	} else {
    	    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page, No Building found with ID="+selectedBuildingId+".", ""));
    	    	}        	    	
				
    		} else {
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page. Building Object is not available.", ""));
    		}   
			
    	} else {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page. Project Object is not available.", ""));
    	}    	    	
		
	} 	

	 /**
	  * On click 'Save' button method.
	  * check validation and save Building and its list of Floor entities in database.
	  */
	public void saveBina() {

		if(!isCreateTable){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
		} else {			

			if(deletedfloorDatabaseIdList!=null && !deletedfloorDatabaseIdList.isEmpty()){
				for (String deletedfloorDatabaseId : deletedfloorDatabaseIdList) {
					floorService.delete(floorService.findById(Integer.valueOf(deletedfloorDatabaseId)));
				}
			}

			for (Floor floor : floors) {
				floorService.save(floor);				
			}		

			selectedBuilding.setFloors(floors);
			binaService.save(selectedBuilding);
			selectedBuilding = binaService.populateFloors(selectedBuilding);
			/*floors = binaService.populateFloors(selectedBuilding).getFloors();*/
			if(!floors.isEmpty()){
				floors.clear();
			}
			isCreateTable = false;
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Floors has been saved to "+selectedBuilding.getName()+" Building", ""));
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainForm:selectedBuildingFloors");
	}								

	}   	 
	
	 /**
	  * On click 'Copy to...' button method.
	  * check validation and save Building and its list of Floor entities in database for selected Building from dropdown list for 'Copy to..' event.
	  */
	public void saveAsBina() {
		
		if(selectedBuildingsForSaveAs==null || selectedBuildingsForSaveAs.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select Building for Copy to...", ""));
			RequestContext.getCurrentInstance().update("msgs");
		/*} else if(selectedBuildingId.equals(selectedBuildingIdForSaveAs)){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select another Building for Copy to...", ""));
			RequestContext.getCurrentInstance().update("msgs");*/
		} else {
			if(!isCreateTable){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
				RequestContext.getCurrentInstance().update("msgs");
			} else {
				
				String copiedToBuildingsName = "";
				for (String selectedBuildingIdForSaveAs : selectedBuildingsForSaveAs) {
					
					if(!selectedBuildingIdForSaveAs.equals(selectedBuildingId)){
						Building buildingForSaveAs = binaService.findById(Integer.valueOf(selectedBuildingIdForSaveAs));					
						buildingForSaveAs = binaService.populateFloors(buildingForSaveAs);
						
						for (Floor floor : buildingForSaveAs.getFloors()) {
							floorService.delete(floor);			
						}
						
						Set<Floor> floorsForBuildingForSaveAs = new ConcurrentHashSet<Floor>();
						for (Floor floor : floors) {			
							if(floor.getStatus()==0){
								Floor flr = new Floor();
								flr.setBuilding(buildingForSaveAs);;
								flr.setName(floor.getName());
								flr.setFlatCount((byte)0);
								flr.setImageType(floor.getImageType());
								flr.setImageTop(floor.getImageTop());
								flr.setImageLeft(floor.getImageLeft());
								flr.setImageWidth(floor.getImageWidth());
								flr.setImageHeight(floor.getImageHeight());			
								flr.setStatus(floor.getStatus());
								flr.setTransactionTime(new DateTime().toDate());						
								floorService.save(flr);
								floorsForBuildingForSaveAs.add(flr);								
							}			
						}
						buildingForSaveAs.setFloors(floorsForBuildingForSaveAs);
						binaService.save(buildingForSaveAs);		
						
						if("".equals(copiedToBuildingsName))
							copiedToBuildingsName = buildingForSaveAs.getName();
						else 
							copiedToBuildingsName = copiedToBuildingsName + ", " + buildingForSaveAs.getName();						
					}										
				}
				
				if(!selectedBuildingsForSaveAs.isEmpty())
					selectedBuildingsForSaveAs.clear();
								
				/*selectedBuildingId = selectedBuildingIdForSaveAs;
				selectedBuilding = binaService.populateFloors(binaService.findById(Integer.valueOf(selectedBuildingIdForSaveAs)));*/
				/*floors = floorsForBuildingForSaveAs;*/
				if(!floors.isEmpty()){
					floors.clear();
				}				
				isCreateTable = false;
				RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Floors has been copied to "+copiedToBuildingsName+" Building", ""));
				RequestContext.getCurrentInstance().update("msgs");
				RequestContext.getCurrentInstance().update("mainForm:selectedBuildingFloors");
			}									
		}				
	}   	 
	
	 /**
	  * On click 'Create Table' button method.
	  * This method is called using remote command of jsf function to set Floor entity data.
	  * This method is called as many times as many floors selected. 
	  */
	@SuppressWarnings("unchecked")
	public void setFloorsList() {
		
		String floorList = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("floorList");
		
		if(floorList != null && !"".equals(floorList)){
			
			JSONParser parser = new JSONParser();
			try {
				
				JSONArray arrayOfFloor = (JSONArray) parser.parse(floorList);
				
				JSONObject jsonFloorObj = null;
				String imageDivId = "";
				String width = "";
				String height = "";
				String top = "";
				String left = "";
				String imageType = "";
				String floorId = "";
				String floorName = "";
				
				Iterator<JSONObject> iterator = arrayOfFloor.iterator();
				while (iterator.hasNext()) {
					jsonFloorObj = (JSONObject) iterator.next();
					
					imageDivId = jsonFloorObj.get("imageDivId").toString();
					width = jsonFloorObj.get("width").toString();
					height = jsonFloorObj.get("height").toString();
					top = jsonFloorObj.get("top").toString();
					left = jsonFloorObj.get("left").toString();
					imageType = jsonFloorObj.get("imageType").toString();

					floorId = jsonFloorObj.get("floorId").toString();
					floorName = jsonFloorObj.get("floorName").toString().trim();

					Floor floor = new Floor();
					if(floorId!=null && !"".equals(floorId)){			
						floor.setId(Integer.valueOf(floorId));
					}
					
					floor.setBuilding(selectedBuilding);;
					floor.setFlatCount((byte)0);
					
					if(imageDivId!=null && !"".equals(imageDivId)){
						floor.setImageDivId(imageDivId);			
					}
					if(floorName!=null && !"".equals(floorName)){
						floor.setName(floorName);			
					}
					if(imageType!=null && !"".equals(imageType)){
						floor.setImageType(imageType);			
					}
					if(top!=null && !"".equals(top)){
						//top = top.substring(0,top.indexOf("px"));			
						//top = top.indexOf(".")!=-1 ? top.substring(0,top.indexOf(".")) : top;			
						floor.setImageTop(Integer.valueOf(top));
					}
					if(left!=null && !"".equals(left)){
						//left = left.substring(0,left.indexOf("px"));			
						//left = left.indexOf(".")!=-1 ? left.substring(0,left.indexOf(".")) : left;			
						floor.setImageLeft(Integer.valueOf(left));
					}
					if(width!=null && !"".equals(width)){			
						//width = width.substring(0,width.indexOf("px"));			
						//width = width.indexOf(".")!=-1 ? width.substring(0,width.indexOf(".")) : width;
						floor.setImageWidth(Integer.valueOf(width));
					}
					if(height!=null && !"".equals(height)){
						//height = height.substring(0,height.indexOf("px"));			
						//height = height.indexOf(".")!=-1 ? height.substring(0,height.indexOf(".")) : height;			
						floor.setImageHeight(Integer.valueOf(height));
					}
					
					floor.setStatus((byte)0);
					floor.setTransactionTime(new DateTime().toDate());

					if(floors == null){
						floors = new ConcurrentHashSet<Floor>(); 
					} else {
						for (Floor flr : floors) {
							String oldFloorImageDivId = flr.getImageDivId();
							if(oldFloorImageDivId!=null && !"".equals(oldFloorImageDivId)){
								if(oldFloorImageDivId.equals(floor.getImageDivId())){
									floors.remove(flr);
									break;
								}					
							}
						}
					}					
					floors.add(floor);
				}
				isCreateTable = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}			
		}				
	}   	 
		
	 /**
	  * On double click 'Floor Image' method.
	  * This method is available for Floors which are saved in database only.
	  * @return link for next page redirection with required data. 
	  */
	public String goToSelectedFloor(){
		this.selectedFloorId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedFloorId");
		return "dragDropFloor?faces-redirect=true";
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
	  * This method is called using remote command of jsf function to delete temporary created floor from list of floors.
	  * Also delete from database if floor id is available. (This is just a soft delete) 
	 */
	public void deleteTmpFloorFromFloors(){
		String dltTmpFloorDivId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dltTmpFloorDivId");
		
		if(dltTmpFloorDivId != null && !"".equals(dltTmpFloorDivId) && floors != null && !floors.isEmpty()){
			Iterator<Floor> floorItr = floors.iterator();
			while(floorItr.hasNext()){
				Floor flr = floorItr.next();
				String flrImgDivId = flr.getImageDivId();
				if(dltTmpFloorDivId.equals(flrImgDivId)){					
					floors.remove(flr);					
				}
			}		
		}	
		
		String deletedfloorDatabaseId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deletedfloorDatabaseId");		
		if(deletedfloorDatabaseId != null && !"".equals(deletedfloorDatabaseId)){
			deletedfloorDatabaseIdList.add(deletedfloorDatabaseId);
			//floorService.delete(floorService.findById(Integer.valueOf(deletedfloorDatabaseId)));
		}
	}
}
