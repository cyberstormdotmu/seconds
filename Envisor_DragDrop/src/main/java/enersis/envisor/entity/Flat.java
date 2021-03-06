package enersis.envisor.entity;

// Generated Apr 28, 2015 10:59:09 AM by Hibernate Tools 4.3.1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Flat generated by hbm2java
 */
@Entity
@Table(name = "Flat")
public class Flat implements Serializable {

	private int id;
	private Building building;
	private String no;
	private Byte floor;
	private Short area;

	private String name; // Added by TatvaSoft, used to set name of selected Flat Image.
	private String imageType; // Added by TatvaSoft, used to set Image Type of selected Flat Image.
	private int imageTop; // Added by TatvaSoft, used to set Image Top (top according to display) of selected Flat Image.
	private int imageLeft; // Added by TatvaSoft, used to set Image Left (left according to display) of selected Flat Image.
	private int imageWidth; // Added by TatvaSoft, used to set Image Width of selected Flat Image.
	private int imageHeight; // Added by TatvaSoft, used to set Image Height of selected Flat Image.
	
	private String imageDivId; // Added by TatvaSoft, used to set HTML Component 'Div' ID of selected Flat Image.

	private Byte status;
	private Date transactionTime;
	private Set<HeatMeter> heatMeters = new HashSet<HeatMeter>(0);
	private Set<Installation> installations = new HashSet<Installation>(0);
	private Set<Room> rooms = new HashSet<Room>(0);
	private Set<WaterMeter> waterMeters = new HashSet<WaterMeter>(0);

	private Floor floorId; //Added by TatvaSoft, Following getter-setter used to set Primary Key- Foreign Key relation between Floor and Flat table.

	public Flat() {
	}

	public Flat(int id, String no) {
		this.id = id;
		this.no = no;
	}

	public Flat(int id, Building building, String no, Byte floor, Short area,
			Byte status, Date transactionTime, Set<HeatMeter> heatMeters,
			Set<Installation> installations, Set<Room> rooms,
			Set<WaterMeter> waterMeters) {
		this.id = id;
		this.building = building;
		this.no = no;
		this.floor = floor;
		this.area = area;
		this.status = status;
		this.transactionTime = transactionTime;
		this.heatMeters = heatMeters;
		this.installations = installations;
		this.rooms = rooms;
		this.waterMeters = waterMeters;
	}

	// Added by TatvaSoft, Following constructor can use with DragDrop functionalities.
	public Flat(int id, Building building, String no, Byte floor, Short area,
			String name, String imageType, int imageTop, int imageLeft,
			int imageWidth, int imageHeight, Byte status, Date transactionTime,
			Set<HeatMeter> heatMeters, Set<Installation> installations,
			Set<Room> rooms, Set<WaterMeter> waterMeters, Floor floorId) {
		this.id = id;
		this.building = building;
		this.no = no;
		this.floor = floor;
		this.area = area;
		this.name = name;
		this.imageType = imageType;
		this.imageTop = imageTop;
		this.imageLeft = imageLeft;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.status = status;
		this.transactionTime = transactionTime;
		this.heatMeters = heatMeters;
		this.installations = installations;
		this.rooms = rooms;
		this.waterMeters = waterMeters;
		this.floorId = floorId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "buildingId")
	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@Column(name = "no", nullable = false)
	public String getNo() {
		return this.no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Column(name = "floor")
	public Byte getFloor() {
		return this.floor;
	}

	public void setFloor(Byte floor) {
		this.floor = floor;
	}

	@Column(name = "area")
	public Short getArea() {
		return this.area;
	}

	public void setArea(Short area) {
		this.area = area;
	}

	//Added by TatvaSoft, Following getters-setters used to set Image name,top,left,width and height : starts.
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "imageType")
	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	@Column(name = "imageTop")
	public int getImageTop() {
		return imageTop;
	}

	public void setImageTop(int imageTop) {
		this.imageTop = imageTop;
	}

	@Column(name = "imageLeft")
	public int getImageLeft() {
		return imageLeft;
	}

	public void setImageLeft(int imageLeft) {
		this.imageLeft = imageLeft;
	}

	@Column(name = "imageWidth")
	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	@Column(name = "imageHeight")
	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	@Transient
	public String getImageDivId() {
		return imageDivId;
	}

	public void setImageDivId(String imageDivId) {
		this.imageDivId = imageDivId;
	}

	//Added by TatvaSoft, Following getters-setters used to set Image name,top,left,width and height : end.

	@Column(name = "status")
	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transactionTime", length = 23)
	public Date getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flat")
	public Set<HeatMeter> getHeatMeters() {
		return this.heatMeters;
	}

	public void setHeatMeters(Set<HeatMeter> heatMeters) {
		this.heatMeters = heatMeters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flat")
	public Set<Installation> getInstallations() {
		return this.installations;
	}

	public void setInstallations(Set<Installation> installations) {
		this.installations = installations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flat")
	public Set<Room> getRooms() {
		return this.rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flat")
	public Set<WaterMeter> getWaterMeters() {
		return this.waterMeters;
	}

	public void setWaterMeters(Set<WaterMeter> waterMeters) {
		this.waterMeters = waterMeters;
	}

	//Added by TatvaSoft, Following getter-setter used to set Primary Key- Foreign Key relation between Floor and Flat table.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "floorId")
	public Floor getFloorId() {
		return this.floorId;
	}

	public void setFloorId(Floor floorId) {
		this.floorId = floorId;
	}

}
