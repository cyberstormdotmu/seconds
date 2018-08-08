package com.ishoal.app.integrationtest.matchers;

import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.isOneOf;

public class AccessDeniedMatcher {
    private AccessDeniedMatcher() {}
    public static Matcher<Integer> accessDenied() {

        return isOneOf(HttpStatus.SC_UNAUTHORIZED, HttpStatus.SC_FORBIDDEN);
    }

}
