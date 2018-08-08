package enersis.envisor.beans;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import enersis.envisor.entity.Bill;
import enersis.envisor.entity.Building;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBill;
import enersis.envisor.entity.DistributionLineBuilding;
import enersis.envisor.entity.DistributionLineMeterType;
import enersis.envisor.entity.Flat;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.HeatMeter;
import enersis.envisor.entity.HeatMeterReading;
import enersis.envisor.entity.MeasurementTypeProject;
import enersis.envisor.entity.Period;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.Room;
import enersis.envisor.entity.WaterMeter;
import enersis.envisor.entity.WaterMeterReading;
import enersis.envisor.reporting.Calculations;
import enersis.envisor.reporting.DistributionLineBehaviours;
import enersis.envisor.reporting.FlatAndReadings;
import enersis.envisor.service.BillService;
import enersis.envisor.service.BuildingService;
import enersis.envisor.service.DistributionLineBillService;
import enersis.envisor.service.DistributionLineBuildingService;
import enersis.envisor.service.DistributionLineMeterTypeService;
import enersis.envisor.service.DistributionLineService;
import enersis.envisor.service.FlatService;
import enersis.envisor.service.HeatCostAllocatorReadingService;
import enersis.envisor.service.HeatCostAllocatorService;
import enersis.envisor.service.HeatMeterReadingService;
import enersis.envisor.service.HeatMeterService;
import enersis.envisor.service.MeasurementTypeProjectService;
import enersis.envisor.service.MeterTypeService;
import enersis.envisor.service.PeriodService;
import enersis.envisor.service.ProjectService;
import enersis.envisor.service.RoomService;
import enersis.envisor.service.WaterMeterReadingService;
import enersis.envisor.service.WaterMeterService;
import enersis.envisor.utils.AllocatorHeader;
import enersis.envisor.utils.Commons;
import enersis.envisor.utils.Constants;
import enersis.envisor.utils.EnergyHeader;
import enersis.envisor.utils.PrimefacesUtils;
import enersis.envisor.utils.ReportHeader;

@Component("reportingBean")
@ViewScoped
// @SessionScoped
// @RequestScoped
// @ApplicationScoped
// @Scope("view")
@ManagedBean
public class ReportingBean extends AbstractBacking implements Serializable {
	
	//region fields
	private static final long serialVersionUID = 6627459506694030465L;
	private UploadedFile file;
	// For
	// reporting---------------------------------------------------------------------------------------------------------------------
	private List<Room> rooms = new ArrayList<Room>();
	private List<Flat> flats = new ArrayList<Flat>();
	private List<ReportHeader> reportHeaders = new ArrayList<ReportHeader>(); // fatura
																				// bilgileri
	private List<EnergyHeader> energyHeaders = new ArrayList<EnergyHeader>(); // genel
																				// enerji
																				// paylaþým
																				// bilgileri
	private List<HeatCostAllocator> allocatorsByDistributionLine = new ArrayList<HeatCostAllocator>();
	private List<Period> periodsByProject = new ArrayList<Period>();
	private List<DistributionLine> distributionLines = new ArrayList<DistributionLine>(); // general
	private List<Flat> flatsByProject = new ArrayList<Flat>();
	private List<String> distributionLineMatches = new ArrayList<String>();
	private List<String> projectMatches = new ArrayList<String>();
	private List<String> periodStrings = new ArrayList<String>();
	List<HeatCostAllocatorReading> allocatorReadingsByStartPeriod = new ArrayList<HeatCostAllocatorReading>();
	List<HeatCostAllocatorReading> allocatorReadingsByEndPeriod = new ArrayList<HeatCostAllocatorReading>();
	// private List<String> endPeriodStrings = new ArrayList<String>();
	private Map<Flat, Double> allocatorValuesSumByFlat = new HashMap<Flat, Double>();
	private EnergyHeader energyHeader = new EnergyHeader();
	private EnergyHeader energyHeader2 = new EnergyHeader();

	private String projectAutoCompleteString = "";
	private String distributionLineAutoCompleteString = "";
	private String startPeriodString;
	private String endPeriodString;

	private Double endAllocatorvalues = 0.0;
	private Double startAllocatorvalues = 0.0;

	private Project selectedProject = new Project();
	private DistributionLine selectedDistributionLine;
	private Period selectedStartPeriod;
	private Period selectedEndPeriod;
	private Date readingPeriodDate;
	private Double additionalPrice;
	//endregion fields
	
	private int sayac=1;
	
	//CONSTANTS--------------------------------------------------------------------
	double allocatorDistributionConstant = 0.7;
	double commonAreasConstant= 1-allocatorDistributionConstant;
	double waterDistributionConstant = 0.2;

	//region services-----------------------------------------------------
	@Autowired
	private MeasurementTypeProjectService measurementTypeProjectService;
	@Autowired
	private DistributionLineMeterTypeService distributionLineMeterTypeService;
	@Autowired
	private HeatCostAllocatorReadingService heatCostAllocatorReadingService;
	@Autowired
	private HeatMeterReadingService heatMeterReadingService;
	@Autowired
	private WaterMeterReadingService waterMeterReadingService;
	@Autowired
	private HeatCostAllocatorService heatCostAllocatorService;
	@Autowired
	private HeatMeterService heatMeterService;
	@Autowired
	private WaterMeterService waterMeterService ;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private DistributionLineService distributionLineService;
	@Autowired
	private DistributionLineBuildingService distributionLineBuildingService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private FlatService flatService;
	@Autowired
	private PeriodService periodService;
	@Autowired
	private BillService billService;
	@Autowired
	private MeterTypeService meterTypeService;
	@Autowired
	private DistributionLineBillService distributionLineBillService;

	//endregion services
	@PostConstruct
	public void postConstruct() {
		selectedDistributionLine = new DistributionLine();
		// System.out.println("through eternity and beyooooooooooooooonddd!!!!");

	}

