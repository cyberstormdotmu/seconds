/*global angular, jasmine, spyOn, console, SHOAL */
(function () {
    'use strict';

    angular.module('shoalCommon.SharedMocks', [])
        .mockFactory('$resource', function ($q, $resource) {
            return $resource;
        })
        .mockFactory('$http', function ($http) {
            return $http;
        })
        .mockFactory('$q', function ($q) {
            return $q;
        })
        .mockFactory('shoalCommon_classes_ProductOffer', function (shoalCommon_classes_ProductOffer) {
            return shoalCommon_classes_ProductOffer;
        })
        .mockFactory('shoalCommon_products_ProductsResource', function () {
            var productResource = jasmine.createSpyObj('shoalCommon_products_ProductsResource', ['findByCode',
                'fetchByTopCategory', 'fetchAllCategories']),
                GET = function (config, success, fail) {
                    if (!productResource.reject && success) {
                        console.log("responseData=" + JSON.stringify(productResource.responseData));
                        success(productResource.responseData);
                    }
                    if (productResource.reject && fail) {
                        fail(productResource.responseData);
                    }
                };
            productResource.reject = false;
            productResource.responseData = {};

            productResource.findByCode.and.callFake(GET);
            productResource.fetchByTopCategory.and.callFake(GET);
            return productResource;
        })
        .constant('ENV', {
            webServiceUrl: "https://domain.com/ws"
        });
}());
