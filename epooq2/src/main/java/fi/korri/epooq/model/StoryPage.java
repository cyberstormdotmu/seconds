package fi.korri.epooq.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class StoryPage implements Serializable, Comparable<StoryPage> {
	
	private long id;
	private long storyId;
	private StoryContent storyContent;
	private Date created;
	private int index;
	
	public StoryPage() {
	}
	
	public StoryPage(long id, long storyId, StoryContent content, Date created) {
		this.id = id;
		this.storyId = storyId;
		this.storyContent = content;
		this.created = created;
	}
	
	public long getId() {
		return id;
	}

	public long getStoryId() {
		return storyId;
	}

	public void setStoryId(long storyId) {
		this.storyId = storyId;
	}

	public StoryContent getStoryContent() {
		return storyContent;
	}

	public void setStoryContent(StoryContent storyContent) {
		this.storyContent = storyContent;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int compareTo(StoryPage o) {
		
		if(created.before(o.created)) {
			return 1;
		} else if(created.after(o.created)) {
			return -1;
		}
		
		return 0;
	}
	
}
