package com.tatvasoft.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tatvasoft.entity.UserEntity;
import com.tatvasoft.service.UserService;

/**
 *	@author TatvaSoft
 *	This is HomeController class for control all requests on
 *     Home page functionality.
 */
@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	/*
	 *	Different home page requests are handle below
	 *		by @RequestMapping annotation.
	 */
	@RequestMapping("/loginRedirect")
	public String loginUser(HttpServletRequest request, Model model) {
		//HttpSession session = request.getSession();
		String referer = request.getHeader("referer");
		
		if (referer.contains("register") || referer.contains("getSearchResult")) {
			model.addAttribute("referer", "/dashboard");
		} else {
			model.addAttribute("referer", referer);
		}

		model.addAttribute("userLogin", new UserEntity());
		model.addAttribute("msg", null);
		return "login";

	}

	@RequestMapping("/authenticate")
	public String authenticateUser(@ModelAttribute("userLogin") UserEntity user,
			Model model,BindingResult result, HttpServletRequest request) {

		String referer = request.getParameter("referer");

		if (userService.isValidUser(user.getEmail(), user.getPassword())) {

			UserEntity currentLoggedinUser = userService.getUserByEmail(user
					.getEmail());

			HttpSession session = request.getSession();
			session.setAttribute("emailSession", user.getEmail());
			session.setAttribute("currentLoggedinUser", currentLoggedinUser);

			if (referer == null) {
				return "redirect:/dashboard";
			} else {
				return "redirect:" + referer;
			}
		} else {
			model.addAttribute("error", "Access Denied !");
			model.addAttribute("msg", "Invalid Username or Password");
			model.addAttribute("referer", referer);
			return "login";
		}

	}

	@RequestMapping(value = "/updatepassword")
	public String updatePassword(@ModelAttribute("userPassUpdate") UserEntity userPassUpdate) {
		return "updatepassword";
	}

	@RequestMapping(value = "/passupdate")
	public String userPassRecovery(@ModelAttribute("userPassUpdate") UserEntity userPassUpdate,
			Model model, BindingResult result, HttpServletRequest request) {

		if (userService.sendPasswordRecovery(userPassUpdate.getEmail())) {
			return "successfull";
		} else {
			return "redirect:/passupdateredirect";
		}

	}

	@RequestMapping(value = "/passupdateredirect")
	public String updateRedirect(@ModelAttribute("userLogin") UserEntity userLogin,
			Model model,BindingResult result, HttpServletRequest request) {

		model.addAttribute("error", "Opps !");
		model.addAttribute("msg", "No such Email ID is registered !");

		return "login";
	}

	@RequestMapping(value = "/successfull")
	public String successfullPage(@ModelAttribute("userPassUpdate") UserEntity userPassUpdate,
			Model model) {
		model.addAttribute("userLogin", new UserEntity());
		model.addAttribute("msg", null);
		return "login";
	}

	@RequestMapping(value = "/privacy-policies")
	public String privacyPage() {
		return "privacypolicies";
	}

	@RequestMapping(value = "/terms-condition")
	public String termsPage() {
		return "termsandcondition";
	}

	@RequestMapping(value = "/contactus")
	public String contactPage() {
		return "contactus";
	}

}
