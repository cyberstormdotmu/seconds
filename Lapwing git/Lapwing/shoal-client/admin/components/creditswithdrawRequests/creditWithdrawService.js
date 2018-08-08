/*global angular */
(function () {
    'use strict';

    angular.module('shoalAdmin.creditWithdrawRequests')
        .factory('shoalAdmin_creditsWithdrawRequests_CreditsWithdrawRequestsService', function ($http, $q, ENV) {

            var orderAdminWebServiceUrl = ENV.webServiceUrl + "/admin/fetchWithdrawCreditList",
                orderAdminWebWithdrawServiceUrl = ENV.webServiceUrl + "/admin/withdrawrequest",

                fetchByStatus = function (status) {

                    var defer = $q.defer();

                    $http.get(orderAdminWebServiceUrl, {params: {status: status}})
                        .then(function (response) {
                            var creditWithdrawRequests = response.data;
                            defer.resolve(Object.freeze(creditWithdrawRequests));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                getUnconfirmedWithdrawCredits = function () {
                    return fetchByStatus("PENDING");
                },
                getConfirmedWithdrawCredits = function () {
                    return fetchByStatus("ACCEPT");
                },
                getCancelledWithdrawCredits = function () {
                    return fetchByStatus("REJECT");
                },
                cancelWithdrawCredit = function (creditWithdrawRequestid) {
                    var defer = $q.defer();

                    $http({
                        method: 'POST',
                        url: orderAdminWebWithdrawServiceUrl + '/cancel',
                        params: {'id': creditWithdrawRequestid}
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                },
                recordPayment = function (creditWithdrawRequest) {
                    var defer = $q.defer();

                    $http({
                        method: 'POST',
                        url: orderAdminWebWithdrawServiceUrl + '/accept',
                        params: {'id': creditWithdrawRequest.buyerWithdrawCreditId, 'date': creditWithdrawRequest.dateReceived, 'type': creditWithdrawRequest.paymentType, 'reference': creditWithdrawRequest.userReference}
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                },
                fetchWithdrawCredit = function (orderReference) {
                    var defer = $q.defer();

                    $http.get(orderAdminWebServiceUrl + orderReference)
                        .then(function (response) {
                            var creditWithdrawRequest = response.data;
                            defer.resolve(Object.freeze(creditWithdrawRequest));
                        }, function (response) {
                            defer.reject({
                                reason : response.data.message
                            });
                        });
                    return defer.promise;
                };

            return {
                getUnconfirmedWithdrawCredits: getUnconfirmedWithdrawCredits,
                getConfirmedWithdrawCredits: getConfirmedWithdrawCredits,
                getCancelledWithdrawCredits: getCancelledWithdrawCredits,
                cancelWithdrawCredit: cancelWithdrawCredit,
                recordPayment: recordPayment,
                fetchWithdrawCredit: fetchWithdrawCredit
            };
        });
}());