package com.ishoal.common;

public class SimpleResult implements Result {

    private final boolean success;
    private final String error;

    protected SimpleResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public static Result success() {
        return new SimpleResult(true, "");
    }

    public static Result error(String error) {
        return new SimpleResult(false, error);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean isError() {
        return !success;
    }

    @Override
    public String getError() {
        return error;
    }

}
