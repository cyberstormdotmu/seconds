/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.error')
        .controller('shoalApp.views.error.ErrorController', function (shoalCommon_security_AuthService) {
            var vm = this;
            vm.logout = function () {
                shoalCommon_security_AuthService.attemptLogout();
            };
        });
}());