/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.products')
        .factory('shoalCommon_products_ProductsResource', function ($resource, ENV) {

            var my = {};
            my.productsWebServiceUrl = ENV.webServiceUrl + "/products";
            return $resource(my.productsWebServiceUrl, null,
                {
                    'findByCode': { url: my.productsWebServiceUrl + '/:code', method: 'GET' },
                    'fetchByTopCategory': { url: my.productsWebServiceUrl + '?category=:category', method: 'GET', isArray: true },
                    'fetchAllCategories': { url: my.productsWebServiceUrl + '/categories', method: 'GET', isArray: true },
                    'fetchCategory': { url: my.productsWebServiceUrl + '/categories/:category', method: 'GET' }
                });
        });
}());
