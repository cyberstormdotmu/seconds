package com.tatva.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tatva.domain.UserMaster;
import com.tatva.utils.MPAContext;


/**
 * Interceptor for all the incoming requests
 * @author pci94
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

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

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception 
	{
		
		/*
		 * Restrict logged out user to navigate through browser's back button  
		 */
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		
		/*
		 * Get the requested URI
		 */
		String url=request.getRequestURI();
		HttpSession session=request.getSession(true);
		
		
		/*
		 * check whether the user is logged in or not..
		 * Allow Counter user to navigate some pages...
		 * if try to access secure admin page then redirect to the home page 
		 */
		if(session.getAttribute(MPAContext.currentUser)!=null){
			
			if(MPAContext.currentRole.equalsIgnoreCase("counter")){
				
				if(!(url.contains("checkinForm") || url.contains("checkAppointment") || url.contains("Report") || url.contains("changePassword") 
						|| url.contains("logout") || url.contains("updateUserForm") || url.contains("homePage"))){
					
					response.sendRedirect(request.getContextPath()+"/login/homePage.mpa");
				}
				else if(url.contains("updateUserForm") || url.contains("updateUserProfile") )
				{
					if(url.contains("updateUserForm") || url.contains("updateUserProfile"))
					{
						String userId = MPAContext.flagUpdateUser;
						
						
						if(!(userId.equalsIgnoreCase(MPAContext.currentUser))){
							response.sendRedirect(request.getContextPath()+"/login/homePage.mpa");
							return false;							
						}
					}
					else
					{
						response.sendRedirect(request.getContextPath()+"/login/homePage.mpa");
					}
				}
			}
			
			else if(MPAContext.currentRole.equalsIgnoreCase("administrator")){
				
				if(url.contains("updateUserForm"))
				{	
					String userId = MPAContext.flagUpdateUser;
					String role = MPAContext.flagUpdateRole;
					if(role.equalsIgnoreCase("administrator"))
					{
						if(!(userId.equalsIgnoreCase(MPAContext.currentUser))){
							response.sendRedirect(request.getContextPath()+"/login/homePage.mpa");
							return false;							
						}
					}
				}
				else if(url.contains("updateUserProfile")){
					String userId = MPAContext.flagUpdateUser;
					if(!(userId.equalsIgnoreCase(MPAContext.currentUser))){
						response.sendRedirect(request.getContextPath()+"/login/homePage.mpa");
						return false;							
					}
					
				}
				
				
				}
			}
		
		/*
		 * Allow Guest user to navigate some pages...
		 * if try to access secure page then redirect to the home page 
		 */
		if(!(url.contains("index") || url.contains("appointmentForm") || url.contains("homePage") || url.contains("availableTimes") || 
				url.contains("checkAppointment") || url.contains("confirmationForm") || url.contains("getLogin") ||
				url.contains("cancelAppointment") || url.contains("rescheduleAppointment") || url.contains("appointmentReportReceipt") || 
				url.contains("loginForm")))
		{
			
			UserMaster u=(UserMaster)session.getAttribute(MPAContext.currentUser);
			if(u==null)
			{
				response.sendRedirect(request.getContextPath()+"/login/homePage.mpa");
			}
		}
		
		/*
		 * Already logged in then should not be able to visit login page
		 */
		if(url.contains("loginForm")){
			UserMaster u1=(UserMaster)session.getAttribute(MPAContext.currentUser);
			if(u1!=null)
			{
				response.sendRedirect(request.getContextPath()+"/login/homePage.mpa");
			}
		}
		
		/*
		 * Already logged out then should not be able to request logout
		 */
		if(url.contains("logout")){
			UserMaster u1=(UserMaster)session.getAttribute(MPAContext.currentUser);
			if(u1==null){
				response.sendRedirect(request.getContextPath()+"/login/homePage.mpa");
			}
			return true;
		}
		return true;
	}
}