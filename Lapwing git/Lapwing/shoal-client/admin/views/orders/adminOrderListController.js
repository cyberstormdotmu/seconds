/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.orders')
        .controller('shoalAdmin.views.orders.AdminOrderListController', function (shoalAdmin_orders_OrderService) {
            var vm = this,
                orderAdminService = shoalAdmin_orders_OrderService,
                fetchUnconfirmedOrders = function () {
                    orderAdminService.getUnconfirmedOrders()
                        .then(function (orders) {
                            vm.unconfirmedOrders = orders;
                        }, function () {
                            console.log("error reading unconfirmed orders");
                        });
                },
                fetchConfirmedOrders = function () {
                    orderAdminService.getConfirmedOrders()
                        .then(function (orders) {
                            vm.confirmedOrders = orders;
                        }, function () {
                            console.log("error reading confirmed orders");
                        });
                },
                fetchCancelledOrders = function () {
                    orderAdminService.getCancelledOrders()
                        .then(function (orders) {
                            vm.cancelledOrders = orders;
                        }, function () {
                            console.log("error reading cancelled orders");
                        });
                },
                refresh = function () {
                    fetchUnconfirmedOrders();
                    fetchConfirmedOrders();
                    fetchCancelledOrders();
                };

            console.log('loading orders');
            refresh();

            vm.refresh = refresh;
            vm.unconfirmedOrders = [];
            vm.confirmedOrders = [];
        });
}());