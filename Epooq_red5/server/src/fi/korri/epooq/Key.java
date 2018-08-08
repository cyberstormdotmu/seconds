package fi.korri.epooq;

import fi.korri.epooq.controller.AbstractBaseController;

public class Key {

	private long id;
	private String question;
	private int age;
	private Long communityId = AbstractBaseController.PUBLIC_COMMUNITY_ID;
	private Long pictureId = null;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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
