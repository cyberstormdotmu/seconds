/*global angular */
(function () {
    'use strict';

    angular.module('shoalSupplier.orders')
        .factory('shoalSupplier_orders_OrderService', function ($http, $q, ENV) {

            var orderAdminWebServiceUrl = ENV.webServiceUrl + "/admin/orders/",
                orderAdminWebServiceUrlSupplier = ENV.webServiceUrl + "/admin/orders/supplier",
                fetchByStatus = function (status) {

                    var defer = $q.defer();

                    $http.get(orderAdminWebServiceUrlSupplier, {params: {status: status}})
                        .then(function (response) {
                            var orders = response.data;
                            defer.resolve(Object.freeze(orders));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                fetchByStatusOfBuyer = function (status, buyerId) {

                    var defer = $q.defer();
                    $http.get(orderAdminWebServiceUrl + 'buyerOrdersOfSupplier', {params: {status: status, buyerId: buyerId}})
                        .then(function (response) {
                            var orders = response.data;
                            defer.resolve(Object.freeze(orders));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                getUnconfirmedOrders = function () {
                    return fetchByStatus("PROCESSING");
                },
                getConfirmedOrders = function () {
                    return fetchByStatus("CONFIRMED");
                },
                getCancelledOrders = function () {
                    return fetchByStatus("CANCELLED");
                },
                getUnconfirmedOrdersOfBuyer = function (buyerID) {
                    return fetchByStatusOfBuyer("PROCESSING", buyerID);
                },
                getConfirmedOrdersOfBuyer = function (buyerID) {
                    return fetchByStatusOfBuyer("CONFIRMED", buyerID);
                },
                getCancelledOrdersOfBuyer = function (buyerID) {
                    return fetchByStatusOfBuyer("CANCELLED", buyerID);
                },
                confirmOrder = function (orderReference, version) {
                    var defer = $q.defer();

                    $http({
                        method: 'PUT',
                        url: orderAdminWebServiceUrl + orderReference + '/confirm',
                        params: {'version': version}
                    }).then(function (response) {
                        if (response.status === 200) {
                            defer.resolve();
                        } else {
                            defer.reject({
                                reason: 'order confirmation not accepted by server'
                            });
                        }
                    }, function (response) {
                        defer.reject({
                            reason : response.data.message
                        });
                    });
                    return defer.promise;
                },
                cancelOrder = function (orderReference, version) {
                    var defer = $q.defer();

                    $http({
                        method: 'PUT',
                        url: orderAdminWebServiceUrl + orderReference + '/cancel',
                        params: {'version': version}
                    }).then(function (response) {
                        if (response.status === 200) {
                            defer.resolve();
                        } else {
                            defer.reject({
                                reason: 'order cancellation not accepted by server'
                            });
                        }
                    }, function (response) {
                        defer.reject({
                            reason : response.data.message
                        });
                    });
                    return defer.promise;
                },
                recordPayment = function (orderReference, version, paymentDetails) {
                    var defer = $q.defer();

                    $http({
                        method: 'POST',
                        url: orderAdminWebServiceUrl + orderReference + '/payments',
                        params: {'version': version},
                        data: paymentDetails
                    }).then(function (response) {
                        if (response.status === 200) {
                            defer.resolve();
                        } else {
                            defer.reject({
                                reason: 'order payment not accepted by server'
                            });
                        }
                    }, function (response) {
                        defer.reject({
                            reason : response.data.message
                        });
                    });
                    return defer.promise;
                },
                deletePayment = function (orderReference, version, paymentReference) {
                    var defer = $q.defer();

                    $http({
                        method: 'DELETE',
                        url: orderAdminWebServiceUrl + orderReference + '/payments/' + paymentReference,
                        params: {'version': version}
                    }).then(function (response) {
                        if (response.status === 200) {
                            defer.resolve();
                        } else {
                            defer.reject({
                                reason: 'deletion of order payment not accepted by server'
                            });
                        }
                    }, function (response) {
                        defer.reject({
                            reason : response.data.message
                        });
                    });
                    return defer.promise;
                },
                fetchOrder = function (orderReference) {
                    var defer = $q.defer();

                    $http.get(orderAdminWebServiceUrl + orderReference)
                        .then(function (response) {
                            var order = response.data;
                            defer.resolve(Object.freeze(order));
                        }, function (response) {
                            defer.reject({
                                reason : response.data.message
                            });
                        });
                    return defer.promise;
                };

            return {
                getUnconfirmedOrders: getUnconfirmedOrders,
                getConfirmedOrders: getConfirmedOrders,
                getCancelledOrders: getCancelledOrders,
                getUnconfirmedOrdersOfBuyer: getUnconfirmedOrdersOfBuyer,
                getConfirmedOrdersOfBuyer: getConfirmedOrdersOfBuyer,
                getCancelledOrdersOfBuyer: getCancelledOrdersOfBuyer,
                confirmOrder: confirmOrder,
                cancelOrder: cancelOrder,
                recordPayment: recordPayment,
                deletePayment: deletePayment,
                fetchOrder: fetchOrder
            };
        });
}());