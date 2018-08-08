/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalPublic.views.registration')
        .controller('shoalPublic.views.registration.RegistrationConfirmationController', function ($scope, $http, $state, $stateParams, $location, shoalPublic_registration_RegistrationService) {
            var vm = this,
                init = function (registrationToken) {
                    vm.registrationToken = $stateParams.registrationToken;
                    shoalPublic_registration_RegistrationService.verifyRegistrationForBuyers(vm.registrationToken);
                },
                Otpsubmit = function () {
                    if (vm.twoFactorAuthenticationVerificationForm.$valid && !vm.submitting) {
                        vm.submitting = true;
                        vm.errorMessage = undefined;
                        shoalPublic_registration_RegistrationService.confirmTwoFactorAuthentication(vm.verificationCode)
                            .then(function () {
                                //vm.successMessage = 'Your Account Request Has Been Submitted Successfully';
                                vm.successMessage = "Thank you. Your account has now been verified and has been submitted for authorisation. You will be notified when your account it ready to use. Sincerely, The Silverwing Team.";
                            }, function (error) {
                                vm.submitting = false;
                                vm.errorMessage = 'Please Enter correct OTP code or Please enter correct URL.';
                            });
                    }
                };
            vm.submitting = false;
            vm.Otpsubmit = Otpsubmit;
            init();
        });
}());