/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalSupplier.views.twoFactorAuthentication')
        .controller('shoalSupplier.views.twoFactorAuthentication.TwoFactorAuthenticationConfirmController', function ($state, $location, shoalSupplier_twoFactorAuthentication_TwoFactorAuthenticationService) {
            var vm = this,
                twoFactorAuthenticationService = shoalSupplier_twoFactorAuthentication_TwoFactorAuthenticationService;

            vm.submitting = false;

            vm.submit = function () {
                if (vm.twoFactorAuthenticationVerificationForm.$valid && !vm.submitting) {
                    vm.submitting = true;
                    vm.errorMessage = undefined;
                    twoFactorAuthenticationService.confirmTwoFactorAuthentication(vm.verificationCode)
                        .then(function () {
                            console.log("hello Done");
                            $location.path('/registrations');
                        }, function (error) {
                            vm.submitting = false;
                            vm.errorMessage = 'Please Enter Correct OTP Code.';
                        });
                }
            };
        });
}());