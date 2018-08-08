/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.security')
        .service('shoalCommon_security_RouteChangeService', function ($rootScope, $state, $q, shoalCommon_security_AuthService) {

            var my = {},
                Auth = shoalCommon_security_AuthService,
                tryAuthentication = function () {
                    var defer = $q.defer();
                    console.log('checking if user authenticated');
                    // if user is already authenticated then proceed
                    Auth.isAuthenticated().then(function () {
                        console.log('user is authenticated');
                        defer.resolve();
                    }).catch(function () {
                        defer.reject();
                    });
                    return defer.promise;
                };


            my.ifViewHasAccessRestrictions = function (toState) {
                return toState.data && toState.data.access && toState.data.access.requiresAuthorisation;
            };
            my.ensureUserAuthenticatedBeforeAllowingRouteChange = function (event) {
                tryAuthentication().then(
                    function () {
                        console.log('allowing state transition');
                    },
                    function () {
                        console.log('user session expired - redirecting user away from access restricted site');
                        event.preventDefault();
                        Auth.attemptLogout();
                    }
                );
            };
            my.registerRouteHandler = function () {
                if ($rootScope) {
                    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                        var redirect = toState.redirectTo;
                        if (redirect) {
                            event.preventDefault();
                            console.log('redirect to ' + redirect);
                            $state.go(redirect, toParams);
                        } else {
                            if (my.ifViewHasAccessRestrictions(toState)) {
                                my.ensureUserAuthenticatedBeforeAllowingRouteChange(event);
                            }
                        }
                    });
                }
            };

            return Object.create({}, {
                handleRouteChange: {
                    value: function () {
                        my.registerRouteHandler();
                    }
                }
            });
        });
}());
