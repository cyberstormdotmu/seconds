package enersis.envisor.entity;

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
 * 
 * @author TatvaSoft
 * Hibernate Entity : Wall mapped with wall table in database.
 *  
 */

@Entity
@Table(name = "Wall")
public class Wall implements Serializable {

	private static final long serialVersionUID = -8323290015739543009L;

	private int id;
	private Room room;	// Used to set Primary Key- Foreign Key relation between Room and Wall table.
	private WallType wallType;	// Used to set Primary Key- Foreign Key relation between WallType and Wall table.
	private String name;
	
	private String imageType;	// Used to set Image Type of selected Room Image.
	private int imageTop;	// Used to set Image Top (top according to display) of selected Room Image.
	private int imageLeft;	// Used to set Image Left (left according to display) of selected Room Image.
	private int imageWidth;	// Used to set Image Width of selected Room Image.
	private int imageHeight;	// Used to set Image Height of selected Room Image.
	private String imageDivId; // Added by TatvaSoft, used to set HTML Component 'Div' ID of selected Room Image.

	private Byte status;
	private Date transactionTime;
	private Set<Door> doors = new HashSet<Door>(0);
	private Set<Window> windows = new HashSet<Window>(0);
	
	public Wall() {
	}

	public Wall(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Wall(int id, Room room, WallType wallType, String name, Byte status,
			Date transactionTime, Set<Door> doors, Set<Window> windows) {
		this.id = id;
		this.room = room;
		this.wallType = wallType;
		this.name = name;
		this.status = status;
		this.transactionTime = transactionTime;
		this.doors = doors;
		this.windows = windows;
	}
	
	public Wall(int id, Room room, WallType wallType, String name,
			String imageType, int imageTop, int imageLeft, int imageWidth,
			int imageHeight, String imageDivId, Byte status,
			Date transactionTime, Set<Door> doors, Set<Window> windows) {
		super();
		this.id = id;
		this.room = room;
		this.wallType = wallType;
		this.name = name;
		this.imageType = imageType;
		this.imageTop = imageTop;
		this.imageLeft = imageLeft;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.imageDivId = imageDivId;
		this.status = status;
		this.transactionTime = transactionTime;
		this.doors = doors;
		this.windows = windows;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roomId", nullable = false)
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	// Following getter-setter added by PCT25
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wallTypeId")
	public WallType getWallType() {
		return wallType;
	}

	public void setWallType(WallType wallType) {
		this.wallType = wallType;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//Following getters-setters used to set Image top,left,width and height : starts.
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
	//Following getters-setters used to set Image top,left,width and height : end.

	@Column(name = "status")
	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transactionTime", length = 23)
	public Date getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	// Following getter-setter added by PCT25
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wall")
	public Set<Door> getDoors() {
		return doors;
	}

	public void setDoors(Set<Door> doors) {
		this.doors = doors;
	}

	// Following getter-setter added by PCT25
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wall")
	public Set<Window> getWindows() {
		return windows;
	}

	public void setWindows(Set<Window> windows) {
		this.windows = windows;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wall other = (Wall) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
