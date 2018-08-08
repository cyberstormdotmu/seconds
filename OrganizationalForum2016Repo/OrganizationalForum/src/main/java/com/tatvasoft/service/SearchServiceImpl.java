package com.tatvasoft.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatvasoft.dao.PostDao;
import com.tatvasoft.dao.ReplyDao;
import com.tatvasoft.dao.SearchDao;
import com.tatvasoft.dao.UserDao;
import com.tatvasoft.entity.PostEntity;

/**
 *	@author TatvaSoft
 *	This interface provide Post search Service methods Implementation.
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {

	@Autowired
	PostDao postDao;

	@Autowired
	UserDao userHibernateDao;

	@Autowired
	ReplyDao replyDao;

	@Autowired
	SearchDao searchdao;

	/*
	 * All implemented methods of SearchService Interface
	 */
	public List<PostEntity> searchPostByCriteria(String keyword,
			long category_id, String createDate, int sortlimit) {
		return searchdao.searchPostByCriteria(keyword, category_id, createDate,
				sortlimit);
	}

}
