/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.registrations', ['ui-notification', 'ui.bootstrap', 'shoalAdmin.buyers'])
        .config([function ($stateProvider, NotificationProvider) {
            $stateProvider
                .state('loggedIn.registrations', {
                    url: '/registrations',
                    templateUrl: 'views/registrations/registrationsView.html',
                    controller: 'shoalAdmin.views.registrations.RegistrationsController',
                    controllerAs: 'vm',
                    data: {
                        access: {
                            requiresAuthorisation: true
                        }
                    },
                    resolve: {
                        newRegistrations: function (shoalAdmin_buyers_BuyerAdminService) {
                            return shoalAdmin_buyers_BuyerAdminService.that.fetchUnauthorisedBuyers();
                        }
                    }
                });
            NotificationProvider.setOptions({
                delay: 5000,
                startTop: 20,
                startRight: 10,
                verticalSpacing: 20,
                horizontalSpacing: 20,
                positionX: 'center',
                positionY: 'top',
                templateUrl: '../shared/components/ui-notification/ui-notification.html'
            });

        }]);
}());
