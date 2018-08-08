package enersis.envisor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import enersis.envisor.entity.Bill;
import enersis.envisor.entity.BillType;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBill;
import enersis.envisor.entity.Project;
import enersis.envisor.service.BillService;
import enersis.envisor.service.BillTypeService;
import enersis.envisor.service.DistributionLineBillService;
import enersis.envisor.service.DistributionLineService;
import enersis.envisor.service.ProjectService;

@Component("billBean")
@ViewScoped
//@Scope("view")
// @SessionScoped
// @RequestScoped
@ManagedBean

public class BillBean extends AbstractBacking implements Serializable {
	private static final long serialVersionUID = 8481565273730002484L;

	private List<DistributionLine> distributionLines = new ArrayList<DistributionLine>();
	private List<DistributionLine> selectedDistributionLines = new ArrayList<DistributionLine>();
	private Project selectedProjectforDistLine = new Project();
	private List<String> distributionLineMatches = new ArrayList<String>();
	private String autocompleteString = "";
	private List<Bill> bills = new ArrayList<Bill>();
	private DistributionLine selectedDistributionLine;
	private Bill selectedBill;
	private String distributionLineToBindBill = "";
	private List<BillType> billTypes= new ArrayList<BillType>();
	private BillType selectedBillType = new BillType();
	private List<String> projectMatches = new ArrayList<String>();
	
	private String projectAutoCompleteString = "";
	

	private Date billDate;
	private Double charge;
	private String fileName;
	private Double usage;
	private String unit;

	@Autowired
	private DistributionLineService distributionLineService;
	
	@Autowired
	private DistributionLineBillService distributionLineBillService;
	
	@Autowired
	private ProjectService projectService ;
	
	@Autowired
	private BillTypeService billTypeService;
	
	@Autowired
	private BillService billService;

//	private BillServiceImpl billServiceImpl = new BillServiceImpl(); 
	
	@PostConstruct
	public void postConstruct() {

		billTypes= billTypeService.findAll();
		billDate = new DateTime().toDate();
		bills = billService.findAll();
	}

	public List<String> autoComplete(String query) {
		query.toLowerCase();
		distributionLineMatches.clear();
		List<DistributionLine> aCDistributionLines = distributionLineService.findAll();
		for (DistributionLine distributionLine: aCDistributionLines) {
			if (distributionLine.getName().toLowerCase().contains(query)) {
				distributionLineMatches.add(distributionLine.getName());
			}
		}
		return distributionLineMatches;
	}
	
	public List<String> projectAutoComplete(String query) {
//		System.out.println("gandalf");
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
//		if (projectAutoCompleteString.equals("")) {
//			System.out.println("equal");
//			distributionLines = distributionLineService.findAll();
//
//		} else {
//			System.out.println("equal deðil");
			selectedProjectforDistLine = projectService.findByProjectName(
					projectAutoCompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ proje"
					+ selectedProjectforDistLine.getProjectName());
			distributionLines = distributionLineService
					.findByProject(selectedProjectforDistLine);
			projectAutoCompleteString = "";
//		}

	}

	public void onSelect(SelectEvent event) {
		// System.out.println("onProjectSelect: seçilen proje "
		// + autocompleteString + "gan");
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (autocompleteString.equals("")) {
			System.out.println("equal");
			bills = billService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedDistributionLine = distributionLineService
					.findByDistributionLineName(autocompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ daðýtým hattý: "
					+ selectedDistributionLine.getName());
			bills = billService
					.findbyDistributionLine(selectedDistributionLine);
		}

	}

	public void deleteWithRelations() {

		billService.delete(selectedBill);
		// Faturas Silindi growlu
		bills = billService.findAll();
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Fatura,  tüm alt deðerleriyle birlikte silindi"));
	}

	public void bindBill() {
		Bill billToSave= new Bill();

//		billToSave.setDistributionLine(distributionLineService
//				.findByDistributionLineName(distributionLineToBindBill)
//				.get(0));
		billToSave.setBillType(selectedBillType);
		billToSave.setCharge(charge);
		billToSave.setDate(billDate);
		billToSave.setFileName(fileName);
		billToSave.setUsage(usage);
		billToSave.setUnit(unit);
		billToSave.setStatus((byte) 0);
		billToSave.setTransactionTime(new DateTime().toDate());
		billService.save(billToSave);
//		System.out.println("id: "+billToSave.getId());
		
		for (DistributionLine distributionLine : selectedDistributionLines) {
			DistributionLineBill distributionLineBill = new DistributionLineBill();
			distributionLineBill.setBill(billToSave);
			distributionLineBill.setDistributionLine(distributionLine);
			distributionLineBill.setStatus((byte) 0);
			distributionLineBill.setTransactionTime(new DateTime().toDate());
			distributionLineBillService.save(distributionLineBill);
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Baþarýlý!",
				"Fatura Eklendi "));
	}

	public List<String> getDistributionLineMatches() {
		return distributionLineMatches;
	}

	public void setDistributionLineMatches(List<String> distributionLineMatches) {
		this.distributionLineMatches = distributionLineMatches;
	}

	public String getAutocompleteString() {
		return autocompleteString;
	}

	public void setAutocompleteString(String autocompleteString) {
		this.autocompleteString = autocompleteString;
	}

	public List<Bill> getBills() {
		return bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	public DistributionLine getSelectedDistributionLine() {
		return selectedDistributionLine;
	}

	public void setSelectedDistributionLine(
			DistributionLine selectedDistributionLine) {
		this.selectedDistributionLine = selectedDistributionLine;
	}

	public Bill getSelectedBill() {
		return selectedBill;
	}

	public void setSelectedBill(Bill selectedBill) {
		this.selectedBill = selectedBill;
	}

	public String getDistributionLineToBindBill() {
		return distributionLineToBindBill;
	}

	public void setDistributionLineToBindBill(
			String distributionLineToBindBill) {
		this.distributionLineToBindBill = distributionLineToBindBill;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Double getCharge() {
		return charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Double getUsage() {
		return usage;
	}

	public void setUsage(Double usage) {
		this.usage = usage;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<BillType> getBillTypes() {
		return billTypes;
	}

	public void setBillTypes(List<BillType> billTypes) {
		this.billTypes = billTypes;
	}

	public BillType getSelectedBillType() {
		return selectedBillType;
	}

	public void setSelectedBillType(BillType selectedBillType) {
		this.selectedBillType = selectedBillType;
	}

	public String getProjectAutoCompleteString() {
		return projectAutoCompleteString;
	}

	public void setProjectAutoCompleteString(String projectAutoCompleteString) {
		this.projectAutoCompleteString = projectAutoCompleteString;
	}

	public List<String> getProjectMatches() {
		return projectMatches;
	}

	public void setProjectMatches(List<String> projectMatches) {
		this.projectMatches = projectMatches;
	}

	public Project getSelectedProjectforDistLine() {
		return selectedProjectforDistLine;
	}

	public void setSelectedProjectforDistLine(Project selectedProjectforDistLine) {
		this.selectedProjectforDistLine = selectedProjectforDistLine;
	}

	public List<DistributionLine> getDistributionLines() {
		return distributionLines;
	}

	public void setDistributionLines(List<DistributionLine> distributionLines) {
		this.distributionLines = distributionLines;
	}

	public List<DistributionLine> getSelectedDistributionLines() {
		return selectedDistributionLines;
	}

	public void setSelectedDistributionLines(
			List<DistributionLine> selectedDistributionLines) {
		this.selectedDistributionLines = selectedDistributionLines;
	}
}
