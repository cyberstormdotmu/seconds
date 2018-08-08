package enersis.envisor.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import enersis.envisor.entity.Bill;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineMeterType;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.HeatMeter;
import enersis.envisor.entity.HeatMeterReading;
import enersis.envisor.entity.MeterType;
import enersis.envisor.entity.Period;
import enersis.envisor.entity.Project;
import enersis.envisor.entity.WaterMeter;
import enersis.envisor.entity.WaterMeterReading;
import enersis.envisor.parsing.AllocatorPojo;
import enersis.envisor.parsing.GeneralParser;
import enersis.envisor.parsing.HeatMeterPojo;
import enersis.envisor.parsing.WaterMeterPojo;
import enersis.envisor.service.BillService;
import enersis.envisor.service.DistributionLineBillService;
import enersis.envisor.service.DistributionLineMeterTypeService;
import enersis.envisor.service.DistributionLineService;
import enersis.envisor.service.HeatCostAllocatorReadingService;
import enersis.envisor.service.HeatCostAllocatorService;
import enersis.envisor.service.HeatMeterReadingService;
import enersis.envisor.service.HeatMeterService;
import enersis.envisor.service.PeriodService;
import enersis.envisor.service.ProjectService;
import enersis.envisor.service.WaterMeterReadingService;
import enersis.envisor.service.WaterMeterService;
import enersis.envisor.utils.Commons;
import enersis.envisor.utils.Constants;

