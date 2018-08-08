package com.tatvasoft.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tatvasoft.entity.AnswerEntity;
import com.tatvasoft.service.ReplyService;
import com.tatvasoft.service.VoteService;

/**
 * @author TatvaSoft
 * This is VoteController class for control all requests on
 *     Vote functionality.
 */
@Controller
public class VoteController {

	@Autowired
	private VoteService voteService;

	@Autowired
	private ReplyService replyService;

	/*
	 *	Different Vote requests are handle below
	 * 		by @RequestMapping annotation.
	 */
	@RequestMapping("/addvote")
	public String addVote(@RequestParam("answerId") long answerId, Model model,
			HttpSession session, HttpServletRequest request) {

		AnswerEntity answerEntity = replyService.getAnswerReplyByID(answerId);
		long postid = answerEntity.getUserPost().getPostid();
		voteService.addVote(answerId);

		return "redirect:/postdetails?postId=" + postid;

	}

	@RequestMapping("/removevote")
	public String removeVote(@RequestParam("answerId") long answerId,
			Model model, HttpServletRequest request) {

		AnswerEntity answerEntity = replyService.getAnswerReplyByID(answerId);
		long postid = answerEntity.getUserPost().getPostid();
		long userid = answerEntity.getUserAnswer().getUserid();
		voteService.removeVote(answerId, userid);

		return "redirect:/postdetails?postId=" + postid;
	}

}
