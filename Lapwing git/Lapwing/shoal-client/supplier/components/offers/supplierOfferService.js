/*global angular */
(function () {
    'use strict';

    angular.module('shoalSupplier.offers')
        .factory('shoalSupplier_offers_OfferService', function ($http, $q, ENV) {

            var offerAdminWebServiceUrl = ENV.webServiceUrl + "/admin/offers/",
                offerAdminPaymentWebServiceUrl = ENV.webServiceUrl + "/admin/supplierPayment/recordPayment",

                fetchOffers = function () {
                    var defer = $q.defer();

                    $http.get(offerAdminWebServiceUrl)
                        .then(function (response) {
                            var offers = response.data;
                            defer.resolve(Object.freeze(offers));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                fetchOfferReport = function (offerReference) {
                    var defer = $q.defer();

                    $http.get(offerAdminWebServiceUrl + offerReference + '/report')
                        .then(function (response) {
                            var report = response.data;
                            defer.resolve(Object.freeze(report));
                        }, function (response) {
                            defer.reject({
                                reason : response.data.message
                            });
                        });
                    return defer.promise;
                },
                recordPayment = function (paymentDetails) {
                    var defer = $q.defer();

                    $http({
                        method: 'POST',
                        url: offerAdminPaymentWebServiceUrl,
                        data: paymentDetails
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                };

            return {
                fetchOffers: fetchOffers,
                fetchOfferReport: fetchOfferReport,
                recordPayment: recordPayment
            };
        });
}());