	public List<String> projectAutoComplete(String query) {
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

	public List<String> startPeriodAutoComplete(String query) {
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

	public void onProjectSelect(SelectEvent event) {
		System.out.println("onProjectSelect: seçilen proje " + projectAutoCompleteString);
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (projectAutoCompleteString.equals("")) {
			System.out.println("equal");
			distributionLines = distributionLineService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedProject = projectService.findByProjectName(projectAutoCompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ proje" + selectedProject.getProjectName());
			distributionLines = distributionLineService.findByProject(selectedProject);
			projectAutoCompleteString = "";
			periodsByProject = periodService.findByProject(selectedProject);
			periodStrings.clear(); // daðýtým hattý tekrar seçildiði için
			// dönemler sýfýrlanýp seçim için tekrar
			// hazýrlanýyor
			periodConverter();

		}

	}

	public void onDistributionLineSelect(SelectEvent event) {
		RequestContext context = RequestContext.getCurrentInstance();
		// context.execute("PF('statusDialog').show()");
		System.out.println("onDistributionLineSelect: seçilen daðýtým hattý: " + distributionLineAutoCompleteString + "gan");
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", event.getObject().toString()));
		if (distributionLineAutoCompleteString.equals("")) {
			// System.out.println("equal");
			// buildings = buildingService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedDistributionLine = distributionLineService.findByDistributionLineName(distributionLineAutoCompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ daðýtým hattý: " + selectedDistributionLine.getName());
			periodsByProject = periodService.findByProject(selectedProject);
			System.out.println("period update - period büyüklüðü: " + periodsByProject.size());
			distributionLineAutoCompleteString = "";
			periodStrings.clear(); // daðýtým hattý tekrar seçildiði için
									// dönemler sýfýrlanýp seçim için tekrar
									// hazýrlanýyor
			periodConverter();
			// context.execute("PF('statusDialog').hide()");
		}

	}

	public void fillManagementReport() {

		System.out.println("fill management");
		Double unitPrice = 0.27; // geçici olarak birim fiyat yerleþtirildi.
									// projenin altýna yerleþtirilebilir. ya da
									// ayrý tablo yapýsýnda tutulabilir.
		reportHeaders.clear();
		Building building = buildingService.findbyDistributionLine(selectedDistributionLine).get(0);
		flats = flatService.findbyBuilding(building);
		ReportHeader header = new ReportHeader();
		allocatorsByDistributionLine = heatCostAllocatorService.findByDistributionLine(selectedDistributionLine);
		header.setBillNo("Isýtma - 32501999");
		header.setPeriodStart(selectedStartPeriod.getDate());
		header.setPeriodEnd(selectedEndPeriod.getDate());
		header.setFirstReading(selectedStartPeriod.getDate().toString());
		// header.setLastindex(selectedEndPeriod.getBill().getUsage().toString());
		// header.setUsage(selectedEndPeriod.getBill().getUsage() );
		// header.setUsageKWH(15938.00);
		// header.setCharge(selectedEndPeriod.getBill().getCharge());
		reportHeaders.add(header);

		energyHeader.setAllocationType("Sayaçlardan");
		energyHeader.setEnergy(15938.00 * 0.7);
		// energyHeader.setPriceBySection(selectedEndPeriod.getBill().getCharge()*0.7);
		List<HeatCostAllocatorReading> allocatorReadingsByDistributionLineAndStartPeriod = new ArrayList<HeatCostAllocatorReading>();
		List<HeatCostAllocatorReading> allocatorReadingsByDistributionLineAndEndPeriod = new ArrayList<HeatCostAllocatorReading>();
		allocatorReadingsByDistributionLineAndStartPeriod = heatCostAllocatorReadingService.findByDistributionLineAndPeriod(selectedDistributionLine, selectedStartPeriod);
		allocatorReadingsByDistributionLineAndEndPeriod = heatCostAllocatorReadingService.findByDistributionLineAndPeriod(selectedDistributionLine, selectedEndPeriod);

		for (HeatCostAllocatorReading heatCostAllocatorReading : allocatorReadingsByDistributionLineAndStartPeriod) {
			startAllocatorvalues = startAllocatorvalues + heatCostAllocatorReading.getValue() * heatCostAllocatorReading.getHeatCostAllocator().getKges();
		}
		for (HeatCostAllocatorReading heatCostAllocatorReading : allocatorReadingsByDistributionLineAndEndPeriod) {
			endAllocatorvalues = endAllocatorvalues + heatCostAllocatorReading.getValue() * heatCostAllocatorReading.getHeatCostAllocator().getKges();
		}
		System.out.println("baþlangýç periyodu pay ölçer x kges deðeri: " + startAllocatorvalues);
		System.out.println("bitiþ periyodu pay ölçer x kges deðeri: " + endAllocatorvalues);
		energyHeader.setValueBySection(endAllocatorvalues - startAllocatorvalues);
		energyHeader.setUnit("Str");
		energyHeader.setUnitPrice(energyHeader.getPriceBySection() / energyHeader.getValueBySection());
		energyHeaders.add(energyHeader);
		// energyHeader.setValueBySection(allocatorReadingsByDistributionLineAndEndPeriod-allocatorReadingsByDistributionLineAndStartPeriod);
		// EnergyHeader energyHeader2 = new EnergyHeader();
		energyHeader2.setAllocationType("Ortak Alandan");
		energyHeader2.setEnergy(15938.00 * 0.3);
		// energyHeader2.setPriceBySection(selectedEndPeriod.getBill().getCharge()*0.3);
		List<Flat> flatsByDistributionLine = new ArrayList<Flat>();
		flatsByDistributionLine = flatService.findByDistributionLine(selectedDistributionLine);

		Integer totalArea = 0;
		for (Flat flat : flatsByDistributionLine) {
			totalArea = totalArea + flat.getArea();
		}
		energyHeader2.setValueBySection((double) totalArea);
		energyHeader2.setUnit("m2");
		energyHeader2.setUnitPrice(energyHeader2.getPriceBySection() / energyHeader2.getValueBySection());
		energyHeaders.add(energyHeader2);
	}

	public List<AllocatorHeader> allocatorsByFlat(Flat flat) {
		List<HeatCostAllocator> allocators = new ArrayList<HeatCostAllocator>();
		List<AllocatorHeader> allocatorHeaders = new ArrayList<AllocatorHeader>();
		Integer lastIndex = new Integer(0);
		Integer currentIndex = new Integer(0);
		allocators = heatCostAllocatorService.findByFlat(flat);
		// System.out.println("servis isteði");
		Double allocatorSumByFlat = 0.0;
		for (HeatCostAllocator allocator : allocators) {
			AllocatorHeader allocatorHeader = new AllocatorHeader();
			allocatorHeader.setRoomNo((int) allocator.getRoom().getOrderNo());
			allocatorHeader.setAllocatorSerialNo(allocator.getSerialNo());
			// lastIndex
			// =heatCostAllocatorReadingService.findByAllocatorAndPeriod(allocator,
			// selectedStartPeriod).getValue();
			boolean allocatorIsFound = false;
			int index = 0;
			while (allocatorIsFound == false) {
				// System.out.println("while döngüsü: "+index);
				if (allocatorReadingsByStartPeriod.get(index).getHeatCostAllocator().getSerialNo().equals(allocator.getSerialNo())) {
					lastIndex = allocatorReadingsByStartPeriod.get(index).getValue();
					allocatorIsFound = true;
				}
				index++;

			}
			allocatorIsFound = false;
			index = 0;
			allocatorHeader.setLastIndex(lastIndex);
			// currentIndex =
			// heatCostAllocatorReadingService.findByAllocatorAndPeriod(allocator,
			// selectedEndPeriod).getValue();

			while (allocatorIsFound == false) {
				if (allocatorReadingsByEndPeriod.get(index).getHeatCostAllocator().getSerialNo().equals(allocator.getSerialNo())) {
					currentIndex = allocatorReadingsByEndPeriod.get(index).getValue();
					allocatorIsFound = true;
				}
				index++;

			}
			allocatorIsFound = false;
			allocatorHeader.setCurrentIndex(currentIndex);
			allocatorHeader.setUsage(currentIndex - lastIndex);
			allocatorHeader.setUnit("HCA");
			allocatorHeader.setKges(allocator.getKges());
			allocatorHeader.setAllocatorusage(allocator.getKges() * (currentIndex - lastIndex));
			allocatorHeaders.add(allocatorHeader);
			allocatorSumByFlat = allocatorSumByFlat + allocatorHeader.getAllocatorusage();
		}
		allocatorValuesSumByFlat.put(flat, allocatorSumByFlat);
		return allocatorHeaders;
	}

	public Double getStrByFlat(Flat flat) {
		return allocatorValuesSumByFlat.get(flat);
	}

	public String getPrintableDateAsString(Date date) {
		return PrimefacesUtils.DateTimeToDateString(date);
	}

	public void preprocessPDF(Object document) throws IOException, BadElementException, DocumentException {

		Document pdf = (Document) document;
		pdf.open();
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logo = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "images" + File.separator + "thumb.png";
		// pdf.add(Image.getInstance(logo));
		// pdf.setMargins(0, 0, 0, 0);

	}

	public void onStartPeriodSelect() {
		System.out.println("on start period select");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
		DateTime dt = formatter.parseDateTime(startPeriodString);
		selectedStartPeriod = periodService.findByProject_Date(selectedProject, dt.toDate());
		System.out.println("date halinde: " + selectedStartPeriod.getDate());
		allocatorReadingsByStartPeriod = heatCostAllocatorReadingService.findByPeriod(selectedStartPeriod);
	}

	public void onEndPeriodSelect() {
		System.out.println("on end period select");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
		DateTime dt = formatter.parseDateTime(endPeriodString);
		selectedEndPeriod = periodService.findByProject_Date(selectedProject, dt.toDate());
		System.out.println("date halinde: " + selectedEndPeriod.getDate());
		allocatorReadingsByEndPeriod = heatCostAllocatorReadingService.findByPeriod(selectedEndPeriod);
	}

	public List<String> periodConverter() {
		List<Period> periods = new ArrayList<Period>();
		periods = periodService.findByProject(selectedProject);
		for (Period period : periods) {
			periodStrings.add(period.getDate().toString());
		}
		System.out.println("period strings size: " + periodStrings.size());
		return periodStrings;
	}

	public List<HeatCostAllocator> getHeatCostAllocatorsByFlat(Flat flat) {
		List<HeatCostAllocator> heatCostAllocators = new ArrayList<HeatCostAllocator>();
		heatCostAllocators = heatCostAllocatorService.findByFlat(flat);
		return heatCostAllocators;
	}

	public Room getRoomOfHeatCostAllocator(HeatCostAllocator heatCostAllocator) {
		return heatCostAllocator.getRoom();
	}

	public HeatCostAllocatorReading getLastHeatCostAllocatorReading(HeatCostAllocator heatCostAllocator) {
		return heatCostAllocatorReadingService.findLastByAllocator(heatCostAllocator);
	}

	public HeatCostAllocatorReading getHeatCostAllocatorReadingByPeriod(HeatCostAllocator heatCostAllocator, Period period) {
		return heatCostAllocatorReadingService.findByAllocatorAndPeriod(heatCostAllocator, period);
	}

	public void getPeriodsByDistributionLine() {
		periodsByProject = periodService.findbyDistributionLine(selectedDistributionLine);
	}

	public void makeReport() throws DocumentException, MalformedURLException, IOException {
		System.out.println("EK BEDEL: "+ additionalPrice);
		
		//!!!! NOT REAL COMMENT!!! ONLY FOR FOLDING PURPOSES
		//region PreperationForReporting
		boolean isProjectHeating = false;
		boolean isProjectCooling = false;
		boolean isProjectWater = false;
		boolean isProjectNaturalGas = false;
		boolean isProjectElectricity = false;

		boolean isIP = false;
		boolean isIS = false;
		boolean isSS = false;
		boolean isGS = false;
		boolean isES = false;
		boolean isSýSS = false;
		boolean isSoSS = false;
		boolean isBoyRefS = false;
		boolean IRefS = false;

		boolean isFirstPage = true; // fill general info if first project
									// control
		
		HashMap<Integer, List<Calculations>> distributionLineCalcsMap= new HashMap<Integer, List<Calculations>>();
		HashMap<Integer, DistributionLineBehaviours> distributionLine_BehavioursMap = new HashMap<Integer, DistributionLineBehaviours>();
		List<MeasurementTypeProject> measurementTypeProjectsByProject = measurementTypeProjectService.findByProject(selectedProject);
		for (MeasurementTypeProject measurementTypeProject : measurementTypeProjectsByProject) {
			// Projede sunulan hizmetler neler?
			if (measurementTypeProject.getMeasurementType().getType().equals("Isýtma"))
				isProjectHeating = true;
			if (measurementTypeProject.getMeasurementType().getType().equals("Soðutma"))
				isProjectCooling = true;
			if (measurementTypeProject.getMeasurementType().getType().equals("Su"))
				isProjectWater = true;
			if (measurementTypeProject.getMeasurementType().getType().equals("Doðalgaz"))
				isProjectNaturalGas = true;
			if (measurementTypeProject.getMeasurementType().getType().equals("Elektrik"))
				isProjectElectricity = true;
		}
		List<DistributionLine> distributionLinesByProject = distributionLineService.findByProject(selectedProject);
		System.out.println("make report");
		System.out.println("selected project: " + selectedProject.getProjectName());

		System.out.println("Baþlangýç saati:" + new DateTime().toDate().toString());

		// PROCESS STARTS
		// HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		List<Building> buildingsByProject = buildingService.findByProject(selectedProject);
		List<Flat> flatsByProject = flatService.findByProject(selectedProject);
		Double additionalPriceByFlat = additionalPrice/flatsByProject.size();
		System.out.println("daire sayýsý(many to many sayý kontrolü): " + flatsByProject.size());
		List<Room> roomsByProject = roomService.findByProject(selectedProject);
		List<HeatCostAllocator> heatCostAllocatorsByproject = heatCostAllocatorService.findByProject(selectedProject);
		List<HeatCostAllocatorReading> heatCostAllocatorReadingsByProjectAndEndPeriod = heatCostAllocatorReadingService.findByPeriod(selectedEndPeriod);
		List<HeatCostAllocatorReading> heatCostAllocatorReadingsByProjectAndStartPeriod = heatCostAllocatorReadingService.findByPeriod(selectedStartPeriod);
		List<HeatMeter> heatMetersByProject = heatMeterService.findByProject(selectedProject);
		List<HeatMeterReading> heatMeterReadingsByProjectAndEndPeriod = heatMeterReadingService.findByPeriod(selectedEndPeriod);
		List<HeatMeterReading> heatMeterReadingsByProjectAndStartPeriod = heatMeterReadingService.findByPeriod(selectedStartPeriod);
		List<WaterMeter> waterMetersByProject = waterMeterService.findByProject(selectedProject);
		List<WaterMeterReading> waterMeterReadingsByProjectAndEndPeriod = waterMeterReadingService.findByPeriod(selectedEndPeriod);
		List<WaterMeterReading> waterMeterReadingsByProjectAndStartPeriod = waterMeterReadingService.findByPeriod(selectedStartPeriod);
		List<DistributionLineBuilding> distributionLineBuildingsByProject = distributionLineBuildingService.findByProject(selectedProject);
		List<DistributionLineMeterType> distributionLineMeterTypesByProject = distributionLineMeterTypeService.findByProject(selectedProject);
		List<DistributionLineBill> distributionLineBillsByProject =distributionLineBillService.findByProject(selectedProject);
		List<Bill> billsByPeriodAndProject = billService.findByPeriod(periodService.findByProject_Date(selectedProject, selectedEndPeriod.getDate()));
		System.out.println("SAYI111 !!= "+distributionLinesByProject.size());
		distributionLinesByProject = Commons.orderDistLinesByType(distributionLinesByProject, distributionLineMeterTypesByProject);
		System.out.println("SAYI222 !!= "+distributionLinesByProject.size());
		System.out.println("selectedStartperiod id " + selectedStartPeriod.getId());
		System.out.println("billsByPeriod: " + billService.findByPeriod(selectedStartPeriod).size());
		String filename = selectedProject.getProjectName()+" Rapor "+ (selectedEndPeriod.getDate().getMonth()+1)+". Ay.pdf";
		Font fontObl10Hel =FontFactory.getFont(FontFactory.HELVETICA_BOLD,  "Cp857", 10, Font.ITALIC);
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, "Cp857", 10);
		Font headerCellsfont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, "Cp857", 8);
		Font innerCellsfont = FontFactory.getFont(FontFactory.HELVETICA, "Cp857", 7);
		Font totalizationFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, "Cp857", 7);
		Font linkFont = FontFactory.getFont(FontFactory.HELVETICA,  "Cp857", 7, Font.UNDERLINE);
		NumberFormat formatter = new DecimalFormat("#0.00");
		Document document = new Document(PageSize.A4);
		document.setMargins(10, 10, 10, 10);

		// baþ tablolar
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logoPath = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "images" + File.separator + "enersislogo.gif";
		System.out.println("logo path: " + logoPath);
		Image logoImage = Image.getInstance(logoPath);
		logoImage.setAlignment(Element.ALIGN_RIGHT);
		
		
		PdfPTable headerTable = new PdfPTable(2);
		headerTable.setWidthPercentage(90);
		PdfPCell leftHeaderCell = new PdfPCell();
		leftHeaderCell.setBorder(Rectangle.NO_BORDER);
		Phrase addressPhrase = new Phrase();
		addressPhrase.add(new Chunk("www.enersis.com.tr", linkFont));
		addressPhrase.add(new Chunk("         Web Rapor",innerCellsfont));
		addressPhrase.add(Chunk.NEWLINE);
		addressPhrase.add(new Chunk(selectedProject.getProjectName(),totalizationFont));
		
		leftHeaderCell.addElement(addressPhrase);
		headerTable.addCell(leftHeaderCell);
		PdfPCell rightHeaderCell = new PdfPCell();
		rightHeaderCell.setBorder(Rectangle.NO_BORDER);
		rightHeaderCell.addElement(logoImage);
		headerTable.addCell(rightHeaderCell);
		
		
		
		
		
		
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		document.add(headerTable);
		document.add(Chunk.NEWLINE);
		
		PdfPTable headerTitleTable = new PdfPTable(12);
		headerTitleTable.setWidthPercentage(90);
		PdfPCell headerTitleLeftCell = new PdfPCell(new Phrase("Tüketim Raporu", fontObl10Hel));
		headerTitleLeftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		headerTitleLeftCell.setBorder(Rectangle.NO_BORDER);
		headerTitleLeftCell.setColspan(3);
		PdfPCell headerTitleMiddleCell = new PdfPCell(new Phrase(Commons.truncateDate(selectedStartPeriod.getDate())+"  -  "+Commons.truncateDate(selectedEndPeriod.getDate())+" Dönemi", fontObl10Hel));
		headerTitleMiddleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerTitleMiddleCell.setBorder(Rectangle.NO_BORDER);
		headerTitleMiddleCell.setColspan(6);
		PdfPCell headerTitleRightCell = new PdfPCell();
		headerTitleRightCell.setBorder(Rectangle.NO_BORDER);
		headerTitleRightCell.setColspan(3);
		headerTitleTable.addCell(headerTitleLeftCell);
		headerTitleTable.addCell(headerTitleMiddleCell);
		headerTitleTable.addCell(headerTitleRightCell);
//		headerTitleTable.setSpacingAfter(20);
		document.add(headerTitleTable);
		document.add(Chunk.NEWLINE);
//		Chunk line1 = new Chunk(new LineSeparator(0.5f, 90, Color.BLACK, Element.ALIGN_BASELINE, -5));
//		Chunk line2 = new Chunk(new LineSeparator(0.5f, 90, Color.BLACK, Element.ALIGN_TOP, 10));
//		Paragraph paragraph = new Paragraph();
////		paragraph.set
//		paragraph.add(line1);
//		paragraph.add(Chunk.NEWLINE);
//		paragraph.add(line2);
//		document.add(paragraph);
//		document.add(Chunk.NEWLINE);
		LineSeparator lineSeparator1= new LineSeparator();
		lineSeparator1.setPercentage(90);
		lineSeparator1.setOffset(-2);
		document.add(lineSeparator1);
		LineSeparator lineSeparator2 = new LineSeparator();
		lineSeparator2.setOffset(-5);
		lineSeparator2.setPercentage(90);
		document.add(lineSeparator2);
		
		
		
		PdfPTable generalTable = new PdfPTable(8); // Genel tablo
		generalTable.setWidthPercentage(90);
		generalTable.setSpacingBefore(20);
		PdfPTable expenditureTable = new PdfPTable(6); // tüketim tablosu
		expenditureTable.setWidthPercentage(90);
		
		
		
		PdfPCell headerCell = new PdfPCell(new Phrase("Genel Özellikler", headerFont));
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setColspan(8);
		generalTable.addCell(headerCell);
		PdfPCell cell0 = new PdfPCell(new Phrase("Fatura No", headerCellsfont));
		generalTable.addCell(cell0);
		PdfPCell cell1 = new PdfPCell(new Phrase("Dönem Baþlangýç", headerCellsfont));
		generalTable.addCell(cell1);
		PdfPCell cell2 = new PdfPCell(new Phrase("Dönem bitiþ", headerCellsfont));
		generalTable.addCell(cell2);
		PdfPCell cell3 = new PdfPCell(new Phrase("Ýlk Okuma", headerCellsfont));
		generalTable.addCell(cell3);
		PdfPCell cell4 = new PdfPCell(new Phrase("Son Endeks", headerCellsfont));
		generalTable.addCell(cell4);
		PdfPCell cell5 = new PdfPCell(new Phrase("Tüketim", headerCellsfont));
		generalTable.addCell(cell5);
		PdfPCell cell6 = new PdfPCell(new Phrase("TüketimKWH", headerCellsfont));
		generalTable.addCell(cell6);
		PdfPCell cell7 = new PdfPCell(new Phrase("Tutar2", headerCellsfont));
		generalTable.addCell(cell7);
		generalTable.setHeaderRows(1);
		for (Bill bill : billsByPeriodAndProject) { // projedeki her fatura
													// yazýlýyor
			generalTable.addCell(new Phrase(bill.getBillType().getType(), innerCellsfont));
			generalTable.addCell(new Phrase(new LocalDate(selectedStartPeriod.getDate()).toString(), innerCellsfont));
			generalTable.addCell(new Phrase(new LocalDate(selectedEndPeriod.getDate()).toString(), innerCellsfont));
			generalTable.addCell(new Phrase(bill.getUsage().toString(), innerCellsfont));
			generalTable.addCell(new Phrase(bill.getUsage().toString(), innerCellsfont));
			generalTable.addCell(new Phrase(bill.getUsage().toString(), innerCellsfont));
			generalTable.addCell(new Phrase(bill.getUnit(), innerCellsfont));
			generalTable.addCell(new Phrase(((Double) bill.getCharge()).toString(), innerCellsfont));
		}
		generalTable.setSpacingAfter(20f);
		document.add(generalTable);
		System.out.println("genel tablo eklendi");

//		// genel tüketim tablosu dolduruluyor
//		PdfPCell expHeaderCell = new PdfPCell(new Phrase("Birim Fiyat Hesaplarý", headerFont));
//		expHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		expHeaderCell.setColspan(6);
//		expenditureTable.addCell(expHeaderCell);
//		PdfPCell expcell0 = new PdfPCell(new Phrase("Ölçüm Tipi", headerCellsfont));
//		expenditureTable.addCell(expcell0);
//		PdfPCell expcell1 = new PdfPCell(new Phrase("Enerji Daðýlýmý", headerCellsfont));
//		expenditureTable.addCell(expcell1);
//		PdfPCell expcell2 = new PdfPCell(new Phrase("Tutar", headerCellsfont));
//		expenditureTable.addCell(expcell2);
//		PdfPCell expcell3 = new PdfPCell(new Phrase("Deðerler", headerCellsfont));
//		expenditureTable.addCell(expcell3);
//		PdfPCell expcell4 = new PdfPCell(new Phrase("Birim", headerCellsfont));
//		expenditureTable.addCell(expcell4);
//		PdfPCell expcell5 = new PdfPCell(new Phrase("Birim Fiyat", headerCellsfont));
//		expenditureTable.addCell(expcell5);
//		expenditureTable.setHeaderRows(1);
//		System.out.println("SAYI3 !!= "+distributionLinesByProject.size());
		
		PdfPTable unitCalculationsTable = new PdfPTable(9);
		PdfPTable heatCalcsTable = new PdfPTable(1);
		PdfPTable unitCalcsleftTable = new PdfPTable(1);
		
		//endregion PreperationForReporting
		
		
		// PROJENÝN DAÐITIM HATLARI GEZÝLMEYE BAÞLANIYOR
		for (DistributionLine distributionLine : distributionLinesByProject) {
			boolean isIPInDistributionLine = false;
			boolean isISInDistributionLine = false;
			boolean isSSInDistributionLine = false;
			boolean isGSInDistributionLine = false;
			boolean isESInDistributionLine = false;
			boolean isSýSSInDistributionLine = false;
			boolean isSoSSInDistributionLine = false;
			boolean isBoyRefSInDistributionLine = false;
			boolean IRefSInDistributionLine = false;
			List<Calculations> distCalculations = new ArrayList<Calculations>();
			System.out.println("ÞU AN ÝÞLENEN DAÐITIM HATTI: "+distributionLine.getName());
			
			DistributionLineBehaviours behavioursByDistributionLine = new DistributionLineBehaviours();
			//daðýtým hattýnýn binalarý
			List<Building> buildingsByDistributionLine = new ArrayList<Building>();
			for (DistributionLineBuilding distributionLineBuilding : distributionLineBuildingsByProject) {
				if(distributionLineBuilding.getDistributionLine().getId()==distributionLine.getId())
				{
					buildingsByDistributionLine.add(distributionLineBuilding.getBuilding());
				}
			}
			//daðýtým hattýnýn daireleri
			List<Flat> flatsBybuilding = new ArrayList<Flat>();
			for (Building building : buildingsByDistributionLine) {
				flatsBybuilding.addAll(building.getFlats());
			}
			//dairelerin toplam alaný
			Double totalFlatsAreaByDistributionLine = 0.0;
			for (Flat flat : flatsBybuilding) {
				totalFlatsAreaByDistributionLine = totalFlatsAreaByDistributionLine + flat.getArea();
			}
			
			for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypesByProject) {
				
				if (distributionLineMeterType.getDistributionLine().getId() == distributionLine.getId()) {
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("IP"))
						isIPInDistributionLine = true;
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("IS"))
						isISInDistributionLine = true;
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SS"))
						isSSInDistributionLine = true;
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("GS"))
						isGSInDistributionLine = true;
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("ES"))
						isESInDistributionLine = true;
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SýSS"))
						isSýSSInDistributionLine = true;
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SoSS"))
						isSoSSInDistributionLine = true;
				}

			}
			List<DistributionLineMeterType> distributionLineMeterTypesByDistLine = new ArrayList<DistributionLineMeterType>();
