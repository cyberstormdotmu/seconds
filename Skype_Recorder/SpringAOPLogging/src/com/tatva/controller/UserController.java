package com.tatva.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author Sushant - Victoy Loves Preparation
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping(value = "/showloginpage")
	public String showLoginPage(HttpServletRequest request,Model model) {
		
		return "login";
	}
	
	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	public String authenticate(HttpServletRequest request,Model model) {
		
		return "showdata";
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.GET)
	public String signUp(HttpServletRequest request,Model model) {
		
		return "signup";
	}
}
