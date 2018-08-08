package com.tatva.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tatva.domain.UserMaster;
import com.tatva.service.IUser;
import com.tatva.utils.MPAContext;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private IUser user;
	
	/**
	 * Navigating to login form
	 * @return login View Page
	 * @throws Exception
	 */
	@RequestMapping("/loginForm.mpa")
	public ModelAndView createLoginForm()throws Exception {
		ModelAndView mav = new ModelAndView("/page.login");
		return mav;
	}
	
	
	/**
	 * Validating the UserName & Password
	 * @param request
	 * @param model
	 * @return Home/Login View Page According to Authentication
	 * @throws Exception
	 */
	@RequestMapping("/getLogin.mpa")
	public String getLogin(HttpServletRequest request,Model model)throws Exception {
		String userId=request.getParameter("userId");
		String password=request.getParameter("password");
		
		if(userId!=null && password!=null && !"".equals(userId.trim()) && !"".equals(password.trim())){
			UserMaster userMaster= user.getLogin(userId, password);
			if(userMaster != null){
				HttpSession session=request.getSession();
				MPAContext.currentUser=userMaster.getUserId();
				String UserNameTemp=userMaster.getFirstName()+" "+userMaster.getLastName();
				MPAContext.currentRole=userMaster.getRole();
				session.setAttribute(MPAContext.currentUser, userMaster);
				session.setAttribute("UserNameTemp", UserNameTemp);
				return "redirect:/login/homePage.mpa";
			}else{
				model.addAttribute("loginError","Invalid UserId and password");
				return "/page.login";
			}
		}else{
			model.addAttribute("loginError","Invalid UserId and password");
			return "/page.login";
		}
	}
	
	/**
	 * Navigating to home page
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping("/homePage.mpa")
	public String returnHome(HttpServletRequest request)throws Exception {
		
		if(request.getParameter("temp")!=null){
			String temp=request.getParameter("temp");
			request.setAttribute("temp", temp);
		}
		return "/page.homePage";
	}
	
	/**
	 * Log out functionality
	 * @param request
	 * @return Home page View
	 * @throws Exception
	 */
	@RequestMapping("/logout.mpa")
	public String logout(HttpServletRequest request)throws Exception {
		HttpSession session=request.getSession();
		session.invalidate();
		MPAContext.currentUser=null;
		MPAContext.currentRole=null;
		return "redirect:/login/homePage.mpa?temp=temp";
	}
	
	@RequestMapping(value="/forgotPassword.mpa",method=RequestMethod.POST)
	public String forgotPassword(HttpServletRequest request) throws Exception{
		String userId=request.getParameter("userId");
		user.forgotPassword(userId);
		return "/page.homePage";
	}
	
	@RequestMapping(value="/changePasswordForm.mpa",method=RequestMethod.GET)
	public String changePasswrod(){
		return "/page.changePassword";
	}
	
	@RequestMapping(value="/changePasswordForm.mpa",method=RequestMethod.POST)
	public String changePassword(HttpServletRequest request,Model model){
		
		String oldPassword=request.getParameter("oldPassword");
		String newPassword=request.getParameter("newPassword");
		
		String message = user.changePassword(oldPassword,newPassword);
		model.addAttribute("message", message);
		return "/page.changePassword";
		
	}
	
}