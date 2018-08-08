/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.orders')
        .controller('shoalAdmin.views.orders.AdminOrderReturnController', function ($scope, $uibModalInstance, quantity, alreadyReturnedQuantity) {
            var vm = this,
                returnQuantity = {},
                ok = function () {
                    $uibModalInstance.close(vm.returnQuantity);
                },
                cancel = function () {
                    $uibModalInstance.dismiss();
                };

            Object.defineProperty(vm, 'quantity', {
                get: function () {
                    return quantity;
                }
            });

            Object.defineProperty(vm, 'alreadyReturnedQuantity', {
                get: function () {
                    return alreadyReturnedQuantity;
                }
            });

            Object.defineProperty(vm, 'returnQuantity', {
                get: function () {
                    return returnQuantity;
                }
            });


            vm.ok = ok;
            vm.cancel = cancel;
        });
}());