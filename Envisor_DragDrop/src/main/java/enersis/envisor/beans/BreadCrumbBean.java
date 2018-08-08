package enersis.envisor.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.springframework.stereotype.Component;

/***
 * 
 * @author TatvaSoft
 * BreadCrumbBean : JSF Backend Bean to manage bread crumb for DragDrop Projects flow.
 * This class is used on all pages.
 */

@Component("breadCrumbBean")
@ManagedBean
@SessionScoped
public class BreadCrumbBean implements Serializable {

	private static final long serialVersionUID = 388804441118217062L;

	private MenuModel menuModel = new DefaultMenuModel();

	/**
	 * default constructor
	 * set bread crumb with 'Home Page i.e Project Page' page only.
	 */
	public BreadCrumbBean() {

		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create Project menuItem
		DefaultMenuItem dragDropProject = new DefaultMenuItem();
		dragDropProject.setValue("Project");
		dragDropProject.setCommand("#{breadCrumbBean.navigateDragDropProject}");

		// Add menuItems
		this.menuModel.addElement(dragDropProject);
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}
	
	/**
	 * set bread crumb with 'project' page only.
	 * @return link for project page
	 */
	public String navigateDragDropProject(){
		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create Project menuItem
		DefaultMenuItem dragDropProject = new DefaultMenuItem();
		dragDropProject.setValue("Project");
		dragDropProject.setCommand("#{breadCrumbBean.navigateDragDropProject}");
		dragDropProject.setUrl("/pages/dragDropProject.xhtml");

		// Add menuItems
		this.menuModel.addElement(dragDropProject);

		return "dragDropProject?faces-redirect=true&includeViewParams=true";
	}
	
	/**
	 * set bread crumb with 'project > building' page only.
	 * @return link for building page
	 */
	public String navigateDragDropBuilding(){
		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create Project menuItem
		DefaultMenuItem dragDropProject = new DefaultMenuItem();
		dragDropProject.setValue("Project");
		dragDropProject.setCommand("#{breadCrumbBean.navigateDragDropProject}");
		dragDropProject.setUrl("/pages/dragDropProject.xhtml");

		// Create Building menuItem
		DefaultMenuItem dragDropBuilding = new DefaultMenuItem();
		dragDropBuilding.setValue("Building");
		dragDropBuilding.setCommand("#{breadCrumbBean.navigateDragDropBuilding}");
		dragDropBuilding.setUrl("/pages/dragDropBuilding.xhtml");

		// Add menuItems
		this.menuModel.addElement(dragDropProject);
		this.menuModel.addElement(dragDropBuilding);

		return "dragDropBuilding?faces-redirect=true&includeViewParams=true";
	}
	
	/**
	 * set bread crumb with 'project > building > Floor' page only.
	 * @return link for floor page
	 */
	public String navigateDragDropFloor(){
		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create Project menuItem
		DefaultMenuItem dragDropProject = new DefaultMenuItem();
		dragDropProject.setValue("Project");
		dragDropProject.setCommand("#{breadCrumbBean.navigateDragDropProject}");
		dragDropProject.setUrl("/pages/dragDropProject.xhtml");

		// Create Building menuItem
		DefaultMenuItem dragDropBuilding = new DefaultMenuItem();
		dragDropBuilding.setValue("Building");
		dragDropBuilding.setCommand("#{breadCrumbBean.navigateDragDropBuilding}");
		dragDropBuilding.setUrl("/pages/dragDropBuilding.xhtml");

		// Create Floor menuItem
		DefaultMenuItem dragDropFloor = new DefaultMenuItem();
		dragDropFloor.setValue("Floor");
		dragDropFloor.setCommand("#{breadCrumbBean.navigateDragDropFloor}");
		dragDropFloor.setUrl("/pages/dragDropFloor.xhtml");

		// Add menuItems
		this.menuModel.addElement(dragDropProject);
		this.menuModel.addElement(dragDropBuilding);
		this.menuModel.addElement(dragDropFloor);

		return "dragDropFloor?faces-redirect=true&includeViewParams=true";
	}
	
