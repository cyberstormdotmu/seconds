package com.tatvasoft.dao;

import java.util.List;

import com.tatvasoft.entity.CategoryEntity;
import com.tatvasoft.entity.PostEntity;
import com.tatvasoft.entity.UserEntity;

/**
 *	@author TatvaSoft
 *	This interface provide PostEntity's Repository methods
 *  	Declaration.
 */
public interface PostDao {
	
	/*
	 * This method returns List of all posts.
	 */
	public List<PostEntity> postList();

	/*
	 * This method add new post. 
	 */
	public void addPost(PostEntity addUserPost, UserEntity userEntity);

	/*
	 * This method is use for find post by it's categoryId. 
	 */
	List<PostEntity> postListByCategory(long category_id);

	/*
	 * This method is use for find post by it's postId. 
	 */
	public PostEntity getPostByID(long post_id);
	
	/*
	 * This method is use for find post by it's userId. 
	 */
	public List<PostEntity> getPostListByUserId(long userId);

	/*
	 * This method is use for delete post by it's postId.
	 */
	public void deleteUserById(long postId);

	/*
	 * This method is use for update post. 
	 */
	public void updatePost(PostEntity post);

	/*
	 * This method is use for find category by categoryId. 
	 */
	public CategoryEntity getCategoryById(Long cateID);

}
