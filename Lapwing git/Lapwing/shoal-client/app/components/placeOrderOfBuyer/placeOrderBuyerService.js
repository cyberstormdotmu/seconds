/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.placeOrderBuyer').
        factory('shoalApp_placeOrder_BuyerService', function ($q, $http, ENV) {
            var serchBuyerServiceUrl = ENV.webServiceUrl + "/admin/buyerorder/searchBuyer",
                pickBuyerServiceUrl = ENV.webServiceUrl + "/admin/buyerorder/selectBuyer",
                submitSerchBuyerRequest = function (searchRequest) {
                    var defer = $q.defer();
                    $http({
                        method: 'POST',
                        url: serchBuyerServiceUrl,
                        params: {'firstName': searchRequest.firstName, 'lastName': searchRequest.lastName, 'emailAddress': searchRequest.emailAddress}
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                },
                pickSerchBuyerRequest = function (searchRequest) {
                    var defer = $q.defer();
                    $http({
                        method: 'POST',
                        url: pickBuyerServiceUrl,
                        params: {'username': searchRequest.username }
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                };
            return {
                submitSerchBuyerRequest: submitSerchBuyerRequest,
                pickSerchBuyerRequest: pickSerchBuyerRequest
            };
        });
}());