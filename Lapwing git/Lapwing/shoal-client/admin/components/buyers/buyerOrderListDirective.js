/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.buyers')
        .directive('shoBuyerOrderList', function () {
            return {
                restrict: 'E',
                templateUrl: '../admin/components/buyers/buyerOrderListDirectiveView.html',
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