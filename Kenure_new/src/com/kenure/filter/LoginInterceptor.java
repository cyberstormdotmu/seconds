package com.kenure.filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kenure.entity.User;
import com.kenure.utils.LoggerUtils;


public class LoginInterceptor extends HandlerInterceptorAdapter  {

	private Logger log = LoggerUtils.getInstance(LoginInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object arg2) throws Exception {


		// Disable browser back button
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		String url = ((HttpServletRequest)request).getRequestURL().toString();

		String queryString = ((HttpServletRequest) request).getQueryString();
		HttpServletRequest req = (HttpServletRequest) request; 

		HttpSession session = req.getSession();

		if((url.contains(".css") || url.contains("login") || url.contains("images") || 
				url.contains(".js") || url.contains(".png") || url.contains(".woff") 
				|| url.contains(".ttf") || url.contains(".woff2") || url.contains(".jpg")
				|| url.contains(".ico") || url.contains(".txt") || url.contains(".bmp")
				|| url.contains(".txt") || url.contains(".jpeg")) && !url.contains("loginForm")) 
		{
			return true ;
		}

		if(queryString != null && (queryString.contains("%3Chtml%3E") || queryString.contains("%3C%2Fhtml%3E") || 
				queryString.contains("%3Cscript%3E") ||queryString.contains("%3C%2Fscript%3E")) ){
			log.warn("Malicious Parameters Detected");
			response.sendRedirect("XSSError");
		}



		if(session.getAttribute("currentUser")!=null){		
			User user = (User) session.getAttribute("currentUser");
			
			if(url.contains("customerOperation") && !user.getRole().getRoleName().equals("customer")){
				url += "/loginForm";
			}
			
			if(url.contains("loginForm")){
				if(user.getRole().getRoleName().equalsIgnoreCase("admin")){
					response.sendRedirect(request.getContextPath()+"/"+"adminDashboard");
				}else if(user.getRole().getRoleName().equalsIgnoreCase("customer")){
					response.sendRedirect("customerOperation/customerDashboard");
				}else if(user.getRole().getRoleName().equalsIgnoreCase("consumer")){
					response.sendRedirect("consumerDashboard");
				}else if(user.getRole().getRoleName().equalsIgnoreCase("installer")){
					response.sendRedirect("installerDashboard");
				}else if(user.getRole().getRoleName().equalsIgnoreCase("normalcustomer")){
					response.sendRedirect("customerDashboard");
				}
			}


			
			return true;
		}else{
			if(url.contains("authenticateUser") || (url.contains("login")) || url.contains("forgetPasswordForm") || 
					url.contains("sendMail") || url.contains("consumerRegistration") || url.contains("initCustomerCodeData") ||
					url.contains("validateConsumerRegistration") || url.contains("consumerDetailsRegistration") ||
					url.contains("initConsumerRegiPage") || url.contains("registerNewConsumer")){
				return true;
			}/*else if(url.contains("%3Chtml%3E") || url.contains("%3C%2Fhtml%3E") || 
					url.contains("%3Cscript%3E") || url.contains("%3C%2Fscript%3E")){

				response.sendRedirect(request.getContextPath() + "/error.jsp");
				System.out.println(" >>>>>>>>>>>>>>>>>>>>>" + request.getContextPath() + "/error.jsp" + " >>>>>>>>>>>>>>>>>>>>>");
				return false;

			}*/
			else{
				response.sendRedirect(request.getContextPath());
				return false;
			}
		}

	}

}