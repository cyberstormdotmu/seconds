/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.orders')
        .controller('shoalAdmin.views.orders.AdminOrderPaymentController', function ($scope, $uibModalInstance, unpaidAmount) {
            var vm = this,
                payment = {},
                openDatePicker = function () {
                    vm.datePicker.opened = true;
                },
                ok = function () {
                    $uibModalInstance.close(vm.payment);
                },
                cancel = function () {
                    $uibModalInstance.dismiss();
                },
                datePicker = {
                    options: {
                        formatYear: 'yy',
                        startingDay: 1,
                        showWeeks: false
                    },
                    maxDate: new Date(),
                    opened: false
                };

            Object.defineProperty(vm, 'unpaidAmount', {
                get: function () {
                    return unpaidAmount;
                }
            });

            Object.defineProperty(vm, 'payment', {
                get: function () {
                    return payment;
                }
            });

            Object.defineProperty(vm, 'datePicker', {
                get: function () {
                    return datePicker;
                }
            });

            vm.openDatePicker = openDatePicker;
            vm.ok = ok;
            vm.cancel = cancel;
        });
}());