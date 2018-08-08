package com.tatvasoft.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tatvasoft.entity.AnswerEntity;
import com.tatvasoft.entity.PostEntity;
import com.tatvasoft.entity.UserEntity;
import com.tatvasoft.service.PostService;
import com.tatvasoft.service.ReplyService;
import com.tatvasoft.service.UserService;

/**
 *	@author TatvaSoft
 *	This is UserController class for control all requests on
 *     User functionality.
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Autowired
	private ReplyService replyService;

	/*
	 *	Different User requests are handle below
	 *		by @RequestMapping annotation.
	 */
	@RequestMapping(value = "/register")
	public String registerUser(
			@ModelAttribute("userRegister") UserEntity userRegister, Model model) {

		model.addAttribute("msg", null);
		return "registration";
	}

	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public String addUser(@ModelAttribute("userRegister") UserEntity userRegister,
			BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			System.out.println("ERROR IS HERE IN CONTROLLER...!!! ");
			return "loginRedirect";
		} else {

			if (userService.checkAvailableUser(userRegister.getEmail())) {
				model.addAttribute("error", "User Exist !");
				model.addAttribute("msg", "Email ID is already Registered..!!");
				return "registration";
			} else {
				userService.addUser(userRegister);
				return "redirect:/loginRedirect";
			}

		}
	}

	@RequestMapping(value = "/dashboard")
	public String homePageRedirect(@ModelAttribute("userRegister") UserEntity userRegister,
			Model model, HttpSession session, HttpServletRequest request) {

		int totalUsers = 0, totalPosts = 0, totalAnswers = 0, totalPostOfHr = 0,
				totalPostOfTechnical = 0, totalPostOfGeneral = 0,
					totalPostOfNetwork = 0;

		List<UserEntity> userList = userService.userList();
		List<PostEntity> postList = postService.postList();
		List<AnswerEntity> answerList = replyService.replyList();

		List<PostEntity> hrCategoryList = postService.postListByCategory(1);
		List<PostEntity> networkCategoryList = postService
				.postListByCategory(2);
		List<PostEntity> technicalCategoryList = postService
				.postListByCategory(3);
		List<PostEntity> generalCategoryList = postService
				.postListByCategory(4);

		totalPostOfHr = hrCategoryList.size();
		totalPostOfGeneral = generalCategoryList.size();
		totalPostOfNetwork = networkCategoryList.size();
		totalPostOfTechnical = technicalCategoryList.size();

		totalUsers = userList.size();
		totalPosts = postList.size();
		totalAnswers = answerList.size();

		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("totalPosts", totalPosts);
		model.addAttribute("totalAnswers", totalAnswers);
		model.addAttribute("hrposts", totalPostOfHr);
		model.addAttribute("technicalposts", totalPostOfTechnical);
		model.addAttribute("generalposts", totalPostOfGeneral);
		model.addAttribute("networkposts", totalPostOfNetwork);

		return "dashboard";
	}

	@RequestMapping(value = "/listuser")
	public String listContacts(Model model, HttpServletRequest request) {

		model.addAttribute("userlist", userService.userList());

		if (request.getParameter("formType") != null) {
			if (request.getParameter("msgUpdate") != null) {
				model.addAttribute("msgUpdate",
						request.getParameter("msgUpdate"));
			}
		}
		return "listuser";
	}

	@RequestMapping("/deleteUser")
	public String deleteUser(@RequestParam("userid") long userid, Model model,
			HttpServletRequest request) {
		userService.deleteUser(userid);
		return "redirect:/listuser";
	}

	@RequestMapping("/restriction")
	public ModelAndView accessDenied() {
		String msg = "You are not Logged in. Please Log in For update Information..!!";
		return new ModelAndView("restriction", "msg", msg);
	}

	@RequestMapping("/updateForm")
	public String updateForm(@RequestParam("userid") long userid,
			@ModelAttribute("userUpdate") UserEntity userUpdate, Model model,
			HttpServletRequest request) {

		UserEntity user1 = userService.getUserByUserId(userid);
		if (user1 != null) {
			userUpdate.setUsername(user1.getUsername());
			userUpdate.setEmail(user1.getEmail());
			userUpdate.setPassword(null);

		}
		return "updateuser";
	}

	@RequestMapping("/updateuser")
	public String updateUser(@ModelAttribute("userUpdate") UserEntity userUpdate) {

		userService.updateUser(userUpdate);
		return "redirect:/listuser";
	}

	@RequestMapping("/editprofile")
	public String editProfile(@ModelAttribute("profileUpdate") UserEntity profileUpdate,
			Model model, HttpSession session, HttpServletRequest request) {

		String emailID = request.getSession().getAttribute("emailSession")
				.toString();
		UserEntity user = new UserEntity();
		user = userService.getUserByEmail(emailID);

		profileUpdate.setUsername(user.getUsername());
		profileUpdate.setUserid(user.getUserid());
		profileUpdate.setEmail(user.getEmail());
		profileUpdate.setPassword(null);

		model.addAttribute("email", user.getEmail());

		return "profileupdate";
	}

	@RequestMapping("/updatedprofile")
	public String afterUpdateProfile(@ModelAttribute("profileUpdate") UserEntity profileUpdate) {

		System.out.println(profileUpdate.getEmail());

		userService.updateUser(profileUpdate);
		return "dashboard";
	}

	@RequestMapping(value = "/logout")
	public String logoutUser(HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.removeAttribute("emailSession");
		session.invalidate();
		return "redirect:/dashboard";
	}
}