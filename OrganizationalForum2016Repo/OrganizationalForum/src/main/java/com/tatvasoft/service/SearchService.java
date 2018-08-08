package com.tatvasoft.service;

import java.util.List;

import com.tatvasoft.entity.PostEntity;

/**
 *	@author TatvaSoft
 *	This interface provide Search Post's Service methods Declaration.
 */
public interface SearchService {
	
	/*
	 * This method is use for search post by criteria.
	 */
	List<PostEntity> searchPostByCriteria(String keyword , long category_id , String createDate , int sortlimit );

}
