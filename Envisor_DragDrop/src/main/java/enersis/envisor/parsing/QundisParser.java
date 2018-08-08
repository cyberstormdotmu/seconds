package enersis.envisor.parsing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.service.HeatCostAllocatorReadingService;
import enersis.envisor.service.HeatCostAllocatorService;
import enersis.envisor.service.RoomService;
import enersis.envisor.service.impl.HeatCostAllocatorReadingServiceImpl;
import enersis.envisor.service.impl.HeatCostAllocatorServiceImpl;

public class QundisParser extends DefaultHandler {

	// @Autowired
	private HeatCostAllocatorService heatCostAllocatorService = new HeatCostAllocatorServiceImpl();
	private HeatCostAllocatorReadingService heatCostAllocatorReadingService = new HeatCostAllocatorReadingServiceImpl();

//	private List<E>
	// @Autowired
	// private HeatCostAllocatorService heatCostAllocatorService;
	// @Autowired
	// private HeatCostAllocatorReadingService heatCostAllocatorReadingService;

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

	private QundisReading qundisReading = new QundisReading();
	boolean lastReading = false;
	private String valueField = "";
	private String allocatorId = "";
	private String hca;
	private Date measurementDate;

	private List<AllocatorPojo> heatCostAllocatorReadings = new ArrayList<AllocatorPojo>();

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
			HeatCostAllocatorReading reading = new HeatCostAllocatorReading();
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
//						measurementDate = new DateTime(2015, 3, 10, 0, 0, 0, 0).toDate();
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
					}
				}
			}

			if (fixeddataheader == true) {
				if (identnr == true) {
					allocatorId = innerString;
					// reading.setHeatCostAllocator(heatCostAllocatorService.findBySerialNo(innerString).get(0));
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
			AllocatorPojo reading = new AllocatorPojo();
			reading.setDate(measurementDate);
			System.out.println("pay ölçer no: " + allocatorId);
			if(allocatorId.equals("72138053"))
			{
				System.out.println("gandalf");
			}
			System.out.println("tarih" + measurementDate);
			// if(heatCostAllocatorService.findBySerialNo(allocatorId).size()==0)
			// {
			// System.out.println("bu pay ölçer yok:" +allocatorId);
			// }
			// else
			// {
			// reading.setHeatCostAllocatorId(Integer.parseInt(allocatorId) );
			// }
			reading.setHeatCostAllocatorId(Integer.parseInt(allocatorId));
			reading.setValue(Integer.parseInt(hca));
			// reading.setStatus((byte) 0 );
			// reading.setTransactionTime(new DateTime().toDate());
			heatCostAllocatorReadings.add(reading);
			measuredev = false;
		}
		if (qName.equalsIgnoreCase("fixeddataheader")) {
			fixeddataheader = false;
		}
		if (qName.equalsIgnoreCase("datapoint")) {
			lastReading=false;
			storagenr=false;
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

	}

	public List<AllocatorPojo> getReadings() {
		return heatCostAllocatorReadings;
	}

}
