/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.registrations', ['ui.bootstrap', 'shoalSupplier.buyers'])
        .config([function ($stateProvider) {
            $stateProvider
                .state('loggedIn.registrations', {
                    url: '/registrations',
                    templateUrl: 'views/registrations/registrationsView.html',
                    controller: 'shoalSupplier.views.registrations.RegistrationsController',
                    controllerAs: 'vm',
                    data: {
                        access: {
                            requiresAuthorisation: true
                        }
                    },
                    resolve: {
                        newRegistrations: function (shoalSupplier_buyers_BuyerAdminService) {
                            return shoalSupplier_buyers_BuyerAdminService.that.fetchUnauthorisedBuyers();
                        }
                    }
                });
        }]);
}());
