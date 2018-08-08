package fi.korri.epooq;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fi.korri.epooq.controller.AbstractBaseController;

@SuppressWarnings("serial")
public class Story implements Serializable, Comparable<Story> {
	
	private String id;
	private String title;
	private String place;
	private Long communityId = AbstractBaseController.PUBLIC_COMMUNITY_ID;
	private boolean ready = false;
	private Date created;

	private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	
	public Story(String id, String title, String place, Date created) {
		this.id = id;
		this.title = title;
		this.place = place;
		this.created = created;
	}

	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public String getPlace() {
		return place;
	}

	public String getDate() {
		return format.format(created);
	}

	public java.sql.Date getSqlDate() {
		return new java.sql.Date(created.getTime());
	}
	
	public long getDateUnixTime() {
		return created.getTime();
	}
	
	public int getYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(created);
		return cal.get(Calendar.YEAR);
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	@Override
	public int compareTo(Story o) {

		if(created.before(o.created)) {
			return 1;
		} else if (created.after(o.created)) {
			return -1;
		}
		
		return 0;
	}
	
	
}
