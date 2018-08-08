/*global angular, btoa, console */
(function () {
    'use strict';
    angular.module('shoalCommon.security')
        .provider('shoalCommon_security_LoginRedirectService', function () {
            var loginPageUri;

            function setLoginPageUri(value) {
                loginPageUri = value;
            }

            function instantiate($window) {

                function redirectToLogin() {
                    $window.location.replace(loginPageUri);
                }

                function redirectFollowingLogout() {
                    $window.location.replace(loginPageUri + '?logout');
                }

                return {
                    redirectToLogin: redirectToLogin,
                    redirectFollowingLogout: redirectFollowingLogout
                };
            }

            return {
                setLoginPageUri: setLoginPageUri,
                $get: instantiate
            };
        });
}());