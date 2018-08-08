/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.checkout')
        .controller('shoalApp.views.checkout.CheckoutAddressesController', function ($state, shoalApp_checkout_CheckoutService,
                                                                            buyerProfile) {

            var vm = this,
                checkoutService = shoalApp_checkout_CheckoutService,
                init = function () {
                    vm.order = checkoutService.order;

                    var buyerAddress = buyerProfile.form.deliveryAddress,
                        i = 0,
                        kData,
                        length = buyerProfile.form.addresses.length;
                    vm.addresses = {};
                    for (i = 0; i < length; i += 1) {
                        kData = buyerProfile.form.addresses[i].departmentName;
                        vm.addresses[kData] = buyerProfile.form.addresses[i];
                        vm.addresses[kData].organisationName = buyerProfile.form.organisation.name;
                    }

                    vm.deliveryAddressKey = buyerAddress.departmentName;
                    vm.invoiceAddressKey = buyerAddress.departmentName;

                    if (vm.order.deliveryAddress && vm.order.deliveryAddress.departmentName) {
                        vm.deliveryAddressKey = vm.order.deliveryAddress.departmentName;
                    }
                    if (vm.order.invoiceAddress && vm.order.invoiceAddress.departmentName) {
                        vm.invoiceAddressKey = vm.order.invoiceAddress.departmentName;
                    }
                },
                allowNext = function () {
                    return vm.order.invoiceAddress && vm.order.deliveryAddress;
                },
                next = function () {
                    if (vm.addressesStepForm.$valid) {
                        $state.go('checkout.payment');
                    }
                };

            init();

            vm.allowNext = allowNext;
            vm.next = next;
        });
}());