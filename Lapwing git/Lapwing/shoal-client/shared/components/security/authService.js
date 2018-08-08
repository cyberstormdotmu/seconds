/*global angular, btoa, console */
(function () {
    'use strict';
    function authService($rootScope, $q, $http, $window, ENV, shoalCommon_security_LoginRedirectService) {
        var loginRedirect = shoalCommon_security_LoginRedirectService,
            attemptLogin = false,
            currentUser = {
                name: '',
                email: '',
                password: '',
                authenticated: false,
                error: false
            },
            clearCurrentUser = function () {
                currentUser = {
                    name: '',
                    email: '',
                    password: '',
                    authenticated: false,
                    error: false
                };
            },
            authenticate = function () {

                var defer = $q.defer(),
                    headers = attemptLogin ? {
                        "Authorization": "Basic " + btoa(currentUser.email + ":" + currentUser.password)
                    } : {};

                currentUser.password = '';

                $http.get(ENV.webServiceUrl + '/login',
                    {
                        headers: headers
                    })
                    .then(function (response) {
                        if (response.data.activated) {
                            currentUser.name = response.data.username;
                            currentUser.roles = response.data.roles;
                            currentUser.authenticated = true;
                            defer.resolve();
                        } else {
                            defer.reject({
                                reason: 'Your user account has not yet been activated'
                            });
                        }
                    }, function (response) {
                        var reason;
                        currentUser.authenticated = false;
                        if (response.status === 401 || response.status === 403) {
                            reason =  'Your email or password was incorrect.';
                        } else {
                            reason = 'The server returned an error. Please try again.';
                        }
                        defer.reject({
                            reason: reason
                        });
                    });
                return defer.promise;
            },
            isAuthenticated = function () {
                var defer = $q.defer();
                if (currentUser.authenticated) {
                    defer.resolve({
                        currentUser: currentUser
                    });
                } else {
                    console.log("user not authenticated - checking server");
                    authenticate().then(function () {
                        console.log("user auth passed");
                        defer.resolve({
                            currentUser: currentUser
                        });

                    }, function (response) {
                        console.log("user auth failed");
                        defer.reject(response);
                    });
                }
                return defer.promise;
            },
            tryLogin = function () {
                var defer = $q.defer();
                attemptLogin = true;
                authenticate().then(function () {
                    $window.sessionStorage.clear();
                    $window.sessionStorage.removeItem('pickBuyerByAdminForPlaceOrder');
                    $window.sessionStorage.removeItem('creditFormCompanyName');
                    $window.sessionStorage.removeItem('creditFormTradingAs');
                    $window.sessionStorage.removeItem('creditFormInvoiceAddress');
                    $window.sessionStorage.removeItem('creditFormDeliveryAddress');
                    $window.sessionStorage.removeItem('creditFormRegisteredAddress');
                    $window.sessionStorage.removeItem('creditFormLandlineNumber');
                    $window.sessionStorage.removeItem('creditFormWebsite');
                    $window.sessionStorage.removeItem('creditFormVatRegistration');
                    $window.sessionStorage.removeItem('creditFormPurchasingManager');
                    $window.sessionStorage.removeItem('creditFormContactName');
                    $window.sessionStorage.removeItem('creditFormTelephoneNumber');
                    $window.sessionStorage.removeItem('creditFormEmailAddress');
                    $window.sessionStorage.removeItem('creditFormIsCreditAccount');
                    $window.sessionStorage.removeItem('creditFormPrintName');
                    $window.sessionStorage.removeItem('creditFormRegistrationDate');
                    $window.sessionStorage.removeItem('creditFormFullCompanyName');
                    $window.sessionStorage.removeItem('creditFormTradingName');
                    $window.sessionStorage.removeItem('creditFormExpectedCredit');
                    $window.sessionStorage.removeItem('creditFormExpectedCredit');
                    $window.sessionStorage.removeItem('registrationToken');
                    currentUser.error = false;
                    currentUser.roles.forEach(function (role) {
                        if (role === "ADMIN") {
                            $window.sessionStorage.setItem("isLoginUserAdmin", true);
                        }
                    });
                    defer.resolve({
                        currentUser: currentUser
                    });

                }, function (response) {
                    currentUser.error = true;
                    defer.reject(response);
                });
                return defer.promise;
            },
            triggerRedirect = function () {
                console.log('triggering redirect');
                loginRedirect.redirectFollowingLogout();
            },
            tryLogout = function () {

                var defer = $q.defer(),
                    url = ENV.webServiceUrl + '/logout';
                console.log("tryLogout: " + url);
                if (currentUser.authenticated) {
                    $http.post(url)
                        .then(function () {
                            clearCurrentUser();
                            $window.sessionStorage.removeItem('registrationToken');
                            triggerRedirect();
                        }, function (response) {
                            clearCurrentUser();
                            defer.reject({
                                reason: response.status + " returned from server"
                            });
                        });
                } else {
                    triggerRedirect();
                }
                return defer.promise;
            };

        return Object.create({}, {
            isAuthenticated: {
                value: isAuthenticated
            },
            attemptLogin: {
                value: tryLogin
            },
            attemptLogout: {
                value: tryLogout
            },
            getCurrentUser: {
                value: function () {
                    return currentUser;
                }
            },
            clearCurrentUser: {
                value: clearCurrentUser
            }
        });
    }

    angular.module('shoalCommon.security')
        .service('shoalCommon_security_AuthService', ['$rootScope', '$q', '$http', '$window', 'ENV', 'shoalCommon_security_LoginRedirectService', authService]);
}());