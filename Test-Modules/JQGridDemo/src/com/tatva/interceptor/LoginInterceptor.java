package com.tatva.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tatva.model.User;

public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception 
	{
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		System.out.println("Interceptor Called");
		String url=request.getRequestURI();
		HttpSession session=request.getSession();
		if(!(url.contains("index") || url.contains("signUp") || url.contains("login")))
		{
			
			System.out.println("@@@@@@@@ IF BLOCK @@@@@@@@@@@@@");
			
			User u=(User)session.getAttribute("user");
			if(u==null)
			{
				response.sendRedirect("index.html");
			}
		}
		
		if(url.contains("login")){
			User u1=(User)session.getAttribute("user");
			if(u1!=null)
			{
				response.sendRedirect("returnHome.html");
			}
		}
		return true;
	
	}
}
