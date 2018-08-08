/*global angular */

(function () {
    'use strict';
    angular.module('shoalSupplier.views.login')
        .controller('shoalSupplier.views.login.LoginController', function ($scope, $location, shoalCommon_security_AuthService, shoalSupplier_twoFactorAuthentication_TwoFactorAuthenticationService) {

            var Auth = shoalCommon_security_AuthService,
                twoFactorAuthenticationService = shoalSupplier_twoFactorAuthentication_TwoFactorAuthenticationService;
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