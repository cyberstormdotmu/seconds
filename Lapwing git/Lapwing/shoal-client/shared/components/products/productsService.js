/*global angular */
(function () {
    'use strict';
    angular.module('shoalCommon.products')
        .factory('shoalCommon_products_ProductService', function ($q, shoalCommon_classes_ProductOffer,
                                                                  shoalCommon_products_ProductsResource) {

            var my = {};
            my.productResource = shoalCommon_products_ProductsResource;

            my.findProductByCode = function (code) {
                var defer = $q.defer();
                if (code === undefined) {
                    throw new Error('no product code supplied');
                }
                my.productResource.findByCode(
                    {
                        code: code
                    },
                    function (response) {
                        var product;

                        if (response) {
                            product = shoalCommon_classes_ProductOffer(response);
                            defer.resolve(Object.freeze(product));
                        }
                    },
                    function (response) {
                        defer.reject(response);
                    }
                );

                return defer.promise;
            };
            my.fetchProductsByTopCategory = function (categoryCode) {
                var defer = $q.defer();
                if (categoryCode === undefined) {
                    throw new Error('no product category supplied');
                }
                my.productResource.fetchByTopCategory(
                    {
                        category: categoryCode
                    },
                    function (response) {
                        if (response) {
                            defer.resolve(response);
                        }
                    },
                    function (response) {
                        defer.reject(response);
                    }
                );

                return defer.promise;
            };
            my.fetchCategory = function (categoryCode) {
                var defer = $q.defer();
                if (categoryCode === undefined) {
                    throw new Error('no product category supplied');
                }
                my.productResource.fetchCategory(
                    {
                        category: categoryCode
                    },
                    function (response) {
                        if (response) {
                            defer.resolve(response);
                        }
                    },
                    function (response) {
                        defer.reject(response);
                    }
                );

                return defer.promise;
            };

            return Object.create({}, {
                findByCode: {
                    value: my.findProductByCode
                },
                fetchByTopCategory: {
                    value: my.fetchProductsByTopCategory
                },
                fetchCategory: {
                    value: my.fetchCategory
                }
            });
        });
}());