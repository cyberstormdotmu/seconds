package com.tatvasoft.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatvasoft.dao.VoteDao;
import com.tatvasoft.entity.VoteEntity;

/**
 *	@author TatvaSoft
 *	This interface provide VoteEntity's Service methods Implementation.
 */
@Service
@Transactional
public class VoteServiceImpl implements VoteService {

	@Autowired
	VoteDao voteDao;

	/*
	 * All implemented methods of VoteService Interface.
	 */
	public void addVote(long answerId) {
		voteDao.addVote(answerId);
	}

	public void removeVote(long answerId, long userid) {
		voteDao.removeVote(answerId, userid);
	}

	public List<VoteEntity> getVoteByUserId(long userid) {
		return voteDao.getVoteByUserId(userid);
	}

	@Transactional
	public List<VoteEntity> voteList() {
		return voteDao.voteList();
	}

}
