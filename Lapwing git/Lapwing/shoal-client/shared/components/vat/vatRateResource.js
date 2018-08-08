/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.vatRate')
        .factory('shoalCommon_vatRate_VatRateResource', function ($resource, ENV) {

            var my = {};
            my.productsWebServiceUrl = ENV.webServiceUrl + "/vatrates";
            return $resource(my.productsWebServiceUrl, null,
                {
                    'fetchAllVatRates': { url: my.productsWebServiceUrl, method: 'GET', isArray: true }
                });
        });
}());
