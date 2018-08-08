/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.buyers')
        .factory('shoalSupplier_buyers_buyersResource', function ($resource, ENV) {

            var buyerAdminServiceUrl = ENV.webServiceUrl + "/admin/buyers";

            return $resource(buyerAdminServiceUrl, null,
                {
                    "fetchInactiveBuyers": {method: 'GET', params: {role: 'INACTIVE'}},
                    "fetchConfirmedBuyers": {method: 'GET', params: {role: 'CONFIRM'}},
                    "fetchRejectedBuyers": {method: 'GET', params: {role: 'REJECT'}}
                });
        });
}());
