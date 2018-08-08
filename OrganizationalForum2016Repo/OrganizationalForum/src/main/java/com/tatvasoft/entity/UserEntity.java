package com.tatvasoft.entity;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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

import org.hibernate.annotations.Cascade;

/**
 * @author TatvaSoft This is entity (Model) class for User.
 */
@Entity
@Table(name = "user", catalog = "orgnizational_forum")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long userid;

	@ManyToOne
	@JoinColumn(name = "roleid", nullable = false)
	private RoleEntity role;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userPost")
	@Cascade(CascadeType.REMOVE)
	private Set<PostEntity> post = new HashSet<PostEntity>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAnswer")
	@Cascade(CascadeType.REMOVE)
	private Set<AnswerEntity> answer = new HashSet<AnswerEntity>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userVote")
	@Cascade(CascadeType.REMOVE)
	private Set<VoteEntity> vote = new HashSet<VoteEntity>(0);

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "avtar")
	private Blob avtar;

	@Column(name = "created_date", updatable = false)
	private Timestamp createDate;

	@Column(name = "last_modified", updatable = false)
	private Timestamp lastModified;

	// Default constructor
	public UserEntity() {
	}

	// Getter and Setter methods
	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public Set<PostEntity> getPost() {
		return post;
	}

	public void setPost(Set<PostEntity> post) {
		this.post = post;
	}

	public Set<AnswerEntity> getAnswer() {
		return answer;
	}

	public void setAnswer(Set<AnswerEntity> answer) {
		this.answer = answer;
	}

	public Set<VoteEntity> getVote() {
		return vote;
	}

	public void setVote(Set<VoteEntity> vote) {
		this.vote = vote;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Blob getAvtar() {
		return avtar;
	}

	public void setAvtar(Blob avtar) {
		this.avtar = avtar;
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

}
