package com.tatvasoft.entity;

import java.sql.Timestamp;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *	@author TatvaSoft
 *	This is entity (Model) class for Post.
 */
@Entity
@Table(name = "post", catalog = "orgnizational_forum")
public class PostEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private long postid;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private UserEntity userPost;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private CategoryEntity category;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userPost" )
	@Cascade(CascadeType.REMOVE)
	private Set<AnswerEntity> UserAnswer = new HashSet<AnswerEntity>(0);
	
	@Column(name="created_date", updatable=false)
	private Timestamp createDate;
	
	@Column(name="last_modified", updatable=false)
	private Timestamp lastModified;
	
	@Column(name = "question")
	private String question;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "viewcount")
	private long viewcount;
	
	@Column(name = "status")
	private char status;
	
	//Default constructor
	public PostEntity() {}
	
	//Getter-Setter methods  
	public long getPostid() {
		return postid;
	}

	public void setPostid(long postid) {
		this.postid = postid;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getViewcount() {
		return viewcount;
	}

	public void setViewcount(long viewcount) {
		this.viewcount = viewcount;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public UserEntity getUserPost() {
		return userPost;
	}

	public void setUserPost(UserEntity userPost) {
		this.userPost = userPost;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public Set<AnswerEntity> getUserAnswer() {
		return UserAnswer;
	}

	public void setUserAnswer(Set<AnswerEntity> userAnswer) {
		UserAnswer = userAnswer;
	}
	
}
