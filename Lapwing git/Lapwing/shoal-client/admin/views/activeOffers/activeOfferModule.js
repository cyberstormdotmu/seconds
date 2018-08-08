/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.activeOffers', ['shoalAdmin.offers', 'ui-notification', 'ui.bootstrap'])
        .config([function ($stateProvider, NotificationProvider) {
            $stateProvider
                .state('loggedIn.activeOffers', {
                    url: '/activeoffers/',
                    template: '<ui-view/>',
                    redirectTo: 'loggedIn.activeOffers.list',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('loggedIn.activeOffers.list', {
                    url: '',
                    templateUrl: 'views/activeOffers/activeOfferListView.html',
                    controller: 'shoalAdmin.views.activeOffers.AdminOfferListController',
                    controllerAs: 'vm'
                })
                .state('loggedIn.activeOffers.detail', {
                    url: ':reference',
                    templateUrl: 'views/activeOffers/activeOfferDetailsView.html',
                    controller: 'shoalAdmin.views.activeOffers.AdminOfferDetailsController',
                    controllerAs: 'vm'
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
