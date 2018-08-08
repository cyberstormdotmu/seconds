package fi.korri.epooq.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class StoryContent implements Serializable {
	
	private long id;
	private String description;
	private String type;
	private String content;
	private char narrationAudio	 = 'N';

	public StoryContent() {
	}
	
	public StoryContent(long id, String description, String type, String content, char narrationAudio) {
		this.id = id;
		this.description = description;
		this.type = type;
		this.content = content;
		this.narrationAudio = narrationAudio; 
	}
	
	public long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public char getNarrationAudio() {
		return narrationAudio;
	}

	public void setNarrationAudio(char narrationAudio) {
		this.narrationAudio = narrationAudio;
	}

	@Override
	public boolean equals(Object oo) 
	{
		StoryContent o = (StoryContent)oo;
		if(!StringUtils.equals(description, o.getDescription()))
		{
			return false;
		}
		else if(!StringUtils.equals(type, o.getType()))
		{
			return false;
		}
		else if(!StringUtils.equals(content, o.getContent()))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
}