package enersis.envisor.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "Window")
public class Window implements Serializable {

	private static final long serialVersionUID = -5662076798466888572L;

	private int id;
	private Wall wall;	// Used to set Primary Key- Foreign Key relation between Wall and Window table.
	private WindowType windowType;	// Used to set Primary Key- Foreign Key relation between Window and WindowType table.
	private String name;
	
	private String imageType;	// Used to set Image Type of selected Window Image.
	private int imageTop;	// Used to set Image Top (top according to display) of selected Window Image.
	private int imageLeft;	// Used to set Image Left (left according to display) of selected Window Image.
	private int imageWidth;	// Used to set Image Width of selected Window Image.
	private int imageHeight;	// Used to set Image Height of selected Window Image.
	private String imageDivId; // Added by TatvaSoft, used to set HTML Component 'Div' ID of selected Window Image.

	private Byte status;
	private Date transactionTime;

	public Window() {
	}

	public Window(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Window(int id, Wall wall, WindowType windowType, String name, Byte status,
			Date transactionTime) {
		this.id = id;
		this.wall = wall;
		this.windowType = windowType;
		this.name = name;
		this.status = status;
		this.transactionTime = transactionTime;
	}

	public Window(int id, Wall wall, WindowType windowType, String name,
			String imageType, int imageTop, int imageLeft, int imageWidth,
			int imageHeight, String imageDivId, Byte status,
			Date transactionTime) {
		super();
		this.id = id;
		this.wall = wall;
		this.windowType = windowType;
		this.name = name;
		this.imageType = imageType;
		this.imageTop = imageTop;
		this.imageLeft = imageLeft;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.imageDivId = imageDivId;
		this.status = status;
		this.transactionTime = transactionTime;
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
	@JoinColumn(name = "wallId", nullable = false)
	public Wall getWall() {
		return wall;
	}

	public void setWall(Wall wall) {
		this.wall = wall;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "windowTypeId")
	public WindowType getWindowType() {
		return windowType;
	}

	public void setWindowType(WindowType windowType) {
		this.windowType = windowType;
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

}