//			System.out.println("DAÐITIMHATTIÖLÇÜMTÝPÝ BÜYÜKLÜÐÜ:   "+ distributionLineMeterTypesByDistLine.size());
			for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypesByProject) {
				if(distributionLineMeterType.getDistributionLine().getId()==distributionLine.getId())
				{
//					System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
					distributionLineMeterTypesByDistLine.add(distributionLineMeterType);
				}
			}
			
			//Daðýtým hattýnýn kombinasyonu hesaplanýyor
			Integer combination =0;
				for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypesByDistLine) {
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("IP") || distributionLineMeterType.getMeterType().getAbbreviation().contains("IS"))
					{
					//	 bu daðýtým hattý ýsýtma hattýdýr
						behavioursByDistributionLine.TYPEOFDISTRIBUTIONLINE=Constants.ISITMA;
						
					}
					
					
					// EÐER DAÐITIM HATTINDA SICAK SU SAYACI VARSA !!!
//					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SýSS"))
//					{
//						// bu daðýtým hattý ýsýtma hattýdýr
//					behavioursByDistributionLine.TYPEOFDISTRIBUTIONLINE=Constants.SU;
//					}
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SoSS") )
					{
						// bu daðýtým hattý ýsýtma hattýdýr
						behavioursByDistributionLine.TYPEOFDISTRIBUTIONLINE=Constants.SU;
//						System.out.println("su daðýtým hattý");
//						combination = combination + distributionLineMeterType.getMeterType().getEnum_();
					}
					combination = combination + distributionLineMeterType.getMeterType().getEnum_();
				}
				System.out.println("combination:"+combination);
				behavioursByDistributionLine.COMBINATIONOFDISTRIBUTIONLINE=combination;
			//__________________________Daðýtým hattýnýn kombinasyonu hesaplandý________________________________________________________________________________________-
			
				
				
				
				//daðýtým hattýna ait faturalarýn bulunmasý. tamamlanmamýþ durumda. TEKRAR BAKILACAK!!!!!!!!
			List<DistributionLineBill> distributionLineBillsByDistributionLine = new ArrayList<DistributionLineBill>();
			
				List<Bill> billsOfDistributionLine = new ArrayList<Bill>();
			
			for (DistributionLineBill distributionLineBill : distributionLineBillsByProject) {
				if(distributionLineBill.getDistributionLine().getId()==distributionLine.getId())
				{
					billsOfDistributionLine.add(distributionLineBill.getBill());
				}
			}
				
				
			PdfPCell distLineHeaderCell = new PdfPCell(new Phrase("Kazan: "+distributionLine.getName(),headerCellsfont ));
			distLineHeaderCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			distLineHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			distLineHeaderCell.setColspan(9);
			unitCalculationsTable.addCell(distLineHeaderCell);
			
			PdfPCell distTypeHeaderCell = new PdfPCell();
			distTypeHeaderCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			distTypeHeaderCell.setColspan(9);
			if(behavioursByDistributionLine.TYPEOFDISTRIBUTIONLINE==Constants.ISITMA)
				distTypeHeaderCell.setPhrase(new Phrase("Isýtma", headerCellsfont));
			if(behavioursByDistributionLine.TYPEOFDISTRIBUTIONLINE==Constants.SU)
				distTypeHeaderCell.setPhrase(new Phrase("Su", headerCellsfont));
			unitCalculationsTable.addCell(distTypeHeaderCell);
			
			
			
			PdfPTable unitCalculationsTableHeaderTable = new PdfPTable(36);
			PdfPCell  unitCalculationsTableHeaderInnerCellInstance = new PdfPCell();
			unitCalculationsTableHeaderInnerCellInstance.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
			unitCalculationsTableHeaderInnerCellInstance.setColspan(12);
			unitCalculationsTableHeaderInnerCellInstance.setPhrase(new Phrase("Tüketim Daðýlýmý",totalizationFont ));
			unitCalculationsTableHeaderTable.addCell(unitCalculationsTableHeaderInnerCellInstance);
			unitCalculationsTableHeaderInnerCellInstance.setHorizontalAlignment(Element.ALIGN_CENTER);
			unitCalculationsTableHeaderInnerCellInstance.setPhrase(new Phrase("Fatura Tutarý",totalizationFont ));
			unitCalculationsTableHeaderInnerCellInstance.setColspan(8);
			unitCalculationsTableHeaderTable.addCell(unitCalculationsTableHeaderInnerCellInstance);
