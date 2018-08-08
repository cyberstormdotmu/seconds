package fi.korri.epooq.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class Story implements Serializable, Comparable<Story> {
	
	private long id;
	private String title;
	private String city;
	private String country;
	private Date storyDate;
	private String publishing = "PRIVATE";
	private char ready = 'N';
	private Date created;
	private List<StoryPage> storyPages = new ArrayList<StoryPage>();
	private String year;
	private String lattitude;
	private String longitude;
	private User user;

	public Story() {
	}
	
	public Story(long id,
				 String title, 
				 String city, 
				 String country, 
				 Date storyDate,
				 String publishing,
				 char ready,
				 Date created,
				 String lattitude,
				 String longitude,
				 User user) 
	{
		this.id = id;
		this.title = title;
		this.city = city;
		this.country = country;
		this.storyDate = storyDate;
		this.publishing = publishing;
		this.ready = ready;
		this.created = created;
		this.lattitude=lattitude;
		this.longitude=longitude;
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getStoryDate() {
		return storyDate;
	}

	public void setStoryDate(Date storyDate) {
		this.storyDate = storyDate;
	}

	public String getPublishing() {
		return publishing;
	}

	public void setPublishing(String publishing) {
		this.publishing = publishing;
	}

	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}

	/*public java.sql.Date getSqlDate() {
		return new java.sql.Date(created.getTime());
	}
	
	public long getDateUnixTime() {
		return created.getTime();
	}
	
	
	
	public int getYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(created);
		return cal.get(Calendar.YEAR);
	}*/
	
	public List<StoryPage> getStoryPages() {
		return storyPages;
	}

	public void setStoryPages(List<StoryPage> storyPages) {
		this.storyPages = storyPages;
	}

	public char isReady() {
		return ready;
	}

	public void setReady(char ready) {
		this.ready = ready;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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
