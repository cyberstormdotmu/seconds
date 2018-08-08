package com.tatvasoft.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatvasoft.entity.AnswerEntity;
import com.tatvasoft.entity.UserEntity;
import com.tatvasoft.entity.VoteEntity;

@Repository
public class VoteDaoImpl implements VoteDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ReplyDao replyDao;
	
	@Autowired
	private UserDao userHibernateDao;
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void addVote(long answerId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		AnswerEntity answerEntity = replyDao.getAnswerReplyByID(answerId);
		long userid = answerEntity.getUserAnswer().getUserid();
		
		UserEntity userEntity = userHibernateDao.getUserByUserId(userid);
		
		VoteEntity voteEntity = new VoteEntity();
		voteEntity.setUserAnswer(answerEntity);
		voteEntity.setUserVote(userEntity);
		
		session.save(voteEntity);
		
	}

	public List<VoteEntity> voteList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from VoteEntity");

		@SuppressWarnings("unchecked")
		List<VoteEntity> allVotes = (List<VoteEntity>) queryResult.list();
		
		return allVotes;
	}
	
	public void removeVote(long answerId, long userid) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from VoteEntity where userVote.userid=:userid AND userAnswer.answerid=:answerId");
		queryResult.setParameter("answerId", answerId);
		queryResult.setParameter("userid", userid);
		
		@SuppressWarnings("unchecked")
		List<VoteEntity> allVotes = (List<VoteEntity>) queryResult.list();
		
		VoteEntity voteEntity = null;
		
		if(!allVotes.isEmpty()){
			voteEntity = allVotes.get(0);
			System.out.println(voteEntity);
		} 	
		
		session.delete(voteEntity);
		//session.flush();
	}

	public List<VoteEntity> getVoteByUserId(long userid) {

		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from VoteEntity where userVote.userid =:userid");
		queryResult.setParameter("userid", userid);
		
		@SuppressWarnings("unchecked")
		List<VoteEntity> allvote = (List<VoteEntity>) queryResult.list();

		return allvote;
	}
	
	
}