//			unitCalculationsTableHeaderInnerCellInstance.setHorizontalAlignment(Element.ALIGN_CENTER);
			unitCalculationsTableHeaderInnerCellInstance.setPhrase(new Phrase("Deðerler",totalizationFont ));
			unitCalculationsTableHeaderTable.addCell(unitCalculationsTableHeaderInnerCellInstance);
			unitCalculationsTableHeaderInnerCellInstance.setPhrase(new Phrase("Birim Fiyat",totalizationFont ));
			unitCalculationsTableHeaderTable.addCell(unitCalculationsTableHeaderInnerCellInstance);	
			PdfPCell unitCalculationsTableHeaderCell = new PdfPCell(unitCalculationsTableHeaderTable);
			unitCalculationsTableHeaderCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
			unitCalculationsTableHeaderCell.setColspan(9);
			unitCalculationsTable.addCell(unitCalculationsTableHeaderCell);
			
			PdfPTable billrowTable = new PdfPTable(36);
			PdfPCell calcsInnerCellA = new PdfPCell();PdfPCell calcsInnerCellB = new PdfPCell();
			PdfPCell calcsInnerCellC = new PdfPCell();PdfPCell calcsInnerCellD = new PdfPCell();PdfPCell calcsInnerCellE = new PdfPCell();
			calcsInnerCellA.setColspan(6);calcsInnerCellA.setHorizontalAlignment(Element.ALIGN_LEFT);calcsInnerCellA.setBorder(Rectangle.NO_BORDER);
			calcsInnerCellB.setColspan(4);calcsInnerCellB.setHorizontalAlignment(Element.ALIGN_RIGHT);calcsInnerCellB.setBorder(Rectangle.NO_BORDER);
			calcsInnerCellC.setColspan(2);calcsInnerCellC.setHorizontalAlignment(Element.ALIGN_LEFT);calcsInnerCellC.setBorder(Rectangle.NO_BORDER);
			calcsInnerCellD.setColspan(4);calcsInnerCellD.setHorizontalAlignment(Element.ALIGN_RIGHT);calcsInnerCellD.setBorder(Rectangle.NO_BORDER);	
			calcsInnerCellE.setColspan(4);calcsInnerCellE.setHorizontalAlignment(Element.ALIGN_LEFT);calcsInnerCellE.setBorder(Rectangle.NO_BORDER);	
			for (Bill bill :billsOfDistributionLine) {
				System.out.println("Fatura :" + bill.getFileName());
				calcsInnerCellA.setPhrase(new Phrase(bill.getBillType().getType(), innerCellsfont)); // fatura tipi
				billrowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(bill.getUsage()+"", innerCellsfont)); // tüketim
				billrowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(bill.getUnit(), innerCellsfont)); // tüketimin birimi
				billrowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(bill.getCharge()+"", innerCellsfont));
				billrowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				billrowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				billrowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				billrowTable.addCell(calcsInnerCellE);
				billrowTable.addCell(calcsInnerCellD);
				billrowTable.addCell(calcsInnerCellE);
				
			}
			PdfPCell billRowCell = new PdfPCell(billrowTable);
			billRowCell.setColspan(9);
			billRowCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
			unitCalculationsTable.addCell(billRowCell);
			
			//eðer daðýtým hattýnda sadece IP var ise
			if(behavioursByDistributionLine.COMBINATIONOFDISTRIBUTIONLINE==Constants.IP)
			{
				Bill heatBill = new Bill();
				for (Bill bill : billsOfDistributionLine) {
					if(bill.getBillType().getId()==Constants.BILL_NGAS)heatBill = bill;
				}
				// DAÐITIM HATTINA GÖRE PAY ÖLÇER OKUMALARI
				List<HeatCostAllocatorReading> heatCostAllocatorReadingsOfStartPeriodByDistributionLine = new ArrayList<HeatCostAllocatorReading>();
				for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndStartPeriod) {
				DistributionLine distributionLineOfReading = heatCostAllocatorService.findDistributionLineOfReading(heatCostAllocatorReading, distributionLineBuildingsByProject);
				if(distributionLineOfReading.getId()==distributionLine.getId()) heatCostAllocatorReadingsOfStartPeriodByDistributionLine.add(heatCostAllocatorReading);
				}
				List<HeatCostAllocatorReading> heatCostAllocatorReadingsOfEndPeriodByDistributionLine = new ArrayList<HeatCostAllocatorReading>();
				for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndEndPeriod) {
				DistributionLine distributionLineOfReading = heatCostAllocatorService.findDistributionLineOfReading(heatCostAllocatorReading, distributionLineBuildingsByProject);
				if(distributionLineOfReading.getId()==distributionLine.getId()) heatCostAllocatorReadingsOfEndPeriodByDistributionLine.add(heatCostAllocatorReading);
				}
				Double totalStrs= totalStrOfAllocatorReadings(heatCostAllocatorReadingsOfStartPeriodByDistributionLine, heatCostAllocatorReadingsOfEndPeriodByDistributionLine);
				//__________________________________________________________________________________________________________________________________________||||||||||||||||||
				Double totalAllocatorUsageKWH ;
				Double totalAllocatorCharge ;
			 	totalAllocatorUsageKWH = heatBill.getUsage();
			 	totalAllocatorCharge = heatBill.getCharge();
				PdfPTable allocatorRowTable = new PdfPTable(36);
				calcsInnerCellA.setPhrase(new Phrase("  Isýtma(Pay Ölçer)", innerCellsfont)); //iki karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %70 Sayaç", innerCellsfont)); //altý karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH*allocatorDistributionConstant), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge*allocatorDistributionConstant), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalStrs), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("Str", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format((heatBill.getCharge()*allocatorDistributionConstant)/totalStrs), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/Str", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %30 Ortak", innerCellsfont)); //altý karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH*(1-allocatorDistributionConstant)), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge*(1-allocatorDistributionConstant)), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalFlatsAreaByDistributionLine), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("m2", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format((heatBill.getCharge()*commonAreasConstant)/totalFlatsAreaByDistributionLine), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/m2", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				PdfPCell allocatorRowTableCell = new PdfPCell(allocatorRowTable);
				allocatorRowTableCell.setColspan(9);
				allocatorRowTableCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
				
				unitCalculationsTable.addCell(allocatorRowTableCell);
			}

			
			
			// eðer daðýtým hattýnda IP ve SISS var ise
			if(behavioursByDistributionLine.COMBINATIONOFDISTRIBUTIONLINE==Constants.IP_SýSS)
			{
				Bill waterBill = new Bill();
				Bill heatBill = new Bill();
				for (Bill bill : billsOfDistributionLine) {
					if(bill.getBillType().getId()==Constants.BILL_NGAS)heatBill = bill;
					if(bill.getBillType().getId()==Constants.BILL_WATER)waterBill = bill;
				}
				// DAÐITIM HATTINA GÖRE PAY ÖLÇER OKUMALARI
				List<HeatCostAllocatorReading> heatCostAllocatorReadingsOfStartPeriodByDistributionLine = new ArrayList<HeatCostAllocatorReading>();
				for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndStartPeriod) {
				DistributionLine distributionLineOfReading = heatCostAllocatorService.findDistributionLineOfReading(heatCostAllocatorReading, distributionLineBuildingsByProject);
				if(distributionLineOfReading.getId()==distributionLine.getId()) heatCostAllocatorReadingsOfStartPeriodByDistributionLine.add(heatCostAllocatorReading);
				}
				List<HeatCostAllocatorReading> heatCostAllocatorReadingsOfEndPeriodByDistributionLine = new ArrayList<HeatCostAllocatorReading>();
				for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndEndPeriod) {
				DistributionLine distributionLineOfReading = heatCostAllocatorService.findDistributionLineOfReading(heatCostAllocatorReading, distributionLineBuildingsByProject);
				if(distributionLineOfReading.getId()==distributionLine.getId()) heatCostAllocatorReadingsOfEndPeriodByDistributionLine.add(heatCostAllocatorReading);
				}
				Double totalStrs= totalStrOfAllocatorReadings(heatCostAllocatorReadingsOfStartPeriodByDistributionLine, heatCostAllocatorReadingsOfEndPeriodByDistributionLine);
				//__________________________________________________________________________________________________________________________________________||||||||||||||||||

				List<WaterMeterReading> watermeterReadingsOfStartPeriodByDistributionLine = new ArrayList<WaterMeterReading>();
				for (WaterMeterReading waterMeterReading: waterMeterReadingsByProjectAndStartPeriod) {
					DistributionLine distributionLineOfReading = waterMeterReadingService.findDistributionLineOfReading(waterMeterReading, distributionLineBuildingsByProject);
					if(distributionLineOfReading.getId()==distributionLine.getId()) watermeterReadingsOfStartPeriodByDistributionLine.add(waterMeterReading);
					}
				List<WaterMeterReading> watermeterReadingsOfEndPeriodByDistributionLine = new ArrayList<WaterMeterReading>();
				for (WaterMeterReading waterMeterReading: waterMeterReadingsByProjectAndEndPeriod) {
					DistributionLine distributionLineOfReading = waterMeterReadingService.findDistributionLineOfReading(waterMeterReading, distributionLineBuildingsByProject);
					if(distributionLineOfReading.getId()==distributionLine.getId()) watermeterReadingsOfEndPeriodByDistributionLine.add(waterMeterReading);
					}
				Double totalAllocatorUsageKWH ;
				Double totalAllocatorCharge ;
			 	Double totalWaterMeterCharge;
				Double totalWaterMeterUsageM3 = totalConsumptionsOfWaterMeterReadings(watermeterReadingsOfStartPeriodByDistributionLine, watermeterReadingsOfEndPeriodByDistributionLine);
			 	Double totalWaterMeterUsageKWH = (1-waterDistributionConstant)*(1200*waterBill.getUsage()*(60-10)/8250*10.8);
			 	System.out.println("water meter usage KWH  "+ totalWaterMeterUsageKWH);
			 	System.out.println("heat bill usage kwh   "+heatBill.getUsage());
			 	totalAllocatorUsageKWH = heatBill.getUsage() - totalWaterMeterUsageKWH;
			 	totalAllocatorCharge = (1-(totalWaterMeterUsageKWH/heatBill.getUsage()))*heatBill.getCharge();
			 	totalWaterMeterCharge = heatBill.getCharge()-totalAllocatorCharge;
			 	
			 	//ilk olarak pay ölçer gösterilecek
				PdfPTable allocatorRowTable = new PdfPTable(36);
				calcsInnerCellA.setPhrase(new Phrase("  Isýtma(Pay Ölçer)", innerCellsfont)); //iki karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %70 Sayaç", innerCellsfont)); //altý karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH*allocatorDistributionConstant), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge*allocatorDistributionConstant), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalStrs), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("Str", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format((heatBill.getCharge()*allocatorDistributionConstant)/totalStrs), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/Str", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %30 Ortak", innerCellsfont)); //altý karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH*(1-allocatorDistributionConstant)), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge*(1-allocatorDistributionConstant)), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalFlatsAreaByDistributionLine), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("m2", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format((heatBill.getCharge()*commonAreasConstant)/totalFlatsAreaByDistributionLine), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/m2", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				PdfPCell allocatorRowTableCell = new PdfPCell(allocatorRowTable);
				allocatorRowTableCell.setColspan(9);
				allocatorRowTableCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
				
				unitCalculationsTable.addCell(allocatorRowTableCell);
				
				//sýss gösterilecek
				PdfPTable warmWaterRowTable = new PdfPTable(36);
				calcsInnerCellA.setPhrase(new Phrase("  Isýtma(Sýcak su)", innerCellsfont)); //iki karakter boþluk
				warmWaterRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalWaterMeterUsageKWH), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalWaterMeterCharge), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %100 Sayaç", innerCellsfont)); //altý karakter boþluk
				warmWaterRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalWaterMeterUsageKWH), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalWaterMeterCharge), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalWaterMeterUsageM3), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("m3", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalWaterMeterCharge/totalWaterMeterUsageM3), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/m3", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %0 Ortak", innerCellsfont)); //altý karakter boþluk
				warmWaterRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH*(1-allocatorDistributionConstant)), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge*(1-allocatorDistributionConstant)), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalFlatsAreaByDistributionLine), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("m2", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format((heatBill.getCharge()*commonAreasConstant)/totalFlatsAreaByDistributionLine), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/m2", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				PdfPCell warmWaterRowTableCell = new PdfPCell(warmWaterRowTable);
				warmWaterRowTableCell.setColspan(9);
				warmWaterRowTableCell.setBorder(Rectangle.TOP |Rectangle.BOTTOM);
				unitCalculationsTable.addCell(warmWaterRowTableCell);
			}

			
			//eðer daðýtým hattýnda IP,SISS ve BOY.REF.S.var ise
			if(behavioursByDistributionLine.COMBINATIONOFDISTRIBUTIONLINE==Constants.IP_SýSS_BOYREFS)
			{
				Bill waterBill = new Bill();
				Bill heatBill = new Bill();
				for (Bill bill : billsOfDistributionLine) {
					if(bill.getBillType().getId()==Constants.BILL_NGAS)heatBill = bill;
					if(bill.getBillType().getId()==Constants.BILL_WATER)waterBill = bill;
				}
				// DAÐITIM HATTINA GÖRE PAY ÖLÇER OKUMALARI
				List<HeatCostAllocatorReading> heatCostAllocatorReadingsOfStartPeriodByDistributionLine = new ArrayList<HeatCostAllocatorReading>();
				for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndStartPeriod) {
				DistributionLine distributionLineOfReading = heatCostAllocatorService.findDistributionLineOfReading(heatCostAllocatorReading, distributionLineBuildingsByProject);
				if(distributionLineOfReading.getId()==distributionLine.getId()) heatCostAllocatorReadingsOfStartPeriodByDistributionLine.add(heatCostAllocatorReading);
				}
				List<HeatCostAllocatorReading> heatCostAllocatorReadingsOfEndPeriodByDistributionLine = new ArrayList<HeatCostAllocatorReading>();
				for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndEndPeriod) {
				DistributionLine distributionLineOfReading = heatCostAllocatorService.findDistributionLineOfReading(heatCostAllocatorReading, distributionLineBuildingsByProject);
				if(distributionLineOfReading.getId()==distributionLine.getId()) heatCostAllocatorReadingsOfEndPeriodByDistributionLine.add(heatCostAllocatorReading);
				}
				Double totalStrs= totalStrOfAllocatorReadings(heatCostAllocatorReadingsOfStartPeriodByDistributionLine, heatCostAllocatorReadingsOfEndPeriodByDistributionLine);
				//__________________________________________________________________________________________________________________________________________||||||||||||||||||

				List<WaterMeterReading> watermeterReadingsOfStartPeriodByDistributionLine = new ArrayList<WaterMeterReading>();
				for (WaterMeterReading waterMeterReading: waterMeterReadingsByProjectAndStartPeriod) {
					DistributionLine distributionLineOfReading = waterMeterReadingService.findDistributionLineOfReading(waterMeterReading, distributionLineBuildingsByProject);
					if(distributionLineOfReading.getId()==distributionLine.getId()) watermeterReadingsOfStartPeriodByDistributionLine.add(waterMeterReading);
					}
				List<WaterMeterReading> watermeterReadingsOfEndPeriodByDistributionLine = new ArrayList<WaterMeterReading>();
				for (WaterMeterReading waterMeterReading: waterMeterReadingsByProjectAndEndPeriod) {
					DistributionLine distributionLineOfReading = waterMeterReadingService.findDistributionLineOfReading(waterMeterReading, distributionLineBuildingsByProject);
					if(distributionLineOfReading.getId()==distributionLine.getId()) watermeterReadingsOfEndPeriodByDistributionLine.add(waterMeterReading);
					}
				Double totalAllocatorUsageKWH ;
				Double totalAllocatorCharge ;
			 	Double totalWaterMeterCharge;
				Double totalWaterMeterUsageM3 = totalConsumptionsOfWaterMeterReadings(watermeterReadingsOfStartPeriodByDistributionLine, watermeterReadingsOfEndPeriodByDistributionLine);
			 	Double totalWaterMeterUsageKWH = (1-waterDistributionConstant)*(1200*waterBill.getUsage()*(60-10)/8250*10.8);
			 	System.out.println("water meter usage KWH  "+ totalWaterMeterUsageKWH);
			 	System.out.println("heat bill usage kwh   "+heatBill.getUsage());
			 	totalAllocatorUsageKWH = heatBill.getUsage() - totalWaterMeterUsageKWH;
			 	totalAllocatorCharge = (1-(totalWaterMeterUsageKWH/heatBill.getUsage()))*heatBill.getCharge();
			 	totalWaterMeterCharge = heatBill.getCharge()-totalAllocatorCharge;
			 	
			 	//ilk olarak pay ölçer gösterilecek
				PdfPTable allocatorRowTable = new PdfPTable(36);
				calcsInnerCellA.setPhrase(new Phrase("  Isýtma(Pay Ölçer)", innerCellsfont)); //iki karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %70 Sayaç", innerCellsfont)); //altý karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH*allocatorDistributionConstant), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge*allocatorDistributionConstant), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalStrs), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("Str", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format((heatBill.getCharge()*allocatorDistributionConstant)/totalStrs), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/Str", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %30 Ortak", innerCellsfont)); //altý karakter boþluk
				allocatorRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH*(1-allocatorDistributionConstant)), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge*(1-allocatorDistributionConstant)), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalFlatsAreaByDistributionLine), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("m2", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format((heatBill.getCharge()*commonAreasConstant)/totalFlatsAreaByDistributionLine), innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/m2", innerCellsfont));
				allocatorRowTable.addCell(calcsInnerCellE);
				PdfPCell allocatorRowTableCell = new PdfPCell(allocatorRowTable);
				allocatorRowTableCell.setColspan(9);
				allocatorRowTableCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
				
				unitCalculationsTable.addCell(allocatorRowTableCell);
				
				//sýss gösterilecek
				PdfPTable warmWaterRowTable = new PdfPTable(36);
				calcsInnerCellA.setPhrase(new Phrase("  Isýtma(Sýcak su)", innerCellsfont)); //iki karakter boþluk
				warmWaterRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalWaterMeterUsageKWH), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalWaterMeterCharge), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase("", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %100 Sayaç", innerCellsfont)); //altý karakter boþluk
				warmWaterRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalWaterMeterUsageKWH), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalWaterMeterCharge), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalWaterMeterUsageM3), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("m3", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalWaterMeterCharge/totalWaterMeterUsageM3), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/m3", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				
				calcsInnerCellA.setPhrase(new Phrase("     %0 Ortak", innerCellsfont)); //altý karakter boþluk
				warmWaterRowTable.addCell(calcsInnerCellA);
				calcsInnerCellB.setPhrase(new Phrase(formatter.format(totalAllocatorUsageKWH*(1-allocatorDistributionConstant)), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellB);
				calcsInnerCellC.setPhrase(new Phrase(heatBill.getUnit(), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellC);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalAllocatorCharge*(1-allocatorDistributionConstant)), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format(totalFlatsAreaByDistributionLine), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("m2", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				calcsInnerCellD.setPhrase(new Phrase(formatter.format((heatBill.getCharge()*commonAreasConstant)/totalFlatsAreaByDistributionLine), innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellD);
				calcsInnerCellE.setPhrase(new Phrase("TL/m2", innerCellsfont));
				warmWaterRowTable.addCell(calcsInnerCellE);
				PdfPCell warmWaterRowTableCell = new PdfPCell(warmWaterRowTable);
				warmWaterRowTableCell.setColspan(9);
				warmWaterRowTableCell.setBorder(Rectangle.TOP |Rectangle.BOTTOM);
				unitCalculationsTable.addCell(warmWaterRowTableCell);
			}
			
			
			if(isIPInDistributionLine) //bu daðýtým hattýnda pay ölçer varsa
			{
				Calculations calculationsByAllocator = new Calculations();
				calculationsByAllocator.TYPE=Constants.IP;
				Bill allocatorBill = new Bill();
				for (Bill bill : billsOfDistributionLine) {
					if(bill.getBillType().getId()==5)
					{
						allocatorBill = bill;
					}
				}
				List<HeatCostAllocatorReading> heatCostAllocatorReadingsOfStartPeriodByDistributionLine = new ArrayList<HeatCostAllocatorReading>();
				for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndStartPeriod) {
				DistributionLine distributionLineOfReading = heatCostAllocatorService.findDistributionLineOfReading(heatCostAllocatorReading, distributionLineBuildingsByProject);
				if(distributionLineOfReading.getId()==distributionLine.getId())
				{
					heatCostAllocatorReadingsOfStartPeriodByDistributionLine.add(heatCostAllocatorReading);
				}
				}
				List<HeatCostAllocatorReading> heatCostAllocatorReadingsOfEndPeriodByDistributionLine = new ArrayList<HeatCostAllocatorReading>();
				for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndEndPeriod) {
				DistributionLine distributionLineOfReading = heatCostAllocatorService.findDistributionLineOfReading(heatCostAllocatorReading, distributionLineBuildingsByProject);
				if(distributionLineOfReading.getId()==distributionLine.getId())
				{
					heatCostAllocatorReadingsOfEndPeriodByDistributionLine.add(heatCostAllocatorReading);
				}
				}
				System.out.println(distributionLine.getName()+" isimli daðýtým hattýnýn baþlangýç periyodundaki okumalarýn sayýsý: "+heatCostAllocatorReadingsOfStartPeriodByDistributionLine.size());
				System.out.println(distributionLine.getName()+" isimli daðýtým hattýnýn bitiþ periyodundaki okumalarýn sayýsý: "+heatCostAllocatorReadingsOfStartPeriodByDistributionLine.size());
/*toplam str*/	Double totalStrs= totalStrOfAllocatorReadings(heatCostAllocatorReadingsOfStartPeriodByDistributionLine, heatCostAllocatorReadingsOfEndPeriodByDistributionLine);
				
				PdfPCell expInnerCell0 = new PdfPCell(new Phrase("Sayaçlardan", headerCellsfont));
				expenditureTable.addCell(expInnerCell0);
				PdfPCell expInnerCell1 = new PdfPCell(new Phrase(formatter.format(allocatorBill.getUsage()*allocatorDistributionConstant) , innerCellsfont));
				expenditureTable.addCell(expInnerCell1);
				calculationsByAllocator.setEnergyUsageOfMeters(allocatorBill.getUsage()*allocatorDistributionConstant);
				PdfPCell expInnerCell2 = new PdfPCell(new Phrase(formatter.format(allocatorBill.getCharge()*allocatorDistributionConstant), innerCellsfont));
				expenditureTable.addCell(expInnerCell2);
				calculationsByAllocator.setEnergyChargeOfMeters(allocatorBill.getCharge()*allocatorDistributionConstant);
				PdfPCell expInnerCell3 = new PdfPCell(new Phrase(formatter.format(totalStrs), innerCellsfont));
				expenditureTable.addCell(expInnerCell3);
				calculationsByAllocator.setTotalConsumptionOfMeters(totalStrs);
				PdfPCell expInnerCell4 = new PdfPCell(new Phrase("Str", innerCellsfont));
				expenditureTable.addCell(expInnerCell4);
				calculationsByAllocator.setUnitOfMeters("Str");
				PdfPCell expInnerCell5 = new PdfPCell(new Phrase(formatter.format((allocatorBill.getCharge()*allocatorDistributionConstant)/totalStrs), innerCellsfont));
				expenditureTable.addCell(expInnerCell5);
				calculationsByAllocator.setUnitValueOfMeters((allocatorBill.getCharge()*allocatorDistributionConstant)/totalStrs);			
				PdfPCell secondexpInnerCell0 = new PdfPCell(new Phrase("Ortak Alanlardan", headerCellsfont));
				expenditureTable.addCell(secondexpInnerCell0);
				PdfPCell secondexpInnerCell1 = new PdfPCell(new Phrase(formatter.format(allocatorBill.getUsage()*commonAreasConstant) , innerCellsfont));
				expenditureTable.addCell(secondexpInnerCell1);
				calculationsByAllocator.setEnergyUsageOfCommonAreas(allocatorBill.getUsage()*commonAreasConstant);
				PdfPCell secondexpInnerCell2 = new PdfPCell(new Phrase(formatter.format(allocatorBill.getCharge()*commonAreasConstant), innerCellsfont));
				expenditureTable.addCell(secondexpInnerCell2);
				calculationsByAllocator.setEnergyChargeOfCommonAreas(allocatorBill.getCharge()*commonAreasConstant);
				PdfPCell secondexpInnerCell3 = new PdfPCell(new Phrase(formatter.format(totalFlatsAreaByDistributionLine), innerCellsfont));
				expenditureTable.addCell(secondexpInnerCell3);
				calculationsByAllocator.setTotalConsumptionOfCommonAreas(totalFlatsAreaByDistributionLine);
				PdfPCell secondexpInnerCell4 = new PdfPCell(new Phrase("m2", innerCellsfont));
				expenditureTable.addCell(secondexpInnerCell4);
				calculationsByAllocator.setUnitOfCommonAreas("m2");
				PdfPCell secondexpInnerCell5 = new PdfPCell(new Phrase(formatter.format((allocatorBill.getCharge()*commonAreasConstant)/totalFlatsAreaByDistributionLine), innerCellsfont));
				expenditureTable.addCell(secondexpInnerCell5);
				calculationsByAllocator.setUnitValueOfCommonAreas((allocatorBill.getCharge()*commonAreasConstant)/totalFlatsAreaByDistributionLine );
				calculationsByAllocator.setBill(allocatorBill);
//				distributionLineCalcsMap.put(distributionLine.getId(), calculationsByAllocator);
				distCalculations.add(calculationsByAllocator);
			
			}
			if(isSýSSInDistributionLine)
			{
				List<HeatMeterReading> heatmeterReadingsOfStartPeriodByDistributionLine = new ArrayList<HeatMeterReading>();
				for (HeatMeterReading heatMeterReading: heatMeterReadingsByProjectAndStartPeriod) {
					DistributionLine distributionLineOfReading = heatMeterService.findDistributionLineOfReading(heatMeterReading, distributionLineBuildingsByProject);
					if(distributionLineOfReading.getId()==distributionLine.getId())
					{
						heatmeterReadingsOfStartPeriodByDistributionLine.add(heatMeterReading);
					}
					}
				List<HeatMeterReading> heatmeterReadingsOfEndPeriodByDistributionLine = new ArrayList<HeatMeterReading>();
				for (HeatMeterReading heatMeterReading: heatMeterReadingsByProjectAndEndPeriod) {
					DistributionLine distributionLineOfReading = heatMeterService.findDistributionLineOfReading(heatMeterReading, distributionLineBuildingsByProject);
					if(distributionLineOfReading.getId()==distributionLine.getId())
					{
						heatmeterReadingsOfEndPeriodByDistributionLine.add(heatMeterReading);
					}
					}
			}	
			if(isISInDistributionLine)
			{
				
			}
	
				distributionLineCalcsMap.put(distributionLine.getId(), distCalculations);
				distributionLine_BehavioursMap.put(distributionLine.getId(), behavioursByDistributionLine);
			

		}// DAÐITIM HATTI DÖNGÜSÜ BÝTÝYOR_____________________________________________________________________________________________________
		
		document.add(unitCalculationsTable);
		
		expenditureTable.setSpacingAfter(20); 
