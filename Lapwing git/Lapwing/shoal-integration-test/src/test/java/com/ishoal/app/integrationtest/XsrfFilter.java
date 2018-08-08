package com.ishoal.app.integrationtest;

import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.internal.http.Method;
import com.jayway.restassured.response.Cookie;
import com.jayway.restassured.response.Cookies;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;

import java.util.ArrayList;
import java.util.List;

public class XsrfFilter implements Filter {

    public static final String XSRF_TOKEN = "XSRF-TOKEN";
    private Cookies cookies = new Cookies();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {

        addXsrfHeaderIfRequired(requestSpec, filterContext);

        mergeCookies(requestSpec);

        Response response = filterContext.next(requestSpec, responseSpec);

        extractCookies(response);

        return response;
    }

    public void reset() {
        cookies = new Cookies();
    }

    private void extractCookies(Response response) {
        Cookies responseCookies = response.getDetailedCookies();

        List<Cookie> mergedCookies = new ArrayList<>();
        for(Cookie cookie : responseCookies) {
            mergedCookies.add(cookie);
        }
        for(Cookie cookie : cookies) {
            if(!responseCookies.hasCookieWithName(cookie.getName())) {
                mergedCookies.add(cookie);
            }
        }

        cookies = new Cookies(mergedCookies);
    }

    private void addXsrfHeaderIfRequired(FilterableRequestSpecification requestSpec, FilterContext context) {
        if(requiresXsrfToken(context) && cookies.hasCookieWithName(XSRF_TOKEN)) {
            String xsrfToken = cookies.getValue(XSRF_TOKEN);
            requestSpec.header(new Header("X-" + XSRF_TOKEN, xsrfToken));
        }
    }

    private boolean requiresXsrfToken(FilterContext context) {
        Method method = context.getRequestMethod();
        return method == Method.POST || method == Method.PUT || method == Method.DELETE;
    }

    private void mergeCookies(FilterableRequestSpecification requestSpec) {
        Cookies requestCookies = requestSpec.getCookies();
        for(Cookie cookie : cookies) {
            if(!requestCookies.hasCookieWithName(cookie.getName())) {
                requestSpec.cookie(cookie);
            }
        }
    }
}
