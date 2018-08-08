/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.vendors')
        .factory('shoalCommon_vendors_VendorsResource', function ($resource, ENV) {

            var my = {};
            my.productsWebServiceUrl = ENV.webServiceUrl + "/vendors";
            return $resource(my.productsWebServiceUrl, null,
                {
                    'fetchAllVendors': { url: my.productsWebServiceUrl, method: 'GET', isArray: true }
                });
        });
}());
