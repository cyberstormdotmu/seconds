/*global angular */

(function () {
    'use strict';
    angular.module('shoalAdmin.views.login')
        .controller('shoalAdmin.views.login.LoginController', function ($scope, $location, shoalCommon_security_AuthService, shoalAdmin_twoFactorAuthentication_TwoFactorAuthenticationService) {
            var Auth = shoalCommon_security_AuthService,
                twoFactorAuthenticationService = shoalAdmin_twoFactorAuthentication_TwoFactorAuthenticationService;
            Auth.clearCurrentUser();
            $scope.currentUser = Auth.getCurrentUser();

            $scope.login = function () {
                Auth.attemptLogin()
                    .then(function () {
                        twoFactorAuthenticationService.initiateTwoFactorAuthentication()
                            .then(function () {
                                $location.path('/twoFactorAuthentication');
                            }, function (error) {
                                $location.path('/login');
                            });
                    });
            };
        });
}());