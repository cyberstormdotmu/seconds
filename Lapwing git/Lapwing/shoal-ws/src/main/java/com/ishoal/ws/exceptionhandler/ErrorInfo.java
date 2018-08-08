package com.ishoal.ws.exceptionhandler;

import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;

public class ErrorInfo {
    private DateTime timestamp = DateTime.now();
    private int status;
    private String error;
    private String message;

    private ErrorInfo(HttpStatus status, String error, String message) {
        this.status = status.value();
        this.error = error;
        this.message = message;
    }

    public static ErrorInfo conflict() {
        return new ErrorInfo(HttpStatus.CONFLICT, "Conflict", "The data has already been updated by another user");
    }

    public static ErrorInfo internalError() {
        return new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An internal error occurred handling the request");
    }

    public static ErrorInfo badRequest(String message) {
        return new ErrorInfo(HttpStatus.BAD_REQUEST, "Bad Request", message);
    }

    public static ErrorInfo notFound(String message) {
        return new ErrorInfo(HttpStatus.NOT_FOUND, "Not Found", message);
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
