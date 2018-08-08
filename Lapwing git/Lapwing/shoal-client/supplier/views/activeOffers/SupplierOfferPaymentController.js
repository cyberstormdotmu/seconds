/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalSupplier.views.activeOffers')
        .controller('shoalSupplier.views.activeOffers.SupplierOfferPaymentController', function ($scope, $uibModalInstance, report, outstandingBalanceOfDay) {
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

            Object.defineProperty(vm, 'amount', {
                get: function () {
                    return outstandingBalanceOfDay.amount;
                }
            });

            Object.defineProperty(vm, 'paymentRecordDate', {
                get: function () {
                    return outstandingBalanceOfDay.paymentRecordDate;
                }
            });

            Object.defineProperty(vm, 'from', {
                get: function () {
                    return outstandingBalanceOfDay.balanceToBePaidToLapwing > outstandingBalanceOfDay.balanceToBeTransferredToSupplier ? 'Silverwing' : report.vendorName;
                }
            });

            Object.defineProperty(vm, 'to', {
                get: function () {
                    return outstandingBalanceOfDay.balanceToBePaidToLapwing > outstandingBalanceOfDay.balanceToBeTransferredToSupplier ? report.vendorName : 'Silverwing';
                }
            });

            Object.defineProperty(vm, 'recordPaymentStatus', {
                get: function () {
                    return outstandingBalanceOfDay.recordPaymentStatus;
                }
            });

            Object.defineProperty(vm, 'offerReference', {
                get: function () {
                    return outstandingBalanceOfDay.offerReference;
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