package com.kenure.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

@Controller
public class ErrorController {

	@RequestMapping(value="/XSSPageError")
	public ResponseEntity<String> XSSErrorHandler(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		
		if((session.getAttribute("currentUser") != null)){
			session.invalidate(); // Session destroy for current user
		}
		return new ResponseEntity<String>(new Gson().toJson("Malicious Parameters Detected"), HttpStatus.OK);
	}
}
