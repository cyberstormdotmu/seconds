package com.ishoal.ws.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.Users;
import com.ishoal.core.orders.RegisterUserConfirmationService;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/admin/registration")
public class AdminRegisterUserConfirmController {
	private static final Logger logger = LoggerFactory.getLogger(AdminRegisterUserConfirmController.class);

    private final RegisterUserConfirmationService registerUserService;

    public AdminRegisterUserConfirmController(RegisterUserConfirmationService registerUserService) {
        this.registerUserService = registerUserService;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "confirm")
    public ResponseEntity<?> confirmRegister() {
        logger.info("Confirm register user");

        PayloadResult<Users> result = this.registerUserService.confirm("CONFIRM");
        ResponseEntity<?> response;
        if(result.isSuccess()) {
             response = ResponseEntity.ok(result.getPayload());
        } else {
        	response = ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
        return response;
    }
    @RequestMapping(method = RequestMethod.GET, value = "reject")
    public ResponseEntity<?> cancelRegister() {
        logger.info("reject register user");

        PayloadResult<Users> result = this.registerUserService.confirm("REJECT");
        ResponseEntity<?> response;
        if(result.isSuccess()) {
             response = ResponseEntity.ok(result.getPayload());
        } else {
        	response = ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
        return response;
    }

}
