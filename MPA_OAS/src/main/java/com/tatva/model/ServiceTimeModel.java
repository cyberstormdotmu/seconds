package com.tatva.model;

import java.sql.Time;
/**
 * 
 * @author pci94
 *	POJO class for generating Service Time Report
 */
public class ServiceTimeModel{

	private String refrenceNumber;
	private Time processTime;
	private Time completeTime;
	

	private int counter;
	private long counterLessThan5;
	private long counterBetween5And10;
	private long counterGreaterThan10;

	private long avg;
	private long avgLessThan5;
	private long avgBetween5And10;
	private long avgGreaterThan10;
	
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public long getCounterLessThan5() {
		return counterLessThan5;
	}
	public void setCounterLessThan5(long counterLessThan5) {
		this.counterLessThan5 = counterLessThan5;
	}
	public long getCounterBetween5And10() {
		return counterBetween5And10;
	}
	public void setCounterBetween5And10(long counterBetween5And10) {
		this.counterBetween5And10 = counterBetween5And10;
	}
	public long getCounterGreaterThan10() {
		return counterGreaterThan10;
	}
	public void setCounterGreaterThan10(long counterGreaterThan10) {
		this.counterGreaterThan10 = counterGreaterThan10;
	}
	public long getAvg() {
		return avg;
	}
	public void setAvg(long avg) {
		this.avg = avg;
	}
	public long getAvgLessThan5() {
		return avgLessThan5;
	}
	public void setAvgLessThan5(long avgLessThan5) {
		this.avgLessThan5 = avgLessThan5;
	}
	public long getAvgBetween5And10() {
		return avgBetween5And10;
	}
	public void setAvgBetween5And10(long avgBetween5And10) {
		this.avgBetween5And10 = avgBetween5And10;
	}
	public long getAvgGreaterThan10() {
		return avgGreaterThan10;
	}
	public void setAvgGreaterThan10(long avgGreaterThan10) {
		this.avgGreaterThan10 = avgGreaterThan10;
	}
	public String getRefrenceNumber() {
		return refrenceNumber;
	}
	public void setRefrenceNumber(String refrenceNumber) {
		this.refrenceNumber = refrenceNumber;
	}
	public Time getProcessTime() {
		return processTime;
	}
	public void setProcessTime(Time processTime) {
		this.processTime = processTime;
	}
	public Time getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Time completeTime) {
		this.completeTime = completeTime;
	}

}
