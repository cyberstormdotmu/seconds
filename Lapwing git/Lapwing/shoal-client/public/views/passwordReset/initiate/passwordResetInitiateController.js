/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalPublic.views.passwordReset')
        .controller('shoalPublic.views.passwordReset.PasswordResetInitiateController', function ($state, shoalPublic_passwordReset_PasswordResetService) {
            var vm = this,
                passwordResetService = shoalPublic_passwordReset_PasswordResetService;

            vm.submitting = false;

            vm.submit = function () {
                if (vm.resetPasswordForm.$valid && !vm.submitting) {
                    vm.submitting = true;
                    passwordResetService.initiatePasswordReset(vm.emailAddress)
                        .then(function () {
                            $state.go('passwordreset.confirm');
                        }, function (error) {
                            vm.submitting = false;
                            vm.errorMessage = error.reason;
                        });
                }
            };
        });
}());