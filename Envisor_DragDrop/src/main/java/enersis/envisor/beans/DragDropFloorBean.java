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
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Floor;
import enersis.envisor.service.FlatService;
import enersis.envisor.service.FloorService;

/**
 * 
 * @author TatvaSoft
 * DragDropFloorBean : JSF Backend Bean to manage dragDropFloor.xhtml page.
 * It is used to update and synchronize dragDropFloor.xhtml page data and backend bean data.
 *
 */


@Component("dragDropFloorBean")
@ViewScoped
@ManagedBean
public class DragDropFloorBean extends AbstractBacking implements Serializable {
	
	private static final long serialVersionUID = -3016991773973101428L;	//Generated Serial version ID.
	
	private static final String flatImageUrl 	= 	"/resources/images/Flat.png";	//Flat Image Path.
	
	private Building selectedBuilding;	//Selected Building Entity.
	private List<Floor> floors = null;	//List of Floor for selectedBuilding entity. This list is lazy loading from Building entity.
	
	private String selectedFloorId = "";	//Selected Floor ID, available from previous page.
	private List<String> selectedFloorsForSaveAs = null;	//Selected Floor IDs from dropdown list for 'Copy to...' event.
	
	private Floor selectedFloor;	//Selected Floor Entity with selectedFloorId.

	private Set<Flat> flats = null;	//List of Flat for selectedFloor entity. This list is used while user click on 'Create Table' button. It will contain all flats(saved and non saved).

	private List<Flat> flatsFromDb = null;	//List of Flat from database for selectedFloor entity. This list is lazy loading from Floor entity.
	
	private List<String> deletedflatDatabaseIdList = new ArrayList<String>();	//List of deleted Flat's Id from database.
	
	private String selectedFlatId = "";	//Selected flat Id used for sending selected flat ID to next page i.e dragDropFlat.xhtml
	
	private boolean isFloorSelected = false;	//Used to check whether floor is selected or not for 'Copy to...' event. 
	private boolean isCreateTable = false;	//Used to check whether 'Create Table' button is clicked or not.
	
	@Autowired
	private BreadCrumbBean breadCrumbBean;	//Autowired to BreadCrumbBean using spring DI with IOC, used for bread crumb of DragDrop Projects flow.

	@Autowired
	private DragDropBuildingBean dragDropBuildingBean;	//Autowired to DragDropBuildingBean using spring DI with IOC, used for get data from previous page i.e dragDropBuilding.xhtml.

	@Autowired
    private FloorService floorService;	//Autowired to FloorService using spring DI with IOC, used for database operation related to Floor entity.	

	@Autowired
    private FlatService flatService;	//Autowired to FlatService using spring DI with IOC, used for database operation related to Flat entity.
	
		
	public String getFlatImageUrl() {
		return flatImageUrl;
	}

	
	public Building getSelectedBuilding() {
		return selectedBuilding;
	}

