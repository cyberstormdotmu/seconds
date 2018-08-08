package com.ishoal.common;

import com.ishoal.common.util.ErrorType;

public class PayloadResult<T> implements Result {
    private final Result result;
    private final T payload;
    private final ErrorType errorType;

    public PayloadResult(boolean success, ErrorType errorType, String error, T payload) {
        this.result = new SimpleResult(success, error);
        this.payload = payload;
        this.errorType = errorType;
    }

    private PayloadResult(boolean success, String error, T payload) {
        this(success, ErrorType.NONE, error, payload);
    }

    public static <T> PayloadResult<T> success(T payload) {
        return new PayloadResult<>(true, "", payload);
    }

    public static <T> PayloadResult<T> error(String error) {
        return error(ErrorType.NONE, error);
    }

    public static <T> PayloadResult<T> error(ErrorType errorType, String error) {

        return new PayloadResult<>(false, errorType, error, null);
    }

    public ErrorType getErrorType() {

        return errorType;
    }

    @Override
    public boolean isSuccess() {
        return result.isSuccess();
    }

    @Override
    public boolean isError() {
        return result.isError();
    }

    @Override
    public String getError() {
        return result.getError();
    }

    public T getPayload() {
        return payload;
    }
}
