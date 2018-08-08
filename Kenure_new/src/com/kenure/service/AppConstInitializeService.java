package com.kenure.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.kenure.constants.ApplicationConstants;

@Service
public class AppConstInitializeService {

	public void initializeAppConstObjService(HttpServletRequest request){
		ApplicationConstants app = ApplicationConstants.getAppConstObject();
		app.setProjectPath(request.getContextPath());
		app.setFullPath(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
	}
	
}
