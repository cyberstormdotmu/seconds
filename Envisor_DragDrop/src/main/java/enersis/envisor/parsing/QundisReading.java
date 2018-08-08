package enersis.envisor.parsing;

import java.util.Date;

import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Project;

public class QundisReading {

	private Date readingDate;
	private Project project;
	private DistributionLine distributionLine;
	private int readingDeviceNumber;
	
	
	public QundisReading() {
	}
	public Date getReadingDate() {
		return readingDate;
	}
	public void setReadingDate(Date readingDate) {
		this.readingDate = readingDate;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public DistributionLine getDistributionLine() {
		return distributionLine;
	}
	public void setDistributionLine(DistributionLine distributionLine) {
		this.distributionLine = distributionLine;
	}
	public int getReadingDeviceNumber() {
		return readingDeviceNumber;
	}
	public void setReadingDeviceNumber(int readingDeviceNumber) {
		this.readingDeviceNumber = readingDeviceNumber;
	}
	
}
