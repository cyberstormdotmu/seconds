/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.orders')
        .directive('shoSupplierOrderList', function () {
            return {
                restrict: 'E',
                templateUrl: '../supplier/components/orders/supplierOrderListDirectiveView.html',
                scope: {},
                bindToController: {
                    orders: '='
                },
                controllerAs: 'vm',
                controller: function () {
                    return;
                }
            };
        });
}());