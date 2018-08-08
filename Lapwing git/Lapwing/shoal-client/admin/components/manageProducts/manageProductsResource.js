/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.manageProducts')
        .factory('shoalAdmin_manageProducts_ProductsResource', function ($resource, ENV) {

            var my = {};
            my.adminManageProductsWebServiceUrl = ENV.webServiceUrl + "/admin/manageProducts";
            my.productsWebServiceUrl = ENV.webServiceUrl + "/vendors";
            return $resource(my.adminManageProductsWebServiceUrl, null,
                {
                    'save': {method: 'POST'},
                    'edit': {method: 'PUT'},
                    'vendorSave': { url: my.productsWebServiceUrl, method: 'POST'}
                });
        });
}());