//		document.add(expenditureTable); //gider paylarý tablosu sonlandýrýlýyor

//		System.out.println("gider paylarý tablosu eklendi");
//
//		System.out.println("bina sayýsý: " + buildingsByProject.size());
//		System.out.println("daire sayýsý: " + flatsByProject.size());
//		System.out.println("pay ölçer sayýsý " + heatCostAllocatorsByproject.size());
//		System.out.println("döneme göre pay ölçer okumasý sayýsý:" + heatCostAllocatorReadingsByProjectAndEndPeriod.size());
//		System.out.println("döneme göre su okumasý");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// PROJELERÝN BÝNALARI DÖNÜLMEYE BAÞLANIYOR
		for (Building building : buildingsByProject) {
			
			//pay ölçer nesneleri
			List<HeatCostAllocator> heatCostAllocatorsByBuilding = new ArrayList<HeatCostAllocator>();
			List<HeatCostAllocatorReading> allocatorReadingsByBuildingAndStartPeriod = new ArrayList<HeatCostAllocatorReading>();
			List<HeatCostAllocatorReading> allocatorReadingsByBuildingAndEndPeriod = new ArrayList<HeatCostAllocatorReading>();
			//ýsý sayacý nesneleri
			List<HeatMeter> heatMetersByBuilding = new ArrayList<HeatMeter>();
			List<HeatMeterReading> heatMeterReadingsByBuildingAndStartPeriod = new ArrayList<HeatMeterReading>();
			List<HeatMeterReading> heatMeterReadingsByBuildingAndEndPeriod = new ArrayList<HeatMeterReading>();
			//su sayacý nesneleri
			List<WaterMeter> waterMetersByBuilding = new ArrayList<WaterMeter>();
			List<WaterMeterReading> waterMeterReadingsByBuildingAndStartPeriod = new ArrayList<WaterMeterReading>();
			List<WaterMeterReading> waterMeterReadingsByBuildingAndEndPeriod = new ArrayList<WaterMeterReading>();
			
			
			List<WaterMeter> warmWaterMetersByBuilding = new ArrayList<WaterMeter>();
			List<WaterMeter> coldWaterMetersByBuilding = new ArrayList<WaterMeter>();
			List<WaterMeter> plainWaterMetersByBuilding = new ArrayList<WaterMeter>();
			List<WaterMeterReading> warmWaterMeterReadingsByBuildingAndStartPeriod = new ArrayList<WaterMeterReading>();
			List<WaterMeterReading> warmWaterMeterReadingsByBuildingAndEndPeriod = new ArrayList<WaterMeterReading>();
			List<WaterMeterReading> coldWaterMeterReadingsByBuildingAndStartPeriod = new ArrayList<WaterMeterReading>();
			List<WaterMeterReading> coldWaterMeterReadingsByBuildingAndEndPeriod = new ArrayList<WaterMeterReading>();
			List<WaterMeterReading> plainWaterMeterReadingsByBuildingAndStartPeriod = new ArrayList<WaterMeterReading>();
			List<WaterMeterReading> plainWaterMeterReadingsByBuildingAndEndPeriod = new ArrayList<WaterMeterReading>();

			
			document.add(Chunk.NEWLINE);
			Paragraph buildingParagraph = new Paragraph("Bina : " + building.getName(), headerFont); 
			buildingParagraph.setIndentationLeft(10);
			buildingParagraph.setSpacingAfter(10);
			document.add(buildingParagraph);
			PdfPTable usageTableHeader = new PdfPTable(11);
			usageTableHeader.setWidthPercentage(90);
			PdfPCell usagecell0 = new PdfPCell(new Phrase("Oda Tipi", headerCellsfont));
			usagecell0.setHorizontalAlignment(Element.ALIGN_CENTER);usageTableHeader.addCell(usagecell0); 
			PdfPCell usagecell1 = new PdfPCell(new Phrase("Seri No", headerCellsfont));
			usagecell1.setHorizontalAlignment(Element.ALIGN_CENTER);usageTableHeader.addCell(usagecell1);
			PdfPCell usagecell2 = new PdfPCell(new Phrase("Önceki Endeks", headerCellsfont));
			usagecell2.setHorizontalAlignment(Element.ALIGN_CENTER);usageTableHeader.addCell(usagecell2);
			PdfPCell usagecell3 = new PdfPCell(new Phrase("Son Endeks", headerCellsfont));
			usagecell3.setHorizontalAlignment(Element.ALIGN_CENTER);usageTableHeader.addCell(usagecell3);
			PdfPCell usagecell4 = new PdfPCell(new Phrase("Tüketim", headerCellsfont));
			usagecell4.setHorizontalAlignment(Element.ALIGN_CENTER);usageTableHeader.addCell(usagecell4);
			PdfPCell usagecell5 = new PdfPCell(new Phrase("Birim", headerCellsfont));
			usagecell5.setHorizontalAlignment(Element.ALIGN_CENTER);usageTableHeader.addCell(usagecell5);
			PdfPCell usagecell7 = new PdfPCell(new Phrase("Kges", headerCellsfont));
			usagecell7.setHorizontalAlignment(Element.ALIGN_CENTER);usageTableHeader.addCell(usagecell7);
			PdfPCell usagecell8 = new PdfPCell(new Phrase("Tüketim Pay Ölçer", headerCellsfont));
			usagecell8.setHorizontalAlignment(Element.ALIGN_CENTER);usageTableHeader.addCell(usagecell8);
			PdfPCell usagecell9 = new PdfPCell(new Phrase("TOPLAMLAR", headerCellsfont));
			usagecell9.setHorizontalAlignment(Element.ALIGN_CENTER);usagecell9.setColspan(3);
			usageTableHeader.addCell(usagecell9);
			
			document.add(usageTableHeader);
			document.add(Chunk.NEWLINE);
			List<Flat> flatsByBuilding = new ArrayList<Flat>();
			for (Flat flat : flatsByProject) { //BÝNANIN DAÝRELERÝ
				if (flat.getBuilding().getId() == building.getId()) {
					flatsByBuilding.add(flat);
				}
			}

			List<DistributionLine> distributionLinesOfBuilding = new ArrayList<DistributionLine>();
			List<DistributionLineBuilding> distributionLineBuildingsByBuilding = new ArrayList<DistributionLineBuilding>();
			for (DistributionLineBuilding distributionLineBuilding : distributionLineBuildingsByProject) {
				if (distributionLineBuilding.getBuilding().getId() == building.getId()) {
					distributionLineBuildingsByBuilding.add(distributionLineBuilding);
				}
			}

			for (DistributionLineBuilding distributionLineBuilding : distributionLineBuildingsByBuilding) {
				distributionLinesOfBuilding.add(distributionLineBuilding.getDistributionLine());
			}
			for (DistributionLine distributionLine : distributionLinesOfBuilding) {
//				System.out.println("Binanýn daðýtým hatlarý: "+ distributionLine.getName());
			}
			
//			System.out.println("binanýn daðýtým hattý sayýsý: " + distributionLinesOfBuilding.size());
//			System.out.println("Bitiþ saati:" + new DateTime().toDate().toString());
			
			
//			if(distributionLinesOfBuilding.size()==1) 
//			//EÐER BÝNANIN DAÐITIM HATTI TEK ÝSE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			{
			for (DistributionLine distributionLineOfBuilding : distributionLinesOfBuilding) {
				
//				DistributionLine distributionLineOfBuilding = distributionLinesOfBuilding.get(0);
//				System.out.println( distributionLineOfBuilding.getId());
				List<DistributionLineMeterType> distributionLineMeterTypes = distributionLineMeterTypeService.findbyDistributionLine(distributionLineOfBuilding);
				for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypes) {
					// daðýtým hattýnda hangi cihazlar var?
					// System.out.println("Ölçüm tipi kýsaltmasý: W"+distributionLineMeterType.getMeterType().getAbbreviation()+"W");
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("IP"))
						isIP = true;
//					System.out.println("isIP: " + isIP);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("IS"))
						isIS = true;
//					System.out.println("isIS: " + isIS);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SS"))
						isSS = true;
//					System.out.println("isSS: " + isSS);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("GS"))
						isGS = true;
//					System.out.println("isGS: " + isGS);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("ES"))
						isES = true;
//					System.out.println("isES: " + isES);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SýSS"))
						isSýSS = true;
//					System.out.println("isSýSS: " + isSýSS);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SoSS"))
						isSoSS = true;
//					System.out.println("isSoSS: " + isSoSS);
				}
				if (isIP ) // bu daðýtým hattýnda pay ölçer varsa
					{
						
//						System.out.println("BÝNADA pay ölçer var");
						 heatCostAllocatorsByBuilding = new ArrayList<HeatCostAllocator>(); // ----------------------------------
						for (HeatCostAllocator heatCostAllocator : heatCostAllocatorsByproject) { //
							if (heatCostAllocator.getRoom().getFlat().getBuilding().getId() == building.getId()) // Binanýn
																													// Pay
																													// ölçerleri
								heatCostAllocatorsByBuilding.add(heatCostAllocator); // -------------------------------------
						}

						// binaya göre pay ölçerlerin güncel endeks deðerleri
						 allocatorReadingsByBuildingAndEndPeriod = new ArrayList<HeatCostAllocatorReading>();
						for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndEndPeriod) {
							if (heatCostAllocatorReading.getHeatCostAllocator().getRoom().getFlat().getBuilding().getId() == building.getId())
							{
							allocatorReadingsByBuildingAndEndPeriod.add(heatCostAllocatorReading);
							}
						}
						// binaya göre pay ölçerlerin geçmiþ endeks deðerleri
						 allocatorReadingsByBuildingAndStartPeriod = new ArrayList<HeatCostAllocatorReading>();
						for (HeatCostAllocatorReading heatCostAllocatorReading : heatCostAllocatorReadingsByProjectAndStartPeriod) {
							if (heatCostAllocatorReading.getHeatCostAllocator().getRoom().getFlat().getBuilding().getId() == building.getId())
							allocatorReadingsByBuildingAndStartPeriod.add(heatCostAllocatorReading);
						}
					}	
				if(isIS) // ýsý sayacý varsa
				{
//					System.out.println("BÝNADA ýsý sayacý var");
					 heatMetersByBuilding = new ArrayList<HeatMeter>();						 		// ----------------------------------
						for (HeatMeter heatMeter: heatMetersByProject) {							// Binanýn ýsý
							if (heatMeter.getFlat().getBuilding().getId() == building.getId())		// sayaçlarý
								heatMetersByBuilding.add(heatMeter); 								// -------------------------------------
						}
						// binaya göre ýsý sayaçlarýnýn güncel endeks deðerleri
						for (HeatMeterReading heatMeterReading: heatMeterReadingsByProjectAndEndPeriod) {
							if (heatMeterReading.getHeatMeter().getFlat().getBuilding().getId() == building.getId())
							{
							heatMeterReadingsByBuildingAndEndPeriod.add(heatMeterReading);
							}
						}
						// binaya göre ýsý sayaçlarýnýn güncel endeks deðerleri
						for (HeatMeterReading heatMeterReading: heatMeterReadingsByProjectAndStartPeriod) {
							if (heatMeterReading.getHeatMeter().getFlat().getBuilding().getId() == building.getId())
							{
								heatMeterReadingsByBuildingAndStartPeriod.add(heatMeterReading);
							}
						}
						
				}
				
				if(isSýSS || isSoSS || isSS) // su sayacý varsa
				{
//					System.out.println("BÝNADA  su sayacý var");
					
					 waterMetersByBuilding = new ArrayList<WaterMeter>();						 																		// ----------------------------------
						for (WaterMeter waterMeter: waterMetersByProject) {							// Binanýn tüm su 
							if (waterMeter.getFlat().getBuilding().getId() == building.getId())		// sayaçlarý
								waterMetersByBuilding.add(waterMeter); 								// -------------------------------------
						}
						
						
						//BÝNAYA AÝT SICAK SU SAYAÇLARI 
						
						for (WaterMeter waterMeter : waterMetersByBuilding) {
							if(waterMeter.getMeterType().getEnum_()==Constants.SýSS)
							{
								warmWaterMetersByBuilding.add(waterMeter);
							}
						}
						
						//BÝNAYA AÝT Soðuk SU SAYAÇLARI 
						for (WaterMeter waterMeter : waterMetersByBuilding) {
							if(waterMeter.getMeterType().getEnum_()==Constants.SoSS)
							{
								coldWaterMetersByBuilding.add(waterMeter);
							}
						}
						
						//BÝNAYA AÝT DÜZ SU SAYAÇLARI
						for (WaterMeter waterMeter : waterMetersByBuilding) {
							if(waterMeter.getMeterType().getEnum_()==Constants.SS)
							{
								plainWaterMetersByBuilding.add(waterMeter);
							}
						}
						
						
						// binaya göre sýcak su sayaçlarýnýn güncel endeks deðerleri
						for (WaterMeterReading waterMeterReading: waterMeterReadingsByProjectAndEndPeriod) {
							if (waterMeterReading.getWaterMeter().getFlat().getBuilding().getId() == building.getId())
							{
							waterMeterReadingsByBuildingAndEndPeriod.add(waterMeterReading);
							}
						}
						// binaya göre ýsý sayaçlarýnýn güncel endeks deðerleri
						for (WaterMeterReading waterMeterReading: waterMeterReadingsByProjectAndStartPeriod) {
							if (waterMeterReading.getWaterMeter().getFlat().getBuilding().getId() == building.getId())
							{
								waterMeterReadingsByBuildingAndStartPeriod.add(waterMeterReading);
							}
						}
						//SU SAYAÇ TÝPLERÝNE GÖRE BÝNANIN GÜNCEL SU OKUMALARI AYRILIYOR
						for (WaterMeterReading waterMeterReading : waterMeterReadingsByBuildingAndEndPeriod) {
							if(waterMeterReading.getWaterMeter().getMeterType().getEnum_()==Constants.SýSS)
							{
								warmWaterMeterReadingsByBuildingAndEndPeriod.add(waterMeterReading);
							}
							if(waterMeterReading.getWaterMeter().getMeterType().getEnum_()==Constants.SoSS)
							{
								coldWaterMeterReadingsByBuildingAndEndPeriod.add(waterMeterReading);
							}
							if(waterMeterReading.getWaterMeter().getMeterType().getEnum_()==Constants.SS)
							{
								plainWaterMeterReadingsByBuildingAndEndPeriod.add(waterMeterReading);
							}
						}
						//SU SAYAÇ TÝPLERÝNE GÖRE BÝNANIN GEÇMÝÞ SU OKUMALARI AYRILIYOR
						for (WaterMeterReading waterMeterReading : waterMeterReadingsByBuildingAndStartPeriod) {
							if(waterMeterReading.getWaterMeter().getMeterType().getEnum_()==Constants.SýSS)
							{
								warmWaterMeterReadingsByBuildingAndStartPeriod.add(waterMeterReading);
							}
							if(waterMeterReading.getWaterMeter().getMeterType().getEnum_()==Constants.SoSS)
							{
								coldWaterMeterReadingsByBuildingAndStartPeriod.add(waterMeterReading);
							}
							if(waterMeterReading.getWaterMeter().getMeterType().getEnum_()==Constants.SS)
							{
								plainWaterMeterReadingsByBuildingAndStartPeriod.add(waterMeterReading);
							}
						}
						
						
						
						
				}
				
						// BÝNAYA AÝT TÜM SAYAÇLARIN DEÐERLERÝ ALINDI 
						// BÝNANIN TÜM DAÝRELERÝ TEK TEK GEZÝLÝYOR
						for (Flat flat : flatsByBuilding) {
							FlatAndReadings flatAndReadings = new FlatAndReadings();
							flatAndReadings.setFlat(flat);
							Double meterCharges=0.0;
							Double commonCharges =0.0;
							PdfPTable usageAndCalcsTable =new PdfPTable(11); // okumalar ve giderler tablosu
//							PdfPCell usageAndCalcsHeaderCell = new PdfPCell();
//							usageAndCalcsHeaderCell.setColspan(11);
//							usageAndCalcsHeaderCell.addElement(flatParagraph);
//							usageAndCalcsTable.setHeaderRows(1);
							usageAndCalcsTable.setWidthPercentage(90);
							
							// dairenin pay ölçerleri bulunuyor
							List<HeatCostAllocator> allocatorsByFlat = new ArrayList<HeatCostAllocator>();
							for (HeatCostAllocator heatCostAllocator : heatCostAllocatorsByBuilding) {
								if (heatCostAllocator.getRoom().getFlat().getId() == flat.getId()) {
									allocatorsByFlat.add(heatCostAllocator);
								}
							}

							Paragraph	flatParagraph = new Paragraph("Daire  : " + flat.getNo() +" / "+flat.getArea()+"m2" , headerCellsfont); 
							flatParagraph.setIndentationLeft(10);
							flatParagraph.setSpacingAfter(10);
							document.add(flatParagraph);
							
							if (distributionLine_BehavioursMap.get(distributionLineOfBuilding.getId()).TYPEOFDISTRIBUTIONLINE==Constants.ISITMA)
//							if(true)
							{
								headerCellsfont.setColor(Color.RED);
								Paragraph	distLineParagraph = new Paragraph("ISITMA  :", headerCellsfont); 
								
								distLineParagraph.setIndentationLeft(10);
								distLineParagraph.setSpacingAfter(10);
								document.add(distLineParagraph);
								headerCellsfont.setColor(Color.BLACK);
								PdfPTable leftUsageTable = new PdfPTable(1);
								
								
								
								if(isIP) // eðer pay ölçer varsa
								{
									PdfPTable allocatorUsageTable = new PdfPTable(8);
									allocatorUsageTable.setWidthPercentage(80);
									// her pay ölçerin güncel okumasý ve geçmiþ okumasý
									// bulunuyor
									Double strsByFlat=0.0;
									
									for (HeatCostAllocator heatCostAllocator : allocatorsByFlat) {
										
										// güncel okuma
										HeatCostAllocatorReading allocatorReadingByEndPeriod = new HeatCostAllocatorReading();
										for (HeatCostAllocatorReading allocatorReading : allocatorReadingsByBuildingAndEndPeriod) {
											if (allocatorReading.getHeatCostAllocator().getId() == heatCostAllocator.getId())
												allocatorReadingByEndPeriod = allocatorReading;
										}
										// geçmiþ okuma
										HeatCostAllocatorReading allocatorReadingByStartPeriod = new HeatCostAllocatorReading();
										for (HeatCostAllocatorReading allocatorReading : allocatorReadingsByBuildingAndStartPeriod) {
											if (allocatorReading.getHeatCostAllocator().getId() == heatCostAllocator.getId())
												allocatorReadingByStartPeriod = allocatorReading;
										}
										Double firstIndex = new Double(allocatorReadingByStartPeriod.getValue());
										Double lastIndex = new Double(allocatorReadingByEndPeriod.getValue());
										Double kges = new Double(heatCostAllocator.getKges());
										
										PdfPCell innerusagecell0 = new PdfPCell(new Phrase("Oda No: " + heatCostAllocator.getRoom().getOrderNo(), innerCellsfont));
										innerusagecell0.setBorder(Rectangle.NO_BORDER); innerusagecell0.setHorizontalAlignment(Element.ALIGN_CENTER);
										allocatorUsageTable.addCell(innerusagecell0);
										PdfPCell innerusagecell1 = new PdfPCell(new Phrase(heatCostAllocator.getSerialNo(), innerCellsfont));
										innerusagecell1.setBorder(Rectangle.NO_BORDER);innerusagecell1.setHorizontalAlignment(Element.ALIGN_CENTER);
										allocatorUsageTable.addCell(innerusagecell1);
										PdfPCell innerusagecell2 = new PdfPCell(new Phrase("" + firstIndex, innerCellsfont));
										innerusagecell2.setBorder(Rectangle.NO_BORDER);innerusagecell2.setHorizontalAlignment(Element.ALIGN_CENTER);
										allocatorUsageTable.addCell(innerusagecell2);
										PdfPCell innerusagecell3 = new PdfPCell(new Phrase("" + lastIndex, innerCellsfont));
										innerusagecell3.setBorder(Rectangle.NO_BORDER);innerusagecell3.setHorizontalAlignment(Element.ALIGN_CENTER);
										allocatorUsageTable.addCell(innerusagecell3);
										PdfPCell innerusagecell4 = new PdfPCell(new Phrase("" + (lastIndex - firstIndex), innerCellsfont));
										innerusagecell4.setBorder(Rectangle.NO_BORDER);innerusagecell4.setHorizontalAlignment(Element.ALIGN_CENTER);
										allocatorUsageTable.addCell(innerusagecell4);
										PdfPCell innerusagecell5 = new PdfPCell(new Phrase("HCA", innerCellsfont));
										innerusagecell5.setBorder(Rectangle.NO_BORDER);innerusagecell5.setHorizontalAlignment(Element.ALIGN_CENTER);
										allocatorUsageTable.addCell(innerusagecell5);
										PdfPCell innerusagecell6 = new PdfPCell(new Phrase("" + kges, innerCellsfont));
										innerusagecell6.setBorder(Rectangle.NO_BORDER);innerusagecell6.setHorizontalAlignment(Element.ALIGN_CENTER);
										allocatorUsageTable.addCell(innerusagecell6);
										PdfPCell innerusagecell7 = new PdfPCell(new Phrase(formatter.format(kges * (lastIndex - firstIndex)), innerCellsfont));
										innerusagecell7.setBorder(Rectangle.NO_BORDER);innerusagecell7.setHorizontalAlignment(Element.ALIGN_CENTER);
										allocatorUsageTable.addCell(innerusagecell7);
										
										strsByFlat = strsByFlat+(kges*(lastIndex-firstIndex));
										
										// document.add(Chunk.NEWLINE);
									}
//									System.out.println("distofbuild id: "+distributionLineOfBuilding.getId());
//									System.out.println(distributionLineCalcsMap.get(distributionLineOfBuilding.getId()).);
									Calculations calculationsOfAllocator = new Calculations();
									for (Calculations calculations: distributionLineCalcsMap.get(distributionLineOfBuilding.getId())) {
										if(calculations.TYPE==Constants.IP)
										{
											calculationsOfAllocator=calculations;
										}
									}
									
									flatAndReadings.setTotalAllocator(strsByFlat);
									meterCharges=calculationsOfAllocator.getUnitValueOfMeters() *flatAndReadings.getTotalAllocator();
									commonCharges=calculationsOfAllocator.getUnitValueOfCommonAreas()*flatAndReadings.getFlat().getArea().doubleValue();
									
									PdfPCell allocatorUsageTableCell = new PdfPCell(allocatorUsageTable);
									allocatorUsageTableCell.setBorder(Rectangle.NO_BORDER);
									leftUsageTable.addCell(allocatorUsageTableCell);
									
								}
								if(isIS) // Isý sayacý varsa
								{
									
									PdfPTable heatMeterUsageTable = new PdfPTable(8);
									heatMeterUsageTable.setWidthPercentage(80);
									
									HeatMeter heatMeterOfFlat = new HeatMeter();    //dairenin ýsý sayacý
									for (HeatMeter heatMeter : heatMetersByBuilding) {
										if(heatMeter.getFlat().getId()==flat.getId())
										{
											heatMeterOfFlat=heatMeter;
										}
									}
									// ýsý sayacýnýn güncel okumasý
									HeatMeterReading heatMeterReadingByEndPeriod = new HeatMeterReading();
									for (HeatMeterReading heatMeterReading : heatMeterReadingsByBuildingAndEndPeriod) {
										
										if(heatMeterReading.getHeatMeter().getId()==heatMeterOfFlat.getId())
										{
											heatMeterReadingByEndPeriod=heatMeterReading;
										}
									}
									//ýsý sayacýnýn geçmiþ okumasý
									HeatMeterReading heatMeterReadingByStartPeriod = new HeatMeterReading();
									for (HeatMeterReading heatMeterReading : heatMeterReadingsByBuildingAndStartPeriod) {
										
										if(heatMeterReading.getHeatMeter().getId()==heatMeterOfFlat.getId())
										{
											heatMeterReadingByStartPeriod=heatMeterReading;
										}
									}
									Double firstIndex = new Double(heatMeterReadingByStartPeriod.getValue());
									Double lastIndex = new Double(heatMeterReadingByEndPeriod.getValue());
									
									PdfPCell innerusagecell0 = new PdfPCell(new Phrase("Isý Sayacý: ", innerCellsfont));
									innerusagecell0.setBorder(Rectangle.NO_BORDER); innerusagecell0.setHorizontalAlignment(Element.ALIGN_CENTER);
									heatMeterUsageTable.addCell(innerusagecell0);
									PdfPCell innerusagecell1 = new PdfPCell(new Phrase(heatMeterOfFlat.getSerialNo(), innerCellsfont));
									innerusagecell1.setBorder(Rectangle.NO_BORDER);innerusagecell1.setHorizontalAlignment(Element.ALIGN_CENTER);
									heatMeterUsageTable.addCell(innerusagecell1);
									PdfPCell innerusagecell2 = new PdfPCell(new Phrase("" + firstIndex, innerCellsfont));
									innerusagecell2.setBorder(Rectangle.NO_BORDER);innerusagecell2.setHorizontalAlignment(Element.ALIGN_CENTER);
									heatMeterUsageTable.addCell(innerusagecell2);
									PdfPCell innerusagecell3 = new PdfPCell(new Phrase("" + lastIndex, innerCellsfont));
									innerusagecell3.setBorder(Rectangle.NO_BORDER);innerusagecell3.setHorizontalAlignment(Element.ALIGN_CENTER);
									heatMeterUsageTable.addCell(innerusagecell3);
									PdfPCell innerusagecell4 = new PdfPCell(new Phrase("" + (lastIndex - firstIndex), innerCellsfont));
									innerusagecell4.setBorder(Rectangle.NO_BORDER);innerusagecell4.setHorizontalAlignment(Element.ALIGN_CENTER);
									heatMeterUsageTable.addCell(innerusagecell4);
									PdfPCell innerusagecell5 = new PdfPCell(new Phrase("kwh", innerCellsfont));
									innerusagecell5.setBorder(Rectangle.NO_BORDER);innerusagecell5.setHorizontalAlignment(Element.ALIGN_CENTER);
									heatMeterUsageTable.addCell(innerusagecell5);
									PdfPCell innerusagecell6 = new PdfPCell(new Phrase("", innerCellsfont));
									innerusagecell6.setBorder(Rectangle.NO_BORDER);innerusagecell6.setHorizontalAlignment(Element.ALIGN_CENTER);
									heatMeterUsageTable.addCell(innerusagecell6);
									PdfPCell innerusagecell7 = new PdfPCell(new Phrase(formatter.format((lastIndex - firstIndex))+" kwh", innerCellsfont));
									innerusagecell7.setBorder(Rectangle.NO_BORDER);innerusagecell7.setHorizontalAlignment(Element.ALIGN_CENTER);
									heatMeterUsageTable.addCell(innerusagecell7);
									
									PdfPCell heatMeterUsageTableCell = new PdfPCell(heatMeterUsageTable);
									heatMeterUsageTableCell.setBorder(Rectangle.TOP);
									leftUsageTable.addCell(heatMeterUsageTableCell);
								}
								
								
								
								if(isSýSS) //sýcak Su sayacý varsa
								{
									
									PdfPTable waterMeterUsageTable = new PdfPTable(8);
									waterMeterUsageTable.setWidthPercentage(80);
									
									WaterMeter warmWaterMeterOfFlat = new WaterMeter();    //dairenin ýsý sayacý
									for (WaterMeter warmWaterMeter : warmWaterMetersByBuilding) {
										if(warmWaterMeter.getFlat().getId()==flat.getId())
										{
											warmWaterMeterOfFlat=warmWaterMeter;
										}
									}
									// sýcak su sayacýnýn güncel okumasý
									WaterMeterReading warmWaterMeterReadingByEndPeriod = new WaterMeterReading();
									for (WaterMeterReading waterMeterReading : warmWaterMeterReadingsByBuildingAndEndPeriod  ) {
										
										if(waterMeterReading.getWaterMeter().getId()==warmWaterMeterOfFlat.getId())
										{
											warmWaterMeterReadingByEndPeriod=waterMeterReading;
										}
									}
									//sýcak su sayacýnýn geçmiþ okumasý
									WaterMeterReading warmWaterMeterReadingByStartPeriod = new WaterMeterReading();
									for (WaterMeterReading waterMeterReading : waterMeterReadingsByBuildingAndStartPeriod) {
										
										if(waterMeterReading.getWaterMeter().getId()==warmWaterMeterOfFlat.getId())
										{
											warmWaterMeterReadingByStartPeriod=waterMeterReading;
										}
									}
									
									
									Double firstIndex = new Double(warmWaterMeterReadingByStartPeriod.getValue());
									Double lastIndex = new Double(warmWaterMeterReadingByEndPeriod.getValue());
									
									PdfPCell innerusagecell0 = new PdfPCell(new Phrase("Sýc. Su Sayacý: ", innerCellsfont));
									innerusagecell0.setBorder(Rectangle.NO_BORDER); innerusagecell0.setHorizontalAlignment(Element.ALIGN_CENTER);
									waterMeterUsageTable.addCell(innerusagecell0);
									PdfPCell innerusagecell1 = new PdfPCell(new Phrase(warmWaterMeterOfFlat.getSerialNo(), innerCellsfont));
									innerusagecell1.setBorder(Rectangle.NO_BORDER);innerusagecell1.setHorizontalAlignment(Element.ALIGN_CENTER);
									waterMeterUsageTable.addCell(innerusagecell1);
									PdfPCell innerusagecell2 = new PdfPCell(new Phrase("" + firstIndex, innerCellsfont));
									innerusagecell2.setBorder(Rectangle.NO_BORDER);innerusagecell2.setHorizontalAlignment(Element.ALIGN_CENTER);
									waterMeterUsageTable.addCell(innerusagecell2);
									PdfPCell innerusagecell3 = new PdfPCell(new Phrase("" + lastIndex, innerCellsfont));
									innerusagecell3.setBorder(Rectangle.NO_BORDER);innerusagecell3.setHorizontalAlignment(Element.ALIGN_CENTER);
									waterMeterUsageTable.addCell(innerusagecell3);
									PdfPCell innerusagecell4 = new PdfPCell(new Phrase("" + (lastIndex - firstIndex), innerCellsfont));
									innerusagecell4.setBorder(Rectangle.NO_BORDER);innerusagecell4.setHorizontalAlignment(Element.ALIGN_CENTER);
									waterMeterUsageTable.addCell(innerusagecell4);
									PdfPCell innerusagecell5 = new PdfPCell(new Phrase("m3", innerCellsfont));
									innerusagecell5.setBorder(Rectangle.NO_BORDER);innerusagecell5.setHorizontalAlignment(Element.ALIGN_CENTER);
									waterMeterUsageTable.addCell(innerusagecell5);
									PdfPCell innerusagecell6 = new PdfPCell(new Phrase("", innerCellsfont));
									innerusagecell6.setBorder(Rectangle.NO_BORDER);innerusagecell6.setHorizontalAlignment(Element.ALIGN_CENTER);
									waterMeterUsageTable.addCell(innerusagecell6);
									PdfPCell innerusagecell7 = new PdfPCell(new Phrase(formatter.format((lastIndex - firstIndex))+" m3", innerCellsfont));
									innerusagecell7.setBorder(Rectangle.NO_BORDER);innerusagecell7.setHorizontalAlignment(Element.ALIGN_CENTER);
									waterMeterUsageTable.addCell(innerusagecell7);
									
									PdfPCell waterMeterUsageTableCell = new PdfPCell(waterMeterUsageTable);
									waterMeterUsageTableCell.setBorder(Rectangle.TOP);
									leftUsageTable.addCell(waterMeterUsageTableCell);
								}
								
								
								
							
								
								PdfPCell leftUsagetableCell = new PdfPCell(leftUsageTable);
								leftUsagetableCell.setBorder(Rectangle.NO_BORDER);
								leftUsagetableCell.setColspan(8);
								usageAndCalcsTable.addCell(leftUsagetableCell);
								
								
							}
							//BURAYA SOÐUTMA SU GÝBÝ DAÐITIM HATLARI BAÞLIKLARI GELECEK
							
							PdfPTable rightUsageUpperTable = new PdfPTable(20);
							PdfPCell emptycell = new PdfPCell(new Phrase("", innerCellsfont));
							emptycell.setColspan(2);
							emptycell.setBorder(Rectangle.NO_BORDER);
							rightUsageUpperTable.addCell(emptycell);
							PdfPCell cell = new PdfPCell(new Phrase("Pay Ölçer Sayým", totalizationFont));
							cell.setColspan(6);
							rightUsageUpperTable.addCell(cell);
							cell = new PdfPCell(new Phrase("Isýtma", totalizationFont));cell.setColspan(6);
							rightUsageUpperTable.addCell(cell);
							cell = new PdfPCell(new Phrase("Ortak", totalizationFont));cell.setColspan(6);
							rightUsageUpperTable.addCell(cell);
							rightUsageUpperTable.setHeaderRows(1);
							cell.setPhrase(new Phrase("", totalizationFont));
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setColspan(2);
							rightUsageUpperTable.addCell(cell);
							cell.setColspan(6);
							cell.setPhrase(new Phrase(formatter.format(flatAndReadings.getTotalAllocator()) + " Str", innerCellsfont));
							rightUsageUpperTable.addCell(cell);
							cell.setPhrase(new Phrase(formatter.format(meterCharges)+ " TL" , innerCellsfont) );
							rightUsageUpperTable.addCell(cell);
							cell.setPhrase(new Phrase(formatter.format(commonCharges)+" TL", innerCellsfont));
							rightUsageUpperTable.addCell(cell);						
							PdfPTable rightUsageTable = new PdfPTable(1);	
							PdfPCell rightUsageUpperCell = new PdfPCell(rightUsageUpperTable);
							rightUsageUpperCell.setBorder(Rectangle.NO_BORDER);rightUsageUpperCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//							rightUsageUpperCell.setColspan(3);
//							innerusagecell8.setRowspan(5);
							rightUsageUpperCell.setBorder(Rectangle.LEFT);
							rightUsageTable.addCell(rightUsageUpperCell);
							PdfPCell totalCell = new PdfPCell(new Phrase("Enerji Bedeli: "+formatter.format(meterCharges+commonCharges), innerCellsfont));
//							totalCell.setColspan(20);
							totalCell.setBorder(Rectangle.TOP | Rectangle.LEFT);
							rightUsageTable.addCell(totalCell);
							totalCell = new PdfPCell(new Phrase("Ek Bedel: "+formatter.format(additionalPriceByFlat) , innerCellsfont));
//							totalCell.setColspan(20);
							totalCell.setBorder(Rectangle.LEFT);
							rightUsageTable.addCell(totalCell);
							totalCell = new PdfPCell(new Phrase("Toplam: "+ formatter.format(additionalPriceByFlat+meterCharges+commonCharges), totalizationFont));
//							totalCell.setColspan(20);
							totalCell.setBorder(Rectangle.LEFT);

							rightUsageTable.addCell(totalCell);
							PdfPCell rightUsageTableCell = new PdfPCell(rightUsageTable);
							
							rightUsageTableCell.setBorder(Rectangle.NO_BORDER);rightUsageUpperCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							rightUsageTableCell.setColspan(3);
							usageAndCalcsTable.addCell(rightUsageTableCell);
//							System.out.println("en alt y deðeri: "+document.bottom()); 
							document.add(usageAndCalcsTable);
							//dairelerin pay ölçerleri gezilmesi bitti
							
						}
							
								
			}
