/*global angular */
(function () {
    'use strict';

    angular.module('shoalApp.orders')
        .factory('shoalApp_orders_OrderService', function ($http, $q, ENV, shoalApp_classes_Order) {
            var ordersWebServiceUrl = ENV.webServiceUrl + "/orders/",
                orderConstructor = shoalApp_classes_Order,

                create = function () {
                    return orderConstructor();
                },
                submitOrder = function (order) {
                    var defer = $q.defer(),
                        payload = {
                            lines: order.lines,
                            creditToBeApplied: order.creditToBeApplied,
                            appliedVendorCredits: order.appliedVendorCredits,
                            invoiceAddress: order.invoiceAddress,
                            deliveryAddress: order.deliveryAddress,
                            paymentMethod: order.paymentMethod,
                            paymentCardToken: order.paymentCardToken
                        };

                    $http.post(ordersWebServiceUrl, payload, { timeout: 60000 })
                        .then(function (response) {
                            if (response.status === 201) {
                                defer.resolve(response.data);
                            } else {
                                defer.reject({
                                    reason: 'order not accepted by server'
                                });
                            }
                        }, function (error) {
                            defer.reject({
                                reason: error.data.message
                            });
                        });
                    return defer.promise;
                },
                getOrder = function (orderReference) {
                    var defer = $q.defer();
                    $http.get(ordersWebServiceUrl + orderReference)
                        .then(function (response) {
                            var order = response.data;
                            defer.resolve(Object.freeze(order));
                        }, function (response) {
                            defer.reject({
                                reason : response.data.message
                            });
                        });
                    return defer.promise;
                },
                getOrders = function () {
                    var defer = $q.defer();
                    $http.get(ordersWebServiceUrl)
                        .then(function (response) {
                            var orders = response.data;
                            defer.resolve(Object.freeze(orders));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                getOrderBalance = function () {
                    var defer = $q.defer();
                    $http.get(ordersWebServiceUrl + 'getOrderBalance')
                        .then(function (response) {
                            var orderBalance = response.data;
                            defer.resolve(Object.freeze(orderBalance));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                };

            return {
                create: create,
                getOrders: getOrders,
                getOrder: getOrder,
                submitOrder: submitOrder,
                getOrderBalance: getOrderBalance
            };
        });
}());