/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.checkout')
        .controller('shoalApp.views.checkout.CheckoutReviewController', function ($state, shoalApp_checkout_CheckoutService, shoalApp_views_basket_BasketViewModal, shoalApp_views_popup_termsandconditions_TermsandconditionsViewModal) {

            var vm = this,
                checkoutService = shoalApp_checkout_CheckoutService,
                init = function () {
                    vm.order = checkoutService.order;
                    vm.paymentCard = checkoutService.paymentCard;
                    vm.order.acceptedTCs = false;
                    checkoutService.fetchCreditBalances();
                },
                allowPlaceOrder = function () {
                    return vm.order.acceptedTCs;
                },
                placeOrder = function () {
                    if (vm.order.acceptedTCs) {
                        checkoutService.placeOrder()
                            .then(function (orderReference) {
                                vm.order.orderReference = orderReference;
                                $state.go('checkout.finished');
                            }, function (error) {
                                console.log('unable to complete checkout');
                                vm.errorMessages = [
                                    'Unable to complete the checkout process',
                                    error.reason
                                ];
                            });
                    }
                },
                termsAndConditionsView = function () {
                    shoalApp_views_popup_termsandconditions_TermsandconditionsViewModal.show();
                };

            init();

            vm.placeOrder = placeOrder;
            vm.termsAndConditionsView = termsAndConditionsView;
            vm.allowPlaceOrder = allowPlaceOrder;
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