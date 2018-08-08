package enersis.envisor.parsing;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.WaterMeterReading;
import enersis.envisor.service.WaterMeterReadingService;
import enersis.envisor.service.WaterMeterService;

public class GeneralParser extends DefaultHandler {

	// Ölçüm Cihazlarýnýn enumerasyonlarý
	private static final Integer HEAT_METER_DEVTYPE = 4; // Isý sayacý
	private static final Integer WATER_READING_DEVTYPE = 7; // Su sayacý
	private static final Integer HEATCOSTALLOCATOR_DEVTYPE = 8; // Isý pay Ölçer

	// private static final Integer

	boolean siemeca = false;
	boolean communicator = false;
	boolean network = false;
	boolean colectordev = false;
	boolean measuredev = false;
	boolean fixeddataheader = false;
	boolean datapoint = false;
	boolean fabnr = false;
	boolean identnr = false;
	boolean storagenr = false;
	boolean dimension = false;
	boolean value = false;
	boolean devtype = false;

	// private QundisReading qundisReading = new QundisReading();
	boolean lastReading = false;
	private String valueField = "";
	private String deviceId = "";
	private String hca;
	private String m3;
	private String kwh;
	private String deviceType;
	private Date measurementDate;

	private List<AllocatorPojo> heatCostAllocatorReadings = new ArrayList<AllocatorPojo>();
	private List<WaterMeterPojo> waterMeterReadings = new ArrayList<WaterMeterPojo>();
	private List<HeatMeterPojo> heatMeterReadings = new ArrayList<HeatMeterPojo>();

	@Autowired
	private WaterMeterReadingService waterMeterReadingService;

