package com.tatvasoft.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tatvasoft.entity.AnswerEntity;
import com.tatvasoft.entity.PostEntity;
import com.tatvasoft.entity.UserEntity;
import com.tatvasoft.service.PostService;
import com.tatvasoft.service.ReplyService;
import com.tatvasoft.service.UserService;

/**
 *	@author TatvaSoft
 *	This is ReplyController class for control all requests on
 *     Answer functionality.
 */
@Controller
public class ReplyController {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReplyService replyService;

	/*
	 *	Different Answer requests are handle below
	 *		by @RequestMapping annotation.
	 */
	@RequestMapping("/adduseranswer")
	public String addAnswer(@ModelAttribute("addReply") AnswerEntity addReply,
			BindingResult result, Model model, HttpSession session,
			HttpServletRequest request) {
		if (result.hasErrors()) {
			System.out.println("ERROR IS HERE IN CONTROLLER...!!! ");
			return "test";
		} else {

			long postid = addReply.getUserPost().getPostid();

			PostEntity postEntity = postService.getPostByID(postid);
			postEntity.getCategory().getCategoryid();

			UserEntity userEntity = (UserEntity) request.getSession()
					.getAttribute("currentLoggedinUser");

			addReply.setUserPost(postEntity);

			replyService.addReply(addReply, userEntity);

			return "redirect:/postdetails?postId=" + postid;

		}
	}

	@RequestMapping("/deleteReply")
	public String deleteReply(@RequestParam("answerId") String answerId,
			@RequestParam("postId") String postId, Model model,
			HttpServletRequest request) {

		boolean postIdIsOnlyDigit = true;
		boolean answerIdIsOnlyDigit = true;

		long actualAnswerId = Long.parseLong(answerId);
		long actualPostId = Long.parseLong(postId);

		for (int i = 0; i < answerId.length(); i++) {
			if (!Character.isDigit(answerId.charAt(i)))
				answerIdIsOnlyDigit = false;
		}

		for (int i = 0; i < postId.length(); i++) {
			if (!Character.isDigit(postId.charAt(i)))
				postIdIsOnlyDigit = false;
		}

		if (answerIdIsOnlyDigit || postIdIsOnlyDigit) {

			replyService.deleteReplyById(actualAnswerId);

		} else {
			model.addAttribute("errormsg", "No such Reply Found");
		}

		return "redirect:/postdetails?postId=" + actualPostId;
	}

	@RequestMapping("/updateReplyRedirect")
	public String updateForm(@RequestParam("answerId") String answerId,
			@RequestParam("postId") String postId,
			@ModelAttribute("replyUpdate") AnswerEntity replyUpdate,
			Model model, HttpServletRequest request) {

		boolean postIdIsOnlyDigit = true;
		boolean answerIdIsOnlyDigit = true;

		long actualAnswerId = Long.parseLong(answerId);
		long actualPostId = Long.parseLong(postId);

		for (int i = 0; i < answerId.length(); i++) {
			if (!Character.isDigit(answerId.charAt(i)))
				answerIdIsOnlyDigit = false;
		}

		for (int i = 0; i < postId.length(); i++) {
			if (!Character.isDigit(postId.charAt(i)))
				postIdIsOnlyDigit = false;
		}

		if (answerIdIsOnlyDigit || postIdIsOnlyDigit) {

			PostEntity postEntity = new PostEntity();
			postEntity = postService.getPostByID(actualPostId);

			model.addAttribute("postDetails", postEntity);

			AnswerEntity answerEntity = replyService
					.getAnswerReplyByID(actualAnswerId);

			if (answerEntity != null) {

				replyUpdate.setAnswerid(answerEntity.getAnswerid());
				replyUpdate.setDescription(answerEntity.getDescription());
			}
		}
		return "updatereply";

	}

	@RequestMapping("/updatereplysucessfull")
	public String updateUser(
			@ModelAttribute("replyUpdate") AnswerEntity replyUpdate) {

		long postId = replyUpdate.getUserPost().getPostid();
		replyService.updateReply(replyUpdate);

		return "redirect:/postdetails?postId=" + postId;
	}

}
