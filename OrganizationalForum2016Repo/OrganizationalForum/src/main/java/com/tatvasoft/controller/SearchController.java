package com.tatvasoft.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tatvasoft.entity.PostEntity;
import com.tatvasoft.service.PostService;
import com.tatvasoft.service.ReplyService;
import com.tatvasoft.service.SearchService;
import com.tatvasoft.service.UserService;

/**
 *	@author TatvaSoft
 *	This is SearchController class for control all requests on
 *     Search functionality.
 */
@Controller
public class SearchController {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private SearchService searchService;

	/*
	 *	Different Search post requests are handle below
	 *		by @RequestMapping annotation.
	 */
	@RequestMapping(value = "/advanceSearchRedirect")
	public String advanceSearch() {
		return "advancesearch";
	}

	@RequestMapping(value = "/getSearchResult", method = RequestMethod.POST)
	public String getSearchResultViaAjax(
			@RequestParam("keyword") String keyword,
			@RequestParam("category_id") long category_id,
			@RequestParam("createDate") String createDate,
			@RequestParam("sortlimit") int sortlimit,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		System.out.println("controller Called..");

		List<PostEntity> resultPosts = searchService.searchPostByCriteria(
				keyword, category_id, createDate, sortlimit);

		model.addAttribute("Hello", keyword);
		model.addAttribute("resultlist", resultPosts);
		return "advancesearch";
	}

}
