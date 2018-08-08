package com.tatva.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tatva.utils.ReportMailService;

@ControllerAdvice
public class ExceptionController extends RuntimeException{

	

	
	private static final long serialVersionUID = 1L;

	/**
	 * Any Exceptions Handling for Whole Application
	 * @param request
	 * @param e
	 * @return Error View Page
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public String error(HttpServletRequest request,Exception e){

		ReportMailService mailService=new ReportMailService();  // Logic for implementing the mail functioanlity 
		try {
			mailService.execute(e.getMessage(),ExceptionUtils.getStackTrace(e),request.getRequestURL().toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "/page.error";
	}
	
}