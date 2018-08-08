/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.orders', ['shoalAdmin.orders', 'shoalCommon.address', 'ui-notification', 'ui.bootstrap'])
        .config([function ($stateProvider, NotificationProvider) {
            $stateProvider
                .state('loggedIn.orders', {
                    url: '/orders',
                    template: '<ui-view/>',
                    redirectTo: 'loggedIn.orders.list',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('loggedIn.orders.list', {
                    url: '',
                    templateUrl: 'views/orders/adminOrderListView.html',
                    controller: 'shoalAdmin.views.orders.AdminOrderListController',
                    controllerAs: 'vm'
                })
                .state('loggedIn.orders.detail', {
                    url: '/:reference',
                    templateUrl: 'views/orders/adminOrderDetailsView.html',
                    controller: 'shoalAdmin.views.orders.AdminOrderDetailsController',
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