	@Autowired
	private WaterMeterService waterMeterService;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {

		if (qName.equalsIgnoreCase("siemeca")) {
			siemeca = true;
		}

		if (qName.equalsIgnoreCase("communicator")) {
			communicator = true;
		}
		if (qName.equalsIgnoreCase("network")) {
			network = true;
		}
		if (qName.equalsIgnoreCase("colectordev")) {
			colectordev = true;
		}
		if (qName.equalsIgnoreCase("measuredev")) {
			measuredev = true;
		}
		if (qName.equalsIgnoreCase("datapoint")) {
			datapoint = true;
		}
		if (qName.equalsIgnoreCase("fixeddataheader")) {
			fixeddataheader = true;
		}
		if (qName.equalsIgnoreCase("fabnr")) {
			fabnr = true;
		}
		if (qName.equalsIgnoreCase("identnr")) {
			identnr = true;
		}

		if (qName.equalsIgnoreCase("storagenr")) {
			storagenr = true;
		}
		if (qName.equalsIgnoreCase("dimension")) {
			dimension = true;
		}
		if (qName.equalsIgnoreCase("value")) {
			value = true;
		}
		if (qName.equalsIgnoreCase("devtype")) {
			devtype = true;
		}

	}

	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {
		String innerString = new String(ch, start, length);
		if (colectordev == true) {
			if (datapoint == true) {

			}
			if (fixeddataheader == true) {
				if (identnr == true) {
					// qundisReading.setReadingDeviceNumber(Integer
					// .parseInt(innerString));
					// allocatorId=innerString;
				}

			}
		}
		if (measuredev == true) {
			// HeatCostAllocatorReading reading = new
			// HeatCostAllocatorReading();
			// reading.setHeatCostAllocator(heatCostAllocatorService.findBySerialNo(serialNo));
			if (datapoint == true) {

				if (storagenr == true) {
					if (innerString.equalsIgnoreCase("0")) {
						lastReading = true;
					}
				}
				if (value == true) {
					if (lastReading == true) {
						valueField = innerString;
					}
				}
				if (dimension == true) {
					if (lastReading) {

						if (innerString.equalsIgnoreCase("H.C.A.")
								|| innerString.equalsIgnoreCase("HCA")) {
							// reading.setValue(Integer.parseInt(valueField));
							hca = valueField;
						}
						// measurementDate = new DateTime(2015, 3, 10, 0, 0, 0,
						// 0).toDate();
						if (innerString.equalsIgnoreCase("dateTime")) {
							DateTimeFormatter formatter = DateTimeFormat
									.forPattern("dd.MM.yy HH:mm:ss");
							measurementDate = formatter.parseDateTime(
									valueField).toDate();
						}
						if (innerString.equalsIgnoreCase("date")) {
							DateTimeFormatter formatter = DateTimeFormat
									.forPattern("dd.MM.yy HH:mm:ss");
							measurementDate = formatter.parseDateTime(
									valueField).toDate();
						}
						if (innerString.equalsIgnoreCase("m3")) {
							m3 = valueField;
						}

					}
				}
			}

			if (fixeddataheader == true) {
				if (identnr == true) {
					deviceId = innerString;
					// reading.setHeatCostAllocator(heatCostAllocatorService.findBySerialNo(innerString).get(0));
				}
				if (devtype == true) {
					deviceType = innerString;
				}

			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("communicator")) {
			communicator = false;
		}
		if (qName.equalsIgnoreCase("network")) {
			network = false;
		}
		if (qName.equalsIgnoreCase("colectordev")) {
			colectordev = false;
		}
		if (qName.equalsIgnoreCase("measuredev")) {

			if (Integer.parseInt(deviceType) == HEATCOSTALLOCATOR_DEVTYPE) {
				AllocatorPojo reading = new AllocatorPojo();
				reading.setDate(measurementDate);
//				System.out.println("pay ölçer no: " + deviceId);
//				System.out.println("tarih" + measurementDate);
				reading.setHeatCostAllocatorId(Integer.parseInt(deviceId));
				reading.setValue(Integer.parseInt(hca));
				heatCostAllocatorReadings.add(reading);
				measuredev = false;
			}

			if (Integer.parseInt(deviceType) == WATER_READING_DEVTYPE) {
				WaterMeterPojo reading = new WaterMeterPojo();
				reading.setDate(measurementDate);
//				System.out.println("su sayacý no: " + deviceId);
//				System.out.println("tarih" + measurementDate);
				reading.setWaterMeterId(Integer.parseInt(deviceId));
				System.out.println("su sayacý cihaz no: "+deviceId);
				try {
					NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
					Number number;
					number = format.parse(m3);
					reading.setValue(number.doubleValue());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				waterMeterReadings.add(reading);
				measuredev = false;
			}

			if (Integer.parseInt(deviceType) == HEAT_METER_DEVTYPE) {
				HeatMeterPojo reading = new HeatMeterPojo();
				reading.setDate(measurementDate);
//				System.out.println("su sayacý no: " + deviceId);
//				System.out.println("tarih" + measurementDate);
				reading.setHeatMeterId(Integer.parseInt(deviceId));
				reading.setValue(Double.parseDouble(kwh));
				heatMeterReadings.add(reading);
				measuredev = false;
			}

		}

		if (qName.equalsIgnoreCase("fixeddataheader")) {
			fixeddataheader = false;
		}
		if (qName.equalsIgnoreCase("datapoint")) {
			lastReading = false;
			storagenr = false;
			datapoint = false;
		}
		if (qName.equalsIgnoreCase("fabnr")) {
			fabnr = false;
		}
		if (qName.equalsIgnoreCase("identnr")) {
			identnr = false;
		}

		if (qName.equalsIgnoreCase("storagenr")) {
			storagenr = false;
		}

		if (qName.equalsIgnoreCase("dimension")) {
			dimension = false;
		}
		if (qName.equalsIgnoreCase("value")) {
			value = false;
		}
		if (qName.equalsIgnoreCase("siemeca")) {

			// for (HeatCostAllocatorReading heatCostAllocatorReading :
			// heatCostAllocatorReadings) {
			// heatCostAllocatorReadingService.save(heatCostAllocatorReading);
			// }
			siemeca = false;
		}
		if (qName.equalsIgnoreCase("devtype")) {
			devtype = false;
		}

	}

	public List<AllocatorPojo> getAllocatorReadingsFromXML() {
		return heatCostAllocatorReadings;
	}

	public List<HeatMeterPojo> getHeatMeterReadingsFromXML() {
		return heatMeterReadings;
	}

	public List<WaterMeterPojo> getWaterMeterReadingsFromXML() {
		return waterMeterReadings;
	}
}
