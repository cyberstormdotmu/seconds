package enersis.envisor.utils;

public class AllocatorHeader {
	
	Integer roomNo;
	String allocatorSerialNo;
	Integer lastIndex;
	Integer currentIndex;
	Integer usage;
	String unit;
	Double kges;
	Double allocatorusage;
	
	
	
	
	public AllocatorHeader() {
		
	}
	public Integer getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(Integer roomNo) {
		this.roomNo = roomNo;
	}
	public String getAllocatorSerialNo() {
		return allocatorSerialNo;
	}
	public void setAllocatorSerialNo(String allocatorSerialNo) {
		this.allocatorSerialNo = allocatorSerialNo;
	}
	public Integer getLastIndex() {
		return lastIndex;
	}
	public void setLastIndex(Integer lastIndex) {
		this.lastIndex = lastIndex;
	}
	public Integer getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(Integer currentIndex) {
		this.currentIndex = currentIndex;
	}
	public Integer getUsage() {
		return usage;
	}
	public void setUsage(Integer usage) {
		this.usage = usage;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getKges() {
		return kges;
	}
	public void setKges(Double kges) {
		this.kges = kges;
	}
	public Double getAllocatorusage() {
		return allocatorusage;
	}
	public void setAllocatorusage(Double allocatorusage) {
		this.allocatorusage = allocatorusage;
	}
	

}
