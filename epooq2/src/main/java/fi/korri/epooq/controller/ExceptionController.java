package fi.korri.epooq.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import fi.korri.epooq.service.ReportMailService;

@ControllerAdvice
public class ExceptionController 
{
	/**
	 * Any Exceptions Handling for Whole Application
	 * @param request
	 * @param e
	 * @return Error View Page
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public String internalServerError(HttpServletRequest request,Exception e){

		ReportMailService mailService=new ReportMailService();  // Logic for implementing the mail functioanlity 
		try {
			mailService.execute(e.getMessage(),ExceptionUtils.getStackTrace(e),request.getRequestURL().toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "error";
	}
	/*
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public String badRequestError(HttpServletRequest request,Exception e){

		ReportMailService mailService=new ReportMailService();  // Logic for implementing the mail functioanlity 
		try {
			mailService.execute(e.getMessage(),ExceptionUtils.getStackTrace(e),request.getRequestURL().toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "error.html";
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.FORBIDDEN)
	public String forbiddenError(HttpServletRequest request,Exception e){

		ReportMailService mailService=new ReportMailService();  // Logic for implementing the mail functioanlity 
		try {
			mailService.execute(e.getMessage(),ExceptionUtils.getStackTrace(e),request.getRequestURL().toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "error.html";
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public String notfoundError(HttpServletRequest request,Exception e){

		ReportMailService mailService=new ReportMailService();  // Logic for implementing the mail functioanlity 
		try {
			mailService.execute(e.getMessage(),ExceptionUtils.getStackTrace(e),request.getRequestURL().toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "error.html";
	}
	*/
}
