package com.tatvasoft.dao;

import java.util.List;

import com.tatvasoft.entity.VoteEntity;

/**
 *	@author TatvaSoft
 *	This interface provide VoteEntity's Repository methods
 *  	Declaration.
 */
public interface VoteDao {

	/*
	 * This method add new vote.
	 */
	public void addVote(long answerId);

	/*
	 * This method is use for delete vote.
	 */
	public void removeVote(long answerId, long userid);

	/*
	 * This method is use for find vote by userId. 
	 */
	public List<VoteEntity> getVoteByUserId(long userid);

	/*
	 * This method returns List of all votes. 
	 */
	public List<VoteEntity> voteList();

}
