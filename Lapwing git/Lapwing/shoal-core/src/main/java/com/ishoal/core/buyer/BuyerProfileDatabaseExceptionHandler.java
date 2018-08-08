package com.ishoal.core.buyer;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.util.ErrorType;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.User;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class BuyerProfileDatabaseExceptionHandler {

    public static final String UNIQUE_USER_NAME = "UQ_USERS_USER_NAME";

    public BuyerProfileDatabaseExceptionHandler() {

    }

    public PayloadResult<BuyerProfile> handleDatabaseException(RegisterBuyerRequest request,
        DataIntegrityViolationException dbException) {

        PayloadResult<BuyerProfile> result;
        Throwable cause = dbException.getCause();
        if (cause instanceof ConstraintViolationException) {
            ConstraintViolationException exception = (ConstraintViolationException) dbException.getCause();

            result = checkUniqueUsernameViolation(request.getUser(), exception);
        } else {
            throw dbException;
        }
        return result;
    }

    PayloadResult<BuyerProfile> checkUniqueUsernameViolation(User user,
        ConstraintViolationException exception) {

        PayloadResult<BuyerProfile> result = null;
        if (UNIQUE_USER_NAME.equalsIgnoreCase(exception.getConstraintName())) {
            result = PayloadResult.error(ErrorType.CONFLICT, String.format("Username '%s' already taken",
                user.getEmailAddress()));
        }
        return result;
    }
}