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
import enersis.envisor.entity.Project;
import enersis.envisor.service.BuildingService;
import enersis.envisor.service.ProjectService;

/***
 * 
 * @author TatvaSoft
 * DragDropProjectBean : JSF Backend Bean to manage dragDropProject.xhtml page.
 * It is used to update and synchronize dragDropProject.xhtml page data and backend bean data.
 *
 */


@Component("dragDropProjectBean")
@ViewScoped
@ManagedBean
public class DragDropProjectBean extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = -1599484617687566284L;	//Generated Serial version ID. 
		
	private static final String buildingImageUrl 	= 	"/resources/images/Building.png";	//Building Image Path.

	private List<Project> projects = null;	//List of Project, used to select Project and find its saved Building structure. 
	
	private String newProjectName = "";	//Project Name, if none of project is selected give new project name. 

	private String selectedProjectId = "";	//Selected Project ID from above list.   
	private Project selectedProject = null;	//Selected Project Entity with selectedProjectId.
	
	private Set<Building> buildings = null;	//List of Building for selectedProject entity.	This list is used while user click on 'Create Table' button. It will contain all Buildings(saved and non saved).

	private List<Building> buildingsFromDb = null;	//List of Building from database for selectedProject entity. This list is lazy loading from Project entity.

	private List<String> deletedbuildingDatabaseIdList = new ArrayList<String>();	//List of deleted Building's Id from database.
	
	private String selectedBuildingId = "";	//Selected building Id used for sending selected Building ID to next page i.e dragDropBuilding.xhtml
	
	private boolean isCreateTable = false;	//Used to check whether 'Create Table' button is clicked or not.

	@Autowired
	private BreadCrumbBean breadCrumbBean;	//Autowired to BreadCrumbBean using spring DI with IOC, used for bread crumb of DragDrop Projects flow.

	@Autowired
    private ProjectService projectService;	//Autowired to ProjectService using spring DI with IOC, used for database operation related to Project entity.  

	@Autowired
    private BuildingService buildingService;	//Autowired to BuildingService using spring DI with IOC, used for database operation related to Building entity.


	public String getBuildingimageurl() {
		return buildingImageUrl;
	}


	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	
	public String getNewProjectName() {
		return newProjectName;
	}
	public void setNewProjectName(String newProjectName) {
		this.newProjectName = newProjectName;
	}

	public String getSelectedProjectId() {
		return selectedProjectId;
	}

	public void setSelectedProjectId(String selectedProjectId) {
		this.selectedProjectId = selectedProjectId;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}
	

	public Set<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(Set<Building> buildings) {
		this.buildings = buildings;
	}

	
	public List<Building> getBuildingsFromDb() {
		return buildingsFromDb;
	}

	public void setBuildingsFromDb(List<Building> buildingsFromDb) {
		this.buildingsFromDb = buildingsFromDb;
	}


	public List<String> getDeletedbuildingDatabaseIdList() {
		return deletedbuildingDatabaseIdList;
	}


	public void setDeletedbuildingDatabaseIdList(
			List<String> deletedbuildingDatabaseIdList) {
		this.deletedbuildingDatabaseIdList = deletedbuildingDatabaseIdList;
	}

	
	public String getSelectedBuildingId() {
		return selectedBuildingId;
	}

	public void setSelectedBuildingId(String selectedBuildingId) {
		this.selectedBuildingId = selectedBuildingId;
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

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public BuildingService getBuildingService() {
		return buildingService;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * On load page method.
	 * set list of project and load drop down list of Project on load of dragDropProject.xhtml Page. 
	 */
	 public void onLoadProject() {	
		 	breadCrumbBean.navigateDragDropProject();	//Load bread crumb with 'project' page only.
		 	this.projects = projectService.findAll();
		 	
		 	if(selectedProject != null){
				buildingsFromDb = buildingService.populateBuildingsWithCriateria(selectedProject);				
		 	}
	 }
	 
	 /**
	  * On click 'Save' button method.
	  * check validation and save Project and its list of Building entities in database.
	  */
	 public void saveProject() {
		if(!isCreateTable){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please click on 'Create Table' First!!", ""));
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
		} else {
			
			if(deletedbuildingDatabaseIdList!=null && !deletedbuildingDatabaseIdList.isEmpty()){
				for (String deletedbuildingDatabaseId : deletedbuildingDatabaseIdList) {
					buildingService.delete(buildingService.findById(Integer.valueOf(deletedbuildingDatabaseId)));
				}
			}
			
			for (Building building : buildings) {
				buildingService.save(building);				
			}		
			
			selectedProject.setBuildings(buildings);
			projectService.save(selectedProject);
			if(!buildings.isEmpty()){
				buildings.clear();
			}
			isCreateTable = false;
			RequestContext.getCurrentInstance().update("mainform:hiddenCreateTable");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Buildings has been saved to "+selectedProject.getProjectName()+" Project.", ""));			
			RequestContext.getCurrentInstance().update("msgs");
			RequestContext.getCurrentInstance().update("mainForm:selectedProjectBuildings");
		}		 
	 }
	 
	 /**
	  * On click 'Find' button method.
	  * check validation and find Project and its list of Building entities in database and load the Project data page.
	  */
	 public void findProject() {
		if( selectedProjectId == null || "".equals(selectedProjectId)){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select Project", ""));
			selectedProject = null;
		} else {
			selectedProject = projectService.findById(Short.valueOf(selectedProjectId));
			if( selectedProject == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Project with ID="+selectedProjectId+" is not available in database.", ""));
			}			
		}		 		 
	 }
	 
	 /**
	  * On click 'Create Table' button method.
	  * This method is called using remote command of jsf function to set Building entity data.
	  * This method is called as many times as many buildings selected. 
	  */
	@SuppressWarnings("unchecked")
	public void setBuildingList() {
		 
		String buildingList = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("buildingList");

		if(buildingList != null && !"".equals(buildingList)){
			
			JSONParser parser = new JSONParser();
			try {
				
				JSONArray arrayOfBuilding = (JSONArray) parser.parse(buildingList);
				
				JSONObject jsonBuildingObj = null;
				String imageDivId = "";
				String width = "";
				String height = "";
				String top = "";
				String left = "";
				String imageType = "";
				String buildingId = "";
				String buildingName = "";
				
				Iterator<JSONObject> iterator = arrayOfBuilding.iterator();
				while (iterator.hasNext()) {
					jsonBuildingObj = (JSONObject) iterator.next();
					
					imageDivId = jsonBuildingObj.get("imageDivId").toString();
					width = jsonBuildingObj.get("width").toString();
					height = jsonBuildingObj.get("height").toString();
					top = jsonBuildingObj.get("top").toString();
					left = jsonBuildingObj.get("left").toString();
					imageType = jsonBuildingObj.get("imageType").toString();

					buildingId = jsonBuildingObj.get("buildingId").toString();
					buildingName = jsonBuildingObj.get("buildingName").toString().trim();
	
					if(selectedProject==null && (newProjectName == null || "".equals(newProjectName))){
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please insert Project Name.", ""));
						RequestContext.getCurrentInstance().update("msgs");
					} else {
						if(selectedProject==null){
							selectedProject = new Project();
							selectedProject.setProjectType((byte)0);
							selectedProject.setProjectCode(newProjectName);
							selectedProject.setProjectName(newProjectName);
							selectedProject.setName("");
							selectedProject.setSurname("");
							selectedProject.setAddress("");
							selectedProject.setAuthorized("");
							selectedProject.setPhone("");
							selectedProject.setEmail("");
							selectedProject.setRegistryDate(new DateTime().toDate());
							selectedProject.setExplanations("");
							selectedProject.setDataServices("");
							selectedProject.setOperator("");
							selectedProject.setTransactionTime(new DateTime().toDate());
							selectedProject.setStatus((byte)0);
							
							projectService.save(selectedProject);			
							
						}

						Building building = null;			
						if(buildingId!=null && !"".equals(buildingId)){			
							building = buildingService.findById(Integer.valueOf(buildingId));			
						} else {
							building = new Building();
						}
						
						building.setProject(selectedProject);

						if(imageDivId!=null && !"".equals(imageDivId)){
							building.setImageDivId(imageDivId);			
						}
						if(buildingName!=null && !"".equals(buildingName)){
							building.setName(buildingName);			
						}
						if(imageType!=null && !"".equals(imageType)){
							building.setImageType(imageType);			
						}
						if(top!=null && !"".equals(top)){
							//top = top.substring(0,top.indexOf("px"));			
							//top = top.indexOf(".")!=-1 ? top.substring(0,top.indexOf(".")) : top;			
							building.setImageTop(Integer.valueOf(top));
						}
						if(left!=null && !"".equals(left)){
							//left = left.substring(0,left.indexOf("px"));			
							//left = left.indexOf(".")!=-1 ? left.substring(0,left.indexOf(".")) : left;			
							building.setImageLeft(Integer.valueOf(left));
						}
						if(width!=null && !"".equals(width)){			
							//width = width.substring(0,width.indexOf("px"));			
							//width = width.indexOf(".")!=-1 ? width.substring(0,width.indexOf(".")) : width;
							building.setImageWidth(Integer.valueOf(width));
						}
						if(height!=null && !"".equals(height)){
							//height = height.substring(0,height.indexOf("px"));			
							//height = height.indexOf(".")!=-1 ? height.substring(0,height.indexOf(".")) : height;			
							building.setImageHeight(Integer.valueOf(height));
						}

						building.setStatus((byte)0);
						building.setTransactionTime(new DateTime().toDate());

						if(buildings == null){
							buildings = new ConcurrentHashSet<Building>(); 
						} else {
							for (Building bldng : buildings) {
								String oldBuildingImageDivId = bldng.getImageDivId();
								if(oldBuildingImageDivId!=null && !"".equals(oldBuildingImageDivId)){
									if(oldBuildingImageDivId.equals(building.getImageDivId())){
										buildings.remove(bldng);
										break;
									}					
								}
							}
						}					
						buildings.add(building);
					}							
				}
				isCreateTable = true;							
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}				
	 }
	 
	 /**
	  * On double click 'Building Image' method.
	  * This method is available for Buildings which are saved in database only.
	  * @return link for next page redirection with required data. 
	  */
	 public String goToSelectedBuilding(){
		this.selectedBuildingId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedBuildingId");
		return "dragDropBuilding?faces-redirect=true&includeViewParams=true";
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
	  * This method is called using remote command of jsf function to delete temporary created building from list of buildings.
	  * Also delete from database if building id is available. (This is just a soft delete) 
	 */
	public void deleteTmpBuildingFromBuildings(){
		String dltTmpBuildingDivId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dltTmpBuildingDivId");
		
		if(dltTmpBuildingDivId != null && !"".equals(dltTmpBuildingDivId) && buildings != null && !buildings.isEmpty()){
			Iterator<Building> buildingItr = buildings.iterator();
			while(buildingItr.hasNext()){
				Building bldg = buildingItr.next();
				String bldgImgDivId = bldg.getImageDivId();
				if(bldgImgDivId.equals(dltTmpBuildingDivId)){					
					buildings.remove(bldg);					
				}
			}		
		}	
		
		String deletedbuildingDatabaseId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deletedbuildingDatabaseId");		
		if(deletedbuildingDatabaseId != null && !"".equals(deletedbuildingDatabaseId)){
			deletedbuildingDatabaseIdList.add(deletedbuildingDatabaseId);
			//buildingService.delete(buildingService.findById(Integer.valueOf(deletedbuildingDatabaseId)));
		}
	}
	
	
	public void onChangeProject(){
		System.out.println("Selected Project ID "+selectedProjectId);
		System.out.println("Selected Project"+selectedProject);
		if(selectedProject!=null)
			System.out.println("Selected Project"+selectedProject.getId());
	}
	
}
