package com.ishoal.common;

public interface Result {
    boolean isSuccess();

    boolean isError();

    String getError();
}
