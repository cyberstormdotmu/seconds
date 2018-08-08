/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.security')
        .factory('shoalCommon_security_AuthFailHandler',
            function ($window, $rootScope, shoalCommon_security_LoginRedirectService) {
                var loginRedirect = shoalCommon_security_LoginRedirectService,
                    isUnauthorisedUser = function (rejection) {
                        return rejection.status === 401 || rejection.status === 403;
                    },
                    redirectToPublicSiteIfWebServiceReportsUserIsUnauthorised = function (rejection) {

                        if (isUnauthorisedUser(rejection)) {
                            loginRedirect.redirectToLogin();
                            return true;
                        }
                        return false;
                    };

                return {
                    handleAuthFailure: redirectToPublicSiteIfWebServiceReportsUserIsUnauthorised
                };
            });
}());
