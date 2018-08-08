/*global angular */

(function () {
    'use strict';
    angular.module('shoalSupplier.views.login')
        .controller('shoalSupplier.views.login.LogoutController', function ($scope, $location, shoalCommon_security_AuthService) {

            var Auth = shoalCommon_security_AuthService;

            Auth.attemptLogout();
        });
}());