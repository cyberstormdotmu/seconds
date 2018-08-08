package com.tatvasoft.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatvasoft.dao.PostDao;
import com.tatvasoft.dao.ReplyDao;
import com.tatvasoft.dao.UserDao;
import com.tatvasoft.entity.AnswerEntity;
import com.tatvasoft.entity.UserEntity;

/**
 *	@author TatvaSoft
 *	This interface provide AnswerEntity's Service methods Implementation.
 */
@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	PostDao postDao;

	@Autowired
	UserDao userHibernateDao;

	@Autowired
	ReplyDao replyDao;

	/*
	 * All implemented methods of ReplyService Interface.
	 */
	@Transactional
	public List<AnswerEntity> replyList() {
		return replyDao.replyList();
	}

	@Transactional
	public void addReply(AnswerEntity answerEntity, UserEntity userEntity) {
		replyDao.addReply(answerEntity, userEntity);
	}

	public List<AnswerEntity> getReplyByPostID(long post_id) {
		return replyDao.getReplyByPostID(post_id);
	}

	public void deleteReplyById(long answerId) {
		replyDao.deleteReplyById(answerId);
	}

	public AnswerEntity getAnswerReplyByID(long answerId) {
		return replyDao.getAnswerReplyByID(answerId);
	}

	public void updateReply(AnswerEntity replyUpdate) {
		replyDao.updateReply(replyUpdate);
	}

}
