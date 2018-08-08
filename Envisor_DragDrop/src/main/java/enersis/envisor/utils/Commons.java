package enersis.envisor.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineMeterType;
import enersis.envisor.entity.HeatCostAllocator;
import enersis.envisor.entity.HeatCostAllocatorReading;
import enersis.envisor.entity.HeatMeter;
import enersis.envisor.entity.HeatMeterReading;
import enersis.envisor.entity.WaterMeter;
import enersis.envisor.entity.WaterMeterReading;
import enersis.envisor.parsing.AllocatorPojo;
import enersis.envisor.parsing.HeatMeterPojo;
import enersis.envisor.parsing.WaterMeterPojo;

public  class Commons {
	
	
	
	public static List<?> sort(List<?> arrayList)
	{
		if(arrayList.get(0) instanceof HeatCostAllocator)	
		{
			System.out.println("commons class allocator instance check");
			List<HeatCostAllocator> heatCostAllocators = (List<HeatCostAllocator>)(List<?>) arrayList;
			Collections.sort(heatCostAllocators, new Comparator<HeatCostAllocator>() {
				@Override
				public int compare(HeatCostAllocator allocator1, HeatCostAllocator allocator2) {
					Integer serial1 =Integer.parseInt(allocator1.getSerialNo().replaceAll("\\s+","")); 
			        Integer serial2 =Integer.parseInt(allocator2.getSerialNo().replaceAll("\\s+","")); 
			        return serial1.compareTo(serial2);
				}
			});
			return  heatCostAllocators;
		}
		
		if(arrayList.get(0) instanceof WaterMeter)
		{
			List<WaterMeter> waterMeters = (List<WaterMeter>)(List<?>) arrayList;
			Collections.sort(waterMeters, new Comparator<WaterMeter>() {
				@Override
				public int compare(WaterMeter waterMeter1, WaterMeter waterMeter2) {
					Integer serial1 =Integer.parseInt(waterMeter1.getSerialNo().replaceAll("\\s+","")); 
			        Integer serial2 =Integer.parseInt(waterMeter2.getSerialNo().replaceAll("\\s+","")); 
			        return serial1.compareTo(serial2);
				}
			});
			return  waterMeters;
		}
		
		if(arrayList.get(0) instanceof HeatMeter)
		{
			List<HeatMeter> heatMeters = (List<HeatMeter>)(List<?>) arrayList;
			Collections.sort(heatMeters, new Comparator<HeatMeter>() {
				@Override
				public int compare(HeatMeter heatMeter1, HeatMeter heatMeter2) {
					Integer serial1 =Integer.parseInt(heatMeter1.getSerialNo().replaceAll("\\s+","")); 
			        Integer serial2 =Integer.parseInt(heatMeter2.getSerialNo().replaceAll("\\s+","")); 
			        return serial1.compareTo(serial2);
				}
			});
			return  heatMeters;
		}
		
		if(arrayList.get(0) instanceof AllocatorPojo)
		{
			List<AllocatorPojo> heatCostAllocators = (List<AllocatorPojo>)(List<?>) arrayList;
			Collections.sort(heatCostAllocators, new Comparator<AllocatorPojo>() {
				@Override
				public int compare(AllocatorPojo allocator1, AllocatorPojo allocator2) {
					Integer serial1 =allocator1.getHeatCostAllocatorId(); 
			        Integer serial2 =allocator2.getHeatCostAllocatorId(); 
			        return serial1.compareTo(serial2);
				}
			});
			return  heatCostAllocators;
		}
		
		if(arrayList.get(0) instanceof WaterMeterPojo)
		{
			List<WaterMeterPojo> waterPeterPojos = (List<WaterMeterPojo>)(List<?>) arrayList;
			Collections.sort(waterPeterPojos, new Comparator<WaterMeterPojo>() {
				@Override
				public int compare(WaterMeterPojo waterPeterPojo1, WaterMeterPojo waterPeterPojo2) {
					Integer serial1 =waterPeterPojo1.getWaterMeterId(); 
			        Integer serial2 =waterPeterPojo2.getWaterMeterId(); 
			        return serial1.compareTo(serial2);
				}
			});
			return  waterPeterPojos;
		}
		
		if(arrayList.get(0) instanceof HeatMeterPojo)
		{
			List<HeatMeterPojo> heatPeterPojos = (List<HeatMeterPojo>)(List<?>) arrayList;
			Collections.sort(heatPeterPojos, new Comparator<HeatMeterPojo>() {
				@Override
				public int compare(HeatMeterPojo heatPeterPojo1, HeatMeterPojo heatPeterPojo2) {
					Integer serial1 =heatPeterPojo1.getHeatMeterId(); 
			        Integer serial2 =heatPeterPojo2.getHeatMeterId(); 
			        return serial1.compareTo(serial2);
				}
			});
			return  heatPeterPojos;
		}
		
		
		
		if(arrayList.get(0) instanceof HeatCostAllocatorReading)	
		{
			System.out.println("commons class allocator Reading instance check");
			List<HeatCostAllocatorReading> heatCostAllocatorReadings = (List<HeatCostAllocatorReading>)(List<?>) arrayList;
			Collections.sort(heatCostAllocatorReadings, new Comparator<HeatCostAllocatorReading>() {
				@Override
				public int compare(HeatCostAllocatorReading allocatorReading1, HeatCostAllocatorReading allocatorReading2) {
					Integer serial1 =Integer.parseInt(allocatorReading1.getHeatCostAllocator().getSerialNo().replaceAll("\\s+","")); 
			        Integer serial2 =Integer.parseInt(allocatorReading2.getHeatCostAllocator().getSerialNo().replaceAll("\\s+","")); 
			        return serial1.compareTo(serial2);
				}
			});
			return  heatCostAllocatorReadings;
		}
		
		if(arrayList.get(0) instanceof WaterMeterReading)	
		{
			System.out.println("commons class water meter Reading instance check");
			List<WaterMeterReading> waterMeterReadings = (List<WaterMeterReading>)(List<?>) arrayList;
			Collections.sort(waterMeterReadings, new Comparator<WaterMeterReading>() {
				@Override
				public int compare(WaterMeterReading waterMeterReading1, WaterMeterReading waterMeterReading2) {
					Integer serial1 =Integer.parseInt(waterMeterReading1.getWaterMeter().getSerialNo().replaceAll("\\s+","")); 
			        Integer serial2 =Integer.parseInt(waterMeterReading2.getWaterMeter().getSerialNo().replaceAll("\\s+","")); 
			        return serial1.compareTo(serial2);
				}
			});
			return  waterMeterReadings;
		}
		if(arrayList.get(0) instanceof HeatMeterReading)	
		{
			System.out.println("commons class heat meter Reading instance check");
			List<HeatMeterReading> heatMeterReadings = (List<HeatMeterReading>)(List<?>) arrayList;
			Collections.sort(heatMeterReadings, new Comparator<HeatMeterReading>() {
				@Override
				public int compare(HeatMeterReading heatMeterReading1, HeatMeterReading heatMeterReading2) {
					Integer serial1 =Integer.parseInt(heatMeterReading1.getHeatMeter().getSerialNo().replaceAll("\\s+","")); 
			        Integer serial2 =Integer.parseInt(heatMeterReading2.getHeatMeter().getSerialNo().replaceAll("\\s+","")); 
			        return serial1.compareTo(serial2);
				}
			});
			return  heatMeterReadings;
		}
		
		
		return null;
	}
	
	
	public static String truncateDate(Date date)
	{
		DateTime dateTime = new DateTime(date);
		String truncated =""+dateTime.getDayOfMonth()+"."+dateTime.getMonthOfYear()+"."+dateTime.getYear();
		return truncated;
	}
	
