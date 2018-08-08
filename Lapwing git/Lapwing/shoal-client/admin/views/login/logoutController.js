/*global angular */

(function () {
    'use strict';
    angular.module('shoalAdmin.views.login')
        .controller('shoalAdmin.views.login.LogoutController', function ($scope, $location, shoalCommon_security_AuthService) {

            var Auth = shoalCommon_security_AuthService;

            Auth.attemptLogout();
        });
}());