//			else // BÝNANIN BÝRDEN FAZLA DAÐITIM HATTI VAR ÝSE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			{
//				
//			}
			for (DistributionLine aDistributionLineOfBuilding : distributionLinesOfBuilding) {
				List<DistributionLineMeterType> distributionLineMeterTypes = distributionLineMeterTypeService.findbyDistributionLine(aDistributionLineOfBuilding);
				for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypes) {
					// daðýtým hattýnda hangi cihazlar var?
					// System.out.println("Ölçüm tipi kýsaltmasý: W"+distributionLineMeterType.getMeterType().getAbbreviation()+"W");
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("IP"))
						isIP = true;
//					System.out.println("isIP: " + isIP);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("IS"))
						isIS = true;
//					System.out.println("isIS: " + isIS);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SS"))
						isSS = true;
//					System.out.println("isSS: " + isSS);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("GS"))
						isGS = true;
//					System.out.println("isGS: " + isGS);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("ES"))
						isES = true;
//					System.out.println("isES: " + isES);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SýSS"))
						isSýSS = true;
//					System.out.println("isSýSS: " + isSýSS);
					if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SoSS"))
						isSoSS = true;
//					System.out.println("isSoSS: " + isSoSS);
				}
			}

			LineSeparator ls = new LineSeparator();
			document.add(new Chunk(ls));
		}

		System.out.println("döküman tamamlandý");
		document.close();

	}
	
	
	private Double totalStrOfAllocatorReadings(List<HeatCostAllocatorReading> startPeriodReadings,List<HeatCostAllocatorReading> endPeriodReadings)
	{
		startPeriodReadings = (List<HeatCostAllocatorReading>) Commons.sort(startPeriodReadings);
		endPeriodReadings = (List<HeatCostAllocatorReading>) Commons.sort(endPeriodReadings);
		Double totalStrs= 0.0;
		for (int i=0;i<startPeriodReadings.size();i++) {
			totalStrs = totalStrs + (endPeriodReadings.get(i).getValue()-startPeriodReadings.get(i).getValue())*endPeriodReadings.get(i).getHeatCostAllocator().getKges();
		}
		return totalStrs;
	}
	private Double totalConsumptionsOfHeatMeterReadings (List<HeatMeterReading> startPeriodReadings,List<HeatMeterReading> endPeriodReadings)
	{
		System.out.println("start period readings size: " +startPeriodReadings.size());
		startPeriodReadings = (List<HeatMeterReading>) Commons.sort(startPeriodReadings);
		endPeriodReadings = (List<HeatMeterReading>) Commons.sort(endPeriodReadings);
		Double totalCons= 0.0;
		for (int i=0;i<startPeriodReadings.size();i++) {
			totalCons = totalCons + (endPeriodReadings.get(i).getValue()-startPeriodReadings.get(i).getValue());
		}
		return totalCons;
	}
	private Double totalConsumptionsOfWaterMeterReadings (List<WaterMeterReading> startPeriodReadings,List<WaterMeterReading> endPeriodReadings)
	{
		System.out.println("start period readings size: " +startPeriodReadings.size());
		startPeriodReadings = (List<WaterMeterReading>) Commons.sort(startPeriodReadings);
		endPeriodReadings = (List<WaterMeterReading>) Commons.sort(endPeriodReadings);
		Double totalCons= 0.0;
		for (int i=0;i<startPeriodReadings.size();i++) {
			totalCons = totalCons + (endPeriodReadings.get(i).getValue()-startPeriodReadings.get(i).getValue());
		}
		return totalCons;
	}
	

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<String> getProjectMatches() {
		return projectMatches;
	}

	public void setProjectMatches(List<String> projectMatches) {
		this.projectMatches = projectMatches;
	}

	public String getProjectAutoCompleteString() {
		return projectAutoCompleteString;
	}

	public void setProjectAutoCompleteString(String projectAutoCompleteString) {
		this.projectAutoCompleteString = projectAutoCompleteString;
	}

	public String getDistributionLineAutoCompleteString() {
		return distributionLineAutoCompleteString;
	}

	public void setDistributionLineAutoCompleteString(String distributionLineAutoCompleteString) {
		this.distributionLineAutoCompleteString = distributionLineAutoCompleteString;
	}

	public List<DistributionLine> getDistributionLines() {
		return distributionLines;
	}

	public void setDistributionLines(List<DistributionLine> distributionLines) {
		this.distributionLines = distributionLines;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public List<Flat> getFlats() {
		return flats;
	}

	public void setFlats(List<Flat> flats) {
		this.flats = flats;
	}

	public List<ReportHeader> getReportHeaders() {
		return reportHeaders;
	}

	public void setReportHeaders(List<ReportHeader> reportHeader) {
		this.reportHeaders = reportHeader;
	}

	public List<HeatCostAllocator> getAllocatorsByDistributionLine() {
		return allocatorsByDistributionLine;
	}

	public void setAllocatorsByDistributionLine(List<HeatCostAllocator> allocatorsByBuilding) {
		this.allocatorsByDistributionLine = allocatorsByBuilding;
	}

	public String getStartPeriodString() {
		return startPeriodString;
	}

	public void setStartPeriodString(String startPeriodString) {
		this.startPeriodString = startPeriodString;
	}

	public String getEndPeriodString() {
		return endPeriodString;
	}

	public void setEndPeriodString(String endPeriodString) {
		this.endPeriodString = endPeriodString;
	}

	public List<Period> getPeriodsByDistLine() {
		return periodsByProject;
	}

	public void setPeriodsByDistLine(List<Period> periodsByDistLine) {
		this.periodsByProject = periodsByDistLine;
	}

	public Period getSelectedStartPeriod() {
		return selectedStartPeriod;
	}

	public void setSelectedStartPeriod(Period selectedStartPeriod) {
		System.out.println("start period set");
		this.selectedStartPeriod = selectedStartPeriod;
	}

	public Period getSelectedEndPeriod() {
		return selectedEndPeriod;
	}

	public void setSelectedEndPeriod(Period selectedEndPeriod) {
		System.out.println("end period set");
		this.selectedEndPeriod = selectedEndPeriod;
	}

	public Date getReadingPeriodDate() {
		return readingPeriodDate;
	}

	public void setReadingPeriodDate(Date readingPeriodDate) {
		this.readingPeriodDate = readingPeriodDate;
	}

	public Project getSelectedProjectforDistLine() {
		return selectedProject;
	}

	public void setSelectedProjectforDistLine(Project selectedProjectforDistLine) {
		this.selectedProject = selectedProjectforDistLine;
	}

	public DistributionLine getSelectedDistributionLine() {
		return selectedDistributionLine;
	}

	public void setSelectedDistributionLine(DistributionLine selectedDistributionLine) {
		this.selectedDistributionLine = selectedDistributionLine;
	}

	public List<String> getPeriodStrings() {
		return periodStrings;
	}

	public void setPeriodStrings(List<String> periodStrings) {
		this.periodStrings = periodStrings;
	}

	public List<EnergyHeader> getEnergyHeaders() {
		return energyHeaders;
	}

	public void setEnergyHeaders(List<EnergyHeader> energyHeaders) {
		this.energyHeaders = energyHeaders;
	}

	public Map<Flat, Double> getAllocatorValuesSumByFlat() {
		return allocatorValuesSumByFlat;
	}

	public void setAllocatorValuesSumByFlat(Map<Flat, Double> allocatorValuesSumByFlat) {
		this.allocatorValuesSumByFlat = allocatorValuesSumByFlat;
	}

	public EnergyHeader getEnergyHeader() {
		return energyHeader;
	}

	public void setEnergyHeader(EnergyHeader energyHeader) {
		this.energyHeader = energyHeader;
	}

	public EnergyHeader getEnergyHeader2() {
		return energyHeader2;
	}

	public void setEnergyHeader2(EnergyHeader energyHeader2) {
		this.energyHeader2 = energyHeader2;
	}

	public Double getAdditionalPrice() {
		return additionalPrice;
	}

	public void setAdditionalPrice(Double additionalPrice) {
		this.additionalPrice = additionalPrice;
	}

}
