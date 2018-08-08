package enersis.envisor.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
 * Hibernate Entity : Floor mapped with floor table in database.
 *  
 */

@Entity
@Table(name = "Floor")
public class Floor implements Serializable {

	private static final long serialVersionUID = 1637965566046464384L;

	private int id;
	private Building building;	// Used to set Primary Key- Foreign Key relation between Building and Floor table.
	private String name;
	private byte flatCount;

	private String imageType;	// Used to set Image Type of selected Floor Image.
	private int imageTop;	// Used to set Image Top (top according to display) of selected Floor Image.
	private int imageLeft;	// Used to set Image Left (left according to display) of selected Floor Image.
	private int imageWidth;	// Used to set Image Width of selected Floor Image.
	private int imageHeight;	// Used to set Image Height of selected Floor Image.
	private String imageDivId; // Added by TatvaSoft, used to set HTML Component 'Div' ID of selected Floor Image.

	private Byte status;
	private Date transactionTime;
	private Set<Flat> flats = new HashSet<Flat>(0); // Used to set Primary Key- Foreign Key relation between Floor and Flat table.
	
	//Default Constructor
	public Floor() {
	}

	public Floor(int id, String name, byte flatCount) {
		this.id = id;
		this.name = name;
		this.flatCount = flatCount;
	}

	public Floor(int id, Building building, String name, byte flatCount,
			String imageType, int imageTop, int imageLeft, int imageWidth,
			int imageHeight, Byte status, Date transactionTime, Set<Flat> flats) {
		this.id = id;
		this.building = building;
		this.name = name;
		this.flatCount = flatCount;
		this.imageType = imageType;
		this.imageTop = imageTop;
		this.imageLeft = imageLeft;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.status = status;
		this.transactionTime = transactionTime;
		this.flats = flats;
	}

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// Used to set Primary Key- Foreign Key relation between Building and Floor table.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "buildingId")
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "flatCount", nullable = false)
	public byte getFlatCount() {
		return flatCount;
	}

	public void setFlatCount(byte flatCount) {
		this.flatCount = flatCount;
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

	// Used to set Primary Key- Foreign Key relation between Floor and Flat table.
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "floorId")
	public Set<Flat> getFlats() {
		return flats;
	}

	public void setFlats(Set<Flat> flats) {
		this.flats = flats;
	}
	
}