	/**
	 * set bread crumb with 'project > building > Floor > Flat' page only.
	 * @return link for flat page
	 */
	public String navigateDragDropFlat(){
		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create Project menuItem
		DefaultMenuItem dragDropProject = new DefaultMenuItem();
		dragDropProject.setValue("Project");
		dragDropProject.setCommand("#{breadCrumbBean.navigateDragDropProject}");
		dragDropProject.setUrl("/pages/dragDropProject.xhtml");

		// Create Building menuItem
		DefaultMenuItem dragDropBuilding = new DefaultMenuItem();
		dragDropBuilding.setValue("Building");
		dragDropBuilding.setCommand("#{breadCrumbBean.navigateDragDropBuilding}");
		dragDropBuilding.setUrl("/pages/dragDropBuilding.xhtml");

		// Create Floor menuItem
		DefaultMenuItem dragDropFloor = new DefaultMenuItem();
		dragDropFloor.setValue("Floor");
		dragDropFloor.setCommand("#{breadCrumbBean.navigateDragDropFloor}");
		dragDropFloor.setUrl("/pages/dragDropFloor.xhtml");

		// Create Flat menuItem
		DefaultMenuItem dragDropFlat = new DefaultMenuItem();
		dragDropFlat.setValue("Flat");
		dragDropFlat.setCommand("#{breadCrumbBean.navigateDragDropFlat}");
		dragDropFlat.setUrl("/pages/dragDropFlat.xhtml");

		// Add menuItems
		this.menuModel.addElement(dragDropProject);
		this.menuModel.addElement(dragDropBuilding);
		this.menuModel.addElement(dragDropFloor);
		this.menuModel.addElement(dragDropFlat);

		return "dragDropFlat?faces-redirect=true&includeViewParams=true";
	}
	
	/**
	 * set bread crumb with 'project > building > Floor > Flat > Room' page only.
	 * @return link for room page
	 */
	public String navigateDragDropRoom(){
		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create Project menuItem
		DefaultMenuItem dragDropProject = new DefaultMenuItem();
		dragDropProject.setValue("Project");
		dragDropProject.setCommand("#{breadCrumbBean.navigateDragDropProject}");
		dragDropProject.setUrl("/pages/dragDropProject.xhtml");

		// Create Building menuItem
		DefaultMenuItem dragDropBuilding = new DefaultMenuItem();
		dragDropBuilding.setValue("Building");
		dragDropBuilding.setCommand("#{breadCrumbBean.navigateDragDropBuilding}");
		dragDropBuilding.setUrl("/pages/dragDropBuilding.xhtml");

		// Create Floor menuItem
		DefaultMenuItem dragDropFloor = new DefaultMenuItem();
		dragDropFloor.setValue("Floor");
		dragDropFloor.setCommand("#{breadCrumbBean.navigateDragDropFloor}");
		dragDropFloor.setUrl("/pages/dragDropFloor.xhtml");

		// Create Flat menuItem
		DefaultMenuItem dragDropFlat = new DefaultMenuItem();
		dragDropFlat.setValue("Flat");
		dragDropFlat.setCommand("#{breadCrumbBean.navigateDragDropFlat}");
		dragDropFlat.setUrl("/pages/dragDropFlat.xhtml");

		// Create Room menuItem
		DefaultMenuItem dragDropRoom = new DefaultMenuItem();
		dragDropRoom.setValue("Room");
		dragDropRoom.setCommand("#{breadCrumbBean.navigateDragDropRoom}");
		dragDropRoom.setUrl("/pages/dragDropRoom.xhtml");

		// Add menuItems
		this.menuModel.addElement(dragDropProject);
		this.menuModel.addElement(dragDropBuilding);
		this.menuModel.addElement(dragDropFloor);
		this.menuModel.addElement(dragDropFlat);
		this.menuModel.addElement(dragDropRoom);

		return "dragDropRoom?faces-redirect=true&includeViewParams=true";
	}
		

}
