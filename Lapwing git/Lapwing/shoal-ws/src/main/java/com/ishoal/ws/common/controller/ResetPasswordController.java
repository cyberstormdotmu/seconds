package com.ishoal.ws.common.controller;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.core.domain.PasswordResetToken;
import com.ishoal.core.domain.User;
import com.ishoal.core.user.ResetPasswordService;
import com.ishoal.ws.common.dto.ResetPasswordRequestDto;
import com.ishoal.ws.exceptionhandler.ErrorInfo;
import com.ishoal.ws.session.PasswordResetSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/resetpassword")
public class ResetPasswordController {
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);
    
    @Value("${shoal.sms.enabled}")
	private boolean smsEnabled;
    
    private final ResetPasswordService resetPasswordService;

    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @RequestMapping(method = RequestMethod.POST, value="twoFactorAuthentication")
    public ResponseEntity<?> twoFactorAuthentication(@RequestParam("verificationCode") String verificationCode, PasswordResetSession session) {
        PasswordResetToken token = session.getPasswordResetToken();
        String verification = "000000";
        if(smsEnabled)
        {
        if(token.getVerificationCode().equals(verificationCode)) {//token.getVerificationCode()
            return ResponseEntity.ok().build();
        }
        }
        else{
        	if(verification.equals(verificationCode)) {//token.getVerificationCode()
                return ResponseEntity.ok().build();
        }
        }
        return ResponseEntity.badRequest().body(ErrorInfo.badRequest("unexpected error"));
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDto dto, PasswordResetSession session) {
        PasswordResetToken token = session.getPasswordResetToken();
        Result result = resetPasswordService.resetPassword(token, dto.getVerificationCode(), dto.getNewPassword());
        /*String verification = "000000";
        System.out.println(smsEnabled);
        if(smsEnabled)
        {
        if(token.getVerificationCode().equals(dto.getVerificationCode())) {//token.getVerificationCode()
            return ResponseEntity.ok().build();
        }
        }
        else{
        	if(verification.equals(dto.getVerificationCode())) {//token.getVerificationCode()
                return ResponseEntity.ok().build();
        }
        }*/
        if (result.isSuccess()) {
        	return ResponseEntity.ok().build();
        } else {
        	return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "send")
    public ResponseEntity<?> initiatePasswordReset(User user, PasswordResetSession session) {
        logger.info("Password reset verification code requested for [{}]", user.getForename());
        PayloadResult<PasswordResetToken> result = resetPasswordService.initiatePasswordReset(user.getUsername());
        if(result.isSuccess()) {
            session.setPasswordResetToken(result.getPayload());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorInfo.internalError());
    }
    @RequestMapping(method = RequestMethod.POST, value = "initiate")
    public ResponseEntity<?> initiatePasswordReset(@RequestParam("username") String email, PasswordResetSession session) {
        logger.info("Password reset verification code requested for [{}]", email);
        PayloadResult<PasswordResetToken> result = resetPasswordService.initiatePasswordReset(email);
        if(result.isSuccess()) {
            session.setPasswordResetToken(result.getPayload());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorInfo.internalError());
    }
    
    /*@RequestMapping(method = RequestMethod.POST, value = "initiate")
    public ResponseEntity<?> initiatePasswordReset(@RequestParam("username") String email, PasswordResetSession session) {
        logger.info("Password reset verification code requested for [{}]", email);
        PayloadResult<PasswordResetToken> result = resetPasswordService.initiatePasswordReset(email);
        if(result.isSuccess()) {
            session.setPasswordResetToken(result.getPayload());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorInfo.internalError());
    }*/
    
    @RequestMapping(method = RequestMethod.POST, value="{confirmOrder}")
    public ResponseEntity<?> orderConfirmation(@PathVariable("confirmOrder") String confirmOrder, PasswordResetSession session) {
        PasswordResetToken token = session.getPasswordResetToken();
        String verification = "000000";
        if(smsEnabled){
        if(token.getVerificationCode().equals(confirmOrder)) {//token.getVerificationCode()
            return ResponseEntity.ok().build();
        }
        }
        else{
        	if(verification.equals(confirmOrder)) {//token.getVerificationCode()
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().body(ErrorInfo.badRequest("unexpected error"));
    }

}