	public static List<DistributionLine> orderDistLinesByType(List<DistributionLine> distributionLines,List<DistributionLineMeterType> distributionLineMeterTypesByProject)
	{
		List<DistributionLine> orderedDistLines= new ArrayList<DistributionLine>();
		List<DistributionLine> heatLines = new ArrayList<DistributionLine>();
		List<DistributionLine> waterLines = new ArrayList<DistributionLine>();
		for (DistributionLine distributionLine : distributionLines) {
			
		
		List<DistributionLineMeterType> distributionLineMeterTypesByDistLine = new ArrayList<DistributionLineMeterType>();
//		System.out.println("DAÐITIMHATTIÖLÇÜMTÝPÝ BÜYÜKLÜÐÜ:   "+ distributionLineMeterTypesByDistLine.size());
		for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypesByProject) {
			if(distributionLineMeterType.getDistributionLine().getId()==distributionLine.getId())
			{
//				System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
				distributionLineMeterTypesByDistLine.add(distributionLineMeterType);
			}
		}
		
		for (DistributionLineMeterType distributionLineMeterType : distributionLineMeterTypesByDistLine) {
			if (distributionLineMeterType.getMeterType().getAbbreviation().contains("IP") || distributionLineMeterType.getMeterType().getAbbreviation().contains("IS"))
			{
				heatLines.add(distributionLine);
			}
			if (distributionLineMeterType.getMeterType().getAbbreviation().contains("SoSS"))
			{
				waterLines.add(distributionLine);
			}
		}
		
		
		
		}
		orderedDistLines.addAll(heatLines);
		orderedDistLines.addAll(waterLines);
		return orderedDistLines;
	}
	

}
