package com.ishoal.ws.buyer.controller;

import com.ishoal.common.PayloadResult;
import com.ishoal.ws.exceptionhandler.ErrorInfo;
import com.ishoal.ws.session.SendSmsSession;
import com.ishoal.core.domain.RegistrationOtp;
import com.ishoal.core.domain.Users;
import com.ishoal.core.user.SendSmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/registrationconfirm")
public class SendSmsController {
    private static final Logger logger = LoggerFactory.getLogger(SendSmsController.class);

    private final SendSmsService SendSmsService;
    
    @Value("${shoal.mail.enabled}")
	private boolean mailEnabled;

    public SendSmsController(SendSmsService SendSmsService) {
        this.SendSmsService = SendSmsService;
    }

    @RequestMapping(method = RequestMethod.GET ,value = "{registrationToken}")
    public ResponseEntity<?> sendingOtp(@PathVariable("registrationToken") String registrationToken, SendSmsSession session) {
        logger.info("Password reset verification code requested for [{}]", registrationToken);
        if (registrationToken != "PENDING" || registrationToken != "CONFIRM" || registrationToken != "REJECT"){
        	PayloadResult<RegistrationOtp> result = SendSmsService.initiatePasswordReset(registrationToken);
            if(result.isSuccess()) {
                session.setPasswordResetToken(result.getPayload());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorInfo.internalError());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registrationOtpconfirm")
    public ResponseEntity<?> resetPassword(@RequestParam(value = "verificationCode") String verificationCode, SendSmsSession session) {
    	RegistrationOtp token = session.getPasswordResetToken();
    	String verification = "000000";
        if(mailEnabled){
        if(verificationCode.equals(token.getVerificationCode())) {
        	SendSmsService.confirm(token.getUsername());
            return ResponseEntity.ok().build();
        }
        }
        else{
        	if(verification.equals(verificationCode)) {
            	SendSmsService.confirm(token.getUsername());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().body(ErrorInfo.badRequest("wrong token"));
    }
}