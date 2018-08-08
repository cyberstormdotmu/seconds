package fi.korri.epooq.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import fi.korri.epooq.model.User;
import fi.korri.epooq.service.StoryService;



/**
 * Interceptor for all the incoming requests
 * @author pci94
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	private final Logger log = Logger.getLogger(LoginInterceptor.class.getName());

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2, ModelAndView arg3) throws Exception {
	}
	// prevent to access other pages except home,viewStory  
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object arg2) throws Exception {
		/*
		 * Restrict logged out user to navigate through browser's back button  
		 */
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		
		User user=(User)request.getSession().getAttribute("userSession");
		if(user==null  && !(request.getRequestURI().contains("/story/list")) && !(request.getRequestURI().contains("/story/get") && !(request.getRequestURI().contains("/home")))){
			response.sendRedirect(request.getContextPath()+"/user/loginMain.html");
			log.info("User is not loggedin");
			return false;
		}else if(user!=null && user.getId()!=0 &&  request.getRequestURI().contains("/admin")){
			response.sendRedirect(request.getContextPath()+"/home.html");
		}
		return true;
	}
}
