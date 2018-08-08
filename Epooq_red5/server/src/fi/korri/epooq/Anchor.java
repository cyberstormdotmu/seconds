package fi.korri.epooq;

import java.util.Date;

import fi.korri.epooq.controller.AbstractBaseController;

public class Anchor {

	private long id;
	private String title;
	private Date date;
	private String place;
	private String content;
	private Long communityId = AbstractBaseController.PUBLIC_COMMUNITY_ID;
	private Long pictureId = null;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public java.sql.Date getSqlDate() {
		return new java.sql.Date(date.getTime());
	}
	
	public long getDateUnixTime() {
		return date.getTime();
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getPictureId() {
		return pictureId;
	}

	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}
}
