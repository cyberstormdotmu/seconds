package com.tatvasoft.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatvasoft.entity.AnswerEntity;
import com.tatvasoft.entity.UserEntity;

@Repository
public class ReplyDaoImpl implements ReplyDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void addReply(AnswerEntity answerEntity , UserEntity userEntity) {
		
		Session session = sessionFactory.getCurrentSession();

		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		answerEntity.setUserAnswer(userEntity);
		
		answerEntity.setAnsDate(timestamp);
		
		session.save(answerEntity);
				
	}
	
	public List<AnswerEntity> replyList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from AnswerEntity");

		@SuppressWarnings("unchecked")
		List<AnswerEntity> allReplys = (List<AnswerEntity>) queryResult.list();
		
		return allReplys;
	}
	
	public List<AnswerEntity> getReplyByPostID(long post_id) {	
	
		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from AnswerEntity where userPost.postid =:post_id");
		queryResult.setParameter("post_id", post_id);
		
		@SuppressWarnings("unchecked")
		List<AnswerEntity> allreply = (List<AnswerEntity>) queryResult.list();

		return allreply;
	}

	public void deleteReplyById(long answerId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		AnswerEntity answerEntity = getAnswerReplyByID(answerId);
		
		session.delete(answerEntity);
				
	}
	
	public AnswerEntity getAnswerReplyByID(long answerId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from AnswerEntity where answerid =:answerId");
		queryResult.setParameter("answerId", answerId);
		
		
		@SuppressWarnings("unchecked")
		List<AnswerEntity> allAnswers = (List<AnswerEntity>) queryResult.list();
		
		AnswerEntity answerEntity = null;
		
		if(!allAnswers.isEmpty()){
			answerEntity = allAnswers.get(0);
		} 		
		return answerEntity;
	}

	public void updateReply(AnswerEntity answerEntity) {
		
		Session session = sessionFactory.getCurrentSession();
		
		long answerid = answerEntity.getAnswerid();
		String description = answerEntity.getDescription();
		
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		Query updateReply = session.createQuery("update AnswerEntity set description=:description, last_modified=:lastmodified where answerid=:answerid");
		updateReply.setParameter("description", description);
		updateReply.setParameter("answerid", answerid);
		updateReply.setParameter("lastmodified", timestamp);
				
		updateReply.executeUpdate();
		
	}
	
}
