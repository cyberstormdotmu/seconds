/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.buyers')
        .factory('shoalAdmin_buyers_buyersResource', function ($resource, ENV) {

            var buyerAdminServiceUrl = ENV.webServiceUrl + "/admin/buyers";

            return $resource(buyerAdminServiceUrl, null,
                {
                    "activateBuyer": {url: buyerAdminServiceUrl + '/:id/activateBuyer', method: 'PUT'},
                    "confirmBuyer": {url: buyerAdminServiceUrl + '/:id/confirmBuyer', method: 'PUT'},
                    "deactivateBuyer": {url: buyerAdminServiceUrl + '/:id/deactivateBuyer', method: 'PUT'},
                    "resendEmail": {url: buyerAdminServiceUrl + '/:id/resendEmail', method: 'PUT'},
                    "fetchInactiveBuyers": {method: 'GET', params: {role: 'INACTIVE'}},
                    "fetchConfirmedBuyers": {method: 'GET', params: {role: 'CONFIRM'}},
                    "fetchRejectedBuyers": {method: 'GET', params: {role: 'REJECT'}},
                    "fetchAuthenticationPendingBuyers": {method: 'GET', params: {role: 'PENDING_AUTHENTICATION'}},
                    "fetchContractSignPendingBuyers": {method: 'GET', params: {role: 'CONTRACT_SIGNING_PENDING'}}
                });
        });
}());
