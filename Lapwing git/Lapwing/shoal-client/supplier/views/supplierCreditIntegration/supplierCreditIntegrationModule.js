/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.supplierCreditIntegration', ['shoalCommon.address', 'ui-notification', 'ui.bootstrap'])
        .config([function ($stateProvider, NotificationProvider) {
            $stateProvider
                .state('loggedIn.supplierCreditIntegration', {
                    url: '/supplierCreditIntegration',
                    template: '<ui-view/>',
                    redirectTo: 'loggedIn.supplierCreditIntegration.list',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('loggedIn.supplierCreditIntegration.list', {
                    url: '',
                    templateUrl: 'views/supplierCreditIntegration/supplierCreditIntegrationListView.html',
                    controller: 'shoalSupplier.views.supplierCreditIntegration.supplierCreditIntegrationListController',
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
