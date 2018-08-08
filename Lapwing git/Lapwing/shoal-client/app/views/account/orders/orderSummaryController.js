/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalApp.views.account.orders')
        .controller('shoalApp.views.account.orders.OrderSummaryController', function ($scope, shoalApp_orders_OrderService, $filter) {
            var vm = this,
                i = 0,
                ordersService = shoalApp_orders_OrderService,
                fetchOrders = function () {
                    ordersService.getOrders()
                        .then(function (orders) {
                            vm.orders = orders;
                            $scope.allItems = orders;
                            $scope.searchedItems = orders;
                            for (i = 0; i < orders.length; i = i + 1) {
                                $scope.data.push("Item " + i);
                            }
                        }, function () {
                            console.log("error reading orders");
                        });
                },
                fetchOrderBalance = function () {
                    ordersService.getOrderBalance()
                        .then(function (orderBalance) {
                            vm.orderBalance = orderBalance;
                        }, function () {
                            console.log("error reading orderBalance");
                        });
                },
                searchedOrderResult = function (itemLists, searchOrderStatus, searchPaymentStatus) {
                    $scope.searchedItems = [];
                    itemLists.forEach(function (value) {
                        if (value.status.toLowerCase().indexOf(searchOrderStatus.toLowerCase()) > -1 && value.paymentStatus.toLowerCase().indexOf(searchPaymentStatus.toLowerCase()) > -1) {
                            $scope.searchedItems.push(value);
                        }
                    });
                    return $scope.searchedItems;
                },
                searchOrderResult = function () {
                    $scope.searchedItems = $scope.allItems;
                    if (vm.searchOrderStatusText !== '' || vm.searchPaymentStatusText !== '') {
                        $scope.searchedItems = searchedOrderResult($scope.allItems, vm.searchOrderStatusText, vm.searchPaymentStatusText);
                        $scope.data = [];
                        for (i = 0; i < $scope.searchedItems.length; i = i + 1) {
                            $scope.data.push("Item " + i);
                        }
                    }
                },
                refresh = function () {
                    fetchOrders();
                    fetchOrderBalance();
                };
            vm.orderBalance = 0;
            $scope.currentPage = 0;
            $scope.pageSize = 10;
            $scope.data = [];
            $scope.q = '';
            $scope.getData = function () {
                return $filter('filter')($scope.data, $scope.q);
            };
            $scope.numberOfPages = function () {
                return Math.ceil($scope.getData().length / $scope.pageSize);
            };
            fetchOrders();
            fetchOrderBalance();
            vm.refresh = refresh;
            vm.searchOrderStatusText = '';
            vm.searchPaymentStatusText = '';
            vm.searchOrderResult = searchOrderResult;
        })
        .filter('startFrom', function () {
            return function (input, start) {
                start = +start; //parse to int
                return input.slice(start);
            };
        });
}());