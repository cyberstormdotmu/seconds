/*global angular */

(function () {
    'use strict';
    angular.module('shoalApp.views.logout')
        .controller('shoalApp.views.logout.LogoutController', function ($scope, $location, shoalCommon_security_AuthService) {

            var Auth = shoalCommon_security_AuthService;

            Auth.attemptLogout();
        });
}());