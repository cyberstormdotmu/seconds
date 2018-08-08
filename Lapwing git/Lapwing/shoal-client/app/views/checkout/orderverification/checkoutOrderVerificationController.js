/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.checkout')
        .controller('shoalApp.views.checkout.CheckoutOrderVerificationController', function ($state, shoalApp_checkout_CheckoutService, shoalApp_orderverification_OrderVerificationService) {

            var vm = this,
                checkoutService = shoalApp_checkout_CheckoutService,
                orderVerificationService = shoalApp_orderverification_OrderVerificationService,
                init = function () {
                    orderVerificationService.initiatePasswordReset();
                    vm.order = checkoutService.order;
                    vm.orderVerificationForm = {};
                    vm.orderVerificationForm.verificationCode = '';
                    vm.verificationCode = '';
                },
                verifyOrder = function () {
                    if (vm.orderVerificationForm.$valid) {
                        orderVerificationService.initiateOTPConfirm(vm.verificationCode)
                            .then(function () {
                                $state.go('checkout.review');
                            }, function () {
                                vm.orderVerificationForm.message = 'Please Enter Correct OTP Code.';
                            });
                    } else {
                        vm.orderVerificationForm.message = 'Please enter Verification Code.';
                    }
                };

            init();

            vm.verifyOrder = verifyOrder;
        });
}());