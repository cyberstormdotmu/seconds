package com.tatvasoft.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.tatvasoft.entity.CategoryEntity;
import com.tatvasoft.entity.PostEntity;
import com.tatvasoft.entity.UserEntity;
import com.tatvasoft.entity.VoteEntity;
import com.tatvasoft.service.PostService;
import com.tatvasoft.service.ReplyService;
import com.tatvasoft.service.UserService;
import com.tatvasoft.service.VoteService;

/**
 *	@author TatvaSoft
 *	This is PostController class for control all requests on
 *     Post functionality.
 */
@Controller
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private VoteService voteService;

	/*
	 *	Different Post requests are handle below
	 *		by @RequestMapping annotation.
	 */
	@RequestMapping("/adduserquestion")
	public String addQuestion(@ModelAttribute("addUserPost") PostEntity addUserPost,
			BindingResult result, Model model, HttpSession session,
			HttpServletRequest request) {
		
		if (result.hasErrors()) {
			System.out.println("ERROR IS HERE IN CONTROLLER...!!! ");
			return "post";
		} else {
			long cateID = addUserPost.getCategory().getCategoryid();
			String emailID = request.getSession().getAttribute("emailSession")
					.toString();
			UserEntity userEntity = new UserEntity();
			userEntity = userService.getUserByEmail(emailID);
			postService.addPost(addUserPost, userEntity);
			return "redirect:/postlist?categoryId=" + cateID;

		}
	}

	@RequestMapping("/postlist")
	public String postListRedirect(@RequestParam("categoryId") long category_id,
			Model model,HttpServletRequest request) {

		List<PostEntity> quelist = new ArrayList<PostEntity>();
		quelist = postService.postListByCategory(category_id);
		model.addAttribute("quelist", quelist);
		model.addAttribute("categoryname", quelist.get(0).getCategory()
				.getCategoryName());
		model.addAttribute("categoryId", quelist.get(0).getCategory()
				.getCategoryid());

		return "questionlist";
	}

	@RequestMapping("/selectedCategory")
	public String selectCategory(@ModelAttribute("addUserPost") PostEntity addUserPost,
			Model model,HttpServletRequest request) {
		
		String category = request.getParameter("categoryId");
		Long cateID = Long.parseLong(category);
		CategoryEntity categoryEntity = postService.getCategoryById(cateID);
		model.addAttribute("category", categoryEntity);
		model.addAttribute("addUserPost", addUserPost);
		model.addAttribute("categoryID", cateID);
		return "post";
	}

	@RequestMapping("/mypostlist")
	public String myPostList(@RequestParam("userId") long userId, Model model,
			HttpServletRequest request) {

		model.addAttribute("myquelist", postService.getPostListByUserId(userId));
		return "mypost";
	}

	@RequestMapping("/allpostlist")
	public String allPostList(Model model, HttpServletRequest request) {

		model.addAttribute("allquelist", postService.postList());
		return "allpostlist";
	}

	@RequestMapping("/postdetails")
	public String postDetails(@RequestParam("postId") String post_id,
			@ModelAttribute("addReply") AnswerEntity answerEntity, Model model,
			HttpServletRequest request) {

		String referer = request.getHeader("referer");
		boolean isOnlyDigit = true;

		for (int i = 0; i < post_id.length(); i++) {
			if (!Character.isDigit(post_id.charAt(i)))
				isOnlyDigit = false;
		}

		if (isOnlyDigit) {
			long actualPostId = Long.parseLong(post_id);
			PostEntity postDetails = postService.getPostByID(actualPostId);
			List<AnswerEntity> replylist = new ArrayList<AnswerEntity>();
			replylist = replyService.getReplyByPostID(actualPostId);

			List<VoteEntity> voteEntity = voteService.voteList();

			if (postDetails != null) {
				
				if (referer.contains("getSearchResult")) {
					model.addAttribute("noback", "noback");
				}
				
				model.addAttribute("votelist", voteEntity);
				model.addAttribute("replylist", replylist);
				model.addAttribute("postDetails", postDetails);
			} else {
				model.addAttribute("errormsg", "No such Post Found");
			}
		} else {
			model.addAttribute("errormsg", "No such Post Found");
		}
		return "postdetails";
	}

	@RequestMapping("/deletePost")
	public String deleteUser(@RequestParam("postId") String postId,
			@ModelAttribute("postDetails") PostEntity postDetails, Model model,
			HttpServletRequest request) {

		boolean isOnlyDigit = true;
		long actualPostId = Long.parseLong(postId);
		long userID = 0;

		for (int i = 0; i < postId.length(); i++) {
			if (!Character.isDigit(postId.charAt(i)))
				isOnlyDigit = false;
		}

		if (isOnlyDigit) {

			postDetails = postService.getPostByID(actualPostId);

			if (postDetails != null) {
				userID = postDetails.getUserPost().getUserid();
			} else {
				model.addAttribute("errormsg", "No such Post Found");
			}
		} else {
			model.addAttribute("errormsg", "No such Post Found");
		}

		postService.deletePostById(actualPostId);

		return "redirect:/mypostlist?userId=" + userID;
	}

	@RequestMapping("/deletePostbyAdmin")
	public String deletePostByAdmin(@RequestParam("postId") String postId,
			@ModelAttribute("allPostDetails") PostEntity allPostDetails,
			Model model, HttpServletRequest request) {

		boolean isOnlyDigit = true;
		long actualPostId = Long.parseLong(postId);

		for (int i = 0; i < postId.length(); i++) {
			if (!Character.isDigit(postId.charAt(i)))
				isOnlyDigit = false;
		}

		if (isOnlyDigit) {

			allPostDetails = postService.getPostByID(actualPostId);

			if (allPostDetails == null) {
				model.addAttribute("errormsg", "No such Post Found");
			}

		} else {
			model.addAttribute("errormsg", "No such Post Found");
		}

		postService.deletePostById(actualPostId);

		return "redirect:/allpostlist";
	}

	@RequestMapping("/updatePostRedirect")
	public String updateForm(@RequestParam("postId") long postId,
			@ModelAttribute("postUpdate") PostEntity postUpdate, Model model,
			HttpServletRequest request) {

		PostEntity postEntity = postService.getPostByID(postId);

		if (postEntity != null) {

			long userid = postEntity.getUserPost().getUserid();

			UserEntity userEntity = new UserEntity();
			userEntity.setUserid(userid);

			postUpdate.setPostid(postEntity.getPostid());
			postUpdate.setUserPost(userEntity);
			postUpdate.setQuestion(postEntity.getQuestion());
			postUpdate.setDescription(postEntity.getDescription());
		}

		return "updatepost";

	}

	@RequestMapping("/updatepostsucessfull")
	public String updateUser(@ModelAttribute("postUpdate") PostEntity postUpdate) {

		long userId = postUpdate.getUserPost().getUserid();
		postService.updatePost(postUpdate);

		return "redirect:/mypostlist?userId=" + userId;
	}

}
