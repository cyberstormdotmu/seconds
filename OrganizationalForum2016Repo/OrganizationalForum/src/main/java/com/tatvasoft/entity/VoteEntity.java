package com.tatvasoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author TatvaSoft This is entity (Model) class for Vote.
 */
@Entity
@Table(name = "vote", catalog = "orgnizational_forum")
public class VoteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vote_id")
	private long voteid;

	@ManyToOne
	@JoinColumn(name = "vote_owner_id", nullable = false)
	private UserEntity userVote;

	@ManyToOne
	@JoinColumn(name = "answer_id", nullable = false)
	private AnswerEntity userAnswer;

	//Default constructor
	public VoteEntity() {}

	// Getter and Setter methods
	public long getVoteid() {
		return voteid;
	}

	public void setVoteid(long voteid) {
		this.voteid = voteid;
	}

	public UserEntity getUserVote() {
		return userVote;
	}

	public void setUserVote(UserEntity userVote) {
		this.userVote = userVote;
	}

	public AnswerEntity getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(AnswerEntity userAnswer) {
		this.userAnswer = userAnswer;
	}

}
