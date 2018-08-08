package com.tatvasoft.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *	@author TatvaSoft
 *	This is Interceptor class for intercept all requests are 
 *		done in this web application. 
 *
 */
@Component("authenticationInterceptor")
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	// This is preHandle method of Interceptor.
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// Below code is for clear cache in browser in every requests.
		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		String uri = request.getRequestURI();

		// Validation on every requests are defined below.
		if (uri.contains("resources") || uri.contains("dashboard")
				|| uri.contains("postlist") || uri.contains("postdetails")
				|| uri.contains("advancesearch") || uri.contains("contactus")
				|| uri.contains("privacy-policies")
				|| uri.contains("terms-condition")
				|| uri.contains("advanceSearchRedirect")
				|| uri.contains("getSearchResult")) {
			return true;
		}

		if (uri.endsWith("register") || uri.endsWith("authenticate")
				|| uri.endsWith("adduser") || uri.endsWith("updatepassword")
				|| uri.endsWith("passupdate") || uri.endsWith("successfull")
				|| uri.endsWith("passupdateredirect")) {
			return true;
		} else if (uri.endsWith("loginRedirect")) {
			HttpSession session = request.getSession();
			if (session.getAttribute("emailSession") == null) {
				return true;
			} else {
				response.sendRedirect("dashboard");
				return false;
			}
		} else {
			HttpSession session = request.getSession();
			if (session.getAttribute("emailSession") != null) {
				return true;
			} else {
				response.sendRedirect("loginRedirect");
				return false;
			}

		}
	}

	// postHandle method of Interceptor.
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	// afterCompletion method of Interceptor.
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
