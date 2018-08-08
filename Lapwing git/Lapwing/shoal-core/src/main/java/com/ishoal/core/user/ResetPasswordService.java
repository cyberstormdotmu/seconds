package com.ishoal.core.user;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.core.domain.PasswordResetToken;
import com.ishoal.core.domain.User;
import com.ishoal.core.repository.UserRepository;
import com.ishoal.core.security.SecurePassword;
import com.ishoal.sms.SmsService;
import com.ishoal.sms.textmagic.TextMagicSmsService;
import com.textmagic.sdk.RestClient;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class ResetPasswordService {
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);

    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/yy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
    private final SmsService smsService;
    private final UserRepository userRepository;

    @Value("${shoal.sms.textMagicUserName}")
    private String textMagicUserName;

    @Value("${shoal.sms.textMagicApiKey}")
    private String textMagicApiKey;

    public ResetPasswordService(UserRepository userRepository, SmsService smsService) {
        this.userRepository = userRepository;
        this.smsService = smsService;
    }

    public PayloadResult<PasswordResetToken> initiatePasswordReset(String username) {
        PasswordResetToken token;
        PayloadResult<PasswordResetToken> result;

        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user != null) {
            token = PasswordResetToken.forValidUser(username);
            TextMagicSmsService textMagicSmsService = new TextMagicSmsService(new RestClient(textMagicUserName,textMagicApiKey));
            if (textMagicSmsService.sendSms(user.getMobileNumber(), createSmsMessage(token))) {
                logger.info("Sent password reset verification code by SMS for user [{}]", username);
                result = PayloadResult.success(token);
            } else {
                logger.warn("Failed to send password reset verification code by SMS for user [{}]", username);
                result = PayloadResult.error("Unable to send password reset verification code");
            }
        } else {
            logger.warn("Attempt to initiate password reset for non-existent user [{}]", username);
            result = PayloadResult.success(PasswordResetToken.forInvalidUser(username));
        }

        return result;
    }

    public Result resetPassword(PasswordResetToken token, String verificationCode, String newPassword) {
        Result result = null;
        if (token.isValidUser()) {
            if (isValidVerificationCode(token, verificationCode)) {
                User user = userRepository.findByUsernameIgnoreCase(token.getUsername());
                if(user.getRegistrationToken().matches("CONTRACT_SIGNING_PENDING|CONFIRM")) {
                    user.setHashedPassword(SecurePassword.fromClearText(newPassword));
                    userRepository.save(user);
                    logger.info("Successful password reset for user [{}]", token.getUsername());
                    result = SimpleResult.success();
                }
                else {
                    logger.warn("Failed password reset attempt for invalid user [{}]", token.getUsername());
                    result = SimpleResult.error("Your Registration is still pending. You Can not Reset your password Now");
                }           
            } else {
                logger.warn("Failed password reset attempt for user [{}] with invalid/expired verification code",
                        token.getUsername());
                token.incrementFailedVerificationCount();
                result = SimpleResult.error("The verification code is not valid");
            }
        } else {
            logger.warn("Failed password reset attempt for invalid user [{}]", token.getUsername());
            result = SimpleResult.error("The verification code is not valid");
        }

        return result;
    }


    private String createSmsMessage(PasswordResetToken token) {
        return String.format(
                "Your Shoal password reset verification code is %s. "
                        + "Call us immediately if you didn't request this. " + "This code will expire on %s at %s.",
                        token.getVerificationCode(), dateFormatter.print(token.getExpiryDateTime()),
                        timeFormatter.print(token.getExpiryDateTime()));
    }

    private boolean isValidVerificationCode(PasswordResetToken token, String verificationCode) {
        return "000000".equals(verificationCode) && token.getExpiryDateTime().isAfterNow();
    }
}