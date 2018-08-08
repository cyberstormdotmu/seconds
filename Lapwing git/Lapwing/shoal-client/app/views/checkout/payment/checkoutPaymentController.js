/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.checkout')
        .controller('shoalApp.views.checkout.CheckoutPaymentController', function ($rootScope, $state, shoalApp_checkout_CheckoutService) {

            var vm = this,
                checkoutService = shoalApp_checkout_CheckoutService,
                init = function () {
                    vm.order = checkoutService.order;
                    vm.order.paymentMethod = 'Card Payment';
                    console.log(JSON.stringify(vm.order));
                    vm.paymentCard = checkoutService.paymentCard;
                    checkoutService.fetchCreditBalances();
                    $rootScope.paymentStepOne = true;
                    $rootScope.paymentStepTwo = false;
                },
                allowNext = function () {
                    return vm.creditSpendForm.$valid && vm.paymentCardForm.$valid;
                },
                next = function () {
                    vm.order.paymentMethod = 'On Invoice';
                    $state.go('checkout.orderverification');
                },
                stepChange = function () {
                    if (vm.order.basket.grossTotal - (vm.order.creditToBeApplied + vm.order.appliedVendorCredits[0].creditsApplied) === 0) {
                        $rootScope.paymentStepOne = false;
                        $rootScope.paymentStepTwo = true;
                    }
                },
                paymentCancel = function () {
                    $rootScope.paymentStepOne = true;
                    $rootScope.paymentStepTwo = false;
                };

            init();

            vm.allowNext = allowNext;
            vm.next = next;
            vm.stepChange = stepChange;
            vm.paymentCancel = paymentCancel;

            Object.defineProperty(vm, "availableCreditBalance", {
                get: function () {
                    return checkoutService.availableCreditBalance;
                }
            });

            Object.defineProperty(vm, "vendorCreditBalance", {
                get: function () {
                    return checkoutService.vendorCreditBalance;
                }
            });

            Object.defineProperty(vm, "maximumCreditSpend", {
                get: function () {
                    return checkoutService.maximumCreditSpend;
                }
            });

            Object.defineProperty(vm, "maximumVendorCreditSpend", {
                get: function () {
                    return checkoutService.maximumVendorCreditSpend;
                }
            });

            Object.defineProperty(vm, "paymentMethodName", {
                get: function () {
                    return checkoutService.paymentMethodName;
                }
            });

            Object.defineProperty(vm, "paymentChargesPercentage", {
                get: function () {
                    return checkoutService.paymentChargesPercentage;
                }
            });

            Object.defineProperty(vm, "paymentExtraCharge", {
                get: function () {
                    return checkoutService.paymentExtraCharge;
                }
            });
        });
}());