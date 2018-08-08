/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.twoFactorAuthentication')
        .controller('shoalAdmin.views.twoFactorAuthentication.TwoFactorAuthenticationConfirmController', function ($state, $location, shoalAdmin_twoFactorAuthentication_TwoFactorAuthenticationService) {
            var vm = this,
                twoFactorAuthenticationService = shoalAdmin_twoFactorAuthentication_TwoFactorAuthenticationService;

            vm.submitting = false;

            vm.submit = function () {
                if (vm.twoFactorAuthenticationVerificationForm.$valid && !vm.submitting) {
                    vm.submitting = true;
                    vm.errorMessage = undefined;
                    twoFactorAuthenticationService.confirmTwoFactorAuthentication(vm.verificationCode)
                        .then(function () {
                            $location.path('/orders');
                        }, function (error) {
                            vm.submitting = false;
                            vm.errorMessage = 'Please Enter Correct OTP Code.';
                        });
                }
            };
        });
}());