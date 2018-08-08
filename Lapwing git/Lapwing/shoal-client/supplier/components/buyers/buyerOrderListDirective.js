/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.buyers')
        .directive('shoBuyerOrderList', function () {
            return {
                restrict: 'E',
                templateUrl: '../supplier/components/buyers/buyerOrderListDirectiveView.html',
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