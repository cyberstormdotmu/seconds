/*global angular, describe, module, beforeEach, afterEach, it, inject, spyOn, jasmine, element, expect, quickmock, SHOAL */

'use strict';

describe('shoalApp.security -> Auth service', function () {
    var Auth,
        $httpBackend,
        webServiceUrl = "https://domain.com/ws",
        loginUrl = webServiceUrl + "/login",
        logoutUrl = webServiceUrl + '/logout',
        currentUser,
        authenticatedUser = {
            username: 'rogerwatkins@codera.com',
            roles: ['BUYER'],
            activated: true,
            authenticated: true,
            error: false
        };

    beforeEach(function () {
        Auth = quickmock({
            providerName: 'shoalCommon_security_AuthService',
            moduleName: 'shoalCommon.security',
            mockModules: ['shoalApp.ShoalAppMocks']
        });
        $httpBackend = Auth.$mocks.$httpBackend;
    });

    describe('Auth service', function () {

        describe('On load', function () {

            beforeEach(function () {
                $httpBackend.whenGET(loginUrl).respond(401, {response: {}});

                currentUser = Auth.getCurrentUser();
            });

            it('should be loaded', function () {
                //spec body

                expect(Auth).toBeDefined();
            });
        });

        describe("After load", function () {

            beforeEach(function () {
                // clear out the initial authentication
                $httpBackend.expectGET(loginUrl).respond(401, {response: {}});

                Auth.isAuthenticated();

                $httpBackend.flush();

                $httpBackend.resetExpectations();
            });

            afterEach(function () {
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should call login http service on authentication", function () {

                $httpBackend.expectGET(loginUrl).respond(401, {response: {}});

                Auth.isAuthenticated();

                $httpBackend.flush();
            });

            it("should report no errors to the user even after failure", function () {

                expect(currentUser.error).toBe(false, 'should be no error');
            });

            describe('When login attempted', function () {

                beforeEach(function () {
                    currentUser = Auth.getCurrentUser();

                    currentUser.email = 'user@email.com';
                    currentUser.password = 'xxxyyy';
                });

                it("should use basic http authentication", function () {

                    $httpBackend.expectGET(loginUrl, function (headers) {
                        expect(headers.Authorization).toEqual("Basic dXNlckBlbWFpbC5jb206eHh4eXl5");
                        return true;
                    }).respond(200, {response: {}});

                    Auth.attemptLogin();

                    $httpBackend.flush();
                });
            });

            describe('when authentication successful', function () {

                beforeEach(function () {
                    $httpBackend.whenGET(loginUrl).respond(200, authenticatedUser);

                    Auth.attemptLogin();

                    // blocking call
                    $httpBackend.flush();

                    currentUser = Auth.getCurrentUser();
                });

                it("should remember that the user is authenticated", function () {

                    expect(currentUser.authenticated).toBe(true, ' user should have been authenticated');
                });

                it("should store authenticated username", function () {

                    expect(currentUser.name).toEqual('rogerwatkins@codera.com', 'user name was not remembered');
                });

                it("should store user roles", function () {
                    expect(currentUser.roles).toEqual(["BUYER"]);
                });

                it("should report no errors to the user", function () {

                    expect(currentUser.error).toBe(false, 'should be no error');
                });

            });

            describe('when authentication fails', function () {
                beforeEach(function () {
                    $httpBackend.whenGET(loginUrl).respond(401);

                    Auth.attemptLogin();

                    // blocking call
                    $httpBackend.flush();

                    currentUser = Auth.getCurrentUser();
                });

                it("should remember that the user failed authentication", function () {

                    expect(currentUser.authenticated).toBe(false, ' user should not have been authenticated');
                });

                it("should report an error to the user", function () {

                    expect(currentUser.error).toBe(true, 'should be an error');
                });
            });

            describe('when the user is authenticated', function () {

                beforeEach(function () {
                    currentUser = Auth.getCurrentUser();
                    currentUser.authenticated = true;

                    Auth.isAuthenticated();
                });

                it('should know that the user is authenticated without verifying with the server again', function () {
                    expect(currentUser.authenticated).toBe(true);
                });
            });

            describe('when logout is called', function () {

                beforeEach(function () {
                    currentUser = Auth.getCurrentUser();
                    currentUser.authenticated = true;

                    $httpBackend.whenPOST(logoutUrl).respond(200);
                    $httpBackend.whenPOST(loginUrl).respond(401);

                    Auth.attemptLogout();
                });

                it('should clear the current user', function () {
                    // blocking call
                    $httpBackend.flush();

                    expect(Auth.getCurrentUser().name).toBe('');
                    expect(Auth.getCurrentUser().authenticated).toBe(false);
                });

                it("should call the logout url", function () {
                    $httpBackend.expectPOST(logoutUrl);

                    // blocking call
                    $httpBackend.flush();
                });
            });

            describe("if the user is not authenticated", function () {

                beforeEach(function () {
                    currentUser = Auth.getCurrentUser();
                    currentUser.authenticated = false;
                    Auth.attemptLogout();
                });

                it("should not attempt to log the user out", function () {
                    expect(currentUser.authenticated).toBe(false);
                });
            });
        });
    });
});