@Component("readingUploadBean")
// @ViewScoped
@Scope("view")
// @SessionScoped
// @RequestScoped
@ManagedBean
public class ReadingUploadBean extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = 7456136397609590036L;
	private UploadedFile file;

	private Integer allocatorValueInputByDevice;
	private Integer heatMeterValueInputByDevice;
	private Integer waterMeterValueInputByDevice;
	private boolean absentReadingAlert = false;
	private String projectAutoCompleteString = "";
	private String distributionLineAutoCompleteString = "";
	private Project selectedProject = new Project();
	private DistributionLine selectedDistributionLine;
	private List<DistributionLine> distributionLines = new ArrayList<DistributionLine>();
	private Date readingPeriodDate;
	private Bill billForReading;
	private String billForReadingString;
	Period period = new Period();
	private Map<String, Integer> absentAllocatorValuesMap = new HashMap<String, Integer>();
	private Map<String, Integer> absentHeatMeterValuesMap = new HashMap<String, Integer>();
	private Map<String, Integer> absentWaterMeterValuesMap = new HashMap<String, Integer>();
	private List<Bill> unbindedBills = new ArrayList<Bill>();
	private List<Bill> selectedBills = new ArrayList<Bill>();
	private List<String> billDateStrings = new ArrayList<String>();
	private List<String> distributionLineMatches = new ArrayList<String>();
	private List<String> projectMatches = new ArrayList<String>();
	private List<Bill> billsByDistributionLine = new ArrayList<Bill>();
	private List<HeatCostAllocator> absentAllocatorsByReading = new ArrayList<HeatCostAllocator>();
	private List<WaterMeter> absentWaterMetersByReading = new ArrayList<WaterMeter>();
	private List<HeatMeter> absentHeatMetersByReading = new ArrayList<HeatMeter>();

	// okumalarýn cihazlarýný bind etmek için gerekli deðiþkenler...
	private List<HeatCostAllocator> heatCostAllocatorsByproject = new ArrayList<HeatCostAllocator>();

	@Autowired
	private ProjectService projectService;

	@Autowired
	private DistributionLineService distributionLineService;

	@Autowired
	private HeatCostAllocatorService heatCostAllocatorService;

	@Autowired
	private HeatCostAllocatorReadingService heatCostAllocatorReadingService;

	@Autowired
	private WaterMeterService waterMeterService;

	@Autowired
	private HeatMeterService heatMeterService;

	@Autowired
	private WaterMeterReadingService waterMeterReadingService;

	@Autowired
	private HeatMeterReadingService heatMeterReadingService;

	@Autowired
	private DistributionLineBillService distributionLineBillService;

	@Autowired
	private DistributionLineMeterTypeService distributionLineMeterTypeService;

	@Autowired
	private BillService billService;

	@Autowired
	private PeriodService periodService;

	@PostConstruct
	public void postConstruct() {
		billForReading = new Bill();
		unbindedBills = billService.findUnbindedBills();
	}

	public void onProjectSelect(SelectEvent event) {
		if (projectAutoCompleteString.equals("")) {
			System.out.println("equal");
			distributionLines = distributionLineService.findAll();

		} else {
			System.out.println("equal deðil");
			selectedProject = projectService.findByProjectName(projectAutoCompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ proje" + selectedProject.getProjectName());
			distributionLines = distributionLineService.findByProject(selectedProject);
			projectAutoCompleteString = "";
			selectedBills = findUnbindedBillsBySelectedProject();

			absentAllocatorsByReading.clear();

		}

		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Bitti!", "sýralandý allocators"));
	}

	public void onDistributionLineSelect(SelectEvent event) {
		billDateStrings.clear();
		System.out.println("onDistributionLineSelect: seçilen daðýtým hattý: " + distributionLineAutoCompleteString + "gan");
		if (distributionLineAutoCompleteString.equals("")) {

		} else {
			System.out.println("equal deðil");
			selectedDistributionLine = distributionLineService.findByDistributionLineName(distributionLineAutoCompleteString).get(0);
			System.out.println("auto complete sonrasý seçilmiþ daðýtým hattý: " + selectedDistributionLine.getName());
			// buildings =
			// buildingService.findbyDistributionLine(selectedDistributionLine);
			billsByDistributionLine = billService.findbyDistributionLine(selectedDistributionLine);
			distributionLineAutoCompleteString = "";
			billDateStrings.clear();
			billConverter();
			// for (Bill bill : billsByDistributionLine) {
			// System.out.println(bill.getDate().toString());
			// }
		}

	}

	public List<String> projectAutoComplete(String query) {
		System.out.println("gandalf");
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

	public void onBillSelect() {
		System.out.println("on bill select");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime dt = formatter.parseDateTime(billForReadingString);
		billForReading = billService.findByDate(dt.toDate()).get(0);
		System.out.println("date halinde: " + billForReading.getDate());
		System.out.println("date halinde:");
	}

	public List<Bill> findUnbindedBillsBySelectedProject() {
		// boolean projectCheck = false;
		// List<Bill> bills = new ArrayList<Bill>();
		// for (Bill bill : unbindedBills) {
		// if
		// (distributionLineBillService.findByBill(bill).get(0).getDistributionLine().getProject().getId()
		// == selectedProject.getId()) {
		// bills.add(bill);
		// }
		// }
		return unbindedBills;
	}

	public List<String> billConverter() {

		// System.out.println("burdayým");
		List<Bill> bills = new ArrayList<Bill>();
		bills = billService.findbyDistributionLine(selectedDistributionLine);
		// DateTimeFormatter formatter =
		// DateTimeFormat.forPattern("yyyy-mm-dd");

		for (Bill bill : bills) {
			// DateTime dateTime=
			// formatter.parseDateTime(bill.getDate().toString());
			billDateStrings.add(bill.getDate().toString());
		}
		return billDateStrings;
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

	public void onDateSelect(SelectEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
	}

	public void handleFileUpload(FileUploadEvent event) {
		boolean isIP = false;
		boolean isIS = false;
		boolean isSS = false;
		boolean isGS = false;
		boolean isES = false;
		boolean isSýSS = false;
		boolean isSoSS = false;
		boolean isBoyRefS = false;
		boolean IRefS = false;

		List<DistributionLineMeterType> distributionLineMeterTypesByProject = distributionLineMeterTypeService.findByProject(selectedProject);
		// List<DistributionLine> distributionLinesByProject =
		// distributionLineService.findByProject(selectedProject);
		System.out.println("Projeye göre daðýtým hattý sayaç çiftleri sayýsý " + distributionLineMeterTypesByProject.size());
		List<MeterType> meterTypesByProject = new ArrayList<MeterType>();
		for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypesByProject) {
			int i = 0;
			boolean isTypeFound = false;
			if (meterTypesByProject.size() == 0) {
				meterTypesByProject.add(distributionLineMeterType.getMeterType());
				isTypeFound = true;
			}

			while (!isTypeFound && i < meterTypesByProject.size()) {
				if (distributionLineMeterType.getMeterType().getEnum_() == meterTypesByProject.get(i).getEnum_()) {
					isTypeFound = true;
				} else {
					i++;
				}

			}
			if (!isTypeFound) {
				meterTypesByProject.add(distributionLineMeterType.getMeterType());
			}

		}
		for (MeterType meterType : meterTypesByProject) {
			System.out.println("Sayaç tipi: " + meterType.getName());
		}

		for (MeterType meterType : meterTypesByProject) {
			if (meterType.getEnum_() == Constants.IP)
				isIP = true;
			if (meterType.getEnum_() == Constants.IS)
				isIS = true;
			if (meterType.getEnum_() == Constants.SoSS)
				isSS = true;
			if (meterType.getEnum_() == Constants.SýSS)
				isSS = true;
		}

		// period.setBill(billForReading);
		period.setProject(selectedProject);
		period.setDate(readingPeriodDate);
		// period.setDistributionLine(selectedDistributionLine);
		period.setStatus((byte) 0);
		period.setTransactionTime(new DateTime().toDate());
		periodService.save(period);
		for (Bill bill : selectedBills) {
			bill.setPeriod(period);
			billService.save(bill);
		}
		file = event.getFile();
		try {
			GeneralParser generalParser = new GeneralParser();
			InputStream inputStream = file.getInputstream();
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(inputStream, generalParser); // parse edilme
															// aþamasý.
															// sadece xml parse
															// ediliyor.

			System.out.println("parsing bitmiþ");

			// pay ölçer
			// okumalarý----------------------------------------------------------------------------------------
			if (isIP) // EÐER PROJEDE PAY ÖLÇER VARSA
			{
				// Listeyi sýralýyoruz
				heatCostAllocatorsByproject = heatCostAllocatorService.findByProject(selectedProject);
				heatCostAllocatorsByproject = (List<HeatCostAllocator>) (List<?>) Commons.sort(heatCostAllocatorsByproject);
				System.out.println("size :" + heatCostAllocatorsByproject.size());
				List<AllocatorPojo> qundisPojos = generalParser.getAllocatorReadingsFromXML();
				qundisPojos = (List<AllocatorPojo>) (List<?>) Commons.sort(qundisPojos);

				if (qundisPojos.size() > heatCostAllocatorsByproject.size()) {
					System.out.println("");
				}
				Integer allocatorIndex = 0;
				Integer readingIndex = 0;

				Integer savingCount = 0;
				while (allocatorIndex < heatCostAllocatorsByproject.size() && readingIndex < heatCostAllocatorsByproject.size()) {
					if (Integer.parseInt(heatCostAllocatorsByproject.get(allocatorIndex).getSerialNo().replaceAll("\\s+", "")) == qundisPojos.get(readingIndex).getHeatCostAllocatorId()) {
						// System.out.println("eþit seri no. indisler: pay ölçer: "+allocatorIndex+" indis okuma: "+readingIndex);
						HeatCostAllocatorReading allocatorReading = new HeatCostAllocatorReading();
						allocatorReading.setDate(qundisPojos.get(readingIndex).getDate());
						allocatorReading.setHeatCostAllocator(heatCostAllocatorsByproject.get(allocatorIndex));
						allocatorReading.setValue(qundisPojos.get(readingIndex).getValue());
						allocatorReading.setPeriod(period);// dönem okumaya
															// baðlanýyor
						allocatorReading.setTransactionTime(new DateTime().toDate());
						allocatorReading.setStatus((byte) 0);
						heatCostAllocatorReadingService.save(allocatorReading);
						allocatorIndex++;
						readingIndex++;
						savingCount++;
					} else {
						if (heatCostAllocatorService.findBySerialNo(Integer.toString(qundisPojos.get(readingIndex).getHeatCostAllocatorId())).size() == 0) {
							System.out.println("Böyle bir pay ölçer yok projede");
							readingIndex++;
						} else // eksik pay ölçer
						{
							System.out.println("eksik okuma verisi: " + heatCostAllocatorsByproject.get(allocatorIndex).getSerialNo());
							absentAllocatorsByReading.add(heatCostAllocatorsByproject.get(allocatorIndex));
							allocatorIndex++;
						}
					}

				}
				System.out.println("pay ölçer okumalarý bitti.");
				if (absentAllocatorsByReading.size() > 0) {
					absentReadingAlert = true;
				}
				if (readingIndex < qundisPojos.size()) {
					System.out.println("projede bulunmayan (fazla) pay ölçerler");
					for (; readingIndex < qundisPojos.size(); readingIndex++) {
						System.out.println(qundisPojos.get(readingIndex).getHeatCostAllocatorId());
					}
				}

				for (HeatCostAllocator heatCostAllocator : absentAllocatorsByReading) {
					System.out.println("eksik pay ölçer (okumadan gelen) id: " + heatCostAllocator.getSerialNo());
				}
				System.out.println(savingCount);
			}

			// Su sayacý
			// okumalarý--------------------------------------------------------------------------------------------------
			if (isSS) {
				// Listeyi sýralýyoruz
				List<WaterMeter> waterMetersByProject = waterMeterService.findByProject(selectedProject);
				waterMetersByProject = (List<WaterMeter>) (List<?>) Commons.sort(waterMetersByProject);
				List<WaterMeterPojo> waterMeterPojos = generalParser.getWaterMeterReadingsFromXML();
				System.out.println("su sayacý okuma büyüklüðü: " + waterMeterPojos.size());
				waterMeterPojos = (List<WaterMeterPojo>) (List<?>) Commons.sort(waterMeterPojos);
				System.out.println("su sayacý büyüklüðü: "+waterMetersByProject.size());
				if (waterMeterPojos.size() > waterMetersByProject.size()) {
					System.out.println("");
				}
				Integer waterMeterIndex = 0;
				Integer readingIndex = 0;

				Integer savingCount = 0;
				
				for (WaterMeterPojo waterMeterPojo : waterMeterPojos) {
					System.out.println(waterMeterPojo.getWaterMeterId());
				}

				while (waterMeterIndex < waterMetersByProject.size() && readingIndex < waterMetersByProject.size()) {
					

					if (Integer.parseInt(waterMetersByProject.get(waterMeterIndex).getSerialNo().replaceAll("\\s+", "")) == waterMeterPojos.get(readingIndex).getWaterMeterId()) {
						// System.out.println("eþit seri no. indisler: pay ölçer: "+allocatorIndex+" indis okuma: "+readingIndex);
						WaterMeterReading waterMeterReading = new WaterMeterReading();
						waterMeterReading.setDate(waterMeterPojos.get(readingIndex).getDate());
						waterMeterReading.setWaterMeter(waterMetersByProject.get(waterMeterIndex));
						waterMeterReading.setValue(waterMeterPojos.get(readingIndex).getValue());
						waterMeterReading.setPeriod(period);// dönem okumaya
															// baðlanýyor
						waterMeterReading.setTransactionTime(new DateTime().toDate());
						waterMeterReading.setStatus((byte) 0);
						waterMeterReadingService.save(waterMeterReading);
						waterMeterIndex++;
						readingIndex++;
						savingCount++;
					} else {
						if (waterMeterService.findBySerialNo(Integer.toString(waterMeterPojos.get(readingIndex).getWaterMeterId())).size() == 0) {
							System.out.println("Böyle su sayacý yok projede");
							readingIndex++;
						} else // eksik su sayacý
						{
							System.out.println("eksik okuma verisi: " + waterMetersByProject.get(waterMeterIndex).getSerialNo());
							absentWaterMetersByReading.add(waterMetersByProject.get(waterMeterIndex));
							waterMeterIndex++;
						}
					}

				}
				System.out.println("su sayacý okumalarý bitti.");
				if (absentWaterMetersByReading.size() > 0) {
					absentReadingAlert = true;
				}
				if (readingIndex < waterMeterPojos.size()) {
					System.out.println("projede bulunmayan (fazla) pay ölçerler");
					for (; readingIndex < waterMeterPojos.size(); readingIndex++) {
						System.out.println(waterMeterPojos.get(readingIndex).getWaterMeterId());
					}
				}

				for (WaterMeter waterMeter : absentWaterMetersByReading) {
					System.out.println("eksik su sayacý (okumadan gelen) id: " + waterMeter.getSerialNo());
				}
				System.out.println(savingCount);
				// _______________________________________________________________________________________________________________________
			}

			// Su sayacý
			// okumalarý--------------------------------------------------------------------------------------------------
			if (isIS) {
				// Listeyi sýralýyoruz
				List<HeatMeter> heatMetersByProject = heatMeterService.findByProject(selectedProject);
				heatMetersByProject = (List<HeatMeter>) (List<?>) Commons.sort(heatMetersByProject);
				List<HeatMeterPojo> heatMeterPojos = generalParser.getHeatMeterReadingsFromXML();
				System.out.println("su sayacý okuma büyüklüðü: " + heatMeterPojos.size());
				heatMeterPojos = (List<HeatMeterPojo>) (List<?>) Commons.sort(heatMeterPojos);

				if (heatMeterPojos.size() > heatMetersByProject.size()) {
					System.out.println("");
				}
				Integer heatMeterIndex = 0;
				Integer readingIndex = 0;

				Integer savingCount = 0;

				while (heatMeterIndex < heatMetersByProject.size() && readingIndex < heatMetersByProject.size()) {
					if (Integer.parseInt(heatMetersByProject.get(heatMeterIndex).getSerialNo().replaceAll("\\s+", "")) == heatMeterPojos.get(readingIndex).getHeatMeterId()) {
						// System.out.println("eþit seri no. indisler: pay ölçer: "+allocatorIndex+" indis okuma: "+readingIndex);
						HeatMeterReading heatMeterReading = new HeatMeterReading();
						heatMeterReading.setDate(heatMeterPojos.get(readingIndex).getDate());
						heatMeterReading.setHeatMeter(heatMetersByProject.get(heatMeterIndex));
						heatMeterReading.setValue(heatMeterPojos.get(readingIndex).getValue());
						heatMeterReading.setPeriod(period);// dönem okumaya
															// baðlanýyor
						heatMeterReading.setTransactionTime(new DateTime().toDate());
						heatMeterReading.setStatus((byte) 0);
						heatMeterReadingService.save(heatMeterReading);
						heatMeterIndex++;
						readingIndex++;
						savingCount++;
					} else {
						if (heatMeterService.findBySerialNo(Integer.toString(heatMeterPojos.get(readingIndex).getHeatMeterId())).size() == 0) {
							System.out.println("Böyle ýsý sayacý yok projede");
							readingIndex++;
						} else // eksik su sayacý
						{
							System.out.println("eksik okuma verisi: " + heatMetersByProject.get(heatMeterIndex).getSerialNo());
							absentHeatMetersByReading.add(heatMetersByProject.get(heatMeterIndex));
							heatMeterIndex++;
						}
					}
				}
				System.out.println("ýsý sayacý okumalarý bitti.");
				if (absentHeatMetersByReading.size() > 0) {
					absentReadingAlert = true;
				}
				if (readingIndex < heatMeterPojos.size()) {
					System.out.println("projede bulunmayan (fazla) ýsý sayacý");
					for (; readingIndex < heatMeterPojos.size(); readingIndex++) {
						System.out.println(heatMeterPojos.get(readingIndex).getHeatMeterId());
					}
				}

				for (HeatMeter heatMeter : absentHeatMetersByReading) {
					System.out.println("eksik ýsý sayacý (okumadan gelen) id: " + heatMeter.getSerialNo());
				}
				System.out.println(savingCount);
				// _______________________________________________________________________________________________________________________
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		if (file != null) {
			FacesMessage message = new FacesMessage("Baþarýlý", event.getFile().getFileName() + " dosyasý yüklendi");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public void allocatorValueInput(String allocatorSerialNo) {
		System.out.println("allocatorValueInput çalýþtý");
		if (absentAllocatorValuesMap.get(allocatorSerialNo) == null) {
			absentAllocatorValuesMap.put(allocatorSerialNo, allocatorValueInputByDevice);
			System.out.println("pay ölçer: " + allocatorSerialNo);
			System.out.println("deðer :" + allocatorValueInputByDevice);
		}

	}
	public void heatMeterValueInput(String heatMeterSerialNo) {
//		System.out.println("allocatorValueInput çalýþtý");
		if (absentHeatMeterValuesMap.get(heatMeterSerialNo) == null) {
			absentHeatMeterValuesMap.put(heatMeterSerialNo, heatMeterValueInputByDevice);
			System.out.println("pay ölçer: " + heatMeterSerialNo);
			System.out.println("deðer :" + heatMeterValueInputByDevice);
		}

	}
	public void waterMeterValueInput(String waterMeterSerialNo) {
//		System.out.println("allocatorValueInput çalýþtý");
		if (absentWaterMeterValuesMap.get(waterMeterSerialNo) == null) {
			absentWaterMeterValuesMap.put(waterMeterSerialNo, waterMeterValueInputByDevice);
			System.out.println("pay ölçer: " + waterMeterSerialNo);
			System.out.println("deðer :" + waterMeterValueInputByDevice);
		}

	}
	
	
	public void saveAbsentMetersReadings() {
		System.out.println("burdayým");
		System.out.println("absentAllocatorValuesMap size: " + absentAllocatorValuesMap.size());
		System.out.println("absentHeatMeterValuesMap size: " + absentHeatMeterValuesMap.size());
		System.out.println("absentWaterMeterValuesMap size: " + absentWaterMeterValuesMap.size());
		for (int i = 0; i < absentAllocatorValuesMap.size(); i++) {

			HeatCostAllocatorReading allocatorReading = new HeatCostAllocatorReading();
			allocatorReading.setDate(new DateTime().toDate());
			allocatorReading.setHeatCostAllocator(absentAllocatorsByReading.get(i));
			allocatorReading.setValue(absentAllocatorValuesMap.get(absentAllocatorsByReading.get(i).getSerialNo()));
			allocatorReading.setPeriod(period);// dönem okumaya baðlanýyor
			allocatorReading.setTransactionTime(new DateTime().toDate());
			allocatorReading.setStatus((byte) 0);
			heatCostAllocatorReadingService.save(allocatorReading);
		}
		for (int i = 0; i < absentHeatMeterValuesMap.size(); i++) {

			HeatMeterReading heatmeterReading = new HeatMeterReading();
			heatmeterReading.setDate(new DateTime().toDate());
			heatmeterReading.setHeatMeter(absentHeatMetersByReading.get(i));
			heatmeterReading.setValue(absentHeatMeterValuesMap.get(absentHeatMetersByReading.get(i).getSerialNo()));
			heatmeterReading.setPeriod(period);// dönem okumaya baðlanýyor
			heatmeterReading.setTransactionTime(new DateTime().toDate());
			heatmeterReading.setStatus((byte) 0);
			heatMeterReadingService.save(heatmeterReading);
		}
		for (int i = 0; i < absentWaterMeterValuesMap.size(); i++) {

			WaterMeterReading watermeterReading = new WaterMeterReading();
			watermeterReading.setDate(new DateTime().toDate());
			watermeterReading.setWaterMeter(absentWaterMetersByReading.get(i));
			watermeterReading.setValue(absentWaterMeterValuesMap.get(absentWaterMetersByReading.get(i).getSerialNo()));
			watermeterReading.setPeriod(period);// dönem okumaya baðlanýyor
			watermeterReading.setTransactionTime(new DateTime().toDate());
			watermeterReading.setStatus((byte) 0);
			waterMeterReadingService.save(watermeterReading);
		}
	}

	public String buttonString() {
		if (absentAllocatorsByReading.size() == 0) {
			return "Tamam";
		} else {
			return "Kaydet";
		}
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

	public List<DistributionLine> getDistributionLines() {
		return distributionLines;
	}

	public void setDistributionLines(List<DistributionLine> distributionLines) {
		this.distributionLines = distributionLines;
	}

	public List<String> getDistributionLineMatches() {
		return distributionLineMatches;
	}

	public void setDistributionLineMatches(List<String> distributionLineMatches) {
		this.distributionLineMatches = distributionLineMatches;
	}

	public List<String> getProjectMatches() {
		return projectMatches;
	}

	public void setProjectMatches(List<String> projectMatches) {
		this.projectMatches = projectMatches;
	}

	public Date getReadingPeriodDate() {
		return readingPeriodDate;
	}

	public void setReadingPeriodDate(Date readingPeriodDate) {
		this.readingPeriodDate = readingPeriodDate;
	}

	public Bill getBillForReading() {
		return billForReading;
	}

	public void setBillForReading(Bill billForReading) {
		System.out.println("bill set");
		this.billForReading = billForReading;
	}

	public List<Bill> getBillsByDistributionLine() {
		return billsByDistributionLine;
	}

	public void setBillsByDistributionLine(List<Bill> billsByDistributionLine) {
		this.billsByDistributionLine = billsByDistributionLine;
	}

	public List<String> getBillDateStrings() {
		return billDateStrings;
	}

	public void setBillDateStrings(List<String> billDateStrings) {
		// System.out.println("burdayým");
		this.billDateStrings = billDateStrings;
	}

	public String getBillForReadingString() {
		return billForReadingString;
	}

	public void setBillForReadingString(String billForReadingString) {
		System.out.println("burdayým");
		this.billForReadingString = billForReadingString;
	}

	public List<Bill> getSelectedBills() {
		return selectedBills;
	}

	public void setSelectedBills(List<Bill> selectedBills) {
		this.selectedBills = selectedBills;
	}

	public List<Bill> getUnbindedBills() {
		return unbindedBills;
	}

	public void setUnbindedBills(List<Bill> unbindedBills) {
		this.unbindedBills = unbindedBills;
	}

	public List<HeatCostAllocator> getAbsentAllocatorsByReading() {
		return absentAllocatorsByReading;
	}

	public void setAbsentAllocatorsByReading(List<HeatCostAllocator> absentAllocatorsByReading) {
		this.absentAllocatorsByReading = absentAllocatorsByReading;
	}

	public boolean isAbsentReadingAlert() {
		return absentReadingAlert;
	}

	public void setAbsentReadingAlert(boolean absentReadingAlert) {
		this.absentReadingAlert = absentReadingAlert;
	}

	public Map<String, Integer> getAbsentAllocatorValuesMap() {
		return absentAllocatorValuesMap;
	}

	public void setAbsentAllocatorValuesMap(Map<String, Integer> absentAllocatorValuesMap) {
		this.absentAllocatorValuesMap = absentAllocatorValuesMap;
	}

	public Integer getAllocatorValueInputByDevice() {
		return allocatorValueInputByDevice;
	}

	public void setAllocatorValueInputByDevice(Integer allocatorValueInput) {
		this.allocatorValueInputByDevice = allocatorValueInput;
	}

	public List<WaterMeter> getAbsentWaterMetersByReading() {
		return absentWaterMetersByReading;
	}

	public void setAbsentWaterMetersByReading(List<WaterMeter> absentWaterMetersByReading) {
		this.absentWaterMetersByReading = absentWaterMetersByReading;
	}

	public List<HeatMeter> getAbsentHeatMetersByReading() {
		return absentHeatMetersByReading;
	}

	public void setAbsentHeatMetersByReading(List<HeatMeter> absentHeatMetersByReading) {
		this.absentHeatMetersByReading = absentHeatMetersByReading;
	}

	public Integer getHeatMeterValueInputByDevice() {
		return heatMeterValueInputByDevice;
	}

	public void setHeatMeterValueInputByDevice(Integer heatMeterValueInputByDevice) {
		this.heatMeterValueInputByDevice = heatMeterValueInputByDevice;
	}

	public Integer getWaterMeterValueInputByDevice() {
		return waterMeterValueInputByDevice;
	}

	public void setWaterMeterValueInputByDevice(Integer waterMeterValueInputByDevice) {
		this.waterMeterValueInputByDevice = waterMeterValueInputByDevice;
	}

}
