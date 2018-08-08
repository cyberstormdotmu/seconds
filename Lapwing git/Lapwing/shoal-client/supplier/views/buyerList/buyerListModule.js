/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.buyerList', ['shoalSupplier.buyers', 'ui-notification', 'ui.bootstrap'])
        .config([function ($stateProvider, NotificationProvider) {
            $stateProvider
                .state('loggedIn.buyerList', {
                    url: '/buyerList',
                    template: '<ui-view/>',
                    redirectTo: 'loggedIn.buyerList.list',
                    data: {
                        access: {
                            requiresAuthorisation: true
                        }
                    }
                })
                .state('loggedIn.buyerList.list', {
                    url: '',
                    templateUrl: 'views/buyerList/buyerListView.html',
                    controller: 'shoalSupplier.views.buyerList.BuyerListController',
                    controllerAs: 'vm'
                })
                .state('loggedIn.buyerList.detail', {
                    url: '/:id',
                    templateUrl: 'views/buyerList/buyerListDetailsView.html',
                    controller: 'shoalSupplier.views.buyerList.BuyerListDetailsController',
                    controllerAs: 'vm'
                })
                .state('loggedIn.buyerList.orders', {
                    url: '/:reference',
                    templateUrl: 'views/buyerList/buyerOrderDetailsView.html',
                    controller: 'shoalSupplier.views.buyerList.BuyerOrderDetailsController',
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
