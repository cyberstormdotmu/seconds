package fi.korri.epooq.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fi.korri.epooq.model.User;
import fi.korri.epooq.service.UserService;
import fi.korri.epooq.util.DateUtil;
import fi.korri.epooq.util.EpooqConstant;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReloadableResourceBundleMessageSource message;
	
	@Autowired
	private Environment env;
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	// Methods for LOGOUT
	
	@RequestMapping(value = "/user/logoutForm", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView model=new ModelAndView("form/logout"); 
		return model; 
	}
	
	// Methods for LOGIN MODULE
	
	@RequestMapping(value = "/user/loginForm", method = RequestMethod.GET)
	public ModelAndView loginForm(HttpServletRequest request) {
		String communityId = "-1";
		ModelAndView model=new ModelAndView("form/login","command",new User()); 
		model.addObject("selectedCommunity", communityId);
		return model; 
	}
	
	@RequestMapping(value = "/user/loginMain")
	public ModelAndView loginMain(@ModelAttribute("message") String message,@ModelAttribute("user") User user) {
		String communityId = "-1";
		ModelAndView model=new ModelAndView("form/loginMain","command",user); 
		model.addObject("selectedCommunity", communityId);
		model.addObject("message",message);
		return model; 
	}
	
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request,  @ModelAttribute User user,final RedirectAttributes redirectAttributes) {
		User userCheck=userService.getLogin(user);
		if(userCheck!=null){
			HttpSession httpSession= request.getSession();
			httpSession.setAttribute("userSession", userCheck);
			return "redirect:/home.html";
		}else{
			redirectAttributes.addFlashAttribute("user", user);
			redirectAttributes.addFlashAttribute("message", message.getMessage("invalid.user",null,null));
			return "redirect:loginMain.html";
		}
	}
	
	// Methods for REGISTRATION MODULE
	
	@RequestMapping(value = "/user/registrationForm", method = RequestMethod.GET)
	public ModelAndView registrationForm(HttpServletRequest request) {
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
	public String registration(HttpServletRequest request,@ModelAttribute User user,
															@RequestParam int birthDay,
															@RequestParam int birthMonth,
															@RequestParam int birthYear,final RedirectAttributes redirectAttributes) {
		Date birthDate=DateUtil.convertStringToDate(birthDay, birthMonth, birthYear);
		user.setBirthDate(birthDate);
		String message=userService.saveUser(user);
		if(message.equalsIgnoreCase(EpooqConstant.USER_REG_SUCCESS)){
			return "redirect:/home.html";
		}else{
			redirectAttributes.addFlashAttribute("message",message);
			redirectAttributes.addFlashAttribute("user",user);
			return "redirect:registrationMain.html";
		}
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
	public String forgotPassword(HttpServletRequest request,  @ModelAttribute User user,RedirectAttributes redirectAttributes) {
		log.debug("Method forgotPassword in  UserController");
		userService.forgotPassword(user);
		if(EpooqConstant.FORGOT_PASSWORD_STATUS){
			//redirectAttributes.addFlashAttribute("successMessage",EpooqConstant.FORGOT_PASSWORD_MESSAGE);
			return "redirect:loginMain.html";
		}else{
			redirectAttributes.addFlashAttribute("errorMessage",EpooqConstant.FORGOT_PASSWORD_MESSAGE);
			return "redirect:forgotPasswordMain.html";
		}
	}
	
	@RequestMapping(value = "/user/logout")
	public String logout(HttpServletRequest request,RedirectAttributes redirectAttributes)throws Exception {
		redirectAttributes.addFlashAttribute("logout","true");
		HttpSession session=request.getSession();
		session.invalidate();
		return "redirect:/home.html";
	}
	
	// Terms Of Usage
	@RequestMapping(value = "/user/termsOfUsage")
	public ModelAndView termsOfUsage(HttpServletRequest request,RedirectAttributes redirectAttributes)throws Exception {
		ModelAndView model=new ModelAndView("form/termsOfUsage"); 
		return model; 
	}
	
}