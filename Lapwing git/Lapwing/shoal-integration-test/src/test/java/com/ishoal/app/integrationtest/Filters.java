package com.ishoal.app.integrationtest;

import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

import java.util.Arrays;
import java.util.List;

public class Filters {
    public static final Filter REQUEST_LOGGING_FILTER = new RequestLoggingFilter();
    public static final Filter RESPONSE_LOGGING_FILTER = new ResponseLoggingFilter();
    public static final XsrfFilter XSRF_FILTER = new XsrfFilter();

    public static final List<Filter> DEFAULT_FILTERS = Arrays.asList(XSRF_FILTER, REQUEST_LOGGING_FILTER, RESPONSE_LOGGING_FILTER);
}
