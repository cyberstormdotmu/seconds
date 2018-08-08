package fi.korri.epooq.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import fi.korri.epooq.model.User;
import fi.korri.epooq.service.UserService;
import fi.korri.epooq.util.DateUtil;
import fi.korri.epooq.util.EpooqConstant;

@Controller
public class UserController {

	private final Logger log = Logger.getLogger(UserController.class.getName());
	
	@Autowired
	private UserService userService;

	@Autowired
	private ReloadableResourceBundleMessageSource message;
	
	
	// Methods for LOGOUT
	@RequestMapping(value = "/user/logoutForm", method = RequestMethod.GET)
	public ModelAndView logoutForm(HttpServletRequest request) {
		log.debug("User logout");
		ModelAndView model=new ModelAndView("form/logout"); 
		return model; 
	}
	
	// Methods for LOGIN MODULE
	@RequestMapping(value = "/user/loginForm", method = RequestMethod.GET)
	public ModelAndView loginForm(HttpServletRequest request) {
		log.debug("User login");
		String communityId = "-1";
		ModelAndView model=new ModelAndView("form/login","command",new User()); 
		model.addObject("selectedCommunity", communityId);
		return model; 
	}
	
	@RequestMapping(value = "/user/loginMain")
	public ModelAndView loginMain(@ModelAttribute("message") String message,@ModelAttribute("user") User user,HttpServletRequest request) {
		if(request.getSession().getAttribute("userSession")!=null){
			ModelAndView mav = new ModelAndView("redirect:/home.html");
			return mav;
		}
		String communityId = "-1";
		ModelAndView model=new ModelAndView("form/loginMain","command",user); 
		model.addObject("selectedCommunity", communityId);
		model.addObject("message",message);
		return model; 
	}
	
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,  @ModelAttribute User user) {
		User userCheck=userService.getLogin(user);
		if(userCheck!=null){
			HttpSession httpSession= request.getSession();
			httpSession.setAttribute("userSession", userCheck);
			log.info("User login successfully");
			ModelAndView mav = new ModelAndView("redirect:/home.html");
			return mav;
		}else{
			log.info("User login failed");
			ModelAndView mav = new ModelAndView("redirect:loginMain.html");
			mav.addObject("flashScope.user",user);
			mav.addObject("flashScope.message", message.getMessage("invalid.user",null,null));
			
			return mav;
		}
	}
	
	// Methods for REGISTRATION MODULE
	@RequestMapping(value = "/user/registrationForm", method = RequestMethod.GET)
	public ModelAndView registrationForm(HttpServletRequest request) {
		log.debug("User registration");
		String communityId = "-1";
		ModelAndView model=new ModelAndView("form/registration","command",new User()); 
		model.addObject("selectedCommunity", communityId);
		return model; 
	}
	
	@RequestMapping(value = "/user/registrationMain", method = RequestMethod.GET)
	public ModelAndView registrationMain(HttpServletRequest request,@ModelAttribute("message") String message,@ModelAttribute User user) {
		String communityId = "-1";
		ModelAndView model=new ModelAndView("form/registrationMain","command",user); 
		model.addObject("selectedCommunity", communityId);
		model.addObject("message",message);
		return model; 
	}
	
	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	public ModelAndView registration(HttpServletRequest request,@ModelAttribute User user,
															@RequestParam(value= "birthDay") int birthDay,
															@RequestParam(value= "birthMonth") int birthMonth,
															@RequestParam(value= "birthYear") String birthYearString) {
		int birthYear=0;
		String message="";
		ModelAndView mav=new ModelAndView("redirect:registrationMain.html");
		if(StringUtils.isNotEmpty(birthYearString)){
			try{
				birthYear=Integer.parseInt(birthYearString);
				if(birthYear<1900 || birthYear>2016){
					message=EpooqConstant.USER_REG_FAIL;	
				}
			}catch(NumberFormatException e){
				message=EpooqConstant.USER_REG_FAIL;
			}
			if(StringUtils.isNotEmpty(message)){
				mav.addObject("flashScope.user",user);
				mav.addObject("flashScope.message",message);
			}
		}
		if(StringUtils.isEmpty(message)){
			Date birthDate=DateUtil.convertStringToDate(birthDay, birthMonth, birthYear);
			user.setBirthDate(birthDate);
			message=userService.saveUser(user);
			if(message.equalsIgnoreCase(EpooqConstant.USER_REG_SUCCESS)){
				mav = new ModelAndView("redirect:/home.html");
			}else{
				mav.addObject("flashScope.user",user);
				mav.addObject("flashScope.message",message);
			}
		}
		return mav;
	}
	
	
	
	//Method for Forgot Password
	@RequestMapping(value = "/user/forgotPasswordForm", method = RequestMethod.GET)
	public ModelAndView forgotPasswordForm(HttpServletRequest request) {
		ModelAndView model=new ModelAndView("form/forgotPassword","command",new User()); 
		return model; 
	}
	
	
	@RequestMapping(value = "/user/forgotPasswordMain", method = RequestMethod.GET)
	public ModelAndView forgotPasswordMain(@ModelAttribute("successMessage") String successMessage,
											@ModelAttribute("errorMessage") String errorMessage) {
		ModelAndView model=new ModelAndView("form/forgotPasswordMain");
		model.addObject("successMessage", successMessage);
		model.addObject("errorMessage", errorMessage);
		return model;
	}
	
	@RequestMapping(value = "/user/forgotPassword", method = RequestMethod.POST)
	public ModelAndView forgotPassword(HttpServletRequest request,  @ModelAttribute User user) {
		log.debug("Method forgotPassword in  UserController");
		userService.forgotPassword(user);
		if(EpooqConstant.FORGOT_PASSWORD_STATUS){
			//redirectAttributes.addFlashAttribute("successMessage",EpooqConstant.FORGOT_PASSWORD_MESSAGE);
//			return new RedirectView("passwordChanged.html");
			/*RedirectView rv = new RedirectView("forgotPasswordMain.html");
			rv.addStaticAttribute("successMessage",EpooqConstant.FORGOT_PASSWORD_MESSAGE);
			return rv;*/
			
			ModelAndView rv = new ModelAndView("redirect:forgotPasswordMain.html");
			rv.addObject("flashScope.successMessage",EpooqConstant.FORGOT_PASSWORD_MESSAGE);
			return rv;

		}else{
			ModelAndView rv = new ModelAndView("redirect:forgotPasswordMain.html");
			rv.addObject("flashScope.errorMessage",EpooqConstant.FORGOT_PASSWORD_MESSAGE);
			return rv;
		}
	}
	
	
	@RequestMapping(value = "/user/passwordChanged")
	public ModelAndView passwordChanged(HttpServletRequest request)throws Exception {
		ModelAndView model=new ModelAndView("passwordChanged"); 
		return model;
	}
	
	
	
	
	@RequestMapping(value = "/user/logout")
	public ModelAndView logout(HttpServletRequest request)throws Exception {
		log.info("User logout successfully");
		ModelAndView mav = new ModelAndView("redirect:/home.html");
		mav.addObject("flashScope.logout","true");
		
		HttpSession session=request.getSession();
		session.invalidate();
		request.getSession(true);
		
		return mav;
	}
	
	// Terms Of Usage
	@RequestMapping(value = "/user/termsOfUsage")
	public ModelAndView termsOfUsage(HttpServletRequest request)throws Exception {
		ModelAndView model=new ModelAndView("form/termsOfUsage"); 
		return model; 
	}
	
}