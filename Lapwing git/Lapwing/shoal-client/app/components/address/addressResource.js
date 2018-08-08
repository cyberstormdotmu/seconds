/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.address')
        .factory('shoalApp_address_AddressResource', function ($resource, ENV) {

            var my = {};
            my.addressWebServiceUrl = ENV.webServiceUrl + "/addresses";
            return $resource(my.addressWebServiceUrl, null,
                {
                    'save': {method: 'POST'},
                    'edit': {method: 'PUT'}
                });
        });
}());
