package fi.korri.epooq.model;

public class MapImage 
{
	private long id;
	private String startLat;
	private String startLong;
	private String endLat;
	private String endLong;
	private String file;
	private boolean enableFlag;
	private int level;
	private String title;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
/*	public double getStartLat() {
		return startLat;
	}
	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}
	public double getStartLong() {
		return startLong;
	}
	public void setStartLong(double startLong) {
		this.startLong = startLong;
	}
	public double getEndLat() {
		return endLat;
	}
	public void setEndLat(double endLat) {
		this.endLat = endLat;
	}
	public double getEndLong() {
		return endLong;
	}
	public void setEndLong(double endLong) {
		this.endLong = endLong;
	}*/
	public String getFile() {
		return file;
	}
	public String getStartLat() {
		return startLat;
	}
	public void setStartLat(String startLat) {
		this.startLat = startLat;
	}
	public String getStartLong() {
		return startLong;
	}
	public void setStartLong(String startLong) {
		this.startLong = startLong;
	}
	public String getEndLat() {
		return endLat;
	}
	public void setEndLat(String endLat) {
		this.endLat = endLat;
	}
	public String getEndLong() {
		return endLong;
	}
	public void setEndLong(String endLong) {
		this.endLong = endLong;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public boolean isEnableFlag() {
		return enableFlag;
	}
	public void setEnableFlag(boolean enableFlag) {
		this.enableFlag = enableFlag;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
