package com.tatvasoft.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatvasoft.dao.PostDao;
import com.tatvasoft.dao.UserDao;
import com.tatvasoft.entity.CategoryEntity;
import com.tatvasoft.entity.PostEntity;
import com.tatvasoft.entity.UserEntity;

/**
 *	@author TatvaSoft
 *	This interface provide PostEntity's Service methods Implementation.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	PostDao postDao;

	@Autowired
	UserDao userHibernateDao;

	/* 
	 * All implemented methods of PostService Interface.
	 */
	@Transactional
	public List<PostEntity> postList() {
		return postDao.postList();
	}

	@Transactional
	public void addPost(PostEntity addUserPost, UserEntity userEntity) {
		postDao.addPost(addUserPost, userEntity);
	}

	@Transactional
	public List<PostEntity> postListByCategory(long category_id) {
		return postDao.postListByCategory(category_id);
	}

	public PostEntity getPostByID(long post_id) {
		return postDao.getPostByID(post_id);
	}

	public List<PostEntity> getPostListByUserId(long userId) {
		return postDao.getPostListByUserId(userId);
	}

	public void deletePostById(long postId) {
		postDao.deleteUserById(postId);
	}

	public void updatePost(PostEntity post) {
		postDao.updatePost(post);
	}

	@Override
	public CategoryEntity getCategoryById(Long cateID) {
		return postDao.getCategoryById(cateID);
	}

}
