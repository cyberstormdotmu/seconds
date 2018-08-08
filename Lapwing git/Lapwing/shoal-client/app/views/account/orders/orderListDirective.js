/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.account.orders')
        .directive('shoOrderList', function () {
            return {
                restrict: 'E',
                templateUrl: '../app/views/account/orders/orderListDirectiveView.html',
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