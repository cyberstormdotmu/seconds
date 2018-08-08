package com.tatvasoft.service;

import java.util.List;

import com.tatvasoft.entity.AnswerEntity;
import com.tatvasoft.entity.UserEntity;

/**
 *	@author TatvaSoft
 *	This interface provide AnswerEntity's Service methods Declaration.
 */
public interface ReplyService {
	
	/*
	 * This method return List of all answers.
	 */
	public List<AnswerEntity> replyList();
	
	/*
	 * This method add answer on post. 
	 */
	public void addReply(AnswerEntity addReply, UserEntity userEntity);

	/*
	 * This method is use for find reply by postId. 
	 */
	public List<AnswerEntity> getReplyByPostID(long post_id);

	/*
	 * This method is use for delete post by it's postId.
	 */
	public void deleteReplyById(long actualAnswerId);

	/*
	 * This method is use for find answer by answerId.
	 */
	public AnswerEntity getAnswerReplyByID(long answerId);

	/*
	 * This method is use for update answer.
	 */
	public void updateReply(AnswerEntity replyUpdate);
	
}
