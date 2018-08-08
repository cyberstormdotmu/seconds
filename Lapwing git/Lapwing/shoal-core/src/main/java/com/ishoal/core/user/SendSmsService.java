package com.ishoal.core.user;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.core.domain.RegistrationOtp;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Users;
import com.ishoal.core.persistence.adapter.UserEntityAdapter;
import com.ishoal.core.repository.UserRepository;
import com.ishoal.core.security.SecurePassword;
import com.ishoal.sms.SmsService;
import com.ishoal.sms.textmagic.TextMagicSmsService;
import com.textmagic.sdk.RestClient;

public class SendSmsService {
	private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);
	
	@Value("${shoal.sms.textMagicUserName}")
	private String textMagicUserName;
	
	@Value("${shoal.sms.textMagicApiKey}")
	private String textMagicApiKey;
	
	private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/yy");
	private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
	private final SmsService smsService;
	private final UserRepository userRepository;

	public SendSmsService(UserRepository userRepository, SmsService smsService) {
		this.userRepository = userRepository;
		this.smsService = smsService;
	}
	
	@Transactional
	public void confirm(String userName) {

		User user = userRepository.findByUsernameIgnoreCase(userName);
		if (user != null){
			user.requestingConfirmation();
		}
		userRepository.savePendingRegistration(user);
	}
	
	public PayloadResult<RegistrationOtp> initiatePasswordReset(String registrationToken) {
		RegistrationOtp token;
		PayloadResult<RegistrationOtp> result;

		User user = userRepository.findByRegistrationTokenIgnoreCase(registrationToken);
		if (user != null) {
			token = RegistrationOtp.forValidUser(user.getUsername());
			TextMagicSmsService TextMagicSmsService = new TextMagicSmsService(new RestClient(textMagicUserName,textMagicApiKey));
			if (TextMagicSmsService.sendSms(user.getMobileNumber(), createSmsMessage(token))) {
				logger.info("Sent mobile verification code by SMS for user [{}]", user.getUsername());
				result = PayloadResult.success(token);
			} else {
				logger.warn("Failed to send password reset verification code by SMS for user [{}]", user.getUsername());
				result = PayloadResult.error("Unable to send password reset verification code");
			}
		} else {
			logger.warn("Attempt to initiate password reset for non-existent user [{}]", user.getUsername());
			result = PayloadResult.success(RegistrationOtp.forInvalidUser(user.getUsername()));
		}

		return result;
	}

	public Result resetPassword(RegistrationOtp token, String verificationCode, String newPassword) {
		Result result = SimpleResult.error("The verification code is not valid");

		if (token.isValidUser()) {
			if (isValidVerificationCode(token, verificationCode)) {
				User user = userRepository.findByUsernameIgnoreCase(token.getUsername());
				user.setHashedPassword(SecurePassword.fromClearText(newPassword));
				userRepository.save(user);
				logger.info("Successful password reset for user [{}]", token.getUsername());
				result = SimpleResult.success();
			} else {
				logger.warn("Failed password reset attempt for user [{}] with invalid/expired verification code",
						token.getUsername());
				token.incrementFailedVerificationCount();
			}
		} else {
			logger.warn("Failed password reset attempt for invalid user [{}]", token.getUsername());
		}

		return result;
	}

	private String createSmsMessage(RegistrationOtp token) {
		return String.format(
				"Your silverwing Account verification code is %s. "
						+ "Call us immediately if you didn't request this. " + "This code will expire on %s at %s.",
				token.getVerificationCode(), dateFormatter.print(token.getExpiryDateTime()),
				timeFormatter.print(token.getExpiryDateTime()));
	}

	private boolean isValidVerificationCode(RegistrationOtp token, String verificationCode) {
		return token.getVerificationCode().equals(verificationCode) && token.getExpiryDateTime().isAfterNow();
	}

}
