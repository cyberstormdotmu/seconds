package com.tatvasoft.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

/**
 *	@author TatvaSoft
 *	This is entity (Model) class for Answer.
 */
@Entity
@Table(name = "answer", catalog = "orgnizational_forum")
public class AnswerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "answer_id")
	private long answerid;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private UserEntity userAnswer;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private PostEntity userPost;

	@Column(name="date", updatable=false)
	private Timestamp ansDate;
	
	@Column(name="last_modified", updatable=false)
	private Timestamp lastModified;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "userAnswer")
	@Cascade(CascadeType.REMOVE)
	private Set<VoteEntity> userVote = new HashSet<VoteEntity>(0);
	
	//Default constructor 	
	public AnswerEntity() {}

	// Getter and Setter methods
	public long getAnswerid() {
		return answerid;
	}

	public void setAnswerid(long answerid) {
		this.answerid = answerid;
	}

	public Timestamp getAnsDate() {
		return ansDate;
	}

	public void setAnsDate(Timestamp ansDate) {
		this.ansDate = ansDate;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserEntity getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(UserEntity userAnswer) {
		this.userAnswer = userAnswer;
	}

	public PostEntity getUserPost() {
		return userPost;
	}

	public void setUserPost(PostEntity userPost) {
		this.userPost = userPost;
	}

	public Set<VoteEntity> getUserVote() {
		return userVote;
	}

	public void setUserVote(Set<VoteEntity> userVote) {
		this.userVote = userVote;
	}

}
