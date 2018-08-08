package com.tatvasoft.dao;

import java.util.List;

import com.tatvasoft.entity.PostEntity;

/**
 *	@author TatvaSoft
 *	This interface provide Post search Repository methods
 *  	Declaration.
 */
public interface SearchDao {

	/*
	 * This method is use for search post by criteria.
	 */
	List<PostEntity> searchPostByCriteria(String keyword , long category_id , String createDate , int sortlimit );

}
