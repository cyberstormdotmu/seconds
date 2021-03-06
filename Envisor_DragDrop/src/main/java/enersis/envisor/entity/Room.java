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
 * Room generated by hbm2java
 */
@Entity
@Table(name = "Room")
public class Room implements Serializable {

	private int id;
	private Flat flat;
	private byte orderNo;
	private Short baseArea;
	private Short windowArea;
	private Byte glassTypeId;
	private Byte wallTypeId;
	private Byte status;
	private Date transactionTime;
	private Set<HeatCostAllocator> heatCostAllocators = new HashSet<HeatCostAllocator>(
			0);

	private String name; // Added by TatvaSoft, used to set name of selected Room Image.
	private String imageType; // Added by TatvaSoft, used to set Image Type of selected Room Image.
	private int imageTop; // Added by TatvaSoft, used to set Image Top (top according to display) of selected Room Image.
	private int imageLeft; // Added by TatvaSoft, used to set Image Left (left according to display) of selected Room Image.
	private int imageWidth; // Added by TatvaSoft, used to set Image Width of selected Room Image.
	private int imageHeight; // Added by TatvaSoft, used to set Image Height of selected Room Image.
	private String imageDivId; // Added by TatvaSoft, used to set HTML Component 'Div' ID of selected Room Image.

	private RoomType roomType; // Added by TatvaSoft, used to set Primary Key- Foreign Key relation between Room and RoomType table.
	private Set<Wall> walls = new HashSet<Wall>(0); // Added by TatvaSoft, used to set Primary Key- Foreign Key relation between Room and Wall table.

	public Room() {
	}

	public Room(int id, Flat flat, byte orderNo) {
		this.id = id;
		this.flat = flat;
		this.orderNo = orderNo;
	}

	public Room(int id, Flat flat, byte orderNo, Short baseArea,
			Short windowArea, Byte glassTypeId, Byte wallTypeId, Byte status,
			Date transactionTime, Set<HeatCostAllocator> heatCostAllocators) {
		this.id = id;
		this.flat = flat;
		this.orderNo = orderNo;
		this.baseArea = baseArea;
		this.windowArea = windowArea;
		this.glassTypeId = glassTypeId;
		this.wallTypeId = wallTypeId;
		this.status = status;
		this.transactionTime = transactionTime;
		this.heatCostAllocators = heatCostAllocators;
	}

	// Added by TatvaSoft, Following constructor can use with DragDrop functionalities.
	public Room(int id, Flat flat, byte orderNo, Short baseArea,
			Short windowArea, Byte glassTypeId, Byte wallTypeId, Byte status,
			Date transactionTime, Set<HeatCostAllocator> heatCostAllocators,
			String name, String imageType, int imageTop, int imageLeft,
			int imageWidth, int imageHeight, RoomType roomType, Set<Wall> walls) {
		this.id = id;
		this.flat = flat;
		this.orderNo = orderNo;
		this.baseArea = baseArea;
		this.windowArea = windowArea;
		this.glassTypeId = glassTypeId;
		this.wallTypeId = wallTypeId;
		this.status = status;
		this.transactionTime = transactionTime;
		this.heatCostAllocators = heatCostAllocators;
		this.name = name;
		this.imageType = imageType;
		this.imageTop = imageTop;
		this.imageLeft = imageLeft;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.roomType = roomType;
		this.walls = walls;
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
	@JoinColumn(name = "flatId", nullable = false)
	public Flat getFlat() {
		return this.flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	@Column(name = "orderNo", nullable = false)
	public byte getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(byte orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "baseArea")
	public Short getBaseArea() {
		return this.baseArea;
	}

	public void setBaseArea(Short baseArea) {
		this.baseArea = baseArea;
	}

	@Column(name = "windowArea")
	public Short getWindowArea() {
		return this.windowArea;
	}

	public void setWindowArea(Short windowArea) {
		this.windowArea = windowArea;
	}

	@Column(name = "glassTypeId")
	public Byte getGlassTypeId() {
		return this.glassTypeId;
	}

	public void setGlassTypeId(Byte glassTypeId) {
		this.glassTypeId = glassTypeId;
	}

	@Column(name = "wallTypeId")
	public Byte getWallTypeId() {
		return this.wallTypeId;
	}

	public void setWallTypeId(Byte wallTypeId) {
		this.wallTypeId = wallTypeId;
	}

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
	public Set<HeatCostAllocator> getHeatCostAllocators() {
		return this.heatCostAllocators;
	}

	public void setHeatCostAllocators(Set<HeatCostAllocator> heatCostAllocators) {
		this.heatCostAllocators = heatCostAllocators;
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

	
	//Added by TatvaSoft, Following getter-setter used to set Primary Key- Foreign Key relation between Room and RoomType table.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roomType")
	public RoomType getRoomType() {
		return this.roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	//Added by TatvaSoft, Following getter-setter used to set Primary Key- Foreign Key relation between Room and Wall table.
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
	public Set<Wall> getWalls() {
		return this.walls;
	}

	public void setWalls(Set<Wall> walls) {
		this.walls = walls;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", flat=" + flat + ", orderNo=" + orderNo
				+ ", baseArea=" + baseArea + ", windowArea=" + windowArea
				+ ", glassTypeId=" + glassTypeId + ", wallTypeId=" + wallTypeId
				+ ", status=" + status + ", transactionTime=" + transactionTime
				+ ", heatCostAllocators=" + heatCostAllocators + ", name="
				+ name + ", imageType=" + imageType + ", imageTop=" + imageTop
				+ ", imageLeft=" + imageLeft + ", imageWidth=" + imageWidth
				+ ", imageHeight=" + imageHeight + ", imageDivId=" + imageDivId
				+ ", roomType=" + roomType + ", walls=" + walls + "]";
	}
	
	

}