	public void setSelectedBuilding(Building selectedBuilding) {
		this.selectedBuilding = selectedBuilding;
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public void setFloors(List<Floor> floors) {
		this.floors = floors;
	}
	

	public String getSelectedFloorId() {
		return selectedFloorId;
	}

	public void setSelectedFloorId(String selectedFloorId) {
		this.selectedFloorId = selectedFloorId;
	}

	public List<String> getSelectedFloorsForSaveAs() {
		return selectedFloorsForSaveAs;
	}


	public void setSelectedFloorsForSaveAs(List<String> selectedFloorsForSaveAs) {
		this.selectedFloorsForSaveAs = selectedFloorsForSaveAs;
	}


	public Floor getSelectedFloor() {
		return selectedFloor;
	}

	public void setSelectedFloor(Floor selectedFloor) {
		this.selectedFloor = selectedFloor;
	}

	public Set<Flat> getFlats() {
		return flats;
	}

	public void setFlats(Set<Flat> flats) {
		this.flats = flats;
	}


	public List<Flat> getFlatsFromDb() {
		return flatsFromDb;
	}


	public void setFlatsFromDb(List<Flat> flatsFromDb) {
		this.flatsFromDb = flatsFromDb;
	}

	
	public List<String> getDeletedflatDatabaseIdList() {
		return deletedflatDatabaseIdList;
	}


	public void setDeletedflatDatabaseIdList(List<String> deletedflatDatabaseIdList) {
		this.deletedflatDatabaseIdList = deletedflatDatabaseIdList;
	}


	public String getSelectedFlatId() {
		return selectedFlatId;
	}

	public void setSelectedFlatId(String selectedFlatId) {
		this.selectedFlatId = selectedFlatId;
	}
	
	
	public boolean getIsFloorSelected() {
		return isFloorSelected;
	}

	public void setIsFloorSelected(boolean isFloorSelected) {
		this.isFloorSelected = isFloorSelected;
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

	public DragDropBuildingBean getDragDropBuildingBean() {
		return dragDropBuildingBean;
	}

	public void setDragDropBuildingBean(DragDropBuildingBean dragDropBuildingBean) {
		this.dragDropBuildingBean = dragDropBuildingBean;
	}


	public FloorService getFloorService() {
		return floorService;
	}

	public void setFloorService(FloorService floorService) {
		this.floorService = floorService;
	}

	public FlatService getFlatService() {
		return flatService;
	}

	public void setFlatService(FlatService flatService) {
		this.flatService = flatService;
	}


	/**
	 * On load page method.
	 * check validation and set list of floor and load drop down list of floor on load of dragDropFloor.xhtml Page.
	 * check validation and load the flat structure of selected floor.  
	 */
    public void onLoadFloor() {
    	    	
    	breadCrumbBean.navigateDragDropFloor();	//Load bread crumb with 'project > building > Floor' page only.
    	
    	this.selectedBuilding = dragDropBuildingBean.getSelectedBuilding();
    	
    	if(selectedBuilding != null){        	    		
    		this.floors =  floorService.findbyBuildingWithDragDrop(selectedBuilding);
    		
			this.selectedFloorId = dragDropBuildingBean.getSelectedFloorId();
			
    		if(selectedFloorId != null  &&   !"".equals(selectedFloorId)){

    			this.selectedFloor = floorService.findById(Integer.valueOf(selectedFloorId));
    	    	
    	    	if(selectedFloor != null){        	    	
    	    		/*this.selectedFloor = floorService.populateFlats(this.selectedFloor);*/
    	    		flatsFromDb = flatService.populateFlatsWithCriateria(selectedFloor);
    	    	} else {
    	    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page, No Floor found with ID="+selectedFloorId+".", ""));
    	    	}        	    	
    	    	
    		} else {
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page. Floor Object is not available.", ""));
    		}   
    		
    	} else {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while loading page. Building Object is not available.", ""));
    	}    	    	
    	
    }
	
	 /**
	  * On click 'Save' button method.
	  * check validation and save Floor and its list of Flat entities in database.
	  */
	public void saveFloor() {
		if(!isCreateTable){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
		} else {
			
			if(deletedflatDatabaseIdList!=null && !deletedflatDatabaseIdList.isEmpty()){
				for (String deletedflatDatabaseId : deletedflatDatabaseIdList) {
					flatService.delete(flatService.findById(Integer.valueOf(deletedflatDatabaseId)));
				}
			}

			for (Flat flat : flats) {
				flatService.save(flat);				
			}		
			
			selectedFloor.setFlats(flats);
			floorService.save(selectedFloor);
			selectedFloor = floorService.populateFlats(selectedFloor);
			/*flats = selectedFloor.getFlats();*/
			if(!flats.isEmpty()){
				flats.clear();
			}
			isCreateTable = false;
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flats has been saved to "+selectedFloor.getName()+" Floor", ""));
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainForm:selectedFloorFlats");
		}
	}
	
	 /**
	  * On click 'Copy to...' button method.
	  * check validation and save Floor and its list of Flat entities in database for selected Floor from dropdown list for 'Copy to..' event.
	  */
	public void saveAsFloor() {
		if(selectedFloorsForSaveAs == null || selectedFloorsForSaveAs.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select Floor for Copy to...", ""));
			RequestContext.getCurrentInstance().update("msgs");
		/*} else if(selectedFloorId.equals(selectedFloorIdForSaveAs)){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select another Floor for Copy to...", ""));
			RequestContext.getCurrentInstance().update("msgs");*/
		} else {
			if(!isCreateTable){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
				RequestContext.getCurrentInstance().update("msgs");
			} else {
				
				String copiedToFloorsName = "";
				for (String selectedFloorIdForSaveAs : selectedFloorsForSaveAs) {
					if(!selectedFloorIdForSaveAs.equals(selectedFloorId)){
						Floor floorForSaveAs = floorService.findById(Integer.valueOf(selectedFloorIdForSaveAs));
						
						floorForSaveAs = floorService.populateFlats(floorForSaveAs);
						
						for (Flat flat : floorForSaveAs.getFlats()) {
							flatService.delete(flat);			
						}
						
						Set<Flat> flatsForFloorForSaveAs = new ConcurrentHashSet<Flat>();
						for (Flat flat : flats) {			
							if(flat.getStatus() == 0){
								Flat flt = new Flat();
								flt.setFloorId(floorForSaveAs);						
								flt.setNo("");
								flt.setName(flat.getName());
								flt.setImageType(flat.getImageType());
								flt.setImageTop(flat.getImageTop());
								flt.setImageLeft(flat.getImageLeft());
								flt.setImageWidth(flat.getImageWidth());
								flt.setImageHeight(flat.getImageHeight());			
								flt.setStatus(flat.getStatus());
								flt.setTransactionTime(new DateTime().toDate());						
								flatService.save(flt);
								flatsForFloorForSaveAs.add(flt);								
							}			
						}
						floorForSaveAs.setFlats(flatsForFloorForSaveAs);
						floorService.save(floorForSaveAs);		

						if("".equals(copiedToFloorsName))
							copiedToFloorsName = floorForSaveAs.getName();
						else 
							copiedToFloorsName = copiedToFloorsName + ", " + floorForSaveAs.getName();
					}
				}
				if(!selectedFloorsForSaveAs.isEmpty())
					selectedFloorsForSaveAs.clear();
			
				/*selectedFloorId = selectedFloorIdForSaveAs;
				selectedFloor = floorService.populateFlats(floorService.findById(Integer.valueOf(selectedFloorIdForSaveAs)));*/
				/*flats = selectedFloor.getFlats();*/
				if(!flats.isEmpty()){
					flats.clear();
				}				
				isCreateTable = false;
				RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flats has been copied to "+copiedToFloorsName+" Floor", ""));
				RequestContext.getCurrentInstance().update("msgs");
				RequestContext.getCurrentInstance().update("mainForm:selectedFloorFlats");
			}
		}		
	}
	

	/**
	  * On click 'Create Table' button method.
	  * This method is called using remote command of jsf function to set Flat entity data.
	  * This method is called as many times as many flats selected. 
	  */
	@SuppressWarnings("unchecked")
	public void setFlatsList() {
		
		String flatList = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("flatList");
		
		if(flatList != null && !"".equals(flatList)){
			
			JSONParser parser = new JSONParser();
			try {
				
				JSONArray arrayOfFlat = (JSONArray) parser.parse(flatList);
				
				JSONObject jsonFlatObj = null;
				String imageDivId = "";
				String width = "";
				String height = "";
				String top = "";
				String left = "";
				String imageType = "";
				String flatId = "";
				String flatName = "";
				
				Iterator<JSONObject> iterator = arrayOfFlat.iterator();
				while (iterator.hasNext()) {
					jsonFlatObj = (JSONObject) iterator.next();
					
					imageDivId = jsonFlatObj.get("imageDivId").toString();
					width = jsonFlatObj.get("width").toString();
					height = jsonFlatObj.get("height").toString();
					top = jsonFlatObj.get("top").toString();
					left = jsonFlatObj.get("left").toString();
					imageType = jsonFlatObj.get("imageType").toString();

					flatId = jsonFlatObj.get("flatId").toString();
					flatName = jsonFlatObj.get("flatName").toString().trim();

					Flat newFlat = new Flat();
					if(flatId!=null && !"".equals(flatId)){			
						newFlat.setId(Integer.valueOf(flatId));
					}

					newFlat.setFloorId(selectedFloor);
					newFlat.setNo("");
					
					if(imageDivId!=null && !"".equals(imageDivId)){
						newFlat.setImageDivId(imageDivId);			
					}
					if(flatName!=null && !"".equals(flatName)){
						newFlat.setName(flatName);			
					}
					if(imageType!=null && !"".equals(imageType)){
						newFlat.setImageType(imageType);			
					}
					if(top!=null && !"".equals(top)){
						//top = top.substring(0,top.indexOf("px"));			
						//top = top.indexOf(".")!=-1 ? top.substring(0,top.indexOf(".")) : top;			
						newFlat.setImageTop(Integer.valueOf(top));
					}
					if(left!=null && !"".equals(left)){
						//left = left.substring(0,left.indexOf("px"));			
						//left = left.indexOf(".")!=-1 ? left.substring(0,left.indexOf(".")) : left;			
						newFlat.setImageLeft(Integer.valueOf(left));
					}
					if(width!=null && !"".equals(width)){			
						//width = width.substring(0,width.indexOf("px"));			
						//width = width.indexOf(".")!=-1 ? width.substring(0,width.indexOf(".")) : width;
						newFlat.setImageWidth(Integer.valueOf(width));
					}
					if(height!=null && !"".equals(height)){
						//height = height.substring(0,height.indexOf("px"));			
						//height = height.indexOf(".")!=-1 ? height.substring(0,height.indexOf(".")) : height;			
						newFlat.setImageHeight(Integer.valueOf(height));
					}
					
					newFlat.setStatus((byte)0);
					newFlat.setTransactionTime(new DateTime().toDate());

					if(flats == null){
						flats = new ConcurrentHashSet<Flat>(); 
					} else {
						for (Flat oldFlat : flats) {
							String oldFlatImageDivId = oldFlat.getImageDivId();
							if(oldFlatImageDivId!=null && !"".equals(oldFlatImageDivId)){
								if(oldFlatImageDivId.equals(newFlat.getImageDivId())){
									flats.remove(oldFlat);
									break;
								}					
							}
						}
					}					
					flats.add(newFlat);					
				}				
			isCreateTable = true;	
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}		
	}

	 /**
	  * On double click 'Flat Image' method.
	  * This method is available for Flats which are saved in database only.
	  * @return link for next page redirection with required data. 
	  */
	public String goToSelectedFlat(){
		this.selectedFlatId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedFlatId");
		return "dragDropFlat?faces-redirect=true&includeViewParams=true";
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
	  * This method is called using remote command of jsf function to delete temporary created flat from list of flats.
	  * Also delete from database if flat id is available. (This is just a soft delete) 
	 */
	public void deleteTmpFlatFromFlats(){
		String dltTmpFlatDivId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dltTmpFlatDivId");
		
		if(dltTmpFlatDivId != null && !"".equals(dltTmpFlatDivId) && flats != null && !flats.isEmpty()){
			Iterator<Flat> flatItr = flats.iterator();
			while(flatItr.hasNext()){
				Flat flt = flatItr.next();
				String fltImgDivId = flt.getImageDivId();
				if(dltTmpFlatDivId.equals(fltImgDivId)){					
					flats.remove(flt);					
				}
			}		
		}	
		
		String deletedflatDatabaseId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deletedflatDatabaseId");		
		if(deletedflatDatabaseId != null && !"".equals(deletedflatDatabaseId)){
			deletedflatDatabaseIdList.add(deletedflatDatabaseId);
			//flatService.delete(flatService.findById(Integer.valueOf(deletedflatDatabaseId)));
		}
	}
}
