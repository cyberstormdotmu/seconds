package com.kenure.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.kenure.entity.User;
import com.kenure.service.INormalCustomerService;
import com.kenure.service.IUserService;
import com.kenure.utils.MD5Encoder;

@Controller
public class SuperUserController {

	@Autowired
	private IUserService service;
	
	@Autowired
	private INormalCustomerService normalService;
	
	@RequestMapping(value="/superUserRequire",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> checkFouSuperUser(HttpServletRequest request,HttpServletResponse response){
		
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		
		if(isNormalCustomer !=null && isNormalCustomer){
			return new ResponseEntity<>(new Gson().toJson("true"),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson("false"),HttpStatus.OK);
	}
	
	@RequestMapping(value="/authenticateSuperCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> authenticateSuperCustomer(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="username") String username,
			@RequestParam(value="password") char[] password){
		
		String passwordObj = String.valueOf(password);
		
		if(username != null && username.trim() == ""){
			//return new ResponseEntity<>(new Gson().toJson("usernamecannotbenull"),HttpStatus.OK);
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
		if(passwordObj != null && passwordObj.trim() == ""){
			//return new ResponseEntity<>(new Gson().toJson("passwordcannotbenull"),HttpStatus.OK);
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
		
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		User loggedUser = null;
		if(isNormalCustomer !=null && isNormalCustomer){
			loggedUser = (User) request.getSession().getAttribute("normalCustomer");
			
			String registeredUsername = loggedUser.getNormalCustomer().getSuperCustomer().getUser().getUserName();
			String registeredPassword = loggedUser.getNormalCustomer().getSuperCustomer().getUser().getPassword();
			
			String encodedPassword = MD5Encoder.MD5Encryptor(passwordObj); 
			
			boolean isUsernameValid = username.equalsIgnoreCase(registeredUsername);
			boolean isPasswordValid = encodedPassword.equalsIgnoreCase(registeredPassword);
			
			if(isUsernameValid && isPasswordValid)
				return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);
			else
				return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
	}
	
}
