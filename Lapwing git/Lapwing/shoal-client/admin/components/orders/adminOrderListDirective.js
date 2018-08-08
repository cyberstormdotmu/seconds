/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.orders')
        .directive('shoAdminOrderList', function () {
            return {
                restrict: 'E',
                templateUrl: '../admin/components/orders/adminOrderListDirectiveView.html',
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