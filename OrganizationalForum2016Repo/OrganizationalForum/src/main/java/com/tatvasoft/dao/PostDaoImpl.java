package com.tatvasoft.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatvasoft.entity.CategoryEntity;
import com.tatvasoft.entity.PostEntity;
import com.tatvasoft.entity.UserEntity;

@Repository
public class PostDaoImpl implements PostDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void addPost(PostEntity addUserPost , UserEntity userEntity) {
		Session session = sessionFactory.getCurrentSession();
				
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		addUserPost.setCreateDate(timestamp);
		
		addUserPost.setUserPost(userEntity);
				
		session.save(addUserPost);
	}

	public List<PostEntity> postList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from PostEntity");

		@SuppressWarnings("unchecked")
		List<PostEntity> allPosts = (List<PostEntity>) queryResult.list();
		
		return allPosts;
	}
	
	public List<PostEntity> postListByCategory(long category_id) {

		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from PostEntity where category.categoryid=:category_id");
		queryResult.setParameter("category_id", category_id);
				
		@SuppressWarnings("unchecked")
		List<PostEntity> allposts = (List<PostEntity>) queryResult.list();
		
		return allposts;
	}
	
	public List<PostEntity> getPostListByUserId(long userId) {
		
		Session session = sessionFactory.openSession();
		
		Query queryResult = session.createQuery("from PostEntity where userPost.userid=:userId");
		queryResult.setParameter("userId", userId);
				
		@SuppressWarnings("unchecked")
		List<PostEntity> myposts = (List<PostEntity>) queryResult.list();
		
		return myposts;
	}
	
	public PostEntity getPostByID(long post_id) {
		Session session = null;
		PostEntity postEntity = null;
		
		session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from PostEntity where postid =:post_id");
		queryResult.setParameter("post_id", post_id);
			
		@SuppressWarnings("unchecked")
		List<PostEntity> allUsers = (List<PostEntity>) queryResult.list();
			
		if(!allUsers.isEmpty()) {
			postEntity = allUsers.get(0);
		} 		
			
		return postEntity;
	}

	public void deleteUserById(long postId) {
		Session session = null;
		
		session = sessionFactory.getCurrentSession();
		PostEntity postEntity = getPostByID(postId);
		session.delete(postEntity);
	}

	public void updatePost(PostEntity post) {
		
		Session session = sessionFactory.getCurrentSession();
		long postID = post.getPostid();
		String question = post.getQuestion();
		String description = post.getDescription();
		
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		Query updatePost = session.createQuery("update PostEntity set question=:question, last_modified=:last_modified , description=:description where postid=:postID ");
		updatePost.setParameter("question", question);
		updatePost.setParameter("description", description);
		updatePost.setParameter("postID", postID);
		updatePost.setParameter("last_modified", timestamp);
				
		updatePost.executeUpdate();
		
	}

	@Override
	public CategoryEntity getCategoryById(Long cateID) {
		
		CategoryEntity categoryEntity = null;
		Session session = sessionFactory.getCurrentSession();
			
		Query queryResult = session.createQuery("from CategoryEntity where categoryid =:cateID");
		queryResult.setParameter("cateID", cateID);
			
		@SuppressWarnings("unchecked")
		List<CategoryEntity> allcategory = (List<CategoryEntity>) queryResult.list();
		
		if(!allcategory.isEmpty()) {
			categoryEntity = allcategory.get(0);
		} 
		
		return categoryEntity;
	}

	
}
