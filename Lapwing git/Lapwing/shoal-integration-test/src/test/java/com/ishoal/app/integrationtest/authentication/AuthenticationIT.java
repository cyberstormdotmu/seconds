package com.ishoal.app.integrationtest.authentication;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_LOGIN;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class AuthenticationIT extends AbstractIntegrationTest {

    @Test
    public void shouldAuthenticateSuccessfully() {
        given().auth().basic("oliver.squires@ishoal.com", "password")
                .when().get(WS_LOGIN).then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldReturnUnauthorizedWhenInvalidPassword() {
        given().auth().basic("oliver.squires@ishoal.com", "wordpass")
                .when().get(WS_LOGIN).then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldReturnUnauthorizedWhenInvalidUsername() {
        given().auth().basic("nobody@ishoal.com", "password")
                .when().get(WS_LOGIN).then().statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    public void shouldReturnUnauthorizedWhenNoCredentials() {
        given().auth().none()
                .when().get(WS_LOGIN).then().statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    public void shouldReturnTheUsernameOnSuccessfulAuthentication() {
        given().auth().basic("oliver.squires@ishoal.com", "password")
                .when().get(WS_LOGIN).then().body("username", is("oliver.squires@ishoal.com"));

    }

    @Test
    public void shouldReturnTheRolesOnSuccessfulAuthentication() {
        given().auth().basic("oliver.squires@ishoal.com", "password")
                .when().get(WS_LOGIN).then().body("roles", hasItems("ADMIN", "BUYER"));

    }

    public static final String WS_LOGOUT = "/ws/logout";

    @Test
    public void shouldRememberAuthenticationStateBetweenCalls() {

        Response response = given().auth().basic("oliver.squires@ishoal.com", "password")
            .when().get(WS_LOGIN);
        String jSessionId = response.getSessionId();
        response.then().statusCode(HttpStatus.SC_OK);

        given().auth().none()
            .sessionId(jSessionId)
            .when().get(WS_LOGIN).then().statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void shouldRemoveSessionOnSuccessfulLogout() {

        Response response = given().auth().basic("oliver.squires@ishoal.com", "password")
            .when().get(WS_LOGIN);
        String sessionId = response.getSessionId();
        response.then().statusCode(HttpStatus.SC_OK);

        given().auth().none()
            .sessionId(sessionId)
            .when().post(WS_LOGOUT).then().statusCode(HttpStatus.SC_OK);

        given().auth().none()
            .sessionId(sessionId)
            .when().get(WS_LOGIN).then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
