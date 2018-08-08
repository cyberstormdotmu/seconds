package enersis.envisor.utils;

import java.util.Date;

public class ReportHeader
{
	private String billNo;
	private Date periodStart;
	private Date periodEnd;
	private String firstReading;
	private String lastindex;
	private Double usage;
	private Double usageKWH;
	private Double charge;
	
	
	
	public ReportHeader() {
//		this.billNo="gandalf";
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Date getPeriodStart() {
		return periodStart;
	}
	public void setPeriodStart(Date periodStart) {
		this.periodStart = periodStart;
	}
	public Date getPeriodEnd() {
		return periodEnd;
	}
	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}
	public String getFirstReading() {
		return firstReading;
	}
	public void setFirstReading(String firstReading) {
		this.firstReading = firstReading;
	}
	public String getLastindex() {
		return lastindex;
	}
	public void setLastindex(String lastindex) {
		this.lastindex = lastindex;
	}
	public Double getUsage() {
		return usage;
	}
	public void setUsage(Double usage) {
		this.usage = usage;
	}
	public Double getUsageKWH() {
		return usageKWH;
	}
	public void setUsageKWH(Double usageKWH) {
		this.usageKWH = usageKWH;
	}
	public Double getCharge() {
		return charge;
	}
	public void setCharge(Double charge) {
		this.charge = charge;
	}
	
	
}