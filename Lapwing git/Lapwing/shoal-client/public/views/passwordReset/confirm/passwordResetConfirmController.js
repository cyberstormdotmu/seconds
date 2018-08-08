/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalPublic.views.passwordReset')
        .controller('shoalPublic.views.passwordReset.PasswordResetConfirmController', function ($state, shoalPublic_passwordReset_PasswordResetService) {
            var vm = this,
                passwordResetService = shoalPublic_passwordReset_PasswordResetService;

            vm.submitting = false;

            vm.submit = function () {
                if (vm.resetPasswordForm.$valid && !vm.submitting) {
                    vm.submitting = true;
                    vm.errorMessage = undefined;
                    passwordResetService.confirmPasswordReset(vm.password, vm.verificationCode)
                        .then(function () {
                            $state.go('passwordreset.complete');
                        }, function (reason) {
                            vm.submitting = false;
                            console.log(reason);
                            vm.errorMessage = reason.message;
                        });
                }
            };
        });
}());